package ru.mirea.Identity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;

@Component
public class DBConnection {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DBConnection(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public User getUser(String login) {
        Boolean cheack =  jdbcTemplate.queryForObject("Select COUNT(id) FROM USER where login = ?", new Object[]{login}, Integer.class)==1;
        if(cheack)return jdbcTemplate.queryForObject("select * from User where login = ?", new Object[]{login}, (ResultSet resultSet, int rowNum) -> {
            return new User(resultSet.getInt("id"), resultSet.getString("login"), resultSet.getString("password"),resultSet.getString("role") );
        });
        else return null;
    }

    public void addUser(String login, String password, String role) {
        jdbcTemplate.update("insert into User (login, password, role) values(?,?,?)", login, password, role);
    }
}
