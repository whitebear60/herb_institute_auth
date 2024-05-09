package ua.edu.udhtu.whitebear60.herbinstituteauth.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ua.edu.udhtu.whitebear60.herbinstituteauth.config.JwtTokenProvider;
import ua.edu.udhtu.whitebear60.herbinstituteauth.error.RegistrationException;
import ua.edu.udhtu.whitebear60.herbinstituteauth.model.AccountEntity;
import ua.edu.udhtu.whitebear60.herbinstituteauth.repository.AccountRepository;

import javax.security.auth.login.LoginException;
import java.text.MessageFormat;
import java.util.*;

@RestController
@RequestMapping

public class AuthRestController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthRestController.class);

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private AccountRepository accountRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

    @PostMapping("auth")
    public String auth(@RequestHeader(value = "Authorization", defaultValue = "") String credentials) throws LoginException {
        if (isBasicAuth(credentials)) {
            Map<String, String> authData = parseBasicCredentials(credentials);
            String login = authData.get("login");
            String password = authData.get("password");

            AccountEntity account = accountRepository.findByLogin(login);

            if (account == null) {
                String errorMsg = MessageFormat.format("Login Error - Could not find the account with the username {0}", login);
                LOGGER.warn(errorMsg);
                throw new LoginException(errorMsg);
            }

            if (!passwordEncoder.matches(password, account.getHashPass())) {
                String errorMsg = "Login Error - The password is incorrect";
                LOGGER.warn(errorMsg);
                throw new LoginException(errorMsg);
            }

            String token = jwtTokenProvider.createToken(account);

            LOGGER.info(MessageFormat.format("Login Successful: [login={0} | token={1}]", login, token));
            return token;
        } else {
            throw new LoginException("Invalid authentication method, please use Basic Authorization header");
        }
    }

    @PostMapping("register")
    public void register(
            @RequestHeader(value = "Authorization", defaultValue = "") String credentials
    ) throws Exception {
        if (isBasicAuth(credentials)) {
            Map<String, String> authData = parseBasicCredentials(credentials);
            if (authData.get("login").isEmpty()) {
                LOGGER.warn("Cannot register - login is empty");
                throw new RegistrationException("Cannot register - login is empty");
            }

            if (authData.get("password").length() < 8) {
                LOGGER.warn("Password can't be shorter than 8 characters");
                throw new RegistrationException("Password can't be shorter than 8 characters");
            }

            if (accountRepository.findByLogin(authData.get("login")) != null) {
                String errorMsg = MessageFormat.format("The account with the login {0} already exists", authData.get("login"));
                LOGGER.warn(errorMsg);
                throw new RegistrationException(errorMsg);
            }

            AccountEntity newAccount = new AccountEntity();
            newAccount.setLogin(authData.get("login"));
            newAccount.setHashPass(passwordEncoder.encode(authData.get("password")));
            accountRepository.save(newAccount);
        }

    }

    private boolean isBasicAuth(String authHeader) {
        List<String> auth = new ArrayList<>();
        Collections.addAll(auth, authHeader.split(" "));

        return auth.getFirst().equals("Basic");
    }

    private Map<String, String> parseBasicCredentials(String authHeader) {
        List<String> auth = new ArrayList<>();
        Collections.addAll(auth, authHeader.split(" "));

        auth.add(new String(Base64.getDecoder().decode(auth.getLast())));
        final String[] fields = {"login", "password"};
        final String[] credentialsArr = auth.getLast().split(":").clone();

        final Map<String, String> credentials = new HashMap<>();
        for (int i = 0; i < credentialsArr.length; i++) {
            credentials.put(fields[i], credentialsArr[i]);
        }

        return credentials;

    }
}