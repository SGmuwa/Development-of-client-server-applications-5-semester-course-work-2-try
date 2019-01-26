package ru.mirea.BalanceService;

/**
 * Данный класс содержит описание о валюте кошелька.
 * @author <a href="github.com/SGmuwa/">[SG]Muwa</a>
 */
public class CurrencyConvert {
    /**
     * Название валюты изначальной.
     */
    private String from;
    /**
     * Название валюты новой.
     */
    private String to;
    /**
     * Сколко 0,000001 валют {@link #from} надо для покупки валюты {@link #to}?
     */
    private long costPennyPennyPenny;

    /**
     * Создание экземпляра валюты.
     * @param from Название изначальной валюты.
     * @param to Название новой валюты.
     * @param costPennyPennyPenny Сколко 0,000001 валют {@link #from} надо для покупки валюты {@link #to}?
     */
    public CurrencyConvert(String from, String to, long costPennyPennyPenny) {
        this.from = from;
        this.to = to;
        this.costPennyPennyPenny = costPennyPennyPenny;
    }

    /**
     * Получение названия изначальной валюты.
     */
    public String getFrom() {
        return from;
    }

    /**
     * Получение название конечной валюты.
     */
    public String getTo() {
        return to;
    }

    /**
     * Конвектирует изначальную сумму денег в новую в новой валюте.
     * @param oldPrice Старая ценая за что-либо.
     * @return
     */
    public long convert(long oldPrice) {

    }
}


/*
100 рублей -> 1 $
1 $ -> 50 рублей.
50 рублей -> 0.5$





 */



