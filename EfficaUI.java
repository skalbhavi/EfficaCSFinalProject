import java.awt.*;
import java.time.LocalDateTime;
import javax.swing.*;

public class EfficaUI extends JFrame {

    private Controller controller;
    private DefaultListModel<String> listModel;

    public EfficaUI(Controller controller) {
        this.controller = controller;
        setTitle("Effica");
        setSize(500,400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        listModel = new DefaultListModel<>();
        JList<String> taskList = new JList<>(listModel);

        JTextField taskInput = new JTextField();
        JButton addButton = new JButton("Add Task");
        addButton.addActionListener(e -> {
            String title = taskInput.getText();
            LocalDateTime taskTime = LocalDateTime.parse("2026-06-01T23:59:59");
            controller.addTask(title, 60, "CS", 97.35, taskTime, false, 3);
            refreshTasks();
            taskInput.setText("");
        });

        add(taskInput, BorderLayout.NORTH);
        add(new JScrollPane(taskList), BorderLayout.CENTER);
        add(addButton, BorderLayout.SOUTH);

        setVisible(true);
        refreshTasks();
    }

    private void refreshTasks() {
        listModel.clear();
        for (Task t : controller.getAllTasks()) {
            listModel.addElement(t.toString());
        }
    }


}
