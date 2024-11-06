package Database;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.MongoException;
import org.bson.Document;
import java.util.ArrayList;
import java.util.List;
import java.time.Year;

public class DatabaseMain extends JFrame {
    
    private JTextField idField, moneyField, interestRateField, firstNameField, lastNameField, ageField;
    private JComboBox<String> dayOpenCombo, monthOpenCombo, yearOpenCombo;
    private JComboBox<String> dayBirthCombo, monthBirthCombo, yearBirthCombo;
    private JButton saveButton, showButton;
    private MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection<Document> collection;

    public DatabaseMain() {
        initializeComponents();
        initializeMongoDB();
        setupGUI();
        initializeComboBoxes();
        addEventListeners();
    }

    private void initializeComponents() {
        // Initialize text fields
        idField = new JTextField(10);
        moneyField = new JTextField(10);
        interestRateField = new JTextField(20);
        firstNameField = new JTextField(20);
        lastNameField = new JTextField(20);
        ageField = new JTextField(5);

        // Initialize combo boxes
        dayOpenCombo = new JComboBox<>();
        monthOpenCombo = new JComboBox<>();
        yearOpenCombo = new JComboBox<>();
        dayBirthCombo = new JComboBox<>();
        monthBirthCombo = new JComboBox<>();
        yearBirthCombo = new JComboBox<>();

        // Initialize buttons
        saveButton = new JButton("SAVE");
        showButton = new JButton("SHOW");
    }

    private void initializeMongoDB() {
        try {
            mongoClient = MongoClients.create("mongodb://localhost:27017");
            database = mongoClient.getDatabase("accountDB");
            collection = database.getCollection("accounts");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error connecting to MongoDB: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void setupGUI() {
        setTitle("Show Detail of Account");
        setSize(390, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("ACCOUNT MONEY", SwingConstants.CENTER);
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        TitledBorder titledBorder = BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            "Enter Data Account Money"
        );
        titledBorder.setTitleJustification(TitledBorder.LEFT);
        formPanel.setBorder(titledBorder);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(2, 5, 2, 5);

        // ID and Money fields
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("ID:"), gbc);
        gbc.gridx = 1;
        formPanel.add(idField, gbc);
        gbc.gridx = 2;
        formPanel.add(new JLabel("MONEY:"), gbc);
        gbc.gridx = 3;
        formPanel.add(moneyField, gbc);
        gbc.gridx = 4;
        formPanel.add(new JLabel("BATH:"), gbc);

        // Interest Rate
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
        formPanel.add(new JLabel("ANNUAL INTEREST RATE:"), gbc);
        gbc.gridx = 2; gbc.gridwidth = 3;
        formPanel.add(interestRateField, gbc);

        // Open Account Date
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        formPanel.add(new JLabel("DAY OPEN ACCOUNT:"), gbc);
        gbc.gridx = 2; gbc.gridwidth = 1;
        formPanel.add(dayOpenCombo, gbc);
        gbc.gridx = 3;
        formPanel.add(monthOpenCombo, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(yearOpenCombo, gbc);
        gbc.gridx = 1; gbc.gridwidth = 1;
        formPanel.add(new JLabel("FIRST NAME:"), gbc);
        gbc.gridx = 2; gbc.gridwidth = 3;
        formPanel.add(firstNameField, gbc);

        // Last Name
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        formPanel.add(new JLabel("LAST NAME:"), gbc);
        gbc.gridx = 2; gbc.gridwidth = 3;
        formPanel.add(lastNameField, gbc);

        // Birth Date
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        formPanel.add(new JLabel("BIRTH DAY:"), gbc);
        gbc.gridx = 2; gbc.gridwidth = 1;
        formPanel.add(dayBirthCombo, gbc);
        gbc.gridx = 3;
        formPanel.add(monthBirthCombo, gbc);
        gbc.gridx = 4;
        formPanel.add(yearBirthCombo, gbc);

        // Age
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 1;
        formPanel.add(new JLabel("AGE:"), gbc);
        gbc.gridx = 1;
        formPanel.add(ageField, gbc);
        gbc.gridx = 2;
        formPanel.add(new JLabel("YEAR"), gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(saveButton);
        buttonPanel.add(showButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void initializeComboBoxes() {
        String[] days = new String[31];
        for (int i = 1; i <= 31; i++) {
            days[i-1] = String.format("%02d", i);
        }
        dayOpenCombo.setModel(new DefaultComboBoxModel<>(days));
        dayBirthCombo.setModel(new DefaultComboBoxModel<>(days));

        String[] months = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
        monthOpenCombo.setModel(new DefaultComboBoxModel<>(months));
        monthBirthCombo.setModel(new DefaultComboBoxModel<>(months));

        String[] years = new String[100];
        int currentYear = Year.now().getValue();
        for (int i = 0; i < 100; i++) {
            years[i] = String.valueOf(currentYear - i);
        }
        yearOpenCombo.setModel(new DefaultComboBoxModel<>(years));
        yearBirthCombo.setModel(new DefaultComboBoxModel<>(years));
    }

    private void addEventListeners() {
        saveButton.addActionListener(e -> saveData());
        showButton.addActionListener(e -> showData());

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (mongoClient != null) {
                    mongoClient.close();
                }
            }
        });
    }

    private void saveData() {
        try {
            if (!validateInput()) {
                return;
            }

            Document document = new Document()
                .append("id", idField.getText().trim())
                .append("money", Double.parseDouble(moneyField.getText().trim()))
                .append("interestRate", Double.parseDouble(interestRateField.getText().trim()))
                .append("openDate", String.format("%s-%s-%s",
                    yearOpenCombo.getSelectedItem(),
                    monthOpenCombo.getSelectedItem(),
                    dayOpenCombo.getSelectedItem()))
                .append("firstName", firstNameField.getText().trim())
                .append("lastName", lastNameField.getText().trim())
                .append("birthDate", String.format("%s-%s-%s",
                    yearBirthCombo.getSelectedItem(),
                    monthBirthCombo.getSelectedItem(),
                    dayBirthCombo.getSelectedItem()))
                .append("age", Integer.parseInt(ageField.getText().trim()));

            // Check for existing account
            Document existing = collection.find(Filters.eq("id", idField.getText().trim())).first();
            if (existing != null) {
                int option = JOptionPane.showConfirmDialog(this,
                    "An account with this ID already exists. Do you want to update it?",
                    "Update Existing Account",
                    JOptionPane.YES_NO_OPTION);
                
                if (option == JOptionPane.YES_OPTION) {
                    collection.replaceOne(Filters.eq("id", idField.getText().trim()), document);
                    JOptionPane.showMessageDialog(this, "Account updated successfully!");
                }
            } else {
                collection.insertOne(document);
                JOptionPane.showMessageDialog(this, "Account saved successfully!");
            }
            clearFields();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error saving data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean validateInput() {
        if (idField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter an ID");
            return false;
        }
        
        try {
            Double.parseDouble(moneyField.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid amount for money");
            return false;
        }
        
        try {
            Double.parseDouble(interestRateField.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid interest rate");
            return false;
        }
        
        if (firstNameField.getText().trim().isEmpty() || lastNameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both first and last names");
            return false;
        }
        
        try {
            int age = Integer.parseInt(ageField.getText().trim());
            if (age < 0 || age > 150) {
                JOptionPane.showMessageDialog(this, "Please enter a valid age (0-150)");
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid age");
            return false;
        }
        
        return true;
    }

    private void showData() {
        try {
            FindIterable<Document> documents = collection.find();
            List<String> data = new ArrayList<>();

            for (Document doc : documents) {
                StringBuilder record = new StringBuilder();
                record.append("ID: ").append(doc.getString("id")).append("\n");
                record.append("Name: ").append(doc.getString("firstName"))
                      .append(" ").append(doc.getString("lastName")).append("\n");
                record.append("Money: ").append(doc.getDouble("money")).append(" BATH\n");
                record.append("Interest Rate: ").append(doc.getDouble("interestRate")).append("%\n");
                record.append("Open Account Date: ").append(doc.getString("openDate")).append("\n");
                record.append("Birth Date: ").append(doc.getString("birthDate")).append("\n");
                record.append("Age: ").append(doc.getInteger("age")).append(" years");
                
                data.add(record.toString());
            }

            if (!data.isEmpty()) {
                for (int i = 0; i < data.size(); i += 4) {
                    StringBuilder message = new StringBuilder();
                    for (int j = i; j < Math.min(i + 4, data.size()); j++) {
                        message.append(data.get(j)).append("\n\n");
                    }
                    JOptionPane.showMessageDialog(this, message.toString(), "Account Data", JOptionPane.PLAIN_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "No accounts found in the database.");
            }
        } catch (MongoException e) {
            JOptionPane.showMessageDialog(this, "Error retrieving data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void clearFields() {
        idField.setText("");
        moneyField.setText("");
        interestRateField.setText("");
        firstNameField.setText("");
        lastNameField.setText("");
        ageField.setText("");
        dayOpenCombo.setSelectedIndex(0);
        monthOpenCombo.setSelectedIndex(0);
        yearOpenCombo.setSelectedIndex(0);
        dayBirthCombo.setSelectedIndex(0);
        monthBirthCombo.setSelectedIndex(0);
        yearBirthCombo.setSelectedIndex(0);
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            DatabaseMain frame = new DatabaseMain();
            frame.setVisible(true);
        });
    }
}