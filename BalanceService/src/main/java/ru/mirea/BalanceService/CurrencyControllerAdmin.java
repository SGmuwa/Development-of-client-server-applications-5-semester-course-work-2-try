package ru.mirea.BalanceService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@RequestMapping(value = "admin/currency")
public class CurrencyControllerAdmin {

    private CurrencyService cs;

    @Autowired
    public CurrencyControllerAdmin(CurrencyService cs) {
        this.cs = cs;
    }

    /**
     * Функция изменяет курсы валют. Если цена курса не существовала ранее, то курс будет добавлен.
     * @param currencyConvert Новые курсы.
     */
    @RequestMapping(value = "/all", method = POST)
    public ResponseEntity update(@RequestBody ListCurrencyConvert currencyConvert) {
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

    @RequestMapping(value = "/example")
    public ResponseEntity<ListCurrencyConvert> example() {
        return ResponseEntity.ok(new ListCurrencyConvert(
                Arrays.asList(
                        new CurrencyConvert("rub", "usd", 70235234), // 70 рублей
                        new CurrencyConvert("usd", "rub", 21341) // Два цента
                )
        ));
    }

    /**
     * Очистка сервиса валют.
     */
    @RequestMapping(method = DELETE)
    public ResponseEntity clear() {
        cs.clear();
        return ResponseEntity.ok().build();
    }

    static class ListCurrencyConvert extends ArrayList<CurrencyConvert> {

        ListCurrencyConvert(Collection<CurrencyConvert> collection) {
            super(collection);
        }
    }
}
