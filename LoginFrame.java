import java.awt.*;
import javax.swing.*;

public class LoginFrame extends JFrame {

    public LoginFrame() {

        setTitle("Login - Effica");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(3, 1));

        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();

        JButton loginButton = new JButton("Login");

        add(new JLabel("Username"));
        add(usernameField);

        add(new JLabel("Password"));
        add(passwordField);

        add(loginButton);

        loginButton.addActionListener(e -> {

            String user = usernameField.getText();
            String pass = new String(passwordField.getPassword());

            Controller controller = new Controller(user);

            try {
                boolean ok = controller.getNetwork().login(user, pass);

                if (!ok) {
                    JOptionPane.showMessageDialog(this, "Login failed");
                    return;
                }

                new EfficaUI(controller);
                dispose();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Server error");
            }
        });

        setLocationRelativeTo(null);
        setVisible(true);
    }
}