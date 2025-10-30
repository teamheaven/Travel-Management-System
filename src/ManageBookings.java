import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class ManageBookings extends JFrame {
    JTable table;
    DefaultTableModel model;

    public ManageBookings() {
        setTitle("Manage Bookings");
        setSize(850, 400);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Table model
        model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{"ID", "Username", "Package", "Status", "Payment", "Booking Date", "Action"});

        table = new JTable(model) {
            public boolean isCellEditable(int row, int column) {
                return column == 6; // only Action column editable
            }
        };

        // Add ButtonRenderer and ButtonEditor for "Action" column
        table.getColumn("Action").setCellRenderer(new ButtonRenderer());
        table.getColumn("Action").setCellEditor(new ButtonEditor(new JCheckBox()));

        add(new JScrollPane(table), BorderLayout.CENTER);

        loadBookings();

        setVisible(true);
    }

    public void loadBookings() {
        model.setRowCount(0);

        try (Connection conn = DBConnection.getConnection()) {
            String sql = """
                    SELECT b.id, u.username, p.name AS package_name, b.status, 
                           b.payment_status, b.booking_date 
                    FROM bookings b
                    JOIN users u ON b.user_id = u.id
                    JOIN packages p ON b.package_id = p.id
                    """;
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("package_name"),
                        rs.getString("status"),
                        rs.getString("payment_status"),
                        rs.getDate("booking_date"),
                        "Update"
                });
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading bookings!");
        }
    }

    // Update booking status
    public static void updateStatus(int bookingId) {
        String[] options = {"Pending", "Confirmed", "Cancelled"};
        String status = (String) JOptionPane.showInputDialog(
                null,
                "Select new status:",
                "Update Booking Status",
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        if (status != null) {
            try (Connection conn = DBConnection.getConnection()) {
                // Update both status and payment_status logically
                String sql = "UPDATE bookings SET status=?, payment_status=? WHERE id=?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, status);

                if (status.equals("Confirmed")) {
                    ps.setString(2, "Pending"); // user can now pay
                } else if (status.equals("Cancelled")) {
                    ps.setString(2, "Unpaid");
                } else {
                    ps.setString(2, "Unpaid");
                }

                ps.setInt(3, bookingId);
                ps.executeUpdate();

                JOptionPane.showMessageDialog(null, "Booking updated to '" + status + "'");
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error updating booking!");
            }
        }
    }

    // Renderer for Update button
    class ButtonRenderer extends JButton implements javax.swing.table.TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "Update" : value.toString());
            setBackground(new Color(0, 123, 255));
            setForeground(Color.WHITE);
            return this;
        }
    }

    // Editor for Update button
    class ButtonEditor extends DefaultCellEditor {
        protected JButton button;
        private boolean clicked;
        private int bookingId;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(e -> fireEditingStopped());
        }

        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            bookingId = (int) table.getValueAt(row, 0);
            button.setText((value == null) ? "Update" : value.toString());
            clicked = true;
            return button;
        }

        public Object getCellEditorValue() {
            if (clicked) {
                ManageBookings.updateStatus(bookingId);
                loadBookings(); // refresh
            }
            clicked = false;
            return "Update";
        }

        public boolean stopCellEditing() {
            clicked = false;
            return super.stopCellEditing();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ManageBookings::new);
    }
}
