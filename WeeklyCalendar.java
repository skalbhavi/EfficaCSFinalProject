import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

/**
 * Provides calendar based task orgnization and retrieval
 * Takes information stored in the task manager 
 * Tasks can be viewed by date, week, or overdue status
 */



public class WeeklyCalendar {

    
    private TaskManager taskManager;

    /**
     * * Creates a weekly calendar linked to a Task Manager
     * @param taskManager the task manager containing tasks 
     * 
     */
    public WeeklyCalendar(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    /**
     * Converts date string into a Local Date object
     * @param dateText
     * @return the corresponding LocalDate or null if the date is invalid
     */

    private LocalDate parseDate(String dateText) {
        if (dateText == null || dateText.isBlank()) return null;

        try {
            return LocalDate.parse(dateText);
        } catch (DateTimeParseException e) {
            try {
                String[] parts = dateText.split("-");
                if (parts.length == 3) {
                    return LocalDate.of(
                        Integer.parseInt(parts[0]),
                        Integer.parseInt(parts[1]),
                        Integer.parseInt(parts[2]) 
                    );
                }
            } catch (Exception ex) {
                return null; 
            }
        }
        return null;
    }

    /**
     * Returns all tasks due on a specific date 
     * @param date
     * @return an ArrayList containing tasks due on that date
     */
    public ArrayList<Task> getTasksForDate(String date) {
        ArrayList<Task> result = new ArrayList<>();
        LocalDate target = parseDate(date);

        if (target == null) {
            return result;
        }

        for (Task task : taskManager.getAllTasks()) {
            if (task.getDueDate() != null && task.getDueDate().toLocalDate().isEqual(target)) {
                result.add(task);
            }
        }

        return result;
    }

    /**
     * Returns all tasks due on a specific date
     * @param startDate
     * @return the corresponding LocalDate or null if date is invalid
     */

    public ArrayList<Task> getTasksForWeek(String startDate) {
        ArrayList<Task> result = new ArrayList<>();
        LocalDate start = parseDate(startDate);

        if (start == null) {
            return result;
        }

        LocalDate end = start.plusDays(7);

        for (Task task : taskManager.getAllTasks()) {
            if (task.getDueDate() == null) {
                continue;
            }

            LocalDate due = task.getDueDate().toLocalDate();
            if ((due.isEqual(start) || due.isAfter(start)) && due.isBefore(end)) {
                result.add(task);
            }
        }

        return result;
    }

    /**
     * Returns all tasks on a specific date
     * @param currentDate
     * @return an Arraylist containing tasks due on that date
     */

    public ArrayList<Task> getTodayTasks(String currentDate) {
        return getTasksForDate(currentDate);
    }

    /**
     * Returns all all incomplete tasks whose due dates have passed
     * @return an Arraylist containing overdue tasks 
     */

    public ArrayList<Task> getOverdueTasks(String currentDate) {
        ArrayList<Task> overdue = new ArrayList<>();
        LocalDate current = parseDate(currentDate);

        if (current == null) {
            return overdue;
        }

        for (Task task : taskManager.getAllTasks()) {
            if (task.getDueDate() != null &&
                task.getDueDate().toLocalDate().isBefore(current) &&
                !task.getStatus()) {
                overdue.add(task);
            }
        }

        return overdue;
    }
    
    /**
     * Counts the number of tasks due on a specific date
     * @param date
     * @return the number of tasks due on that date 
     */
    public int countTasksForDate(String date) {
        return getTasksForDate(date).size();
    }
}