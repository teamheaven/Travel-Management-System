import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class PackageBrowse extends JFrame {
    String roleOrUsername;
    JTable table;
    DefaultTableModel model;

    public PackageBrowse(String roleOrUsername) {
        this.roleOrUsername = roleOrUsername;

        setTitle("Browse Packages");
        setSize(800, 500);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel header = new JLabel(roleOrUsername.equals("admin") ? "Manage Packages" : "Available Packages", JLabel.CENTER);
        header.setFont(new Font("SansSerif", Font.BOLD, 22));
        header.setOpaque(true);
        header.setBackground(new Color(70, 130, 180));
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(800, 60));
        add(header, BorderLayout.NORTH);

        // Table
        model = new DefaultTableModel();
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        model.addColumn("ID");
        model.addColumn("Name");
        model.addColumn("Location");
        model.addColumn("Type");
        model.addColumn("Budget");
        model.addColumn("Rating");

        loadPackages();

        // For admin, allow add/edit/delete
        if (roleOrUsername.equals("admin")) {
            JPanel bottomPanel = new JPanel();
            JButton addBtn = new JButton("Add Package");
            JButton editBtn = new JButton("Edit Package");
            JButton deleteBtn = new JButton("Delete Package");

            addBtn.setBackground(new Color(34, 139, 34));
            addBtn.setForeground(Color.WHITE);
            editBtn.setBackground(new Color(255, 140, 0));
            editBtn.setForeground(Color.WHITE);
            deleteBtn.setBackground(new Color(178, 34, 34));
            deleteBtn.setForeground(Color.WHITE);

            bottomPanel.add(addBtn);
            bottomPanel.add(editBtn);
            bottomPanel.add(deleteBtn);
            add(bottomPanel, BorderLayout.SOUTH);

            addBtn.addActionListener(e -> new AddPackageForm());
            editBtn.addActionListener(e -> editPackage());
            deleteBtn.addActionListener(e -> deletePackage());
        }

        setVisible(true);
    }

    private void loadPackages() {
        try (Connection conn = DBConnection.getConnection()) {
            model.setRowCount(0); // clear table
            String sql = "SELECT * FROM packages";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("location"),
                        rs.getString("type"),
                        rs.getDouble("budget"),
                        rs.getDouble("rating")
                });
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading packages!");
        }
    }

    private void editPackage() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a package to edit!");
            return;
        }
        int packageId = (int) model.getValueAt(selectedRow, 0);
        new EditPackageForm(packageId);
    }

    private void deletePackage() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a package to delete!");
            return;
        }
        int packageId = (int) model.getValueAt(selectedRow, 0);

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure to delete this package?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM packages WHERE id=?");
            ps.setInt(1, packageId);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Package Deleted!");
            loadPackages();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Cannot delete package (may have bookings or reviews)!");
        }
    }
}