package Boot;
import java.util.ArrayList;
import javax.vecmath.Vector3d;

import org.lwjgl.util.Color;

/**
 * Date Created: Dec 18, 2017
 * @author Eli Rhyne
 * @author Liam Marshall 
 */

public class CircleGen {
	public static ArrayList<Vector3d> colors  = new ArrayList<Vector3d>();
	
	/**
	 * @return Generates circles and fills in the colors based on change in elevation
	 * @param center - Center of the rings to be created
	 * @param points - ArrayList of all the points in the mesh to be colored
	 * @param spokes - Number of spokes to generate the circle Higher = more accuracy
	 * @param numCircle - Number of circle to make 
	 */
	public static void circleGeneration(Point center, ArrayList<Point> points, int spokes, int numCircle){
		if(colors.isEmpty()) {
			colors.add(new Vector3d(1,255,1)); //Green - index 0
			colors.add(new Vector3d(255,255,1)); //Yellow - index 1
			colors.add(new Vector3d(255,1,1)); //Red - index 2
			colors.add(new Vector3d(1,1,255)); //Blue - index 3
			colors.add(new Vector3d(255,69,1)); //Orange - index 4
			colors.add(new Vector3d(255,255,255)); //White - index 5
		}
		
		Point point = new Point(points.get(0).getX(), points.get(0).getY(), points.get(0).getZ());
		double minX = point.getX();
		double minY = point.getY();
		double maxX = point.getX();
		double maxY = point.getY();
		for(int i = 0; i < points.size(); i++) {
			if(minX > points.get(i).getX()) {
				minX = points.get(i).getX();
			}
			else if(maxX < points.get(i).getX()) {
				maxX = points.get(i).getX();
			}
			else if(minY > points.get(i).getY()) {
				minY = points.get(i).getY();
			}
			else if(maxY < points.get(i).getY()) {
				maxY = points.get(i).getY();
			}
		}
		double length;
		if((maxX - minX)>(maxY - minY)){
			length = (maxX - minX);
		}
		else {
			length = (maxY - minY);
		}

		ArrayList<Circle> circles = new ArrayList<Circle>();
		for(int i = 1; i <= numCircle; i++) {
			double radius = (((double)length)*((double)i/(double)numCircle));
			circles.add(new Circle(radius, center, spokes, points));	
			for(Point p : circles.get(i-1).ring) {
				p.setRGB((int)colors.get(i-1).x,(int)colors.get(i-1).y,(int)colors.get(i-1).z);
			}
			if(i == 1) {
				for (Point p : points) {
					if(insideRing(p,circles.get(i-1).ring)){
						p.setRGB((int)colors.get(i-1).x,(int)colors.get(i-1).y,(int)colors.get(i-1).z);
					}
				}
			}
			else if(i == numCircle) {
				for(Point p : points) {
					if(!insideRing(p, circles.get(i-1).ring)) {
						p.setRGB((int)colors.get(i-1).x,(int)colors.get(i-1).y,(int)colors.get(i-1).z);
					}
				}
			}
			else {
				fillRings(points, circles.get(i-1).ring, circles.get(i-2).ring);
			}
		}
		System.out.println("Finished");
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
				if(ring.get(i).getY() < ring.get(item).getY()) { //if slant up
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
		double outerDistance = Math.sqrt((outer.getX()-color.getX())*(outer.getX()-color.getX())  +  (outer.getY()-color.getY())*(outer.getY()-color.getY()) + (outer.getZ()-color.getZ())*(outer.getZ()-color.getZ()));
		double innerDistance = Math.sqrt((color.getX()-outer.getX())*(color.getX()-outer.getX())  +  (color.getY()-outer.getY()*(color.getY())-outer.getY()) + (color.getZ()-outer.getZ())*(color.getZ()-outer.getZ()));
		double outerPercent = outerDistance/(innerDistance+outerDistance);
		double innerPercent = innerDistance/(innerDistance+outerDistance);
		color.setRGB((int)((outer.getRGBAsArray()[0]*outerPercent)+(inner.getRGBAsArray()[0]*innerPercent)), (int)((outer.getRGBAsArray()[1]*outerPercent)+(inner.getRGBAsArray()[1]*innerPercent)), (int)((outer.getRGBAsArray()[2]*outerPercent)+(inner.getRGBAsArray()[2]*innerPercent)));
	}
	/**
	 * 
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
}
