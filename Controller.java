import java.time.LocalDateTime;
import java.util.ArrayList;

public class Controller {
    
    private TaskManager taskManager;
    private Timer timer;
    private WeeklyCalendar calendar;
    private Task activeTask;
    
    public Controller() {
        taskManager = new TaskManager();
        timer = new Timer(25, 5);
        calendar = new WeeklyCalendar(taskManager);
    }

    public TaskManager getTaskManager() {
        return taskManager;
    }
    
    public void addTask(String title, int estimatedMins, double grade, LocalDateTime dueDate, boolean status, int priority) {
        Task task = new Task(title, estimatedMins, grade, dueDate, status, priority);
        taskManager.addTask(task);
    }

    public boolean removeTask(long taskID) {
        return taskManager.removeTask(taskID);
    }

    public boolean editTask(long taskId, String newTitle, int newMinutes, LocalDateTime newDueDate, double newGrade, int newPriority) {
        return taskManager.editTask(taskId, newTitle, newMinutes, newDueDate, newGrade, newPriority);
    }

    public boolean markTaskComplete(long taskID) {
        return taskManager.markComplete(taskID);
    }

    public ArrayList<Task> getAllTasks() {
        return taskManager.getAllTasks();
    }

    public void setActiveTask(Task task) {
        activeTask = task;
    }

    public Task getActiveTask() {
        return activeTask;
    }

public ArrayList<Task> getTasksSortedBy(SortMode mode, String currentTime) {
    return taskManager.getTasksSortedBy(mode, currentTime);
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
