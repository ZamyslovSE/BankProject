package web;

public interface DBService {

    void registerUser(User user) throws UsernameTakenException;
    User getUser(String login);
    User getUserByPassport(String passport);

    void withdrawFunds(String passport, double amount) throws InsufficientFundsException;
    void addFunds(String passport, double amount);

    double checkBalance(String passport);

}
