package Boot;
import java.util.ArrayList;

/**
 * Date Created: Dec 18, 2017
 * @author Liam Marshall 
 * @author Eli Rhyne
 */

public class Circle {
	/**
	 * Holds all the points in a ring
	 */
	public ArrayList<Point> ring;
	
	public ArrayList<Point> Bounds;
	
	/**
	 * Use to initialize rings around the center point
	 * @param radius - the distance from the center point
	 * @param center - contains the X,Y,Z points of the center
	 * @param numSpokes - the number of spokes to draw points on
	 * @param points - a list of the object files points
	 * @param prev - Previous Circle made
	 */
	public Circle(double radius, Point center, int numSpokes, ArrayList<Point> points, ArrayList<Point> bounds, Circle prev) {
		Bounds = bounds;
		makeCircle(prev,center,numSpokes);
		assignHeights(points);
		pushCircle(center, radius);
		assignHeights(points);
	}
	
	/**
	 * @return a list of points that make up the ring
	 */
	public ArrayList<Point> getRing(){
        return ring;
    }
	
	/**
	 * Stores the point within bounds of the ArrayList
	 * @param point - Point to be stored 
	 */
	private void storePoint(Point point) {
		if(((point.getX()<=Bounds.get(0).getX())&&(point.getX()>=Bounds.get(1).getX()))&&((point.getY()<=Bounds.get(0).getY())&&(point.getY()>=Bounds.get(1).getY()))){
			ring.add(point);
		}
		else if((point.getX()>=Bounds.get(0).getX())){ // Above Upper X
			if((point.getY()>Bounds.get(0).getY())){//Above Upper Y and Above X
				point.setX(Bounds.get(0).getX());
				point.setY(Bounds.get(0).getY());
				ring.add(point);
			}
			else if((point.getY()<Bounds.get(1).getY())){//Below Lower Y and above X
				point.setX(Bounds.get(0).getX());
				point.setY(Bounds.get(1).getY());
				ring.add(point);
			}
			else {//Just X condition
				point.setX(Bounds.get(0).getX());
				ring.add(point);
			}
		}
		else if((point.getX()<=Bounds.get(1).getX())){ // Below Lower X
			if((point.getY()>=Bounds.get(0).getY())){//Above Upper Y and below X
				point.setX(Bounds.get(1).getX());
				point.setY(Bounds.get(0).getY());
				ring.add(point);
			}
			else if((point.getY()<=Bounds.get(1).getY())){//Below Lower Y and X
				point.setX(Bounds.get(1).getX());
				point.setY(Bounds.get(1).getY());
				ring.add(point);
			}
			else {//Just X condition
				point.setX(Bounds.get(1).getX());
				ring.add(point);
			}
		}
		else { // X is fine
			if((point.getY()>=Bounds.get(0).getY())){ //Above Upper Y 
				point.setY(Bounds.get(0).getY());
				ring.add(point);
			}
			else if((point.getY()<=Bounds.get(1).getY())){//Below Lower Y
				point.setY(Bounds.get(1).getY());
				ring.add(point);
			}
		}
	}
	
	/**
	 * Generate points along a set number of spokes for a circle of a given radius 
	 * @param prev - Previous circle
	 * @param center - contains the X,Y,Z points of the center
	 * @param radius - the distance from the center point
	 * @param numSpokes - the number of spokes to draw point on
	 */
	public void makeCircle(Circle prev, Point center, int numSpokes){
		double radius = 5;
		double deltax,deltay; 
		double radCount = 0;
		double radians = (float) (2.0 * Math.PI)/numSpokes;
		ring = new ArrayList<Point>();
		Point tempPoint = null;
		for(int i = 0; i<numSpokes; i++) {
			deltax = radius * Math.cos(radCount);
			deltay = radius * Math.sin(radCount);
			if(prev != null) {
				tempPoint = new Point(prev.ring.get(i).getX() + deltax, prev.ring.get(i).getY() + deltay);	
			}
			else {
				tempPoint = new Point(center.getX() + deltax, center.getY() + deltay);
			}
			storePoint(tempPoint);
			tempPoint = null;
			radCount += radians;
		}  
		return;
    }
	
    /**
     * @return a coefficient used when swaying the circle to generate a more intuitive heat-map
     */
    private double coeff(){
        return .75;
    }
    
    /**
     * Snaps created points to existing points
     * @param points
     */
    private void assignHeights(ArrayList<Point> points) {
        for(Point p : ring) {
        	p.setZ(CircleGen.nearestNeighbor(p, points).getZ());
        }
    }
    
    /**
     * sways the circle based on the change in elevation between the point on the spoke and the center
     * @param radius - the distance from the center point
	 * @param center - contains the X,Y,Z points of the center
     */
    private void pushCircle (Point center, double radius){
        double deltaz;
        Point temp = new Point(0,0,0);
        int numSpokes = ring.size();
        for(int count = 0; count < numSpokes; count++){        	
	        deltaz = center.getZ() - ring.get(count).getZ();
	        temp.setXYZ((ring.get(count).getX() + deltaz * coeff() * ((ring.get(count).getX() - center.getX()) / radius)),(ring.get(count).getY() + deltaz * coeff() * ((ring.get(count).getY() - center.getY()) / radius)),0);
	        if(!((Bounds.get(1).getX()==ring.get(count).getX())||(Bounds.get(1).getX()==ring.get(count).getX())||(Bounds.get(0).getY()==ring.get(count).getY())||(Bounds.get(0).getY()==ring.get(count).getY()))) {
	        	ring.get(count).setXYZ(temp.getX(), temp.getY(), temp.getZ());
	        }
        }
    }    
}