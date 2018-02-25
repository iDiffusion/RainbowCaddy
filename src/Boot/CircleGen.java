package Boot;

import java.awt.Color;
import java.util.ArrayList;
import javax.swing.JColorChooser;

/**
 * Date Created: Dec 18, 2017
 * @author Liam Marshall 
 * @author Eli Rhyne
 */


public class CircleGen{

	public static ArrayList<Point> Bounds;
	public static Circle circles[] = new Circle[5];
	public static ArrayList<Point> Points;
	public static double[] rad = {2,9,16,23,30};
	
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
	public static Circle[] createCircles(Point center, int numSpokes, ArrayList<Point> points, ArrayList<Point> bounds) {

		System.out.println("Intializing threads...");
		SpokeGen[] threads = new SpokeGen[Runtime.getRuntime().availableProcessors()];
		int count = 0;
		Bounds = bounds;
		for(int i = 0; i<circles.length; i++) {
			circles[i] = new Circle(center, rad[count], count++, addColors());
			System.out.println("Set Base Data and color for Circle " + (count));
			for(int j = 0; j< numSpokes; j++) {
				circles[i].add(new Point(0,0));
			}
		}
		
		System.out.println("Initializing spoke generation, Begining thread workers:\n");
		
		for (int i = 0; i < threads.length; i++) {
		    threads[i] = new SpokeGen();
		    threads[i].threadNum = i;
		    threads[i].points = points;
		    threads[i].start();
		}
		for (int i = 0; i < threads.length; i++) {
			try {
				threads[i].join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("\nThreads Done");
		return circles;
	}
	
	

	/**
	 * @author earhy
	 * 
	 * Prompts the user to add colors
	 */
	private static Color addColors() {
		Color color = Color.LIGHT_GRAY;
		color = JColorChooser.showDialog( null,"Choose a color", color );
		return color;
	}


	/**
	 * 
	 * @author earhy
	 * Stores the point within bounds of the ArrayList
	 * @param point - Point to be stored 
	 * @param circle - Pointer to the circle being worked with
	 */
	private static void storePoint(Point point, Circle circle, int count) {
		point.setRGB(circle.get(0).getR(), circle.get(0).getG(), circle.get(0).getB());
		if(((point.getX()<=Bounds.get(0).getX())&&(point.getX()>=Bounds.get(1).getX()))&&
				((point.getY()<=Bounds.get(0).getY())&&(point.getY()>=Bounds.get(1).getY()))){
			circle.setPoint(count, point);
		}
		else if((point.getX()>=Bounds.get(0).getX())){ // Above Upper X
			if((point.getY()>Bounds.get(0).getY())){//Above Upper Y and Above X
				point.setX(Bounds.get(0).getX());
				point.setY(Bounds.get(0).getY());
				circle.setPoint(count, point);
			}
			else if((point.getY()<Bounds.get(1).getY())){//Below Lower Y and above X
				point.setX(Bounds.get(0).getX());
				point.setY(Bounds.get(1).getY());
				circle.setPoint(count, point);
			}
			else {//Just X condition
				point.setX(Bounds.get(0).getX());
				circle.setPoint(count, point);
			}
		}
		else if((point.getX()<=Bounds.get(1).getX())){ // Below Lower X
			if((point.getY()>=Bounds.get(0).getY())){//Above Upper Y and below X
				point.setX(Bounds.get(1).getX());
				point.setY(Bounds.get(0).getY());
				circle.setPoint(count, point);
			}
			else if((point.getY()<=Bounds.get(1).getY())){//Below Lower Y and X
				point.setX(Bounds.get(1).getX());
				point.setY(Bounds.get(1).getY());
				circle.setPoint(count, point);
			}
			else {//Just X condition
				point.setX(Bounds.get(1).getX());
				circle.setPoint(count, point);
			}
		}
		else { // X is fine
			if((point.getY()>=Bounds.get(0).getY())){ //Above Upper Y 
				point.setY(Bounds.get(0).getY());
				circle.setPoint(count, point);
			}
			else if((point.getY()<=Bounds.get(1).getY())){//Below Lower Y
				point.setY(Bounds.get(1).getY());
				circle.setPoint(count, point);
			}
		}
	}
	
	/**
	 * Generate points along a set number of spokes for a circle of a given radius 
	 * @param prevC - Previous circle
	 * @param count - the spoke being worked on
	 * @param circle - The Circle being made
	 */
	public static void makePoint(Circle prevC, int count, Circle circle){
		int numSpokes = circle.getRing().size();
		double radius;
		Point prev;
		if(prevC.getRad()==circle.getRad()) {
			prev = prevC.getCenter();
			radius = circle.getRad();
		}
		else {
			prev = prevC.get(count);
			radius = circle.getRad()-prevC.getRad();
		}
		double radian =  count*(2.0 * Math.PI)/(double)numSpokes;
		double deltax,deltay;
		deltax = radius * Math.cos(radian);
		deltay = radius * Math.sin(radian);
		Point t = new Point(prev.getX() + deltax, prev.getY() + deltay);
		storePoint(t,circle,count);
		t = null;
		return;
    }
	
    /**
     * @return a coefficient used when swaying the circle to generate a more intuitive heat-map
     */
    private static double coeff(){
        return 2;
    }
    
    /**
     * Snaps created points to existing points
     * @param points
     */
    public static void assignHeight(ArrayList<Point> points, Point p) {
        p.setZ(CircleColor.nearestNeighbor(p, points).getZ());
    }
    
    /**
     * sways the circle based on the change in elevation between the point on the spoke and the center
     * @param radius - the distance from the center point
	 * @param center - contains the X,Y,Z points of the center
     */
    public static void pushPoint(Circle c, int spoke){
    	Point center = c.getCenter();
    	double radius = c.getRad();
        Point temp = new Point(0,0,0);
        double deltaz = center.getZ() - c.get(spoke).getZ();
        temp.setXYZ((c.get(spoke).getX() + deltaz * coeff() * ((c.get(spoke).getX() - center.getX()) / radius)),
        			(c.get(spoke).getY() + deltaz * coeff() * ((c.get(spoke).getY() - center.getY()) / radius)),0);
        if(!(( Bounds.get(1).getX() == c.get(spoke).getX()) ||
        		( Bounds.get(1).getX() == c.get(spoke).getX() ) ||
        		( Bounds.get(0).getY() == c.get(spoke).getY() ) ||
        		( Bounds.get(0).getY() == c.get(spoke).getY() ))) {
        	c.setPoint(spoke , temp);
        }
    } 

}

class SpokeGen extends Thread{
	public int threadNum;
	public ArrayList<Point> points;
	@Override
	public void run() {
		System.out.println("\tStarting SpokeGen Thread number " + (threadNum+1) + " of "+ (Runtime.getRuntime().availableProcessors()));
		int processorNum = Runtime.getRuntime().availableProcessors();
		for(int i = threadNum; CircleGen.circles[0].getRing().size()>i ; i = (i+processorNum)) {
			for(Circle c: CircleGen.circles) {
				if(c.getCount()==0) {
					CircleGen.makePoint(c,i,c);
				}
				else {
					CircleGen.makePoint(CircleGen.circles[c.getCount()-1],i,c);
				}
				CircleGen.assignHeight(points, c.get(i));
				CircleGen.pushPoint(c, i);
				CircleGen.assignHeight(points, c.get(i));
			}
		}
		System.out.println("\tEnding Thread number " + (threadNum+1) + " of "+ (Runtime.getRuntime().availableProcessors()));
	}
}