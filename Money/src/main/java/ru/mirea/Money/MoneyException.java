package ru.mirea.Money;

/**
 * Символизирует ошибку при денежных операциях.
 */
public class MoneyException extends Exception {
    public MoneyException(String s) {
        super(s);
    }
}
