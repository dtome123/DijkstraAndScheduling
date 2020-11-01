package UI;

import javax.swing.JPanel;

import org.junit.jupiter.api.Timeout;

import Bus.ExecuteDijkstra;
import DAO.Edge;
import DAO.Vertex;

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
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

public class Panneau extends JPanel {

    private int radius = 20;
    private String text = "stack";
    //bien graphic
    private List<Ellipse2D> nodes;
    private Ellipse2D dragged;
    private Point offset;
    private Vertex vertexCurrent;
    Graphics2D g2d;
    // bien du lieu
    Hashtable<Vertex, Boolean> isDestination = new Hashtable<>();
    Hashtable<Ellipse2D,Vertex > nodeToVertex = new Hashtable<>();
    Hashtable<Vertex,Ellipse2D > vertexToNode = new Hashtable<>();
    Hashtable<Ellipse2D, Boolean> isPath = new Hashtable<Ellipse2D, Boolean>();
    
    
    
    
	List<Vertex> vertexs = new ArrayList<Vertex>();
	ExecuteDijkstra ex = new ExecuteDijkstra();
	
	
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
//		for(Vertex v : vertexs) {
//			System.out.println("Node "+v.getId()+": ("+v.x+","+v.y+")");
//		}
	}

    public Panneau() {
        nodes = new ArrayList<>(25);

        //nodes.add(new Ellipse2D.Float(50 - (radius / 2), 100 - (radius / 2), radius, radius));
        //nodes.add(new Ellipse2D.Float(350 - (radius / 2), 100 - (radius / 2), radius, radius));
        init();
        for(Vertex v : vertexs) {
        	Ellipse2D.Float temp =new Ellipse2D.Float(v.x - (radius / 2), v.y - (radius / 2), radius, radius);
        	nodes.add(temp);
        	nodeToVertex.put(temp,v);
        	vertexToNode.put(v, temp);
        }

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
                        //System.out.println("Click: Node vertex "+vertexToNode.get(vertexCurrent).getBounds().x);
//                        System.out.println("id"+nodeToVertex.get(dragged).getId());
                        
                        //System.out.println("id"+nodeToId.get(vertexToNode.get(vertexCurrent)));
                        //Highlight the clicked node
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
                    vertexToNode.put(vertexCurrent,dragged);
                    nodeToVertex.put(dragged, vertexCurrent);
                	
                    
                    // repaint...
                    //System.out.println(nodeToId.size());
                    repaint();
                    
                }

            }
        });
    }

    public void drawLines() {
    	
		/*
		 * for (Ellipse2D node : nodes) {
		 * 
		 * g2d.setColor(Color.BLACK); Point to = node.getBounds().getLocation(); to.x +=
		 * radius / 2; to.y += radius / 2; if (p != null) { g2d.draw(new Line2D.Float(p,
		 * to)); } p = to;
		 * 
		 * }
		 */
    	for (Edge e : ex.getEdges()) {
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
            g2d.draw(new Line2D.Float(from, to));

			//g2d.drawLine(sou.x + 10, sou.y + 20, des.x + 10, des.y + 20);
			//g2d.drawString(String.valueOf(e.getWeight()), (sou.x + des.x) / 2 + 20, (sou.y + des.y) / 2 + 20);
            g2d.drawString(String.valueOf(e.getWeight()), (from.x+to.x)/2,(from.y+to.y)/2);
		}
    	
    	Point p = null;
    	LinkedList<Vertex> path = ex.handle(0, 7);
		for (int i = 1; i < path.size(); i++) {
			g2d.setPaint(Color.red);

			Vertex sou = path.get(i - 1);
			Vertex des = path.get(i);
			
			
			Ellipse2D node_begin =vertexToNode.get(sou);
			Ellipse2D node_end =vertexToNode.get(des);
            Point from = node_begin.getBounds().getLocation();
            Point to = node_end.getBounds().getLocation();
            isPath.put(node_begin, true);
            isPath.put(node_end, true);
            from.x += radius / 2;
            from.y += radius / 2;
            to.x += radius / 2;
            to.y += radius / 2;
            g2d.draw(new Line2D.Float(from, to));
            
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
        
        // Draw the nodes...
        
        for (Ellipse2D node : nodes) {
        	
        	g2d.setColor(Color.yellow);
            if(isPath.containsKey(node)==true) {
            	g2d.setColor(Color.red);
            }
            g2d.fill(node);
            
            if (node == dragged) {
                g2d.setColor(Color.BLUE);
                g2d.draw(node);
                
            }
            
            
            //text=nodeToVertex.get(node).getId();
            FontMetrics fm = g.getFontMetrics();
            text=nodeToVertex.get(node).getId();
            int textWidth = fm.stringWidth(text);
            int x = node.getBounds().x;
            int y = node.getBounds().y;
            int width = node.getBounds().width;
            int height = node.getBounds().height;
            //System.out.println(nodeToVertex.get(node).getId());
            g.drawString(text,
            				x + ((width - textWidth)) / 2,
                            y + ((height - fm.getHeight()) / 2) + fm.getAscent());

        }
        
        g2d.dispose();

    }

}

