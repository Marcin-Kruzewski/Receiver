package com.example.mkruz.receiver;

public class TodoTask {
    private long id;
    private String description;
    private float price;
    private int qty;
    private boolean completed;

    public TodoTask(long id, String description, int qty, float price, boolean completed) {
        this.id = id;
        this.description = description;
        this.qty = qty;
        this.price = price;
        this.completed = completed;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getPrice() { return price; }

    public void setPrice(float price) {
        this.price = price;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }


    @Override
    public String toString() {
        return "id: " + id + " description: " + description + " qty: " + qty + " price: " + price;
    }
}