package Client.UI;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.xml.ws.handler.MessageContext;

import Client.Controler.InitData;
import Client.DAO.Edge;
import Client.DAO.Vertex;
import Server.ExecuteDijkstra;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.lang.Object;

public class Panneau extends JPanel {

	private int radius = 25;
	private String text = "stack";
	// bien graphic
	private List<Ellipse2D> nodes;
	private Ellipse2D dragged;
	private Point offset;
	private Vertex vertexCurrent;
	Graphics2D g2d;
	
	
	// attribute
	public boolean isDirectional;
	Hashtable<Ellipse2D, Vertex> nodeToVertex = new Hashtable<>();
	Hashtable<Vertex, Ellipse2D> vertexToNode = new Hashtable<>();
	Hashtable<Ellipse2D, Boolean> isPath = new Hashtable<Ellipse2D, Boolean>();

	private List<Vertex> vertexs = new ArrayList<Vertex>();
	private List<Edge> edges = new ArrayList<Edge>();
	private LinkedList<String> path = null;

	public LinkedList<String> getPath() {
		return path;
	}

	public void setPath(LinkedList<String> path) {
		this.path = path;
	}

	public void initData(List<Vertex> vertexs, List<Edge> edges) {

		this.vertexs.clear();
		this.edges.clear();
		nodeToVertex.clear();
		vertexToNode.clear();
		this.vertexs = vertexs;
		this.edges = edges;

		System.out.println(vertexs);
		System.out.println(edges);
		
		isDirectional = true;

		nodes = new ArrayList<>(25);

		for (Vertex v : vertexs) {
			Ellipse2D.Float temp = new Ellipse2D.Float(v.x - (radius / 2), v.y - (radius / 2), radius, radius);
			nodes.add(temp);
			nodeToVertex.put(temp, v);
			vertexToNode.put(v, temp);
		}

	}

	public Panneau() {

		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {

				for (Ellipse2D node : nodes) {

					if (node.contains(e.getPoint())) {

						System.out.println("Clicked...");

						dragged = node;
						vertexCurrent = nodeToVertex.get(node);
						// Adjust for the different between the top/left corner of the
						// node and the point it was clicked...

						offset = new Point(node.getBounds().x - e.getX(), node.getBounds().y - e.getY());
						repaint();
						break;
					}
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// Erase the "click" highlight
				if (dragged != null) {
					repaint();
				}
				dragged = null;
				offset = null;
			}
		});

		addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				if (dragged != null && offset != null) {
					// Adjust the position of the drag point to allow for the
					// click point offset
					Point to = e.getPoint();
					to.x += offset.x;
					to.y += offset.y;
					

					// Modify the position of the node...
					Rectangle bounds = dragged.getBounds();
					bounds.setLocation(to);
					dragged.setFrame(bounds);
//                    System.out.println("Drag:id"+vertexCurrent.getId());
					vertexToNode.put(vertexCurrent, dragged);
					nodeToVertex.put(dragged, vertexCurrent);
					repaint();

				}

			}
		});
	}

	public void choseTypeOfLine(Graphics2D g2d, Point from, Point to) {
		// kiem tra loai do thi de ve duong thang

		if (isDirectional == true) // nếu là có hướng vẽ đường là mũi tên
		{
			Arrow arrow;
			// câu lệnh if kiểm vị trí đế vẽ mũi tên hợp lý
			if (from.y < to.y) {
				arrow = new Arrow(from.x, from.y, to.x, to.y - 10);
				arrow.paint(g2d);
			} else {
				arrow = new Arrow(from.x, from.y, to.x - 10, to.y + 10);
				arrow.paint(g2d);
			}

		} else {
			g2d.draw(new Line2D.Float(from, to));
		}
	}

	public void drawLines() {
		for (Edge e : edges) {
			// System.out.println(e.getSource()+"_"+e.getDestination());

			Vertex sou = e.getSource();
			Vertex des = e.getDestination();

			g2d.setColor(Color.BLACK);
			Point from = vertexToNode.get(sou).getBounds().getLocation();
			Point to = vertexToNode.get(des).getBounds().getLocation();
			from.x += radius / 2;
			from.y += radius / 2;
			to.x += radius / 2;
			to.y += radius / 2;

			choseTypeOfLine(g2d, from, to);
			g2d.drawString(String.valueOf(e.getWeight()), (from.x + to.x) / 2, (from.y + to.y) / 2);
		}

	}

	public void drawPathDijkstra(LinkedList<String> path) {
		Point p = null;
		// LinkedList<Vertex> path = ex.Execute(node_source, node_destination);
		isPath.clear();

		// System.out.println(path);
		for (int i = 1; i < path.size(); i++) {
			g2d.setPaint(Color.red);
			Vertex sou = vertexs.get(Integer.valueOf(path.get(i - 1)));
			Vertex des = vertexs.get(Integer.valueOf(path.get(i)));
			Ellipse2D node_begin = vertexToNode.get(sou);
			Ellipse2D node_end = vertexToNode.get(des);
			Point from = node_begin.getBounds().getLocation();
			Point to = node_end.getBounds().getLocation();
			isPath.put(node_begin, true);
			isPath.put(node_end, true);
			from.x += radius / 2;
			from.y += radius / 2;
			to.x += radius / 2;
			to.y += radius / 2;
			choseTypeOfLine(g2d, from, to);
			// g2d.draw(new Line2D.Float(from, to));

		}
	}

	public void drawNodes(Graphics g) {
		for (Ellipse2D node : nodes) {
			g2d.setColor(Color.yellow);
			if (isPath.containsKey(node) == true) {
				g2d.setColor(Color.red);
			}
			g2d.fill(node);

			if (node == dragged) {
				g2d.setColor(Color.BLUE);
				g2d.draw(node);

			}
			// text=nodeToVertex.get(node).getId();
			FontMetrics fm = g.getFontMetrics();
			text = nodeToVertex.get(node).getId();
			int textWidth = fm.stringWidth(text);
			int x = node.getBounds().x;
			int y = node.getBounds().y;
			int width = node.getBounds().width;
			int height = node.getBounds().height;

			// System.out.println(nodeToVertex.get(node).getId());
			if (isPath.containsKey(node) == true) {
				g.setColor(Color.white);
			} else
				g.setColor(Color.black);
			g.drawString(text, x + ((width - textWidth)) / 2, y + ((height - fm.getHeight()) / 2) + fm.getAscent());

		}

		g2d.dispose();
	}

	public void save(String imageFile) {
		Rectangle r = getBounds();

		try {
			BufferedImage i = new BufferedImage(r.width, r.height, BufferedImage.TYPE_INT_RGB);
			Graphics g = i.getGraphics();
			paint(g);
			ImageIO.write(i, "png", new File(imageFile));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(400, 400);
	}

	@Override
	protected void paintComponent(Graphics g) {
		// declaration

		super.paintComponent(g);
		g2d = (Graphics2D) g.create();
		// Draw the connecting lines first
		// This ensures that the lines are under the nodes...
		drawLines();
		System.out.println("Path: " + path);
		if (path != null) {
			drawPathDijkstra(path);
		}

		// Draw the nodes...

		drawNodes(g);

		// g.dispose();
		System.out.println("repaint");
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
