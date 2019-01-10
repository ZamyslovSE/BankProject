package web.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
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

    @Autowired
    private void BankDAOImpl(DataSource dataSource) {
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public BigDecimal withdrawFunds(TransactionRequest transaction) throws InsufficientFundsException {
        return null;
    }

    @Override
    public void addFunds(TransactionRequest transaction) {

    }

    @Override
    public void transfer(TransactionRequest transaction) throws InsufficientFundsException {

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
}
