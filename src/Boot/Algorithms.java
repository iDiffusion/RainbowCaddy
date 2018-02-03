package Boot;

import java.util.ArrayList;

/**
 * @author Tylon Lee
 */
public class Algorithms {
	
	/**
	 * Short a list of points based on the Y component
	 * @param a
	 * @param left
	 * @param right
	 */
	public static void quicksortY(ArrayList<Point> a, int left, int right) {
		int m;
		if (left >= right) return;
		m = partitionY(a, left, right);
		quicksortY(a, left, m - 1);
		quicksortY(a, m + 1, right);
		return;
	}
	
	/**
	 * Used in conjunction with quicksortY to actually sort the list of component based on Y
	 * @param a
	 * @param left
	 * @param right
	 * @return
	 */
	public static int partitionY(ArrayList<Point> a, int left, int right) {
		int i = left - 1, j = right;
		Point p = new Point(a.get(right));
		while (true) {
			while (a.get(++i).getY() < p.getY());
			while (p.getY() < a.get(--j).getY())
				if (j == left) break;
			if (i >= j) break;
			EXCH(a.get(i), a.get(j));
		}
		EXCH(a.get(i), a.get(right));
		return i;
	}
	
	/**
	 * Short a list of points based on the X component
	 * @param a
	 * @param left
	 * @param right
	 */
	public static void quicksortX(ArrayList<Point> a, int left, int right) {
		int m;
		if (left >= right) return;
		m = partitionX(a, left, right);
		quicksortX(a, left, m - 1);
		quicksortX(a, m + 1, right);
		return;
	}

	/**
	 * Used in conjunction with quicksortX to actually sort the list of component based on X
	 * @param a
	 * @param left
	 * @param right
	 * @return
	 */
	public static int partitionX(ArrayList<Point> a, int left, int right) {
		int i = left - 1, j = right;
		Point p = new Point(a.get(right));
		while (true) {
			while (a.get(++i).getX() < p.getX());
			while (p.getX() < a.get(--j).getX())
				if (j == left) break;
			if (i >= j) break;
			EXCH(a.get(i), a.get(j));
		}
		EXCH(a.get(i), a.get(right));
		return i;
	}

	/** TESTED
	 * Swaps the points position in memory by exchanging the point's values
	 * @param a - holds one point
	 * @param b - holds the other point
	 */
	public static void EXCH(Point a, Point b) {
		Point temp = new Point(a.getX(), a.getY(), a.getZ(), a.getRGBAsArray()[0], a.getRGBAsArray()[1],
				a.getRGBAsArray()[2]);

		a.setXYZ(b.getX(), b.getY(), b.getZ());
		a.setRGB(b.getR(), b.getG(), b.getB());

		b.setXYZ(temp.getX(), temp.getY(), temp.getZ());
		b.setRGB(temp.getR(), temp.getG(), temp.getB());

		temp = null;
	}

	/** TESTED
	 * Creates a new list of points with values around the desired point
	 * @param points - is a list of points provided by the caller
	 * @param x - is the desired x value
	 * @param y - is the desired y value
	 * @param accuracy - is how close the points are to the desired x,y (i.e. 0 is the lowest)
	 * @return a new list of points near the desired point
	 */
	public static ArrayList<Point> narrowListC(ArrayList<Point> points, double x, double y, double accuracy) {
		double left = Math.floor(x) - Math.abs(accuracy);
		double right = Math.ceil(x) + Math.abs(accuracy);
		double up = Math.ceil(y) + Math.abs(accuracy);
		double down = Math.floor(y) - Math.abs(accuracy);

		ArrayList<Point> newList = new ArrayList<Point>();

		for (Point p : points) {
			if ((p.getX() > left && p.getX() < right) && (p.getY() > down && p.getY() < up)) {
				newList.add(p);
			} 
			else {
				continue;
			}
		}
		if (newList.isEmpty()) {
			newList = narrowListC(points, x, y, ++accuracy);
		}
		return newList;
	}
}
