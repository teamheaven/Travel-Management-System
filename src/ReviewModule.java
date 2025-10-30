import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class ReviewModule extends JFrame {
    String username;
    JComboBox<String> packageBox;
    JComboBox<Integer> ratingBox;
    JTextArea reviewArea;
    JButton submitBtn;

    public ReviewModule(String username) {
        this.username = username;
        setTitle("Add / Update Review");
        setSize(500, 400);
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel title = new JLabel("Submit Your Review");
        title.setBounds(130, 20, 250, 30);
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        add(title);

        JLabel packageLabel = new JLabel("Select Package:");
        packageLabel.setBounds(50, 70, 120, 25);
        add(packageLabel);

        packageBox = new JComboBox<>();
        packageBox.setBounds(180, 70, 220, 25);
        add(packageBox);

        JLabel ratingLabel = new JLabel("Rating (1-5):");
        ratingLabel.setBounds(50, 110, 120, 25);
        add(ratingLabel);

        ratingBox = new JComboBox<>();
        for (int i = 1; i <= 5; i++) ratingBox.addItem(i);
        ratingBox.setBounds(180, 110, 220, 25);
        add(ratingBox);

        JLabel reviewLabel = new JLabel("Review:");
        reviewLabel.setBounds(50, 150, 120, 25);
        add(reviewLabel);

        reviewArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(reviewArea);
        scrollPane.setBounds(180, 150, 220, 100);
        add(scrollPane);

        submitBtn = new JButton("Submit Review");
        submitBtn.setBounds(180, 270, 150, 30);
        submitBtn.setBackground(new Color(34, 139, 34));
        submitBtn.setForeground(Color.WHITE);
        add(submitBtn);

        loadPackages();

        submitBtn.addActionListener(e -> submitReview());

        setVisible(true);
    }

    private void loadPackages() {
        try (Connection conn = DBConnection.getConnection()) {
            // Only show packages booked by this user
            String sql = "SELECT p.name FROM packages p " +
                    "JOIN bookings b ON p.id = b.package_id " +
                    "JOIN users u ON b.user_id = u.id " +
                    "WHERE u.username = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                packageBox.addItem(rs.getString("name"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading packages!");
        }
    }

    private void submitReview() {
        String selectedPackage = (String) packageBox.getSelectedItem();
        int rating = (int) ratingBox.getSelectedItem();
        String reviewText = reviewArea.getText().trim();

        if (reviewText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please write a review!");
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            // get user_id
            PreparedStatement ps1 = conn.prepareStatement("SELECT id FROM users WHERE username=?");
            ps1.setString(1, username);
            ResultSet rs1 = ps1.executeQuery();
            rs1.next();
            int userId = rs1.getInt("id");

            // get package_id
            PreparedStatement ps2 = conn.prepareStatement("SELECT id FROM packages WHERE name=?");
            ps2.setString(1, selectedPackage);
            ResultSet rs2 = ps2.executeQuery();
            rs2.next();
            int packageId = rs2.getInt("id");

            // check if review already exists
            PreparedStatement psCheck = conn.prepareStatement(
                    "SELECT * FROM reviews WHERE user_id=? AND package_id=?");
            psCheck.setInt(1, userId);
            psCheck.setInt(2, packageId);
            ResultSet rsCheck = psCheck.executeQuery();

            if (rsCheck.next()) {
                // Update existing review
                PreparedStatement psUpdate = conn.prepareStatement(
                        "UPDATE reviews SET rating=?, review=?, review_date=CURDATE() WHERE user_id=? AND package_id=?");
                psUpdate.setInt(1, rating);
                psUpdate.setString(2, reviewText);
                psUpdate.setInt(3, userId);
                psUpdate.setInt(4, packageId);
                psUpdate.executeUpdate();
                JOptionPane.showMessageDialog(this, "Review updated successfully!");
            } else {
                // Insert new review
                PreparedStatement psInsert = conn.prepareStatement(
                        "INSERT INTO reviews(user_id, package_id, rating, review, review_date) " +
                                "VALUES(?, ?, ?, ?, CURDATE())");
                psInsert.setInt(1, userId);
                psInsert.setInt(2, packageId);
                psInsert.setInt(3, rating);
                psInsert.setString(4, reviewText);
                psInsert.executeUpdate();
                JOptionPane.showMessageDialog(this, "Review submitted successfully!");
            }
            dispose();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error submitting review!");
        }
    }

    public static void main(String[] args) {
        new ReviewModule("customer"); // test with a sample username
    }
}