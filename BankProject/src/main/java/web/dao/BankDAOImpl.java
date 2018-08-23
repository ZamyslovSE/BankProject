package web.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import web.User;
import web.exception.InsufficientFundsException;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@Component
public class BankDAOImpl implements BankDAO {

    private static Logger log = Logger.getLogger(BankDAOImpl.class.getName());

    private static  NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    private void DBServiceImpl(DataSource dataSource) {
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public void addUser(User user) {

        Map<String,String> params = new HashMap<>();
        params.put("password",user.getPassword());
        params.put("passport",user.getPassport());
        params.put("first_name",user.getFirstName());
        params.put("last_name",user.getLastName());
        params.put("phone_number",user.getPhoneNumber());

        jdbcTemplate.update("INSERT INTO Users(Passport, Password, first_name, last_name, phone_number) VALUES (:passport, :password, :first_name, :last_name, :phone_number)", params);
    }

    @Override
    public User findUserByPassport(String passport) {

        log.info(String.format("Try to retrieve user %s",passport));

        Map<String,String> params = new HashMap<>();
        params.put("passport",passport);

        User user = jdbcTemplate.queryForObject("SELECT id, passport, password FROM Users WHERE Passport =:passport", params, (resultSet, i) -> {
            User user1 = new User();
            user1.setId(resultSet.getInt("id"));
            user1.setPassport(resultSet.getString("passport"));
            user1.setPassword(resultSet.getString("password"));
            return user1;
        });
        return user;
    }

    @Override
    public void deleteUser(String id) {
        Map<String,String> params = new HashMap<>();
        params.put("id",id);
        jdbcTemplate.update("DELETE FROM Users WHERE id=:id", params);
    }

    @Override
    public void withdrawFunds(String passport, double amount) throws InsufficientFundsException { //TODO refactor to BigDecimal
        log.info(String.format("Withdrawing %s to %s.",amount,passport));
        double currentBalance = checkBalance(passport);
        if (currentBalance >= amount) {
            double newBalance = currentBalance - amount;
            Map<String, String> params = new HashMap<>();
            params.put("passport", passport);
            params.put("balance", Double.toString(newBalance));
            jdbcTemplate.update("UPDATE Users SET balance=:balance WHERE passport=:passport", params);
        } else {
            throw new InsufficientFundsException("Not enough money on account.");
        }
    }

    @Override
    public void addFunds(String passport, double amount) {
        log.info(String.format("Adding %s to %s.",amount,passport));
        double currentBalance = checkBalance(passport);
        double newBalance = currentBalance + amount;
        Map<String, String> params = new HashMap<>();
        params.put("passport", passport);
        params.put("balance", Double.toString(newBalance));
        jdbcTemplate.update("UPDATE Users SET balance=:balance WHERE passport=:passport", params);
    }

    @Override
    public double checkBalance(String passport) {
        Map<String,String> params = new HashMap<>();
        params.put("passport", passport);
        return jdbcTemplate.queryForObject("SELECT Balance FROM Users WHERE passport=:passport", params, double.class);
    }

    @Override
    @Transactional(rollbackFor = {InsufficientFundsException.class})
    public void transfer(String senderPassport, String receiverPassport, double amount) throws InsufficientFundsException{
        log.info(String.format("Sending %s from %s to %s.",amount,senderPassport,receiverPassport));
        withdrawFunds(senderPassport,amount);
        addFunds(receiverPassport,amount);

    }
}
