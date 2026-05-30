import java.awt.*;
import javax.swing.*;

/**
 * Login window for the Effica application
 * Frame allows users to enter username & password
 * If sucessful, main Effica UI is launched, 
 * otherwise error msg displayed
 */
public class LoginFrame extends JFrame {

    /**
     * Constructs the Login window & initializes all UI components
     */
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