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
      
        ArrayList<Task> targetResult = new ArrayList<Task>();
        LocalDateTime targetDate = makeDateTime(date);

        for (Task task: taskManager.getAllTasks()) {
            LocalDateTime due = task.getDueDate(); 

            if(due.getYear() == targetDate.getYear() && due.getMonthValue() == targetDate.getMonthValue() && due.getDayOfMonth() == targetDate.getDayOfMonth()) {
                targetResult.add(task); 
            }
        }
        return targetResult; 


    }

    public ArrayList<Task> getTasksForWeek(String startDate) {

        ArrayList<Task> result = new ArrayList<Task>();
        LocalDateTime start = makeDateTime(startDate);
        LocalDateTime end = start.plusDays(7);

        for(Task task: taskManager.getAllTasks()) {
            LocalDateTime due = task.getDueDate(); 
            boolean afterStart = due.isEqual(start) || due.isAfter(start); 
            boolean beforeEnd = due.isBefore(end); 

            if(afterStart && beforeEnd) {
                result.add(task); 
            }

        }
        return result; 
    }

    public ArrayList<Task> getTodayTasks(String currentDate) {
       
        return getTasksForDate(currentDate); 
    }

    public ArrayList<Task> getOverdueTasks(String currentDate) {
    
        ArrayList<Task> overdue = new ArrayList<Task>();
        LocalDateTime current = makeDateTime(currentDate); 

        for(Task task : taskManager.getAllTasks()) {
            LocalDateTime due = task.getDueDate(); 

            if(due.isBefore(current) && !task.getStatus()) {
                overdue.add(task); 
            }
        }
        return overdue; 

    }

    public int countTasksForDate(String date) {

        return getTasksForDate(date).size(); 
    }

}