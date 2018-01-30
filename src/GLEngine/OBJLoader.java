package GLEngine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import Boot.Boot;
import Boot.Error_Popup;
import Boot.Mesh;
import Models.RawModel;

public class OBJLoader {
	private List<Vector3f> vertices = new ArrayList<Vector3f>();
	private float[] verticesArray = null;
	private float[] normalsArray = null;
	private float[] textureArray = null;
	private int[] indicesArray = null;
	private float[] colorArray = null;
	private RawModel model = null;
	
	public OBJLoader(String fileName, Loader loader, Mesh mesh, Error_Popup pop) {
		String fileType = "";

		int i = fileName.lastIndexOf('.');
		if (i > 0) {
			fileType = fileName.substring(i+1);
		}
		if ("obj".equals(fileType)) {
			model = loadModelFromObjFile(fileName, loader, mesh, pop);
		} else {
			System.out.println("File Type Invalid");
		}
	}


	private RawModel loadModelFromObjFile(String fileName, Loader loader, Mesh mesh, Error_Popup pop) {
		FileReader fr = null;
		try {
			fr = new FileReader(new File(fileName));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		BufferedReader reader = new BufferedReader(fr);
		String line;
		List<Vector2f> textures = new ArrayList<Vector2f>();
		List<Vector3f> normals = new ArrayList<Vector3f>();
		List<Integer> indices = new ArrayList<Integer>();
		
		try {
			while (true) {
				line = reader.readLine();
				String[] currentLine = line.split(" ");
				if (line.startsWith("v ")) {
					Vector3f vertex = new Vector3f(Float.parseFloat(currentLine[1]),
							Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3]));
					vertices.add(vertex);
				} else if (line.startsWith("vt ")) {
					Vector2f texture = new Vector2f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]));
					textures.add(texture);
				} else if (line.startsWith("vn ")) {
					Vector3f normal = new Vector3f(Float.parseFloat(currentLine[1]),
							Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3]));
					normals.add(normal);
				} else if (line.startsWith("f ")) {
					textureArray = new float[vertices.size() * 2];
					normalsArray = new float[vertices.size() * 3];
					break;
				}
			}
			while (line!=null) {
				if(!line.startsWith("f ")) {
					line = reader.readLine();
					continue;
				}
				String[] currentLine = line.split(" ");
				String[] vertex1 = currentLine[1].split("/");
				String[] vertex2 = currentLine[2].split("/");
				String[] vertex3 = currentLine[3].split("/");
				
				processVertex(vertex1, indices, textures, normals, textureArray, normalsArray);
				processVertex(vertex2, indices, textures, normals, textureArray, normalsArray);
				processVertex(vertex3, indices, textures, normals, textureArray, normalsArray);
				line = reader.readLine();
			}
			reader.close();
		}catch (Exception e) {
			
		}
		verticesArray = new float[vertices.size() * 3];
		indicesArray = new int[indices.size()];
		colorArray = new float[verticesArray.length];
		
		int vertexPointer = 0;
		for (Vector3f vertex : vertices) {
			verticesArray[vertexPointer++] = vertex.x;
			verticesArray[vertexPointer++] = vertex.y;
			verticesArray[vertexPointer++] = vertex.z;
		}
		
		for (int i = 0; i < indices.size(); i++) {
			indicesArray[i] = indices.get(i);
		}
		
		System.out.println("Loading Original Colors");
		pop.appendText("\n - Loading Original Colors");
		int colorIndex = 0;
		for (int i = 0; i < verticesArray.length; i+=3) {
			colorArray[i] = (float)Boot.map(mesh.points.get(colorIndex).getR(), 0, 255, 0, 1);
			colorArray[i+1] = (float)Boot.map(mesh.points.get(colorIndex).getG(), 0, 255, 0, 1);
			colorArray[i+2] = (float)Boot.map(mesh.points.get(colorIndex).getB(), 0, 255, 0, 1);
			colorIndex++;
		}
		pop.appendText("\n - Finished Loading Original Colors");
		return loader.loadToVao(verticesArray, textureArray, normalsArray, indicesArray, colorArray, vertices);
	}
	
	public RawModel updateColors(Mesh mesh, Loader loader, Error_Popup pop) {
		System.out.println("Loading New Colors");
		pop.appendText("\n - Loading New Colors");
		int colorIndex = 0;
		for (int i = 0; i < verticesArray.length; i+=3) {
			colorArray[i] = (float)Boot.map(mesh.points.get(colorIndex).getR(), 0, 255, 0, 1);
			colorArray[i+1] = (float)Boot.map(mesh.points.get(colorIndex).getG(), 0, 255, 0, 1);
			colorArray[i+2] = (float)Boot.map(mesh.points.get(colorIndex).getB(), 0, 255, 0, 1);
			colorIndex++;
		}		
		return loader.loadToVao(verticesArray, textureArray, normalsArray, indicesArray, colorArray, vertices);
	}
	
	private void processVertex(String[] vertexData, List<Integer> indices,
			List<Vector2f> textures, List<Vector3f> normals, float[] textureArray, 
			float[] normalsArray) {
		int currentVertexPointer = Integer.parseInt(vertexData[0]) -1;
		indices.add(currentVertexPointer);
		Vector2f currentTex = textures.get(Integer.parseInt(vertexData[1])-1);
		textureArray[currentVertexPointer * 2] = currentTex.x;
		textureArray[currentVertexPointer * 2 + 1] = 1 - currentTex.y;
		Vector3f currentNorm = normals.get(Integer.parseInt(vertexData[2]) -1);
		normalsArray[currentVertexPointer * 3] = currentNorm.x;
		normalsArray[currentVertexPointer * 3 + 1] = currentNorm.y;
		normalsArray[currentVertexPointer * 3 + 2] = currentNorm.z;
		
	}
	
	public RawModel getModel() {
		return model;
	}
}
