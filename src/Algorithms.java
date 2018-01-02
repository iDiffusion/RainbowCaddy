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
		
		a.r = b.r;
		a.g = b.g;
		a.b = b.b;
		a.x = b.x;
		a.y = b.y;
		a.z = b.z;
		
		b.r = temp.r;
		b.g = temp.g;
		b.b = temp.b;
		b.x = temp.x;
		b.y = temp.y;
		b.z = temp.z;
	}
	
	public static boolean LESS(double l, double r) {
		return l < r;
	}
	
//	public static ArrayList<Point> narrowList(ArrayList<Point>, double x, double y, int zone){
//		double left = Math.floor(x) - (double) zone;
//		double right = math.ceil(x) + (double) zone;
//		double up = math.ceil(y) + (double) zone;
//		double down = Math.floor(y) - (double) zone;
//		
//		ArrayList<Point> newList = new ArrayList<Point>();
//		
//	}
	
}
