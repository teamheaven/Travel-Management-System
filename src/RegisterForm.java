import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class RegisterForm extends JFrame {
    JTextField usernameField;
    JPasswordField passwordField, confirmPasswordField;
    JButton registerBtn;

    public RegisterForm() {
        setTitle("Travel Recommendation System - Register");
        setSize(500, 350);
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel title = new JLabel("Register");
        title.setBounds(200, 30, 100, 30);
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        add(title);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(100, 80, 100, 25);
        add(userLabel);

        usernameField = new JTextField();
        usernameField.setBounds(200, 80, 180, 25);
        add(usernameField);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(100, 120, 100, 25);
        add(passLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(200, 120, 180, 25);
        add(passwordField);

        JLabel confirmPassLabel = new JLabel("Confirm Password:");
        confirmPassLabel.setBounds(80, 160, 120, 25);
        add(confirmPassLabel);

        confirmPasswordField = new JPasswordField();
        confirmPasswordField.setBounds(200, 160, 180, 25);
        add(confirmPasswordField);

        registerBtn = new JButton("Register");
        registerBtn.setBounds(180, 220, 120, 30);
        registerBtn.setBackground(new Color(70, 130, 180));
        registerBtn.setForeground(Color.WHITE);
        add(registerBtn);

        registerBtn.addActionListener(e -> registerUser());

        setVisible(true);
    }

    private void registerUser() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields!");
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match!");
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            // Check if username already exists
            PreparedStatement checkStmt = conn.prepareStatement("SELECT * FROM users WHERE username=?");
            checkStmt.setString(1, username);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "Username already exists!");
                return;
            }

            // Insert new user
            PreparedStatement insertStmt = conn.prepareStatement(
                    "INSERT INTO users(username, password, role) VALUES (?, ?, 'customer')");
            insertStmt.setString(1, username);
            insertStmt.setString(2, password);
            insertStmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Registration Successful!");
            dispose(); // close form
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database Error!");
        }
    }

    public static void main(String[] args) {
        new RegisterForm();
    }
}
