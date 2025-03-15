import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

class MainFrame extends JFrame {
    private ContactBookManager contactBookManager;
    private DefaultTableModel tableModel;
    private JTable phoneBookTable;
    private JTextField searchField;

    public MainFrame() {
        contactBookManager = new ContactBookManager();
        tableModel = new DefaultTableModel(new Object[] { "Name", "Contact Number" }, 0);
        phoneBookTable = new JTable(tableModel);
        searchField = new JTextField();

        setTitle("Contact Book");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Menu bar
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem saveItem = new JMenuItem("Save to File");
        JMenuItem loadItem = new JMenuItem("Load from File");
        JMenuItem exitItem = new JMenuItem("Exit");

        // Action listeners for menu items
        saveItem.addActionListener(e -> contactBookManager.saveToFile(this));
        loadItem.addActionListener(e -> loadPhoneBook());
        exitItem.addActionListener(e -> System.exit(0)); // Exit the application

        fileMenu.add(saveItem);
        fileMenu.add(loadItem);
        fileMenu.add(exitItem); // Add the Exit item to the menu
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);

        // Input panel
        JPanel inputPanel = new JPanel(new GridLayout(4, 2));
        JPanel buttonPanel = new JPanel(new BorderLayout());
        JTextField nameField = new JTextField();
        JTextField phoneField = new JTextField();
        JButton addButton = new JButton("Add");
        JButton deleteButton = new JButton("Delete Selected");
        JButton deleteAllButton = new JButton("Delete All");

        inputPanel.add(new JLabel("Search:"));
        inputPanel.add(searchField);
        inputPanel.add(new JLabel("Enter Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Enter Phone Number:"));
        inputPanel.add(phoneField);
        inputPanel.add(addButton);
        inputPanel.add(deleteButton);
        buttonPanel.add(deleteAllButton);

        // Add components to the frame
        JScrollPane tableScrollPane = new JScrollPane(phoneBookTable);
        add(tableScrollPane, BorderLayout.CENTER);
        add(inputPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);

        // Action listeners
        addButton.addActionListener(e -> {
            String name = nameField.getText();
            String phone = phoneField.getText();
            if (!name.isEmpty() && !phone.isEmpty()) {
                contactBookManager.addEntry(name, phone);
                tableModel.addRow(new Object[]{name, phone});
                nameField.setText("");
                phoneField.setText("");
            } else {
                JOptionPane.showMessageDialog(null, "Both fields must be filled!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        deleteButton.addActionListener(e -> {
            int selectedRow = phoneBookTable.getSelectedRow();
            if (selectedRow != -1) {
                contactBookManager.deleteEntry(selectedRow);
                tableModel.removeRow(selectedRow);
            }
        });

        deleteAllButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete all entries?", "Delete All? Sure???", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                contactBookManager.clearEntries();
                tableModel.setRowCount(0); // Clear all rows
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

        // Set table properties
        phoneBookTable.setShowGrid(false);  // Hide gridlines
        phoneBookTable.setIntercellSpacing(new Dimension(0, 0));  // Remove cell spacing

        // Customize the first row background color
        phoneBookTable.getTableHeader().setReorderingAllowed(false);  // Disable column reordering
        phoneBookTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                label.setBackground(new Color(221, 255, 221)); // Light GREEN color for first row

                // Set foreground color for selected rows
                if (isSelected) {
                    label.setBackground(Color.BLUE);
                    label.setForeground(Color.WHITE);
                }
                return label;
            }
        });
    }

    private void filterTable() {
        String filter = searchField.getText().toLowerCase();
        tableModel.setRowCount(0); // Clear the table
        for (ContactBookEntry entry : contactBookManager.getEntries()) {
            if (entry.getName().toLowerCase().contains(filter) || entry.getPhoneNumber().contains(filter)) {
                tableModel.addRow(new Object[]{entry.getName(), entry.getPhoneNumber()});
            }
        }
    }

    private void loadPhoneBook() {
        contactBookManager.loadFromFile(this);
        updateTableModel();
    }

    private void updateTableModel() {
        tableModel.setRowCount(0); // Clear the table
        for (ContactBookEntry entry : contactBookManager.getEntries()) {
            tableModel.addRow(new Object[]{entry.getName(), entry.getPhoneNumber()});
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
