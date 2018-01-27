package Models;

import java.util.List;

import org.lwjgl.util.vector.Vector3f;

public class RawModel {
	private List<Vector3f> verticesArray = null;
	private int vaoID = 0;
	private int vertexCount = 0;
	public RawModel(int vaoID, int vertexCount, List<Vector3f> vertices) {
		this.vaoID = vaoID;
		this.vertexCount = vertexCount;
		this.verticesArray = vertices;
	}
	public int getVaoID() {
		return vaoID;
	}
	public int getVertexCount() {
		return vertexCount;
	}
	
	public List<Vector3f> getVertices() {
		return verticesArray;
	}
}
