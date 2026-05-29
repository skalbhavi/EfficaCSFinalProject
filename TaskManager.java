import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.Collections; 

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

    public boolean editTask(long taskID, String title, String course, int minutes, String dueDate, double grade) {
        Task task = findTaskById(taskID);

        if (task == null) {
            return false;
        }

        task.setTitle(title);
        task.setClassName(course);
        task.setEstimatedMins(minutes);
        task.setDueDate(dueDate);
        task.setGrade(grade);

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

    public PriorityQueue<Task> makePriorityQueue(String currentTime) {
        Comparator<Task> taskPriorityComparator = new Comparator<Task>() {

    @Override
    public int compare(Task taskA, Task taskB) {
        int priorityA = PriorityCalculator.duePRTY(taskA, currentTime);
        int priorityB = PriorityCalculator.duePRTY(taskB, currentTime);

        if (priorityA != priorityB) {
            return Integer.compare(priorityA, priorityB);
        }

        return taskA.getDueDate().compareTo(taskB.getDueDate());
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

    //////////////////////

    public Task getTopPRTY(String currentTime) {
        PriorityQueue<Task> pq = makePriorityQueue(currentTime);

        if (pq.isEmpty()) {
            return null;
        }

        return pq.peek();
    }

    public ArrayList<Task> getAllSortedTasks(String currentTime) {
        PriorityQueue<Task> pq = makePriorityQueue(currentTime);
        ArrayList<Task> sortedTasks = new ArrayList<Task>();

        while (!pq.isEmpty()) {
            sortedTasks.add(pq.poll());
        }

        return sortedTasks;
    }

    public ArrayList<Task> getTasksSortedBy(SortMode mode, String currentTime) {
        if (mode == SortMode.PRIORITY_SCORE) {
            return getAllSortedTasks(currentTime);
        }

        ArrayList<Task> copy = new ArrayList<Task>(tasks);
        Comparator<Task> comparator = TaskComparator.getComparator(mode);
        Collections.sort(copy, comparator);

        return copy;
    }

    public ArrayList<Task> getTasksSortedBy(SortMode mode) {
        ArrayList<Task> copy = new ArrayList<Task>(tasks);
        Comparator<Task> comparator = TaskComparator.getComparator(mode);
        Collections.sort(copy, comparator);

        return copy;
    }

    public void clearCompleted() {
        for (int i = tasks.size()-1; i >= 0; i--) {
            if (tasks.get(i).getStatus()) {
                tasks.remove(i);
            }
        }
    }

    public int size() {
        return tasks.size();
    }
}
