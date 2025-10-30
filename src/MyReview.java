import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class MyReview extends JFrame {
    String username;
    JTable reviewTable;
    DefaultTableModel model;

    public MyReview(String username) {
        this.username = username;

        setTitle("My Reviews");
        setSize(700, 400);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Header
        JLabel header = new JLabel("My Reviews", JLabel.CENTER);
        header.setFont(new Font("SansSerif", Font.BOLD, 22));
        header.setOpaque(true);
        header.setBackground(new Color(70, 130, 180));
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(700, 50));
        add(header, BorderLayout.NORTH);

        // Table
        model = new DefaultTableModel();
        model.setColumnIdentifiers(new Object[]{"Package", "Rating", "Review", "Date"});
        reviewTable = new JTable(model);
        JScrollPane scroll = new JScrollPane(reviewTable);
        add(scroll, BorderLayout.CENTER);

        // Bottom panel for Add Review button
        JPanel bottomPanel = new JPanel();
        JButton addReviewBtn = new JButton("Add Review");
        addReviewBtn.setBackground(new Color(34, 139, 34));
        addReviewBtn.setForeground(Color.WHITE);
        addReviewBtn.setFont(new Font("SansSerif", Font.BOLD, 16));
        bottomPanel.add(addReviewBtn);
        add(bottomPanel, BorderLayout.SOUTH);

        // Load reviews
        loadReviews();

        // Button action
        addReviewBtn.addActionListener(e -> new AddReview(username));

        setVisible(true);
    }

    private void loadReviews() {
        model.setRowCount(0);
        try (Connection conn = DBConnection.getConnection()) {
            // Get user_id
            PreparedStatement ps1 = conn.prepareStatement("SELECT id FROM users WHERE username=?");
            ps1.setString(1, username);
            ResultSet rs1 = ps1.executeQuery();
            if (!rs1.next()) return;
            int userId = rs1.getInt("id");

            // Get reviews
            PreparedStatement ps2 = conn.prepareStatement(
                "SELECT p.name AS package_name, r.rating, r.review, r.review_date " +
                "FROM reviews r JOIN packages p ON r.package_id = p.id WHERE r.user_id=?"
            );
            ps2.setInt(1, userId);
            ResultSet rs2 = ps2.executeQuery();
            while (rs2.next()) {
                model.addRow(new Object[]{
                    rs2.getString("package_name"),
                    rs2.getInt("rating"),
                    rs2.getString("review"),
                    rs2.getDate("review_date")
                });
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading reviews!");
        }
    }
}
