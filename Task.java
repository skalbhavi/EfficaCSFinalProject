import java.time.LocalDateTime;  

public class Task {

    private String assignmentName; 
    private int estimatedTime; 
    private double ClassGrade; 
    private LocalDateTime dueDate; 
    private boolean status;
    private long ID;
    private int priority;

    public Task(String name, int estimatedMins, double grade, LocalDateTime dueDate, boolean status, int priority) {
        this.assignmentName = name; 
        this.estimatedTime = estimatedMins; 
        this.ClassGrade = grade; 
        this.dueDate = dueDate; 
        this.status = status; 
        ID = 0;
        for (int i = 0; i < 10; i++) {
            ID = ID * 10 + (int)(Math.random()*10);
        }
        this.priority = priority;
    }

    public Task(String title,
            int estimatedMins,
            double classGrade,
            LocalDateTime dueDate,
            boolean status,
            int priority,
            long ID) {

        this.assignmentName = title;
        this.estimatedTime = estimatedMins;
        this.ClassGrade = classGrade;
        this.dueDate = dueDate;
        this.status = status;
        this.priority = priority;
        this.ID = ID;
    }

    public String getAssignmentName(){ return assignmentName;}
    public void setAssignmentName(String title) {this.assignmentName = title;} 

    public int getEstimatedTime() {return estimatedTime;}
    public void setEstimatedTime(int estimatedMins) {this.estimatedTime = estimatedMins;}

    public double getClassGrade() {return ClassGrade;}
    public void setClassGrade(double grade) {this.ClassGrade = grade;}

    public LocalDateTime getDueDate() {return dueDate;}
    public void setDueDate(LocalDateTime dueDate) {this.dueDate = dueDate;}

    public boolean getStatus() {return status;}
    public void setStatus(boolean status) {this.status = status;}

    public String toString() {
        return assignmentName  + " | Estimated Time: " + estimatedTime + " | Due: " + dueDate + "| Status: " + status ; 
    }

    public long getID() {
        return ID;
    }

    public int getPriority() {
        return priority;
    }
    
    public void setPriority(int m) {
        this.priority = m;
    }



}
