package web.pojo;

public class User {
    private long id;
    private String passport;
    private String password;
    String firstName;
    String lastName;
    String phoneNumber;

    public User(String login, String passport, String password) {
        this.passport=passport;
        this.password=password;
    }

    public User() {
    }

    public User(String login, String password) {
        this.passport=null;
        this.password=password;
    }

    public User(String passport, String password, String firstName, String lastName, String phoneNumber) {
        this.passport=passport;
        this.password=password;
        this.firstName=firstName;
        this.lastName=lastName;
        this.phoneNumber=phoneNumber;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public String getPassport() {
        return passport;
    }

    public void setPassport(String passport) {
        this.passport = passport;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
