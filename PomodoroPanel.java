import java.awt.*;
import javax.swing.*;

/**
 * Provides a Pomodoro timer panel for the application
 * Displays the current task, remaining time, and controls for 
 * starting, pausing, & resuming the productivity timer.
 */

class PomodoroPanel extends JPanel {

    private Controller controller;
    private JLabel taskLabel;
    private JLabel timeLabel;

    /**
     * Creates a Pomodoro panel connected to the application 
     * controller
     * @param controller
     */
    public PomodoroPanel(Controller controller) {
        this.controller = controller;

        setLayout(new BorderLayout());
        setBackground(new Color(18, 18, 18));

        taskLabel = new JLabel("No active task");
        taskLabel.setForeground(Color.WHITE);
        taskLabel.setFont(new Font("SansSerif", Font.BOLD, 18));

        timeLabel = new JLabel("25:00");
        timeLabel.setForeground(Color.WHITE);
        timeLabel.setFont(new Font("SansSerif", Font.BOLD, 40));

        new javax.swing.Timer(500, e -> {
            Timer t = controller.getTimer();

            if (t != null) {
                timeLabel.setText(t.getFormattedTime());

                Task active = controller.getActiveTask();

                if (active != null) {
                    taskLabel.setText("Working on: " + active.getTaskName());
                } else {
                    taskLabel.setText("No active task");
                }
            }
        }).start();

        JButton start = new JButton("Start");
        JButton pause = new JButton("Pause");
        JButton resume = new JButton("Resume");

        start.addActionListener(e -> {
            controller.startPomodoro(25, 5);
        });

        pause.addActionListener(e -> {
            Timer t = controller.getTimer();

            if (t != null) {
                t.pause();
            }
        });

        resume.addActionListener(e -> {
            Timer t = controller.getTimer();

            if (t != null) {
                t.start();
            }
        });

        JPanel center = new JPanel();
        center.setBackground(new Color(18, 18, 18));
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));

        taskLabel.setAlignmentX(CENTER_ALIGNMENT);
        timeLabel.setAlignmentX(CENTER_ALIGNMENT);

        center.add(taskLabel);
        center.add(Box.createVerticalStrut(20));
        center.add(timeLabel);

        JPanel buttons = new JPanel();
        buttons.setBackground(new Color(18, 18, 18));
        buttons.add(start);
        buttons.add(pause);
        buttons.add(resume);

        add(center, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);
    }
}