import java.awt.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import javax.swing.*;

class CalendarPanel extends JPanel {

    private Controller controller;
    private WeeklyCalendar cal;

    public CalendarPanel(Controller controller) {
        this.controller = controller;
        this.cal = new WeeklyCalendar(controller.getTaskManager());

        setLayout(new GridLayout(1, 7));
        setBackground(new Color(18, 18, 18));

        LocalDateTime now = LocalDateTime.now();

        for (int i = 0; i < 7; i++) {
            LocalDateTime day = now.plusDays(i);
            add(createDayPanel(day));
        }
    }

    private JPanel createDayPanel(LocalDateTime day) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(32, 32, 32));

        JLabel label = new JLabel(day.getMonthValue() + "/" + day.getDayOfMonth());
        label.setForeground(Color.WHITE);
        panel.add(label);

        ArrayList<Task> tasks = cal.getTasksForDate(
            String.format("%04d-%02d-%02d",
                day.getYear(),
                day.getMonthValue(),
                day.getDayOfMonth())
        );

        for (Task t : tasks) {
            JLabel taskLabel = new JLabel("• " + t.getTitle());
            taskLabel.setForeground(Color.LIGHT_GRAY);
            panel.add(taskLabel);
        }

        return panel;
    }
}