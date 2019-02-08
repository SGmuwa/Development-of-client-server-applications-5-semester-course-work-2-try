package ru.mirea.Money;

import java.util.ArrayList;

/**
 * Описывает состояние кошелька, который может хранить в себе только одну валюту.
 * @author <a href="https://github.com/SGmuwa">[SG]Muwa<a/>
 * @since 1.0
 */
public class Money implements Comparable<Money> {

    /**
     * Данный конструктор необходим для того, чтобы можно было
     * из json кода преобразовать в данный класс java.
     */
    public Money() {
        penny = 0;
        currency = "";
    }

    /**
     * Создание нового <b>состояния</b> одновалютного кошелька.
     * @param penny Количество <a href=https://ru.wikipedia.org/wiki/%D0%A1%D1%83%D1%89%D0%B5%D1%81%D1%82%D0%B2%D1%83%D1%8E%D1%89%D0%B8%D0%B5_%D1%80%D0%B0%D0%B7%D0%BC%D0%B5%D0%BD%D0%BD%D1%8B%D0%B5_%D0%B4%D0%B5%D0%BD%D0%B5%D0%B6%D0%BD%D1%8B%D0%B5_%D0%B5%D0%B4%D0%B8%D0%BD%D0%B8%D1%86%D1%8B>
     *                   разменных денежных единиц</a> в кошельке.
     * @param currency Валюта денежных единиц кошелька.
     */
    public Money(long penny, String currency) {
        if(currency == null)
            throw new NullPointerException("currency");
        this.penny = penny;
        this.currency = currency;
    }

    /**
     * Количество <a href=https://ru.wikipedia.org/wiki/%D0%A1%D1%83%D1%89%D0%B5%D1%81%D1%82%D0%B2%D1%83%D1%8E%D1%89%D0%B8%D0%B5_%D1%80%D0%B0%D0%B7%D0%BC%D0%B5%D0%BD%D0%BD%D1%8B%D0%B5_%D0%B4%D0%B5%D0%BD%D0%B5%D0%B6%D0%BD%D1%8B%D0%B5_%D0%B5%D0%B4%D0%B8%D0%BD%D0%B8%D1%86%D1%8B>
     *     разменных денежных единиц</a> в шокельке.
     * Это могут быть копейки, центы и другие.
     */
    private final long penny;

    /**
     * Название валюты, в которой содержится баланс.
     */
    private final String currency;

    /**
     * @seenizer penny
     */
    public long getPenny() {
        return penny;
    }

    /**
     * @seenizer currency
     */
    public String getCurrency() {
        return currency;
    }


    /**
     * Вычисление, сколько будет денег, если прибавить к текущим ещё денег.
     * @param value Сколько нужно добавить минимальных единиц денег к текущим?
     * @return Новый экземпляр состояния денег.
     * @throws ArithmeticException Превышена точность long.
     */
    public Money plus(long value) throws ArithmeticException {
        return new Money(Math.addExact(penny, value), currency);
    }

    /**
     * Вычисление, сколько будет денег, если отнять от текущих ещё денег.
     * @param value Сколько нужно отнять минимальных единиц денег к текущим?
     * @return Новый экземпляр состояния денег.
     * @throws ArithmeticException Превышена точность long.
     */
    public Money minus(long value) throws ArithmeticException {
        return new Money(Math.subtractExact(penny, value), currency);
    }

    /**
     * Вычисление, сколько будет денег, если прибавить к текущим ещё денег.
     * @param value Сколько нужно добавить денег к текущим?
     * @return Новый экземпляр состояния денег.
     * @throws MoneyException Не поддерживается работа с разными валютами.
     */
    public Money plus(Money value) throws MoneyException {
        if(this.currency.equals(value.currency))
            return plus(value.penny);
        else
            throw new MoneyException("Currency not equal: " + this.currency + " and " + value.currency);
    }

    /**
     * Вычисление, сколько будет денег, если отнять от текущих денег заданную сумму денег.
     * @param value Сколько нужно отнять денег из текущих?
     * @return Новый экземпляр состояния денег.
     * @throws MoneyException Не поддерживается работа с разными валютами.
     */
    public Money minus(Money value) throws MoneyException {
        if(this.currency.equals(value.currency))
            return minus(value.penny);
        else
            throw new MoneyException("Currency not equal: " + this.currency + " and " + value.currency);
    }

    /**
     * Делит кошелёк на несколько кошельков.
     * Если есть неделимые копейки, они раздаются по-одной первым персонам.
     * @param count Количество персон, кому надо разделить деньги.
     * @return Список сумм, сколько им положено.
     * @throws RuntimeException {@code count} должен быть положительным числом.
     */
    public ArrayList<Money> div(short count) {
        if(count < 1)
            throw new RuntimeException("count must be positive");
        ArrayList<Money> out = new ArrayList<>(count);
        // Столько получит каждый человек.
        long pennyForEvery = penny /count;
        // Столько человек получит дополнительно по одной копейке.
        long countOfPeopleWhoGetPlusOnePenny = penny % count;
        for(short i = 0; i < count; i++) {
            pennyForEvery += 1;
            if(countOfPeopleWhoGetPlusOnePenny-- == 0) {
                pennyForEvery -= 1;
            }
            out.add(new Money(pennyForEvery, currency));
        }
        return out;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Money money = (Money) o;

        if (penny != money.penny) return false;
        return currency.equals(money.currency);
    }

    @Override
    public int hashCode() {
        int result = (int) (penny ^ (penny >>> 32));
        result = 31 * result + currency.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Money{" +
                "penny=" + penny +
                ", currency=" + currency +
                '}';
    }

    /**
     * Сравнивает две денежных суммы, если их валюты одинаковы.
     * @param o Вторая сумма денег.
     * @return the value 0 if {@code this == o};
     *      * a value less than 0 if {@code this < o};
     *      * and a value greater than 0 if {@code this > o}.
     * @throws RuntimeException Эти деньги несравнимы, так как у них разная валюта.
     */
    @Override
    public int compareTo(Money o) throws RuntimeException {
        try {
            return compare(this, o);
        } catch (MoneyException me) {
            throw new RuntimeException(me.getLocalizedMessage());
        }
    }

    /**
     * Сравнивает две денежных суммы, если их валюты одинаковы.
     * @param x Первая сумма денег.
     * @param y Вторая сумма денег.
     * @return the value 0 if {@code x == y};
     * a value less than 0 if {@code x < y};
     * and a value greater than 0 if {@code x > y}.
     * @throws MoneyException Эти деньги несравнимы, так как у них разная валюта.
     */
    public static int compare(Money x, Money y) throws MoneyException {
        if(x.getCurrency().equals(y.getCurrency()))
            return Long.compare(x.penny, y.penny);
        else
            throw new MoneyException("Can't compare: " + x + " and " + y);
    }
}
