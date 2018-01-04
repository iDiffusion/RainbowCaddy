import java.util.ArrayList;
import java.util.Collections;
import javax.vecmath.Vector3d;


public class CircleGen {
	public static ArrayList<Vector3d> colors  = new ArrayList<Vector3d>();
	
	public static void circleGeneration(Point center, ArrayList<Point> points, int spokes, int numCircle){
		colors.add(new Vector3d(0,255,0)); //green - index 0
		colors.add(new Vector3d(255,255,0)); //yellow - index 1
		colors.add(new Vector3d(255,0,0)); //red - index 2		
		
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
		Vector3d nextColor = colors.get(1);
		radius = (length)*(1/numCircle);
		Circle prevCircle = new Circle(radius, center, spokes, points);
		for(int i = 1; i <= numCircle; i++) {
			radius = (length)*(i/numCircle);
			Circle tempCircle = new Circle(radius, center, spokes, points);
			for(int j = 0; j < tempCircle.ring.size(); j++) {
				tempCircle.ring.get(j).setRGB((int)nextColor.x, (int)nextColor.y, (int)nextColor.z);
			}
			nextColor = colors.get(i);
			
			for (Point p : points) { //for each point in mesh
				for (Point p2 : tempCircle.ring) { //for each point in ring
					if (p.x == p2.x && p.y == p2.y) { //if same x and y for both points
						p.setRGB(p2.getRGBAsArray()[0], p2.getRGBAsArray()[1], p2.getRGBAsArray()[2]); //set mesh point rgb to ring point rgb
					}
				}
			}
			fillCircle(points, tempCircle, prevCircle, numCircle, i, center);
			prevCircle = tempCircle;
		}
	}

	public static void fillCircle(ArrayList<Point> points, Circle ring1, Circle ring2, int numCircle, int count, Point center) {
		Point outerRing;
		Point innerRing;
		if(count == 1) { /* first ring set all points as base color*/
			for (Point p : points) {
				p.setRGB(ring1.ring.get(0).getRGBAsArray()[0], ring1.ring.get(0).getRGBAsArray()[1], ring1.ring.get(0).getRGBAsArray()[2]);
			}
		}
		else if(count == numCircle) { 													/*Last ring*/
			for (Point p : points) {
				if(pastRing(p,ring1.ring)) {
					p.setRGB(ring1.ring.get(0).getRGBAsArray()[0], ring1.ring.get(0).getRGBAsArray()[1], ring1.ring.get(0).getRGBAsArray()[2]);
				}
			}	
		}
		else {/*middle ring*/
			for (Point p : points) {
				outerRing = nearestNeighbor(p, ring1.ring, 1).get(0);
				innerRing = nearestNeighbor(p, ring2.ring, 1).get(0);
				if(betweenSameQuadrant(outerRing, innerRing, p)) {
					genColor(outerRing, innerRing, p);
				}
				else if(between(p, ring1.ring, ring2.ring)){
					genColor(outerRing, innerRing, p);
				}
			}			
		}
		ArrayList<Point> centerColor = nearestNeighbor(center, points, 25);
		for(Point p : centerColor) {
			p.setRGB(148,0,211);
		}
	}
	
	private static boolean pastRing(Point test, ArrayList<Point> ring) { 
		if((Math.abs(test.getX())>= Math.abs(nearestNeighbor(test, ring, 1).get(0).getX())) && (Math.abs(test.getY())>= Math.abs(nearestNeighbor(test, ring, 1).get(0).getY()))) {
			return true;
		}
		else {
			return false;
		}
	}
	
	private static boolean between(Point test, ArrayList<Point> outer, ArrayList<Point> inner){
		boolean outerCheck = false;
		boolean innerCheck = false;
		ArrayList<Point> closestOuter = closestTwo(test, outer, true);
		ArrayList<Point> closestInner = closestTwo(test, inner, false);
		ArrayList<Point> outerAxis = axisList(closestOuter.get(0),closestOuter.get(1));
		ArrayList<Point> innerAxis = axisList(closestInner.get(0),closestInner.get(1));
		for(Point p : outerAxis) {
			if(p.getX()==test.getX()) {
				if(p.getY() >= test.getY()) {
					outerCheck = true;
					break;
				}
			}
		}
		for(Point c : innerAxis) {
			if(c.getX()==test.getX()) {
				if(c.getY() <= test.getY()) {
					innerCheck = true;
					break;
				}
			}
		}
		if(innerCheck && outerCheck) {
			return true;
		}
		else {
			return false;
		}
	}
	/**
	 * returns all points between two given points
	 * @param point1 - the first point location
	 * @param point2 - the second point location
	 */
	private static ArrayList<Point> axisList(Point point1, Point point2){
	    double dist = .000001;
	    double distanceBetween = (Math.sqrt(Math.pow(point2.getX()-point1.getX(), 2) + Math.pow(point2.getY() - point1.getY(), 2) + Math.pow(point2.getZ() - point2.getZ(), 2)));
	    ArrayList<Point> points = new ArrayList<Point>();
	    for (int index = 0; dist < distanceBetween; index++){
	        points.get(index).setX(point1.getX() + dist * (Math.sqrt(Math.pow(point2.getX() - point1.getX(), 2))));
	        points.get(index).setY(point1.getY() + dist * (Math.sqrt(Math.pow(point2.getY() - point1.getY(), 2))));
	        points.get(index).setZ(point1.getZ() + dist * (Math.sqrt(Math.pow(point2.getZ() - point2.getZ(), 2))));
	        dist+=dist;
	    }
	    return points;
	}
	
	private static ArrayList<Point> closestTwo(Point Test, ArrayList<Point> list1, boolean outerRing) {
		ArrayList<Point> closest = new ArrayList<Point>();
		Algorithms.quicksortY(list1, 0, (list1.size()-1));
		ArrayList<Point> temp1 = new ArrayList<Point>();
    	Collections.addAll(temp1, (Point[]) list1.toArray());
    	int size = list1.size();
		if(outerRing) {
			if(list1.get(list1.size()/2).getY() <= Test.getY()) {
				for(int i = 0; i <= ((size/2)+2); i++) {
					temp1.remove(0);
				}
			}
			else {
				for(int i = (size-1); i >= ((size/2)-3); i--) {
					temp1.remove(i);
				}
			}
		}
		else {
			if(list1.get(list1.size()/2).getY() >= Test.getY()) {
				for(int i = 0; i <= ((size/2)+2); i++) {
					temp1.remove(0);
				}
			}
			else {
				for(int i = (size-1); i >= ((size/2)-3); i--) {
					temp1.remove(i);
				}
			}
		}
		closest.get(0).setX(list1.get(0).getX());
		closest.get(0).setY(list1.get(0).getY());
		closest.get(1).setX(list1.get(1).getX());
		closest.get(1).setY(list1.get(1).getY());
		for(Point p : temp1) {
			if((p.getX() >= closest.get(0).getX()) && (p.getX() <= Test.getX()) ) {
				closest.get(0).setX(p.getX());
				closest.get(0).setY(p.getY());
				closest.get(0).setZ(p.getZ());
			}
			else if((p.getX() <= closest.get(1).getX()) && (p.getX() >= Test.getX()) ) {
				closest.get(1).setX(p.getX());
				closest.get(1).setY(p.getY());
				closest.get(1).setZ(p.getZ());
			}
		}
		return closest;
	}
	
	
	private static boolean betweenSameQuadrant(Point outer, Point inner, Point Test) {
		if(quadrant(Test) == quadrant(outer) && quadrant(outer) == quadrant(inner)) {
			if((Math.abs(outer.getX())>=Math.abs(Test.getX())) && (Math.abs(inner.getX())<=Math.abs(Test.getX())) && (Math.abs(outer.getY())>=Math.abs(Test.getY())) && (Math.abs(inner.getY())<=Math.abs(Test.getY())) && (Math.abs(outer.getZ())>=Math.abs(Test.getZ())) && (Math.abs(inner.getZ())<=Math.abs(Test.getZ()))) {
				return true;
			}
			else {
				return false;
			}
		}
		else {
			return false;
		}
	}
	

	private static int quadrant(Point point) {
		int quadrant = 0;
		if((point.getX() > 0) && (point.getY() > 0) && (point.getZ() > 0)){
			quadrant = 1;
		}
		else if((point.getX() < 0) && (point.getY() > 0) && (point.getZ() > 0)){
			quadrant = 2;
		}
		else if((point.getX() < 0) && (point.getY() < 0) && (point.getZ() > 0)){
			quadrant = 3;
		}
		else if((point.getX() > 0) && (point.getY() < 0) && (point.getZ() > 0)){
			quadrant = 4;
		}
		else if((point.getX() > 0) && (point.getY() > 0) && (point.getZ() < 0)){
			quadrant = 5;
		}
		else if((point.getX() < 0) && (point.getY() > 0) && (point.getZ() < 0)){
			quadrant = 6;
		}
		else if((point.getX() < 0) && (point.getY() < 0) && (point.getZ() < 0)){
			quadrant = 7;
		}
		else if((point.getX() > 0) && (point.getY() < 0) && (point.getZ() < 0)){
			quadrant = 8;
		}
		else {
			quadrant = 0;
		}
		return quadrant;
	}
	
	private static void genColor(Point outer, Point inner, Point color) {
		double outerDistance = Math.sqrt((outer.getX()-color.getX())*(outer.getX()-color.getX())  +  (outer.getY()-color.getY())*(outer.getY()-color.getY()) + (outer.getZ()-color.getZ())*(outer.getZ()-color.getZ()));
		double innerDistance = Math.sqrt((color.getX()-outer.getX())*(color.getX()-outer.getX())  +  (color.getY()-outer.getY()*(color.getY())-outer.getY()) + (color.getZ()-outer.getZ())*(color.getZ()-outer.getZ()));
		double outerPercent = outerDistance/(innerDistance+outerDistance);
		double innerPercent = innerDistance/(innerDistance+outerDistance);
		color.setRGB((int)((outer.getRGBAsArray()[0]*outerPercent)+(inner.getRGBAsArray()[0]*innerPercent)), (int)((outer.getRGBAsArray()[1]*outerPercent)+(inner.getRGBAsArray()[1]*innerPercent)), (int)((outer.getRGBAsArray()[2]*outerPercent)+(inner.getRGBAsArray()[2]*innerPercent)));
	}
	
	public static ArrayList<Point> nearestNeighbor(Point point, ArrayList<Point> points, int numPoints) {
		boolean found = false;
    	boolean xCoords = true;
    	ArrayList<Point> temp1 = new ArrayList<Point>();
    	Collections.addAll(temp1, (Point[]) points.toArray());
		while(!found) {															/*If nearest neighbor is not found*/
	    	if(xCoords) {															/*If looking for x coords*/
	    		if(temp1.size() <= numPoints) {
	    			found = true;  
	    		}
	    		else if(temp1.size() % 2 == 1) {										/*If Odd*/
		    		if(point.getX() > temp1.get(temp1.size()/2 + 1).getX()) {				/*if looking for point x is larger*/
		    			for(int j = 0; j < (temp1.size()/2); j++) {									/*store bigger than median sorted by y in temp*/
		    				temp1.remove(0);
		    			}
		    			Algorithms.quicksortX(temp1, 0, (temp1.size()-1));
		    			/*sort*/
		    		}
		    		else if(point.getX() <= temp1.get(temp1.size()/2 + 1).getX()){		/*median smaller than x*/
		    			for(int j = temp1.size(); j > (temp1.size()/2); j--) {								/*store smaller than median sorted by y in temp*/
		    				temp1.remove(temp1.size() - 1);
		    			}
		    			Algorithms.quicksortX(temp1, 0, (temp1.size()-1));
		    			/*sort*/
		    		}
		    	}
		    	else {																/*even*/
		    		if(point.getX() > temp1.get(temp1.size()/2).getX()) {				/*x is bigger than median*/
		    			for(int j = 0; j < (temp1.size()); j++) {									/*store bigger than median sorted by y in temp*/
		    				temp1.remove(0);
		    			}
		    			Algorithms.quicksortX(temp1, 0, (temp1.size()-1));
		    			/*sort*/
		    		}
		    		else if(point.getX() <= temp1.get(temp1.size()/2).getX()) {	    	/*x is smaller than median*/
		    			for(int j = temp1.size(); j > (temp1.size()/2); j--) {								/*store smaller than median sorted by y in temp*/
		    				temp1.remove(temp1.size() - 1);
		    			}
		    			Algorithms.quicksortX(temp1, 0, (temp1.size()-1));
		    			/*sort*/
		    		}
	    		}
	    		xCoords = false;
	    	}
	    	else {																/*if y coords*/
	    		if(temp1.size() % 2 == 1) {											/*If Odd*/
		    		if(point.getY() > temp1.get(temp1.size()/2 + 1).getY()) {			/*if looking for point y is larger*/
		    			for(int j = 0; j < (temp1.size()); j++) {									/*store bigger than median sorted by x in temp*/
		    				temp1.remove(0);
		    			}
		    			Algorithms.quicksortY(temp1, 0, (temp1.size()-1));
		    			/*sort*/
		    		}
		    		else if(point.getY() <= temp1.get(temp1.size()/2 + 1).getY()) {	/*if looking for point y is smaller*/
		    			/*store smaller than median sorted by x in temp*/
		    			for(int j = temp1.size(); j > (temp1.size()/2); j--) {
		    				temp1.remove(temp1.size() - 1);
		    			}
		    			Algorithms.quicksortY(temp1, 0, (temp1.size()-1));
		    			/*sort*/
		    		}
		    	}
		    	else {																/*even*/
		    		if(point.getY() > temp1.get(temp1.size()/2).getY()) {				/*y is bigger than median*/
		    			for(int j = 0; j < (temp1.size()); j++) {									/*store bigger than median sorted by x in temp*/
		    				temp1.remove(0);
		    			}
		    			Algorithms.quicksortY(temp1, 0, (temp1.size()-1));
		    			/*sort*/
		    		}
		    		else if(point.getY() <= temp1.get(temp1.size()/2).getY()) {		/*y is smaller than median*/
		    			for(int j = temp1.size(); j > (temp1.size()/2); j--) {							/*store smaller than median sorted by x in temp*/
		    				temp1.remove(temp1.size() - 1);
		    			}
		    			Algorithms.quicksortY(temp1, 0, (temp1.size()-1));
		    			/*sort*/
		    		}
	    		}
	    		xCoords = true;									/*look for X coords on next loop*/
	    	}
    	}
	return temp1;
	}
}
