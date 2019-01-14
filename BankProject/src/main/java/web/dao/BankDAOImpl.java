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


}
