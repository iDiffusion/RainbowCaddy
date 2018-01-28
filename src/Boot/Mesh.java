package Boot;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.lwjgl.util.vector.Vector3f;
/**
 * A mesh is a collection of points and faces to create a model to be displayed on the Display Frame.
 * @author Keegan Bruer
 *
 */
public class Mesh {
	public ArrayList<Point> points = new ArrayList<Point>();
	public ArrayList<Vector3f> normalizedPoints = new ArrayList<Vector3f>();
	ArrayList<Face> faces = new ArrayList<Face>();
	double minX = 0, minY = 0, maxX = 0, maxY = 0;
	/**
	 * Create an empty mesh
	 */
	public Mesh() {
		normalizePoints();
		findExtremes();
	}
	/**
	 * Create a mesh from existing point array
	 * @param points
	 */
	public Mesh(Point[] points) {
		this.points.addAll(Arrays.asList(points));
		if (faces.size() == 0) {
			createFaces();
		}
		findExtremes();
		normalizePoints();
	}
	/**
	 * Create a mesh from existing point ArrayList
	 * @param points
	 */
	public Mesh(ArrayList<Point> points) {
		this.points = points;
		if (faces.size() == 0) {
			createFaces();
		}
		findExtremes();
		normalizePoints();
	}
	
	/**
	 * Create a mesh from a obj file
	 * @param obj
	 */
	public Mesh(File obj) {
		try {
			readFromFile(obj);
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println("Could Not Read From File");
		}
		findExtremes();
		normalizePoints();
	}
	/**
	 * Use this method to generate faces from the meshes array of points.
	 * Useful for creating a model from a bunch of points.
	 * Not needed if mesh is loaded directly from a obj file.
	 * With the exception of if the mesh was edited this method needs to be called.
	 */
	public void createFaces() {
		faces = new ArrayList<Face>();
		ArrayList<Point> temp = points;
		Collections.sort(temp);
		int lastY = -10000;
		int rowSize = 0;
		for (Point p : temp) {
			if (lastY == -10000) {
				lastY = (int)p.getY();
			}
			if ((int)p.getY() == lastY) {
				rowSize++;
			}
		}
		for (int i = 0; i < temp.size()-rowSize-1; i+=2) {
			faces.add(new Face(temp.get(i), temp.get(i+1), temp.get(i+rowSize)));
			faces.add(new Face(temp.get(i+1), temp.get(i+rowSize), temp.get(i+rowSize+1)));
		}
	}
	
	public void moveMesh(double x, double y) {
		for(int i = 0; i < points.size(); i++) {
			points.get(i).setX(x + points.get(i).getX());
			points.get(i).setY(y + points.get(i).getY());
		}
	}
	
	
	
	private void readFromFile(File obj) throws Exception {
		//double largest = findLargest(obj);
		BufferedReader bufferedReader = new BufferedReader(new FileReader(obj));
		String line;
        while((line = bufferedReader.readLine()) != null) {
        	String[] array = line.split(" ");
        	if (array[0].equals("v")) {
        		Point p = new Point(Float.parseFloat(array[1]),
						Float.parseFloat(array[2]), Float.parseFloat(array[3]));
				points.add(p);
        	} else if (array[0].equals("f")) {
        		int x = Integer.parseInt(array[1].split("/")[0]);
        		int y = Integer.parseInt(array[2].split("/")[0]);
        		int z = Integer.parseInt(array[3].split("/")[0]);
        		faces.add(new Face(points.get(x-1), points.get(y-1), points.get(z-1)));
        	}
        }   
        // Always close files.
        bufferedReader.close();
    
	}
	
	
//	private double findLargest(File obj) throws Exception {
//		double largest = 0;
//		BufferedReader bufferedReader = new BufferedReader(new FileReader(obj));
//		String line;
//        while((line = bufferedReader.readLine()) != null) {
//        	String[] array = line.split(" ");
//        	if (array[0].equals("v")) {
//        		for (int i = 1; i < 4; i++) {
//	        		if (Double.parseDouble(array[i]) > largest) {
//	        			largest = Double.parseDouble(array[i]);
//	        		}
//        		}
//        	}
//        }
//        return largest;
//	}
	public static double map(double x, double in_min, double in_max, double out_min, double out_max) {
		return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
	}
	/**
	 * set the color of every point in the mesh.
	 * @param r - red
	 * @param g - green
	 * @param b - blue
	 */
	public void setColor(int r, int g, int b) {
		for (Point p : points) {
			p.setR(r);
			p.setG(g);
			p.setB(b);
		}
	}
	
	private void findExtremes() {
		for (Point p : points) {
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
	}
	
	private void normalizePoints() {
		for (Point p : this.points) {
			float x = (float)Boot.map(p.getX(), this.minX, this.maxX, -0.5, 0.5);
			float y = (float)Boot.map(p.getY(), this.minY, this.maxY, -0.5, 0.5);
			this.normalizedPoints.add(new Vector3f(x, y, 0));
		}
	}
}
