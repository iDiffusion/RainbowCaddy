package Boot;

import java.util.ArrayList;

/**
 * Date Created: Dec 18, 2017
 * @author Liam Marshall 
 * @author Tylon Lee
 */

public class Circle {
	/**
	 * Holds all the points in a ring
	 */
	public ArrayList<Point> ring;
	
	/**
	 * Use to initialize rings around the center point
	 * @param radius - the distance from the center point
	 * @param center - contains the X,Y,Z points of the center
	 * @param numSpokes - the number of spokes to draw points on
	 * @param points - a list of the object files points
	 */
	public Circle(double radius, Point center, int numSpokes, ArrayList<Point> points) {
		makeCircle(center,radius,numSpokes);
		assignHeights(points);
		pushCircle(center, radius);
		assignHeights(points);
	}
	
	/**TESTED
	 * @return a list of points that make up the ring
	 */
	public ArrayList<Point> getCircle(){
        return ring;
    }
	
	/** TESTED
	 * Generate points along a set number of spokes for a circle of a given radius 
	 * @param center - contains the X,Y,Z points of the center
	 * @param radius - the distance from the center point
	 * @param numSpokes - the number of spokes to draw point on
	 */
    private void makeCircle (Point center, double radius, int numSpokes){
        double deltax,deltay; 
        double degree = (double) 360/numSpokes;
        ring = new ArrayList<Point>();
        for(double degCount = 0; degCount < 360; degCount += degree){
        	deltax = radius * Math.cos(Math.toRadians(degCount));
            deltay = radius * Math.sin(Math.toRadians(degCount));
            Point tempPoint = new Point(center.getX() + deltax,center.getY() + deltay);
            ring.add(tempPoint);
        }
    }
    
    /**TESTED
     * @return a coefficient used when swaying the circle to generate a more intuitive heat-map
     */
    private double coeff(){
        return 1.0;
    }
    
    /**
     * Snaps created points to existing points
     * @param points
     */
    private void assignHeights(ArrayList<Point> points) {
    	Point temp;
        for(Point p : ring) {
            temp = CircleGen.nearestNeighbor(p, points);
            p.setXYZ(temp.getX(), temp.getY(), temp.getZ());
            temp = null;
        }
    }
    
    /**TESTED
     * sways the circle based on the change in elevation between the point on the spoke and the center
     * @param radius - the distance from the center point
	 * @param center - contains the X,Y,Z points of the center
     */
    private void pushCircle (Point center, double radius){
        double deltaz;
        int numSpokes = ring.size();
        for(int count = 0; count < numSpokes; count++){
            deltaz = center.getZ() - ring.get(count).getZ();
            ring.get(count).setX(ring.get(count).getX() + deltaz * coeff() * ((ring.get(count).getX() - center.getX()) / radius));
            ring.get(count).setY(ring.get(count).getY() + deltaz * coeff() * ((ring.get(count).getY() - center.getY()) / radius));
        }
    }    
}
