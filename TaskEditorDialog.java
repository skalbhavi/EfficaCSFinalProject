import java.awt.*;
import java.time.LocalDateTime;
import javax.swing.*;

public class TaskEditorDialog extends JDialog {

    private Task result;

    public TaskEditorDialog(JFrame parent) {
        super(parent, "Task Editor", true);

        setSize(350, 300);
        setLayout(new GridLayout(0, 1));

        JTextField title = new JTextField();
        JTextField course = new JTextField();
        JTextField mins = new JTextField();
        JTextField grade = new JTextField();
        JTextField priority = new JTextField();

        add(new JLabel("Title"));
        add(title);

        add(new JLabel("Class"));
        add(course);

        add(new JLabel("Minutes"));
        add(mins);

        add(new JLabel("Grade"));
        add(grade);

        add(new JLabel("Priority"));
        add(priority);

        JButton save = new JButton("Save");

        save.addActionListener(e -> {
            result = new Task(
                title.getText(),
                Integer.parseInt(mins.getText()),
                course.getText(),
                Double.parseDouble(grade.getText()),
                LocalDateTime.now().plusDays(1),
                false,
                Integer.parseInt(priority.getText())
            );
            dispose();
        });

        add(save);
    }

    public Task showDialog() {
        setVisible(true);
        return result;
    }
}