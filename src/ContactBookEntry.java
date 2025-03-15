/**
 * This is the individual entry
 * do NOT support setters because:
 *  we straightaway allow user to delete an entry if not satisfactory
 * future functionalities:
 *  allow sorting based on alpha
 */
class ContactBookEntry {
    private String name;
    private String phoneNumber;

    public ContactBookEntry(String name, String phoneNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Override
    public String toString() {
        return name + " - " + phoneNumber;
    }
}