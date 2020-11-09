package Server;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import Server.DAO.Edge;
import Server.DAO.Graph;
import Server.DAO.Vertex;

public class ExecuteDijkstra {

    private List<Vertex> nodes=new ArrayList<Vertex>();
    private List<Edge> edges= new ArrayList<Edge>();
    public ExecuteDijkstra() {
    	
    }
    
    public ExecuteDijkstra(List<Vertex> nodes,List<Edge> edges) {
    	this.nodes = nodes;
    	this.edges=edges;
        
//        for (int i = 0; i < 11; i++) {
//            Vertex location = new Vertex( i+"");
//            nodes.add(location);
//        }
//        
//        addLane( 0, 1, 85);
//        addLane( 0, 2, 217);
//        addLane(0, 4, 173);
//        addLane( 2, 6, 186);
//        addLane(2, 7, 103);
//        addLane( 3, 7, 183);
//        addLane( 5, 8, 250);
//        addLane( 8, 9, 84);
//        addLane(7, 9, 167);
//        addLane(4, 9, 502);
//        addLane( 9, 10, 40);
//        addLane(1, 10, 600);

    }
    public LinkedList<Vertex> Execute(int source, int destination) {
        
        // Lets check from location Loc_1 to Loc_10
        Graph graph = new Graph(nodes, edges);
        DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(graph);
        dijkstra.execute(nodes.get(source));
        LinkedList<Vertex> path = dijkstra.getPath(nodes.get(destination));
//        for (Vertex vertex : path) {
//            System.out.println(vertex);
//        }
        return path;

    }

    private void addLane( int sourceLocNo, int destLocNo,
            int duration) {
    	
        Edge lane = new Edge(nodes.get(sourceLocNo), nodes.get(destLocNo), duration );
        edges.add(lane);
    }

	
}