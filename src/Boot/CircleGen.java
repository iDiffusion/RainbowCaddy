package Boot;

import java.awt.Color;
import java.util.ArrayList;
import javax.swing.JColorChooser;
import java.util.concurrent.locks.*;
import javax.vecmath.Vector3d;

/**
 * Date Created: Dec 18, 2017
 * @author Liam Marshall 
 * @author Eli Rhyne
 */

public class CircleGen implements Runnable{

	public ArrayList<Point> Bounds;
	public static ArrayList<Color> colors = new ArrayList<Color>();
	ReentrantLock lock = new ReentrantLock();

	/**
	 * @author earhy
	 * 
	 * @return an array of circles
	 * @param radius - the distance from the center point
	 * @param center - contains the X,Y,Z points of the center
	 * @param numSpokes - the number of spokes to draw points on
	 * @param points - a list of the object files points
	 * @param prev - Previous Circle made
	 * @param bounds - A two point array containing the largest and smallest points
	 * 
	 */
	public Circle[] createCircles(double radius, Point center, int numSpokes, ArrayList<Point> points, ArrayList<Point> bounds, Circle prev) {
		Circle[] circles = null;
		if(colors.isEmpty()) addColors();
		Bounds = bounds;
		
		return circles;
	}
	
	
	/**
	 * @author earhy
	 * 
	 * Prompts the user to add colors
	 */
	private void addColors() {
		Color color = Color.LIGHT_GRAY;
		for(int i =0; i<4; i++) {
			colors.add(JColorChooser.showDialog( null,"Choose a color", color ));
		}
		
	}


	/**
	 * 
	 * @author earhy
	 * Stores the point within bounds of the ArrayList
	 * @param point - Point to be stored 
	 * @param circle - Pointer to the circle being worked with
	 */
	private void storePoint(Point point, Circle circle) {
		if(((point.getX()<=Bounds.get(0).getX())&&(point.getX()>=Bounds.get(1).getX()))&&((point.getY()<=Bounds.get(0).getY())&&(point.getY()>=Bounds.get(1).getY()))){
			circle.add(point);
		}
		else if((point.getX()>=Bounds.get(0).getX())){ // Above Upper X
			if((point.getY()>Bounds.get(0).getY())){//Above Upper Y and Above X
				point.setX(Bounds.get(0).getX());
				point.setY(Bounds.get(0).getY());
				circle.add(point);
			}
			else if((point.getY()<Bounds.get(1).getY())){//Below Lower Y and above X
				point.setX(Bounds.get(0).getX());
				point.setY(Bounds.get(1).getY());
				circle.add(point);
			}
			else {//Just X condition
				point.setX(Bounds.get(0).getX());
				circle.add(point);
			}
		}
		else if((point.getX()<=Bounds.get(1).getX())){ // Below Lower X
			if((point.getY()>=Bounds.get(0).getY())){//Above Upper Y and below X
				point.setX(Bounds.get(1).getX());
				point.setY(Bounds.get(0).getY());
				circle.add(point);
			}
			else if((point.getY()<=Bounds.get(1).getY())){//Below Lower Y and X
				point.setX(Bounds.get(1).getX());
				point.setY(Bounds.get(1).getY());
				circle.add(point);
			}
			else {//Just X condition
				point.setX(Bounds.get(1).getX());
				circle.add(point);
			}
		}
		else { // X is fine
			if((point.getY()>=Bounds.get(0).getY())){ //Above Upper Y 
				point.setY(Bounds.get(0).getY());
				circle.add(point);
			}
			else if((point.getY()<=Bounds.get(1).getY())){//Below Lower Y
				point.setY(Bounds.get(1).getY());
				circle.add(point);
			}
		}
	}
	
	/**
	 * Generate points along a set number of spokes for a circle of a given radius 
	 * @param prev - Previous circle
	 * @param radius - the distance from the center point
	 * @param numSpokes - the number of spokes to draw point on
	 * @param count - the spoke being worked on
	 * @param circle - The Circle being made
	 */
	public void makePoint(Point prev, int numSpokes, double radius, int count, Circle circle){
		double radian =  count*(2.0 * Math.PI)/(double)numSpokes;
		double deltax,deltay;
		deltax = radius * Math.cos(radian);
		deltay = radius * Math.sin(radian);
		Point t = new Point(prev.getX() + deltax, prev.getY() + deltay);
		storePoint(t,circle);
		t = null;
		return;
    }
	
    /**
     * @return a coefficient used when swaying the circle to generate a more intuitive heat-map
     */
    private double coeff(){
        return 8;
    }
    
    /**
     * Snaps created points to existing points
     * @param points
     */
    private void assignHeights(ArrayList<Point> points, Circle circle) {
        for(Point p : circle.getRing()) {
        	p.setZ(CircleColor.nearestNeighbor(p, points).getZ());
        }
    }
    
    /**
     * sways the circle based on the change in elevation between the point on the spoke and the center
     * @param radius - the distance from the center point
	 * @param center - contains the X,Y,Z points of the center
     */
    private void pushCircle (Point center, double radius, Circle c, int count, int numSpokes){
        double deltaz;
        Point temp = new Point(0,0,0);
        deltaz = center.getZ() - c.get(count).getZ();
        temp.setXYZ((c.get(count).getX() + deltaz * coeff() * ((c.get(count).getX() - center.getX()) / radius)),
        			(c.get(count).getY() + deltaz * coeff() * ((c.get(count).getY() - center.getY()) / radius)),0);
        if(!(( Bounds.get(1).getX() == c.get(count).getX()) ||
        		( Bounds.get(1).getX() == c.get(count).getX() ) ||
        		( Bounds.get(0).getY() == c.get(count).getY() ) ||
        		( Bounds.get(0).getY() == c.get(count).getY() ))) {
        	c.setPoint(count , temp);
        }
    } 


	@Override
	public void run() {
		lock.lock();

		
		lock.unlock();
	}    
}