package Boot;
/**
 * A face is an array of 3 points.
 *  
 * @author Keegan Bruer
 *
 */
public class Face {
	Point[] points = new Point[3];
	public Face(Point one, Point two, Point three) {
		points[0] = one;
		points[1] = two;
		points[2] = three;
	}
}
