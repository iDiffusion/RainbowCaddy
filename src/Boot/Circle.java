/* Circle.java
 * Date Created: Dec 18, 2017
 * Author: Liam Marshall
 * */

import java.util.ArrayList;

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
		pushCircle(center, radius, points);
	}
	
	/**
	 * @return a list of points that make up the ring
	 */
	public ArrayList<Point> getCircle(){
        return ring;
    }
	
	/** 
	 * Generate points along a set number of spokes for a circle of a given radius 
	 * @param center - contains the X,Y,Z points of the center
	 * @param radius - the distance from the center point
	 * @param numSpokes - the number of spokes to draw point on
	 */
    public void makeCircle (Point center, double radius, int numSpokes){
        double deltax,deltay; 
        double degCount=0;
        double degree = (float) 360/numSpokes;
        ring = new ArrayList<Point>();
        for(degCount=0; degCount<360; degCount+=degree){
        	deltax = radius*Math.cos(Math.toRadians(degCount));
            deltay = radius*Math.sin(Math.toRadians(degCount));
            Point tempPoint = new Point(center.getX() + deltax,center.getY() + deltay, 0);
            ring.add(tempPoint);
        }
    }
    
    /**
     * @return a coefficient used when swaying the circle to generate a more intuitive heat-map
     */
    public double coeff(){
        return 1.0;
    }
    
    /**
     * 
     * @param points
     */
    public void assignHeights(ArrayList<Point> points) {    /** Assign Heights - */
        for(int i = 0; i < ring.size(); i++) {                                        /*Nearest Neighbor*/
            ArrayList<Point> temp1 = CircleGen.nearestNeighbor(ring.get(i), points, 1);
            ring.get(i).setX(temp1.get(0).getX());
            ring.get(i).setY(temp1.get(0).getY());
            ring.get(i).setZ(temp1.get(0).getZ());
        }
    }
    
    /**
     * sways the circle based on the change in elevation between the point on the spoke and the center
     * @param radius - the distance from the center point
	 * @param center - contains the X,Y,Z points of the center
	 * @param points - a list of the object files points
     */
    public void pushCircle (Point center, double radius, ArrayList<Point> points){
        int count;
        double deltaz;
        int numSpokes = ring.size();
        for(count=0; count<numSpokes; count++){
            deltaz = center.getZ() - ring.get(count).getZ();
            ring.get(count).setX(ring.get(count).getX() + deltaz * coeff() * ((ring.get(count).getX() - center.getX()) / radius));
            ring.get(count).setY(ring.get(count).getY() + deltaz * coeff() * ((ring.get(count).getY() - center.getY()) / radius));
        }
        assignHeights(points);
    }    
}
