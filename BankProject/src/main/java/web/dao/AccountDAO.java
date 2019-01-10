package web.dao;

import web.exception.InsufficientFundsException;
import web.pojo.Account;
import web.pojo.TransactionRequest;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by zdoba on 04.12.2018.
 */
public interface AccountDAO {

    BigDecimal withdrawFunds(TransactionRequest transaction) throws InsufficientFundsException;
    void addFunds(TransactionRequest transaction);
    void transfer(TransactionRequest transaction) throws InsufficientFundsException;
    BigDecimal checkBalance(String passport);
    List<Account> getAccounts(String clientId);
}
