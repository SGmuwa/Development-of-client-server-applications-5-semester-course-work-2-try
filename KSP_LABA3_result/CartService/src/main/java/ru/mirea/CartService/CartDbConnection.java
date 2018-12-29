package ru.mirea.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.List;

@Component
public class CartDbConnection {
    volatile int id_cart = 0;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CartDbConnection(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    //Получить корзину
    public List<Cart> getCart(int user_id){
            return jdbcTemplate.query("select * from Cart where user_id = ?", new Object[]{user_id},(ResultSet resultSet, int rowNum) -> {
            return new Cart(resultSet.getInt("id"), resultSet.getInt("user_id"), resultSet.getInt("item_id"),resultSet.getString("type"),resultSet.getDouble("price") );
        });
    }

    //Удалить корзину
    public String deleteCart(int user_id) {
        try {
            jdbcTemplate.update("DELETE FROM Cart WHERE user_id = ?", user_id);
        }catch(DataAccessException dataAccessException){
            return "ERROR";
        }
        return "Item hes been removed";
    }

    //Удалить запись в таблице
    public String deleteOneItem(int user_id,int id) {
        try {
            jdbcTemplate.update("DELETE FROM Cart WHERE user_id = ? AND  id=?", user_id,id);
        }catch(DataAccessException dataAccessException){
            return "ERROR";
        }
        return "Item hes been removed";
    }

    //Положить Item в корзину//Связь с бд Item
    public String putItem_inCart(String type, int user_id, int id, double price) {
        String tempString = "Item added successfully";
        String tempStringNOT = "Item not added";
        try {
            System.out.println(price+" price in Connection");
            jdbcTemplate.update("INSERT INTO Cart (id,user_id,item_id,type,price) values(?,?,?,?,?)", new Object[]{id_cart++, user_id, id, type, price});
            return tempString;
        }catch (DataAccessException dataAccessException) {
                return tempStringNOT;
        }
    }



    //--------------------------------------------ПЕРЕДЕЛАТЬ-----------------------------------------------------------------------------
    //Оплатить корзину//Нужна связь с бд баланс и итем (списать средства)
    public String payCart(int user_id) {
        double result  =  jdbcTemplate.queryForObject("SELECT bal.balance - SUM(i.price) " +
                "FROM cart ca " +
                "INNER JOIN Item i ON i.id = ca.item_id AND ca.user_id = ? LEFT JOIN Balance bal ON bal.user_id = ca.user_id GROUP BY ca.user_id",
                 Double.class ,new Object[]{user_id});
        if (result >= 0) {
            deleteCart(user_id);
            //jdbcTemplate.update("UPDATE Item SET count = (count - ?) WHERE id = ? ", new Object[]{1, id});
            /////updateBalance2(user_id, result);
            return "Cart has been successfully paid";
        }
        else return "You cannot afford this purchase";
    }

    public double getCartCost(int user_id) {
        try {
            return jdbcTemplate.queryForObject("Select SUM (price) FROM Cart WHERE user_id = ? GROUP bY user_id", Double.class, user_id);
        }catch(DataAccessException dataAccessException) {
            return -0;
        }
    }
}