package Client;

import java.util.ArrayList;
import java.util.List;

import Server.DAO.Edge;
import Server.DAO.Vertex;

public class InitData {
	private List<Vertex> vertexs;
	private List<Edge> edges;
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
	public void readFile() {
		vertexs = new ArrayList<Vertex>();
        edges = new ArrayList<Edge>();
    	for (int i = 0; i < 11; i++) {
            Vertex location = new Vertex( i+"");
            vertexs.add(location);
        }
    	addLane( 0, 1, 85);
        addLane( 0, 2, 217);
        addLane( 0, 4, 173);
        addLane( 1, 10,600);
        addLane( 2, 6, 186);
        addLane( 2, 7, 103);
        addLane( 3, 7, 183);
        addLane( 4, 9, 502);
        addLane( 5, 8, 250);
        addLane( 7, 9, 167);
        addLane( 8, 9, 84);
        addLane( 9, 10, 40);
	}
	private void addLane( int sourceLocNo, int destLocNo,int duration) {
        Edge lane = new Edge(vertexs.get(sourceLocNo), vertexs.get(destLocNo), duration );
        edges.add(lane);
    }
	
	

}
