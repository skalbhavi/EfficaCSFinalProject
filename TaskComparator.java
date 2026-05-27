import java.util.Comparator;

public class TaskComparator {

    public static Comparator<Task> getComparator(SortMode mode) {
        switch(mode) {
            case DUE_DATE: 
                return TaskComparator :: compareByDueDate;
            case ESTIMATED_TIME: 
                return TaskComparator :: compareByEstimatedTime;
            case GRADE: 
                return TaskComparator :: compareByGrade; 
            case CUSTOM_PRIORITY: 
                return TaskComparator :: compareByPriority;
            case COURSE: 
                return TaskComparator :: compareByCourse;
            case COMPLETED: 
                return TaskComparator :: compareByCompletion; 
            case ALPHABETICAL:  
                return TaskComparator :: compareByTitle;

            case NONE: 
            default: 
                return (a,b) ->  0; 
        }

    }

    public static int compareByDueDate(Task a, Task b) {
        return a.getDueDate().compareTo(b.getDueDate()); 
    }

    public static int compareByEstimatedTime(Task a, Task b) {
        return Integer.compare(a.getEstimatedMins(), b.getEstimatedMins()); 
    }

    public static int compareByPriority(Task a, Task b) {
        return Integer.compare(b.getPriority(), a.getPriority());
    }

    public static int compareByGrade(Task a, Task b) {
        return Double.compare(b.getGrade(), a.getGrade()); 
    }

    public static int compareByCourse(Task a, Task b) { 
        if (a.getClassName() == null && b.getClassName() == null) {
            return 0; 
        }
        else if (a.getClassName() == null || b.getClassName() == null) {
            return -1; 
        }
        return a.getClassName().compareToIgnoreCase(b.getClassName());
        
    }

    public static int compareByCompletion(Task a, Task b) {
        return Boolean.compare(a.getStatus(), b.getStatus()); 
    } 

    public static int compareByTitle(Task a, Task b) {
        return a.getTitle().compareToIgnoreCase(b.getTitle()); 
    }
}
