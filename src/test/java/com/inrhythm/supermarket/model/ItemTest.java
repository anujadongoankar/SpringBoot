package com.inrhythm.supermarket.model;

import com.inrhythm.supermarket.model.Item;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ItemTest {

    @Test
    public void itShouldReturnExistingItemWithMatchingName() {

        //given

        List<Item> existingItems = new ArrayList<>();
        existingItems.add(new Item("Item 1",2));
        existingItems.add(new Item("Item 2",1));
        existingItems.add(new Item("Item 3",4));

        Item newItem = new Item("Item 1", 3);
        //when

        Item existAlready = newItem.ifItemExistAlready(existingItems);

        //then

        assertEquals(existAlready.getName(),newItem.getName());
        assertEquals(existAlready.getQuantity(),2);

    }


    @Test
    public void itShouldReturnNullForNonExistingItem() {

        //given

        List<Item> existingItems = new ArrayList<>();
        existingItems.add(new Item("Item 1",2));
        existingItems.add(new Item("Item 2",1));
        existingItems.add(new Item("Item 3",4));

        Item newItem = new Item("Item 6", 3);
        //when

        Item existAlready = newItem.ifItemExistAlready(existingItems);

        //then
        Assert.assertNull(existAlready);

    }
}