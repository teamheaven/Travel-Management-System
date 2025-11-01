import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class EditPackageForm extends JFrame {
    JTextField nameField, locationField, typeField, budgetField, ratingField;
    JButton updateBtn;
    int packageId;

    public EditPackageForm(int packageId) {
        this.packageId = packageId;

        setTitle("Edit Package");
        setSize(450, 400);
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel title = new JLabel("Edit Package");
        title.setBounds(140, 20, 200, 30);
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        add(title);

        JLabel nameLabel = new JLabel("Package Name:");
        nameLabel.setBounds(50, 70, 120, 25);
        add(nameLabel);

        nameField = new JTextField();
        nameField.setBounds(180, 70, 200, 25);
        add(nameField);

        JLabel locationLabel = new JLabel("Location:");
        locationLabel.setBounds(50, 110, 120, 25);
        add(locationLabel);

        locationField = new JTextField();
        locationField.setBounds(180, 110, 200, 25);
        add(locationField);

        JLabel typeLabel = new JLabel("Type:");
        typeLabel.setBounds(50, 150, 120, 25);
        add(typeLabel);

        typeField = new JTextField();
        typeField.setBounds(180, 150, 200, 25);
        add(typeField);

        JLabel budgetLabel = new JLabel("Budget:");
        budgetLabel.setBounds(50, 190, 120, 25);
        add(budgetLabel);

        budgetField = new JTextField();
        budgetField.setBounds(180, 190, 200, 25);
        add(budgetField);

        JLabel ratingLabel = new JLabel("Rating:");
        ratingLabel.setBounds(50, 230, 120, 25);
        add(ratingLabel);

        ratingField = new JTextField();
        ratingField.setBounds(180, 230, 200, 25);
        add(ratingField);

        updateBtn = new JButton("Update");
        updateBtn.setBounds(180, 280, 120, 30);
        updateBtn.setBackground(new Color(34,139,34));
        updateBtn.setForeground(Color.WHITE);
        add(updateBtn);

        // Load package details
        loadPackageDetails();

        updateBtn.addActionListener(e -> updatePackage());

        setVisible(true);
    }

    private void loadPackageDetails() {
        try(Connection conn = DBConnection.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM packages WHERE id=?");
            ps.setInt(1, packageId);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                nameField.setText(rs.getString("name"));
                locationField.setText(rs.getString("location"));
                typeField.setText(rs.getString("type"));
                budgetField.setText(String.valueOf(rs.getDouble("budget")));
                ratingField.setText(String.valueOf(rs.getDouble("rating")));
            } else {
                JOptionPane.showMessageDialog(this, "Package not found!");
                dispose();
            }
        } catch(SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database Error!");
        }
    }

    private void updatePackage() {
        String name = nameField.getText().trim();
        String location = locationField.getText().trim();
        String type = typeField.getText().trim();
        double budget;
        double rating;

        try {
            budget = Double.parseDouble(budgetField.getText().trim());
            rating = Double.parseDouble(ratingField.getText().trim());
        } catch(NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Budget and Rating must be numbers!");
            return;
        }

        try(Connection conn = DBConnection.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(
                    "UPDATE packages SET name=?, location=?, type=?, budget=?, rating=? WHERE id=?");
            ps.setString(1, name);
            ps.setString(2, location);
            ps.setString(3, type);
            ps.setDouble(4, budget);
            ps.setDouble(5, rating);
            ps.setInt(6, packageId);

            int updated = ps.executeUpdate();
            if(updated > 0) {
                JOptionPane.showMessageDialog(this, "Package Updated Successfully!");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Update Failed!");
            }
        } catch(SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database Error!");
        }
    }

    // Optional main to test
    public static void main(String[] args) {
        new EditPackageForm(1); // Pass any valid packageId
    }
}