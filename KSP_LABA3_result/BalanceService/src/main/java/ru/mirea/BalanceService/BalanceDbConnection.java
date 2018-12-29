package ru.mirea.BalanceService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;

@Component
public class BalanceDbConnection {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public BalanceDbConnection(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    //получить баланс//Связь с валютой
    public Balance getBalance(int user_id) {
        double tmp=0;
        CurrencyService cs = new CurrencyService();
         Balance tempBal =  jdbcTemplate.queryForObject("SELECT * FROM Balance WHERE user_id = ?", new Object[]{user_id}, (ResultSet rs, int rowNum) ->{
                return new Balance (rs.getInt("user_id"), rs.getDouble("balance"), rs.getString("currency_name"));
            }
        );
         tmp = cs.getCurrency(tempBal.getBalance(), tempBal.getCurrency_name());
         tempBal.setBalance(tmp);
         return tempBal;
    }


    //Увелечение баланса//связь с Валютой
    public String updateBalance(int user_id, double bal){
        try {
            Balance tempBal = getBalance(user_id);
            CurrencyService cs = new CurrencyService();
            double tempBasisVal = cs.changeValue_toUSD(bal,tempBal.getCurrency_name());
            System.out.println(tempBasisVal+"  tempBasisVal");
            jdbcTemplate.update("UPDATE Balance SET balance = (balance +?) WHERE user_id = ? ", new Object[]{tempBasisVal, user_id});

        }catch(DataAccessException dataAccessException) {
            return "ERROR";
        }
        return "Your balance has been recharged";
    }

    //Изменение валюты
    public String changeCurrency(int user_id, String change_currency) {
        jdbcTemplate.update("UPDATE Balance SET currency_name = ? WHERE user_id = ? ", new Object[]{change_currency, user_id});
        return  "Currency successfully changed";
    }


    //Запретить доступ пользователя
    //Уменьшение баланса после покупки
    public String updateBalance2(int id, double balance) {
        jdbcTemplate.update("UPDATE Balance SET balance = ? WHERE user_id = ? ", new Object[]{balance,id});
        return "ОК";
    }

    //Посмотреть валюту пользователя
    public String getCurrency(int user_id) {
        return jdbcTemplate.queryForObject("Select currency_name FROM Balance WHERE user_id = ? ", String.class, user_id);
    }
}