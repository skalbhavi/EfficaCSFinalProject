
/**
 * Represents a productivity timer that alternates between
 * work sessions & break sessions. 
 * The timer can be started, paused, reset, & switched between
 * work & break modes. 
 */

public class Timer {
    
    private int workMinutes;
    private int breakMinutes;
    private int remainingSeconds;
    private boolean running;
    private boolean onBreak;
    
    /**
     * Creates a new timer with specified work & break durations
     * @param workMinutes
     * @param breakMinutes
     */
    public Timer(int workMinutes, int breakMinutes) {
        this.workMinutes = workMinutes;
        this.breakMinutes = breakMinutes;
        remainingSeconds = workMinutes*60;
        running = false;
        onBreak = false;
    }

    /** 
     * Starts the timer
     */

    public void start() {
        running = true;
    }

    /**
     * Pauses the timer
     */

    public void pause() {
        running = false;
    }

    /**
     * Resets the timer to the beginning of the work session
     */

    public void reset() {
        running = false;
        onBreak = false;
        remainingSeconds = workMinutes*60;
    }

    /**
     * Decreases the remaining time by 1 second if 
     * the timer is running. 
     * When the timer reaches zero, it automatically switches
     * between work and break modes. 
     */

    public void tick() {
        if (running && remainingSeconds > 0) {
            remainingSeconds--;
        }
        if (remainingSeconds == 0) {
            switchMode();
        }
    }

    /**
     * Switches the timer between work & break mode 
     * The remaining time is updated to match the selected mode
     */
    public void switchMode() {
        onBreak = !onBreak;
        if (onBreak) {
            remainingSeconds = breakMinutes * 60;
        }
        else {
            remainingSeconds = workMinutes * 60;
        }
    }

    /**
     * Returns the number of seconds remaining in the current session 
     * @return the remaining time in seconds
     */
    public int getRemainingSeconds() {
        return remainingSeconds;
    }

    /**
     * Indicates whether the timer is currently running
     * @return true if the time running; false otherwise
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * Indicates whether the timer is currently in break mode
     * @return true if the time is on a break; false otherwise
     */
    public boolean isOnBreak() {
        return onBreak;
    }

    /**
     * Returns a label describing the current timer mode
     * @return "Break Time!" if on a break; otherwise "Focusing . . ."
     */

    public String getStatusLabel() {
        return onBreak ? "Break Time!" : "Focusing...";
    }

    /**
     * Returns the remaining time in MM : SS format 
     * @return the formatted remaining time 
     */

    public String getFormattedTime() {
        int minutes = remainingSeconds / 60;
        int seconds = remainingSeconds % 60;
        String secondString;
        if (seconds < 10) {
            secondString = "0" + seconds;
        }
        else {
            secondString = "" + seconds;
        }
        return minutes + ":" + secondString;
    }
}
