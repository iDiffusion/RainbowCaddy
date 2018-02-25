package Boot;
import java.util.ArrayList;
import java.awt.Color;

/**
 * Date Created: Feb 22, 2018
 * @author Eli Rhyne
 */

public class Circle {
	/**
	 * Holds all the points in a ring
	 */
	private ArrayList<Point> ring;
	private double radius;
	private int count;
	private Point center;
	private Color color;
	
	public Circle() {
		this.ring = new ArrayList<Point>();
		this.radius = 0.0;
		this.count = 0;
		this.center = new Point(0,0);
		this.setColor(null);
	}
	public Circle(Point center , double radius, int count, Color c) {
		this.ring = new ArrayList<Point>();
		this.radius = radius;
		this.count = count;
		this.center = center;
		this.color = c;
	}
	/**
	 * @return a list of points that make up the ring
	 */
	public ArrayList<Point> getRing(){
        return ring;
    }
	
	/**
	 * @param a point to be added
	 */
	public void add(Point point){
        ring.add(point);
    }
	
	/**
	 * @param a point to be gotten
	 */
	public Point get(int i){
        return ring.get(i);
    }
	
	/**
	 * @param i - index
	 * @param p - a point to be changed
	 */
	public void setPoint(int i, Point p){
        ring.get(i).setXYZ(p.getX(), p.getY(), p.getZ());
    }
	
	/**
	 * @param set Radius
	 */
	public void setRad(double rad) {
		radius = rad;
	}
	
	/**
	 * @return radius
	 */
	public double getRad() {
		return radius;
	}
	
	/**
	 * @param sets circle count
	 */
	public void setCount(int circleCount) {
		count = circleCount;
	}
	
	/**
	 * @return Returns the Circle count
	 */
	public int getCount() {
		return count;
	}
	
	/**
	 * @param sets circle center
	 */
	public void setCenter(Point p) {
		center = p;
	}
	
	/**
	 * @return Returns the Circle center
	 */
	public Point getCenter() {
		return center;
	}
	public Color getColor() {
		return color;
	}
	public void setColor(Color color) {
		this.color = color;
	}
}