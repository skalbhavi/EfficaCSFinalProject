import java.awt.*;
import java.time.LocalDateTime;
import javax.swing.*;


/**
 * Provides a dialog window for creating & editing tasks.
 * Users can enter task information and use them within task manager.
 * 
 */
public class TaskEditorDialog extends JDialog {

    private Task result;
    
    /**
     * Creates a modal dialog for entering task information
     * @param parent
     */
    public TaskEditorDialog(JFrame parent) {
        super(parent, "Task Editor", true);

        setSize(350, 300);
        setLayout(new GridLayout(0, 1));

        JTextField taskName = new JTextField();
        JTextField estimatedTime = new JTextField();
        JTextField classGrade = new JTextField();
        JTextField priority = new JTextField();

        add(new JLabel("Task Name"));
        add(taskName);

        add(new JLabel("Estimated Completion Time (minutes)"));
        add(estimatedTime);

        add(new JLabel("Current Class Grade"));
        add(classGrade);

        add(new JLabel("Priority"));
        add(priority);

        JButton save = new JButton("Save");

        save.addActionListener(e -> {
            result = new Task(
                taskName.getText(),
                LocalDateTime.now().plusDays(1),
                Integer.parseInt(estimatedTime.getText()),
                Integer.parseInt(classGrade.getText()),
                false,
                Integer.parseInt(priority.getText())
            );
            dispose();
        });

        add(save);
    }

    /**
     * Displays the dialog & returns the task entered by the user. 
     * @return
     */
    public Task showDialog() {
        setVisible(true);
        return result;
    }
}