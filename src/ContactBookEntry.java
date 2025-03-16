/**
 * This is the individual entry
 * do NOT support setters because:
 * we straightaway allow user to delete an entry if not satisfactory
 * future functionalities:
 * allow sorting based on alpha
 */
class ContactBookEntry {
    private String name;
    private String street;
    private String city;
    private String state;
    private String phoneNumber;
    private String email;

    public ContactBookEntry(String name, String street, String city, String state, String phoneNumber, String email) {
        this.name = name;
        this.street = street;
        this.city = city;
        this.state = state;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getStreet() {
        return street;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return name + " - " +
                street + " - " +
                city + " - " +
                state + " - "
                + phoneNumber + " - " +
                email;
    }
}