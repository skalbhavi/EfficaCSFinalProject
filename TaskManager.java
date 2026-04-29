import java.time.LocalDateTime;
import java.util.ArrayList; 

public class TaskManager {
    
    private ArrayList<Task> tasks;
    public TaskManager() {
        tasks = new ArrayList<Task>();
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    public boolean removeTask(long taskID) {
        for (Task task : tasks) {
            if (task.getID() == taskID) {
                tasks.remove(task);
                return true;
            }
        }
        return false;
    }

    public void removeTask(Task task) {
        tasks.remove(task);
    }

    public boolean editTask(long taskId, String title, String course, int minutes, LocalDateTime dueDate, int priority) {
        for (Task task : tasks) {
            if (task.getID() == taskId) {
                task.setTitle(title);
                task.setClassName(course);
                task.setEstimatedMins(minutes);
                task.setDueDate(dueDate);
                task.setPriority(priority);
                return true;
            }
        }
        return false;
    }

    public boolean markComplete(int taskId) {
        for (Task task : tasks) {
            if (task.getID() == taskId) {
                task.setStatus(true);
                return true;
            }
        }
        return false;
    }

    public ArrayList<Task> getAllTasks() {
        return tasks;
    }

    public ArrayList<Task> getIncompleteTasks() {
        ArrayList<Task> inc = new ArrayList<Task>();
        for (Task task : tasks) {
            if (task.getStatus() == false) inc.add(task);
        }
        return inc;
    }

    public ArrayList<Task> getCompleteTasks() {
        ArrayList<Task> comp = new ArrayList<Task>();
        for (Task task : tasks) {
            if (task.getStatus() == true) comp.add(task);
        }
        return comp;
    }

    public ArrayList<Task> getTasksSortedBy(SortMode mode) {
        
    }

    public void clearCompletedTasks() {
        for (Task task : tasks) {
            if (task.getStatus () == true) tasks.remove(task);
        }
    }

    public int size() {
        return tasks.size();
    }



}
