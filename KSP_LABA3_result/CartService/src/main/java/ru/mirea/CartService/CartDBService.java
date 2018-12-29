
package ru.mirea.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class CartDBService {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    CartDBService(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    void init(){
        // init db инициализация базы данных
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS Cart(id int PRIMARY KEY ,user_id int ,item_id int , type VARCHAR ,price double )");

    }
}
