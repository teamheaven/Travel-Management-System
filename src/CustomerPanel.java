import javax.swing.*;
import java.awt.*;

public class CustomerPanel extends JFrame {
    String username;

    public CustomerPanel(String username) {
        this.username = username;

        setTitle("Travel Management System");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // ====== TOP BAR ======
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(new Color(25, 42, 86)); // deep blue header
        topBar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel appTitle = new JLabel("Travel Management System");
        appTitle.setFont(new Font("Poppins", Font.BOLD, 18));
        appTitle.setForeground(Color.WHITE);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightPanel.setOpaque(false);

        JLabel userLabel = new JLabel(username);
        userLabel.setFont(new Font("Poppins", Font.PLAIN, 14));
        userLabel.setForeground(Color.WHITE);

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setBackground(new Color(231, 76, 60));
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setFocusPainted(false);
        logoutBtn.setFont(new Font("Poppins", Font.BOLD, 14));
        logoutBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        rightPanel.add(userLabel);
        rightPanel.add(logoutBtn);

        topBar.add(appTitle, BorderLayout.WEST);
        topBar.add(rightPanel, BorderLayout.EAST);
        add(topBar, BorderLayout.NORTH);

        // ====== SIDEBAR ======
        JPanel sidebar = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gradient = new GradientPaint(
                        0, 0, new Color(33, 37, 41),
                        0, getHeight(), new Color(23, 27, 31)
                );
                g2.setPaint(gradient);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        sidebar.setLayout(new BorderLayout());
        sidebar.setPreferredSize(new Dimension(230, getHeight()));
        sidebar.setBorder(BorderFactory.createEmptyBorder(25, 20, 15, 20));

        // Buttons container
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new GridLayout(5, 1, 0, 15)); // spacing between buttons

        JButton bookPackageBtn = createSidebarButton("Book Package");
        JButton myBookingsBtn = createSidebarButton("My Bookings");
        JButton myReviewsBtn = createSidebarButton("My Reviews");
        JButton browsePackagesBtn = createSidebarButton("Browse Packages");

        buttonPanel.add(bookPackageBtn);
        buttonPanel.add(myBookingsBtn);
        buttonPanel.add(myReviewsBtn);
        buttonPanel.add(browsePackagesBtn);

        sidebar.add(buttonPanel, BorderLayout.CENTER);

        // Footer text at bottom
        JLabel footerLabel = new JLabel("@trav_manager", JLabel.CENTER);
        footerLabel.setFont(new Font("Poppins", Font.PLAIN, 13));
        footerLabel.setForeground(new Color(180, 180, 180));
        footerLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 5, 0));
        sidebar.add(footerLabel, BorderLayout.SOUTH);

        add(sidebar, BorderLayout.WEST);

        // ====== MAIN CONTENT ======
        ImageIcon bgImage = new ImageIcon("C:\\Users\\deves\\Downloads\\Travel\\src\\customer_panel_bg.jpeg");
        Image img = bgImage.getImage();

        JPanel mainPanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();

                // Draw scaled background image
                g2d.drawImage(img, 0, 0, getWidth(), getHeight(), this);

                // Full black overlay (40% transparency)
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
                g2d.setColor(Color.BLACK);
                g2d.fillRect(0, 0, getWidth(), getHeight());

                g2d.dispose();
            }
        };
        mainPanel.setOpaque(false);

        JPanel textPanel = new JPanel(new GridLayout(2, 1, 0, 10));
        textPanel.setOpaque(false);

        JLabel welcomeLabel = new JLabel("Welcome, " + username, JLabel.CENTER);
        welcomeLabel.setFont(new Font("Segoe Script", Font.BOLD, 34));
        welcomeLabel.setForeground(Color.WHITE);

        JLabel subText = new JLabel("Select an option from the sidebar to get started.", JLabel.CENTER);
        subText.setFont(new Font("Poppins", Font.PLAIN, 16));
        subText.setForeground(new Color(220, 220, 220));

        textPanel.add(welcomeLabel);
        textPanel.add(subText);
        mainPanel.add(textPanel);

        add(mainPanel, BorderLayout.CENTER);

        // ====== ACTIONS ======
        bookPackageBtn.addActionListener(e -> new BookingForm(username));
        myBookingsBtn.addActionListener(e -> new MyBookings(username));
        myReviewsBtn.addActionListener(e -> new MyReview(username));
        browsePackagesBtn.addActionListener(e -> new PackageBrowse("customer"));

        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginForm();
        });

        setVisible(true);
    }

    private JButton createSidebarButton(String text) {
        JButton btn = new JButton(text);
        btn.setFocusPainted(false);
        btn.setBackground(new Color(52, 58, 64));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Poppins", Font.PLAIN, 15));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 10));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setOpaque(true);
        return btn;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CustomerPanel("user1"));
    }
}
