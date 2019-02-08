package ru.mirea.Money;

import org.junit.Test;

import static org.junit.Assert.*;

public class PriceTest {

    /**
     * Цена молока 70 рублей.
     */
    private Price milk = new Price(70000000, "rub");

    /**
     * У меня есть 70 рублей. 7000 копеек.
     */
    private Money iHave = new Money(7000, "rub");

    @Test
    public void getPrice() {
        assertEquals(iHave, milk.calculate(1));
    }

    @Test
    public void howMuchICanBuy() {
        try {
            assertEquals(1, milk.howMuchICanBuy(iHave));
        } catch (MoneyException e) {
            fail(e.getLocalizedMessage());
        }
    }
}