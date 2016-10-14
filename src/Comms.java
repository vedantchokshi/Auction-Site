import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class Comms {
	private Socket sock;
	private OutputStream out;
	private InputStream in;
	private ConcurrentLinkedDeque<Message> messages;
	public boolean open = true;
	
	//Creates new Socket
	public Comms() {
		try {
			this.sock = new Socket("localhost", 4444);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//Sets socket
	public Comms(Socket s) {
		this.sock = s;
	}
	
	//Gets input and output stream of the socket
	public void init() {
		try {
			messages = new ConcurrentLinkedDeque<>();
			out = sock.getOutputStream();
			in = sock.getInputStream();	
		} catch (IOException e) {
			e.printStackTrace();
		}
		startMessageThread();
	}
	
	//Establishes a socket connection
	public static Comms connect() {
		Comms client = null;
		try(ServerSocket server = new ServerSocket(4444)) {
			client = new Comms(server.accept());
		} catch(Exception e) {
			e.printStackTrace();
		}
		return client;
	}
	
	//Sends message
	public void sendMessage(Message m) {
		try {
			ObjectOutputStream out = new ObjectOutputStream(this.out);
			out.writeObject(m);
			out.flush();
		} catch (SocketException e) {
			close(false);
		} catch (IOException e) {
			e.printStackTrace();
		}					
	}
	
	//Recieves message
	public void startMessageThread() {
		//Starts thread which constantly recieves messsages
		Thread x = new Thread(new Runnable() {
			@Override
			public void run() {
				while(true) {
					Message m = null;
					try {
						//Reads input stream
						ObjectInputStream read = new ObjectInputStream(in);
						m = (Message) read.readObject();
					} catch (SocketException e) {
						//Closes socket when connection ends
						close(false);
					} catch (Exception e) {
						e.printStackTrace();
					}
					if(m != null) {
						//Adds message to ConcurrentLinkedDeque
						messages.add(m);
					}
				}
			}
		});
		x.start();
	}

	//Gets the last message in the ConcurrentLinkedDeque
	public Message receiveMessage() {
		return messages.poll();
	}
	
	//Gets connection name
	public String name() {
		return sock.getRemoteSocketAddress().toString().substring(1);
	}
	
	//Closes socket
	public void close(boolean b) {
		if(open) {
			if(b) {
				this.sendMessage(new ExitMessage());
			}
			try {
				in.close();
				out.close();
				sock.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			open = false;			
		}		
	}
	
	//Checks if the connection is open or not
	public boolean isOpen() {
		return open;
	}
}
