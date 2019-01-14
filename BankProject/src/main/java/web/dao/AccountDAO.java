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

    List<Account> getAccounts(String clientId);

    void transfer(String senderAccount, String receiverAccount, Double amount);

    Account getAccountByNumber(String accountNumber);
}
