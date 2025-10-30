
import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class AddReview extends JFrame {
    JComboBox<String> packageBox;
    JComboBox<Integer> ratingBox;
    JTextArea reviewArea;
    JButton submitBtn;
    String username;

    public AddReview(String username) {
        this.username = username;
        setTitle("Add Review");
        setSize(500, 400);
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel pkgLabel = new JLabel("Select Package:");
        pkgLabel.setBounds(50, 50, 120, 25);
        add(pkgLabel);

        packageBox = new JComboBox<>();
        packageBox.setBounds(180, 50, 200, 25);
        add(packageBox);

        JLabel ratingLabel = new JLabel("Rating:");
        ratingLabel.setBounds(50, 100, 120, 25);
        add(ratingLabel);

        ratingBox = new JComboBox<>(new Integer[]{1,2,3,4,5});
        ratingBox.setBounds(180, 100, 200, 25);
        add(ratingBox);

        JLabel reviewLabel = new JLabel("Review:");
        reviewLabel.setBounds(50, 150, 120, 25);
        add(reviewLabel);

        reviewArea = new JTextArea();
        reviewArea.setBounds(180, 150, 200, 100);
        add(reviewArea);

        submitBtn = new JButton("Submit Review");
        submitBtn.setBounds(180, 270, 150, 30);
        submitBtn.setBackground(new Color(34,139,34));
        submitBtn.setForeground(Color.WHITE);
        add(submitBtn);

        loadPackages();

        submitBtn.addActionListener(e -> submitReview());

        setVisible(true);
    }

    private void loadPackages() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT name FROM packages";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                packageBox.addItem(rs.getString("name"));
            }
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void submitReview() {
        String pkg = (String) packageBox.getSelectedItem();
        int rating = (int) ratingBox.getSelectedItem();
        String reviewText = reviewArea.getText();

        try (Connection conn = DBConnection.getConnection()) {
            // Get user id
            PreparedStatement ps1 = conn.prepareStatement("SELECT id FROM users WHERE username=?");
            ps1.setString(1, username);
            ResultSet rs1 = ps1.executeQuery();
            rs1.next();
            int userId = rs1.getInt("id");

            // Get package id
            PreparedStatement ps2 = conn.prepareStatement("SELECT id FROM packages WHERE name=?");
            ps2.setString(1, pkg);
            ResultSet rs2 = ps2.executeQuery();
            rs2.next();
            int pkgId = rs2.getInt("id");

            PreparedStatement ps3 = conn.prepareStatement(
                "INSERT INTO reviews(user_id, package_id, rating, review, review_date) " +
                "VALUES(?, ?, ?, ?, CURDATE())"
            );
            ps3.setInt(1, userId);
            ps3.setInt(2, pkgId);
            ps3.setInt(3, rating);
            ps3.setString(4, reviewText);

            ps3.executeUpdate();
            JOptionPane.showMessageDialog(this, "Review Submitted!");
            dispose();
        } catch(SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to submit review!");
        }
    }
}