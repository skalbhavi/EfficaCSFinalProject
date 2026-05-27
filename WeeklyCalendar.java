import java.util.spi.CalendarDataProvider;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class WeeklyCalendar {

    private TaskManager taskManager; 

    public WeeklyCalendar(TaskManager taskManager) {
        this.taskManager = taskManager; 
    }


    // HANDLE DATES & TIMES
    private static LocalDateTime makeDateTime(String dateText) {
        int year = Integer.parseInt(dateText.substring(0, 4));
        int month = Integer.parseInt(dateText.substring(5, 7));
        int day = Integer.parseInt(dateText.substring(8, 10));

        int hour = 23;
        int minute = 59;

        if (dateText.length() >= 16) {
            hour = Integer.parseInt(dateText.substring(11, 13));
            minute = Integer.parseInt(dateText.substring(14, 16));
        }

        return LocalDateTime.of(year, month, day, hour, minute);

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