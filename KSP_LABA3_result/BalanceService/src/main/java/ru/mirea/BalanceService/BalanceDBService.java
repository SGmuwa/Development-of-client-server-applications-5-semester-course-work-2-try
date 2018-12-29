package ru.mirea.BalanceService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class BalanceDBService {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    BalanceDBService(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    void init(){
        // init db инициализация базы данных
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS Balance(user_id int PRIMARY KEY ,balance DOUBLE , currency_name VARCHAR )");
        jdbcTemplate.execute("INSERT INTO Balance (user_id, balance, currency_name) VALUES (1, 0, 'Rubles'),(2, 0, 'Rubles'),(3, 0, 'Euro')");

    }
}
