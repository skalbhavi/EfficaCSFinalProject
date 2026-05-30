import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Comparator;

/**
 * Manages a collection of tasks and provides methods for adding, removing, 
 * sorting, editing, & retrieving tasks 
 */

public class TaskManager {
    
    private ArrayList<Task> tasks;

    /**
     * Creates an empty task manager
     */
    public TaskManager() {
        tasks = new ArrayList<Task>();
    }
    

    /**
     * Adds a task to the task manager
     * @param task
     */
    public void addTask(Task task) {
        tasks.add(task);
    }

    
    /**
     * Removes a task with the specified task ID
     * @param taskID
     * @return true if the task was correctly removed; false otherwise 
     */
    public boolean removeTask(long taskID) {
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getTaskID() == taskID) {
                tasks.remove(i);
                return true;
            }
        }

        return false;
    }

    /**
     * Remove the specified task 
     * @param task
     */

    public void removeTask(Task task) {
        tasks.remove(task);
    }

    
    /**
     * Finds a task using its task ID 
     * @param taskID
     * @return the matching task, or null if no task found 
     */
    public Task findTaskById(long taskID) {
        for (Task task : tasks) {
            if (task.getTaskID() == taskID) {
                return task;
            }
        }

        return null;
    }


    /**
     * * Updates information of an existing task 
     * 
     * @param taskID
     *  @param taskName
     * @param dueDate
     * @param estimatedTime
     * @param classGrade
     * @param priority
     * @return true if the task is sucessfully updated; false otherwise 
     * */
    public boolean editTask(long taskID, String taskName, LocalDateTime dueDate, int estimatedTime, int classGrade, int priority) {
        Task task = findTaskById(taskID);

        if (task == null) {
            return false;
        }

        task.setTaskName(taskName);
        task.setDueDate(dueDate);
        task.setEstimatedTime(estimatedTime);
        task.setClassGrade(classGrade);
        task.setPriority(priority);

        return true;
    }

    /**
     * Marks a task as completed
     * 
     * @param taskID
     * @return true if the task was  found & completed; false otherwise
     */

    public boolean markComplete(long taskID) {
        Task task = findTaskById(taskID);

        if (task == null) {
            return false;
        }

        task.setStatus(true);
        return true;
    }

    /**
     * Returns a copy of all tasks currently stored
     * @return an Arraylist containing all tasks 
     */

    public ArrayList<Task> getAllTasks() {
        return new ArrayList<Task>(tasks);
    }

    /**
     * Returns all completed tasks 
     * @return an Arraylist of completed tasks 
     */

    public ArrayList<Task> getComplete() {
        ArrayList<Task> complete = new ArrayList<Task>();

        for (Task task : tasks) {
            if (task.getStatus()) {
                complete.add(task);
            }
        }

        return complete;
    }

    /**
     * Returns all incomplete tasks 
     * @return an Arraylist of incomplete tasks 
     */

    public ArrayList<Task> getIncomplete() {
        ArrayList<Task> incomplete = new ArrayList<Task>();

        for (Task task : tasks) {
            if (!task.getStatus()) {
                incomplete.add(task);
            }
        }

        return incomplete;
    }

    /**
     * Creates a priority queue of incomplete tasks based on
     * the selected sorting mode 
     * @param mode 
     * @param currentTime 
     * @return a priority queue containing the sorted incomplete tasks
     */

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

    /**
     * Returns the highest priority incomplete task
     * 
     * @param mode
     * @param currentTime
     * @return the highest priority task, or null if it doesn't exist
     */

    public Task getTopPRTY(SortMode mode, String currentTime) {
        PriorityQueue<Task> pq = makePriorityQueue(mode, currentTime);

        if (pq.isEmpty()) {
            return null;
        }

        return pq.peek();
    }

    /**
     * Returns all tasks sorted according to the selected mode
     * 
     * @param mode
     * @param currentTime
     * @return
     */

    public ArrayList<Task> getAllSortedTasks(SortMode mode, String currentTime) {
        PriorityQueue<Task> pq = makePriorityQueue(mode, currentTime);
        ArrayList<Task> sortedTasks = new ArrayList<Task>();

        while (!pq.isEmpty()) {
            sortedTasks.add(pq.poll());
        }

        return sortedTasks;
    }

    /**
     * Returns tasks sorted according to the selected mode
     * @param mode
     * @param currentTime
     * @return
     */

    public ArrayList<Task> getTasksSortedBy(SortMode mode, String currentTime) {
        return getAllSortedTasks(mode, currentTime);
    }

    /**
     * Removes all completed tasks from the task manager
     */

    public void clearCompleted() {
        for (int i = tasks.size() - 1; i >= 0; i--) {
            if (tasks.get(i).getStatus()) {
                tasks.remove(i);
            }
        }
    }

    /**
     * Returns the total number of tasks currently stored
     * @return the number of tasks 
     */
    public int size() {
        return tasks.size();
    }
}