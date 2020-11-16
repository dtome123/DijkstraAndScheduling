package Server.DAO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DataObject implements Serializable{
	private List<Vertex> vertexs = new ArrayList<Vertex>();
	private List<Edge> edges = new ArrayList<Edge>();
	
	public DataObject(List<Vertex> list,List<Edge> list2) {
		this.vertexs=list;
		this.edges = list2;
	}

	public List<Vertex> getVertexs() {
		return vertexs;
	}

	public void setVertexs(List<Vertex> vertexs) {
		this.vertexs = vertexs;
	}

	public List<Edge> getEdges() {
		return edges;
	}

	public void setEdges(List<Edge> edges) {
		this.edges = edges;
	}

}
