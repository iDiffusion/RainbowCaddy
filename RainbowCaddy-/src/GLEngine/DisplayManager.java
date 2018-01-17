package GLEngine;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;

/**
 * Displays Boot.mesh.points Objects
 * 
 * @author Keegan Bruer <br>
 *
 */
public class DisplayManager {
	private static int WIDTH = 640;
	private static int HEIGHT = 420;
	private static final int FPS_MAX = 120;
	private static int x = 0;
	private static int y = 0;
	public static void createDisplay() {
		createDisplay(WIDTH, HEIGHT);
	}
	public static void createDisplay(int width, int height) {
		WIDTH = width;
		HEIGHT = height;
		ContextAttribs attribs = new ContextAttribs(3,2)
		.withForwardCompatible(true)
		.withProfileCore(true);
		
		try {
			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
			Display.create(new PixelFormat(), attribs);
			x = Display.getX();
			y = Display.getY();
			Display.setTitle("Rainbow Caddy");
			
			
			//---------Set Display Icon---------
			
			
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		
		GL11.glViewport(0, 0, WIDTH, HEIGHT);
	}

	public static void updateDisplay() {
		Display.sync(FPS_MAX);
		Display.update();
	}
	
	public static void closeDisplay() {
		Display.destroy();
	}

	public static int getWidth() {
		return WIDTH;
	}

	public static int getHeight() {
		return HEIGHT;
	}
	
	
	public static int getX() {
		x = Display.getX();
		return x;
	}

	public static int getY() {
		y = Display.getY();
		return y;
	}
	

}
