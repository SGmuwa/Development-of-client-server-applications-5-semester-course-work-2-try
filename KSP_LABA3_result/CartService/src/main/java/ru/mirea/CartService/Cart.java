package ru.mirea.CartService;
public class Cart {

    /*private List<Item> items = new LinkedList<Item>();
    public Cart(List<Item> items) {
        this.items = items;
    }
    public List<Item> getItems() { return items;}
    */

    private int id;
    private int user_id;
    private int item_id;
    private String type;
    private double price;

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Cart(int id, int user_id, int item_id, String type, double price) {
        this.id = id;
        this.user_id = user_id;
        this.item_id = item_id;
        this.type = type;
        this.price = price;
    }

    public int getId() {
        return id;
    }
    public String getType() {
        return type;
    }

    public int getUser_id() {
        return user_id;
    }

    public int getItem_id() {
        return item_id;
    }

}
