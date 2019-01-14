package web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.parsing.Problem;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import web.dao.AccountDAO;
import web.dao.BankDAO;
import web.dao.OtherDAO;
import web.exception.UnauthorizedException;
import web.exception.UserNotFoundException;
import web.exception.UsernameTakenException;
import web.pojo.Account;
import web.pojo.Product;
import web.pojo.Token;
import web.pojo.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@SuppressWarnings("unused")
@Controller
public class RESTController {

    private final BankDAO bankDAO;
    private final AccountDAO accountDAO;
    private final OtherDAO otherDAO;

    private final ObjectMapper objectMapper;
//    private final SessionService sessionService;

    private static final Logger log = Logger.getLogger(RESTController.class.getName());
    private static final String GENERIC_ERROR = "Error, please try again later";
//    private static final String RESOURCE_FOLDER = "resources";

    @Autowired
    public RESTController(BankDAO bankDAO, AccountDAO accountDAO, OtherDAO otherDAO) {
        this.bankDAO = bankDAO;
        this.accountDAO = accountDAO;
        this.otherDAO = otherDAO;
        objectMapper = new ObjectMapper();
//        sessionService = new SessionService();
    }


    @GetMapping("/*")
    public String index(HttpServletResponse response) throws IOException {
        return "redirect:/home";
    }

    @GetMapping("/home")
    public String home(HttpServletResponse response) throws IOException {
        return "/home";
    }

    @GetMapping("/client")
    public String client(HttpServletResponse response/*, @RequestBody String json*/) throws IOException {
//        String token = objectMapper.readValue(json, Token.class).getToken();
//        if (token==null){
//            throw new IllegalArgumentException("No auth token in request");
//        }
//        if (!loggedIn(token)){
////            throw new UnauthorizedException("Not authorized");
//            return "redirect:/home";
//        }
        return "/client";
    }

    @RequestMapping(value = "/register", method = POST)
    public ResponseEntity<String> register(@RequestBody String userJson) {
        try {
            User user = objectMapper.readValue(userJson, User.class);
            bankDAO.addUser(user);
            return new ResponseEntity<>("Registration success.", HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(String.format("%s %s", e.getClass().getName(), e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (UsernameTakenException e) {
            return new ResponseEntity<>(String.format("%s %s", e.getClass().getName(), e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/auth", method = POST)
    @ResponseBody
    public String auth(HttpServletRequest request, HttpServletResponse response, @RequestBody String userJson) throws Exception {
        String login = null;
        String password = null;
        try {
            ObjectNode objectNode = objectMapper.readValue(userJson, ObjectNode.class);
            login = objectNode.get("login").textValue();
            password = objectNode.get("password").textValue();
            log.info(String.format("Logging in U:%s P:%s", login, password));
            User user = bankDAO.findUserByUsername(login);

            if (login == null || password == null) {
                throw new Exception("Login/Password cant be empty.");
            }

            if (user.getPassword().equals(password)) {
                response.setHeader("Location", "/client");
                return SessionService.getInstance().createUserSession(user.getId());
            } else {
                throw new Exception("Combination of username/password was not found.");
            }
        } catch (UserNotFoundException e) {
            log.info(String.format("Could not find user with credentials U:%s P:%s", login, password));
            throw e;
        }
    }

    @RequestMapping(value="/logout", method = {POST,GET})
    public String logout(@RequestBody String json) throws IOException {
        String token = objectMapper.readValue(json, Token.class).getToken();
        SessionService.getInstance().endUserSession(token);
        return "redirect:/home";
    }

    @RequestMapping(value="/accounts", method = POST)
    @ResponseBody
    public String accounts(HttpServletRequest request, HttpServletResponse response, @RequestBody String json) {
        String token;
        try {
            token = objectMapper.readValue(json, Token.class).getToken();
            if (token==null){
                throw new IllegalArgumentException("No client id in request");
            }
            if (!loggedIn(token)){
                response.setHeader("Location", "/home");
                response.setStatus(403);
                return "";
//                throw new UnauthorizedException("Not authorized");
            }
            List<Account> accountList = accountDAO.getAccounts(SessionService.getInstance().getUserSession(token).getClientId());
            String result = objectMapper.writeValueAsString(accountList);
            response.setStatus(HttpServletResponse.SC_ACCEPTED);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }
    }

    @RequestMapping(value="/products", method = POST)
    @ResponseBody
    public String products(HttpServletRequest request, HttpServletResponse response, @RequestBody String json) {
        String token;
        try {
//            token = objectMapper.readValue(json, Token.class).getToken();
//            if (token==null){
//                throw new IllegalArgumentException("No client id in request");
//            }
//            if (!loggedIn(token)){
//                response.setHeader("Location", "/home");
//                response.setStatus(403);
//                return "";
////                throw new UnauthorizedException("Not authorized");
//            }
            List<Product> productList = otherDAO.getProducts();
            String result = objectMapper.writeValueAsString(productList);
            response.setStatus(HttpServletResponse.SC_ACCEPTED);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return null;
        }
    }

    @RequestMapping(value = "/transfer", method = POST)
    @ResponseBody
    public String transfer(HttpServletRequest request, HttpServletResponse response, @RequestBody String userJson) throws Exception {
        String senderAccount = null;
        String receiverAccount = null;
        String token = null;
        Double amount = null;
        try {
            ObjectNode objectNode = objectMapper.readValue(userJson, ObjectNode.class);
            senderAccount = objectNode.get("senderAccount").textValue();
            receiverAccount = objectNode.get("receiverAccount").textValue();
            token = objectNode.get("token").textValue();
            amount = objectNode.get("amount").doubleValue();
            log.info(String.format("Transfering %s from %s to %s", amount, senderAccount, receiverAccount));
            accountDAO.transfer(senderAccount, receiverAccount, amount);

            if (login == null || password == null) {
                throw new Exception("Login/Password cant be empty.");
            }

            if (user.getPassword().equals(password)) {
                response.setHeader("Location", "/client");
                return SessionService.getInstance().createUserSession(user.getId());
            } else {
                throw new Exception("Combination of username/password was not found.");
            }
        } catch (UserNotFoundException e) {
            log.info(String.format("Could not find user with credentials U:%s P:%s", login, password));
            throw e;
        }
    }

    public boolean loggedIn(String token){
        return SessionService.getInstance().contains(token);
    }
}