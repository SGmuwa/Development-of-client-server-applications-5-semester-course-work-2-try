package ru.mirea.BalanceService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;

@Component
public class BalanceDBService {

    private JdbcTemplate jdbcTemplate;

    private RowMapper<User> userRowMapper = (ResultSet rs, int row) -> new User(
            rs.getLong("user_id"),
            new Money(rs.getLong("penny"), rs.getString("currency_name"))
    );

    @Autowired
    BalanceDBService(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    void init(){
        // init db инициализация базы данных
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS BalanceService(" +
                "user_id BIGINT NOT NULL," +
                "currency_name CHAR(32) NOT NULL," +
                "penny BIGINT NOT NULL, " +
                "PRIMARY KEY(user_id, currency_name))");

    }
}
