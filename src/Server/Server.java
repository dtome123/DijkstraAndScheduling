package Server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;


import Server.DAO.Edge;
import Server.DAO.Vertex;






public class Server {
	private ServerSocket server = null;
	private Socket socket = null;
	private int port =4321;
	
	private BufferedReader in = null;
	private BufferedWriter out =null;
	private ExecuteDijkstra ex = null; 
	private StringTokenizer st =null;
	List<Edge> edges = new LinkedList<Edge>();
	List<Vertex> vertexs = new LinkedList<Vertex>();
	private String regexFind ="^find;[0-9]+;[0-9]+$";
	private String regexCreate= "^create$";
	private String regexReset="^reset$";
	public Server() {
		
	}
	public void send(String message) {
		try {
			out.write(message);
			out.newLine();
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void clearData() {
		
		edges.clear();
		vertexs.clear();
		
		
	}
	public void initData() {
		vertexs.clear();
		edges.clear();
		String dataVertexs;
		try {
			dataVertexs = in.readLine();
			st = new StringTokenizer(dataVertexs," ",false);
			while(st.hasMoreTokens()) {
				Vertex v = new Vertex(st.nextToken());
				vertexs.add(v);
			}
			System.out.println(vertexs);
			send("Adding vertexs is sucsess");
			
			
			String dataEdges = in.readLine();
			st = new StringTokenizer(dataEdges," ",false);
			while(st.hasMoreTokens()) {
				addLane(Integer.valueOf(st.nextToken()), Integer.valueOf(st.nextToken()), Integer.valueOf(st.nextToken()));
			}
			ex= new ExecuteDijkstra(vertexs, edges);
			System.out.println();
			send("Adding edges is sucsess");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	public void run() throws IOException, ClassNotFoundException {
		System.out.println("Server connected ");

		
		server = new ServerSocket(port);
		socket = server.accept();
		
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		
		initData();
		while(true) {
			String command = in.readLine();
			
			if(command.equals("close")) {
				break;
			}
			if(command.matches(regexCreate)) {
				
				//chưa làm xong
			}
			if(command.matches(regexReset)) {
				send(StatusServer.Ready.toString());	
				initData();
				continue;
			}
			if(command.matches(regexFind)) {
				st = new StringTokenizer(command,";",false);
				st.nextToken();
				int source = Integer.valueOf(st.nextToken());
				int destination = Integer.valueOf(st.nextToken());
				send(getPath(source, destination));
				continue;
				
			}
			
		}
		in.close();
		out.close();
		socket.close();
	}
	private String getPath(int source,int destination) {
		String result="";
		try {
			LinkedList<Vertex> path = ex.Execute(source, destination);
			
			for(Vertex v:path) {
				result+=v.getId()+" ";
			}
		}
		catch (Exception e) {
			System.out.println("Không tìm thấy");
		}
		
		return result.trim();
	}
	private void addLane( int sourceLocNo, int destLocNo,int duration) {
        Edge lane = new Edge(vertexs.get(sourceLocNo), vertexs.get(destLocNo), duration );
        edges.add(lane);
    }
	

}
