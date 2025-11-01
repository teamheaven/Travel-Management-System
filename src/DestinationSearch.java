
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class DestinationSearch extends JFrame {
    JTextField searchField;
    JButton searchBtn;
    JTable resultTable;
    DefaultTableModel tableModel;
    String username; // For booking if needed

    public DestinationSearch(String username) {
        this.username = username;

        setTitle("Search Destinations");
        setSize(700, 450);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Top panel for search
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JLabel searchLabel = new JLabel("Search by Location/Type:");
        searchField = new JTextField(20);
        searchBtn = new JButton("Search");
        topPanel.add(searchLabel);
        topPanel.add(searchField);
        topPanel.add(searchBtn);
        add(topPanel, BorderLayout.NORTH);

        // Table to display results
        tableModel = new DefaultTableModel(new String[]{"ID", "Name", "Location", "Type", "Budget", "Rating"}, 0);
        resultTable = new JTable(tableModel);
        resultTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(resultTable);
        add(scrollPane, BorderLayout.CENTER);

        // Search action
        searchBtn.addActionListener(e -> performSearch());

        // Double-click to book
        resultTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if(evt.getClickCount() == 2) {
                    int row = resultTable.getSelectedRow();
                    if(row != -1) {
                        int packageId = (int) tableModel.getValueAt(row, 0);
                        String packageName = (String) tableModel.getValueAt(row, 1);
                        new BookingModule(username); // Open booking module
                    }
                }
            }
        });

        setVisible(true);
    }

    private void performSearch() {
        String keyword = searchField.getText().trim();
        tableModel.setRowCount(0); // clear previous results

        if(keyword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter a location or type to search.");
            return;
        }

        try(Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM packages WHERE location LIKE ? OR type LIKE ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, "%" + keyword + "%");
            ps.setString(2, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                Object[] row = new Object[]{
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("location"),
                        rs.getString("type"),
                        rs.getDouble("budget"),
                        rs.getDouble("rating")
                };
                tableModel.addRow(row);
            }

            if(tableModel.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "No packages found for: " + keyword);
            }
        } catch(SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database Error!");
        }
    }

    // Optional test main
    public static void main(String[] args) {
        new DestinationSearch("customer1");
    }
}