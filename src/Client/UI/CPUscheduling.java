package Client.UI;

import Client.DAO.Process;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.swing.JOptionPane;

import org.jfree.ui.RefineryUtilities;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import Client.Controler.Client;

public class CPUscheduling {
	private static Client c= new Client();
	public static boolean connect() {
		boolean connect= c.connect();
		if(connect==false) {
			JOptionPane.showMessageDialog(null, "Không kết nối được server");
		}
		return connect;
		
	}
	
    public static int S2I(String str) {
        return Integer.parseInt(str);
    } 
    public static Process[] InitDataProcesses(String path) throws FileNotFoundException, ParseException {
        String data = "";
        try {
            File file = new File(path);
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line;
            while ((line = br.readLine()) != null){
              data = data + line;
            }
            fr.close();
            br.close();
           } catch (IOException ex) {
             System.out.println("Đã xảy ra lỗi khi đọc dữ liệu từ file: "+ex);
         }
        
        c.initScheduling(data);
        
        JSONParser Parser = new JSONParser();
        JSONObject objectJson = (JSONObject) Parser.parse(data);
        System.out.println("Type: " + objectJson.get("type"));
        
        JSONArray jsonArray = (JSONArray) Parser.parse(objectJson.get("data").toString());
        Process arrProcess[] = new Process[jsonArray.size()];
        for (int i = 0; i < jsonArray.size(); i++) {
            
            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
            int name = S2I(jsonObject.get("name").toString());
            int arriveTime = S2I(jsonObject.get("arrivetime").toString());
            int burstTime = S2I(jsonObject.get("burttime").toString());
            int priority = S2I(jsonObject.get("priority").toString());
            int completeTime = 0;
            int turnAroundTime = 0;
            int waitingTime = 0;
            int focusCheck = 0;
            
            arrProcess[i] = new Process(name, arriveTime, burstTime, completeTime,  turnAroundTime, waitingTime, focusCheck, priority);

//            System.out.println("name: " + jsonObject.get("name"));
//            System.out.println("arrivetime: " + jsonObject.get("arrivetime"));
//            System.out.println("burttime: " + jsonObject.get("burttime"));
//            System.out.println("priority: " + jsonObject.get("priority"));
//            System.out.println("===============");
        }
        
         return arrProcess;
    }
    
    public static Process[] parrseStringToArr(String result,int size) {
    	StringTokenizer st = new StringTokenizer(result,";",false);
    	Process[]temp= new Process[size];
    	int i=0;
    	while(st.hasMoreTokens()) {
    		String line=st.nextToken();
    		StringTokenizer s = new StringTokenizer(line," ",false);
    		System.out.println(line);
    		 int pid=Integer.valueOf(s.nextToken());
    		 int arrTime =Integer.valueOf(s.nextToken());
    		 int burtTime=Integer.valueOf(s.nextToken());
    		 int compTime=Integer.valueOf(s.nextToken());
    		 int turnArrTime=Integer.valueOf(s.nextToken());
    		 int waitingTime=Integer.valueOf(s.nextToken());
    		 int focusCheck=Integer.valueOf(s.nextToken());
    		 int priority=Integer.valueOf(s.nextToken());
    		 temp[i]=new Process(pid, arrTime, burtTime, compTime, turnArrTime, waitingTime, focusCheck, priority);
    		 i++;
    	}
    	return temp;
    	
    }
    public static int InitDataQuanTime(String path) throws FileNotFoundException, ParseException {
        String data = "";
        try {
            File file = new File(path);
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line;
            while ((line = br.readLine()) != null){
              data = data + line;
            }
            fr.close();
            br.close();
           } catch (IOException ex) {
             System.out.println("Đã xảy ra lỗi khi đọc dữ liệu từ file: "+ex);
         }
        c.initScheduling(data);
        JSONParser Parser = new JSONParser();
        JSONObject objectJson = (JSONObject) Parser.parse(data);
        System.out.println("Quantime: " + objectJson.get("quantime"));
        
        return S2I(objectJson.get("quantime").toString());
    }
    
    public static void drawFCSC(String TypeSheduling, String path) throws FileNotFoundException, ParseException {
        Process[] arrProcesses = InitDataProcesses(path);
    
        String result=c.schedule("FCFS");
        System.out.println(result);
        Process[]temp= new Process[arrProcesses.length]; 
        temp = parrseStringToArr(result,arrProcesses.length);
//       
        DrawGranttChart(TypeSheduling, temp, 0);
    }
    
      
    
    public static void drawSJF(String TypeSheduling, String path) throws FileNotFoundException, ParseException {
        Process[] arrProcesses = InitDataProcesses(path);
        String result=c.schedule("SJF");
        System.out.println(result);
        Process[]temp= new Process[arrProcesses.length]; 
        temp = parrseStringToArr(result,arrProcesses.length);
//       
        DrawGranttChart(TypeSheduling, temp, 0);

    }
    
    public static void drawPriority(String TypeSheduling, String path) throws FileNotFoundException, ParseException {
        Process[] arrProcesses = InitDataProcesses(path);
        String result=c.schedule("Priority");
        System.out.println(result);
        Process[]temp= new Process[arrProcesses.length]; 
        temp = parrseStringToArr(result,arrProcesses.length);

        DrawGranttChart(TypeSheduling, temp, 0);
        
    }
    
    public static void drawRR(String TypeSheduling, String path) throws FileNotFoundException, ParseException {
        Process[] arrProcesses = InitDataProcesses(path);        
        String result=c.schedule("RR");
        System.out.println(result);
        Process[]temp= new Process[arrProcesses.length]; 
        temp = parrseStringToArr(result,arrProcesses.length);

        DrawGranttChart(TypeSheduling, temp, 0);
        
    }
            
    public static void DrawGranttChart(String TypeSheduling, Process[] arrProcesses, int quantime) {
        DrawGrantt ganttdemo = new DrawGrantt(TypeSheduling, arrProcesses, quantime);
        ganttdemo.pack();
        RefineryUtilities.centerFrameOnScreen(ganttdemo);
        ganttdemo.setVisible(true);
        
        System.out.println();
        System.out.println("Đã vẽ Grantt Chart");
    }
     
}