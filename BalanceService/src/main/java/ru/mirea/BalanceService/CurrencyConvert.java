package ru.mirea.BalanceService;

import ru.mirea.Money.Price;

/**
 * Данный класс содержит описание о валюте кошелька.
 * @author <a href="github.com/SGmuwa/">[SG]Muwa</a>
 */
class CurrencyConvert {
    /**
     * Название новой валюты.
     * Валюта, которую можем купить.
     */
    private final String to;
    /**
     * Сколько стоит валюта {@link #to}?
     */
    private final Price price;

    public CurrencyConvert() {
        to = "";
        price = new Price();
    }

    /**
     * Создание экземпляра валюты.
     * @param from Название изначальной валюты.
     * @param to Название новой валюты.
     * @param price Сколко 0,000001 валют from надо для покупки валюты to?
     */
    public CurrencyConvert(String from, String to, long price) {
        if(from == null || to == null || from.isEmpty() || to.isEmpty())
            throw new NullPointerException("from and to must be not null and must be not empty.");
        this.to = to;
        this.price = new Price(price, from);
    }

    /**
     * Создание экземпляра валюты.
     * @param to Название новой валюты.
     * @param price Стоимость новой валюты.
     */
    public CurrencyConvert(String to, Price price) {
        if(to == null || to.isEmpty())
            throw new NullPointerException("from and to must be not null and must be not empty.");
        this.to = to;
        this.price = price;
    }

    /**
     * Получение название конечной валюты.
     */
    public String getTo() {
        return to;
    }

    /**
     * Стоимость новой валюты.
     */
    public Price getPrice() {
        return price;
    }

    /**
     * @throws RuntimeException Вызывается ошибка в случае, если данной стоимостью нельзя пользоваться.
     * Поддерживается только положительная стоимость и известная валюта.
     * @see Price#Price(long, String) Конструктор безопасной цены.
     */
    public void ready() throws RuntimeException {
        price.ready();
    }
}

