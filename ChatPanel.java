import java.awt.*;
import javax.swing.*;

public class ChatPanel extends JPanel {
    private JTextArea chatArea;
    private JTextField inputField;
    private Controller controller;

    public ChatPanel(Controller controller) {
        this.controller = controller;
        setLayout(new BorderLayout());
        setBackground(new Color(18, 18, 18));

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        chatArea.setBackground(new Color(25, 25, 25));
        chatArea.setForeground(Color.WHITE);
        chatArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
        
        JScrollPane scroll = new JScrollPane(chatArea);
        scroll.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(scroll, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout(10, 0));
        bottomPanel.setBackground(new Color(18, 18, 18));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        inputField = new JTextField();
        inputField.setBackground(new Color(40, 40, 40));
        inputField.setForeground(Color.WHITE);
        inputField.setCaretColor(Color.WHITE);
        
        JButton sendBtn = new JButton("Ask Effica");
        sendBtn.addActionListener(e -> sendMessage());
        inputField.addActionListener(e -> sendMessage());

        bottomPanel.add(inputField, BorderLayout.CENTER);
        bottomPanel.add(sendBtn, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void sendMessage() {
        String msg = inputField.getText().trim();
        if (msg.isEmpty()) return;

        chatArea.append("You: " + msg + "\n");
        inputField.setText("");

        new Thread(() -> {
            String response = controller.getAIAdvice(msg);
            SwingUtilities.invokeLater(() -> {
                chatArea.append("Effica AI: " + response + "\n\n");
            });
        }).start();
    }
}