package com.inrhythm.supermarket.controller;

import com.inrhythm.supermarket.model.Cart;
import com.inrhythm.supermarket.model.Item;
import com.inrhythm.supermarket.repository.CartRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CartControllerTest {

    @Mock
    CartRepository mockCartRepository;

    @InjectMocks
    CartController cartController;

    @Test
    public void itShouldAddItemsToCart() {
        //given
        List<Item> items = new ArrayList<>();
        items.add(new Item("Item 1", 2));
        items.add(new Item("Item 2", 1));
        items.add(new Item("Item 3", 4));

        Item newItem = new Item("Item 1", 3);

        //when
        Cart actualCart = cartController.create(items);

        //then
        Mockito.verify(mockCartRepository, times(1)).save(actualCart);
        Assert.assertEquals(actualCart.getItems().size(), items.size());
        Assert.assertEquals(actualCart.getItems(), items);

    }

    @Test
    public void itShouldUpdateExistingCartWithNewItem() {
        List<Item> items = new ArrayList<>();
        items.add(new Item("Item 1", 2));
        items.add(new Item("Item 2", 1));
        items.add(new Item("Item 3", 4));
        Cart existingCart = new Cart(items);

        List<Item> newItems = new ArrayList<>();
        Item newItem = new Item("new item", 3);
        newItems.add(newItem);
        Cart requestCart = new Cart(newItems);

        when(mockCartRepository.findOne(existingCart.getId())).thenReturn(existingCart);

        Cart actualCart = cartController.update(existingCart.getId(),requestCart);

        Mockito.verify(mockCartRepository, times(1)).findOne(Matchers.anyString());
        Mockito.verify(mockCartRepository, times(1)).save(Matchers.any(Cart.class));
        Assert.assertEquals(4,actualCart.getItems().size());
        Assert.assertEquals(actualCart.getItems().get(3).getName(),newItem.getName());

    }


    @Test
    public void itShouldUpdateQuantityOfExistingItem() {
        List<Item> items = new ArrayList<>();
        items.add(new Item("Item 1", 2));
        items.add(new Item("Item 2", 1));
        items.add(new Item("Item 3", 4));
        Cart existingCart = new Cart(items);

        List<Item> newItems = new ArrayList<>();
        Item existingItem = new Item("Item 1", 3);
        newItems.add(existingItem);
        Cart requestCart = new Cart(newItems);

        when(mockCartRepository.findOne(existingCart.getId())).thenReturn(existingCart);

        Cart actualCart = cartController.update(existingCart.getId(),requestCart);

        Mockito.verify(mockCartRepository, times(1)).findOne(Matchers.anyString());
        Mockito.verify(mockCartRepository, times(1)).save(Matchers.any(Cart.class));
        Assert.assertEquals(3,actualCart.getItems().size());
        Assert.assertEquals(5,actualCart.getItems().get(0).getQuantity());

    }

    @Test
    public void itShouldDeleteExistingItem() {
        List<Item> items = new ArrayList<>();
        Item existingItem = new Item("Item 1", 3);

        items.add(existingItem);
        items.add(new Item("Item 2", 1));
        items.add(new Item("Item 3", 4));
        Cart existingCart = new Cart(items);

        List<Item> itemsToDelete = new ArrayList<>();
        itemsToDelete.add(existingItem);

        when(mockCartRepository.findOne(existingCart.getId())).thenReturn(existingCart);

        Cart actualCart = cartController.delete(existingCart.getId(),itemsToDelete);

        Mockito.verify(mockCartRepository, times(1)).findOne(Matchers.anyString());
        Mockito.verify(mockCartRepository, times(1)).save(Matchers.any(Cart.class));
        Assert.assertEquals(2, actualCart.getItems().size());
        Assert.assertEquals(false, actualCart.getItems().contains(existingItem));

    }


    @Test
    public void itShouldDeleteQuantityOfExistingItem() {
        List<Item> items = new ArrayList<>();
        items.add(new Item("Item 1", 4));
        items.add(new Item("Item 2", 1));
        items.add(new Item("Item 3", 4));
        Cart existingCart = new Cart(items);

        List<Item> itemsToDelete = new ArrayList<>();
        Item existingItem = new Item("Item 1", 2);
        itemsToDelete.add(existingItem);

        when(mockCartRepository.findOne(existingCart.getId())).thenReturn(existingCart);

        Cart resultantCart = cartController.delete(existingCart.getId(),itemsToDelete);

        Mockito.verify(mockCartRepository, times(1)).findOne(Matchers.anyString());
        Mockito.verify(mockCartRepository, times(1)).save(Matchers.any(Cart.class));
        Assert.assertEquals(3,resultantCart.getItems().size());
        Assert.assertEquals(2,resultantCart.getItems().get(0).getQuantity());
        Assert.assertEquals(true,resultantCart.getItems().contains(existingItem));

    }

}