package ru.mirea.BalanceService;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonStreamContext;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.ResolvableDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.*;

public class User {
    private final long user_id;
    private final List<Money> balance; //количество денег в базовой валюте

    public User() {
        this(0);
    }

    /**
     * Создание пользователя, у которого есть деньги только в одной валюте.
     * @param user_id Идентификатор пользователя.
     * @param money Количество денег в заданной валюте.
     */
    User(long user_id, Money money) {
        this(user_id, new ArrayList<>(Collections.singleton(money)));
    }

    /**
     * Создание пользователя, у которого нет денег.
     * @param user_id Идентификатор пользователя.
     */
    User(long user_id) {
        this(user_id, new ArrayList<>());
    }

    /**
     * Создание пользователя, у которого есть деньги.
     * @param user_id Идентификатор пользователя.
     * @param balance Кошельки пользователя.
     */
    public User(long user_id, Collection<Money> balance) {
        this.user_id = user_id;
        if(balance == null)
            balance = Collections.emptyList();
        if(balance.contains(null))
            throw new NullPointerException("Balance element can't be null!");
        this.balance = new ArrayList<>(balance);
    }

    public long getUser_id() {
        return user_id;
    }

    /**
     * Возвращает копию кошельков пользователя.
     * Копию нельзя модифицировать.
     */
    public List<Money> getBalance() {
        return Collections.unmodifiableList(balance);
    }

    /**
     * Ищет баланс пользователя по определённой валюте.
     * @param nameCurrency Название необходимой валюты.
     * @return Возвращает количество денег по данной валюте.
     */
    Money getBalance(String nameCurrency) {
        for (Money m : getBalance())
            if (m.getCurrency().equals(nameCurrency))
                return m;
        return new Money(0, nameCurrency);
    }

    /**
     * Возвращает количество валют у пользователя.
     */
    int size() {
        return balance.size();
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

    /**
     * Изменяет баланс пользователя заданной валюты.
     * Если заданной валюты у пользователя нет, то добавляется новая.
     * У пользователя может быть только один кошелёк в заданной валюте.
     * @param balance Новый баланс пользователя.
     */
    void updateOrAddBalance(Money balance) {
        synchronized (this.balance) {
            this.balance.removeIf(m -> m.getCurrency().equals(balance.getCurrency()));
            this.balance.add(balance);
        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder()
                .append("User{")
                .append("user_id=")
                .append(user_id)
                .append(", balance=[");
        for (Money m :
                balance) {
            stringBuilder.append(m);
        }
        stringBuilder.append("]}");
        return stringBuilder.toString();
    }
}
