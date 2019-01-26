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
                // Пример верных данных (26.12.2019):
                // "ruble" "usd" 65 986000 - пробел поставил для наглядности
                "CREATE TABLE IF NOT EXISTS currencyService (" +
                        "currencyNameFrom CHAR(32) NOT NULL," +                 // Имя валюты, откуда надо сделать перевод.
                        "currencyNameTo CHAR(32) NOT NULL," +                   // Имя валюты, куда надо сделать перевод.
                        // Сколько стоит изначальной валюты в единицах 0,000001 одна итоговая целая единица новой валюты?
                        "costPennyPennyPenny BIGINT NOT NULL DEFAULT 0," +
                        "PRIMARY KEY (currencyNameFrom, currencyNameTo)" +
                        ")"
        );
    }

    /**
     * Функция, которая расчитывает стоимость покупки новой валюты из старой.
     * @param from Сколько человек готов заплатить за новую валюту?
     * @param target Название новой валюты.
     * @return Деньги в новой валюте.
     * @throws Exception Обмен валют невозможен.
     */
    Money howMuchYouGetNewCurrencyFromOldCurrency(Money from, String target) throws Exception {
        CurrencyConvert convert = getCurrency(from.getCurrency(), target);
        return new Money(convert.convert(target.getCountPenny()), from);
    }

    /**
     * Функция, которая расчитывает сколько человеку нужно заплатить
     * старой валюты, чтобы получить заданную сумму в новой валюте.
     * @param from Название старой валюты.
     * @param target То, сколько клиенту нужно новых денег.
     * @return Количество, сколько необходимо старых денег.
     * @throws Exception Обмен валют невозможен.
     */
    Money howMuchYouNeedOldCurrencyForBuyCurrentNewCurrency(String from, Money target) throws Exception {
        CurrencyConvert convert = getCurrency(from, target.getCurrency());
        return new Money(convert.convert(target.getCountPenny()), from);
    }

    /**
     * Получает конвектор валют.
     * @param from Название валюты, кторая у нас есть.
     * @param target Название валюты, которую хотим купить.
     * @return Возвращает готовый переводчик валют. Если не готов, throw.
     * @throws Exception В базе данных нет информации по поводу перевода данных валют,
     * либо присутствуют анамалии.
     */
    private CurrencyConvert getCurrency(String from, String target) throws Exception {
        List<CurrencyConvert> list = jdbcTemplate.query("SELECT costPennyPennyPenny FROM currencyService WHERE " +
                "currencyNameFrom='?1' AND currencyNameTo='?2'", currencyConvertMapper, from, target);
        if(list.size() != 1) {
            if(list.size() == 0)
                log.warn(String.format("Can't find currencyConvert: %1s %2s", from, target));
            else// if(list.size() > 1)
                log.error(String.format("To much for currencyConvert! (%d) : %s %s", list.size(), from, target));
            throw new Exception("you can't buy currency: from " + from + " target " + target); // Нельзя продавать валюту когда нет цены или
        }
        CurrencyConvert convert = list.get(0);
        if(convert.isReady())
            return convert;
        else
            throw new Exception("you can't buy currency: from " + from + " target " + target);
    }

    double changeValue_toUSD(double cur, String name){
        for(CurrencyConvert it : currencies)
            if (it.getFrom().equals(name))
                return (cur / it.getMultiplication());
        return -1;
    }

}