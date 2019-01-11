package web.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import web.pojo.TransactionRequest;
import web.pojo.User;
import web.exception.InsufficientFundsException;
import web.exception.UserNotFoundException;
import web.exception.UsernameTakenException;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Component
public class BankDAOImpl implements BankDAO {

    private static Logger log = Logger.getLogger(BankDAOImpl.class.getName());

    private static  NamedParameterJdbcTemplate jdbcTemplate;

    private static String SQL_SELECT_OPER_BY_PASSPORT = "SELECT * FROM Operations WHERE Sender_passport=:passport OR Receiver_passport=:passport";
    private static String SQL_CHECK_BALANCE = "SELECT Balance FROM Users WHERE passport=:passport";

    @Autowired
    private void BankDAOImpl(DataSource dataSource) {
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public void addUser(User user) throws UsernameTakenException {

        Map<String,String> params = new HashMap<>();
        params.put("password",user.getPassword());
        params.put("login",user.getLogin());
        try {
            jdbcTemplate.update("INSERT INTO Users(login, password, passport, firstName, secondName, lastName) VALUES (:login, :password, :passport, :firstName, :secondName, :lastName)", params);
        } catch (DuplicateKeyException e){
//            e.printStackTrace();
            throw new UsernameTakenException("Passport number taken",e);
        } catch (DataAccessException e){
            e.printStackTrace();
        }
    }

    @Override
    public User findUserByUsername(String login) throws UserNotFoundException{

        log.info(String.format("Try to retrieve user %s",login));

        Map<String,String> params = new HashMap<>();
        params.put("login",login);
        try {
            User user = jdbcTemplate.queryForObject("SELECT * FROM client WHERE login=:login", params, (resultSet, i) -> {
                User user1 = new User();
                user1.setId(resultSet.getString("id"));
                user1.setLogin(resultSet.getString("login"));
                user1.setPassword(resultSet.getString("password"));
                return user1;
            });
            return user;
        } catch (IncorrectResultSizeDataAccessException e){
            throw new UserNotFoundException(login);
        }
    }
//
//    @Override
//    public void deleteUser(String id) {
//        Map<String,String> params = new HashMap<>();
//        params.put("id",id);
//        jdbcTemplate.update("DELETE FROM Users WHERE id=:id", params);
//    }
//
//    @Override
//    @Transactional
//    public BigDecimal withdrawFunds(TransactionRequest transaction) throws InsufficientFundsException { //TODO refactor to BigDecimal
//        log.info(String.format("Withdrawing %s to %s.",transaction.getAmount(),transaction.getReceiverId()));
//        BigDecimal currentBalance = checkBalance(transaction.getReceiverId());
//        if (currentBalance.compareTo(transaction.getAmount()) >= 0) {
//            BigDecimal newBalance = currentBalance.subtract(transaction.getAmount());
//            Map<String, String> params = new HashMap<>();
//            params.put("passport", transaction.getReceiverId());
//            params.put("balance", newBalance.toString());
//            jdbcTemplate.update("UPDATE Users SET balance=:balance WHERE passport=:passport", params);
//            return newBalance;
//        } else {
//            throw new InsufficientFundsException(String.format("Could not withdraw %s, not enough money on account.",transaction.getAmount()));
//        }
//    }
//
//    @Override
//    @Transactional
//    public void addFunds(TransactionRequest transaction) {
//        log.info(String.format("Adding %s to %s.", transaction.getAmount(), transaction.getReceiverId()));
//        BigDecimal currentBalance = checkBalance(transaction.getReceiverId());
//        BigDecimal newBalance = currentBalance.add(transaction.getAmount());
//        Map<String, String> params = new HashMap<>();
//        params.put("passport", transaction.getReceiverId());
//        params.put("balance", newBalance.toString());
//        jdbcTemplate.update("UPDATE Users SET balance=:balance WHERE passport=:passport", params);
//    }
//
//    @Override
//    public BigDecimal checkBalance(String passport) {
//        Map<String,String> params = new HashMap<>();
//        params.put("passport", passport);
//        BigDecimal balance = jdbcTemplate.queryForObject(SQL_CHECK_BALANCE, params, BigDecimal.class);
//        return balance;
//    }
//
//    @Override
//    @Transactional
//    public void transfer(TransactionRequest transaction) throws InsufficientFundsException{
////        log.info(String.format("Sending %s from %s to %s.",amount,senderPassport,receiverPassport));
//        withdrawFunds(transaction);
//        addFunds(transaction);
//
//    }
//
//    @Override
//    public List<TransactionRequest> getOperations(String passport) {
//        Map<String,String> params = new HashMap<>();
//        params.put("passport", passport);
//        List<TransactionRequest> resultList = jdbcTemplate.queryForList(SQL_SELECT_OPER_BY_PASSPORT, params, TransactionRequest.class);
//        return resultList;
//    }
}
