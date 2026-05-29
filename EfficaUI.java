import java.awt.*;
import java.time.LocalDateTime;
import javax.swing.*;

public class EfficaUI extends JFrame {

    private Controller controller;
    private JPanel taskPanel;
    private JTabbedPane tabs;            
    private CalendarPanel calendarView; 

    private SortMode currentSort = SortMode.DUE_DATE;

    private final Color BG = new Color(248, 245, 252);
    private final Color CARD = new Color(58, 48, 74);
    private final Color ACCENT = new Color(200, 190, 220);
    private final Color TEXT = new Color(58, 48, 74);

    public EfficaUI(Controller controller) {
        this.controller = controller;

        setTitle("Effica");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(BG);

        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topBar.setBackground(BG);
        topBar.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel title = new JLabel("Effica");
        title.setForeground(TEXT);
        title.setFont(new Font("SansSerif", Font.BOLD, 24));

        JButton addButton = new JButton("+ Add Task");
        styleButton(addButton);
        addButton.addActionListener(e -> openTaskDialog());

        String[] sortOptions = {"Due Date", "Grade", "Estimated Time", "Custom Priority"};
        JComboBox<String> sortBox = new JComboBox<>(sortOptions);
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

        tabs = new JTabbedPane();

        taskPanel = new JPanel();
        taskPanel.setLayout(new BoxLayout(taskPanel, BoxLayout.Y_AXIS));
        taskPanel.setBackground(BG);
        JScrollPane scrollPane = new JScrollPane(taskPanel);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(BG);
        tabs.addTab("Tasks", scrollPane);

        tabs.addTab("Pomodoro", new PomodoroPanel(controller));

        tabs.addTab("AI Assistant", new ChatPanel(controller));
        this.calendarView = new CalendarPanel(controller);
        tabs.addTab("Calendar", calendarView);

        add(tabs, BorderLayout.CENTER);

        refreshTasks();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private String getTimeString() {
        return LocalDateTime.now().toString().substring(0, 16);
    }

    private void refreshTasks() {
        taskPanel.removeAll();

        for (Task t : controller.getTasksSortedBy(currentSort, getTimeString())) {
            taskPanel.add(createTaskCard(t));
            taskPanel.add(Box.createVerticalStrut(10));
        }

        if (calendarView != null) {
            calendarView.refreshCalendar();
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

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);

        JLabel title = new JLabel(t.getAssignmentName());
        title.setForeground(TEXT);
        title.setFont(new Font("SansSerif", Font.BOLD, 16));

        JLabel info = new JLabel(
            t.getClassGrade() + " | " + t.getEstimatedTime() + " min | Due: " + t.getDueDate()
        );
        info.setForeground(new Color(180, 180, 180));

        infoPanel.add(title);
        infoPanel.add(info);
        card.add(infoPanel, BorderLayout.CENTER);

        JPopupMenu menu = new JPopupMenu();
        JMenuItem editItem = new JMenuItem("Edit Task");
        JMenuItem deleteItem = new JMenuItem("Delete Task");

        editItem.addActionListener(e -> openEditDialog(t));
        deleteItem.addActionListener(e -> {
            controller.removeTask(t.getID());
            refreshTasks();
        });

        menu.add(editItem);
        menu.add(deleteItem);

        card.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent e) {
                if (e.isPopupTrigger()) menu.show(e.getComponent(), e.getX(), e.getY());
            }
            public void mouseReleased(java.awt.event.MouseEvent e) {
                if (e.isPopupTrigger()) menu.show(e.getComponent(), e.getX(), e.getY());
            }
        });

        return card;
    }

    private void openEditDialog(Task t) {

        JDialog dialog = new JDialog(this, "Edit Task", true);
        dialog.setSize(350, 450);
        dialog.setLayout(new GridLayout(0, 1));

        JTextField title = new JTextField(t.getAssignmentName());
        JTextField mins = new JTextField("" + t.getEstimatedTime());
        JTextField grade = new JTextField("" + t.getClassGrade());
        JTextField priority = new JTextField("" + t.getPriority());
        JTextField dueField = new JTextField(t.getDueDate().toString());

        dialog.add(new JLabel("Assignment Name"));
        dialog.add(title);

        dialog.add(new JLabel("Estimated Minutes"));
        dialog.add(mins);

        dialog.add(new JLabel("Current Grade (%)"));
        dialog.add(grade);

        dialog.add(new JLabel("Priority (1-5)"));
        dialog.add(priority);

        dialog.add(new JLabel("Due Date (YYYY-MM-DDTHH:MM)"));
        dialog.add(dueField);

        JButton save = new JButton("Save Changes");

        save.addActionListener(e -> {

            try {

                controller.editTask(
                    t.getID(),
                    title.getText(),
                    Integer.parseInt(mins.getText()),
                    LocalDateTime.parse(dueField.getText().trim()),
                    Double.parseDouble(grade.getText()),
                    Integer.parseInt(priority.getText())
                );

                dialog.dispose();
                refreshTasks();

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(
                    dialog,
                    "Error: Check your formats!"
                );

            }

        });

        dialog.add(save);

        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void openTaskDialog() {
        JDialog dialog = new JDialog(this, "Create Task", true);
        dialog.setSize(350, 400);
        dialog.setLayout(new GridLayout(0, 1));

        JTextField dueField = new JTextField(LocalDateTime.now().toString().substring(0, 16));
        JTextField title = new JTextField();
        JTextField course = new JTextField();
        JTextField mins = new JTextField();
        JTextField grade = new JTextField();
        JTextField priority = new JTextField();

        dialog.add(new JLabel(" Due Date (YYYY-MM-DDTHH:MM)"));
        dialog.add(dueField);
        dialog.add(new JLabel(" Assignment Name"));
        dialog.add(title);
        dialog.add(new JLabel(" Class Name"));
        dialog.add(course);
        dialog.add(new JLabel(" Estimated Minutes"));
        dialog.add(mins);
        dialog.add(new JLabel(" Current Grade"));
        dialog.add(grade);
        dialog.add(new JLabel(" Priority (1-5)"));
        dialog.add(priority);

        JButton save = new JButton("Save Task");
        save.addActionListener(e -> {
            try {
                controller.addTask(
                    title.getText(),
                    Integer.parseInt(mins.getText()),
                    Double.parseDouble(grade.getText()),
                    LocalDateTime.parse(dueField.getText().trim()),
                    false,
                    Integer.parseInt(priority.getText())
                );
                dialog.dispose();
                refreshTasks();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Invalid input. Check your formats!");
            }
        });

        dialog.add(save);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void styleButton(JButton button) {
        button.setBackground(ACCENT);
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
    }

    class RoundedPanel extends JPanel {
        private int radius;
        public RoundedPanel(int radius) {
            this.radius = radius;
            setOpaque(false);
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            super.paintComponent(g);
        }
    }
}