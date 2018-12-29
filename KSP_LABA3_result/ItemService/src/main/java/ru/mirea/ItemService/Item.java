package ru.mirea.ItemService;

public class Item {
    private int id;
    private String name;
    private String type;
    private int count;
    double price;


    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Item(int id, String name, String type, int count, double price) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.count = count;
        this.price = price;
    }
    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public int getCount() {
        return count;
    }

    public String getName() { return name; }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
