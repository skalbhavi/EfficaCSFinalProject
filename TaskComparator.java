import java.util.Comparator;

/**
 * Generates comparators used to sort tasks based on
 * different priority & scheduling strategies. 
 * The comparator returns based on the sortmode selected
 * to be used. 
 */

public class TaskComparator {
    /**
     * Return a comparator for task objects based on the
     * specified sorting mode. 
     * @param mode
     * @param currentTime
     * @return a comparator that sorts tasks according to the selected mode
     */
    public static Comparator<Task> getComparator(SortMode mode, String currentTime) {
        switch (mode) {
            case DUE_DATE:
                return new Comparator<Task>() {
                    public int compare(Task a, Task b) {
                        int result = Integer.compare(PriorityCalculator.duePRTY(a, currentTime), PriorityCalculator.duePRTY(b, currentTime));

                        if (result != 0) {
                            return result;
                        }

                        return a.getDueDate().compareTo(b.getDueDate());
                    }
                };

            case CLASS_GRADE:
                return new Comparator<Task>() {
                    public int compare(Task a, Task b) {
                        return Integer.compare(PriorityCalculator.gradePRTY(a), PriorityCalculator.gradePRTY(b));
                    }
                };

            case ESTIMATED_TIME_LONG_FIRST:
                return new Comparator<Task>() {
                    public int compare(Task a, Task b) {
                        int result = Integer.compare(PriorityCalculator.longTimePRTY(a), PriorityCalculator.longTimePRTY(b));

                        if (result != 0) {
                            return result;
                        }

                        return a.getDueDate().compareTo(b.getDueDate());
                    }
                };

            case ESTIMATED_TIME_SHORT_FIRST:
                return new Comparator<Task>() {
                    public int compare(Task a, Task b) {
                        int result = Integer.compare(PriorityCalculator.shortTimePRTY(a), PriorityCalculator.shortTimePRTY(b));
                        
                        if (result != 0) {
                            return result;
                        }

                        return a.getDueDate().compareTo(b.getDueDate());
                    }
                };

            case CUSTOM:
                return new Comparator<Task>() {
                    public int compare(Task a, Task b) {
                        return Integer.compare(PriorityCalculator.customPRTY(a), PriorityCalculator.customPRTY(b));
                    }
                };

            default:
                return new Comparator<Task>() {
                    public int compare(Task a, Task b) {
                        return 0;
                    }
                };
        }
    }
}
