package Client.UI;

import java.io.File;

import Client.Controler.InitData;

public class main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		InitData data = new InitData();
		data.readFile(new File("src//Client//data.txt"));
		//Panneau p= new Panneau(data.getVertexs(),data.getEdges());
		

	}

}
