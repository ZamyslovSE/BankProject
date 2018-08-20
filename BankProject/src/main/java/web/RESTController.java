package web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RESTController {

    private final DBService dbService = new DBServiceImpl();

    @RequestMapping("/register")
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

    @RequestMapping("/login")
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

    @RequestMapping("/withdrawFunds")
    public ResponseEntity<String> withdrawFunds(@RequestParam(value="passport") String passport,
                                        @RequestParam(value="amount") double amount){
            try {
                dbService.withdrawFunds(passport, amount);
                return new ResponseEntity<>("Withdrawal success.", HttpStatus.OK);

            } catch (InsufficientFundsException e){
                return new ResponseEntity<>("Insufficient funds.", HttpStatus.BAD_REQUEST);
            }
    }

    @RequestMapping("/addFunds")
    public ResponseEntity<String> addFunds(@RequestParam(value="passport") String passport,
                                                @RequestParam(value="amount") double amount){
            dbService.addFunds(passport, amount);
            return new ResponseEntity<>("Addition success.", HttpStatus.OK);
    }

    @RequestMapping("/checkBalance")
    public ResponseEntity<Double> checkBalance(@RequestParam(value="passport") String passport){
        Double balance = dbService.checkBalance(passport);
        return new ResponseEntity<>(balance, HttpStatus.OK);
    }
}