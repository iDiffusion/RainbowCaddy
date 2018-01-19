package Boot;

import java.util.ArrayList;

/**
 * @author Tylon Lee
 *
 */
public class Test {

	public static ArrayList<Point> ring;
	
//  public static void main(String args[]) {
//  	newList = new ArrayList<Point>();
//  	double tempX, tempY, tempZ;
//  	for(int i= 0; i < 20; i++) {
//  		tempX = Math.random()*5 + Math.random();
//  		tempY = Math.random()*7 + Math.random();
//  		tempZ = Math.random()*9 + Math.random();
//  		Point newPoint = new Point(tempX, tempY, tempZ);
//  		newList.add(newPoint);
//  	}
//  	System.out.println("Finished");
//  }
	
  public static void main(String args[]) {
  	ring = new ArrayList<Point>();
//  	Point center = new Point(0,0,0);
//  	makeCircle(center, 1.0, 8);
//  	pushCircle(center, 1.0, new ArrayList<Point>());
//  	EXCH(ring.get(0), ring.get(1));
//  	ring = narrowListC(ring, 2, 0, 0.5);
//  	for(int i = 0; i < 8; i++) {
//  		if(i < 4)
//  			ring.get(i).setRGB(255, 255, 0);
//  		else
//  			ring.get(i).setRGB(0, 0, 255);
//  	}
//  	genColor(ring.get(0), ring.get(4), center);
  	
  	Point test1 = new Point(0.001, 0, 0);
  	Point test2 = new Point(0.002, 0, 0);
  	ring=axisList(test1, test2);
  	System.out.println("Finished");
  }
  
/////////////////////////////////////////////////////////////////////////////////////////
  
  private static ArrayList<Point> axisList(Point point1, Point point2){
	    double dist = .000001;
	    double distanceBetween = (Math.sqrt(Math.pow(point2.getX()-point1.getX(), 2) + Math.pow(point2.getY() - point1.getY(), 2) + Math.pow(point2.getZ() - point2.getZ(), 2)));
	    ArrayList<Point> points = new ArrayList<Point>();
	    while(dist < distanceBetween) {
	    	Point temp = new Point(0, 0, 0);
	        temp.setX(point1.getX() + dist * (Math.sqrt(Math.pow(point2.getX() - point1.getX(), 2))));
	        temp.setY(point1.getY() + dist * (Math.sqrt(Math.pow(point2.getY() - point1.getY(), 2))));
	        temp.setZ(point1.getZ() + dist * (Math.sqrt(Math.pow(point2.getZ() - point2.getZ(), 2))));
	        dist+=0.000001;
	        points.add(temp);
	    }
	    return points;
	}
  
@SuppressWarnings("unused")
private static void genColor(Point outer, Point inner, Point color) {
		double outerDistance = Math.sqrt((outer.getX()-color.getX())*(outer.getX()-color.getX())  +  (outer.getY()-color.getY())*(outer.getY()-color.getY()) + (outer.getZ()-color.getZ())*(outer.getZ()-color.getZ()));
		double innerDistance = Math.sqrt((color.getX()-outer.getX())*(color.getX()-outer.getX())  +  (color.getY()-outer.getY()*(color.getY())-outer.getY()) + (color.getZ()-outer.getZ())*(color.getZ()-outer.getZ()));
		double outerPercent = outerDistance/(innerDistance+outerDistance);
		double innerPercent = innerDistance/(innerDistance+outerDistance);
		color.setRGB((int)((outer.getRGBAsArray()[0]*outerPercent)+(inner.getRGBAsArray()[0]*innerPercent)), (int)((outer.getRGBAsArray()[1]*outerPercent)+(inner.getRGBAsArray()[1]*innerPercent)), (int)((outer.getRGBAsArray()[2]*outerPercent)+(inner.getRGBAsArray()[2]*innerPercent)));
	}
  
  //TESTED-GOOD
  public static ArrayList<Point> narrowListC(ArrayList<Point> points, double x, double y, double accuracy){
		double left = Math.floor(x) - Math.abs(accuracy);
		double right = Math.ceil(x) + Math.abs(accuracy);
		double up = Math.ceil(y) + Math.abs(accuracy);
		double down = Math.floor(y) - Math.abs(accuracy);
		
		ArrayList<Point> newList = new ArrayList<Point>();
		
		for(Point p : points) {
			if((p.getX() > left && p.getX() < right) && (p.getY() > down && p.getY() < up)) {
				newList.add(p);
			}
			else {
				continue;
			}
		}
		while(newList.isEmpty()) {
			newList = narrowListC(points, x, y, ++accuracy);
		}
		return newList;
	}
  
  //TESTED-GOOD
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
  
  //TESTED-GOOD
  public static void makeCircle (Point center, double radius, int numSpokes){
      double deltax,deltay; 
      double degCount=0;
      double degree = (float) 360/numSpokes;
      ring = new ArrayList<Point>();
      for(degCount=0; degCount<360; degCount+=degree){
          deltax = radius*Math.cos(Math.toRadians(degCount));
          deltay = radius*Math.sin(Math.toRadians(degCount));
          Point tempPoint = new Point(center.getX() + deltax,center.getY() + deltay, 0.0);
          ring.add(tempPoint);
      }
  }
  
  //TESTED-GOOD
  public static void pushCircle (Point center, double radius, ArrayList<Point> points){
      int count;
      double deltaz;
      int numSpokes = ring.size();
      for(count=0; count<numSpokes; count++){
          deltaz = center.getZ() - ring.get(count).getZ();
          ring.get(count).setX(ring.get(count).getX() + deltaz * coeff() * ((ring.get(count).getX() - center.getX()) / radius));
          ring.get(count).setY(ring.get(count).getY() + deltaz * coeff() * ((ring.get(count).getY() - center.getY()) / radius));
      }
      //assignHeights(points);
  }
  
  //TESTED-GOOD
  public static double coeff(){
      return 1.0;
  }
}
