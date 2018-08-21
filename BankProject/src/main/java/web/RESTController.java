package web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class RESTController {

    private final DBService dbService = new DBServiceImpl();
    private static final Logger log = Logger.getLogger(RESTController.class.getName());

    @RequestMapping(value="/register", method = POST)
    public ResponseEntity<String> register(@RequestParam(value="login") String login,
                                           @RequestParam(value="passport") String passport,
                                           @RequestParam(value="password") String password) {
        User user = dbService.getUser(login);
        if (user != null){
            return new ResponseEntity<>("Username already taken", HttpStatus.FORBIDDEN);
        } else {
            try {
                dbService.registerUser(new User(login, passport, password));
            } catch (UsernameTakenException e){

            }
            return new ResponseEntity<>("Registration success.", HttpStatus.OK);
        }
    }

    @RequestMapping(value="/login", method = POST)
    public ResponseEntity<String> login(@RequestParam(value="login") String login,
                                        @RequestParam(value="password") String password) {
        User user = dbService.getUser(login);
        if (user != null){
            if (user.getPassword().equals(password)){
                return new ResponseEntity<>("Login success.", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Incorrect password.", HttpStatus.FORBIDDEN);
            }
        } else{
            return new ResponseEntity<>("Username not found.", HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value="/withdrawFunds", method = POST)
    public ResponseEntity<String> withdrawFunds(@RequestParam(value="passport") String passport,
                                        @RequestParam(value="amount") double amount){
            try {
                dbService.withdrawFunds(passport, amount);
                return new ResponseEntity<>("Withdrawal success.", HttpStatus.OK);

            } catch (InsufficientFundsException e){
                return new ResponseEntity<>("Insufficient funds.", HttpStatus.BAD_REQUEST);
            }
    }

    @RequestMapping(value="/addFunds", method = POST)
    public ResponseEntity<String> addFunds(@RequestParam(value="passport") String passport,
                                                @RequestParam(value="amount") double amount){
            dbService.addFunds(passport, amount);
            return new ResponseEntity<>("Addition success.", HttpStatus.OK);
    }

    @RequestMapping(value="/checkBalance", method = POST)
    public ResponseEntity<Double> checkBalance(@RequestParam(value="passport") String passport){
        Double balance = dbService.checkBalance(passport);
        return new ResponseEntity<>(balance, HttpStatus.OK);
    }

    @RequestMapping(value="/transferFunds", method = POST)
    public ResponseEntity<String> transferFunds(@RequestParam(value="passport") String passport,
                                                @RequestParam(value="target_passport") String targetPassport,
                                                @RequestParam(value="target_bank_id") String targetBankId,
                                                @RequestParam(value="amount") double amount){
        try {
            //Transfer funds somehow
            log.info(String.format("Transfer %s from account %s to account %s, bank %s.", amount, passport, targetPassport, targetBankId));
            dbService.withdrawFunds(passport, amount);
            return new ResponseEntity<>("Transfer success.", HttpStatus.OK);
        } catch (InsufficientFundsException e) {
            return new ResponseEntity<>("Insufficient funds.", HttpStatus.BAD_REQUEST);
        }
    }
}