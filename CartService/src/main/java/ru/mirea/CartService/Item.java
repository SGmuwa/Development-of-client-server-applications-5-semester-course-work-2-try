package ru.mirea.CartService;

import ru.mirea.Money.Money;

public class Item {
    /**
     * Идентификатор товара.
     */
    private long item_id;
    /**
     * Наименование товара.
     */
    private String name;
    /**
     * Описание товара.
     */
    private String description;
    /**
     * Количество товара на складе.
     */
    private long count;
    /**
     * Стоимость товара.
     * Предполагается, что стоимость товара в одной единственной валюте.
     */
    private Money price;

    /**
     * Данный конструктор необходим для того, чтобы можно было
     * из json кода преобразовать в данный класс java.
     */
    public Item() {
        item_id = count = 0;
        name = description = "";
        price = new Money();
    }

    /**
     * Создание образа, что такое товар.
     * @param item_id Идентификатор товара.
     * @param name Имя товара. Следует учесть, что сервис поддерживает запись только на одном языке.
     * @param description Описание товара. Следует учесть, что сервис поддерживает запись только на одном языке.
     * @param count Количество товара. Считается 1 товара - минимальное количество товара, которое
     *              можно продать пользователю. Это может быть либо 1 клетка, либо 1 грамм корма.
     * @param price Цена за 1 единицу товара.
     */
    public Item(long item_id, String name, String description, int count, Money price) {
        this.item_id = item_id;
        this.name = name;
        this.description = description;
        this.count = count;
        this.price = price;
    }

    /**
     * @seenizer #item_id
     */
    public long getItem_id() {
        return item_id;
    }

    /**
     * @seenizer #name
     */
    public String getName() {
        return name;
    }

    /**
     * @seenizer #name
     */
    public String getDescription() {
        return description;
    }

    /**
     * @seenizer #count
     */
    public long getCount() {
        return count;
    }

    /**
     * @seenizer #price
     */
    public Money getPrice() {
        return price;
    }
}
