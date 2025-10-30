import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class ProfileManagement extends JFrame {
    String username;
    JTextField usernameField;
    JPasswordField passwordField;
    JButton updateBtn;

    public ProfileManagement(String username) {
        this.username = username;

        setTitle("Profile Management");
        setSize(400, 300);
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel title = new JLabel("Profile Management", JLabel.CENTER);
        title.setBounds(50, 20, 300, 30);
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        add(title);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(50, 80, 100, 25);
        add(userLabel);

        usernameField = new JTextField();
        usernameField.setBounds(150, 80, 180, 25);
        add(usernameField);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(50, 120, 100, 25);
        add(passLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(150, 120, 180, 25);
        add(passwordField);

        updateBtn = new JButton("Update Profile");
        updateBtn.setBounds(130, 180, 150, 30);
        updateBtn.setBackground(new Color(34, 139, 34));
        updateBtn.setForeground(Color.WHITE);
        add(updateBtn);

        loadProfile();

        updateBtn.addActionListener(e -> updateProfile());

        setVisible(true);
    }

    private void loadProfile() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT username, password FROM users WHERE username=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                usernameField.setText(rs.getString("username"));
                passwordField.setText(rs.getString("password"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading profile!");
        }
    }

    private void updateProfile() {
        String newUsername = usernameField.getText().trim();
        String newPassword = new String(passwordField.getPassword());

        if (newUsername.isEmpty() || newPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username and Password cannot be empty!");
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "UPDATE users SET username=?, password=? WHERE username=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, newUsername);
            ps.setString(2, newPassword);
            ps.setString(3, username);

            int updated = ps.executeUpdate();
            if (updated > 0) {
                JOptionPane.showMessageDialog(this, "Profile updated successfully!");
                username = newUsername; // update current session username
            } else {
                JOptionPane.showMessageDialog(this, "Profile update failed!");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database Error!");
        }
    }
}
