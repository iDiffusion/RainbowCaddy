import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_FILL;
import static org.lwjgl.opengl.GL11.GL_FRONT_AND_BACK;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glColor3d;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glGenLists;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glNormal3f;
import static org.lwjgl.opengl.GL11.glPolygonMode;
import static org.lwjgl.opengl.GL11.glVertex3f;

import java.awt.Canvas;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JFrame;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;


public class mainMenu {

	public JFrame frmRainbowCaddy;
	public static mainMenu window;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		new Thread(new controlFrame()).start();
				try {
					mainMenu window = new mainMenu();
					mainMenu.window = window;
					window.frmRainbowCaddy.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
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
	public static boolean frameOpen = true;
	private void initialize() {
		frmRainbowCaddy = new JFrame();
		frmRainbowCaddy.setBounds(100, 100, 634, 474);
		frmRainbowCaddy.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmRainbowCaddy.getContentPane().setLayout(null);
		
		
		Canvas canvas = new Canvas();
		canvas.setSize(frmRainbowCaddy.getWidth(), frmRainbowCaddy.getHeight());
		
		
		frmRainbowCaddy.getContentPane().add(canvas);
		frmRainbowCaddy.setUndecorated(true);
		frmRainbowCaddy.setVisible(true);
		
		try {
			Display.setParent(canvas);
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
			 System.err.println("The display wasn't initialized correctly. :(");
	         Display.destroy();
	         System.exit(1);
		}
		
		setUpDisplayLists();
        setUpCamera();

       while (frameOpen) {
            render();
            checkInput();
            Display.update();
            Display.sync(60);
        }
        cleanUp();
	}
    private static Camera camera;
    public static Model m = null;

	private static void setUpDisplayLists() {
	        glGenLists(1);
	        GL11.glShadeModel(GL11.GL_SMOOTH);
	        try {
	            m = OBJLoader.loadModel(new File("res/model/model.obj"));
	        } catch (FileNotFoundException e) {
	            e.printStackTrace();
	            Display.destroy();
	            System.exit(1);
	        } catch (IOException e) {
	            e.printStackTrace();
	            Display.destroy();
	            System.exit(1);
	        }
	    }
	
	private static void checkInput() {
        //camera.processMouse(1, 80, -80);
        camera.processKeyboard(16, 10, 10, 10);
        if (Mouse.isButtonDown(0)) {
            Mouse.setGrabbed(true);
        } else if (Mouse.isButtonDown(1)) {
            Mouse.setGrabbed(false);
        }
    }

    private static void cleanUp() {
        try {
        	Display.destroy();
        } catch (Exception e) {
        	
        }
        ArrayList<Point> array = new ArrayList<Point>();
        Collections.sort(array);
    }

    private static void render() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glLoadIdentity();
        camera.applyTranslations();
        glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
        
        glBegin(GL_TRIANGLES);
        for (Model.Face face : m.getFaces()) {
        	glColor3d(0,0,1); //green
            Vector3f n1 = m.getNormals().get(face.getNormalIndices()[0] - 1);
            glNormal3f(n1.x, n1.y, n1.z);
            Vector3f v1 = m.getVertices().get(face.getVertexIndices()[0] - 1);
            glVertex3f(v1.x, v1.y, v1.z);

        	glColor3d(0,1,0); //green
            Vector3f n2 = m.getNormals().get(face.getNormalIndices()[1] - 1);
            glNormal3f(n2.x, n2.y, n2.z);
            Vector3f v2 = m.getVertices().get(face.getVertexIndices()[1] - 1);
            glVertex3f(v2.x, v2.y, v2.z);

        	glColor3d(1,0,0); //green
            Vector3f n3 = m.getNormals().get(face.getNormalIndices()[2] - 1);
            glNormal3f(n3.x, n3.y, n3.z);
            Vector3f v3 = m.getVertices().get(face.getVertexIndices()[2] - 1);
            glVertex3f(v3.x, v3.y, v3.z);
        }
        glEnd();
    }

    private static void setUpCamera() {
    	//----------- SIDE - ABOVE -----------
//        camera = new EulerCamera.Builder().setAspectRatio((float) Display.getWidth() / Display.getHeight())
//                .setRotation(40f, 65f, 0f).setPosition(-30f, 30f, 30f).setFieldOfView(60).build();
        //----------- AREIAL -----------------
        camera = new EulerCamera.Builder().setAspectRatio((float) Display.getWidth() / Display.getHeight())
                .setRotation(90f, 61f, 0f).setPosition(5f, 40f, 15f).setFieldOfView(60).build();
        camera.applyOptimalStates();
        camera.applyPerspectiveMatrix();
    }
}
