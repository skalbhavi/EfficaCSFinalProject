import java.awt.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import javax.swing.*;

class CalendarPanel extends JPanel {

    private Controller controller;

    public CalendarPanel(Controller controller) {
        this.controller = controller;

        setLayout(new GridLayout(1, 7, 5, 5));
        setBackground(new Color(18, 18, 18));

        refreshCalendar();
    }

    public void refreshCalendar() {
        this.removeAll();
        
        LocalDateTime now = LocalDateTime.now();

        for (int i = 0; i < 7; i++) {
            LocalDateTime day = now.plusDays(i);
            add(createDayPanel(day));
        }

        this.revalidate();
        this.repaint();
    }

    private JPanel createDayPanel(LocalDateTime day) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(32, 32, 32));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));

        JLabel label = new JLabel(day.getMonthValue() + "/" + day.getDayOfMonth());
        label.setForeground(Color.WHITE);
        label.setFont(new Font("SansSerif", Font.BOLD, 14));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(label);
        panel.add(Box.createVerticalStrut(10));

        String date = String.format("%d-%02d-%02d",
                day.getYear(),
                day.getMonthValue(),
                day.getDayOfMonth());

        ArrayList<Task> tasks = filterTasksForDate(controller.getCalendarTasks(), date);
        
        for (Task t : tasks) {
            boolean overdue = !t.getStatus() && t.getDueDate().isBefore(LocalDateTime.now());
            String labelText;
            if (overdue) {
                labelText = "OVERDUE: " + t.getTaskName();
            } else {
                labelText = "• " + t.getTaskName();
            }
            JLabel taskLabel = new JLabel(labelText);
            if (overdue) {
                taskLabel.setForeground(Color.RED);
            } else {
                taskLabel.setForeground(Color.LIGHT_GRAY);
            }
            taskLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
            taskLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(taskLabel);
            panel.add(Box.createVerticalStrut(2));
        }

        return panel;
    }

    private ArrayList<Task> filterTasksForDate(ArrayList<Task> tasks, String date) {
        ArrayList<Task> result = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getDueDate() == null) continue;
            String taskDate = String.format("%d-%02d-%02d",
                    task.getDueDate().getYear(),
                    task.getDueDate().getMonthValue(),
                    task.getDueDate().getDayOfMonth());

            if (taskDate.equals(date)) {
                result.add(task);
            }
        }

        return result;
    }
}