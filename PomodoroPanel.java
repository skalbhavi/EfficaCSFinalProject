import java.awt.*;
import javax.swing.*;

class PomodoroPanel extends JPanel {

    private Controller controller;
    private JLabel taskLabel;
    private JLabel timeLabel;

    private javax.swing.Timer swingTimer;

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

        JButton start = new JButton("Start");
        JButton pause = new JButton("Pause");

        start.addActionListener(e -> {
            controller.startPomodoro(25, 5);

            swingTimer = new javax.swing.Timer(1000, ev -> {
                Timer t = controller.getTimer();
                if (t == null) return;

                t.tick();
                timeLabel.setText(t.getFormattedTime());

                Task active = controller.getActiveTask();
                if (active != null) {
                    taskLabel.setText("Working on: " + active.getTitle());
                }
            });

            swingTimer.start();
        });

        pause.addActionListener(e -> {
            if (controller.getTimer() != null) {
                controller.getTimer().pause();
            }
            if (swingTimer != null) {
                swingTimer.stop();
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

        add(center, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);
    }
}