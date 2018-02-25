package Boot;

import java.util.ArrayList;


/**
 * Date Created: Dec 18, 2017
 * @author Eli Rhyne
 * @author Liam Marshall 
 */

public class CircleColor {
	public static double minX;
	public static double maxX;
	public static double minY;
	public static double maxY;
	public static double modulate = .75;
	
	/**
	 * @return Generates circles and fills in the colors based on change in elevation
	 * @param center - Center of the rings to be created
	 * @param points - ArrayList of all the points in the mesh to be colored
	 * @param spokes - Number of spokes to generate the circle Higher = more accuracy
	 * @throws InterruptedException 
	 */
	public static void circleColor(Point center, ArrayList<Point> points, int spokes){	
		System.out.println("\n\n\nFinding bounds for this green:");
		
		ArrayList<Point> bounds  = new ArrayList<Point>();
		
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
		
		System.out.println("\tCenter X:" + center.getX() + " Y:" + center.getY());
		System.out.println("\tBounds: \n\tX Max: " + bounds.get(0).getX() + " Y Max: " +
						   bounds.get(0).getY() +"\n\tX Min: " +
						   bounds.get(1).getX() + " Y Min: " +
						   bounds.get(1).getY() + "\n");
		
		ArrayList<Point> tempBounds = new ArrayList<Point>();
		
		tempBounds.add(new Point(maxX+3,maxY+3));
		tempBounds.add(new Point(maxX+3,minY-3));
		tempBounds.add(new Point(minX-3,minY-3));
		tempBounds.add(new Point(minX-3,maxY+3)); 
		
		System.out.println("Making Circles...");
		
		Circle[] circles = CircleGen.createCircles(center, spokes, points, bounds);
		
		System.out.println("\nCircles Done: Coloring Circles...");
		
		Thread[] threads = new Thread[Runtime.getRuntime().availableProcessors()];
		
		for(Circle c: circles) {
			if(c.getCount()==0) {
				for (int i = 0; i < threads.length; i++) {
				    threads[i] = new Thread(new inside(points,c,i));
				    threads[i].start();
				    try {
						threads[i].join();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			else if(c.getCount()!=circles.length-1) {
				threads[c.getCount()] = new Thread(new fill(points, c, circles[c.getCount()-1],c.getCount()));
				threads[c.getCount()].start();
			}
			else {
				fillRings(points, c, circles[c.getCount()-1]);
				for (int i = 0; i < threads.length; i++) {
				    threads[i] = new Thread(new outside(points,c,i));
				    threads[i].start();
				    try {
						threads[i].join();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
		
		bounds = new ArrayList<Point>();
		tempBounds = null;
//		double err = .25;
//		int counter = 4;
//		for(Circle c: circles) {
//			for(Point closestPoint: c.getRing()) {
//				for(Point p: points) {
//					if (p.getX() > closestPoint.x - err && p.getX() < closestPoint.x + err &&
//							p.getY() > closestPoint.y - err && p.getY() < closestPoint.y + err) {
//							p.setColor(circles[counter].getColor());
//					}
//				}
//			}
//			counter--;
//		}
		circles = new Circle[5];
		System.out.println("Finished!");
	}
	/**
	 * @return Fills the rgb values of the points between the rings based on the color of the rings
	 * @param points - ArrayList of all points
	 * @param ring1 - ArrayList of the outer ring
	 * @param ring2 - ArrayList of the inner ring
	 */
	public static void fillRings(ArrayList<Point> points, Circle c1, Circle c2) {
		Point outerRing;
		Point innerRing;
		for (Point p : points) {
			if(insideRing(p, c1) && !insideRing(p, c2)){
				outerRing = nearestNeighbor(p, c1.getRing());
				innerRing = nearestNeighbor(p, c2.getRing());
				outerRing.setColor(c1.getColor(),modulate);
				innerRing.setColor(c2.getColor(),modulate);
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
	public static boolean insideRing(Point point, Circle c) { 
		int count = 0;
		int item;
		ArrayList<Point> ring = c.getRing();
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
		double innerDistance = Math.sqrt((outer.getX()-color.getX())*(outer.getX()-color.getX())  +
										  (outer.getY()-color.getY())*(outer.getY()-color.getY()) + 
										  (outer.getZ()-color.getZ())*(outer.getZ()-color.getZ()));
		
		double outerDistance = Math.sqrt((inner.getX()-color.getX())*(inner.getX()-color.getX()) +
										 (inner.getY()-color.getY())*(inner.getY()-color.getY()) + 
										 (inner.getZ()-color.getZ())*(inner.getZ()-color.getZ()));
		
		double outerPercent = outerDistance/(innerDistance+outerDistance);
		double innerPercent = innerDistance/(innerDistance+outerDistance);
		
		color.setRGB((int)(((outer.getR()*outerPercent)+(inner.getR()*innerPercent))), 
					 (int)(((outer.getG()*outerPercent)+(inner.getG()*innerPercent))), 
					 (int)(((outer.getB()*outerPercent)+(inner.getB()*innerPercent))));
	}
	/**
	 * @param point - Point to be looked for
	 * @param points - Array to look for it in
	 * @return - The point in the array that is closest to the point looking for
	 */
    public static Point nearestNeighbor(Point point, ArrayList<Point> points) {
		Point closest = points.get(0);
		double closestDis = Math.sqrt((points.get(0).getX()-point.getX())*(points.get(0).getX()-point.getX()) +
									 ((points.get(0).getY()-point.getY())*(points.get(0).getY()-point.getY())));
		for(Point p : points) {
			if( closestDis >= Math.sqrt((p.getX()-point.getX())*(p.getX()-point.getX()) +
									   ((p.getY()-point.getY())*(p.getY()-point.getY())))) 
			{
				
				closestDis = Math.sqrt((p.getX()-point.getX())*(p.getX()-point.getX()) +
									  ((p.getY()-point.getY())*(p.getY()-point.getY())));
				
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
		double farthestDis = Math.sqrt((points.get(0).getX()-point.getX())*(points.get(0).getX()-point.getX()) +
									  ((points.get(0).getY()-point.getY())*(points.get(0).getY()-point.getY())));
		for(Point p : points) {
			if( farthestDis <= Math.sqrt((p.getX()-point.getX())*(p.getX()-point.getX()) +
										((p.getY()-point.getY())*(p.getY()-point.getY())))) 
			{
				farthestDis = Math.sqrt((p.getX()-point.getX())*(p.getX()-point.getX()) +
									   ((p.getY()-point.getY())*(p.getY()-point.getY())));
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
    	double distance = Math.sqrt(Math.pow(p1.getX()-p2.getX(),2) + 
    								Math.pow(p1.getY()-p2.getY(),2) +
    								Math.pow(p1.getZ()-p2.getZ(),2));
    	return distance;
    }
}

class inside implements Runnable{
	public ArrayList<Point> points;
	public Circle c;
	int threadNum;
	inside(ArrayList<Point> P, Circle circle, int i) {
		points = P;
		c = circle;
		threadNum = i;
	}
	@Override
	public void run() {
		System.out.println("\tStarting Inside Test Thread number " + (threadNum+1) + " of "+ (Runtime.getRuntime().availableProcessors()));
		int processorNum = Runtime.getRuntime().availableProcessors();
		for(int i = threadNum; i<points.size(); i=i+processorNum) {
			if(CircleColor.insideRing(points.get(i),c)) {
				points.get(i).setColor(c.getColor(),CircleColor.modulate);
			}
		}
	}	
	
}
class outside implements Runnable{
	public ArrayList<Point> points;
	public Circle c;
	int threadNum;
	outside(ArrayList<Point> P, Circle circle, int i) {
		points = P;
		c = circle;
		threadNum = i;
	}
	@Override
	public void run() {
		System.out.println("\tStarting Outside Test Thread number " + (threadNum+1) + " of "+ (Runtime.getRuntime().availableProcessors()));
		int processorNum = Runtime.getRuntime().availableProcessors();
		for(int i = threadNum; i<points.size(); i=i+processorNum) {
			if(!CircleColor.insideRing(points.get(i),c)) {
				points.get(i).setColor(c.getColor(),CircleColor.modulate);
			}
		}
	}
}
class fill implements Runnable{
	public ArrayList<Point> points;
	public Circle c1;
	public Circle c2;
	int threadNum;
	fill(ArrayList<Point> p, Circle r1,Circle r2, int i) {
		points = p;
		c1 = r1;
		c2 = r2;
		threadNum = i;
	}
	@Override
	public void run() {
		System.out.println("\tStarting Fill Thread number " + (threadNum+1) + " of "+ (Runtime.getRuntime().availableProcessors()));
		CircleColor.fillRings(points, c1, c2);
	}	
}