package ru.mirea.BalanceService;

/**
 * Данный класс содержит описание о валюте кошелька.
 * @author <a href="github.com/SGmuwa/">[SG]Muwa</a>
 */
class CurrencyConvert {
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
    CurrencyConvert(String from, String to, long costPennyPennyPenny) {
        if(from == null || to == null || from.isEmpty() || to.isEmpty())
            throw new NullPointerException("from and to must be not null and must be not empty.");
        this.from = from;
        this.to = to;
        this.costPennyPennyPenny = costPennyPennyPenny;
    }

    /**
     * Получение названия изначальной валюты.
     */
    String getFrom() {
        return from;
    }

    /**
     * Получение название конечной валюты.
     */
    String getTo() {
        return to;
    }

    /**
     * Отвечает на вопрос, можно ли конвектировать валюту.
     * @return {@code True} - можно. Иначе - {@code false}.
     */
    boolean isReady() {
        return costPennyPennyPenny > 0;
    }

    /**
     * Конвектирует изначальную сумму денег в новую в новой валюте.
     * Вы называете, сколько Вам нужно неделимых единиц новой валюты (копейки, центы), а функция
     * возвращает вам цену в старой валюте.
     * @param needNew Сколько покупатель хочет купить новой валюты.
     * @return Новая валюта в неделимых единицах (центах или копейках и так далее).
     */
    long convert(long needNew) {
        /*
        example.
        from = rub
        to = dollar
        mul = 70 000000
        need 2 cent?
        2 * 70 000000 = 140 000000
        140 000000 / 10000 = 140 00 копеек. Округлить надо в большую сторону.
         */
        return Math.floorDiv(Math.multiplyExact(costPennyPennyPenny, needNew), 10000);
    }

    /**
     * Конвектирует новые деньги в старую валюту.
     * Вы называете, сколько нужно старой валюты
     * @param needOld
     * @return
     */
    long convertUndo(long needOld) {

    }
}

