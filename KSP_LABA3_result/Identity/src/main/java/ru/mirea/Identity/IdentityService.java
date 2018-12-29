package ru.mirea.Identity;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IdentityService {

    private DBConnection dbConnection;
    private String secret_key;

    @Autowired
    public IdentityService(DBConnection dbConnection) {
        this.dbConnection = dbConnection;
        this.secret_key = "sdkfda";
    }

    public Token getToken(String login, String password) throws Exception {
        User user = dbConnection.getUser(login);
        if (user == null) {
            throw new Exception("Пользователь не найден");
        }
        if (!user.getPassword().equals(password)) {
            throw new Exception("Неверный пароль");
        }
        String signature = DigestUtils.sha256Hex(Integer.toString(user.getId())+user.getLogin()+user.getRole()+secret_key);
        return new Token(user.getId(),user.getLogin(), user.getRole(), signature);

    }

    public Token addUser(String login, String password) throws Exception {
        User user = dbConnection.getUser(login);
        if (user == null) {
            dbConnection.addUser(login, password, "user");
            return getToken(login, password);
        }
        else return null;
    }
}

