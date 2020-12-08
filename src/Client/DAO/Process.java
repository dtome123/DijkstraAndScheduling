package Client.DAO;

public class Process {
    private int pid;
    private int arrTime ;
    private int burtTime;
    private int compTime;
    private int turnArrTime;
    private int waitingTime;
    private int focusCheck;
    private int priority;
    public Process(int pid, int arrTime, int burtTime, int compTime, int turnArrTime, int waitingTime, int focusCheck,  int priority) {
        this.pid = pid;
        this.arrTime = arrTime;
        this.burtTime = burtTime;
        this.compTime = compTime;
        this.turnArrTime = turnArrTime;
        this.waitingTime = waitingTime;
        this.focusCheck = focusCheck;
        this.priority = priority;
    }

    public Process(int pid, int arrTime, int burtTime, int priority) {
        this.pid = pid;
        this.arrTime = arrTime;
        this.burtTime = burtTime;
        this.priority = priority;
    }
    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getArrTime() {
        return arrTime;
    }

    public void setArrTime(int arrTime) {
        this.arrTime = arrTime;
    }

    public int getBurtTime() {
        return burtTime;
    }

    public void setBurtTime(int burtTime) {
        this.burtTime = burtTime;
    }

    public int getCompTime() {
        return compTime;
    }

    public void setCompTime(int compTime) {
        this.compTime = compTime;
    }

    public int getTurnArrTime() {
        return turnArrTime;
    }

    public void setTurnArrTime(int turnArrTime) {
        this.turnArrTime = turnArrTime;
    }

    public int getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }

    public int getFocusCheck() {
        return focusCheck;
    }

    public void setFocusCheck(int focusCheck) {
        this.focusCheck = focusCheck;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
    
}
