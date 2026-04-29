import java.util.spi.CalendarDataProvider;
import java.util.ArrayList;

public class Calendar {

    private TaskManager taskManager; 

    public Calendar(TaskManager taskManager) {
        this.taskManager = taskManager; 
    }

    public ArrayList<Task> getTasksForDate(String date) {
        // TODO 
    }

    public ArrayList<Task> getTasksForWeek(String startDate) {
        // TODO
    }

    public ArrayList<Task> getTodayTasks(String currentDate) {
        // TODO
    }

    public ArrayList<Task> getOverdueTasks(String currentDate) {
        // TODO 
    }

    public int countTasksForDate(String date) {
        // TODO 
    }







}
