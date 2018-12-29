package ru.mirea.Identity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class UserDBService {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    UserDBService(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    void init(){
        // init db инициализация базы данных пользователей
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS User(id int PRIMARY KEY AUTO_INCREMENT,login VARCHAR(50) not null ,password VARCHAR(50) not null , role VARCHAR(50) not null )");
        jdbcTemplate.execute("INSERT INTO User (id, login, password, role) VALUES (1, 'pupysheva','ytrewq','user'),(2, 'admin', 'okmijn','admin')");

    }
}