import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class PaymentModule extends JFrame {
    String username;
    JComboBox<String> bookingBox;
    JTextField amountField;
    JButton payBtn;

    public PaymentModule(String username) {
        this.username = username;
        setTitle("Make Payment");
        setSize(500, 350);
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel title = new JLabel("Payment Module");
        title.setBounds(150, 20, 200, 30);
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        add(title);

        JLabel bookingLabel = new JLabel("Select Booking:");
        bookingLabel.setBounds(50, 80, 120, 25);
        add(bookingLabel);

        bookingBox = new JComboBox<>();
        bookingBox.setBounds(180, 80, 220, 25);
        add(bookingBox);

        JLabel amountLabel = new JLabel("Amount:");
        amountLabel.setBounds(50, 120, 120, 25);
        add(amountLabel);

        amountField = new JTextField();
        amountField.setBounds(180, 120, 220, 25);
        add(amountField);

        payBtn = new JButton("Pay Now");
        payBtn.setBounds(180, 180, 120, 30);
        payBtn.setBackground(new Color(34, 139, 34));
        payBtn.setForeground(Color.WHITE);
        add(payBtn);

        loadBookings();

        payBtn.addActionListener(e -> makePayment());

        setVisible(true);
    }

    private void loadBookings() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT b.id, p.name FROM bookings b " +
                         "JOIN packages p ON b.package_id = p.id " +
                         "JOIN users u ON b.user_id = u.id " +
                         "WHERE u.username = ? AND b.status = 'Pending'";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int bookingId = rs.getInt("id");
                String packageName = rs.getString("name");
                bookingBox.addItem(bookingId + " - " + packageName);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading bookings!");
        }
    }

    private void makePayment() {
        if (bookingBox.getItemCount() == 0) {
            JOptionPane.showMessageDialog(this, "No pending bookings available!");
            return;
        }

        String selected = (String) bookingBox.getSelectedItem();
        int bookingId = Integer.parseInt(selected.split(" - ")[0]);
        double amount;

        try {
            amount = Double.parseDouble(amountField.getText());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Enter a valid amount!");
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            // Insert payment
            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO payments(booking_id, amount, payment_date) VALUES (?, ?, CURDATE())");
            ps.setInt(1, bookingId);
            ps.setDouble(2, amount);
            ps.executeUpdate();

            // Update booking status to Confirmed
            PreparedStatement ps2 = conn.prepareStatement(
                    "UPDATE bookings SET status='Confirmed' WHERE id=?");
            ps2.setInt(1, bookingId);
            ps2.executeUpdate();

            JOptionPane.showMessageDialog(this, "Payment Successful! Booking Confirmed.");
            dispose();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Payment Failed!");
        }
    }
}