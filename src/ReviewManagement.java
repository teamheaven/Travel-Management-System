import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class ReviewManagement extends JFrame {
    JTable reviewTable;
    DefaultTableModel model;

    public ReviewManagement() {
        setTitle("Manage Reviews");
        setSize(800, 400);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Header
        JLabel header = new JLabel("Manage Reviews", JLabel.CENTER);
        header.setFont(new Font("SansSerif", Font.BOLD, 22));
        header.setOpaque(true);
        header.setBackground(new Color(70, 130, 180));
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(800, 50));
        add(header, BorderLayout.NORTH);

        // Table
        model = new DefaultTableModel();
        model.setColumnIdentifiers(new Object[]{"ID", "User", "Package", "Rating", "Review", "Date", "Action"});
        reviewTable = new JTable(model);
        reviewTable.getColumn("Action").setCellRenderer(new ButtonRenderer());
        reviewTable.getColumn("Action").setCellEditor(new ButtonEditor(new JCheckBox()));

        JScrollPane scroll = new JScrollPane(reviewTable);
        add(scroll, BorderLayout.CENTER);

        loadReviews();

        setVisible(true);
    }

    private void loadReviews() {
        model.setRowCount(0);
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT r.id, u.username, p.name AS package_name, r.rating, r.review, r.review_date " +
                         "FROM reviews r " +
                         "JOIN users u ON r.user_id = u.id " +
                         "JOIN packages p ON r.package_id = p.id";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("package_name"),
                        rs.getInt("rating"),
                        rs.getString("review"),
                        rs.getDate("review_date"),
                        "Delete"
                });
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading reviews!");
        }
    }

    // Inner class for Delete button
    class ButtonRenderer extends JButton implements javax.swing.table.TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "" : value.toString());
            setBackground(Color.RED);
            setForeground(Color.WHITE);
            return this;
        }
    }

    class ButtonEditor extends DefaultCellEditor {
        protected JButton button;
        private int reviewId;
        private boolean clicked;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(e -> fireEditingStopped());
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            reviewId = (int) table.getValueAt(row, 0); // review ID
            button.setText((value == null) ? "" : value.toString());
            clicked = true;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            if(clicked) {
                int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this review?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
                if(confirm == JOptionPane.YES_OPTION) {
                    try(Connection conn = DBConnection.getConnection()) {
                        PreparedStatement ps = conn.prepareStatement("DELETE FROM reviews WHERE id=?");
                        ps.setInt(1, reviewId);
                        ps.executeUpdate();
                        JOptionPane.showMessageDialog(null, "Review deleted!");
                        loadReviews(); // refresh table
                    } catch(SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Error deleting review!");
                    }
                }
            }
            clicked = false;
            return "Delete";
        }

        @Override
        public boolean stopCellEditing() {
            clicked = false;
            return super.stopCellEditing();
        }

        @Override
        protected void fireEditingStopped() {
            super.fireEditingStopped();
        }
    }
}