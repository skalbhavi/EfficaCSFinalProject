import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.PriorityQueue;
import java.util.Comparator;

public class TaskManager {
    
    private ArrayList<Task> tasks;

    public TaskManager() {
        tasks = new ArrayList<Task>();
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    public boolean removeTask(long taskID) {
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getID() == taskID) {
                tasks.remove(i);
                return true;
            }
        }

        return false;
    }

    public void removeTask(Task task) {
        tasks.remove(task);
    }

    public Task findTaskById(long taskID) {
        for (Task task : tasks) {
            if (task.getID() == taskID) {
                return task;
            }
        }

        return null;
    }

    public boolean editTask(long taskID, String title, int minutes, LocalDateTime dueDate, double grade, int priority) {
        Task task = findTaskById(taskID);

        if (task == null) {
            return false;
        }

        task.setAssignmentName(title);
        task.setEstimatedTime(minutes);
        task.setDueDate(dueDate);
        task.setClassGrade(grade);
        task.setPriority(priority);

        return true;
    }

    public boolean markComplete(long taskID) {
        Task task = findTaskById(taskID);

        if (task == null) {
            return false;
        }

        task.setStatus(true);
        return true;
    }

    public ArrayList<Task> getAllTasks() {
        return new ArrayList<Task>(tasks);
    }

    public ArrayList<Task> getComplete() {
        ArrayList<Task> complete = new ArrayList<Task>();

        for (Task task : tasks) {
            if (task.getStatus()) {
                complete.add(task);
            }
        }

        return complete;
    }

    public ArrayList<Task> getIncomplete() {
        ArrayList<Task> incomplete = new ArrayList<Task>();

        for (Task task : tasks) {
            if (!task.getStatus()) {
                incomplete.add(task);
            }
        }

        return incomplete;
    }

    //////////////

    public PriorityQueue<Task> makePriorityQueue(SortMode mode, String currentTime) {
        Comparator<Task> taskPriorityComparator = TaskComparator.getComparator(mode, currentTime);
        PriorityQueue<Task> pq = new PriorityQueue<Task>(taskPriorityComparator);

        for (Task task : tasks) {
            if (!task.getStatus()) {
                pq.add(task);
            }
        }

        return pq;
    }

    //////////////////////

    public Task getTopPRTY(SortMode mode, String currentTime) {
        PriorityQueue<Task> pq = makePriorityQueue(mode, currentTime);

        if (pq.isEmpty()) {
            return null;
        }

        return pq.peek();
    }

    public ArrayList<Task> getAllSortedTasks(SortMode mode, String currentTime) {
        PriorityQueue<Task> pq = makePriorityQueue(mode, currentTime);
        ArrayList<Task> sortedTasks = new ArrayList<Task>();

        while (!pq.isEmpty()) {
            sortedTasks.add(pq.poll());
        }

        return sortedTasks;
    }

    public ArrayList<Task> getTasksSortedBy(SortMode mode, String currentTime) {
        return getAllSortedTasks(mode, currentTime);
    }

    public void clearCompleted() {
        for (int i = tasks.size() - 1; i >= 0; i--) {
            if (tasks.get(i).getStatus()) {
                tasks.remove(i);
            }
        }
    }

    public int size() {
        return tasks.size();
    }
}