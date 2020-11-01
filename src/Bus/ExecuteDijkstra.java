package Bus;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import DAO.Edge;
import DAO.Graph;
import DAO.Vertex;

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
            Vertex location = new Vertex( i+"", i+"");
            vertexs.add(location);
        }
    	addLane("Edge_0", 0, 1, 85);
        addLane("Edge_1", 0, 2, 217);
        addLane("Edge_2", 0, 4, 173);
        addLane("Edge_11",1, 10, 600);
        addLane("Edge_3", 2, 6, 186);
        addLane("Edge_4", 2, 7, 103);
        addLane("Edge_5", 3, 7, 183);
        addLane("Edge_9", 4, 9, 502);
        addLane("Edge_6", 5, 8, 250);
        addLane("Edge_8", 7, 9, 167);
        addLane("Edge_7", 8, 9, 84);
        addLane("Edge_10", 9, 10, 40);
        

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
    private void addLane(String laneId, int sourceLocNo, int destLocNo,int duration) {
        Edge lane = new Edge(laneId,vertexs.get(sourceLocNo), vertexs.get(destLocNo), duration );
        edges.add(lane);
    }

}
