import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.*;
import com.toedter.calendar.JDateChooser;

public class BookingForm extends JFrame {
    JComboBox<String> packageCombo;
    JDateChooser startDateChooser, endDateChooser;
    JSpinner adultCount, childCount;
    JComboBox<String> hotelPreference;
    JButton bookBtn;
    String username;

    public BookingForm(String username) {
        this.username = username;

        setTitle("Book a Travel Package");
        setSize(550, 520);
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(new Color(245, 247, 250));

        JLabel header = new JLabel("Book Your Trip", JLabel.CENTER);
        header.setFont(new Font("Poppins", Font.BOLD, 22));
        header.setBounds(50, 20, 450, 40);
        header.setForeground(new Color(25, 42, 86));
        add(header);

        // ===== Package selection =====
        JLabel packageLabel = new JLabel("Select Package:");
        packageLabel.setBounds(80, 90, 150, 25);
        packageLabel.setFont(new Font("Poppins", Font.PLAIN, 14));
        add(packageLabel);

        packageCombo = new JComboBox<>();
        packageCombo.setBounds(230, 90, 220, 25);
        add(packageCombo);

        // ===== Start Date =====
        JLabel startLabel = new JLabel("Start Date:");
        startLabel.setBounds(80, 140, 150, 25);
        startLabel.setFont(new Font("Poppins", Font.PLAIN, 14));
        add(startLabel);

        startDateChooser = new JDateChooser();
        startDateChooser.setBounds(230, 140, 220, 25);
        add(startDateChooser);

        // ===== End Date =====
        JLabel endLabel = new JLabel("End Date:");
        endLabel.setBounds(80, 190, 150, 25);
        endLabel.setFont(new Font("Poppins", Font.PLAIN, 14));
        add(endLabel);

        endDateChooser = new JDateChooser();
        endDateChooser.setBounds(230, 190, 220, 25);
        add(endDateChooser);

        // ===== Adults =====
        JLabel adultLabel = new JLabel("Adults:");
        adultLabel.setBounds(80, 240, 150, 25);
        adultLabel.setFont(new Font("Poppins", Font.PLAIN, 14));
        add(adultLabel);

        adultCount = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
        adultCount.setBounds(230, 240, 60, 25);
        add(adultCount);

        // ===== Children =====
        JLabel childLabel = new JLabel("Children:");
        childLabel.setBounds(320, 240, 100, 25);
        childLabel.setFont(new Font("Poppins", Font.PLAIN, 14));
        add(childLabel);

        childCount = new JSpinner(new SpinnerNumberModel(0, 0, 10, 1));
        childCount.setBounds(400, 240, 60, 25);
        add(childCount);

        // ===== Hotel Preference =====
        JLabel hotelLabel = new JLabel("Hotel Preference:");
        hotelLabel.setBounds(80, 290, 150, 25);
        hotelLabel.setFont(new Font("Poppins", Font.PLAIN, 14));
        add(hotelLabel);

        String[] hotelOptions = {"Any", "3 Star", "4 Star", "5 Star", "Luxury Resort"};
        hotelPreference = new JComboBox<>(hotelOptions);
        hotelPreference.setBounds(230, 290, 220, 25);
        add(hotelPreference);

        // ===== Book Button =====
        bookBtn = new JButton("Book Now");
        bookBtn.setBounds(200, 370, 150, 40);
        bookBtn.setBackground(new Color(25, 111, 61));
        bookBtn.setForeground(Color.WHITE);
        bookBtn.setFocusPainted(false);
        bookBtn.setFont(new Font("Poppins", Font.BOLD, 16));
        add(bookBtn);

        // Load packages
        loadPackages();

        // Book button action
        bookBtn.addActionListener(e -> bookPackage());

        setVisible(true);
    }

    private void loadPackages() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT id, name FROM packages";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                packageCombo.addItem(id + " - " + name);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading packages!");
        }
    }

    private void bookPackage() {
        if (packageCombo.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Please select a package!");
            return;
        }

        if (startDateChooser.getDate() == null || endDateChooser.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Please select start and end dates!");
            return;
        }

        String selected = (String) packageCombo.getSelectedItem();
        int packageId = Integer.parseInt(selected.split(" - ")[0]);

        int adults = (int) adultCount.getValue();
        int children = (int) childCount.getValue();
        String hotelPref = (String) hotelPreference.getSelectedItem();

        java.sql.Date startDate = new java.sql.Date(startDateChooser.getDate().getTime());
        java.sql.Date endDate = new java.sql.Date(endDateChooser.getDate().getTime());

        try (Connection conn = DBConnection.getConnection()) {
            // Get user ID
            String userSql = "SELECT id FROM users WHERE username=?";
            PreparedStatement userPs = conn.prepareStatement(userSql);
            userPs.setString(1, username);
            ResultSet userRs = userPs.executeQuery();
            if (!userRs.next()) {
                JOptionPane.showMessageDialog(this, "User not found!");
                return;
            }
            int userId = userRs.getInt("id");

            // Insert booking with new fields
            String sql = "INSERT INTO bookings(user_id, package_id, start_date, end_date, adults, children, hotel_preference, status) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, 'Pending')";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setInt(2, packageId);
            ps.setDate(3, startDate);
            ps.setDate(4, endDate);
            ps.setInt(5, adults);
            ps.setInt(6, children);
            ps.setString(7, hotelPref);

            int inserted = ps.executeUpdate();
            if (inserted > 0) {
                JOptionPane.showMessageDialog(this, "Booking Successful!\nStatus: Pending");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Booking Failed!");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database Error!");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BookingForm("customer1"));
    }
}
