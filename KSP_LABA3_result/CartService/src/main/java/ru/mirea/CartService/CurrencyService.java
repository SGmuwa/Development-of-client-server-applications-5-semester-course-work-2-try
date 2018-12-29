package ru.mirea.CartService;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
public class CurrencyService {

    private List<Currency> currencies;

    public CurrencyService() {
        this.currencies = new LinkedList<Currency>();
        this.currencies.add(new Currency(70, "Rubles"));
        this.currencies.add(new Currency(0.89, "Euro"));
        this.currencies.add(new Currency(1, "USD"));
    }

    public double getCurrency(double cur,String name){
        if(cur == 0) return 0;
        for(Currency it : currencies)
            if (it.getType().equals(name))
                return cur * it.getMultiplicator();
        return -1;
    }

    public double changeValue_toUSD(double cur,String name){
        for(Currency it : currencies)
            if (it.getType().equals(name))
                return (cur / it.getMultiplicator());
        return -1;
    }

}