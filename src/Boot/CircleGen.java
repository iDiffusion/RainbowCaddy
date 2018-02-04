package Boot;
import java.util.ArrayList;
import javax.vecmath.Vector3d;

/**
 * Date Created: Dec 18, 2017
 * @author Eli Rhyne
 * @author Liam Marshall 
 */

public class CircleGen {
	public static ArrayList<Vector3d> colorList  = new ArrayList<Vector3d>();
	public static ArrayList<Vector3d> colors  = new ArrayList<Vector3d>();
	public static double minX;
	public static double maxX;
	public static double minY;
	public static double maxY;
	public static int numcircle;
	
	/**
	 * @return Generates circles and fills in the colors based on change in elevation
	 * @param center - Center of the rings to be created
	 * @param points - ArrayList of all the points in the mesh to be colored
	 * @param spokes - Number of spokes to generate the circle Higher = more accuracy
	 * @param numCircle - Number of circle to make 
	 */
	public static void circleGeneration(Point center, ArrayList<Point> points, int spokes, int numCircle){
		ArrayList<Point> bounds  = new ArrayList<Point>();
		numcircle = numCircle;
		if(bounds.isEmpty()) {
			minX = points.get(0).getX();
			minY = points.get(0).getY();
			maxX = points.get(0).getX();
			maxY = points.get(0).getY();
			for(Point p : points) {
				if (p.getX() < minX) {
					minX = p.getX();
				} 
				if (p.getX() > maxX) {
					maxX = p.getX();
				}
				if (p.getY() < minY) {
					minY = p.getY();
				}
				if (p.getY() > maxY) {
					maxY = p.getY();
				}
			}
			bounds.add(new Point(maxX,maxY));
			bounds.add(new Point(minX,minY));
		}
		System.out.println("Center X:" + center.getX() + " Y:" + center.getY() +"\n");
		System.out.println("Bounds: \nX Max: " + bounds.get(0).getX() + " Y Max: " + bounds.get(0).getY() +"\nX Min: " + bounds.get(1).getX() + " Y Min: " + bounds.get(1).getY() + "\n");
		colorList.add(new Vector3d(0,255,0)); //Green
		colorList.add(new Vector3d(150,175,0)); //Yellow
		colorList.add(new Vector3d(255,0,0)); //Red
		colorList.add(new Vector3d(125,0,205)); //Magenta
		colorList.add(new Vector3d(0,0,255)); //Blue
		colorList.add(new Vector3d(0,130,200)); //Pale Blue
		boolean done = false;
		int count = 0;
		double radius = 0;
		ArrayList<Circle> circles = new ArrayList<Circle>();
		ArrayList<Point> tempBounds = new ArrayList<Point>();
		tempBounds.add(new Point(maxX+3,maxY+3));
		tempBounds.add(new Point(maxX+3,minY-3));
		tempBounds.add(new Point(minX-3,minY-3));
		tempBounds.add(new Point(minX-3,maxY+3)); 
		while(!done) {
			addColor(count);
			radius+=5;
			if(count!=0) {
				circles.add(new Circle(radius, center, spokes, points, bounds, circles.get(count-1)));
			}
			else {
				circles.add(new Circle(radius, center, spokes, points, bounds, null));
			}
			for(Point p : circles.get(count).ring) {
				p.setRGB((int)colors.get(count).x,(int)colors.get(count).y,(int)colors.get(count).z);
				nearestNeighbor(p,points).setRGB((int)colors.get(count).x,(int)colors.get(count).y,(int)colors.get(count).z);
			}
			if(count == 0) {
				for (Point p : points) {
					if(insideRing(p,circles.get(count).ring)){
						p.setRGB((int)colors.get(count).x,(int)colors.get(count).y,(int)colors.get(count).z);
					}
				}
			}
			else {
				fillRings(points, circles.get(count).ring, circles.get(count-1).ring);
			}
			if(numCircle+1 == count) {
				for(Point p: tempBounds) {
					p.setRGB((int)colors.get(count).x,(int)colors.get(count).y,(int)colors.get(count).z);
				}
				fillRings(points, tempBounds, circles.get(count).ring);
				done = true;
			}
			count++;
			System.out.print("Circle Number: " + count + "\n");
		}
		System.out.println("Finished");
	}
	/**
	 * @return Fills the colors list dynamically for all numbers and sizes of color arrays
	 * @param count - circle you are generating
	 */
	private static void addColor(int count) {
		colors.add(colorList.get(count%5));
	}
	/**
	 * @return Fills the rgb values of the points between the rings based on the color of the rings
	 * @param points - ArrayList of all points
	 * @param ring1 - ArrayList of the outer ring
	 * @param ring2 - ArrayList of the inner ring
	 */
	public static void fillRings(ArrayList<Point> points, ArrayList<Point> ring1, ArrayList<Point> ring2) {
		Point outerRing;
		Point innerRing;
		for (Point p : points) {
			if(insideRing(p, ring1) && !insideRing(p, ring2)){
				outerRing = nearestNeighbor(p, ring1);
				innerRing = nearestNeighbor(p, ring2);
				outerRing.setRGB(ring1.get(0).getR(), ring1.get(0).getG(), ring1.get(0).getB());
				innerRing.setRGB(ring2.get(0).getR(), ring2.get(0).getG(), ring2.get(0).getB());
				genColor(outerRing, innerRing, p);
			}			
		}
	}
	/**
	 * 
	 * @param point - Point to be tested
	 * @param ring - Ring that the point is relative to
	 * @return Returns 1 if a point is inside of a ring, and 0 if it is outside
	 */
	private static boolean insideRing(Point point, ArrayList<Point> ring) { 
		int count = 0;
		int item;
		for(int i = 0; i < ring.size(); i++) {
			item = ((i+1) == ring.size()) ? 0 : i+1;
			if((ring.get(item).getX()>point.getX())&&(ring.get(i).getX()>point.getX())) {
				if(ring.get(i).getY() > ring.get(item).getY()) {  // if slant down
					if(((ring.get(item).getY() <= point.getY()) && (ring.get(i).getY() >= point.getY()))) {
						count++;
					}
				}
				else if(ring.get(i).getY() < ring.get(item).getY()) { //if slant up
					if(((ring.get(item).getY() >= point.getY()) && (ring.get(i).getY() <= point.getY()))) {
						count++;
					}
				}
			}
		}
		if(count%2 == 1) {
			return true;
		}
		else {
			return false;
		}
	}
	/**
	 * @return Generates a color proportional to the distance of the point to the inner and outer rings
	 * @param outer - Outer Ring Point
	 * @param inner - Inner Ring Point
	 * @param color - Point to be colored
	 */
	private static void genColor(Point outer, Point inner, Point color) {
		double innerDistance = Math.sqrt((outer.getX()-color.getX())*(outer.getX()-color.getX())  +  (outer.getY()-color.getY())*(outer.getY()-color.getY()) + (outer.getZ()-color.getZ())*(outer.getZ()-color.getZ()));
		double outerDistance = Math.sqrt((inner.getX()-color.getX())*(inner.getX()-color.getX())  +  (inner.getY()-color.getY())*(inner.getY()-color.getY()) + (inner.getZ()-color.getZ())*(inner.getZ()-color.getZ()));
		double outerPercent = outerDistance/(innerDistance+outerDistance);
		double innerPercent = innerDistance/(innerDistance+outerDistance);
		color.setRGB((int)((outer.getRGBAsArray()[0]*outerPercent)+(inner.getRGBAsArray()[0]*innerPercent)), (int)((outer.getRGBAsArray()[1]*outerPercent)+(inner.getRGBAsArray()[1]*innerPercent)), (int)((outer.getRGBAsArray()[2]*outerPercent)+(inner.getRGBAsArray()[2]*innerPercent)));
	}
	/**
	 * @param point - Point to be looked for
	 * @param points - Array to look for it in
	 * @return - The point in the array that is closest to the point looking for
	 */
    public static Point nearestNeighbor(Point point, ArrayList<Point> points) {
		Point closest = points.get(0);
		double closestDis = Math.sqrt((points.get(0).getX()-point.getX())*(points.get(0).getX()-point.getX()) + ((points.get(0).getY()-point.getY())*(points.get(0).getY()-point.getY())));
		for(Point p : points) {
			if( closestDis >= Math.sqrt((p.getX()-point.getX())*(p.getX()-point.getX()) + ((p.getY()-point.getY())*(p.getY()-point.getY())))) {
				closestDis = Math.sqrt((p.getX()-point.getX())*(p.getX()-point.getX()) + ((p.getY()-point.getY())*(p.getY()-point.getY())));
				closest = p;
			}
		}
		return closest;
	}
    /**
	 * @param point - Point in relation to farthest to be looked for
	 * @param points - Array to look for it in
	 * @return - The point in the array that is farthest to the point looking for
	 */
    public static Point farthestNeighbor(Point point, ArrayList<Point> points) {
		Point farthest = points.get(0);
		double farthestDis = Math.sqrt((points.get(0).getX()-point.getX())*(points.get(0).getX()-point.getX()) + ((points.get(0).getY()-point.getY())*(points.get(0).getY()-point.getY())));
		for(Point p : points) {
			if( farthestDis <= Math.sqrt((p.getX()-point.getX())*(p.getX()-point.getX()) + ((p.getY()-point.getY())*(p.getY()-point.getY())))) {
				farthestDis = Math.sqrt((p.getX()-point.getX())*(p.getX()-point.getX()) + ((p.getY()-point.getY())*(p.getY()-point.getY())));
				farthest = p;
			}
		}
		return farthest;
	}
    /**
	 * @param p1 - first point
	 * @param p2 - second point
	 * @return - distance between 2 in 3d space
	 */
    public static double distanceBetween(Point p1, Point p2) {
    	double distance = Math.sqrt(Math.pow(p1.getX()-p2.getX(),2) + Math.pow(p1.getY()-p2.getY(),2) +Math.pow(p1.getZ()-p2.getZ(),2));
    	return distance;
    }
}
