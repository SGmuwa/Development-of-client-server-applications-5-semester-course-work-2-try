package ru.mirea.BalanceService;

import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Component
public class CurrencyService {

    private Log log = LogFactory.getLog(CurrencyService.class);

    JdbcTemplate jdbcTemplate;

    /**
     * Переменная хранит в себе коэффициент валют по отношению к доллару.
     */
    private Map<String, Long> currencies;

    public CurrencyService(Environment environment, JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        jdbc.


        this.currencies = new LinkedList<>();
        String currenciesProperty = environment.getProperty("currencyService.values");
        if(currenciesProperty == null) {
            log.error("Can't find configuration 'currencyService.values'. " +
                    "Please, set properties 'currencyService.values' example: " +
                    "'currencyService.values=ruble euro usd'. I am setting the default.");
            this.currencies.add(new Currency(70, "Rubles"));
            this.currencies.add(new Currency(0.89, "Euro"));
            this.currencies.add(new Currency(1, "USD"));
        }
        else {
            String[] currencies = currenciesProperty.trim().toLowerCase().split(" ");
            for (String currency : currencies) {
                this.currencies.put(currency, environment.getProperty("currencyService.cost." + currency));
            }
        }
    }

    double getCurrency(double cur, String name){
        if(cur == 0) return 0;
        for(Currency it : currencies)
            if (it.getType().equals(name))
                return cur * it.getMultiplicator();
        return -1;
    }

    double changeValue_toUSD(double cur, String name){
        for(Currency it : currencies)
            if (it.getType().equals(name))
                return (cur / it.getMultiplicator());
        return -1;
    }

}