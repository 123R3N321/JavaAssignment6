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

    public void addEntry(String name, String street, String city, String state, String phoneNumber, String email) {
        entries.add(new ContactBookEntry(name, street, city, state, phoneNumber, email));
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
        fileChooser.setFileFilter(new FileNameExtensionFilter("CSV", "csv"));

        int userSelection = fileChooser.showSaveDialog(parent);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            String fileName = fileToSave.getName();
            String extension = "csv";
            if (!fileName.endsWith("." + extension)) {
                fileToSave = new File(fileToSave.getParentFile(), fileName + "." + extension);
            }

            try (FileWriter writer = new FileWriter(fileToSave)) {
                writer.append("Name,Street,City,State,Phone Number,Email\n");
                for (ContactBookEntry entry : entries) {
                    writer.append(String.join(",", entry.getName(), entry.getStreet(), entry.getCity(), entry.getState(), entry.getPhoneNumber(), entry.getEmail())).append("\n");
                }
                JOptionPane.showMessageDialog(parent, "Contact book saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(parent, "Error saving contact book.", "Error", JOptionPane.ERROR_MESSAGE);
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
                boolean isFirstLine = true;
                while ((line = reader.readLine()) != null) {
                    if (isFirstLine) { isFirstLine = false; continue; }
                    String[] data = line.split(",");
                    if (data.length == 6) {
                        addEntry(data[0].trim(), data[1].trim(), data[2].trim(), data[3].trim(), data[4].trim(), data[5].trim());
                    }
                }
                JOptionPane.showMessageDialog(parent, "Contact book loaded successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(parent, "Error loading contact book.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
