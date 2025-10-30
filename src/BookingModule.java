import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class BookingModule extends JFrame {
    String username;
    JComboBox<String> packageBox;
    JTextField personsField;
    JTextField hotelField;
    JButton bookBtn;

    public BookingModule(String username) {
        this.username = username;
        setTitle("Book a Package");
        setSize(500, 350);
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel title = new JLabel("Book a Package");
        title.setBounds(160, 20, 200, 30);
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        add(title);

        JLabel packageLabel = new JLabel("Select Package:");
        packageLabel.setBounds(50, 80, 120, 25);
        add(packageLabel);

        packageBox = new JComboBox<>();
        packageBox.setBounds(180, 80, 220, 25);
        add(packageBox);

        JLabel personsLabel = new JLabel("No. of Persons:");
        personsLabel.setBounds(50, 120, 120, 25);
        add(personsLabel);

        personsField = new JTextField();
        personsField.setBounds(180, 120, 220, 25);
        add(personsField);

        JLabel hotelLabel = new JLabel("Hotel Preference:");
        hotelLabel.setBounds(50, 160, 120, 25);
        add(hotelLabel);

        hotelField = new JTextField();
        hotelField.setBounds(180, 160, 220, 25);
        add(hotelField);

        bookBtn = new JButton("Book Now");
        bookBtn.setBounds(180, 220, 120, 30);
        bookBtn.setBackground(new Color(34, 139, 34));
        bookBtn.setForeground(Color.WHITE);
        add(bookBtn);

        loadPackages();

        bookBtn.addActionListener(e -> bookPackage());

        setVisible(true);
    }

    private void loadPackages() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT name FROM packages";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                packageBox.addItem(rs.getString("name"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load packages!");
        }
    }

    private void bookPackage() {
        String selectedPackage = (String) packageBox.getSelectedItem();
        int persons;
        try {
            persons = Integer.parseInt(personsField.getText());
            if (persons <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Enter a valid number of persons!");
            return;
        }

        String hotel = hotelField.getText().trim();

        try (Connection conn = DBConnection.getConnection()) {
            // Get user_id
            PreparedStatement ps1 = conn.prepareStatement("SELECT id FROM users WHERE username=?");
            ps1.setString(1, username);
            ResultSet rs1 = ps1.executeQuery();
            if (!rs1.next()) {
                JOptionPane.showMessageDialog(this, "User not found!");
                return;
            }
            int userId = rs1.getInt("id");

            // Get package_id
            PreparedStatement ps2 = conn.prepareStatement("SELECT id FROM packages WHERE name=?");
            ps2.setString(1, selectedPackage);
            ResultSet rs2 = ps2.executeQuery();
            if (!rs2.next()) {
                JOptionPane.showMessageDialog(this, "Package not found!");
                return;
            }
            int packageId = rs2.getInt("id");

            // Insert booking
            PreparedStatement ps3 = conn.prepareStatement(
                    "INSERT INTO bookings(user_id, package_id, status) VALUES(?, ?, 'Pending')");
            ps3.setInt(1, userId);
            ps3.setInt(2, packageId);
            ps3.executeUpdate();

            JOptionPane.showMessageDialog(this, "Booking Successful!");
            dispose();

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Booking Failed!");
        }
    }

    // Optional main for testing
    public static void main(String[] args) {
        new BookingModule("customer1");
    }
}
