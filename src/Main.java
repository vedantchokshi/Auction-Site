import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;

/*
 * Creates the client GUI and runs the client
 */
public class Main {
	
	//Creates JFrame and starts up the client
	public static void main(String[] args) {	
		SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
            	//Sets theme
                try {
                	javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
                } catch (UnsupportedLookAndFeelException e) {
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                	e.printStackTrace();
                }
                JFrame frame = new JFrame("Auction Site");
            	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            	Client client = new Client(frame);
                frame.add(client.getGUI());
                
                frame.addWindowListener(new WindowAdapter() {
					@Override
					public void windowClosing(WindowEvent e) {
						client.exit();
					}
				});
                
                frame.setSize(1000, 700);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
	}
}
