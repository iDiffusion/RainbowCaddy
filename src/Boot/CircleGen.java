package Boot;

import java.awt.Color;
import java.util.ArrayList;
import javax.vecmath.Vector3d;

/**
 * @author Eli Rhyne
 */

public class CircleGen {
	public static ArrayList<Vector3d> colors = new ArrayList<Vector3d>();

	/**
	 * 
	 * @param center
	 * @param points
	 * @param spokes
	 * @param numCircle
	 */
	public static void circleGeneration(Point center, ArrayList<Point> points, int spokes, int numCircle){
		colors.add(new Vector3d(0,255,0)); //Green - index 0
		colors.add(new Vector3d(255,255,0)); //Yellow - index 1
		colors.add(new Vector3d(255,0,0)); //Red - index 2
		colors.add(new Vector3d(0,0,255)); //Blue - index 3
		colors.add(new Vector3d(255,69,0)); //Orange - index 4
		colors.add(new Vector3d(255,255,255)); //White - index 5
		
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
		
		double radius;
		ArrayList<Circle> circles = new ArrayList<Circle>();
		for(int i = 1; i <= numCircle; i++) {
			radius = (((double)length)*((double)i/(double)numCircle));
			circles.add(new Circle(radius, center, spokes, points));	
			for(Point p : circles.get(i-1).getCircle()) {
				p.setRGB((int)colors.get(i-1).x,(int)colors.get(i-1).y,(int)colors.get(i-1).z);
			}
			if(i == 1) {
				for (Point p : points) {
					if(insideRing(p,circles.get(i-1).getCircle())){
						p.setRGB((int)colors.get(i-1).x,(int)colors.get(i-1).y,(int)colors.get(i-1).z);
					}
				}
			}
			else if(i == numCircle) {
				for(Point p : points) {
					if(!insideRing(p, circles.get(i-1).getCircle())) {
						p.setRGB((int)colors.get(i-1).x,(int)colors.get(i-1).y,(int)colors.get(i-1).z);
					}
				}
			}
			else {
				fillRings(points, circles.get(i).getCircle(), circles.get(i-1).getCircle());
			}
		}	
	}

	/**
	 * 
	 * @param points
	 * @param ring1
	 * @param ring2
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
	 * @param point
	 * @param ring
	 * @return
	 */
	private static boolean insideRing(Point point, ArrayList<Point> ring) { 
		int count = 0;
		for(int i = 0; i < ring.size(); i++) {
			int item = i+1 == ring.size() ? 0 : i + 1;
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
		if(count%2 == 1) {
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * 
	 * @param outer
	 * @param inner
	 * @param color
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
	 * @param point
	 * @param points
	 * @return
	 */
    public static Point nearestNeighbor(Point point, ArrayList<Point> points) {
    	boolean xSearch=true;
    	int size;
    	ArrayList<Point> temp1 = new ArrayList<Point>();
    	if(points.size() > 500) {
    		temp1 = Test.narrowListC(points, point, 25);
	    	Algorithms.quicksortX(temp1, 0, temp1.size()-1);
			while(true) {
				size = temp1.size();
				if(xSearch) {
					if(point.getX() > temp1.get(temp1.size()/2 + 1).getX()) {				/*if looking for point x is larger*/
		    			for(int j = (temp1.size()-1)/2; j >= 0 ; j--) {									/*store bigger than median sorted by y in temp*/
		    				temp1.remove(j);
		    			}
					}
					else if(point.getX() < temp1.get(temp1.size()/2 + 1).getX()){		/*median smaller than x*/
		    			for(int j = temp1.size()-1; j >= (size+1)/2; j--) {								/*store smaller than median sorted by y in temp*/
		    				temp1.remove(j);
		    			}
					}
					else if(point.getX() == temp1.get(temp1.size()/2).getX()){	
						size = temp1.size();
						while((point.getY() == temp1.get(size/2).getY()) && (point.getX() == temp1.get(size/2).getX())){
							size--;
						}
						for(int j = 0; j <= size+1; j++) {								/*store smaller than median sorted by y in temp*/
		    				temp1.remove(j);
		    			}
					}
					xSearch=false;
					Algorithms.quicksortY(temp1, 0, (temp1.size()-1));
				}
				else {
					if(point.getY() > temp1.get(temp1.size()/2).getY()) {			/*if looking for point y is larger*/
						for(int j = (temp1.size()-1)/2; j >= 0 ; j--) {									/*store bigger than median sorted by y in temp*/
		    				temp1.remove(j);
		    			}
		    			Algorithms.quicksortX(temp1, 0, (temp1.size()-1));
		    		}
					else if(point.getY() < temp1.get(temp1.size()/2).getY()) {		/*y is smaller than median*/
						for(int j = temp1.size()-1; j >= (size+1)/2; j--) {								/*store smaller than median sorted by y in temp*/
		    				temp1.remove(j);
		    			}
		    		}
					else if(point.getY() == temp1.get(temp1.size()/2).getY()){	
						size = temp1.size();
						while((point.getY() == temp1.get(size/2).getY()) && (point.getX() == temp1.get(size/2).getX())){
							size--;
						}
						for(int j = 0; j <= size+1; j++) {								/*store smaller than median sorted by y in temp*/
		    				temp1.remove(j);
		    			}
					}
					Algorithms.quicksortX(temp1, 0, (temp1.size()-1));
					xSearch=true;
				}
				if(temp1.size() <= 500) {
					break;
				}
			}
    	}
    	else {
    		for(Point p: points) {
    			temp1.add(p);
    		}
    	}
		Point closest = temp1.get(0);
		double closestDis = Math.sqrt(Math.pow((temp1.get(0).getX()-point.getX()),2) + Math.pow((temp1.get(0).getY()-point.getY()),2));
		for(Point p : temp1) {
			if( closestDis >= Math.sqrt(Math.pow((p.getX()-point.getX()),2) + Math.pow((p.getY()-point.getY()),2))) {
				closestDis = Math.sqrt(Math.pow((p.getX()-point.getX()),2) + Math.pow((p.getY()-point.getY()),2));
				closest = p;
			}
		}
		for(Point p : points) {
			if((closest.getX() == p.getX()) && (closest.getY() == p.getY()) && (closest.getZ() == p.getZ())) {
				return p;
			}
		}
		return null;
	}
}
