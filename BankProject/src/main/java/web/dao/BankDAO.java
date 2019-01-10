package web.dao;

import web.pojo.Account;
import web.pojo.TransactionRequest;
import web.pojo.User;
import web.exception.InsufficientFundsException;
import web.exception.UserNotFoundException;
import web.exception.UsernameTakenException;

import java.math.BigDecimal;
import java.util.List;

public interface BankDAO {

    void addUser(User user) throws UsernameTakenException;
    User findUserByUsername(String username) throws UserNotFoundException;

}
