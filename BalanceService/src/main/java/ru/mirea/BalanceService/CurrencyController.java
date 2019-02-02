package ru.mirea.BalanceService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.Collection;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@RequestMapping(value = "admin/currency")
public class CurrencyController {

    private CurrencyService cs;

    @Autowired
    public CurrencyController(CurrencyService cs) {
        this.cs = cs;
    }

    /**
     * Обновление курса валют. Если его не существует, то будет добавлен.
     * @param currencyConvert Новые курсы.
     */
    @RequestMapping(method = POST)
    public ResponseEntity update(@RequestBody Collection<CurrencyConvert> currencyConvert) {
        cs.addConvert(currencyConvert);
        return ResponseEntity.ok().build();
    }

    /**
     * Обновление курса валюты. Если его не существует, то будет добавлен.
     * @param currencyConvert Новый курс.
     */
    @RequestMapping(method = POST)
    public ResponseEntity update(@RequestBody CurrencyConvert currencyConvert) {
        cs.addConvert(currencyConvert);
        return ResponseEntity.ok().build();
    }

    /**
     * Получает все записи из БД по ценам валют.
     */
    @RequestMapping(method = GET)
    public ResponseEntity<Collection<CurrencyConvert>> show() {
        return ResponseEntity.ok(
                cs.getAll()
        );
    }

    @RequestMapping(value = "/example")
    public ResponseEntity<Collection<CurrencyConvert>> example() {
        return ResponseEntity.ok(
                Arrays.asList(
                        new CurrencyConvert("rub", "usd", 70235234), // 70 рублей
                        new CurrencyConvert("usd", "rub", 21341) // Два цента
                )
        );
    }

    /**
     * Очистка сервиса валют.
     */
    @RequestMapping(method = DELETE)
    public ResponseEntity clear() {
        cs.clear();
        return ResponseEntity.ok().build();
    }
}
