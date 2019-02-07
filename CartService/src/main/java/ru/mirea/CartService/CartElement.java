package ru.mirea.CartService;

/**
 * Символизация группы однотипных предметов корзины.
 * Например: 2 пачки молока, 1000 грамм картошки, 50 пачек спичек
 */
public class CartElement {

    private long item_id;
    private long count;

    /**
     * Создание группы однотипных предметов.
     * @param item_id Идентификатор предмета, который пользователь хочет купить.
     * @param count Число, символизирующее количество предмета, которое мы хотим купить.
     *              Точнее сказать: сколько раз мы хотим купить item_id?
     *              Число может быть только положительное или равно нулю.
     */
    public CartElement(long item_id, long count) {
        this.item_id = item_id;
        if(count < 0)
            this.count = count;
    }

    /**
     * Получение идентификатора желаемого товара.
     */
    public long getItem_id() {
        return item_id;
    }

    /**
     * Получение количество желаемого товара.
     */
    public long getCount() {
        return count;
    }
}
