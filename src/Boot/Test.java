package Boot;

import java.util.ArrayList;

public class Test {

	public static ArrayList<Point> points;
	
	public static void main(String args[]) {
	  	points = new ArrayList<Point>();
	  	int length = 500;
	  	Point center = new Point(0,0,0);
	  	Point newPoint = null;
	  	for(int i= -(length/2); i <= (length/2); i++) {
	  		for(int j = -(length/2); j <= (length/2); j++) {
	  	  		newPoint = new Point(i, j, (i+j)/2);
	  	  		newPoint.printPoint(newPoint);
	  	  		points.add(newPoint);
	  		}
	  	}
	  	CircleGen.circleGeneration(center, points, 8, 3);
	  	System.out.println("Finished");
	}

	/**
	 * Creates a new list of points with values around the desired point
	 * @param points is a list of points provided by the caller
	 * @param x is the desired x value
	 * @param y is the desired y value
	 * @param percent
	 * @return a new list of points near the desired point
	 */
  	public static ArrayList<Point> narrowListC(ArrayList<Point> points, Point point, int percent){      
	    int size = 0;
	    double totalX = 0;
	    double totalY = 0;
	    double removeX;
	    double removeY;
		ArrayList<Point> newList = new ArrayList<Point>();
	    for(Point p : points) {
	        size++;
	        totalX =+ p.getX();
	        totalY =+ p.getY();
	    }
	    removeX=(totalX/size)*(percent/200);
	    removeY=(totalY/size)*(percent/200);
	    for(Point p: points) {
	    	if(((((point.getX())-((totalX/size)*(100-(percent/200)))) <= p.getX())&&(((point.getX())+((totalX/size)*(100-(percent/200)))) <= p.getX()))&&((((point.getY())-((totalY/size)*(100-(percent/200)))) <= p.getY())&&(((point.getY())+((totalY/size)*(100-(percent/200)))) <= p.getY()))){
	    		newList.add(p);
	    	}
	    }
	    return newList;
	}
  	
}
