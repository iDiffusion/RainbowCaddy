/* Circle.java
 * Date Created: Dec 18, 2017
 * Author: Liam Marshall & Liam Marshall
 * Math related functions for Rainbow Caddy 
 * */

import java.util.ArrayList;

public class Circle {
	public ArrayList<Point> ring;
	public Circle(double radius, Point center, int numSpokes, ArrayList<Point> points) {
		ring = makeCircle(center,radius,numSpokes);
		assignHeights(ring,points);
	}
	
	/* Generate points along a set number of spokes for a circle of a given radius */
    public ArrayList<Point> makeCircle (Point cup, double radius, int numSpokes){
        double deltax,deltay; 
        double degCount=0;
        double degree = (float) 360/numSpokes;
        int count=0;
        ArrayList<Point> circle = new ArrayList<Point>();
        for(degCount=0; degCount<numSpokes; degCount+=degree){
            deltax = radius*Math.cos(degCount);
            deltay = radius*Math.sin(degCount);
            circle.get(count).setX(cup.getX() + deltax);
            circle.get(count).setY(cup.getY() + deltay);
            count++;
        }
        return circle;
    }
    
    /* returns a coefficient used when swaying the circle to generate a more intuitive heat-map */
    public double coeff(){
        return 1.0;
    }
    
    public void assignHeights(ArrayList<Point> ring, ArrayList<Point> points) {
    	boolean found = false;
    	boolean xCoords = true;
    	Point point;
    	int size;
    	for(int i = 0; i < ring.size(); i++) {										/*Nearest Neighbor*/
    		point = new Point(ring.get(i).getX(), ring.get(i).getY(), 0.0, 0, 0, 0);	
	    	ArrayList<Point> temp1 = points ;
	    	size = temp1.size();
	    	while(!found) {															/*If nearest neighbor is not found*/
		    	if(xCoords) {															/*If looking for x coords*/
		    		if(temp1.size() == 1) {
		    			found = true;  
		    		}
		    		else if(temp1.size() % 2 == 1) {										/*If Odd*/
			    		if(point.getX() > temp1.get(temp1.size()/2 + 1).getX()) {				/*if looking for point x is larger*/
			    			for(int j = 0; j < (size/2); j++) {									/*store bigger than median sorted by y in temp*/
			    				temp1.remove(0);
			    			}
			    			/*sort*/
			    		}
			    		else if(point.getX() < temp1.get(temp1.size()/2 + 1).getX()){		/*median smaller than x*/
			    			for(int j = size; j > (size/2); j--) {								/*store smaller than median sorted by y in temp*/
			    				temp1.remove(temp1.size() - 1);
			    			}
			    			/*sort*/
			    		}
			    		else {															
			    			found = true;
			    		}
			    	}
			    	else {																/*even*/
			    		if(point.getX() > temp1.get(temp1.size()/2).getX()) {				/*x is bigger than median*/
			    			for(int j = 0; j < (size); j++) {									/*store bigger than median sorted by y in temp*/
			    				temp1.remove(0);
			    			}
			    			/*sort*/
			    		}
			    		else if(point.getX() < temp1.get(temp1.size()/2).getX()) {	    	/*x is smaller than median*/
			    			for(int j = size; j > (size/2); j--) {								/*store smaller than median sorted by y in temp*/
			    				temp1.remove(temp1.size() - 1);
			    			}
			    			/*sort*/
			    		}
			    		else {															
			    			found = true;
			    		}
		    		}
		    		xCoords = false;
		    	}
		    	else {																/*if y coords*/
		    		if(temp1.size() % 2 == 1) {											/*If Odd*/
			    		if(point.getY() > temp1.get(temp1.size()/2 + 1).getY()) {			/*if looking for point y is larger*/
			    			for(int j = 0; j < (size); j++) {									/*store bigger than median sorted by x in temp*/
			    				temp1.remove(0);
			    			}
			    			/*sort*/
			    		}
			    		else if(point.getY() < temp1.get(temp1.size()/2 + 1).getY()) {	/*if looking for point y is smaller*/
			    			/*store smaller than median sorted by x in temp*/
			    			for(int j = size; j > (size/2); j--) {
			    				temp1.remove(temp1.size() - 1);
			    			}
			    			/*sort*/
			    		}
			    		else {															
			    			found = true;
			    		}
			    	}
			    	else {																/*even*/
			    		if(point.getY() > temp1.get(temp1.size()/2).getY()) {				/*y is bigger than median*/
			    			for(int j = 0; j < (size); j++) {									/*store bigger than median sorted by x in temp*/
			    				temp1.remove(0);
			    			}
			    			/*sort*/
			    		}
			    		else if(point.getY() < temp1.get(temp1.size()/2).getY()) {		/*y is smaller than median*/
			    			for(int j = size; j > (size/2); j--) {							/*store smaller than median sorted by x in temp*/
			    				temp1.remove(temp1.size() - 1);
			    			}
			    			/*sort*/
			    		}
			    		else {															
			    			found = true;
			    		}
		    		}
		    		xCoords = true;									/*look for X coords on next loop*/
		    	}
	    	}
	    	ring.get(i).setZ(temp1.get(0).getZ());					/*set z*/
    	}
    }
    
    
    /* sways the circle based on the change in elevation between the point on the spoke and the cup */
    public ArrayList<Point> pushCircle (ArrayList<Point> circle, Point cup, double radius){
        int count;
        double deltaz;
        int numSpokes = circle.size();
        for(count=0; count<numSpokes; count++){
            deltaz = circle.get(count).getZ() - cup.getZ();
            circle.get(count).setX(circle.get(count).getX() + deltaz * coeff() * ((circle.get(count).getX() - cup.getX()) / radius));
            circle.get(count).setY(circle.get(count).getY() + deltaz * coeff() * ((circle.get(count).getY() - cup.getY()) / radius));
        }
        return circle;
    }
}
