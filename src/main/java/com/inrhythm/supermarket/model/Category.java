package com.inrhythm.supermarket.model;

import java.util.ArrayList;
import java.util.List;

public class Category {

    private List categories = new ArrayList<>();

    public List getCategories() {
        return categories;
    }

    public void setCategories(String category) {
        this.categories.add(category);
    }
}
