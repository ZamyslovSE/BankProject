package web;

public class User {
    private long id;
    private String login;
    private String passport;
    private String password;
    private double balance;

    public User(String login, String passport, String password) {
        this.login=login;
        this.passport=passport;
        this.password=password;
        this.balance=0.0;
    }

    public User() {
    }

    public User(String login, String password) {
        this.login=login;
        this.passport=null;
        this.password=password;
        this.balance=0.0;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
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

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
