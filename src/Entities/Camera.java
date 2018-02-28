package Entities;


import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

import GLEngine.DisplayManager;

public class Camera {
	private Vector3f position = new Vector3f(0,0,0);
	private float pitch;
	private float yaw;
	private static float roll;
	
	public Camera() {
		try {
			Mouse.create();
		} catch (LWJGLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void destroy() {
		Mouse.destroy();
	}

	public void move() {
		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
			position.y += 0.02f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			position.x += 0.02f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			position.x -= 0.02f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			position.y -= 0.02f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_R)) {
			roll -= .1f;
		}
		if (Mouse.isButtonDown(2)) {
			Mouse.setGrabbed(true);
			float yaw =   (Mouse.getX() - (DisplayManager.getWidth() /2))/10;
			float pitch = (Mouse.getY() - (DisplayManager.getHeight()/2))/10;
			//Mouse.setCursorPosition(DisplayManager.getWidth()/2, DisplayManager.getHeight()/2);
			this.yaw = yaw;
			this.pitch = -pitch;
		} else {
			position.z -= Mouse.getDWheel()/10;
			Mouse.setGrabbed(false);
		}
		
	}
	
	public Vector3f getPosition() {
		return position;
	}

	public float getPitch() {
		return pitch;
	}

	public float getYaw() {
		return yaw;
	}

	public float getRoll() {
		return roll;
	}
}
