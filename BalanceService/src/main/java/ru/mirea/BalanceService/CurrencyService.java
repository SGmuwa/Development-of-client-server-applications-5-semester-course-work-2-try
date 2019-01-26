package ru.mirea.BalanceService;

import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.List;

/**
 * Данный сервис предназначет для работы с разными валютами.
 *
 */
@Component
public class CurrencyService {

    private Log log = LogFactory.getLog(CurrencyService.class);

    JdbcTemplate jdbcTemplate;

    RowMapper<CurrencyConvert> currencyConvertMapper = (ResultSet rs, int rowNum) ->
            new CurrencyConvert(
                    rs.getString("currencyNameFrom"),
                    rs.getString("currencyNameTo"),
                    rs.getLong("costPennyPennyPennyFrom")
            );

    public CurrencyService(JdbcTemplate jdbcTemplate) {
        log.info("init CurrencyService.");
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcTemplate.execute(
                "CREATE TABLE IF NOT EXISTS currencyService (" +
                        "currencyNameFrom CHAR(32) NOT NULL," +                 // Имя валюты, откуда надо сделать перевод.
                        "currencyNameTo CHAR(32) NOT NULL," +                   // Имя валюты, куда надо сделать перевод.
                        "costPennyPennyPennyFrom BIGINT NOT NULL DEFAULT 0," +  // Сколько стоит изначальной валюты одна итоговая валюта в единицах 0,000001?
                        "PRIMARY KEY (currencyNameFrom, currencyNameTo)" +
                        ")"
        );
    }

    Double howCostTargetCurrency(String from, String target) {
        List<CurrencyConvert> list = jdbcTemplate.query("SELECT costPennyPennyPennyFrom FROM currencyService WHERE " +
                "currencyNameFrom='?1' AND currencyNameTo='?2'", currencyConvertMapper, from, target);
        if(list.size() != 1) {
            log.warn(new String("Can't find currencyConfert: {%1, %2{"));
        }
    }

    double changeValue_toUSD(double cur, String name){
        for(CurrencyConvert it : currencies)
            if (it.getFrom().equals(name))
                return (cur / it.getMultiplication());
        return -1;
    }

}