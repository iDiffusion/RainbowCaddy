import javax.vecmath.Vector3f;

public class Point implements Comparable<Point> {
	public Vector3f xyz;
	public Vector3f rgb;
	public Point(double x, double y, double z, double r, double g, double b) {
		xyz = new Vector3f();
		rgb = new Vector3f();
		xyz.x = (float) x;
		xyz.y = (float) y;
		xyz.z = (float) z;
		rgb.x = (float) r;
		rgb.y = (float) g;
		rgb.z = (float) b;
	}
	
	public void setX(double x) {
		xyz.x = (float) x;
	}
	public void setY(double y) {
		xyz.y = (float) y;
	}
	public void setZ(double z) {
		xyz.z = (float) z;
	}
	
	public void setR(double r) {
		rgb.x = (float) r;
	}
	public void setG(double g) {
		rgb.y = (float) g;
	}
	public void setB(double b) {
		rgb.z = (float) b;
	}
	
	
	public float getX() {
		return xyz.x;
	}
	public float getY() {
		return xyz.y;
	}
	public float getZ() {
		return xyz.z;
	}
	
	public float getR() {
		return rgb.x;
	}
	public float getG() {
		return rgb.y;
	}
	public float getB() {
		return rgb.z;
	}

	@Override
	public int compareTo(Point p) {
		return (int)(((this.xyz.y - p.getY() ) * 2) + (this.xyz.x - p.getX())) * 100;
	}
}
