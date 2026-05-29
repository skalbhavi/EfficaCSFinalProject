import java.time.LocalDateTime;
import java.util.ArrayList;


public class Controller {
    
    private TaskManager taskManager;
    private Timer timer;
    private WeeklyCalendar calendar;
    private User currentUser;
    private SortMode currentSortMode;
    private String currentDate;
    
    public Controller() {
        taskManager = new TaskManager();
        timer = new Timer(25, 5);
        calendar = new WeeklyCalendar(taskManager);
    }

    public TaskManager getTaskManager() {
        return taskManager;
    }
    
    public void addTask(String title, int estimatedMins, String className, double grade, LocalDateTime dueDate, boolean status, int priority) {
        Task task = new Task(title, estimatedMins, className, grade, dueDate, status, priority);
        taskManager.addTask(task);
        timer = new Timer(25, 5);
    }


    public boolean removeTask(long taskId) {
        return taskManager.removeTask(taskId);
    }

    public boolean editTask(long taskId, String newTitle, String newCourse, int newMinutes, LocalDateTime newDueDate, int newPriority) {
        return taskManager.editTask(taskId, newTitle, newCourse, newMinutes, newDueDate, newPriority);
    }

    public boolean markTaskComplete(long taskId) {
        return taskManager.markComplete(taskId);
    }

    public ArrayList<Task> getAllTasks() {
        return taskManager.getAllTasks();
    }

    public ArrayList<Task> getTasksSortedBy(SortMode mode) {
        return taskManager.getTasksSortedBy(mode);
    }

    public void startPomodoro(int work, int breakMin) {
        timer = new Timer(work, breakMin);
        timer.start();
    }

    public WeeklyCalendar getCalendar() {
    return calendar;
}

    // Ensure the timer is never null to avoid NullPointerExceptions in PomodoroPanel
    public Timer getTimer() {
        if (timer == null) {
            timer = new Timer(25, 5);
        }
        return timer;
    }
}
