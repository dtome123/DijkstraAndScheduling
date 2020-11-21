package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Servers {
	public int port =1234;
	public int numTheard=5;
	private ServerSocket server ;
	public static List<Worker>workers = new ArrayList<Worker>();
	
	public void run() {
		int i=0;
		ExecutorService ex = Executors.newCachedThreadPool();
		try {
			server = new ServerSocket(port);
			System.out.println("Server is binding at "+ port);
			while (true) {
				i++;
				Socket socket = server.accept();
				socket.setSoTimeout(600*1000);
				Worker w =new Worker(socket, Integer.toString(i));
				workers.add(w);
				ex.execute(w);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			
				try {
					if(server!=null)
						server.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		
	}

}
