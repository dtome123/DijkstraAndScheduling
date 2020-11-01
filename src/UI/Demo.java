package UI;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;

import Bus.ExecuteDijkstra;
import DAO.Edge;
import DAO.Vertex;

public class Demo extends JPanel {



	Hashtable<Vertex, Boolean> isDestination = new Hashtable<>();
	List<Vertex> vertexs = new ArrayList<Vertex>();
	ExecuteDijkstra ex = new ExecuteDijkstra();
	Graphics2D g2d;
	
	private void init() {
		String flagId = "";
		
		vertexs = ex.getVertexs();
		vertexs.get(0).x=100;
		vertexs.get(0).y=100;
		int k=0;
		for(Vertex v :vertexs) {
			isDestination.put(v, false);
		}
		for (Edge e : ex.getEdges()){
			Vertex des = e.getDestination();
			isDestination.put(des, true);
		}
		//khoi tao diem source
		int xSou=100;
		int ySou=100;
		for (Edge e : ex.getEdges()) {
			Vertex sou = e.getSource();
			Vertex des = e.getDestination();
			if(isDestination.get(sou)==false) {
				sou.x=xSou;
				sou.y=ySou;
				xSou=xSou+200;
				
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
		for(Vertex v : vertexs) {
			System.out.println("Node "+v.getId()+": ("+v.x+","+v.y+")");
		}
	}
	private void drawNodes() {
		for(Vertex v : vertexs) {
			
			g2d.fillOval(v.x, v.y, 30, 30);
			g2d.setPaint(Color.white);
			g2d.drawString(v.getId(), v.x+10, v.y+20);
			g2d.setPaint(new Color(150, 150, 150));
		}
	}
	private void drawLines() {
		for (Edge e : ex.getEdges()) {
			// System.out.println(e.getSource()+"_"+e.getDestination());

			Vertex sou = e.getSource();
			Vertex des = e.getDestination();

			g2d.drawLine(sou.x + 10, sou.y + 20, des.x + 10, des.y + 20);
			g2d.drawString(String.valueOf(e.getWeight()), (sou.x + des.x) / 2 + 20, (sou.y + des.y) / 2 + 20);

		}

	}
	private void drawDijksta(int node_source,int node_destination) {
		
		LinkedList<Vertex> path = ex.handle(node_source, node_destination);
		for (int i = 1; i < path.size(); i++) {
			g2d.setPaint(Color.red);

			Vertex sou = path.get(i - 1);
			Vertex des = path.get(i);

			g2d.drawLine(sou.x + 10, sou.y + 20, des.x + 10, des.y + 20);
			g2d.fillOval(des.x + 8, des.y + 18, 5, 5);
		}
	}
	private void doDrawing(Graphics g) {
		g2d = (Graphics2D) g;
		g2d.setPaint(new Color(150, 150, 150));

		RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		rh.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

		g2d.setRenderingHints(rh);
		
		init();
		drawNodes();
		drawLines();
		drawDijksta(0, 7);
		
	}

	public Demo() {
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				
				repaint();
			}
		});
		addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				super.mouseDragged(e);
				
				repaint();
			}

		});
	}

	@Override
	public void paintComponent(Graphics g) {

		super.paintComponent(g);
		doDrawing(g);
	}
}
