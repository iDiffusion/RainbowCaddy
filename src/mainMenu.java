import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.Toolkit;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JLabel;

public class mainMenu {

	private JFrame frmRainbowCaddy;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					mainMenu window = new mainMenu();
					window.frmRainbowCaddy.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	/**
	 * Create the application.
	 */
	public mainMenu() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmRainbowCaddy = new JFrame();
		frmRainbowCaddy.setIconImage(Toolkit.getDefaultToolkit().getImage("D:\\Ikaika Lee\\Downloads\\772b521cb08f99b464eef729c62fa978.png"));
		frmRainbowCaddy.setTitle("Rainbow Caddy");
		frmRainbowCaddy.setBounds(100, 100, 634, 474);
		frmRainbowCaddy.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmRainbowCaddy.getContentPane().setLayout(null);
		
		JButton btnRefresh = new JButton("Refresh");
		btnRefresh.setBounds(519, 367, 89, 23);
		frmRainbowCaddy.getContentPane().add(btnRefresh);
		
		JButton btnPrint = new JButton("Print");
		btnPrint.setBounds(519, 401, 89, 23);
		frmRainbowCaddy.getContentPane().add(btnPrint);
		
		JPanel panel = new JPanel();
		panel.setBounds(519, 296, 89, 60);
		frmRainbowCaddy.getContentPane().add(panel);
		
		JLabel lblHole = new JLabel("Hole");
		panel.add(lblHole);
		
		JSpinner spinner = new JSpinner();
		panel.add(spinner);
		
		JButton btnAddobj = new JButton("Add .obj");
		panel.add(btnAddobj);
	}
}
