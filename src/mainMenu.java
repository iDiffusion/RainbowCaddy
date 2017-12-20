import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_COMPILE;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_FILL;
import static org.lwjgl.opengl.GL11.GL_FRONT_AND_BACK;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glCallList;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glColor3d;
import static org.lwjgl.opengl.GL11.glDeleteLists;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glEndList;
import static org.lwjgl.opengl.GL11.glGenLists;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glNewList;
import static org.lwjgl.opengl.GL11.glNormal3f;
import static org.lwjgl.opengl.GL11.glPolygonMode;
import static org.lwjgl.opengl.GL11.glVertex3f;

import java.awt.Canvas;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
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
		frmRainbowCaddy.setIconImage(Toolkit.getDefaultToolkit().getImage("D:\\Ikaika Lee\\Downloads\\772b521cb08f99b464eef729c62fa978.png"));
		frmRainbowCaddy.setTitle("Rainbow Caddy");
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
    private static int bunnyDisplayList;
    private static Camera camera;
    public static Model m = null;

	private static void setUpDisplayLists() {
	        bunnyDisplayList = glGenLists(1);
	        glNewList(bunnyDisplayList, GL_COMPILE);
	        {
	        
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
	            glColor3d(0,1,0); //green
	            glBegin(GL_TRIANGLES);
	            for (Model.Face face : m.getFaces()) {
	                Vector3f n1 = m.getNormals().get(face.getNormalIndices()[0] - 1);
	                glNormal3f(n1.x, n1.y, n1.z);
	                Vector3f v1 = m.getVertices().get(face.getVertexIndices()[0] - 1);
	                glVertex3f(v1.x, v1.y, v1.z);
	                Vector3f n2 = m.getNormals().get(face.getNormalIndices()[1] - 1);
	                glNormal3f(n2.x, n2.y, n2.z);
	                Vector3f v2 = m.getVertices().get(face.getVertexIndices()[1] - 1);
	                glVertex3f(v2.x, v2.y, v2.z);
	                Vector3f n3 = m.getNormals().get(face.getNormalIndices()[2] - 1);
	                glNormal3f(n3.x, n3.y, n3.z);
	                Vector3f v3 = m.getVertices().get(face.getVertexIndices()[2] - 1);
	                glVertex3f(v3.x, v3.y, v3.z);
	            }
	            glEnd();
	        }
	        glEndList();
	    }
	
	private static void checkInput() {
        camera.processMouse(1, 80, -80);
        camera.processKeyboard(16, 1, 1, 1);
        if (Mouse.isButtonDown(0)) {
            Mouse.setGrabbed(true);
        } else if (Mouse.isButtonDown(1)) {
            Mouse.setGrabbed(false);
        }
    }

    private static void cleanUp() {
        glDeleteLists(bunnyDisplayList, 1);
        Display.destroy();
    }

    private static void render() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glLoadIdentity();
        camera.applyTranslations();
        glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
        glCallList(bunnyDisplayList);
    }

    private static void setUpCamera() {
        camera = new EulerCamera.Builder().setAspectRatio((float) Display.getWidth() / Display.getHeight())
                .setRotation(40f, 65f, 0f).setPosition(-30f, 30f, 30f).setFieldOfView(60).build();
        camera.applyOptimalStates();
        camera.applyPerspectiveMatrix();
    }
}
