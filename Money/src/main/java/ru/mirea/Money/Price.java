package ru.mirea.Money;
/**
 * Описывает состояние цену товара в конкретной валюте.
 * @author <a href="https://github.com/SGmuwa">[SG]Muwa<a/>
 * @since 1.0
 */
public class Price implements Comparable<Price> {

    /**
     * Данный конструктор необходим для того, чтобы можно было
     * из json кода преобразовать в данный класс java.
     */
    public Price() {
        countUnits = 0;
        currency = "";
    }

    /**
     * Создание нового <b>состояния</b> цены в заданной валюте.
     * Например, 0.000001 рубль за грамм (0.0001 копеек за грамм, 0.1 копеек за киллограмм).
     * @param countUnits Количество <a href=https://ru.wikipedia.org/wiki/%D0%A1%D1%83%D1%89%D0%B5%D1%81%D1%82%D0%B2%D1%83%D1%8E%D1%89%D0%B8%D0%B5_%D1%80%D0%B0%D0%B7%D0%BC%D0%B5%D0%BD%D0%BD%D1%8B%D0%B5_%D0%B4%D0%B5%D0%BD%D0%B5%D0%B6%D0%BD%D1%8B%D0%B5_%D0%B5%D0%B4%D0%B8%D0%BD%D0%B8%D1%86%D1%8B>
     *                   разменных денежных единиц</a>, умноженное на 10000. <br/>
     *                   Так, например, если Ваш товар стоит 10 рублей за киллограмм, то
     *                   надо сначала перевести это в граммы: 0,01 рублей за грамм.
     *                   Это делается для того, чтобы единицы товара были недилимы.
     *                   Затем переведите это в копейки. Это будет
     *                   1 копейка за грамм.
     *                   Это необходимо, чтобы деньги были в неделимых единицах.
     *                   Затем умножьте результат на 10000. Это необходимо, чтобы
     *                   точность вычислений была высокой.
     *                   Итого в аргумент надо записать: 10000.
     *                   При такой точности минимальная цена: 0.1 копеек за киллограмм
     *                   или 0.0001 копеек за пачку молока.
     *                   Цена может быть только положительной.
     * @param currency Валюта денежных единиц цены.
     * @throws NullPointerException currency был подан Null или currency пуст. Цена в данном классе может
     * указываться только в единственной, заранее известной валюте.
     * @throws IllegalArgumentException countUnits был подан 0 или меньше. Поддерживается
     * только положительная стоимость за единицу товара.
     */
    public Price(long countUnits, String currency) throws NullPointerException, IllegalArgumentException {
        if(currency == null || currency.isEmpty())
            throw new NullPointerException("currency");
        if(countUnits <= 0)
            throw new IllegalArgumentException("countUnits must be more than 0");
        this.countUnits = countUnits;
        this.currency = currency;
    }

    /**
     * Стоимость товара в разменных денежных единицах, умноженное на 10000.
     * Стоит заметить, что данный класс хранит состояние цены.
     * То есть при изменении цены необходимо создовать новый экземпляр класса.
     */
    private final long countUnits;
    /**
     * Валюта, в которой указана стоимость за единицу товара.
     */
    private final String currency;

    /**
     * Получение стоимости товара в <a href=https://ru.wikipedia.org/wiki/%D0%A1%D1%83%D1%89%D0%B5%D1%81%D1%82%D0%B2%D1%83%D1%8E%D1%89%D0%B8%D0%B5_%D1%80%D0%B0%D0%B7%D0%BC%D0%B5%D0%BD%D0%BD%D1%8B%D0%B5_%D0%B4%D0%B5%D0%BD%D0%B5%D0%B6%D0%BD%D1%8B%D0%B5_%D0%B5%D0%B4%D0%B8%D0%BD%D0%B8%D1%86%D1%8B>
     *     разменных денежных единиц</a>, умноженное на 10000.
     */
    public long getCountUnits() {
        return countUnits;
    }

    /**
     * Получение названия валюты, в которой указна стоимость.
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * Вы называете, сколько Вам нужно неделимых единиц товара (пачки, граммы), а функция
     * возвращает вам суммарную цену.
     * @param needProduct Сколько покупатель хочет купить товара.
     * @return Количество денег, сколько понадоибится на покупку заданного количества.
     * @throws ArithmeticException Нехватка точности long.
     * @throws RuntimeException Цена не инициализирована правильно и не готова к использованию.
     * Используйте {@link #Price(long, String)} для безопасного создания цены.
     */
    public Money calculate(long needProduct) throws ArithmeticException, RuntimeException {
        ready();
        /*
        example.
        from = rub
        to = dollar
        mul = 70 000000
        need 2 cent?
        2 * 70 000000 = 140 000000
        140 000000 / 10000 = 140 00 копеек. Округлить надо в большую сторону.
         */
        long bigCourse = Math.multiplyExact(getCountUnits(), needProduct); // Большой курс
        long result = Math.floorDiv(bigCourse, 10000);
        long mod = Math.floorMod(bigCourse, 10000);
        if (mod > 10000 / 3)
            result += 1;
        return new Money(result, currency);
    }

    /**
     * Функция рачитывает, сколько можно купить товара за названные деньги.
     * @param howMuchIHaveMoney Сколько вы готовы предложить за товар.
     *                          Писать в неделимых единицах. Копейки, центы...
     * @return Сколько вам могут дать за это товара.
     * Результат в неделимых единицах (штук, граммы, и так далее).
     * @throws ArithmeticException Нехватка точности long.
     * @throws RuntimeException Цена не инициализирована правильно и не готова к использованию.
     * Используйте {@link #Price(long, String)} для безопасного создания цены.
     */
    public long howMuchICanBuy(long howMuchIHaveMoney) throws ArithmeticException, RuntimeException {
        ready();
        /*
        example.
        from = rub
        to = dollar
        mul = 70 000000
        have 70 00 кпеек?
        70 00 * 10000 = 70 000000
        70 000000 / mul = 1 $
        1 * 100 = 1 00 cent.
         */
        return Math.multiplyExact(howMuchIHaveMoney, 10000) / countUnits;
    }

    /**
     * Функция рачитывает, сколько можно купить товара за названные деньги.
     * Данный метод отличается от {@link #howMuchICanBuy(long)} тем, что идёт дополнительная
     * проверка на совпадение валют.
     * @param howMuchIHaveMoney Сколько вы готовы предложить за товар.
     * @return Сколько вам могут дать за это товара.
     * Результат в неделимых единицах (штук, граммы, и так далее).
     * @throws ArithmeticException Нехватка точности long.
     * @throws MoneyException Валюты не совпадают.
     * @throws RuntimeException Цена не инициализирована правильно и не готова к использованию.
     * Используйте {@link #Price(long, String)} для безопасного создания цены.
     */
    public long howMuchICanBuy(Money howMuchIHaveMoney) throws ArithmeticException, MoneyException, RuntimeException {
        if(!howMuchIHaveMoney.getCurrency().equals(currency))
            throw new MoneyException("Currency not equal: " + currency + " and " + howMuchIHaveMoney.getCurrency());
        return howMuchICanBuy(howMuchIHaveMoney.getPenny());
    }

    /**
     * @throws RuntimeException Вызывается ошибка в случае, если данной стоимостью нельзя пользоваться.
     * Поддерживается только положительная стоимость и известная валюта.
     * @see #Price(long, String) Конструктор безопасной цены.
     */
    public void ready() throws RuntimeException {
        if(currency.isEmpty() || countUnits <= 0)
            throw new RuntimeException("currency or countUnits not correct: " + this);
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
    public int compareTo(Price o) throws RuntimeException {
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
    public static int compare(Price x, Price y) throws MoneyException {
        if(x.getCurrency().equals(y.getCurrency()))
            return Long.compare(x.countUnits, y.countUnits);
        else
            throw new MoneyException("Can't compare: " + x + " and " + y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Price price = (Price) o;

        if (countUnits != price.countUnits) return false;
        return currency.equals(price.currency);
    }

    @Override
    public int hashCode() {
        int result = (int) (countUnits ^ (countUnits >>> 32));
        result = 31 * result + currency.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Price{" +
                "countUnits=" + countUnits +
                ", currency='" + currency + '\'' +
                '}';
    }
}
