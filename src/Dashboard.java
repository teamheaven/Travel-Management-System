import javax.swing.*;
import java.awt.*;

public class Dashboard extends JFrame {
    String username;
    String role;

    public Dashboard(String username, String role) {
        this.username = username;
        this.role = role.toLowerCase();

        setTitle("Dashboard - Travel System");
        setSize(800, 500);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Header
        JLabel header = new JLabel("Welcome, " + username + " (" + role + ")", JLabel.CENTER);
        header.setFont(new Font("SansSerif", Font.BOLD, 22));
        header.setOpaque(true);
        header.setBackground(new Color(70, 130, 180));
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(800, 60));
        add(header, BorderLayout.NORTH);

        // Main panel for buttons
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(2, 3, 20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        if(role.equals("admin")) {
            JButton manageBookingsBtn = new JButton("Manage Bookings");
            JButton manageUsersBtn = new JButton("Manage Users");
            JButton manageReviewsBtn = new JButton("Manage Reviews");
            JButton viewPackagesBtn = new JButton("View Packages");
            JButton addPackageBtn = new JButton("Add Package");

            styleButton(manageBookingsBtn, new Color(34, 139, 34));
            styleButton(manageUsersBtn, new Color(178, 34, 34));
            styleButton(manageReviewsBtn, new Color(255, 140, 0));
            styleButton(viewPackagesBtn, new Color(70, 130, 180));
            styleButton(addPackageBtn, new Color(128, 0, 128));

            mainPanel.add(manageBookingsBtn);
            mainPanel.add(manageUsersBtn);
            mainPanel.add(manageReviewsBtn);
            mainPanel.add(viewPackagesBtn);
            mainPanel.add(addPackageBtn);

            // Actions
            manageBookingsBtn.addActionListener(e -> new ManageBookings());
            manageUsersBtn.addActionListener(e -> new UserManagement());
            manageReviewsBtn.addActionListener(e -> new ReviewManagement());
            viewPackagesBtn.addActionListener(e -> new PackageBrowse("admin"));
            addPackageBtn.addActionListener(e -> new AddPackageForm());

        } else {
            JButton browsePackagesBtn = new JButton("Browse Packages");
            JButton myBookingsBtn = new JButton("My Bookings");
            JButton myReviewsBtn = new JButton("My Reviews");
            JButton bookPackageBtn = new JButton("Book a Package");

            styleButton(browsePackagesBtn, new Color(70, 130, 180));
            styleButton(myBookingsBtn, new Color(34, 139, 34));
            styleButton(myReviewsBtn, new Color(255, 140, 0));
            styleButton(bookPackageBtn, new Color(178, 34, 34));

            mainPanel.add(browsePackagesBtn);
            mainPanel.add(myBookingsBtn);
            mainPanel.add(myReviewsBtn);
            mainPanel.add(bookPackageBtn);

            // Actions
            browsePackagesBtn.addActionListener(e -> new PackageBrowse(username));
            myBookingsBtn.addActionListener(e -> new MyBookings(username));
            myReviewsBtn.addActionListener(e -> new MyReview(username));
            bookPackageBtn.addActionListener(e -> new BookingModule(username));
        }

        add(mainPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    private void styleButton(JButton btn, Color bg) {
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("SansSerif", Font.BOLD, 16));
    }

    // Optional main for testing
    public static void main(String[] args) {
        new Dashboard("admin", "admin");
        new Dashboard("customer1", "customer");
    }
}
