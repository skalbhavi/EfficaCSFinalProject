import java.time.LocalDateTime;  


/**
 * A task is created, storing information on its name, due date, estimated time completion, associated
 *  class grade, completion status, priority, & task ID 
 */

public class Task {

    private String taskName; 
    private int estimatedTime; 
    private double classGrade; 
    private LocalDateTime dueDate; 
    private boolean status;
    private long taskID;
    private int priority;

   /**
    * Creates a new task & generates an unique Task ID 
    * 
    * @param taskName
    * @param dueDate
    * @param estimatedTime
    * @param classGrade
    * @param status
    * @param priority
    */
  
    public Task(String taskName, LocalDateTime dueDate, int estimatedTime, double classGrade, boolean status, int priority) {
        this.taskName = taskName; 
        this.dueDate = dueDate; 
        this.estimatedTime = estimatedTime; 
        this.classGrade = classGrade; 
        this.status = status; 
        taskID = 0;
        for (int i = 0; i < 10; i++) {
            taskID = taskID * 10 + (int)(Math.random()*10);
        }
        this.priority = priority;
    }

    /**
     * Creates a task using an existing Task ID 
     * 
     * @param taskName
     * @param dueDate
     * @param estimatedTime
     * @param classGrade
     * @param status
     * @param priority
     * @param taskID
     */

    public Task(String taskName,
            LocalDateTime dueDate,
            int estimatedTime,
            int classGrade,
            boolean status,
            int priority,
            long taskID) {

        this.taskName = taskName;
        this.classGrade = classGrade;
        this.estimatedTime = estimatedTime;
        this.dueDate = dueDate;
        this.status = status;
        this.priority = priority;
        this.taskID = taskID;
    }

    /** 
     * Getter & setter methods for each parameter
     */

    public String getTaskName(){ return taskName;}
    public void setTaskName(String taskName) {this.taskName = taskName;} 

    public LocalDateTime getDueDate() {return dueDate;}
    public void setDueDate(LocalDateTime dueDate) {this.dueDate = dueDate;}

    public int getEstimatedTime() {return estimatedTime;}
    public void setEstimatedTime(int estimatedTime) {this.estimatedTime = estimatedTime;}

    public double  getClassGrade() {return classGrade;}
    public void setClassGrade(double classGrade) {this.classGrade = classGrade;}

    public boolean getStatus() {return status;}
    public void setStatus(boolean status) {this.status = status;}

    public long getTaskID() {return taskID;}

    public int getPriority() {return priority;}
    
    public void setPriority(int m) {this.priority = m;}
    
    /**
     * Returns the task as a string
     * @return a string formatted with the correct information 
     */
    
    @Override
    public String toString() { 
        return taskName + " | Due: " + dueDate  + " | Estimated Time: " + estimatedTime + "| Status: " + status ; 
    }

   
}
