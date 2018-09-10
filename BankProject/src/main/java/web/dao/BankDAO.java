package web.dao;

import web.pojo.Operation;
import web.pojo.User;
import web.exception.InsufficientFundsException;
import web.exception.UserNotFoundException;
import web.exception.UsernameTakenException;

import java.util.List;

public interface BankDAO {

    void addUser(User user) throws UsernameTakenException;
    User findUserByPassport(String passport) throws UserNotFoundException;
    void deleteUser(String id);

    void withdrawFunds(String passport, double amount) throws InsufficientFundsException;
    void addFunds(String passport, double amount);
    void transfer(String senderPassport, String receiverPassport, double amount) throws InsufficientFundsException;
    double checkBalance(String passport);

    List<Operation> getOperations(String passport);
}
