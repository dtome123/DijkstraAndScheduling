package Client;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

import Server.DAO.Edge;
import Server.DAO.Vertex;

public class InitData {
	private List<Vertex> vertexs;
	private List<Edge> edges;
	Hashtable<Vertex, Boolean> isDestination = new Hashtable<>();
	private String fileName = "src\\Client\\data.txt";
	public InitData() {
		readFile();
		initCoordinate();
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
	private void initCoordinate() {
		String flagId = "";
		vertexs.get(0).x=100;
		vertexs.get(0).y=100;
		int k=0;
		for(Vertex v :vertexs) {
			isDestination.put(v, false);
		}
		for (Edge e : edges){
			Vertex des = e.getDestination();
			isDestination.put(des, true);
		}
		int xSou=100;
		int ySou=100;
		for (Edge e : edges) {
			Vertex sou = e.getSource();
			Vertex des = e.getDestination();
			if(isDestination.get(sou)==false) {
				sou.x=xSou;
				sou.y=ySou;
				xSou=xSou+100;
			}
			if(flagId.equals(sou.getId())) {
				k+=50;
			}
			else {
				k=0;
			}
			des.x=sou.x+k;
			des.y=sou.y+100+k+10;
			flagId=sou.getId();
			
		}
	}
	private void addLane( int sourceLocNo, int destLocNo,int duration) {
        Edge lane = new Edge(vertexs.get(sourceLocNo), vertexs.get(destLocNo), duration );
        edges.add(lane);
    }
	
	

}
