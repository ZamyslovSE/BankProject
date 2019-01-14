package web.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import web.exception.InsufficientFundsException;
import web.pojo.Account;
import web.pojo.TransactionRequest;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zdoba on 04.12.2018.
 */
@Component
public class AccountDAOImpl implements AccountDAO {

    private static  NamedParameterJdbcTemplate jdbcTemplate;

    private static String SQL_ACCOUNTS = "SELECT accountNumber, balance " +
                                         "FROM client JOIN account ON account.clientId=client.id " +
                                         "WHERE client.id=:clientId";


    private static String SQL_ACCOUNT = "SELECT accountNumber, balance " +
            "FROM account " +
            "WHERE account.accountNumber=:accountNumber";

    private static String SQL_TRANSACTION = "INSERT INTO transaction( " +
            "VALUES account.accountNumber=:accountNumber";

    @Autowired
    private void AccountDAOImpl(DataSource dataSource) {
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }


    @Override
    public BigDecimal checkBalance(String passport) {
        return null;
    }

    @Override
    public List<Account> getAccounts(String clientId) {
        Map<String, Object> params = new HashMap<>();
        params.put("clientId", clientId);
        List<Account> accountList = jdbcTemplate.query(SQL_ACCOUNTS, params, new RowMapper<Account>() {
            @Override
            public Account mapRow(ResultSet resultSet, int i) throws SQLException {
                Account account = new Account();
//                account.setId(resultSet.getString("id"));
                account.setAccountNumber(resultSet.getString("accountNumber"));
//                account.setClientId(resultSet.getString("clientId"));
                account.setBalance(resultSet.getDouble("balance"));
                return account;
            }
        });
        return accountList;
    }

    @Override
    @Transactional
    public void transfer(String senderAccountNumber, String receiverAccountNumber, double amount) {
        Account senderAccount = getAccountByNumber(senderAccountNumber);
        Account receiverAccount = getAccountByNumber(senderAccountNumber);
        withdrawFunds(senderAccountNumber, amount);
        addFunds(receiverAccountNumber,amount);
//        Map<String, Object> params = new HashMap<>();
//        params.put("senderAccount", senderAccount);
//        params.put("receiverAccount", receiverAccount);
//        List<Account> accountList = jdbcTemplate.query(SQL_ACCOUNTS, params, new RowMapper<Account>() {
//            @Override
//            public Account mapRow(ResultSet resultSet, int i) throws SQLException {
//                Account account = new Account();
////                account.setId(resultSet.getString("id"));
//                account.setAccountNumber(resultSet.getString("accountNumber"));
////                account.setClientId(resultSet.getString("clientId"));
//                account.setBalance(resultSet.getDouble("balance"));
//                return account;
//            }
//        });
//        return accountList;
    }

    @Override
    public Account getAccountByNumber(String accountNumber) {
        Map<String, Object> params = new HashMap<>();
        params.put("accountNumber", accountNumber);
        Account account = jdbcTemplate.queryForObject(SQL_ACCOUNT, params, (resultSet, i) -> {
            Account account1 = new Account();
//                account.setId(resultSet.getString("id"));
            account1.setAccountNumber(resultSet.getString("accountNumber"));
//                account.setClientId(resultSet.getString("clientId"));
            account1.setBalance(resultSet.getDouble("balance"));
            return account1;
        });
        return account;
    }

    @Override
    public boolean withdrawFunds(String accountNumber, double amount){
        Account account = getAccountByNumber(accountNumber);
        if (account.getBalance() < amount)
            return false;
        Map<String, Object> params = new HashMap<>();
        params.put("accountNumber", accountNumber);
        params.put("balance", accountNumber);
        account.setBalance(account.getBalance()-amount);
        jdbcTemplate.queryForObject(SQL_TRANSACTION, params);
    }

    @Override
    public void addFunds(String accountNumber, double amount){

    }
}
