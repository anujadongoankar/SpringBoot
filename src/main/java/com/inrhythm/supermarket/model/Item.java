package com.inrhythm.supermarket.model;

import java.util.List;
import java.util.Optional;

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

    public void reduceQuantity(int quantity) {
        this.quantity = this.quantity - quantity;
    }

    @Override
    public boolean equals(Object anObject) {
        if (this == anObject) {
            return true;
        }
        if (anObject instanceof Item) {
            Item anotherItem = (Item) anObject;
            if (this.name.equals(anotherItem.name))
                return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + (this.name != null ? this.name.hashCode() : 0);
        return hash;
    }

    public Optional<Item> getExistingItem(List<Item> existingCartItems) {
        return existingCartItems.stream().filter(existingItem -> existingItem.getName().equals(this.name)).findFirst();
    }

    public boolean checkValidItem(List<Product> products) {

        boolean isValid = products.stream().filter(product -> product.getName().equals(this.name)).findFirst().isPresent();
        return isValid;
    }
}



