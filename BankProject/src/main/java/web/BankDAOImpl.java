package web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import web.exception.InsufficientFundsException;
import web.exception.UsernameTakenException;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
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
        params.put("login",user.getLogin());
        params.put("password",user.getPassword());
        params.put("passport",user.getPassport());

        jdbcTemplate.update("INSERT INTO Users(Login, Password, Passport) VALUES (:login, :password, :passport)", params);
    }

    @Override
    public User findUserByLogin(String login) throws IncorrectResultSizeDataAccessException{

        log.info(String.format("Try to retrieve user %s",login));

        Map<String,String> params = new HashMap<>();
        params.put("login",login);

        User user = jdbcTemplate.queryForObject("SELECT Id, Login, Password FROM Users WHERE Login =:login", params, new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet resultSet, int i) throws SQLException {

                User user = new User();
                user.setId(resultSet.getInt("id"));
                user.setLogin(resultSet.getString("login"));
                user.setPassword(resultSet.getString("password"));

                return user;
            }
        });
        return user;
    }

    @Override
    public User findUserByPassport(String passport) {

        log.info(String.format("Try to retrieve user %s",passport));

        Map<String,String> params = new HashMap<>();
        params.put("passport",passport);

        User user = jdbcTemplate.queryForObject("SELECT Id, Login, Password FROM Users WHERE Passport =:passport", params, new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet resultSet, int i) throws SQLException {

                User user = new User();
                user.setId(resultSet.getInt("id"));
                user.setPassport(resultSet.getString("passport"));

                return user;
            }
        });
        return user;
    }

    @Override
    public void withdrawFunds(String passport, double amount) throws InsufficientFundsException {
        User user = findUserByPassport(passport);
        if (user.getBalance() > amount){
//            user.removeBalance(amount);
            log.info(String.format("Withdraw %S from %S", amount, user.getPassport()));
        } else {
            log.info(String.format("Failed attempt to withdraw %S from %S", amount, user.getPassport()));
            throw new InsufficientFundsException("Insufficient funds.");
        }
    }

    @Override
    public void addFunds(String passport, double amount){
        User user = findUserByPassport(passport);
//        user.addBalance(amount);
        log.info(String.format("Add %S to %S", amount, user.getPassport()));
    }

    @Override
    public double checkBalance(String passport) {
        return findUserByPassport(passport).getBalance();
    }
}
