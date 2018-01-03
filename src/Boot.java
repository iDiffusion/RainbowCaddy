import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;

import com.jogamp.opengl.GL2;

public class Boot {
	public static Mesh mesh;
	public static void main(String[] args) {
		logErrorsToFile("Errors.log");
		mesh = new Mesh(new File("res/model/model.obj"));
		mesh.setColor(255, 255, 255);
		mesh.centerMesh();
		initalize(new DisplayRunner() {
			@Override
			public void run(GL2 gl) {
				mesh.DisplayMesh(gl);
			}
		});
		
	}
	

	public static void initalize(DisplayRunner runner) {
		Display display = new Display(640, 420, 100, 100);
		
		
		display.setDisplayMethod(runner);
		
		
		JFrame frame = new JFrame("Control Panel");
		frame.setSize(100, display.height);
		frame.setLocation(display.frameX + display.width, display.frameY);
		frame.addComponentListener(new ComponentListener() {
			@Override
			public void componentHidden(ComponentEvent arg0) {}
			@Override
			public void componentMoved(ComponentEvent arg0) {
				display.setLocation(frame.getX() - display.getWidth(), frame.getY());
			}
			@Override
			public void componentResized(ComponentEvent arg0) {}
			@Override
			public void componentShown(ComponentEvent arg0) {}	
		});
		frame.getContentPane().setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
		JButton refreshBtn = new JButton("Refresh");
		refreshBtn.setBounds(10, 141, 100, 23);
		refreshBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Boot.mesh.setColor(255, 255, 255);
			}
		});
		frame.getContentPane().add(refreshBtn);
		
		JButton printBtn = new JButton("Print");
		printBtn.setBounds(10, 175, 100, 23);
		printBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				display.saveImage = true;
			}
		});
		frame.getContentPane().add(printBtn);
		
		frame.setVisible(true);
	}
	
	private static void logErrorsToFile(String string) {
		try {
			PrintStream pst = new PrintStream(string);
			System.setErr(pst);
		} catch (Exception e) {
			
		}
	}
	
	
	public static void passArrayList(ArrayList<Point> from, ArrayList<Point> to) {
		for (Point p : from) {
			to.add(p);
		}
	}
	
	public static double map(double x, double in_min, double in_max, double out_min, double out_max) {
		return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
	}
}
