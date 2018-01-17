package GLEngine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import Boot.Mesh;
import Models.RawModel;

public class OBJLoader {
	private float[] verticesArray = null;
	private float[] normalsArray = null;
	private float[] textureArray = null;
	private int[] indicesArray = null;
	private RawModel model = null;
	
	public OBJLoader(String fileName, Loader loader, Mesh mesh) {
		model = loadObjModel(fileName, loader, mesh);
	}


	private RawModel loadObjModel(String fileName, Loader loader, Mesh mesh) {
		FileReader fr = null;
		try {
			fr = new FileReader(new File("res/model/" + fileName + ".obj"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		BufferedReader reader = new BufferedReader(fr);
		String line;
		List<Vector3f> vertices = new ArrayList<Vector3f>();
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
		
		int vertexPointer = 0;
		for (Vector3f vertex : vertices) {
			verticesArray[vertexPointer++] = vertex.x;
			
			verticesArray[vertexPointer++] = vertex.y;
			verticesArray[vertexPointer++] = vertex.z;
		}
		
		for (int i = 0; i < indices.size(); i++) {
			indicesArray[i] = indices.get(i);
		}
		
		float[] colorArray = new float[mesh.points.size()];
		for (int i = 0; i < colorArray.length; i += 3) {
			colorArray[i] = (float) Boot.Boot.map(mesh.points.get(i).r, 0, 255, 0, 1);
			colorArray[i+1] = (float) Boot.Boot.map(mesh.points.get(i+1).g, 0, 255, 0, 1);
			colorArray[i+2] = (float) Boot.Boot.map(mesh.points.get(i+2).b, 0, 255, 0, 1);
		}
		
		return loader.loadToVao(verticesArray, textureArray, normalsArray, indicesArray, colorArray);
	}
	
	public RawModel updateColors(Mesh mesh, Loader loader) {
		float[] colorArray = new float[mesh.points.size()];
		for (int i = 0; i < colorArray.length; i += 3) {
			colorArray[i] = (float) Boot.Boot.map(mesh.points.get(i).r, 0, 255, 0, 1);
			colorArray[i+1] = (float) Boot.Boot.map(mesh.points.get(i+1).g, 0, 255, 0, 1);
			colorArray[i+2] = (float) Boot.Boot.map(mesh.points.get(i+2).b, 0, 255, 0, 1);
		}
		return loader.loadToVao(verticesArray, textureArray, normalsArray, indicesArray, colorArray);
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
