import java.awt.*;
import java.time.LocalDateTime;
import javax.swing.*;

public class EfficaUI extends JFrame {

    private Controller controller;
    private JPanel taskPanel;

    private SortMode currentSort = SortMode.DUE_DATE;

    private final Color BG = new Color(18, 18, 18);
    private final Color CARD = new Color(32, 32, 32);
    private final Color ACCENT = new Color(120, 140, 255);
    private final Color TEXT = new Color(240, 240, 240);

    public EfficaUI(Controller controller) {
        this.controller = controller;

        setTitle("Effica");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        getContentPane().setBackground(BG);

        // ===== TOP BAR =====
        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topBar.setBackground(BG);
        topBar.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel title = new JLabel("Effica");
        title.setForeground(TEXT);
        title.setFont(new Font("SansSerif", Font.BOLD, 24));

        JButton addButton = new JButton("+ Add Task");
        styleButton(addButton);

        String[] sortOptions = {
            "Due Date",
            "Grade",
            "Estimated Time",
            "Custom Priority"
        };
        
        JComboBox<String> sortBox = new JComboBox<>(sortOptions);

        addButton.addActionListener(e -> openTaskDialog());

        sortBox.addActionListener(e -> {
    int i = sortBox.getSelectedIndex();

    currentSort = switch (i) {
        case 0 -> SortMode.DUE_DATE;
        case 1 -> SortMode.GRADE;
        case 2 -> SortMode.ESTIMATED_TIME;
        case 3 -> SortMode.CUSTOM;
        default -> SortMode.DUE_DATE;
    };

    refreshTasks();
});

        topBar.add(title);
        topBar.add(addButton);
        topBar.add(sortBox);

        add(topBar, BorderLayout.NORTH);

        // ===== TASK PANEL =====
        taskPanel = new JPanel();
        taskPanel.setLayout(new BoxLayout(taskPanel, BoxLayout.Y_AXIS));
        taskPanel.setBackground(BG);

        JScrollPane scrollPane = new JScrollPane(taskPanel);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(BG);

        // === tabs (new)

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Tasks", scrollPane);

        // POMODORO TAB (only shows if class exists)
        tabs.addTab("Pomodoro", new PomodoroPanel(controller));

        // CALENDAR TAB (only shows if class exists)
        tabs.addTab("Calendar", new CalendarPanel(controller));

        // ADD TO FRAME
        add(tabs, BorderLayout.CENTER);

    }


    // ================= TASK CREATION POPUP =================
    private void openTaskDialog() {

        JDialog dialog = new JDialog(this, "Create Task", true);
        dialog.setSize(350, 300);
        dialog.setLayout(new GridLayout(0, 1));

        JTextField dueField = new JTextField();
        dialog.add(new JLabel("Due Date (YYYY-MM-DDTHH:MM)"));
        dialog.add(dueField);
        JTextField title = new JTextField();
        JTextField course = new JTextField();
        JTextField mins = new JTextField();
        JTextField grade = new JTextField();
        JTextField priority = new JTextField();

        dialog.add(new JLabel("Assignment Name"));
        dialog.add(title);

        dialog.add(new JLabel("Estimated Completion Time"));
        dialog.add(mins);

        dialog.add(new JLabel("Class Grade"));
        dialog.add(grade);

        dialog.add(new JLabel("Importance: 1 = low priority; 5 = high priority"));
        dialog.add(priority);

        JButton save = new JButton("Save Task");

        save.addActionListener(e -> {
        try {
            LocalDateTime due = LocalDateTime.parse(dueField.getText().trim());

            controller.addTask(
                title.getText(),
                Integer.parseInt(mins.getText()),
                course.getText(),
                Double.parseDouble(grade.getText()),
                due,
                false,
                Integer.parseInt(priority.getText())
            );

            dialog.dispose();
            refreshTasks();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(dialog,
                "Invalid input.\nUse format: 2026-06-01T23:59");
        }
    });

        dialog.add(save);

        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    // ================= TASK RENDER =================
    private String getTimeString() {
        return LocalDateTime.now().toString().substring(0, 16);
    }

    private void refreshTasks() {
        taskPanel.removeAll();

        for (Task t : controller.getTasksSortedBy(currentSort, getTimeString())) {
            taskPanel.add(createTaskCard(t));
            taskPanel.add(Box.createVerticalStrut(10));
        }

        taskPanel.revalidate();
        taskPanel.repaint();
    }

    private JPanel createTaskCard(Task t) {

        JPanel card = new RoundedPanel(20);
        card.setLayout(new BorderLayout());
        card.setBackground(CARD);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        card.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JLabel title = new JLabel(t.getTitle());
        title.setForeground(TEXT);
        title.setFont(new Font("SansSerif", Font.BOLD, 16));

        JLabel info = new JLabel(
            t.getClassName() +
            " | " +
            t.getEstimatedMins() +
            " min | Grade: " +
            t.getGrade() +
            " | Priority: " +
            t.getPriority() +
            " | Due: " +
            t.getDueDate()
        );

        info.setForeground(new Color(180, 180, 180));

        card.add(title, BorderLayout.NORTH);
        card.add(info, BorderLayout.SOUTH);

        return card;
    }

    // ================= STYLING =================
    private void styleButton(JButton button) {
        button.setBackground(ACCENT);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
    }

    // ================= ROUNDED PANEL =================
    class RoundedPanel extends JPanel {
        private int radius;

        public RoundedPanel(int radius) {
            this.radius = radius;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);

            super.paintComponent(g);
        }
    }
}