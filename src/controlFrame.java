import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JButton;

public class controlFrame implements Runnable {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public void run() {
		try {
			controlFrame window = new controlFrame();
			window.frame.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the application.
	 */
	public controlFrame() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		System.out.println("New Thread");
		frame = new JFrame();
		frame.setBounds(100, 100, 120, 474);
		frame.setLocation(100 + 634, 100);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JButton btnPrint = new JButton("Print");
		btnPrint.setBounds(10, 227, 100, 23);
		
		JButton btnRefresh = new JButton("Refresh");
		btnRefresh.setBounds(10, 167, 100, 23);
		btnRefresh.addActionListener(new ActionListener() {

		    @Override
		    public void actionPerformed(ActionEvent e) {
		    	JButton btn =  (JButton) e.getSource();
		        if(btn.isSelected()) {
		        	System.out.println("Refreshing");
		        	try {
						mainMenu.m = OBJLoader.loadModel(new File("res/model/model.obj"));
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
		        }
		    }
		});

		
		
		frame.getContentPane().add(btnPrint);
		frame.getContentPane().add(btnRefresh); 
		frame.addWindowListener(new WindowListener() {

			@Override
			public void windowClosing(WindowEvent arg0) {
				mainMenu.frameOpen = false;
				System.exit(0);
			}

			@Override
			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowClosed(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowDeactivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowDeiconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowIconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowOpened(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
	}
}
