import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.Vector;

public class RecommendationEngine extends JFrame {
    String username;
    JTable recommendationTable;
    DefaultTableModel tableModel;
    JTextField budgetField;
    JButton recommendBtn;

    public RecommendationEngine(String username) {
        this.username = username;

        setTitle("Package Recommendation");
        setSize(700, 400);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Top panel for input
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout());
        topPanel.add(new JLabel("Enter Maximum Budget:"));
        budgetField = new JTextField(10);
        topPanel.add(budgetField);
        recommendBtn = new JButton("Recommend Packages");
        topPanel.add(recommendBtn);
        add(topPanel, BorderLayout.NORTH);

        // Table for recommendations
        String[] columns = {"Package ID", "Name", "Location", "Type", "Budget", "Rating"};
        tableModel = new DefaultTableModel(columns, 0);
        recommendationTable = new JTable(tableModel);
        add(new JScrollPane(recommendationTable), BorderLayout.CENTER);

        recommendBtn.addActionListener(e -> loadRecommendations());

        setVisible(true);
    }

    private void loadRecommendations() {
        tableModel.setRowCount(0); // Clear previous results
        double maxBudget;
        try {
            maxBudget = Double.parseDouble(budgetField.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid budget!");
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM packages WHERE budget <= ? ORDER BY rating DESC LIMIT 10";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setDouble(1, maxBudget);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("id"));
                row.add(rs.getString("name"));
                row.add(rs.getString("location"));
                row.add(rs.getString("type"));
                row.add(rs.getDouble("budget"));
                row.add(rs.getDouble("rating"));
                tableModel.addRow(row);
            }

            if (tableModel.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "No packages found within this budget!");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database Error!");
        }
    }

    // For testing independently
    public static void main(String[] args) {
        new RecommendationEngine("testUser");
    }
}
