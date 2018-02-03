package Boot;

import javax.vecmath.Vector3d;

/**
 * A point object holds x, y, z values as well as r, g, b values.
 * @author Keegan Bruer
 * @author Tylon Lee
 */

public class Point implements Comparable<Point> {
	
	public double x, y, z;
	public int r, g, b;
	
	/**
	 * create point with x, y, z and set the color.
	 * @param p
	 */
	public Point(Point p) {
		this.x = p.getX();
		this.y = p.getY();
		this.z = p.getZ();
		this.r = p.getR();
		this.g = p.getG();
		this.b = p.getB();
	}
	
	/**
	 * create point with x, y, z and black as default the color.
	 * @param x
	 * @param y
	 */
	public Point(double x, double y) {
		this.x = x;
		this.y = y;
		this.z = 0.0;
		this.r = 0;
		this.g = 0;
		this.b = 0;
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
		this.r = 0;
		this.g = 0;
		this.b = 0;
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
	 * Get the xyz values as a array of doubles.
	 * @return double[] xyz
	 */
	public double[] getXYZAsArray() {
		double[] rtn = {x, y, z};
		return rtn;
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
	 * Get the xyz values as a vector array of doubles.
	 * @return Vector3d xyz
	 */
	public Vector3d getXYZ() {
		return new Vector3d(x, y, z);
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
	 * set r value
	 * @param r
	 */
	public void setR(int r) {
		this.r = r;
	}
	
	/**
	 * set g value
	 * @param g
	 */
	public void setG(int g) {
		this.g = g;
	}
	
	/**
	 * set b value
	 * @param b
	 */
	public void setB(int b) {
		this.b = b;
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
	double getY() {
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
	 * get r value
	 * @return r
	 */
	public int getR() {
		return this.r;
	}
	
	/**
	 * get g value
	 * @return g
	 */
	public int getG() {
		return this.g;
	}
	
	/**
	 * get b value
	 * @return b
	 */
	public int getB() {
		return this.b;
	}
	
	/**
	 * Creates a string to represent the point
	 * @return String containing the point's values
	 */
	@Override
	public String toString() {
		String thePoint = "Point[" + getClass().hashCode() + "]:";
		String xValue = this.x + "X";
		String yValue = this.y + "Y";
		String zValue = this.z + "Z";
		return (thePoint + "   " + xValue + "    " + yValue + "   " + zValue + "\n");
	}

	/**
	 * used to sort Points in an ArrayList
	 */
	@Override
	public int compareTo(Point p) {
		return (int) ((int)p.x - (int)this.x);
	}
	
}
