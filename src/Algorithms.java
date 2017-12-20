import java.util.ArrayList;

/* algorihms.java
 * Date Created: Dec 18, 2017
 * Author: Liam Marshall
 * Math related functions for Rainbow Caddy 
 * */
 
public class Algorithms {
    
    /* Generate points along a set number of spokes for a circle of a given radius */
    public static ArrayList<Point> makeCircle (Point cup, double radius, int numSpokes){
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
    public static double coeff(){
        return 1.0;
    }
    
    /* sways the circle based on the change in elevation between the point on the spoke and the cup */
    public static ArrayList<Point> pushCircle (ArrayList<Point> circle, Point cup, double radius){
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