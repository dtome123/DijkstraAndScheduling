package Bus;

import java.util.LinkedList;

import DAO.Vertex;

public class Main {

	public static void main(String[] args) {
		ExecuteDijkstra ex = new ExecuteDijkstra();
		LinkedList<Vertex> nodes = ex.handle(3, 10);
		for(Vertex v: nodes) {
			System.out.println(v);
		}
	}

}
