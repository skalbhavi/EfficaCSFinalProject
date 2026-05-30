import java.awt.*;
import javax.swing.*;

public class LoginUI extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private NetworkClient client;

    public LoginUI() {

        client = new NetworkClient("");

        setTitle("Login");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(3, 1));

        usernameField = new JTextField();
        passwordField = new JPasswordField();

        JButton loginBtn = new JButton("Login");
        JButton registerBtn = new JButton("Register");

        loginBtn.addActionListener(e -> login());
        registerBtn.addActionListener(e -> register());

        add(usernameField);
        add(passwordField);
        add(loginBtn);
        add(registerBtn);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void login() {
        try {
            String user = usernameField.getText().trim();
            String pass = new String(passwordField.getPassword()).trim();

            client = new NetworkClient(user);

            if (client.login(user, pass)) {
                openMain(user);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Login failed");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void register() {
        try {
            String user = usernameField.getText().trim();
            String pass = new String(passwordField.getPassword()).trim();

            client.register(user, pass);

            JOptionPane.showMessageDialog(this, "Account created");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openMain(String username) {
        Controller controller = new Controller(username);
        new EfficaUI(controller);
    }
}