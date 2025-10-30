import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class AllBookings extends JFrame {
    JTable bookingsTable;
    DefaultTableModel tableModel;

    public AllBookings() {
        setTitle("All Bookings - Travel System");
        setSize(900, 500);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel header = new JLabel("All Bookings", JLabel.CENTER);
        header.setFont(new Font("SansSerif", Font.BOLD, 22));
        header.setOpaque(true);
        header.setBackground(new Color(70, 130, 180));
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(900, 60));
        add(header, BorderLayout.NORTH);

        // Table setup
        tableModel = new DefaultTableModel();
        bookingsTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(bookingsTable);
        add(scrollPane, BorderLayout.CENTER);

        // Load bookings data
        loadBookings();

        setVisible(true);
    }

    private void loadBookings() {
        tableModel.setRowCount(0);
        tableModel.setColumnCount(0);

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT b.id AS BookingID, u.username AS User, p.name AS Package, b.status, b.booking_date " +
                         "FROM bookings b " +
                         "JOIN users u ON b.user_id = u.id " +
                         "JOIN packages p ON b.package_id = p.id " +
                         "ORDER BY b.booking_date DESC";

            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            // Add columns dynamically
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                tableModel.addColumn(rsmd.getColumnLabel(i));
            }

            // Add rows
            while (rs.next()) {
                Object[] row = new Object[columnCount];
                for (int i = 0; i < columnCount; i++) {
                    row[i] = rs.getObject(i + 1);
                }
                tableModel.addRow(row);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading bookings!");
        }
    }

    // Optional main method for testing
    public static void main(String[] args) {
        new AllBookings();
    }
}
