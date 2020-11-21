package Client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.StringTokenizer;

import javax.swing.JOptionPane;

import Server.Server;
import Server.DAO.Edge;
import Server.DAO.Vertex;

public class Client {
	private Socket socket = null;
	private BufferedReader in = null;
	private BufferedWriter out = null;
	private InitData data = null;
	private AESEncryption ase = new AESEncryption();
	private String key = "DIJ";

	String address = "localhost";
	int port = 1234;

	public Client() {

	}

	public boolean connect() {
		try {
			socket = new Socket(address, port);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

			send(Status.New.toString());
			this.key = recive();
		} catch (UnknownHostException e) {
			return false;
		} catch (IOException e) {
			return false;
		}
		return true;
	}

	private String VertexsToString() {
		String result = "";
		for (Vertex v : data.getVertexs()) {
			result += v.getId() + " ";
		}
		return result;
	}

	private String EdgesToString(boolean isDirectional) {
		String result = "";
		for (Edge e : data.getEdges()) {
			result += e.getSource() + " " + e.getDestination() + " " + e.getWeight() + " ";
			if (isDirectional == false) {
				result += e.getDestination() + " " + e.getSource() + " " + e.getWeight() + " ";
			}
		}
		return result.trim();
	}

	public void send(String message) {
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

	public String recive() {
		try {
			String result = in.readLine();
			return ase.decrypt(result, key);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, "Không nhận được");
		}
		return null;
	}

	public void init(InitData data, boolean isDirectional) {

		this.data = data;
		send(VertexsToString());

		String result = recive();
		System.out.println(result);

		send(EdgesToString(isDirectional));

		result = recive();
		System.out.println(result);

	}

	public void resetData(InitData data, boolean isDirectional) throws IOException {
		send("reset");
		String status = recive();
		if (status.equals(Status.Ready.toString())) {
			init(data, isDirectional);
		}
		System.out.println("reseting is successfull");

	}

	public LinkedList<String> findPath(int source, int destination) throws IOException {
		send("find;" + source + ";" + destination);
		String result = recive();
		StringTokenizer st = new StringTokenizer(result, " ", false);
		LinkedList<String> tmp = new LinkedList<String>();
		while (st.hasMoreTokens()) {
			tmp.add(st.nextToken());
		}
		return tmp;
	}

	public void close() {
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
