package Boot;

import java.util.ArrayList;

public class Algorithms {   
	public static void quicksortY(ArrayList<Point> a, int left, int right) {
	    int m;
	    if (left >= right) return;
	    m = partitionY(a, left, right);
	    quicksortY(a, left, m-1);
	    quicksortY(a, m+1, right);
	    return;
	} 

	public static int partitionY(ArrayList<Point> a, int left, int right) {
	    int i = left-1, j = right;
	    Point p = new Point(a.get(right).getX(), a.get(right).getY(), a.get(right).getZ(), a.get(right).getRGBAsArray()[0], a.get(right).getRGBAsArray()[1], a.get(right).getRGBAsArray()[2]);
	    while (true) {
	        while (LESS(a.get(++i).getY(),p.getY()));
	        while (LESS(p.getY(),a.get(--j).getY())) if (j == left) break;
	        if (i >= j) break;
	        EXCH(a.get(i),a.get(j));
	    }
	    EXCH(a.get(i),a.get(right));

	    return i;
	}
	
	public static void quicksortX(ArrayList<Point> a, int left, int right) {
	    int m;
	    if (left >= right) return;
	    m = partitionX(a, left, right);
	    quicksortX(a, left, m-1);
	    quicksortX(a, m+1, right);
	    return;
	} 

	public static int partitionX(ArrayList<Point> a, int left, int right) {
	    int i = left-1, j = right;
	    Point p = new Point(a.get(right).getX(), a.get(right).getY(), a.get(right).getZ(), a.get(right).getRGBAsArray()[0], a.get(right).getRGBAsArray()[1], a.get(right).getRGBAsArray()[2]);
	    while (true) {
	        while (LESS(a.get(++i).getX(),p.getX()));
	        while (LESS(p.getX(),a.get(--j).getX())) if (j == left) break;
	        if (i >= j) break;
	        EXCH(a.get(i),a.get(j));
	    }
	    EXCH(a.get(i),a.get(right));
	    return i;
	}
	
	public static void EXCH(Point a, Point b) {
		Point temp = new Point(a.getX(), a.getY(), a.getZ(), a.getRGBAsArray()[0], a.getRGBAsArray()[1], a.getRGBAsArray()[2]);
		
		a.setR(b.getR());
		a.setG(b.getG());
		a.setB(b.getB());
		a.setX(b.getX());
		a.setY(b.getY());
		a.setZ(b.getZ());
		
		b.setR(temp.getR());
		b.setG(temp.getG());
		b.setB(temp.getB());
		b.setX(temp.getX());
		b.setY(temp.getY());
		b.setZ(temp.getZ());
	}
	
	public static boolean LESS(double l, double r) {
		return l < r;
	}
/**
 * Removes points from a list that are not near the desired point
 * @param points is a list of points provided by the caller
 * @param x is the desired x value
 * @param y is the desired y value
 * @param accuracy is how close the points are to the desired x,y (i.e. 0 is the lowest)
 * @return a modified list of points near the desired point
 */
	public static void narrowListR(ArrayList<Point> points, double x, double y, double accuracy){
	    double left = Math.floor(Math.abs(x)) - (double) accuracy;
	    double right = Math.ceil(Math.abs(x)) + (double) accuracy;
	    double up = Math.ceil(Math.abs(y)) + (double) accuracy;
	    double down = Math.floor(Math.abs(y)) - (double) accuracy;
	    ArrayList<Point> newList = new ArrayList<Point>();
	    Boot.passArrayList(points, newList);
	    for(Point p : newList) {
	        if((p.getX() > left && p.getX() < right) && (p.getY() > down && p.getY() < up)) {
	            continue;
	        }
	        else {
	            points.remove(p);
	        }
	    }
	    Boot.passArrayList(newList, points);
	}
	/**
     * Creates a new list of points with values around the desired point
     * @param points is a list of points provided by the caller
     * @param x is the desired x value
     * @param y is the desired y value
     * @param accuracy is how close the points are to the desired x,y (i.e. 0 is the lowest)
     * @return a new list of points near the desired point
     */
    public static ArrayList<Point> narrowListC(ArrayList<Point> points, double x, double y, double accuracy){
        double left = Math.floor(x) - (double) accuracy;
        double right = Math.ceil(x) + (double) accuracy;
        double up = Math.ceil(y) + (double) accuracy;
        double down = Math.floor(y) - (double) accuracy;
        
        ArrayList<Point> newList = new ArrayList<Point>();
        
        for(Point p : points) {
            if((p.getX() > left && p.getX() < right) && (p.getY() > down && p.getY() < up)) {
                newList.add(p);
            }
            else {
                continue;
            }
        }
        
        return newList;
    }
}

