import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections; 
import java.util.Comparator;
import java.util.PriorityQueue;

public class TaskManager {
    
    private ArrayList<Task> tasks;

    public TaskManager() {
        tasks = new ArrayList<Task>();
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    public PriorityQueue<Task> makePriorityQueue(String currentTime) {
        Comparator<Task> taskPriorityComparator = new Comparator<Task>() {

            @Override
            public int compare(Task a, Task b) {
                int priorityA = PriorityCalculator.duePRTY(a, currentTime);
                int priorityB = PriorityCalculator.duePRTY(b, currentTime);

                return Integer.compare(priorityA, priorityB);
            }
        };

        PriorityQueue<Task> pq = new PriorityQueue<Task>(taskPriorityComparator);

        for (Task task : tasks) {
            if (!task.getStatus()) {
                pq.add(task);
            }
        }

        return pq;

    }

    public Task getHighestPriorityTask(String currentTime) {
        PriorityQueue<Task> pq = makePriorityQueue(currentTime);

        if (pq.isEmpty()) {
            return null;
        }

        return pq.peek();
    }

    public ArrayList<Task> getTasksByPriority(String currentTime) {
        PriorityQueue<Task> pq = makePriorityQueue(currentTime);
        ArrayList<Task> sortedTasks = new ArrayList<Task>();

        while (!pq.isEmpty()) {
            sortedTasks.add(pq.poll());
        }

        return sortedTasks;
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

    public boolean markComplete(long taskId) {
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
        ArrayList<Task> copy = new ArrayList<Task>(tasks);
        Comparator<Task> comparator = TaskComparator.getComparator(mode);
        Collections.sort(copy, comparator);
        return copy;
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



/*
    
*/
