package web;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.thoughtworks.xstream.XStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import web.dao.AccountDAO;
import web.dao.BankDAO;
import web.exception.InsufficientFundsException;
import web.exception.UnauthorizedException;
import web.exception.UserNotFoundException;
import web.exception.UsernameTakenException;
import web.pojo.Account;
import web.pojo.TransactionRequest;
import web.pojo.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

    @SuppressWarnings("unused")
@Controller
public class RESTController {

    private final BankDAO bankDAO;
    private final AccountDAO accountDAO;

    private final ObjectMapper objectMapper;
//    private final SessionService sessionService;

    private static final Logger log = Logger.getLogger(RESTController.class.getName());
    private static final String GENERIC_ERROR = "Error, please try again later";
    private static final String RESOURCE_FOLDER = "resources";

    @Autowired
    public RESTController(BankDAO bankDAO, AccountDAO accountDAO) {
        this.bankDAO = bankDAO;
        this.accountDAO = accountDAO;
        objectMapper = new ObjectMapper();
//        sessionService = new SessionService();
    }

    @GetMapping("/login")
    public /*void*/ String login(HttpServletResponse response) throws IOException {
        return "/login";
    }

    @GetMapping("/")
    public /*void*/String index(HttpServletResponse response) throws IOException {
        return "redirect:/home";
    }

    @GetMapping("/home")
    public /*void*/String home(HttpServletResponse response) throws IOException {
        return "/home";
    }

    @GetMapping("/client")
    public /*void*/String client(HttpServletResponse response) throws IOException {
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
                return SessionService.getInstance().getSessionId(user.getId());
            } else {
                throw new Exception("Combination of username/password was not found.");
            }
        } catch (UserNotFoundException e) {
            log.info(String.format("Could not find user with credentials U:%s P:%s", login, password));
            throw e;
        }
    }

    @RequestMapping(value="/logout", method = {POST,GET})
    public String logout() {
        return "redirect:/home";
    }

    @RequestMapping(value="/accounts", method = POST)
    @ResponseBody
    public String accounts(HttpServletRequest request, HttpServletResponse response, @RequestBody String json) {
        String token;
        try {
            if (!loggedIn(request)){
                throw new UnauthorizedException("Not authorized");
            }

            ObjectNode objectNode = objectMapper.readValue(json, ObjectNode.class);
            token = objectNode.get("token_bank").textValue();
            if (token==null){
                throw new IllegalArgumentException("No client id in request");
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

    public boolean loggedIn(HttpServletRequest request){
//        request.getHeader("");
        return true;
    }
}