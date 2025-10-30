
import javax.swing.*;
import java.awt.*;

public class AdminPanel extends JFrame {
    String username;

    public AdminPanel(String username) {
        this.username = username;

        setTitle("Admin Panel - Travel System");
        setSize(800, 500);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Header
        JLabel header = new JLabel("Welcome, Admin: " + username, JLabel.CENTER);
        header.setFont(new Font("SansSerif", Font.BOLD, 22));
        header.setOpaque(true);
        header.setBackground(new Color(70, 130, 180));
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(800, 60));
        add(header, BorderLayout.NORTH);

        // Main panel with buttons
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(2, 2, 20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        JButton manageBookingsBtn = new JButton("Manage Bookings");
        JButton manageReviewsBtn = new JButton("Manage Reviews");
        JButton manageUsersBtn = new JButton("Manage Users");
        JButton viewPackagesBtn = new JButton("View Packages");

        manageBookingsBtn.setBackground(new Color(34, 139, 34));
        manageBookingsBtn.setForeground(Color.WHITE);
        manageReviewsBtn.setBackground(new Color(255, 140, 0));
        manageReviewsBtn.setForeground(Color.WHITE);
        manageUsersBtn.setBackground(new Color(178, 34, 34));
        manageUsersBtn.setForeground(Color.WHITE);
        viewPackagesBtn.setBackground(new Color(70, 130, 180));
        viewPackagesBtn.setForeground(Color.WHITE);

        manageBookingsBtn.setFont(new Font("SansSerif", Font.BOLD, 16));
        manageReviewsBtn.setFont(new Font("SansSerif", Font.BOLD, 16));
        manageUsersBtn.setFont(new Font("SansSerif", Font.BOLD, 16));
        viewPackagesBtn.setFont(new Font("SansSerif", Font.BOLD, 16));

        mainPanel.add(manageBookingsBtn);
        mainPanel.add(manageReviewsBtn);
        mainPanel.add(manageUsersBtn);
        mainPanel.add(viewPackagesBtn);

        add(mainPanel, BorderLayout.CENTER);

        // Logout button panel
        JPanel logoutPanel = new JPanel();
        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setBackground(new Color(128, 0, 0));
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setFont(new Font("SansSerif", Font.BOLD, 16));
        logoutPanel.add(logoutBtn);
        add(logoutPanel, BorderLayout.SOUTH);

        // Button actions
        manageBookingsBtn.addActionListener(e -> new ManageBookings());
        manageReviewsBtn.addActionListener(e -> new ReviewManagement());
        manageUsersBtn.addActionListener(e -> new UserManagement());
        viewPackagesBtn.addActionListener(e -> new PackageBrowse("admin")); // admin view

        logoutBtn.addActionListener(e -> {
            dispose();  // Close admin panel
            new LoginForm(); // Open login page
        });

        setVisible(true);
    }
}