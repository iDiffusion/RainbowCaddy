package Boot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import Entities.Camera;
import Entities.Entity;
import GLEngine.DisplayManager;
import GLEngine.Loader;
import GLEngine.OBJLoader;
import GLEngine.Renderer;
import Models.RawModel;
import Models.TexturedModel;
import Shaders.StaticShader;
import Textures.ModelTexture;

public class Boot {
	public static Mesh mesh;
	private static JFrame frame = new JFrame("Control Panel");

	public static void main(String[] args) {
		logErrorsToFile("Errors.log");
		DisplayManager.createDisplay(1000, 700);
		initalize();
		//-------------POPUP TELLING USER MESH IS LOADING------------
		Error_Popup firstPop = new Error_Popup("LOADING", "Please wait as the mesh is loaded", 0, 0, false);
		firstPop.setLocation(DisplayManager.getX()-(firstPop.getWidth()/2)+(DisplayManager.getWidth()/2), DisplayManager.getY()- (firstPop.getHeight()/2)+(DisplayManager.getHeight()/2));

		//--------LOADING SHADERS, TEXTURES and MESHES------------
		Loader loader = new Loader();
		StaticShader shader = new StaticShader();
		Renderer renderer = new Renderer(shader);
		Mesh mesh = new Mesh(new File("res/model/model.obj"));
		mesh.setColor(0, 0, 255);
		OBJLoader objLoader = new OBJLoader("model", loader, mesh);
		
		
		RawModel model = objLoader.getModel();
		TexturedModel staticModel = new TexturedModel(model, new ModelTexture(loader.loadTexture("model")));
		Entity entity = new Entity(staticModel, new Vector3f(0, -20, -50), 0, 0, 0, 1);

		//---------LOADING CAMERA---------
		Camera camera = new Camera();
		
		//-------REMOVE LOADING MESSAGE-------------
		firstPop.dispose();
		
		//----------MAIN LOOP----------------
		while(!Display.isCloseRequested()) {
			camera.move(); //Allow Camera Movement
			//------------LOAD COLORS ON MESH---------
			if(camera.changeColor()) {
				//----CONSOLE FOR DEBUGGING----------
				System.out.println("Loading New Color");
				//-----MESSAGE FOR USER (LOADING COLORS)-------------
				Error_Popup pop = new Error_Popup("LOADING", "Please wait as the new color is applied to the mesh.", 0, 0, false);
				pop.setLocation(DisplayManager.getX()-(pop.getWidth()/2)+(DisplayManager.getWidth()/2), DisplayManager.getY()- (pop.getHeight()/2)+(DisplayManager.getHeight()/2));
				
				//---------CHANGE THE COLORS ON THE MESH-----------
				mesh.setColor(0, 255, 0); //Set Color of mesh
				//TODO Generate circles and load to Mesh object
				
				//--------END CHANGE COLORS---------------
				//---------RELOAD DISPLAY WITH NEW COLORS
				objLoader = new OBJLoader("model", loader, mesh);
				model = objLoader.getModel();
				staticModel = new TexturedModel(model, new ModelTexture(loader.loadTexture("model")));
				entity = new Entity(staticModel, new Vector3f(0, -20, -50), 0, 0, 0, 1);
				//-----------DISPLOSE OF USER MESSAGE------------
				pop.dispose();
				//----CONSOLE FOR DEBUGGING----------
				System.out.println("New Color Loaded.");
			}
			//-------RENDERS MESH WITH SHADERS AND TEXTURES---------
			renderer.prepare();
			shader.start();
			shader.loadViewMatrix(camera);
			renderer.render(entity, shader);
			shader.stop();
			//-------UPDATES DISPLAY WITH NEW STUFF(above)---------
			DisplayManager.updateDisplay();
			//------KEEP CONTROL PANEL NEXT TO DISPLAY---------------
			frame.setLocation(DisplayManager.getX() + DisplayManager.getWidth(), DisplayManager.getY());
		}
		//----DISPOSE AND CLOSE DISPLAY AND REMOVE OBJECTS FROM MEMORY-------
		frame.dispose(); //close control panel
		shader.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();//close display
		
	}
	
	/**
	 * Initalize the control panel
	 */
	public static void initalize() {
		//-------SETUP AND SET LOCATION----------
		frame.setSize(120, DisplayManager.getHeight()+40);
		frame.setLocation(DisplayManager.getX() + DisplayManager.getWidth(), DisplayManager.getY());
		frame.getContentPane().setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setUndecorated(true);
        int lastX = 0;
        //---------------ADD BUTTONS TO FRAME----------------
		JButton refreshBtn = new JButton("Refresh");
		refreshBtn.setBounds(10, frame.getHeight()/2-81, 100, 23);
		refreshBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
			}
		});
		frame.getContentPane().add(refreshBtn);
		
		JButton printBtn = new JButton("Print");
		printBtn.setBounds(10, refreshBtn.getY()+35, 100, 23);
		printBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				//display.saveImage = true;
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
