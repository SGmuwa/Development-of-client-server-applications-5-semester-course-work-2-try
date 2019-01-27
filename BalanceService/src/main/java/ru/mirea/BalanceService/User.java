package ru.mirea.BalanceService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class User {
    private final long user_id;
    private final List<Money> balance; //количество денег в базовой валюте

    /**
     * Создание пользователя, у которого есть деньги только в одной валюте.
     * @param user_id Идентификатор пользователя.
     * @param balance Количество денег в заданной валюте.
     */
    User(long user_id, Money balance) {
        this(user_id, Collections.singletonList(balance));
    }

    /**
     * Создание пользователя, у которого нет денег.
     * @param user_id Идентификатор пользователя.
     */
    User(long user_id) {
        this(user_id, Collections.emptyList());
    }

    /**
     * Создание пользователя, у которого есть деньги.
     * @param user_id Идентификатор пользователя.
     * @param balance Кошельки пользователя.
     */
    User(long user_id, List<Money> balance) {
        this.user_id = user_id;
        if(balance == null)
            balance = Collections.emptyList();
        if(balance.contains(null))
            throw new NullPointerException("Balance element can't be null!");
        this.balance = balance;
    }

    long getUser_id() {
        return user_id;
    }

    /**
     * Изменяет баланс пользователя заданной валюты.
     * У пользователя может быть только один кошелёк в заданной валюте.
     * @param balance Новый баланс пользователя.
     */
    void updateBalance(Money balance) {
        synchronized (balance) {
            this.balance.removeIf(m -> m.getCurrency().equals(balance.getCurrency()));
            this.balance.add(balance);
        }
    }

    /**
     * Возвращает копию кошельков пользователя.
     */
    List<Money> getBalance() {
        synchronized (balance) {
            return new ArrayList<>(balance);
        }
    }

    /**
     * Возвращает валюты пользователя.
     */
    List<String> getCurrency_name() {
        synchronized (balance) {
            ArrayList<String> currencies = new ArrayList<>(balance.size());
            for (Money m :
                    balance) {
                currencies.add(m.getCurrency());
            }
            return currencies;
        }
    }
}
