package screens;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import org.mindrot.jbcrypt.BCrypt;  // Import BCrypt for password checking

public class LoginScreenGui extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton, registerButton;
    private JLabel statusLabel;

    public LoginScreenGui() {
        setTitle("Login");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // UI Components
        usernameField = new JTextField(15);
        passwordField = new JPasswordField(15);
        loginButton = new JButton("Login");
        registerButton = new JButton("Register");  // Add a Register button
        statusLabel = new JLabel();

        // Layout
        JPanel panel = new JPanel(new GridLayout(4, 2));
        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(loginButton);
        panel.add(registerButton);  // Add register button to the panel
        panel.add(statusLabel);

        add(panel);

        // Action Listener for Login Button
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                authenticateUser();
            }
        });

        // Action Listener for Register Button
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open the registration window
                new RegisterScreenGui().setVisible(true);
            }
        });
    }

    private void authenticateUser() {
        String username = usernameField.getText().trim(); // Trim to remove leading/trailing spaces
        String password = new String(passwordField.getPassword()).trim(); // Trim to remove leading/trailing spaces

        // Check if username or password is empty
        if (username.isEmpty() || password.isEmpty()) {
            statusLabel.setText("Please enter both username and password");
            return; // Exit the method early
        }

        if (validateCredentials(username, password)) {
            statusLabel.setText("Login successful");
            // Transition to the main screen (TitleScreenGui)
            new TitleScreenGui().setVisible(true);
            this.dispose();  // Close the login screen
        } else {
            statusLabel.setText("Invalid username or password");
        }
    }

    private boolean validateCredentials(String username, String password) {
        boolean isAuthenticated = false;

        // Database connection details (use your own configuration)
        String dbUrl = "jdbc:mysql://127.0.0.1:3306/quiz-gui-db";
        String dbUser = "root";
        String dbPassword = "munaf";

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword)) {
            String query = "SELECT password_hash FROM users WHERE username = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String storedHash = rs.getString("password_hash");
                // Use BCrypt to check if the entered password matches the stored hash
                isAuthenticated = BCrypt.checkpw(password, storedHash);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return isAuthenticated;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginScreenGui().setVisible(true));
    }
}
