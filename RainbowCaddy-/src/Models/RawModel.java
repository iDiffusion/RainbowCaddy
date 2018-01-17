package Models;

public class RawModel {
	private int vaoID = 0;
	private int vertexCount = 0;
	public RawModel(int vaoID, int vertexCount) {
		this.vaoID = vaoID;
		this.vertexCount = vertexCount;
	}
	public int getVaoID() {
		return vaoID;
	}
	public int getVertexCount() {
		return vertexCount;
	}
}
