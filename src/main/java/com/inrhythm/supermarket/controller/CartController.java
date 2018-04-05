package com.inrhythm.supermarket.controller;

import com.inrhythm.supermarket.model.Cart;
import com.inrhythm.supermarket.model.Item;
import com.inrhythm.supermarket.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CartController {

    @Autowired
    CartRepository cartRepository;

    @RequestMapping(method = RequestMethod.POST, value = "/cart")
    public Cart create(@RequestBody List<Item> items) {
        Cart newCart = new Cart(items);
        cartRepository.save(newCart);
        return newCart;
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/cart/{id}")
    public Cart update(@PathVariable String id, @RequestBody Cart cart) {

        //TODO verification of product name with actual existing list of product
        Cart existingCart = cartRepository.findOne(id);
        if (existingCart != null) {
            return updateCart(existingCart, cart);
        } else {
            cartRepository.save(cart);
            return cart;
        }
    }


    @RequestMapping(method = RequestMethod.DELETE, value = "/cart/{id}")
    public Cart delete(@PathVariable String id, @RequestBody List<Item> items) {

        Cart existingCart = cartRepository.findOne(id);
        if (existingCart != null) {
            return deleteItems(existingCart, items);
        } else {
            //throw exception invalid cart and invalid item
            return existingCart;
        }

    }

    private Cart deleteItems(Cart existingCart, List<Item> items) {
        List<Item> existingCartItems = existingCart.getItems();

        for (Item item : items) {
            Item existingItem = item.ifItemExistAlready(existingCartItems);
            if (existingItem != null) {
                RemoveExistingItem(existingCartItems, item, existingItem);
            }else {
                throw new ItemNotFoundException("Item :" + item.getName() + "not found");
            }

        }
        cartRepository.save(existingCart);
        return existingCart;
    }

    private void RemoveExistingItem(List<Item> existingCartItems, Item item, Item existingItem) {
        if (item.getQuantity() < existingItem.getQuantity()) {
            existingItem.reduceQuantity(item.getQuantity());
        } else {
            existingCartItems.remove(existingItem);
        }
    }


    private Cart updateCart(Cart existingCart, Cart newCart) {
        List<Item> newItems = newCart.getItems();
        List<Item> existingCartItems = existingCart.getItems();
        for (Item newItem : newItems) {
            Item existingItem = newItem.ifItemExistAlready(existingCartItems);
            if (existingItem != null) {
                existingItem.updateQuantity(newItem.getQuantity());
            } else {
                existingCartItems.add(newItem);
            }
        }
        cartRepository.save(existingCart);
        return existingCart;
    }


}
