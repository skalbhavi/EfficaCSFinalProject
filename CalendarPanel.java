import java.awt.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import javax.swing.*;

class CalendarPanel extends JPanel {

    private Controller controller;

    public CalendarPanel(Controller controller) {
        this.controller = controller;

        // Use a small gap (5px) between days so they don't blend together
        setLayout(new GridLayout(1, 7, 5, 5));
        setBackground(new Color(18, 18, 18));

        refreshCalendar();
    }

    /**
     * Call this method whenever a task is added or edited 
     * to update the visual calendar.
     */
    public void refreshCalendar() {
        this.removeAll();
        
        LocalDateTime now = LocalDateTime.now();

        for (int i = 0; i < 7; i++) {
            LocalDateTime day = now.plusDays(i);
            add(createDayPanel(day));
        }

        // Essential for Swing to redraw the new components
        this.revalidate();
        this.repaint();
    }

    private JPanel createDayPanel(LocalDateTime day) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(32, 32, 32));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));

        // Display Date Header (e.g., 5/28)
        JLabel label = new JLabel(day.getMonthValue() + "/" + day.getDayOfMonth());
        label.setForeground(Color.WHITE);
        label.setFont(new Font("SansSerif", Font.BOLD, 14));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(label);
        panel.add(Box.createVerticalStrut(10));

        // Format date strictly as YYYY-MM-DD (e.g., 2026-05-28)
        // This MUST match the LocalDate.parse requirements in WeeklyCalendar
        String date = String.format("%d-%02d-%02d",
                day.getYear(),
                day.getMonthValue(),
                day.getDayOfMonth());

        // Use the shared calendar instance from the controller
        ArrayList<Task> tasks = controller.getCalendar().getTasksForDate(date);

        for (Task t : tasks) {
            JLabel taskLabel = new JLabel("• " + t.getTitle());
            taskLabel.setForeground(Color.LIGHT_GRAY);
            taskLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
            taskLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(taskLabel);
            panel.add(Box.createVerticalStrut(2));
        }

        return panel;
    }
}