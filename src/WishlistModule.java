import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class WishlistModule extends JFrame {
    String username;
    JTable wishlistTable;
    DefaultTableModel model;
    JButton removeBtn, addBtn;

    public WishlistModule(String username) {
        this.username = username;
        setTitle("My Wishlist");
        setSize(600, 400);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        model = new DefaultTableModel(new String[]{"ID", "Package Name", "Location", "Type", "Budget"}, 0);
        wishlistTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(wishlistTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        addBtn = new JButton("Add Package");
        removeBtn = new JButton("Remove Selected");

        addBtn.setBackground(new Color(34, 139, 34));
        addBtn.setForeground(Color.WHITE);
        removeBtn.setBackground(new Color(178, 34, 34));
        removeBtn.setForeground(Color.WHITE);

        buttonPanel.add(addBtn);
        buttonPanel.add(removeBtn);

        add(buttonPanel, BorderLayout.SOUTH);

        loadWishlist();

        addBtn.addActionListener(e -> addPackageToWishlist());
        removeBtn.addActionListener(e -> removeFromWishlist());

        setVisible(true);
    }

    private void loadWishlist() {
        model.setRowCount(0);
        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement ps1 = conn.prepareStatement("SELECT id FROM users WHERE username=?");
            ps1.setString(1, username);
            ResultSet rs1 = ps1.executeQuery();
            if (!rs1.next()) return;
            int userId = rs1.getInt("id");

            String sql = "SELECT w.id as wishlist_id, p.name, p.location, p.type, p.budget " +
                    "FROM wishlist w JOIN packages p ON w.package_id = p.id WHERE w.user_id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("wishlist_id"),
                        rs.getString("name"),
                        rs.getString("location"),
                        rs.getString("type"),
                        rs.getDouble("budget")
                });
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading wishlist!");
        }
    }

    private void addPackageToWishlist() {
        String packageName = JOptionPane.showInputDialog(this, "Enter Package Name to Add:");
        if (packageName == null || packageName.trim().isEmpty()) return;

        try (Connection conn = DBConnection.getConnection()) {
            // Get user_id
            PreparedStatement ps1 = conn.prepareStatement("SELECT id FROM users WHERE username=?");
            ps1.setString(1, username);
            ResultSet rs1 = ps1.executeQuery();
            if (!rs1.next()) return;
            int userId = rs1.getInt("id");

            // Get package_id
            PreparedStatement ps2 = conn.prepareStatement("SELECT id FROM packages WHERE name=?");
            ps2.setString(1, packageName.trim());
            ResultSet rs2 = ps2.executeQuery();
            if (!rs2.next()) {
                JOptionPane.showMessageDialog(this, "Package not found!");
                return;
            }
            int packageId = rs2.getInt("id");

            // Insert into wishlist
            PreparedStatement ps3 = conn.prepareStatement("INSERT INTO wishlist(user_id, package_id) VALUES(?, ?)");
            ps3.setInt(1, userId);
            ps3.setInt(2, packageId);
            ps3.executeUpdate();
            JOptionPane.showMessageDialog(this, "Package added to wishlist!");
            loadWishlist();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding package. Maybe it's already in your wishlist.");
        }
    }

    private void removeFromWishlist() {
        int selectedRow = wishlistTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a package to remove!");
            return;
        }

        int wishlistId = (int) model.getValueAt(selectedRow, 0);
        int option = JOptionPane.showConfirmDialog(this, "Are you sure you want to remove this package?", "Remove Package", JOptionPane.YES_NO_OPTION);

        if (option == JOptionPane.YES_OPTION) {
            try (Connection conn = DBConnection.getConnection()) {
                PreparedStatement ps = conn.prepareStatement("DELETE FROM wishlist WHERE id=?");
                ps.setInt(1, wishlistId);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Package removed from wishlist!");
                loadWishlist();
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error removing package!");
            }
        }
    }

    public static void main(String[] args) {
        new WishlistModule("customer1"); // test
    }
}
