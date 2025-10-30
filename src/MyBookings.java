import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class MyBookings extends JFrame {
    String username;
    JTable table;
    DefaultTableModel model;

    public MyBookings(String username) {
        this.username = username;

        setTitle("My Bookings");
        setSize(800, 420);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{"Booking ID", "Package", "Status", "Payment", "Booking Date", "Action"});

        table = new JTable(model) {
            public boolean isCellEditable(int row, int column) {
                return column == 5; // only Action column editable
            }
        };

        table.getColumn("Action").setCellRenderer(new ButtonRenderer());
        table.getColumn("Action").setCellEditor(new ButtonEditor(new JCheckBox()));

        add(new JScrollPane(table), BorderLayout.CENTER);
        loadBookings();

        setVisible(true);
    }

    private void loadBookings() {
        model.setRowCount(0);
    
        try (Connection conn = DBConnection.getConnection()) {
            // Get user_id
            PreparedStatement ps1 = conn.prepareStatement("SELECT id FROM users WHERE username=?");
            ps1.setString(1, username);
            ResultSet rs1 = ps1.executeQuery();
            if (!rs1.next()) return;
            int userId = rs1.getInt("id");
    
            // Fetch bookings with payment status too
            String sql = "SELECT b.id, p.name AS package_name, b.status, b.payment_status, b.booking_date " +
                         "FROM bookings b " +
                         "JOIN packages p ON b.package_id = p.id " +
                         "WHERE b.user_id=?";
            PreparedStatement ps2 = conn.prepareStatement(sql);
            ps2.setInt(1, userId);
            ResultSet rs2 = ps2.executeQuery();
    
            while (rs2.next()) {
                String status = rs2.getString("status");
                String paymentStatus = rs2.getString("payment_status");
                String actionText;
    
                // Logic for which button to show
                if (status.equalsIgnoreCase("Confirmed") && paymentStatus.equalsIgnoreCase("Unpaid")) {
                    actionText = "Pay Now";
                } else if (status.equalsIgnoreCase("Pending")) {
                    actionText = "Waiting";
                } else if (status.equalsIgnoreCase("Cancelled")) {
                    actionText = "-";
                } else {
                    actionText = "Cancel";
                }
    
                model.addRow(new Object[]{
                        rs2.getInt("id"),
                        rs2.getString("package_name"),
                        status,
                        paymentStatus,
                        rs2.getDate("booking_date"),
                        actionText
                });
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading bookings!");
        }
    }
    

    private void cancelBooking(int bookingId) {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to cancel this booking?",
                "Confirm Cancel", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conn = DBConnection.getConnection()) {
                PreparedStatement ps = conn.prepareStatement(
                        "UPDATE bookings SET status='Cancelled', payment_status='Unpaid' WHERE id=?");
                ps.setInt(1, bookingId);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Booking cancelled!");
                loadBookings();
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error cancelling booking!");
            }
        }
    }

    private void makePayment(int bookingId) {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Proceed to payment?",
                "Payment Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conn = DBConnection.getConnection()) {
                PreparedStatement ps = conn.prepareStatement(
                        "UPDATE bookings SET payment_status='Paid' WHERE id=?");
                ps.setInt(1, bookingId);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Payment successful!");
                loadBookings();
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error in payment!");
            }
        }
    }

    class ButtonRenderer extends JButton implements javax.swing.table.TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            String text = (value == null) ? "" : value.toString();
            setText(text);
            setForeground(Color.WHITE);

            switch (text) {
                case "Cancel" -> setBackground(new Color(220, 53, 69));
                case "Pay Now" -> setBackground(new Color(40, 167, 69));
                case "Paid" -> setBackground(new Color(100, 100, 100));
                default -> setBackground(Color.GRAY);
            }

            return this;
        }
    }

    class ButtonEditor extends DefaultCellEditor {
        protected JButton button;
        private boolean clicked;
        private int bookingId;
        private String action;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(e -> fireEditingStopped());
        }

        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            bookingId = (int) table.getValueAt(row, 0);
            action = (String) value;
            button.setText(action);
            clicked = true;
            return button;
        }

        public Object getCellEditorValue() {
            if (clicked) {
                switch (action) {
                    case "Cancel" -> cancelBooking(bookingId);
                    case "Pay Now" -> makePayment(bookingId);
                }
            }
            clicked = false;
            return action;
        }

        public boolean stopCellEditing() {
            clicked = false;
            return super.stopCellEditing();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MyBookings("customer1"));
    }
}
