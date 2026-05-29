import java.awt.*;
import java.time.LocalDateTime;
import javax.swing.*;

public class EfficaUI extends JFrame {

private Controller controller;
private JPanel taskPanel;
private JTextField taskInput;

private final Color BG = new Color(18, 18, 18);
private final Color CARD = new Color(32, 32, 32);
private final Color ACCENT = new Color(120, 140, 255);
private final Color TEXT = new Color(240, 240, 240);

public EfficaUI(Controller controller) {
    this.controller = controller;

    setTitle("Effica");
    setSize(700, 500);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLayout(new BorderLayout());

    getContentPane().setBackground(BG);

    // ===== TOP BAR =====
    JPanel topBar = new JPanel(new BorderLayout());
    topBar.setBackground(BG);
    topBar.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

    JLabel title = new JLabel("Effica");
    title.setForeground(TEXT);
    title.setFont(new Font("SansSerif", Font.BOLD, 26));

    topBar.add(title, BorderLayout.WEST);
    add(topBar, BorderLayout.NORTH);

    // ===== TASK PANEL =====
    taskPanel = new JPanel();
    taskPanel.setLayout(new BoxLayout(taskPanel, BoxLayout.Y_AXIS));
    taskPanel.setBackground(BG);

    JScrollPane scrollPane = new JScrollPane(taskPanel);
    scrollPane.setBorder(null);
    scrollPane.getViewport().setBackground(BG);

    add(scrollPane, BorderLayout.CENTER);

    // ===== BOTTOM INPUT BAR =====
    JPanel inputBar = new JPanel(new BorderLayout());
    inputBar.setBackground(BG);
    inputBar.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    taskInput = new JTextField();
    styleField(taskInput);

    JButton addButton = new JButton("Add Task");
    styleButton(addButton);

    addButton.addActionListener(e -> {
        String titleText = taskInput.getText().trim();
        if (titleText.isEmpty()) return;

        LocalDateTime due = LocalDateTime.now().plusDays(1);

        controller.addTask(
            titleText,
            60,
            "CS",
            95.0,
            due,
            false,
            3
        );

        taskInput.setText("");
        refreshTasks();
    });

    inputBar.add(taskInput, BorderLayout.CENTER);
    inputBar.add(addButton, BorderLayout.EAST);

    add(inputBar, BorderLayout.SOUTH);

    refreshTasks();
    setVisible(true);
}

private void refreshTasks() {
    taskPanel.removeAll();

    for (Task t : controller.getAllTasks()) {
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
    title.setFont(new Font("SansSerif", Font.BOLD, 18));

    JLabel info = new JLabel(
        "Due: " + t.getDueDate() +
        " | " + t.getEstimatedMins() + " min"
    );
    info.setForeground(new Color(180, 180, 180));

    card.add(title, BorderLayout.NORTH);
    card.add(info, BorderLayout.SOUTH);

    return card;
}

private void styleField(JTextField field) {
    field.setBackground(CARD);
    field.setForeground(TEXT);
    field.setCaretColor(TEXT);
    field.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    field.setFont(new Font("SansSerif", Font.PLAIN, 14));
}

private void styleButton(JButton button) {
    button.setBackground(ACCENT);
    button.setForeground(Color.WHITE);
    button.setFocusPainted(false);
    button.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
    button.setFont(new Font("SansSerif", Font.BOLD, 13));
}

// ===== Rounded Panel Class =====
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
