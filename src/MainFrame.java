import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

class MainFrame extends JFrame {
    private ContactBookManager contactBookManager;
    private DefaultListModel<String> listModel;
    private JList<String> phoneBookList;
    private JTextField searchField;

    public MainFrame() {
        contactBookManager = new ContactBookManager();
        listModel = new DefaultListModel<>();
        phoneBookList = new JList<>(listModel);
        searchField = new JTextField();

        setTitle("PhoneBook App");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // [redacted]
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
        JPanel inputPanel = new JPanel(new GridLayout(5, 2));
        JTextField nameField = new JTextField();
        JTextField phoneField = new JTextField();
        JButton addButton = new JButton("Add");
        JButton deleteButton = new JButton("Delete");
        JButton deleteAllButton = new JButton("Delete All");

        inputPanel.add(new JLabel("Search:"));
        inputPanel.add(searchField);
        inputPanel.add(new JLabel("Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Phone Number:"));
        inputPanel.add(phoneField);
        inputPanel.add(addButton);
        inputPanel.add(deleteButton);
        inputPanel.add(deleteAllButton);

        add(new JScrollPane(phoneBookList), BorderLayout.CENTER);
        add(inputPanel, BorderLayout.SOUTH);

        addButton.addActionListener(e -> {
            String name = nameField.getText();
            String phone = phoneField.getText();
            if (!name.isEmpty() && !phone.isEmpty()) {
                contactBookManager.addEntry(name, phone);
                listModel.addElement(name + " - " + phone);
                nameField.setText("");
                phoneField.setText("");
            } else {
                JOptionPane.showMessageDialog(null, "Both fields must be filled!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        deleteButton.addActionListener(e -> {
            int selectedIndex = phoneBookList.getSelectedIndex();
            if (selectedIndex != -1) {
                contactBookManager.deleteEntry(selectedIndex);
                listModel.remove(selectedIndex);
            }
        });

        deleteAllButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete all entries?", "Delete All? Sure???", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                contactBookManager.clearEntries();
                listModel.clear();
            }
        });

        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filterList();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filterList();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filterList();
            }
        });
    }

    private void filterList() {
        String filter = searchField.getText().toLowerCase();
        listModel.clear();
        for (ContactBookEntry entry : contactBookManager.getEntries()) {
            if (entry.getName().toLowerCase().contains(filter) || entry.getPhoneNumber().contains(filter)) {
                listModel.addElement(entry.toString());
            }
        }
    }

    private void loadPhoneBook() {
        contactBookManager.loadFromFile(this);
        updateListModel();
    }

    private void updateListModel() {
        listModel.clear();
        for (ContactBookEntry entry : contactBookManager.getEntries()) {
            listModel.addElement(entry.toString());
        }
    }
}