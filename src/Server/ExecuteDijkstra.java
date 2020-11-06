package Server;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import Server.DAO.Edge;
import Server.DAO.Graph;
import Server.DAO.Vertex;

public class ExecuteDijkstra {

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
	public ExecuteDijkstra() {
		init();
	}
	
    private void init() {
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
    public LinkedList<Vertex> handle(int node_begin,int node_end) {
        
        // Lets check from location Loc_1 to Loc_10
        Graph graph = new Graph(vertexs, edges);
        DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(graph);
        dijkstra.execute(vertexs.get(node_begin));
        LinkedList<Vertex> path = dijkstra.getPath(vertexs.get(node_end));
		/*
		 * for (Vertex vertex : path) { System.out.println(vertex); }
		 */
        return path;

    }
    private void addLane( int sourceLocNo, int destLocNo,int duration) {
        Edge lane = new Edge(vertexs.get(sourceLocNo), vertexs.get(destLocNo), duration );
        edges.add(lane);
    }

}
