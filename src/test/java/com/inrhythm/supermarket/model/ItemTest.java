package com.inrhythm.supermarket.model;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ItemTest {

    @Test
    public void itShouldReturnExistingItemWithMatchingName() {

        //given

        List<Item> existingItems = new ArrayList<>();
        existingItems.add(new Item("Item 1", 2));
        existingItems.add(new Item("Item 2", 1));
        existingItems.add(new Item("Item 3", 4));

        Item newItem = new Item("Item 1", 3);
        //when

        Item existAlready = newItem.getExistingItem(existingItems).get();

        //then

        assertEquals(existAlready.getName(), newItem.getName());
        assertEquals(existAlready.getQuantity(), 2);

    }

    @Test
    public void itShouldReturnNullForNonExistingItem() {

        //given
        List<Item> existingItems = new ArrayList<>();
        existingItems.add(new Item("Item 1", 2));
        existingItems.add(new Item("Item 2", 1));
        existingItems.add(new Item("Item 3", 4));

        Item newItem = new Item("Item 6", 3);
        //when

        Item existAlready = newItem.getExistingItem(existingItems).get();

        //then
        Assert.assertNull(existAlready);

    }

    @Test
    public void itShouldCheckItemEquality() {
        List<Item> existingItems = new ArrayList<>();
        Item i1 = new Item("one", 2);
        Item i2 = new Item("two", 2);
        existingItems.add(i1);
        existingItems.add(i2);
        Item i3 = new Item("one", 3);

        Assert.assertTrue(i1.equals(i3));
        Assert.assertTrue(existingItems.contains(i3));

    }
}