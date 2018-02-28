package GLEngine;
import java.io.IOException;
import java.nio.ByteBuffer;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

/**
 * Displays Boot.mesh.points Objects
 * 
 * @author Keegan Bruer <br>
 *
 */
public class DisplayManager {
	private static int WIDTH = 640;
	private static int HEIGHT = 420;
	private static int FPS_MAX = 120;
	private static int x = 0;
	private static int y = 0;

	public static void createDisplay() {

		ContextAttribs attribs = new ContextAttribs(3,2)
		.withForwardCompatible(true)
		.withProfileCore(true);
		
		try {
			int biggestWidth = 0;
			int biggestHeight = 0;
			int fastest = 0;
			DisplayMode mode = new DisplayMode(0,0);
			DisplayMode[] modes = Display.getAvailableDisplayModes();
			for(DisplayMode d: modes) {
				if(d.getHeight()>biggestHeight) {biggestHeight =  d.getHeight();}
				if(d.getWidth()>biggestWidth) {biggestWidth = d.getWidth();}
				if(d.getFrequency()>fastest) {fastest =  d.getFrequency();}
				if(d.getFrequency()==fastest&&d.getWidth()==biggestWidth&&d.getHeight()==biggestHeight) {
					mode = d;
				}
			}
			HEIGHT = mode.getHeight();
			WIDTH = mode.getWidth()-118;
			FPS_MAX = mode.getFrequency();
			Display.setDisplayMode(new DisplayMode(WIDTH,HEIGHT));
			Display.create(new PixelFormat(), attribs);
			x = Display.getX()-61;
			y = Display.getY();
			Display.setLocation(x, y);
			Display.setTitle("Rainbow Caddy");
			
			
			//---------Set Display Icon---------
			ByteBuffer icon16, icon32;
			icon16 = icon32 = null;
			try {
				icon16 = ByteBuffer.wrap(TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/logo.png")).getTextureData());
				icon32 = ByteBuffer.wrap(TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/logo.png")).getTextureData());
			} catch (IOException e) {
				e.printStackTrace();
			}
			Display.setIcon(new ByteBuffer[]{icon16, icon32});
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
		WIDTH = Display.getWidth();
		return WIDTH;
	}

	public static int getHeight() {
		HEIGHT = Display.getHeight();
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
