package Client.Controler;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

import javax.swing.JOptionPane;

import Server.DAO.Edge;
import Server.DAO.Vertex;

public class InitData {
	// du liệu chính thức
	private List<Vertex> vertexs;
	private List<Edge> edges;
	// du liệu tạm để kiểm tra trước khi gán dữ liệu
	private List<Vertex> tmpvertexs;
	private List<Edge> tmpedges;
	private int sl;// số lượng đỉnh

	Hashtable<Vertex, Boolean> isDestination = new Hashtable<>();
	private String fileName = "src\\Client\\data.txt";

	public InitData() {

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

	public void ShowMessage(String message) {
		JOptionPane.showMessageDialog(null, message);
	}

	public boolean check(int linenumber, int source, int destination, int weight) {
		if (source == destination) {
			ShowMessage("Dòng " + linenumber + ": Nguồn và đich không được trùng nhau");
			return false;
		}
		if (source < 0 || source >= sl || destination < 0 || destination >= sl) {
			ShowMessage("Dòng " + linenumber + ": Nút nguồn và đích từ 0 đến " + (sl - 1));
			return false;
		}
		if (weight == 0) {
			ShowMessage("Trọng số khác 0");
			return false;
		}

		return true;
	}

	public boolean readFile(File file) {
		tmpvertexs = new ArrayList<Vertex>();
		tmpedges = new ArrayList<Edge>();
		int flag = 1;
		Scanner sc;
		try {
			sc = new Scanner(file);
			sl = Integer.parseInt(sc.nextLine());

			for (int i = 0; i < sl; i++) {
				Vertex location = new Vertex(i + "");
				tmpvertexs.add(location);
			}
			int linenumber = 1;
			while (sc.hasNextLine()) {
				String t[] = sc.nextLine().split(" ");

				int soucre = Integer.parseInt(t[0]);
				int destination = Integer.parseInt(t[1]);
				int weight = Integer.parseInt(t[2]);

				if (check(linenumber, soucre, destination, weight)) {
					addLaneTmp(Integer.parseInt(t[0]), Integer.parseInt(t[1]), Integer.parseInt(t[2]));
					linenumber++;
				} else {
					flag = 0;
					break;
				}
			}

		} catch (FileNotFoundException ex) {
			System.err.println(ex);
		}

		if (flag == 1) {
			vertexs = new ArrayList<Vertex>();
			edges = new ArrayList<Edge>();
			convert();
			initCoordinate();
			return true;
		} else {
			return false;
		}
	}

	private void convert() {
		for (Vertex i : tmpvertexs) {
			Vertex n = new Vertex(i.getId());
			vertexs.add(n);
		}
		for (Edge e : tmpedges) {
			int s = Integer.parseInt(e.getSource().getId());
			int d = Integer.parseInt(e.getDestination().getId());
			addLaneMain(s, d, e.getWeight());
		}
		tmpvertexs.clear();
		tmpedges.clear();
	}

	private void initCoordinate() {
		String flagId = "";
		vertexs.get(0).x = 100;
		vertexs.get(0).y = 100;
		int k = 0;
		for (Vertex v : vertexs) {
			isDestination.put(v, false);
		}
		for (Edge e : edges) {
			Vertex des = e.getDestination();
			isDestination.put(des, true);
		}
		int xSou = 100;
		int ySou = 100;
		for (Edge e : edges) {
			Vertex sou = e.getSource();
			Vertex des = e.getDestination();
			if (isDestination.get(sou) == false) {
				sou.x = xSou;
				sou.y = ySou;
				xSou = xSou + 100;
			}
			if (flagId.equals(sou.getId())) {
				k += 50;
			} else {
				k = 0;
			}
			des.x = sou.x + k;
			des.y = sou.y + 100 + k + 10;
			flagId = sou.getId();

		}
	}

	private void addLaneTmp(int sourceLocNo, int destLocNo, int duration) {
		Edge lane = new Edge(tmpvertexs.get(sourceLocNo), tmpvertexs.get(destLocNo), duration);
		tmpedges.add(lane);
	}

	private void addLaneMain(int sourceLocNo, int destLocNo, int duration) {
		Edge lane = new Edge(vertexs.get(sourceLocNo), vertexs.get(destLocNo), duration);
		edges.add(lane);
	}

}
