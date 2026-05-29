public class Timer {
    private int workMinutes;
    private int breakMinutes;
    private int remainingSeconds;
    private boolean running;
    private boolean onBreak;
    
    public Timer(int workMinutes, int breakMinutes) {
        this.workMinutes = workMinutes;
        this.breakMinutes = breakMinutes;
        remainingSeconds = workMinutes*60;
        running = false;
        onBreak = false;
    }

    public void start() {
        running = true;
    }

    public void pause() {
        running = false;
    }

    public void reset() {
        running = false;
        onBreak = false;
        remainingSeconds = workMinutes*60;
    }

    public void tick() {
        if (running && remainingSeconds > 0) {
            remainingSeconds--;
        }
        if (remainingSeconds == 0) {
            switchMode();
        }
    }

    public void switchMode() {
        onBreak = !onBreak;
        if (onBreak) {
            remainingSeconds = breakMinutes * 60;
        }
        else {
            remainingSeconds = workMinutes * 60;
        }
    }

    public int getRemainingSeconds() {
        return remainingSeconds;
    }

    public boolean isRunning() {
        return running;
    }

    public boolean isOnBreak() {
        return onBreak;
    }

    public String getStatusLabel() {
        return onBreak ? "Break Time!" : "Focusing...";
    }

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
