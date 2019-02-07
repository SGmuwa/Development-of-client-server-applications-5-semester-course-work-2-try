package ru.mirea.BalanceService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.Collection;

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
    @PostMapping
    public ResponseEntity update(@RequestBody Collection<CurrencyConvert> currencyConvert) {
        cs.addConvert(currencyConvert);
        return ResponseEntity.ok().build();
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
    @DeleteMapping
    public ResponseEntity clear() {
        cs.clear();
        return ResponseEntity.ok().build();
    }
}
