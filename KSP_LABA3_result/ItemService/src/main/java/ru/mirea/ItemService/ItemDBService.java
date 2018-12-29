package ru.mirea.ItemService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class ItemDBService {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    ItemDBService(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    public void init(){
        // init db инициализация базы данных
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS Item(id int NOT NULL PRIMARY KEY ,name VARCHAR , type VARCHAR,count int, price DOUBLE )");

        //в базе данных цена в долларах
        //наполняем базу тоже тут
        jdbcTemplate.execute("INSERT INTO Item( id, name,type,count,price) VALUES (1, 'dog', 'pet',3, 15), ( 2,'cat', 'pet',3,13),( 3,'cat food', 'stuff',4,10),( 4,'dog food', 'stuff',3,10)");


    }
}
