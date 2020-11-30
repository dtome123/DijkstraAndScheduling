package Client.Controler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.StringTokenizer;

import javax.swing.JOptionPane;

import Client.DAO.Edge;
import Client.DAO.Vertex;



public class Client {
	private static Socket socket = null;
	private static BufferedReader in = null;
	private static BufferedWriter out = null;
	private static InitData data = null;
	private static AESEncryption ase = new AESEncryption();
	private static String key = "DIJ";
	
	static boolean isConnected;
	static String address = "192.168.31.59";
	static int port = 1234;

	public Client() {

	}
	public static boolean isConnected() {
		return isConnected;
	}

	public static void connect() {
		try {
			socket = new Socket(address, port);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

			send(Status.New.toString());
			key = recive();
		} catch (UnknownHostException e) {
			isConnected= false;
			return;
		} catch (IOException e) {
			isConnected =false;
			return;
		}
		isConnected=  true;
		
	}

	private static String VertexsToString() {
		String result = "";
		for (Vertex v : data.getVertexs()) {
			result += v.getId() + " ";
		}
		return result;
	}

	private static String EdgesToString(boolean isDirectional) {
		String result = "";
		for (Edge e : data.getEdges()) {
			result += e.getSource() + " " + e.getDestination() + " " + e.getWeight() + " ";
			if (isDirectional == false) {
				result += e.getDestination() + " " + e.getSource() + " " + e.getWeight() + " ";
			}
		}
		return result.trim();
	}

	public static void send(String message) {
		try {
			message = ase.encrypt(message, key);
			out.write(message);
			out.newLine();
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Không gửi đi được");
		}

	}

	public static String recive() {
		try {
			String result = in.readLine();
			return ase.decrypt(result, key);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, "Không nhận được");
		}
		return null;
	}

	public static void initDij(InitData new_data, boolean isDirectional) {

		data = new_data;
		send(VertexsToString());

		String result = recive();
		System.out.println(result);

		send(EdgesToString(isDirectional));

		result = recive();
		System.out.println(result);

	}
	public static void initScheduling(String data) {
		send("createScheduling");
		String status =recive();
		if(status.equals(Status.Ready.toString())) {
			send(data);
		}
	}
	
	private static ArrayList<String> getResultOfSchedule() {
		ArrayList<String> result = new ArrayList<String>();
		StringTokenizer st = new StringTokenizer(recive()," ",false);
		while(st.hasMoreTokens()) {
			result.add(st.nextToken());
		}
		System.out.println("Result: "+result);
		return result;
	}
	public static String schedule(String type){
		send(type);
		return recive(); 
	}

	public static void resetData(InitData data, boolean isDirectional) throws IOException {
		send("resetDij");
		String status = recive();
		if (status.equals(Status.Ready.toString())) {
			initDij(data, isDirectional);
		}
		System.out.println("reseting is successfull");

	}

	public static LinkedList<String> findPath(int source, int destination) throws IOException {
		send("find;" + source + ";" + destination);
		String result = recive();
		StringTokenizer st = new StringTokenizer(result, " ", false);
		LinkedList<String> tmp = new LinkedList<String>();
		while (st.hasMoreTokens()) {
			tmp.add(st.nextToken());
		}
		return tmp;
	}
	
	public static void close() {
		try {
			send("close");
			in.close();
			out.close();
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public InitData getData() {
		return data;
	}

	public void setData(InitData data) {
		this.data = data;
	}

}
