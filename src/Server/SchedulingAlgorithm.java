package Server;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import org.jfree.ui.RefineryUtilities;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import Server.DAO.Process;

public class SchedulingAlgorithm {
	private String data;
	private int quantime;
	
    public int getQuantime() {
		return quantime;
	}
	public void setQuantime(int quantime) {
		this.quantime = quantime;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public static int S2I(String str) {
        return Integer.parseInt(str);
    } 
    public Process[] InitDataProcesses() throws FileNotFoundException, ParseException {
//        String data = "";
//        try {
//            File file = new File(path);
//            FileReader fr = new FileReader(file);
//            BufferedReader br = new BufferedReader(fr);
//            String line;
//            while ((line = br.readLine()) != null){
//              data = data + line;
//            }
//            fr.close();
//            br.close();
//           } catch (IOException ex) {
//             System.out.println("Đã xảy ra lỗi khi đọc dữ liệu từ file: "+ex);
//         }
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
    
//    public int InitDataQuanTime() throws FileNotFoundException, ParseException {
////        try {
////            File file = new File(path);
////            FileReader fr = new FileReader(file);
////            BufferedReader br = new BufferedReader(fr);
////            String line;
////            while ((line = br.readLine()) != null){
////              data = data + line;
////            }
////            fr.close();
////            br.close();
////           } catch (IOException ex) {
////             System.out.println("Đã xảy ra lỗi khi đọc dữ liệu từ file: "+ex);
////         }
//
//        JSONParser Parser = new JSONParser();
//        JSONObject objectJson = (JSONObject) Parser.parse(data);
//        System.out.println("Quantime: " + objectJson.get("quantime"));
//        
//        return S2I(objectJson.get("quantime").toString());
//    }
    
    private String getStringResult(Process[] arrProcesses) {
    	String result ="";
    	for(int i = 0; i < arrProcesses.length; i++) {
            //System.out.println(arrProcesses[i].getPid()+"\t\t\t" + arrProcesses[i].getArrTime()+ "\t\t" + arrProcesses[i].getBurtTime() + "\t\t" + arrProcesses[i].getCompTime() + "\t\t" + arrProcesses[i].getTurnArrTime() + "\t\t" + arrProcesses[i].getWaitingTime() + "\t\t" + arrProcesses[i].getPriority());
            //result += arrProcesses[i].getPid()+" ";
    		result += String.format("%d %d %d %d %d %d %d %d;",
    				arrProcesses[i].getPid(),arrProcesses[i].getArrTime(),
    				arrProcesses[i].getBurtTime(),arrProcesses[i].getCompTime(),
    				arrProcesses[i].getTurnArrTime(),arrProcesses[i].getWaitingTime(),
    				arrProcesses[i].getFocusCheck(),arrProcesses[i].getPriority() ) ;
        }
    	return result.substring(0,result.length()-1);
    }
    public String drawFCSC() throws FileNotFoundException, ParseException {
        Process[] arrProcesses = InitDataProcesses();
        
        for (int i = 0; i < arrProcesses.length; i++) {
            for (int j = i+1; j < arrProcesses.length; j++) {
                if (arrProcesses[i].getArrTime() > arrProcesses[j].getArrTime()) {
                    Process temp = new Process(0, 0, 0, 0, 0, 0, 0, 0);
                    temp = arrProcesses[i];
                    arrProcesses[i] = arrProcesses[j];
                    arrProcesses[j] = temp;
                }
            }
        }
        
        arrProcesses[0].setCompTime(arrProcesses[0].getArrTime() + arrProcesses[0].getBurtTime());
        for(int i = 1; i < arrProcesses.length; i++) {
            arrProcesses[i].setCompTime(arrProcesses[i-1].getCompTime() + arrProcesses[i].getBurtTime());
        }
        for(int i = 0; i < arrProcesses.length; i++) {
            arrProcesses[i].setTurnArrTime(arrProcesses[i].getCompTime() - arrProcesses[i].getArrTime());
            arrProcesses[i].setWaitingTime(arrProcesses[i].getTurnArrTime() - arrProcesses[i].getBurtTime());
        }
        
        
        System.out.println("Process\t\tAT\t\tBT\t\tCT\t\tTAT\t\tWT\t\tPriority");
        for(int i = 0; i < arrProcesses.length; i++) {
            System.out.println(arrProcesses[i].getPid()+"\t\t\t" + arrProcesses[i].getArrTime()+ "\t\t" + arrProcesses[i].getBurtTime() + "\t\t" + arrProcesses[i].getCompTime() + "\t\t" + arrProcesses[i].getTurnArrTime() + "\t\t" + arrProcesses[i].getWaitingTime() + "\t\t" + arrProcesses[i].getPriority());
        }
        System.out.println("gantt chart: ");
        for(int i = 0; i < arrProcesses.length; i++) {
            System.out.print("P" + arrProcesses[i].getPid() +" ");
        }
        
        System.out.println();
        System.out.print("0 ");
        for(int i = 0; i < arrProcesses.length; i++) {
            System.out.print(arrProcesses[i].getCompTime() +" ");
        }
        
        return getStringResult(arrProcesses);
        
        //DrawGranttChart(TypeSheduling, arrProcesses, 0);
        
    }
      
    public String drawSJF() throws FileNotFoundException, ParseException {
        Process[] arrProcesses = InitDataProcesses();
        
        int st=0, tot=0;
        while(true)
        {
            int c=arrProcesses.length, min = 999999;

            if (tot == arrProcesses.length)
                break;

            for (int i=0; i<arrProcesses.length; i++)
            {

                if ((arrProcesses[i].getArrTime() <= st) && (arrProcesses[i].getFocusCheck() == 0) && (arrProcesses[i].getBurtTime()<min))
                {
                    min=arrProcesses[i].getBurtTime();
                    c=i;
                }
            }
            if (c==arrProcesses.length)
                st++;
            else
            {
                arrProcesses[c].setCompTime(st + arrProcesses[c].getBurtTime());
                st+= arrProcesses[c].getBurtTime();
                arrProcesses[c].setTurnArrTime(arrProcesses[c].getCompTime()- arrProcesses[c].getArrTime());
                arrProcesses[c].setWaitingTime(arrProcesses[c].getTurnArrTime() - arrProcesses[c].getBurtTime() );
                arrProcesses[c].setFocusCheck(1);
                arrProcesses[tot].setPid(c+1);
                tot++;
                
            }
        }
        
        System.out.println("Process\t\tAT\t\tBT\t\tCT\t\tTAT\t\tWT\t\tPriority");
        for(int i = 0; i < arrProcesses.length; i++) {
            System.out.println(arrProcesses[i].getPid()+"\t\t\t" + arrProcesses[i].getArrTime()+ "\t\t" + arrProcesses[i].getBurtTime() + "\t\t" + arrProcesses[i].getCompTime() + "\t\t" + arrProcesses[i].getTurnArrTime() + "\t\t" + arrProcesses[i].getWaitingTime() +  "\t\t" + arrProcesses[i].getPriority());
        }
        
        System.out.println("gantt chart: ");
        for(int i = 0; i < arrProcesses.length; i++) {
            System.out.print("P" + arrProcesses[i].getPid() +" ");
        }
        System.out.println();
        System.out.print("0 ");
        for(int i = 0; i < arrProcesses.length; i++) {
            System.out.print(arrProcesses[arrProcesses[i].getPid() - 1].getCompTime() +" ");
        }
        return getStringResult(arrProcesses);
    }
    
    public String drawPriority() throws FileNotFoundException, ParseException {
        Process[] arrProcesses = InitDataProcesses();
        for (int i = 0; i < arrProcesses.length; i++) {
            for (int j = i+1; j < arrProcesses.length; j++) {
                if (arrProcesses[i].getArrTime() > arrProcesses[j].getArrTime()) {
                    Process temp = new Process(0, 0, 0, 0, 0, 0, 0 ,0);
                    temp = arrProcesses[i];
                    arrProcesses[i] = arrProcesses[j];
                    arrProcesses[j] = temp;
                }
                else if(arrProcesses[i].getArrTime() == arrProcesses[j].getArrTime()) 
                {
                     if (arrProcesses[i].getPriority()> arrProcesses[j].getPriority()) {
                        Process temp = new Process(0, 0, 0, 0, 0, 0, 0 ,0);
                        temp = arrProcesses[i];
                        arrProcesses[i] = arrProcesses[j];
                        arrProcesses[j] = temp;
                    }
                }
            }
        }
        
        int SumCheck = 0;
        int checkTus = 1;
        
        arrProcesses[0].setFocusCheck(checkTus);
        arrProcesses[0].setCompTime(SumCheck + arrProcesses[0].getBurtTime());
        arrProcesses[0].setTurnArrTime(arrProcesses[0].getCompTime() - arrProcesses[0].getArrTime());
        arrProcesses[0].setWaitingTime(arrProcesses[0].getTurnArrTime() - arrProcesses[0].getBurtTime());

        
        System.out.println("Process\t\tAT\t\tBT\t\tCT\t\tTA\t\tWT\t\tPri\t\tCheck");
        System.out.println(arrProcesses[0].getPid()+"\t\t" + arrProcesses[0].getArrTime()+ "\t\t" + arrProcesses[0].getBurtTime()+ "\t\t" + arrProcesses[0].getCompTime()+ "\t\t" + arrProcesses[0].getTurnArrTime() + "\t\t" + arrProcesses[0].getWaitingTime() + "\t\t" + arrProcesses[0].getPriority() + "\t\t" + arrProcesses[0].getFocusCheck());
        SumCheck += arrProcesses[0].getBurtTime();
        
        while(checkTus != arrProcesses.length) {
            int getNextProcess = 0;
            int maxPriority = 99999;
            int maxArriveTime = 99999;
            int chooseIndex = 0;
            for(int i = 1; i < arrProcesses.length; i++) {
//                System.out.println(arrProcess[i].getArrTime() + " -- " + SumCheck);
//                System.out.println(arrProcess[i].getFocusCheck() + " -- " + 0);
//                System.out.println(arrProcess[i].getPriority() + " -- " + maxPriority);
//                System.out.println(arrProcess[i].getArrTime() + " -- " + maxArriveTime);
//                System.out.println("====");
//                
                
                if (arrProcesses[i].getArrTime() <= SumCheck && arrProcesses[i].getFocusCheck() == 0 && 
                        arrProcesses[i].getPriority() < maxPriority) {
                    maxPriority = arrProcesses[i].getPriority();
                    maxArriveTime = arrProcesses[i].getArrTime();
                    getNextProcess = arrProcesses[i].getPid();
//                     System.out.println("i: " + i);
//                    System.out.println("arrtime: " + arrProcess[i].getArrTime());
                    //System.out.println("next: " + getNextProcess);
                    
                    chooseIndex = i;
                }
            }
           // System.out.println("next: " + chooseIndex);
            //getNextProcess -= 1;
          // System.out.println(getNextProcess);
//            System.out.println(SumCheck);
            checkTus += 1;
            arrProcesses[chooseIndex].setFocusCheck(checkTus);
            arrProcesses[chooseIndex].setCompTime(SumCheck + arrProcesses[chooseIndex].getBurtTime());
            arrProcesses[chooseIndex].setTurnArrTime(arrProcesses[chooseIndex].getCompTime() - arrProcesses[chooseIndex].getArrTime());
            arrProcesses[chooseIndex].setWaitingTime(arrProcesses[chooseIndex].getTurnArrTime() - arrProcesses[chooseIndex].getBurtTime());
//            
            
            System.out.println(arrProcesses[chooseIndex].getPid()+"\t\t" + arrProcesses[chooseIndex].getArrTime()+ "\t\t" + arrProcesses[chooseIndex].getBurtTime()+ "\t\t" + arrProcesses[chooseIndex].getCompTime()+ "\t\t" + arrProcesses[chooseIndex].getTurnArrTime() + "\t\t" + arrProcesses[chooseIndex].getWaitingTime() + "\t\t" + arrProcesses[chooseIndex].getPriority() + "\t\t" + arrProcesses[chooseIndex].getFocusCheck());
            SumCheck += arrProcesses[chooseIndex].getBurtTime();
            
            
//            System.out.println("sumcheck: " + SumCheck);
//            System.out.println("sumStatus: " + arrProcess[getNextProcess].getFocusCheck());
           if ( checkTus == arrProcesses.length) break;
        }
         return getStringResult(arrProcesses);
        //DrawGranttChart(TypeSheduling, arrProcesses, 0);
        
    }
    
    public String drawRR() throws FileNotFoundException, ParseException {
        Process[] arrProcesses = InitDataProcesses();
        int quantime = this.quantime;
        
        int rem_time[] = new int[arrProcesses.length];

        for (int i = 0; i < arrProcesses.length; i++) {
            rem_time[i] = arrProcesses[i].getBurtTime();
        }
        int t = 0;
        int arrival = 0;
        
        while (true) {
            boolean done = true;
            for (int i = 0; i < arrProcesses.length; i++) {
                if (rem_time[i] > 0) {
                    done = false;
                    if (rem_time[i] > quantime && arrProcesses[i].getArrTime() <= arrival) {
                        t += quantime;
                        rem_time[i] -= quantime;
                        arrival++;
                        //System.out.println("P"+ (i+1));
                        //System.out.println(t);
                    } else {
                        if (arrProcesses[i].getArrTime() <= arrival) {
                            arrival++;
                            t += rem_time[i];
                            rem_time[i] = 0;
                            arrProcesses[i].setCompTime(t);
                            
                            //System.out.println("P"+ (i+1));
                            //System.out.println(t);
                        }
                    }
                }
            }

            if (done == true) {
                break;
            }
        }
         
        for (int i = 0; i < arrProcesses.length; i++) {
            arrProcesses[i].setTurnArrTime(arrProcesses[i].getCompTime() - arrProcesses[i].getArrTime());
            arrProcesses[i].setWaitingTime(arrProcesses[i].getTurnArrTime()- arrProcesses[i].getBurtTime());
        }
        
        System.out.println("Process\t\tAT\t\tBT\t\tCT\t\tTAT\t\tWT\t\tPriority");
        for(int i = 0; i < arrProcesses.length; i++) {
            System.out.println(arrProcesses[i].getPid()+"\t\t\t" + arrProcesses[i].getArrTime()+ "\t\t" + arrProcesses[i].getBurtTime() + "\t\t" + arrProcesses[i].getCompTime() + "\t\t" + arrProcesses[i].getTurnArrTime() + "\t\t" + arrProcesses[i].getWaitingTime() +  "\t\t" + arrProcesses[i].getPriority());
        }
        
        return getStringResult(arrProcesses);
        //DrawGranttChart(TypeSheduling, arrProcesses, quantime);
        
    }
            
//    public static void DrawGranttChart(String TypeSheduling, Process[] arrProcesses, int quantime) {
//    	DrawGrantt ganttdemo = new DrawGrantt(TypeSheduling, arrProcesses, quantime);
////        DrawGrantt ganttdemo = new DrawGrantt(TypeSheduling, arrProcesses, quantime);
//        ganttdemo.pack();
//        RefineryUtilities.centerFrameOnScreen(ganttdemo);
//        ganttdemo.setVisible(true);
//        
//        System.out.println();
//        System.out.println("Đã vẽ Grantt Chart");
//    }
     
}
