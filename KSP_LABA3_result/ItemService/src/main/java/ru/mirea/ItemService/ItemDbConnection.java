package ru.mirea.ItemService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.List;

@Component
public final class ItemDbConnection {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ItemDbConnection(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Item> getAll() {
        return jdbcTemplate.query("select * from Item",(ResultSet resultSet, int rowNum) -> {
            return new Item(resultSet.getInt("id"), resultSet.getString("name"), resultSet.getString("type"),resultSet.getInt("count"),resultSet.getDouble("price") );
        });
    }
    public Item getItems (int id) {
        Item item = null;
        try {
            item = jdbcTemplate.queryForObject("select * from Item where id = ?", new Object[]{id}, (ResultSet resultSet, int rowNum) -> {
                return new Item(resultSet.getInt("id"), resultSet.getString("name"), resultSet.getString("type"),resultSet.getInt("count"),resultSet.getDouble("price") );
            });
        } catch (DataAccessException dataAccessException) {
        }
        return item;
    }

    public List<Item> getPets_orStuffs(String pet) {
         return  jdbcTemplate.query("select * from Item where type = ?", new Object[]{pet}, (ResultSet resultSet, int rowNum) -> {
             return new Item(resultSet.getInt("id"), resultSet.getString("name"), resultSet.getString("type"),resultSet.getInt("count"),resultSet.getDouble("price") );
         });

    }
    public Item getPet_orStuff (String pet, int id) {
        Item item = null;
        try {
           item =  jdbcTemplate.queryForObject("select * from Item where type = ?1 AND id = ?2  ", new Object[]{pet,id}, (ResultSet resultSet, int rowNum) -> {
               return new Item(resultSet.getInt("id"), resultSet.getString("name"), resultSet.getString("type"),resultSet.getInt("count"),resultSet.getDouble("price") );
           });
        } catch (DataAccessException dataAccessException) {
        }
        return item;
    }

    public String updateCountForItem(int id){
        try {
            jdbcTemplate.update("UPDATE Item SET count = (count - ?) WHERE id = ? ", new Object[]{1, id});
        }catch (DataAccessException dataAccessException) {
            return "Error";
        }
        return "Ok";
    }


    public Integer get_count(int id) {
        try {
            return jdbcTemplate.queryForObject("Select count FROM Item WHERE id = ?", Integer.class, id);
        }catch(DataAccessException dataAccessException) {
            return -0;
        }
    }
}