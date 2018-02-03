package ToolBox;

import org.lwjgl.util.vector.Vector3f;

public class Ray {

	public Vector3f Position = null;
	public Vector3f Direction = null;

	public Ray(Vector3f rayPosition, Vector3f rayDirection) {
		Position = rayPosition;
		Direction = rayDirection;
	}

}
