import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import com.jogamp.opengl.GL2;
/**
 * A mesh is a collection of points and faces to create a model to be displayed on the Display Frame.
 * @author Keegan Bruer
 *
 */
public class Mesh {
	ArrayList<Point> points = new ArrayList<Point>();
	ArrayList<Face> faces = new ArrayList<Face>();
	double minX = 0.0, minY = 0, minZ = 0, maxX = 0, maxY = 0, maxZ = 0;
	
	/**
	 * Create an empty mesh
	 */
	public Mesh() {
		
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
	}
	
	/**
	 * Create a mesh from a obj file
	 * @param obj
	 */
	public Mesh(File obj) {
		try {
			readFromFile(obj);
			if (faces.size() == 0) {
				createFaces();
			}
			findExtremes();
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println("Could Not Read From File");
		}
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
				lastY = (int)p.y;
			}
			if ((int)p.y == lastY) {
				rowSize++;
			}
		}
		for (int i = 0; i < temp.size()-rowSize-1; i+=2) {
			faces.add(new Face(temp.get(i), temp.get(i+1), temp.get(i+rowSize)));
			faces.add(new Face(temp.get(i+1), temp.get(i+rowSize), temp.get(i+rowSize+1)));
		}
	}
	
	/**
	 * Display The Mesh But Rotate It.
	 * @param gl - pass the GL2
	 * @param amount - how much to rotate
	 * @param dir - direction of x, y, z char
	 */
	public void DisplayMeshRotated(GL2 gl, double amount, char dir) {
		switch (dir) {
		case 'x':
			gl.glRotated( amount, 1, 0, 0 ); //zoom based on w and s
			break;
		case 'y':
			gl.glRotated( amount, 0, 1, 0 ); //zoom based on w and s
			break;
		case 'z':
			gl.glRotated( amount, 1, 0, 0 ); //zoom based on w and s
			break;
		}
	    DisplayMesh(gl);
	}
	
	/**
	 * display Mesh without rotating it.
	 * @param gl
	 */
	public void DisplayMesh(GL2 gl) {
		gl.glCullFace(GL2.GL_CULL_FACE);
		for (Face face : faces) {
			gl.glBegin(GL2.GL_TRIANGLES);
			gl.glColor3d(face.points[0].r, face.points[0].g, face.points[0].b);
			gl.glVertex3d(face.points[0].x, face.points[0].y, face.points[0].z);
			
			gl.glColor3d(face.points[1].r, face.points[1].g, face.points[0].b);
			gl.glVertex3d(face.points[1].x, face.points[1].y, face.points[1].z);
			
			gl.glColor3d(face.points[2].r, face.points[2].g, face.points[2].b);
			gl.glVertex3d(face.points[2].x, face.points[2].y, face.points[2].z);
			gl.glEnd();
		}
	}
	
	/**
	 * Center the mesh on a grid. make the center point of the mesh 0,0 on a plain.
	 */
	public void centerMesh() {
		double minX = 0, maxX = 0;
		double minY = 0, maxY = 0;
		double minZ = 0, maxZ = 0;
		for (Point p : points) {
			if (p.x > maxX) {
				maxX = p.x;
			}
			if (p.x < minX) {
				minX = p.x;
			}
			if (p.y > maxY) {
				maxY = p.y;
			}
			if (p.y < minY) {
				minY = p.y;
			}
			if (p.z > maxZ) {
				maxZ = p.z;
			}
			if (p.z < minZ) {
				minZ = p.z;
			}
		}
		System.out.println("Minimum X: "+ minX +"   Maximum X: "+ maxX +"\n");
		System.out.println("Minimum Y: "+ minY +"   Maximum Y: "+ maxY +"\n");
		System.out.println("Minimum Z: "+ minZ +"   Maximum Z: "+ maxZ +"\n");
		
		Point center = new Point(minX + ((maxX-minX)/2), minY + ((maxY-minY)/2), minZ + ((maxZ-minZ)/2));
		for(Point p : points) {
			p.x -= center.x;
			p.y -= center.y;
			p.z -= center.z;
		}
		//--------Rotate to be alligned with grid
		
		createFaces();
		
	}
	
	private void readFromFile(File obj) throws Exception {
		//double largest = findLargest(obj);
		BufferedReader bufferedReader = new BufferedReader(new FileReader(obj));
		String line;
        while((line = bufferedReader.readLine()) != null) {
        	String[] array = line.split(" ");
        	if (array[0].equals("v")) {
//        		double x = map(Double.parseDouble(array[1]), 0, largest, 0, 1);
//        		double y = map(Double.parseDouble(array[2]), 0, largest, 0, 1);
//        		double z = map(Double.parseDouble(array[3]), 0, largest, 0, 1);
        		double x = Double.parseDouble(array[1]);
        		double y = Double.parseDouble(array[2]);
        		double z = Double.parseDouble(array[3]);
        		points.add(new Point(x, y, z));
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
			p.r = r;
			p.g = g;
			p.b = b;
		}
	}
	
	private void findExtremes() {
		for (Point p : points) {
			if (p.x > maxX) {
				maxX = p.x;
			}
			if (p.x < minX) {
				minX = p.x;
			}
			if (p.y > maxY) {
				maxY = p.y;
			}
			if (p.y < minY) {
				minY = p.y;
			}
			if (p.z > maxZ) {
				maxZ = p.z;
			}
			if (p.z < minZ) {
				minZ = p.z;
			}
		}
		System.out.println("Minimum X: "+ minX +"   Maximum X: "+ maxX +"\n");
		System.out.println("Minimum Y: "+ minY +"   Maximum Y: "+ maxY +"\n");
		System.out.println("Minimum Z: "+ minZ +"   Maximum Z: "+ maxZ +"\n");
	}
}
