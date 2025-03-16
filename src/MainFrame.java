import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

class MainFrame extends JFrame {
    private ContactBookManager contactBookManager;
    private DefaultTableModel tableModel;
    private JTable phoneBookTable;
    private JTextField searchField;

    public MainFrame() {
        contactBookManager = new ContactBookManager();
        tableModel = new DefaultTableModel(new Object[]{"Name", "Street", "City", "State", "Phone", "Email"}, 0);
        phoneBookTable = new JTable(tableModel);
        searchField = new JTextField();

        setTitle("Contact Book");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Menu bar
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem saveItem = new JMenuItem("Save to File");
        JMenuItem loadItem = new JMenuItem("Load from File");
        JMenuItem exitItem = new JMenuItem("Exit");

        saveItem.addActionListener(e -> contactBookManager.saveToFile(this));
        loadItem.addActionListener(e -> loadPhoneBook());
        exitItem.addActionListener(e -> System.exit(0));

        fileMenu.add(saveItem);
        fileMenu.add(loadItem);
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);

        // Input panel
        JPanel inputPanel = new JPanel(new GridLayout(3, 4, 5, 5));
        JTextField nameField = new JTextField();
        JTextField streetField = new JTextField();
        JTextField cityField = new JTextField();
        JTextField stateField = new JTextField();
        JTextField phoneField = new JTextField();
        JTextField emailField = new JTextField();

        inputPanel.add(new JLabel("Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Street:"));
        inputPanel.add(streetField);
        inputPanel.add(new JLabel("City:"));
        inputPanel.add(cityField);
        inputPanel.add(new JLabel("State:"));
        inputPanel.add(stateField);
        inputPanel.add(new JLabel("Phone:"));
        inputPanel.add(phoneField);
        inputPanel.add(new JLabel("Email:"));
        inputPanel.add(emailField);

        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add");
        JButton deleteButton = new JButton("Delete Selected");
        JButton deleteAllButton = new JButton("Delete All");

        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(deleteAllButton);

        // Top panel (search + input fields)
        JPanel topPanel = new JPanel(new BorderLayout());
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.add(new JLabel("Search: "), BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);
        topPanel.add(searchPanel, BorderLayout.NORTH);
        topPanel.add(inputPanel, BorderLayout.CENTER);

        // Add components to the frame
        JScrollPane tableScrollPane = new JScrollPane(phoneBookTable);
        add(topPanel, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Action listeners
        addButton.addActionListener(e -> {
            String name = nameField.getText();
            String street = streetField.getText();
            String city = cityField.getText();
            String state = stateField.getText();
            String phone = phoneField.getText();
            String email = emailField.getText();
            if (!name.isEmpty() && !phone.isEmpty()) {
                contactBookManager.addEntry(name, street, city, state, phone, email);
                updateTableModel();
                nameField.setText("");
                streetField.setText("");
                cityField.setText("");
                stateField.setText("");
                phoneField.setText("");
                emailField.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Name and phone must be filled!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        deleteButton.addActionListener(e -> {
            int selectedRow = phoneBookTable.getSelectedRow();
            if (selectedRow != -1) {
                contactBookManager.deleteEntry(selectedRow);
                updateTableModel();
            }
        });

        deleteAllButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete all entries?", "Delete All?", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                contactBookManager.clearEntries();
                updateTableModel();
            }
        });

        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filterTable();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filterTable();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filterTable();
            }
        });
    }

    private void filterTable() {
        String filter = searchField.getText().toLowerCase();
        tableModel.setRowCount(0);
        for (ContactBookEntry entry : contactBookManager.getEntries()) {
            if (entry.getName().toLowerCase().contains(filter) ||
                    entry.getStreet().toLowerCase().contains(filter) ||
                    entry.getCity().toLowerCase().contains(filter) ||
                    entry.getState().toLowerCase().contains(filter) ||
                    entry.getPhoneNumber().contains(filter) ||
                    entry.getEmail().toLowerCase().contains(filter)) {
                tableModel.addRow(new Object[]{entry.getName(), entry.getStreet(), entry.getCity(), entry.getState(), entry.getPhoneNumber(), entry.getEmail()});
            }
        }
    }

    private void loadPhoneBook() {
        contactBookManager.loadFromFile(this);
        updateTableModel();
    }

    private void updateTableModel() {
        tableModel.setRowCount(0);
        for (ContactBookEntry entry : contactBookManager.getEntries()) {
            tableModel.addRow(new Object[]{entry.getName(), entry.getStreet(), entry.getCity(), entry.getState(), entry.getPhoneNumber(), entry.getEmail()});
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
