import java.time.LocalDateTime;  

public class Task {

    private String title; 
    private int estimatedMins; 
    private String className; 
    private double grade; 
    private LocalDateTime dueDate; 
    private boolean status;
    private long ID;
    private int priority;

    public Task(String title, int estimatedMins, String className, double grade, LocalDateTime dueDate, boolean status, int priority) {
        this.title = title; 
        this.estimatedMins = estimatedMins; 
        this.className = className; 
        this.grade = grade; 
        this.dueDate = dueDate; 
        this.status = status; 
        ID = 0;
        for (int i = 0; i < 10; i++) {
            ID = ID * 10 + (int)(Math.random()*10);
        }
        this.priority = priority;
    }

    public String getTitle(){ return title;}
    public void setTitle(String title) {this.title = title;} 

    public int getEstimatedMins() {return estimatedMins;}
    public void setEstimatedMins(int estimatedMins) {this.estimatedMins = estimatedMins;}

    public String getClassName() {return className;}
    public void setClassName(String className) {this.className = className;}

    public double getGrade() {return grade;}
    public void setGrade(double grade) {this.grade = grade;}

    public LocalDateTime getDueDate() {return dueDate;}
    public void setDueDate(LocalDateTime dueDate) {this.dueDate = dueDate;}

    public boolean getStatus() {return status;}
    public void setStatus(boolean status) {this.status = status;}

    public String toString() {
        return title  + " | Estimated Time: " + estimatedMins + " | Due: " + dueDate + "| Status: " + status ; 
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
