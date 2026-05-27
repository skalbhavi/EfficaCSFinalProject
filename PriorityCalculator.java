import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class PriorityCalculator {

        /*
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
            return -1 * (( Math.abs(hours) + 23 ) / 24);
        }
    }

    public static boolean isOverdue(Task task, String currentDate) {
        return dueScore(task, currentDate) < 0;
    }

    public static int dueScore(Task task, String currentTime) {
        LocalDateTime now = getTime(currentTime);
        LocalDateTime due = getTime(task.getDueDate());

        int untilDue;

        return 0;
    }


}
