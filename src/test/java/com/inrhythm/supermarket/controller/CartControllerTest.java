package com.inrhythm.supermarket.controller;

import com.inrhythm.supermarket.exceptions.InvalidItemFoundException;
import com.inrhythm.supermarket.model.Cart;
import com.inrhythm.supermarket.model.Item;
import com.inrhythm.supermarket.model.Product;
import com.inrhythm.supermarket.repository.CartRepository;
import com.inrhythm.supermarket.repository.ProductRepository;
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

    @Mock
    ProductRepository productRepository;

    @InjectMocks
    CartController cartController;


    private List<Item> items;
    private List<Product> products;

    @Before
    public void setUp() {
        items = new ArrayList<>();
        items.add(new Item("Item 1", 2));
        items.add(new Item("Item 2", 1));
        items.add(new Item("Item 3", 4));

        products = new ArrayList<>();
        products.add(new Product("Item 1", "Personal Care", 5.0));
        products.add(new Product("Item 2", "Electronics", 350.0));
        products.add(new Product("Item 3", "Electronics", 350.0));
    }


    @Test
    public void itShouldAddItemsToCart() {

        when(productRepository.findAll()).thenReturn(products);

        //when
        Cart actualCart = cartController.create(items);

        //then
        Mockito.verify(productRepository, times(1)).findAll();
        Mockito.verify(mockCartRepository, times(1)).save(actualCart);
        Assert.assertEquals(actualCart.getItems().size(), items.size());
        Assert.assertEquals(actualCart.getItems(), items);

    }

    @Test(expected = InvalidItemFoundException.class)
    public void itShouldThrowIInvalidItemFoundExceptionForInvalidItem() {
        List<Item> invalidItems = new ArrayList<>();
        invalidItems.add(new Item("invalid item", 2));

        when(productRepository.findAll()).thenReturn(products);
        //when
        Cart actualCart = cartController.create(invalidItems);
        // then
        Mockito.verify(productRepository, times(1)).findAll();
        Mockito.verify(mockCartRepository, times(1)).save(actualCart);
    }


    @Test
    public void itShouldUpdateExistingCartWithNewItem() {
        Cart existingCart = new Cart(items);
        products.add(new Product("new item", "someCategory", 5.0));

        List<Item> newItems = new ArrayList<>();
        Item newItem = new Item("new item", 3);
        newItems.add(newItem);
        Cart requestCart = new Cart(newItems);

        when(productRepository.findAll()).thenReturn(products);

        when(mockCartRepository.findOne(existingCart.getId())).thenReturn(existingCart);

        Cart actualCart = cartController.update(existingCart.getId(), requestCart);

        Mockito.verify(mockCartRepository, times(1)).findOne(Matchers.anyString());
        Mockito.verify(mockCartRepository, times(1)).save(Matchers.any(Cart.class));
        Assert.assertEquals(4, actualCart.getItems().size());
        Assert.assertEquals(actualCart.getItems().get(3).getName(), newItem.getName());

    }


    @Test
    public void itShouldUpdateQuantityOfExistingItem() {
        Cart existingCart = new Cart(items);

        List<Item> newItems = new ArrayList<>();
        Item existingItem = new Item("Item 1", 1);
        newItems.add(existingItem);
        Cart requestCart = new Cart(newItems);

        when(productRepository.findAll()).thenReturn(products);

        when(mockCartRepository.findOne(existingCart.getId())).thenReturn(existingCart);

        Cart actualCart = cartController.update(existingCart.getId(), requestCart);

        Mockito.verify(mockCartRepository, times(1)).findOne(Matchers.anyString());
        Mockito.verify(mockCartRepository, times(1)).save(Matchers.any(Cart.class));
        Assert.assertEquals(3, actualCart.getItems().size());
        Assert.assertEquals(3, actualCart.getItems().get(0).getQuantity());

    }

    @Test
    public void itShouldDeleteExistingItem() {
        Item existingItem = new Item("Item 1", 3);
        Cart existingCart = new Cart(items);

        List<Item> itemsToDelete = new ArrayList<>();
        itemsToDelete.add(existingItem);
        when(productRepository.findAll()).thenReturn(products);


        when(mockCartRepository.findOne(existingCart.getId())).thenReturn(existingCart);

        Cart actualCart = cartController.delete(existingCart.getId(), itemsToDelete);

        Mockito.verify(mockCartRepository, times(1)).findOne(Matchers.anyString());
        Mockito.verify(mockCartRepository, times(1)).save(Matchers.any(Cart.class));
        Assert.assertEquals(2, actualCart.getItems().size());
        Assert.assertEquals(false, actualCart.getItems().contains(existingItem));

    }


    @Test
    public void itShouldDeleteQuantityOfExistingItem() {
        Cart existingCart = new Cart(items);

        List<Item> itemsToDelete = new ArrayList<>();
        Item existingItem = new Item("Item 1", 1);
        itemsToDelete.add(existingItem);
        when(productRepository.findAll()).thenReturn(products);

        when(mockCartRepository.findOne(existingCart.getId())).thenReturn(existingCart);

        Cart resultantCart = cartController.delete(existingCart.getId(), itemsToDelete);

        Mockito.verify(mockCartRepository, times(1)).findOne(Matchers.anyString());
        Mockito.verify(mockCartRepository, times(1)).save(Matchers.any(Cart.class));
        Assert.assertEquals(3, resultantCart.getItems().size());
        Assert.assertEquals(1, resultantCart.getItems().get(0).getQuantity());
        Assert.assertEquals(true, resultantCart.getItems().contains(existingItem));

    }

}