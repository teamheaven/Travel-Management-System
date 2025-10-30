import javax.swing.*;
import java.sql.*;

public class LoginForm extends JFrame {
    JTextField usernameField;
    JPasswordField passwordField;
    JButton loginBtn, registerBtn;

    public LoginForm() {
        setTitle("Travel Management System - Login");
        setSize(500, 350);
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel title = new JLabel("Login");
        title.setBounds(200, 30, 100, 30);
        title.setFont(new java.awt.Font("SansSerif", java.awt.Font.BOLD, 20));
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

        loginBtn = new JButton("Login");
        loginBtn.setBounds(120, 180, 100, 30);
        loginBtn.setBackground(new java.awt.Color(34, 139, 34));
        loginBtn.setForeground(java.awt.Color.WHITE);
        add(loginBtn);

        registerBtn = new JButton("Register");
        registerBtn.setBounds(250, 180, 100, 30);
        registerBtn.setBackground(new java.awt.Color(70, 130, 180));
        registerBtn.setForeground(java.awt.Color.WHITE);
        add(registerBtn);

        loginBtn.addActionListener(e -> loginUser());
        registerBtn.addActionListener(e -> new RegisterForm());

        setVisible(true);
    }

    private void loginUser() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if(username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both username and password!");
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM users WHERE username=? AND password=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if(rs.next()) {
                String role = rs.getString("role");
                JOptionPane.showMessageDialog(this, "Login Successful!");
                dispose();
                if(role.equalsIgnoreCase("admin")) {
                    new AdminPanel(username);
                } else {
                    new CustomerPanel(username);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Credentials!");
            }
        } catch(SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database Error!");
        }
    }

    public static void main(String[] args) {
        new LoginForm();
    }
}
