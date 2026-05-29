import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

public class WeeklyCalendar {

    private TaskManager taskManager;

    public WeeklyCalendar(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

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

    public ArrayList<Task> getTodayTasks(String currentDate) {
        return getTasksForDate(currentDate);
    }

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

    public int countTasksForDate(String date) {
        return getTasksForDate(date).size();
    }
}