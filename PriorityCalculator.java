import java.time.Duration;
import java.time.LocalDateTime;

public class PriorityCalculator {

    /*  SORT BY DUE DATE
    get current time
    get task due time
    hours until due = due time - current time
        assume 11:59pm / 23:59 if no time entered
        
    overdue tasks = lowest score, negative hours
    less hours until due = lower score
    more hours until due = higher score

    return int hours until due
        nonnegative score = floor hours until due
        negative score = ceiling hours overdue

    sort by priority (lower score = high priority)
        ascending order from overdue, due soon, due later
    */

    public static int duePRTY(Task task, String currentDate) {
        return dueScore(task, currentDate);
    }

    public static int untilDue(Task task, String currentDate) {
        int hours = dueScore(task, currentDate);

        if (hours >= 0) {
            return hours / 24; //int division to round down
        } else {
            return -1 * (( Math.abs(hours) + 23 ) / 24); //round up overdue days
        }
    }

    public static boolean isOverdue(Task task, String currentDate) {
        return dueScore(task, currentDate) < 0;
    }

    public static int dueScore(Task task, String currentTime) {
        LocalDateTime now = makeDateTime(currentTime);
        LocalDateTime due = task.getDueDate();

        long untilDue = Duration.between(now, due).toMinutes();

        if (untilDue >= 0) {
            return (int)(untilDue / 60); //floor hours remaining
        } else {
            long pastDue = Math.abs(untilDue);
            return (int)(-1 * ((pastDue + 59) / 60));
        }
    }

    /////////////////////////
    
    public static int gradePRTY(Task task) {
        return (int) task.getClassGrade();
    }

    public static int timePRTY(Task task) {
        return -1 * task.getEstimatedTime();
    }

    public static int customPRTY(Task task) {
        return -1 * task.getPriority();
    }

    ////////////////////////////

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
}
