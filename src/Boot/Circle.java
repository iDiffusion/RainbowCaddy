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
	 */
	public Circle(double radius, Point center, int numSpokes, ArrayList<Point> points, ArrayList<Point> bounds) {
		Bounds = bounds;
		makeCircle(center,radius,numSpokes);
		assignHeights(points);
		pushCircle(center, radius);
		assignHeights(points);
	}
	
	/**
	 * @return a list of points that make up the ring
	 * @param N/A
	 */
	public ArrayList<Point> getCircle(){
        return ring;
    }
	/**
	 * @return stores the point within bounds of the ArrayList
	 * @param point - Point to be stored 
	 */
	private void storePoint(Point point) {
		if(((point.getX()<=Bounds.get(0).getX())&&(point.getX()>=Bounds.get(1).getX()))&&((point.getY()<=Bounds.get(0).getY())&&(point.getY()>=Bounds.get(1).getY()))){
			ring.add(point);
		}
		else {
			if((point.getX()>Bounds.get(0).getX())){ // Above Upper X
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
			if((point.getX()<Bounds.get(1).getX())){ // Below Lower X
				if((point.getY()>Bounds.get(0).getY())){//Above Upper Y and below X
					point.setX(Bounds.get(1).getX());
					point.setY(Bounds.get(0).getY());
					ring.add(point);
				}
				else if((point.getY()<Bounds.get(1).getY())){//Below Lower Y and X
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
				if((point.getY()>Bounds.get(0).getY())){ //Above Upper Y 
					point.setY(Bounds.get(0).getY());
					ring.add(point);
				}
				else if((point.getY()<Bounds.get(1).getY())){//Below Lower Y
					point.setY(Bounds.get(1).getY());
					ring.add(point);
				}
			}
		}
	}
	/**
	 * @return Generate points along a set number of spokes for a circle of a given radius 
	 * @param center - contains the X,Y,Z points of the center
	 * @param radius - the distance from the center point
	 * @param numSpokes - the number of spokes to draw point on
	 */
	public void makeCircle(Point center, double radius, int numSpokes){
	      double deltax,deltay; 
	      double degCount=0;
	      double degree = (float) 360/numSpokes;
	      ring = new ArrayList<Point>();
	      Point tempPoint = null;
	      for(degCount=0; degCount<360; degCount+=degree){
	          deltax = radius*Math.cos(Math.toRadians(degCount));
	          deltay = radius*Math.sin(Math.toRadians(degCount));
	          tempPoint = new Point(center.getX() + deltax,center.getY() + deltay,0);
	          storePoint(tempPoint);
	          tempPoint = null;
	      }
	  }
    
    /**
     * @return a coefficient used when swaying the circle to generate a more intuitive heat-map
     */
    private double coeff(){
        return 5.0;
    }
    
    /**
     * Snaps created points to existing points
     * @param points
     */
    private void assignHeights(ArrayList<Point> points) {
    	Point temp;
        for(Point p : ring) {
            temp = CircleGen.nearestNeighbor(p, points);
            if((Bounds.get(1).getX()==p.getX())||(Bounds.get(1).getX()==p.getX())||(Bounds.get(1).getY()==p.getY())||(Bounds.get(1).getY()==p.getY())) {
            	if((Bounds.get(1).getX()==p.getX())||(Bounds.get(1).getX()==p.getX())&&(Bounds.get(1).getY()==p.getY())||(Bounds.get(1).getY()==p.getY())) {
            		p.setXYZ(p.getX(), p.getY(), temp.getZ());
            	}
            	else if((Bounds.get(1).getX()==p.getX())||(Bounds.get(1).getX()==p.getX())) {
            		p.setXYZ(p.getX(), temp.getY(), temp.getZ());
            	}
            	else {
            		p.setXYZ(temp.getX(), p.getY(), temp.getZ());
            	}
            }
            else {
            	p.setXYZ(temp.getX(), temp.getY(), temp.getZ());
            }
        }
        temp = null;
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
	        if(!((Bounds.get(1).getX()==ring.get(count).getX())||(Bounds.get(1).getX()==ring.get(count).getX())||(Bounds.get(1).getY()==ring.get(count).getY())||(Bounds.get(1).getY()==ring.get(count).getY()))) {
	        	ring.get(count).setXYZ(temp.getX(), temp.getY(), temp.getZ());
	        }
        }
    }    
}