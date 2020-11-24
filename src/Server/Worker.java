package Server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;

import org.apache.commons.codec.digest.DigestUtils;
import org.json.simple.parser.ParseException;

import Server.DAO.Edge;
import Server.DAO.Vertex;

public class Worker implements Runnable{
	
		private String name;
		private Socket socket;
		private BufferedReader in;
		private BufferedWriter out;
		
		private ExecuteDijkstra ex = null; 
		private SchedulingAlgorithm schedule= new SchedulingAlgorithm();
		private StringTokenizer st =null;
		private String key="DIJ";
		private AESEncryption aes = new AESEncryption();
		List<Edge> edges = new LinkedList<Edge>();
		List<Vertex> vertexs = new LinkedList<Vertex>();
		private String regexFind ="^find;[0-9]+;[0-9]+$";
		private String regexCreateDiJ= "^createDij$";
		private String regexResetDij="^resetDij$";
		
		public Worker(Socket socket, String name) {
			try {
				this.socket =socket;
				this.name =name;
				this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		public void send(String message) {
			try {
				message = aes.encrypt(message, key);
				out.write(message);
				out.newLine();
				out.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				
				
			}
			
		}
		public String recive() {
			try {
				String result = in.readLine();
				return aes.decrypt(result, key);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				return "close";
			}
		}
		public void clearData() {
			
			edges.clear();
			vertexs.clear();
		}
		public void initData() {
			clearData();
			String dataVertexs;
			dataVertexs = recive();
			st = new StringTokenizer(dataVertexs," ",false);
			while(st.hasMoreTokens()) {
				Vertex v = new Vertex(st.nextToken());
				vertexs.add(v);
			}
			System.out.println(vertexs);
			send("Adding vertexs is sucsess");
			
			
			String dataEdges = recive();
			st = new StringTokenizer(dataEdges," ",false);
			while(st.hasMoreTokens()) {
				addLane(Integer.valueOf(st.nextToken()), Integer.valueOf(st.nextToken()), Integer.valueOf(st.nextToken()));
			}
			ex= new ExecuteDijkstra(vertexs, edges);
			System.out.println();
			send("Adding edges is sucsess");
			
			
		}
		private void generateKey() {
			Random r = new Random();
			int number = r.nextInt();
			String rs = DigestUtils.md5Hex(number+"");
			if(recive().equals(Status.New.toString())) {
				send(rs);
				this.key=rs;
			}
		}
		@Override
		public void run() {
			
			System.out.println("Server connected with client "+name);
			generateKey();
			
//			initData();
			while(true) {
				String command = recive();
				
				if(command.equals("close")) {
					break;
				}

				if(command.equals("createScheduling")) {
					send(Status.Ready.toString());
					schedule.setData(recive());
					continue;
				}
				if(command.equals("FCFS"))
				{
					try {
						send(schedule.drawFCSC());
					} catch (FileNotFoundException | ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if(command.equals("SJF"))
				{
					try {
						send(schedule.drawSJF());
					} catch (FileNotFoundException | ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if(command.equals("RR"))
				{
					try {
						send(schedule.drawRR());
					} catch (FileNotFoundException | ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if(command.equals("Priority"))
				{
					try {
						send(schedule.drawPriority());
					} catch (FileNotFoundException | ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				if(command.matches(regexResetDij)) {
					send(Status.Ready.toString());	
					initData();
					continue;
				}
				if(command.matches(regexFind)) {
					st = new StringTokenizer(command,";",false);
					st.nextToken();
					int source = Integer.valueOf(st.nextToken());
					int destination = Integer.valueOf(st.nextToken());
					String result =getPath(source, destination);
					System.out.println("find: "+result);
					send(result);
					continue;
					
				}
				
			}
			try {
				in.close();
				out.close();
				socket.close();
				Servers.workers.remove(this);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
			
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
