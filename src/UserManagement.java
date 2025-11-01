import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class UserManagement extends JFrame {
    JTable userTable;
    DefaultTableModel model;
    JButton addBtn, editBtn, deleteBtn;

    public UserManagement() {
        setTitle("User Management - Admin");
        setSize(700, 400);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Table setup
        model = new DefaultTableModel(new String[]{"ID", "Username", "Role"}, 0);
        userTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(userTable);
        add(scrollPane, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel();
        addBtn = new JButton("Add User");
        editBtn = new JButton("Edit User");
        deleteBtn = new JButton("Delete User");

        addBtn.setBackground(new Color(34, 139, 34));
        addBtn.setForeground(Color.WHITE);
        editBtn.setBackground(new Color(255, 140, 0));
        editBtn.setForeground(Color.WHITE);
        deleteBtn.setBackground(new Color(178, 34, 34));
        deleteBtn.setForeground(Color.WHITE);

        buttonPanel.add(addBtn);
        buttonPanel.add(editBtn);
        buttonPanel.add(deleteBtn);

        add(buttonPanel, BorderLayout.SOUTH);

        // Load users
        loadUsers();

        // Button actions
        addBtn.addActionListener(e -> addUser());
        editBtn.addActionListener(e -> editUser());
        deleteBtn.addActionListener(e -> deleteUser());

        setVisible(true);
    }

    private void loadUsers() {
        model.setRowCount(0); // clear table
        try (Connection conn = DBConnection.getConnection()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM users");
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("role")
                });
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading users!");
        }
    }

    private void addUser() {
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        String[] roles = {"admin", "customer"};
        JComboBox<String> roleBox = new JComboBox<>(roles);

        Object[] message = {
                "Username:", usernameField,
                "Password:", passwordField,
                "Role:", roleBox
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Add User", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());
            String role = (String) roleBox.getSelectedItem();

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Username and Password cannot be empty!");
                return;
            }

            try (Connection conn = DBConnection.getConnection()) {
                PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO users(username, password, role) VALUES(?, ?, ?)");
                ps.setString(1, username);
                ps.setString(2, password);
                ps.setString(3, role);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "User added successfully!");
                loadUsers();
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error adding user! Username might already exist.");
            }
        }
    }

    private void editUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a user to edit!");
            return;
        }

        int userId = (int) model.getValueAt(selectedRow, 0);
        String currentUsername = (String) model.getValueAt(selectedRow, 1);
        String currentRole = (String) model.getValueAt(selectedRow, 2);

        JTextField usernameField = new JTextField(currentUsername);
        String[] roles = {"admin", "customer"};
        JComboBox<String> roleBox = new JComboBox<>(roles);
        roleBox.setSelectedItem(currentRole);

        Object[] message = {
                "Username:", usernameField,
                "Role:", roleBox
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Edit User", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String username = usernameField.getText().trim();
            String role = (String) roleBox.getSelectedItem();

            if (username.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Username cannot be empty!");
                return;
            }

            try (Connection conn = DBConnection.getConnection()) {
                PreparedStatement ps = conn.prepareStatement(
                        "UPDATE users SET username=?, role=? WHERE id=?");
                ps.setString(1, username);
                ps.setString(2, role);
                ps.setInt(3, userId);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "User updated successfully!");
                loadUsers();
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error updating user!");
            }
        }
    }

    private void deleteUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a user to delete!");
            return;
        }

        int userId = (int) model.getValueAt(selectedRow, 0);

        int option = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this user?", "Delete User", JOptionPane.YES_NO_OPTION);

        if (option == JOptionPane.YES_OPTION) {
            try (Connection conn = DBConnection.getConnection()) {
                PreparedStatement ps = conn.prepareStatement("DELETE FROM users WHERE id=?");
                ps.setInt(1, userId);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "User deleted successfully!");
                loadUsers();
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error deleting user! There may be linked bookings or payments.");
            }
        }
    }

    public static void main(String[] args) {
        new UserManagement();
    }
}