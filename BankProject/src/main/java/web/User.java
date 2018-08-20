package web;

public class User {
    private final String login;
    private final String passport;
    private final String password;
    private double balance;

    public User(String login, String passport, String password) {
        this.login=login;
        this.passport=passport;
        this.password=password;
        this.balance=0.0;
    }

    public String getLogin() {
        return login;
    }

    public String getPassport() {
        return passport;
    }

    public String getPassword() {
        return password;
    }

    public double getBalance() {
        return balance;
    }

    public void addBalance(double amount){
        balance+=amount;
    }

    public void removeBalance(double amount){
        balance-=amount;
    }
}
