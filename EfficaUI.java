import java.awt.*;
import java.awt.event.MouseAdapter;
import java.time.LocalDateTime;
import javax.swing.*;

public class EfficaUI extends JFrame {

    private Controller controller;
    private JPanel taskPanel;

    private SortMode currentSort = SortMode.NONE;

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

        String[] sortOptions = {"None", "Due Date", "Priority", "Estimated Time"};
        JComboBox<String> sortBox = new JComboBox<>(sortOptions);

        addButton.addActionListener(e -> openTaskDialog());

        sortBox.addActionListener(e -> {
            int i = sortBox.getSelectedIndex();
            currentSort = switch (i) {
                case 1 -> SortMode.DUE_DATE;
                case 2 -> SortMode.CUSTOM_PRIORITY;
                case 3 -> SortMode.ESTIMATED_TIME;
                default -> SortMode.NONE;
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

        dialog.add(new JLabel("Title"));
        dialog.add(title);

        dialog.add(new JLabel("Class"));
        dialog.add(course);

        dialog.add(new JLabel("Estimated Minutes"));
        dialog.add(mins);

        dialog.add(new JLabel("Grade"));
        dialog.add(grade);

        dialog.add(new JLabel("Priority"));
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
    private void refreshTasks() {
        taskPanel.removeAll();

        for (Task t : controller.getTasksSortedBy(currentSort)) {
            taskPanel.add(createTaskCard(t));
            taskPanel.add(Box.createVerticalStrut(10));
        }

        taskPanel.revalidate();
        taskPanel.repaint();
    }

    private JPanel createTaskCard(Task t) {
        JPanel card = new RoundedPanel(20);
        card.setLayout(new BorderLayout());
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        card.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        // ===== ACTIVE TASK HIGHLIGHT =====
        if (controller.getActiveTask() == t) {
            card.setBackground(new Color(60, 60, 90));
        } else {
            card.setBackground(CARD);
        }

        JLabel title = new JLabel(t.getTitle());
        title.setForeground(TEXT);
        title.setFont(new Font("SansSerif", Font.BOLD, 16));

        JLabel info = new JLabel(
            t.getClassName() +
            " | " +
            t.getEstimatedMins() +
            " min | Due: " +
            t.getDueDate() + 
            " | Priority: " + t.getPriority()
        );
        info.setForeground(new Color(180, 180, 180));

        card.add(title, BorderLayout.NORTH);
        card.add(info, BorderLayout.SOUTH);

        // ===== DRAG & CLICK HANDLER =====
        MouseAdapter mouseHandler = new MouseAdapter() {
            private Point startPoint;
            private boolean isDragging = false;
            private final int DRAG_THRESHOLD = 15; // Pixels to move before it counts as a drag

            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                startPoint = e.getPoint();
                isDragging = false;
            }

            @Override
            public void mouseDragged(java.awt.event.MouseEvent e) {
                if (startPoint == null) return;

                int dy = e.getY() - startPoint.y;

                // If moved beyond threshold, mark as dragging and change cursor
                if (Math.abs(dy) > DRAG_THRESHOLD) {
                    isDragging = true;
                    card.setCursor(Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR));
                }
            }

            @Override
            public void mouseReleased(java.awt.event.MouseEvent e) {
                card.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                
                if (startPoint == null) return;

                if (isDragging) {
                    int dy = e.getY() - startPoint.y;
                    
                    // DRAG LOGIC: Up decreases priority (higher on list), Down increases
                    if (dy < 0) {
                        t.setPriority(t.getPriority() + 1);
                    } else {
                        t.setPriority(t.getPriority() - 1);
                    }
                    refreshTasks();
                } else {
                    // CLICK LOGIC: Open edit dialog
                    controller.setActiveTask(t);
                    refreshTasks(); // Update highlight
                    openEditDialog(t);
                }

                startPoint = null;
                isDragging = false;
            }
        };

        // REGISTER TO BOTH LISTENER TYPES
        card.addMouseListener(mouseHandler);
        card.addMouseMotionListener(mouseHandler);

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

    private void openEditDialog(Task t) {

        JDialog dialog = new JDialog(this, "Edit Task", true);
        dialog.setSize(350, 300);
        dialog.setLayout(new GridLayout(0, 1));

        JTextField title = new JTextField(t.getTitle());
        JTextField course = new JTextField(t.getClassName());
        JTextField mins = new JTextField(String.valueOf(t.getEstimatedMins()));
        JTextField grade = new JTextField(String.valueOf(t.getGrade()));
        JTextField priority = new JTextField(String.valueOf(t.getPriority()));

        dialog.add(new JLabel("Title"));
        dialog.add(title);

        dialog.add(new JLabel("Class"));
        dialog.add(course);

        dialog.add(new JLabel("Minutes"));
        dialog.add(mins);

        dialog.add(new JLabel("Grade"));
        dialog.add(grade);

        dialog.add(new JLabel("Priority"));
        dialog.add(priority);

        JButton save = new JButton("Save Changes");

        save.addActionListener(e -> {
            t.setTitle(title.getText());
            t.setClassName(course.getText());
            t.setEstimatedMins(Integer.parseInt(mins.getText()));
            t.setGrade(Double.parseDouble(grade.getText()));
            t.setPriority(Integer.parseInt(priority.getText()));

            dialog.dispose();
            refreshTasks();
        });

        dialog.add(save);

        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
}