package ru.mirea.BalanceService;

import java.util.ArrayList;
import java.util.List;

/**
 * Описывает состояние кошелька, который может хранить в себе только одну валюту.
 * @author <a href="https://github.com/SGmuwa">[SG]Muwa<a/>
 */
public class Money {

    /**
     * Создание нового состояние одновалютного кошелька.
     * @param countPenny Количество <a href=https://ru.wikipedia.org/wiki/%D0%A1%D1%83%D1%89%D0%B5%D1%81%D1%82%D0%B2%D1%83%D1%8E%D1%89%D0%B8%D0%B5_%D1%80%D0%B0%D0%B7%D0%BC%D0%B5%D0%BD%D0%BD%D1%8B%D0%B5_%D0%B4%D0%B5%D0%BD%D0%B5%D0%B6%D0%BD%D1%8B%D0%B5_%D0%B5%D0%B4%D0%B8%D0%BD%D0%B8%D1%86%D1%8B>
     *                   разменных денежных единиц</a> в кошельке.
     * @param currency Валюта денежных единиц кошелька..
     */
    public Money(long countPenny, Currency currency) {
        if(currency == null)
            throw new NullPointerException("currency");
        this.countPenny = countPenny;
        this.currency = currency;
    }

    /**
     * Количество <a href=https://ru.wikipedia.org/wiki/%D0%A1%D1%83%D1%89%D0%B5%D1%81%D1%82%D0%B2%D1%83%D1%8E%D1%89%D0%B8%D0%B5_%D1%80%D0%B0%D0%B7%D0%BC%D0%B5%D0%BD%D0%BD%D1%8B%D0%B5_%D0%B4%D0%B5%D0%BD%D0%B5%D0%B6%D0%BD%D1%8B%D0%B5_%D0%B5%D0%B4%D0%B8%D0%BD%D0%B8%D1%86%D1%8B>
     *     разменных денежных единиц</a> в шокельке.
     * Это могут быть копейки, центы и другие.
     */
    private final long countPenny;

    /**
     * Валюта, в которой содержится баланс.
     */
    private final Currency currency;

    /**
     * Получение текущего кошелька в эквиваленте кошелька с требуемой валютой.
     * @param target Требуемая валюта.
     * @return Новый кошелёк с заданной валютой.
     * @throws ArithmeticException Конвертация не поддерживается (long overflow). Возможно, сумма слишком большая.
     * @throws NullPointerException Поле {@code target} пустое.
     */
    Money convert(Currency target) {
        if (target == null)
            throw new NullPointerException(this.toString());
        return new Money(
                Math.multiplyExact(
                        Math.multiplyExact(
                                countPenny, currency.howCostCurrencyByOneBase()
                        ) /* Получили в единицах основной валюты */,
                        target.howCostBaseByOneCurrency()) /* Получили в единицах новой валюты */,
                target
        );
    }

    /**
     * @seenizer countPenny
     */
    public long getCountPenny() {
        return countPenny;
    }

    /**
     * @seenizer currency
     */
    public Currency getCurrency() {
        return currency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Money money = (Money) o;

        if (countPenny != money.countPenny) return false;
        return currency.equals(money.currency);
    }

    @Override
    public int hashCode() {
        int result = (int) (countPenny ^ (countPenny >>> 32));
        result = 31 * result + currency.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Money{" +
                "countPenny=" + countPenny +
                ", currency=" + currency +
                '}';
    }

    Money plus(long value) {
        return new Money(Math.addExact(countPenny, value), currency);
    }

    Money plus(Money value) {
        if(!this.currency.equals(value.currency))
            value = value.convert(this.currency);
        return plus(value.countPenny);
    }

    Money minus(long value) {
        return new Money(Math.subtractExact(countPenny, value), currency);
    }

    Money minus(Money value) {
        if(!this.currency.equals(value.currency))
            value = value.convert(this.currency);
        return minus(value.countPenny);
    }

    /**
     * Делит кошелёк на несколько кошельков.
     * Если есть неделимые копейки, они раздаются по-одной первым персонам.
     * @param count Количество персон, кому надо разделить деньги.
     * @return Список сумм, сколько им положено.
     * @throws RuntimeException {@code count} должен быть положительным числом.
     */
    ArrayList<Money> div(short count) {
        if(count < 1)
            throw new RuntimeException("count must be positive");
        ArrayList<Money> out = new ArrayList<>(count);
        // Столько получит каждый человек.
        long pennyForEvery = countPenny/count;
        // Столько человек получит дополнительно по одной копейке.
        long countOfPeopleWhoGetPlusOnePenny;
    }
}
