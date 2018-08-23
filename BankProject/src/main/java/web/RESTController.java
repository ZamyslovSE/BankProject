package web;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import web.dao.BankDAO;
import web.dao.BankDAOImpl;
import web.exception.InsufficientFundsException;

import java.util.logging.Logger;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class RESTController {

    private final BankDAO bankDAO = new BankDAOImpl();
    private static final Logger log = Logger.getLogger(RESTController.class.getName());

    @RequestMapping(value="/register", method = POST)
    public ResponseEntity<String> register(@RequestParam(value="passport") String passport,
                                           @RequestParam(value="password") String password,
                                           @RequestParam(value="first_name") String firstName,
                                           @RequestParam(value="last_name") String lastName,
                                           @RequestParam(value="phone_number") String phoneNumber) {
            try {
                bankDAO.addUser(new User(passport, password, firstName, lastName, phoneNumber));
                return new ResponseEntity<>("Registration success.", HttpStatus.OK);
            } catch (DuplicateKeyException e){
                return new ResponseEntity<>("Account with this passport number already exists.", HttpStatus.BAD_REQUEST);
            } catch (DataAccessException e){
                e.printStackTrace();
                return new ResponseEntity<>("Registration failed, please try again later.", HttpStatus.INTERNAL_SERVER_ERROR);
            }
//        }
    }

    @RequestMapping(value="/deleteUser", method = POST)
    public ResponseEntity<String> deleteUser(@RequestParam(value="id") String id) {
        try {
            bankDAO.deleteUser(id);
            return new ResponseEntity<>("Registration success.", HttpStatus.OK);
        } catch (DataAccessException e){
            e.printStackTrace();
            return new ResponseEntity<>("Registration failed, please try again later.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
//        }
    }

    @RequestMapping(value="/login", method = POST)
    public ResponseEntity<String> login(@RequestParam(value="passport") String passport,
                                        @RequestParam(value="password") String password) {

        log.info(String.format("Login attempt with credentials: %s %s", passport, password));

        try {
            User user = bankDAO.findUserByPassport(passport);
            if (user.getPassword().equals(password)){
                return new ResponseEntity<>("Login success.", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Incorrect password.", HttpStatus.FORBIDDEN);
            }
        } catch (IncorrectResultSizeDataAccessException e){
            return new ResponseEntity<>("Username not found.", HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value="/withdrawFunds", method = POST)
    public ResponseEntity<String> withdrawFunds(@RequestParam(value="passport") String passport,
                                        @RequestParam(value="amount") double amount){
            try {
                bankDAO.withdrawFunds(passport, amount);
                return new ResponseEntity<>("Withdrawal success.", HttpStatus.OK);

            } catch (InsufficientFundsException e){
                e.printStackTrace();
                return new ResponseEntity<>("Insufficient funds.", HttpStatus.BAD_REQUEST);
            }
    }

    @RequestMapping(value="/addFunds", method = POST)
    public ResponseEntity<String> addFunds(@RequestParam(value="passport") String passport,
                                                @RequestParam(value="amount") double amount){
            bankDAO.addFunds(passport, amount);
            return new ResponseEntity<>("Addition success.", HttpStatus.OK);
    }

    @RequestMapping(value="/checkBalance", method = POST)
    public ResponseEntity<Double> checkBalance(@RequestParam(value="passport") String passport){
        Double balance = bankDAO.checkBalance(passport);
        return new ResponseEntity<>(balance, HttpStatus.OK);
    }

    @RequestMapping(value="/transferFunds", method = POST)
    public ResponseEntity<String> transferFunds(@RequestParam(value="sender_passport") String senderPassport,
                                                @RequestParam(value="target_passport") String targetPassport,
                                                @RequestParam(value="amount") double amount){
        try {
            //Transfer funds somehow
            log.info(String.format("Transfer %s from account %s to account %s.", amount, senderPassport, targetPassport));
            bankDAO.transfer(senderPassport, targetPassport, amount);
            return new ResponseEntity<>("Transfer success.", HttpStatus.OK);
        } catch (InsufficientFundsException e) {
            return new ResponseEntity<>("Could not transfer funds, not enough funds on sender account.", HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value="/receiveFunds", method = POST)
    public ResponseEntity<String> receiveFunds( @RequestParam(value="sender_passport") String senderPassport,
                                                @RequestParam(value="sender_bank_id") String senderBankId,
                                                @RequestParam(value="target_passport") String targetPassport,
                                                @RequestParam(value="amount") double amount){
        try {
            bankDAO.addFunds(senderPassport, amount);

            log.info(String.format("Transfer %s from account %s (bank %s) to account %s.", amount, senderPassport, senderBankId, targetPassport));
            return new ResponseEntity<>("Transfer success.", HttpStatus.OK);
        } catch (DataAccessException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Insufficient funds.", HttpStatus.BAD_REQUEST);
        }
    }
}