package ru.mirea.BalanceService;

import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * Данный сервис предназначет для работы с разными валютами.
 *
 */
@Component
public class CurrencyService {

    private Log log = LogFactory.getLog(CurrencyService.class);

    JdbcTemplate jdbcTemplate;

    public CurrencyService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcTemplate.execute(
                "CREATE TABLE IF NOT EXISTS currencyService (" +
                "\n" +
                "  id int(11) NOT NULL auto_increment," +
                "  currencyName INTEGER(64) NOT NULL," +
                "  costPenny INTEGER(250)  NOT NULL default ''," +
                "   PRIMARY KEY  ('id')\n" +
                ");"
        );
    }

    double getCurrency(double cur, String name){
        if(cur == 0) return 0;
        for(Currency it : currencies)
            if (it.getType().equals(name))
                return cur * it.getMultiplication();
        return -1;
    }

    double changeValue_toUSD(double cur, String name){
        for(Currency it : currencies)
            if (it.getType().equals(name))
                return (cur / it.getMultiplication());
        return -1;
    }

}