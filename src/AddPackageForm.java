import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class AddPackageForm extends JFrame {
    JTextField nameField, locationField, typeField, budgetField;
    JButton addBtn, backBtn;

    public AddPackageForm() {
        setTitle("Add New Package - Travel System");
        setSize(500, 400);
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel header = new JLabel("Add New Package", JLabel.CENTER);
        header.setBounds(0, 20, 500, 30);
        header.setFont(new Font("SansSerif", Font.BOLD, 20));
        add(header);

        JLabel nameLabel = new JLabel("Package Name:");
        nameLabel.setBounds(50, 80, 120, 25);
        add(nameLabel);
        nameField = new JTextField();
        nameField.setBounds(180, 80, 250, 25);
        add(nameField);

        JLabel locationLabel = new JLabel("Location:");
        locationLabel.setBounds(50, 120, 120, 25);
        add(locationLabel);
        locationField = new JTextField();
        locationField.setBounds(180, 120, 250, 25);
        add(locationField);

        JLabel typeLabel = new JLabel("Type:");
        typeLabel.setBounds(50, 160, 120, 25);
        add(typeLabel);
        typeField = new JTextField();
        typeField.setBounds(180, 160, 250, 25);
        add(typeField);

        JLabel budgetLabel = new JLabel("Budget:");
        budgetLabel.setBounds(50, 200, 120, 25);
        add(budgetLabel);
        budgetField = new JTextField();
        budgetField.setBounds(180, 200, 250, 25);
        add(budgetField);

        addBtn = new JButton("Add Package");
        addBtn.setBounds(100, 260, 140, 35);
        addBtn.setBackground(new Color(34, 139, 34));
        addBtn.setForeground(Color.WHITE);
        add(addBtn);

        backBtn = new JButton("Back");
        backBtn.setBounds(260, 260, 120, 35);
        backBtn.setBackground(new Color(178, 34, 34));
        backBtn.setForeground(Color.WHITE);
        add(backBtn);

        // Button Actions
        addBtn.addActionListener(e -> addPackage());
        backBtn.addActionListener(e -> dispose());

        setVisible(true);
    }

    private void addPackage() {
        String name = nameField.getText().trim();
        String location = locationField.getText().trim();
        String type = typeField.getText().trim();
        String budgetStr = budgetField.getText().trim();

        if(name.isEmpty() || location.isEmpty() || type.isEmpty() || budgetStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields!");
            return;
        }

        double budget;
        try {
            budget = Double.parseDouble(budgetStr);
        } catch(NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Budget must be a valid number!");
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "INSERT INTO packages(name, location, type, budget) VALUES(?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, location);
            ps.setString(3, type);
            ps.setDouble(4, budget);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Package Added Successfully!");
            // Clear fields
            nameField.setText("");
            locationField.setText("");
            typeField.setText("");
            budgetField.setText("");
        } catch(SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database Error!");
        }
    }

    public static void main(String[] args) {
        new AddPackageForm();
    }
}
