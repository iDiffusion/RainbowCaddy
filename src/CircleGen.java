import java.util.ArrayList;
import javax.vecmath.Vector3d;


public class CircleGen {
	public ArrayList<Vector3d> colors  = new ArrayList<Vector3d>();
	public CircleGen(Point center, ArrayList<Point> points, int spokes, int numCircle, Mesh mesh){
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
		for(int i = 0; i < numCircle; i++) {
			radius = (length)*(i/numCircle);
			Circle tempCircle = new Circle(radius, center, spokes, points);
			for(int j = 0; j < tempCircle.ring.size(); j++) {
				tempCircle.ring.get(j).setRGB((int)nextColor.x, (int)nextColor.y, (int)nextColor.z);
			}
			nextColor = colors.get(i);
			
			for (Point p : mesh.points) { //for each point in mesh
				for (Point p2 : tempCircle.ring) { //for each point in ring
					if (p.x == p2.x && p.y == p2.y) { //if same x and y for both points
						p.setRGB(p2.getRGBAsArray()[0], p2.getRGBAsArray()[1], p2.getRGBAsArray()[2]); //set mesh point rgb to ring point rgb
					}
				}
			}
		}
	}
}
