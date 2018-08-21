package web;

import web.exception.InsufficientFundsException;
import web.exception.UsernameTakenException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class DBServiceImpl implements DBService{

    private static Logger log = Logger.getLogger(DBServiceImpl.class.getName());

    private Map<String, User> userMap = new ConcurrentHashMap<>();

    public void registerUser(User user) throws UsernameTakenException {
        if (userMap.containsKey(user.getLogin())){
            throw new UsernameTakenException("Username taken.");
        } else {
            userMap.put(user.getLogin(), user);
        }
    }

    public User getUser(String login) {
        return userMap.getOrDefault(login, null);
    }

    @Override
    public User getUserByPassport(String passport) {
        for (User user : userMap.values()){
            if (user.getPassport().equals(passport)){
                return user;
            }
        }
        return null;
    }

    @Override
    public void withdrawFunds(String passport, double amount) throws InsufficientFundsException {
        User user = getUserByPassport(passport);
        if (user.getBalance() > amount){
            user.removeBalance(amount);
            log.info(String.format("Withdraw %S from %S", amount, user.getPassport()));
        } else {
            log.info(String.format("Failed attempt to withdraw %S from %S", amount, user.getPassport()));
            throw new InsufficientFundsException("Insufficient funds.");
        }
    }

    @Override
    public void addFunds(String passport, double amount){
        User user = getUserByPassport(passport);
        user.addBalance(amount);
        log.info(String.format("Add %S to %S", amount, user.getPassport()));
    }

    @Override
    public double checkBalance(String passport) {
        return getUserByPassport(passport).getBalance();
    }
}
