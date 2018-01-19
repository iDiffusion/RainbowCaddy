package Boot;

import javax.vecmath.Vector3d;
/**
 * A point object holds x, y, z values as well as r, g, b values.
 * @author Tylon Lee
 */
public class Point implements Comparable<Point> {
	
	double x, y, z;
	int r, g, b;
	
	/**
	 * create point with x, y, z and black as default the color.
	 * @param x
	 * @param y
	 */
	public Point(double x, double y) {
		this.x = x;
		this.y = y;
		this.z = 0.0;
		r = 0;
		g = 0;
		b = 0;
	}
	
	/**
	 * create point with x, y, z and black as default the color.
	 * @param x
	 * @param y
	 * @param z
	 */
	public Point(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
		r = 0;
		g = 0;
		b = 0;
	}
	
	/**
	 * create new point specifying the x, y, z and r, g, b values.
	 * @param x
	 * @param y
	 * @param z
	 * @param r
	 * @param g
	 * @param b
	 */
	public Point(double x, double y, double z, int r, int g, int b) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.r = r;
		this.g = g;
		this.b = b;
	}
	
	/**
	 * set XYZ after being created.
	 * @param x
	 * @param y
	 * @param z
	 */
	public void setXYZ(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	/**
	 * Get the xyz values as a array of doubles.
	 * @return
	 */
	public double[] getXYZAsArray() {
		double[] rtn = {x, y, z};
		return rtn;
	}
	
	/**
	 * Get the xyz values as a vector array of doubles.
	 * @return Vector3d xyz
	 */
	public Vector3d getXYZ() {
		return new Vector3d(x, y, z);
	}
	
	/**
	 * set the RGB values after being created.
	 * @param r
	 * @param g
	 * @param b
	 */
	public void setRGB(int r, int g, int b) {
		this.r = r;
		this.g = g;
		this.b = b;
	}
	
	/**
	 * get the rgb values as an array of integers.
	 * @return int[] rgb
	 */
	public int[] getRGBAsArray() {
		int[] rtn = {r, g, b};
		return rtn;
	}
	
	/**
	 * get the rgb values as an vector array of doubles.
	 * @return Vector3d rgb
	 */
	public Vector3d getRGB() {
		return new Vector3d(r, g, b);
	}
	
	/**
	 * set x position
	 * @param x
	 */
	public void setX(double x) {
		this.x = x;
	}
	
	/**
	 * set y position
	 * @param y
	 */
	public void setY(double y) {
		this.y = y;
	}
	
	/**
	 * set z position
	 * @param z
	 */
	public void setZ(double z) {
		this.z = z;
	}
	
	/**
	 * get x position
	 * @return x
	 */
	public double getX() {
		return this.x;
	}
	
	/**
	 * get y position
	 * @return y
	 */
	public double getY() {
		return this.y;
	}
	
	/**
	 * get z position
	 * @return z
	 */
	public double getZ() {
		return this.z;
	}
	
	/**
	 * Creates a string to represent the point
	 * @return String containing the point's values
	 */
	public String toString() {
		String Xval = "X = " + this.x + "\n";
		String Yval = "Y = " + this.y + "\n";
		String Zval = "Z = " + this.z + "\n";
		return Xval + Yval + Zval;
	}

	/**
	 * used to sort Points in an ArrayList
	 */
	@Override
	public int compareTo(Point p) {
		return (int) ((int)p.x - (int)this.x);
	}
	
}
