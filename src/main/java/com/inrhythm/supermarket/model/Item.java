package com.inrhythm.supermarket.model;

import java.util.List;

public class Item {

    private String name;
    private int quantity;

    public Item() {
    }

    public Item(String name, int quantity) {

        this.name = name;
        this.quantity = quantity;
    }


    public String getName() {
        return this.name;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void updateQuantity(int quantity) {
        this.quantity = this.quantity + quantity;
    }

    //TODO equals method for name
    public Item ifItemExistAlready( List<Item> existingCartItems) {
        for (Item existingItem : existingCartItems) {
            if (existingItem.getName().equals(name)) {
                return existingItem;
            }
        }
        return null;
    }
}
