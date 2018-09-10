package web;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.xstream.XStream;
import jdk.jfr.ContentType;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import web.dao.BankDAO;
import web.dao.BankDAOImpl;
import web.exception.InsufficientFundsException;
import web.exception.UserNotFoundException;
import web.exception.UsernameTakenException;
import web.pojo.Operation;
import web.pojo.User;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class RESTController {

    private final BankDAO bankDAO = new BankDAOImpl();
    private static final Logger log = Logger.getLogger(RESTController.class.getName());
    private static final String GENERIC_ERROR = "Error, please try again later";

    @RequestMapping(value="/register", method = POST)
    public ResponseEntity<String> register(@RequestParam(value="passport") String passport,
                                           @RequestParam(value="password") String password,
                                           @RequestParam(value="first_name", required = false) String firstName,
                                           @RequestParam(value="last_name", required = false) String lastName,
                                           @RequestParam(value="phone_number", required = false) String phoneNumber) {

        if (password.length()<6){
            return new ResponseEntity<>("Password should be at least 6 characters.", HttpStatus.BAD_REQUEST);
        }
        try {
            bankDAO.addUser(new User(passport, password, firstName, lastName, phoneNumber));
        } catch (UsernameTakenException e){
            return new ResponseEntity<>("Account with this passport number already exists.", HttpStatus.BAD_REQUEST);
        } catch (DataAccessException e){
            e.printStackTrace();
            return new ResponseEntity<>("Registration failed, please try again later.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("Registration success.", HttpStatus.OK);
    }

    @RequestMapping(value="/deleteUser", method = POST)
    public ResponseEntity<String> deleteUser(@RequestParam(value="id") String id) {
        try {
            bankDAO.deleteUser(id);
            return new ResponseEntity<>("Removal successful.", HttpStatus.OK);
        } catch (DataAccessException e){
            e.printStackTrace();
            return new ResponseEntity<>("Failed to remove account, please try again later.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value="/login", method = POST)
    public ResponseEntity<String> login(@RequestParam(value="passport") String passport,
                                        @RequestParam(value="password") String password) {

        try {
            User user = bankDAO.findUserByPassport(passport);
            if (user.getPassword().equals(password)){
                return new ResponseEntity<>("Login success.", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Combination of username/password was not found.", HttpStatus.BAD_REQUEST);
            }
        } catch (UserNotFoundException e){
            return new ResponseEntity<>("Combination of username/password was not found.", HttpStatus.BAD_REQUEST);
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
            return new ResponseEntity<>(GENERIC_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value="/getOperations", method = POST)
    public ResponseEntity<String> getOperations( @RequestParam(value = "passport") String passport,
                                                 @RequestHeader HttpHeaders headers){
        MediaType mime = headers.getContentType();
        if (mime == null){
            log.info("Missing header in request.");
            return new ResponseEntity<>(GENERIC_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        List<Operation> operations = bankDAO.getOperations(passport);

        if (mime.equals(MediaType.APPLICATION_JSON) || mime.equals(MediaType.APPLICATION_JSON_UTF8)) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                String json = objectMapper.writeValueAsString(operations);
                return new ResponseEntity<>(json, HttpStatus.OK);
            } catch (JsonProcessingException e) {
                log.info("Error serializing json.");
                e.clearLocation();
                return new ResponseEntity<>(GENERIC_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else if (mime.equals(MediaType.APPLICATION_XML) || mime.equals(MediaType.TEXT_XML)){
            try{
                XStream xstream = new XStream();
                String xml = xstream.toXML(operations);
                return new ResponseEntity<>(xml, HttpStatus.OK);
            } catch (Exception e){
                log.info("Error marshalling xml.");
                e.printStackTrace();
                return new ResponseEntity<>(GENERIC_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>(GENERIC_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value="/batchOperation", method = POST)
    public ResponseEntity<String> loadBatchOperations( @RequestParam(value = "content") String content,
                                                       @RequestHeader HttpHeaders headers){
//        String jsonArray = "[{\"brand\":\"ford\"}, {\"brand\":\"Fiat\"}]";
        if (content.startsWith("{") || content.startsWith("[")) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                List<Operation> operations = objectMapper.readValue(content, new TypeReference<List<Operation>>() {
                });
                //TODO db operation
                return new ResponseEntity<>("Success.", HttpStatus.OK);
            } catch (JsonMappingException|JsonParseException e){
                log.info("Error parsing json");
                return new ResponseEntity<>("Could not finish transaction.", HttpStatus.INTERNAL_SERVER_ERROR);
            } catch (IOException e){
                return new ResponseEntity<>(GENERIC_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else if (content.startsWith("<")){
            //TODO parsing and db operation
            return new ResponseEntity<>("Success.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Incorrect format.", HttpStatus.BAD_REQUEST);
        }
    }
}