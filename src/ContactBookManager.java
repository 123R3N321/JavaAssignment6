import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

class ContactBookManager {
    private List<ContactBookEntry> entries;

    public ContactBookManager() {
        entries = new ArrayList<>();
    }

    public void addEntry(String name, String phoneNumber) {
        entries.add(new ContactBookEntry(name, phoneNumber));
    }

    public void deleteEntry(int index) {
        if (index >= 0 && index < entries.size()) {
            entries.remove(index);
        }
    }

    public void clearEntries() {
        entries.clear();
    }

    public List<ContactBookEntry> getEntries() {
        return entries;
    }

    public void saveToFile(Component parent) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save to File");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setFileFilter(new FileNameExtensionFilter("CSV, TXT, or MD Files", "csv", "txt", "md"));

        int userSelection = fileChooser.showSaveDialog(parent);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            String fileName = fileToSave.getName();
            String extension = ((FileNameExtensionFilter) fileChooser.getFileFilter()).getExtensions()[0];
            if (!fileName.endsWith("." + extension)) {
                fileToSave = new File(fileToSave.getParentFile(), fileName + "." + extension);
            }

            try (FileWriter writer = new FileWriter(fileToSave)) {
                writer.append("Name,Phone Number\n");
                for (ContactBookEntry entry : entries) {
                    writer.append(entry.getName()).append(",").append(entry.getPhoneNumber()).append("\n");
                }
                JOptionPane.showMessageDialog(parent, "Phonebook saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(parent, "Error saving phonebook.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void loadFromFile(Component parent) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Load CSV file");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setFileFilter(new FileNameExtensionFilter("CSV Files", "csv"));

        int userSelection = fileChooser.showOpenDialog(parent);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToOpen = fileChooser.getSelectedFile();

            try (BufferedReader reader = new BufferedReader(new FileReader(fileToOpen))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    // Skip the header line
                    if (line.startsWith("Name")) continue;

                    String[] data = line.split(",");
                    if (data.length == 2) {
                        addEntry(data[0].trim(), data[1].trim());
                    }
                }
                JOptionPane.showMessageDialog(parent, "Phonebook loaded successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(parent, "Error loading phonebook.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}