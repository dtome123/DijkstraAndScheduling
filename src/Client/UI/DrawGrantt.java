package Client.UI;

import Client.DAO.Process;
import java.awt.Dimension;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.IntervalCategoryDataset;
import org.jfree.data.gantt.Task;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;


public class DrawGrantt extends JFrame {
    
   public DrawGrantt(String titleScheduling, Process[] arrProcesses, int quantime) {
        super(titleScheduling);
        JPanel jpanel = createDemoPanel(titleScheduling, arrProcesses, quantime);
        jpanel.setPreferredSize(new Dimension(500, 270));
        setContentPane(jpanel);
    }

    private static JFreeChart createChart(String title, IntervalCategoryDataset dataset) {
        final JFreeChart chart = GanttChartFactory.createGanttChart(
            title + " Scheduling", "Task", "Time", dataset, true, true, false);
        return chart;
    }

    private static IntervalCategoryDataset createDataset(String title, Process[] arrProcesses, int quantime) {
        TaskSeries taskseries = new TaskSeries(title);
        Task task;
        switch(title)
            {
                case "FCFS":
                    task= new TaskNumeric("Process 1", 0, arrProcesses[0].getCompTime());
                    taskseries.add(task);

                    for (int i=1; i<arrProcesses.length; i++) {

                        int numberProcess = i+1;
                        String processTittle = "Process " + numberProcess;
                        Task task1 = new TaskNumeric(processTittle, arrProcesses[i-1].getCompTime(), arrProcesses[i].getCompTime());
                        taskseries.add(task1);
                    }
                    break;
                case "SJF":
                	
                	System.out.println("CT: " + arrProcesses[0].getCompTime());
                	System.out.println("CT: " + arrProcesses[1].getCompTime());
                	System.out.println("CT: " + arrProcesses[2].getCompTime());
                	System.out.println("CT: " + arrProcesses[3].getCompTime());
                	
                    task = new TaskNumeric("Process " + arrProcesses[0].getPid() , 0, arrProcesses[arrProcesses[0].getPid()-1].getCompTime());
                    taskseries.add(task);

                    for (int i=1; i<arrProcesses.length; i++) {
                        String processTittle = "Process " + arrProcesses[i].getPid();
                        Task task1 = new TaskNumeric(processTittle, arrProcesses[arrProcesses[i-1].getPid()-1].getCompTime(), arrProcesses[arrProcesses[i].getPid()-1].getCompTime());
                        taskseries.add(task1);
                    }
                    break;
                 case "Priority":
                    task= new TaskNumeric("Process 1", 0, arrProcesses[0].getCompTime());
                    taskseries.add(task);

                    for (int i=1; i<arrProcesses.length; i++) {

                        int numberProcess = i+1;
                        String processTittle = "Process " + numberProcess;
                        Task task1 = new TaskNumeric(processTittle, arrProcesses[i-1].getCompTime(), arrProcesses[i].getCompTime());
                        taskseries.add(task1);
                    }
                    break;
                 case "RR":
                        int rem_time[] = new int[arrProcesses.length];
                        for (int i = 0; i < arrProcesses.length; i++) {
                            rem_time[i] = arrProcesses[i].getBurtTime();
                        }
                        int t = 0;
                        int arrival = 0;

                        int saveMove = 0;
                        while (true) {
                            boolean done = true;
                            
                            for (int i = 0; i < arrProcesses.length; i++) {
                                if (rem_time[i] > 0) {
                                    done = false;
                                    
                                    if (rem_time[i] > quantime && arrProcesses[i].getArrTime() <= arrival) {
                                        t += quantime;
                                        rem_time[i] -= quantime;
                                        arrival++;
                                        
                                        String processTittle = "Process " + (i+1) + " - " + randomString();
                                        Task task1 = new TaskNumeric(processTittle, saveMove, t);
                                        taskseries.add(task1);
                                        saveMove = t ;
                                        //System.out.println("saveMove: " + saveMove);
                                    } else {
                                        if (arrProcesses[i].getArrTime() <= arrival) {
                                            arrival++;
                                            t += rem_time[i];
                                            rem_time[i] = 0;
                                            arrProcesses[i].setCompTime(t);

                                            String processTittle = "Process " + (i+1) + " - " + randomString();
                                            Task task1 = new TaskNumeric(processTittle, saveMove, t);
                                            taskseries.add(task1);
                                            saveMove = t ;
                                            //System.out.println("saveMove: " + saveMove);
                                        }
                                    }
                                }
                            }

                            if (done == true) {
                                break;
                            }
                        }
                     
                     
                    break;
                default:
                    break;
            }
        

        TaskSeriesCollection taskseriescollection = new TaskSeriesCollection();
        taskseriescollection.add(taskseries);
        return taskseriescollection;
    }
    
    public static String randomString() {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 3;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
          .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
          .limit(targetStringLength)
          .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
          .toString();
        return generatedString;
    }

    public static JPanel createDemoPanel(String title, Process[] arrProcesses, int quantime) {
        JFreeChart jfreechart = createChart(title, createDataset(title, arrProcesses, quantime));
        ChartPanel chartpanel = new ChartPanel(jfreechart);
        chartpanel.setMouseWheelEnabled(true);
        return chartpanel;
    }
    
}
