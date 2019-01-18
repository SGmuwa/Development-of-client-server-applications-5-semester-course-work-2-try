package ru.mirea.Identity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mirea.Tokens.PayloadToken;
import ru.mirea.Tokens.TokenFactory;

import java.util.Collections;

@Service
public class IdentityService {

    private DBConnection dbConnection;

    @Autowired
    public IdentityService(DBConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    public String getToken(String login, String password) throws Exception {
        User user = dbConnection.getUser(login);
        if (user == null) {
            throw new Exception("Пользователь не найден");
        }
        if (!user.getPassword().equals(password)) {
            throw new Exception("Неверный пароль");
        }
        return TokenFactory.generateToken(new PayloadToken(user.getId(), user.getLogin(), Collections.singletonList(user.getRole())));
    }

    public String addUser(String login, String password) throws Exception {
        User user = dbConnection.getUser(login);
        if (user == null) {
            dbConnection.addUser(login, password, "user");
            return getToken(login, password);
        }
        else return null;
    }
}

