package web;

import web.exception.InsufficientFundsException;
import web.exception.UsernameTakenException;

public interface BankDAO {

    void addUser(User user);
    User findUserByLogin(String login);
    User findUserByPassport(String passport);

    void withdrawFunds(String passport, double amount) throws InsufficientFundsException;
    void addFunds(String passport, double amount);
    double checkBalance(String passport);

}
