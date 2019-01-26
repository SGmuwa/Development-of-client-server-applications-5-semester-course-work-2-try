package ru.mirea.BalanceService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.Collection;
import java.util.Optional;

@Component
public class BalanceDbConnection {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public BalanceDbConnection(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Получение баланса по идентификатору пользователя.
     * Запрос идёт базе данных.
     * @param user_id Идентификатор пользователя.
     * @return Баланс заданного пользователя. Not null.
     */
    public Collection<Money> getBalance(Integer user_id) {
        double tmp;
        CurrencyService cs = new CurrencyService();
        Balance tempBal = jdbcTemplate.queryForObject("SELECT * FROM Balance WHERE user_id = ?",
                (ResultSet rs, int rowNum) ->
                        new Balance(rs.getInt("user_id"), rs.getLong("balance"), rs.getString("currency_name")),
                user_id
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