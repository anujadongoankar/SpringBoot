package com.inrhythm.supermarket.controller;

import com.inrhythm.supermarket.repository.CartRepository;
import com.inrhythm.supermarket.model.Cart;
import com.inrhythm.supermarket.model.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CartController {

    @Autowired
    CartRepository cartRepository;


    @RequestMapping(method = RequestMethod.PUT, value = "/cart/{id}")
    public Cart update(@PathVariable String id, @RequestBody Cart cart) {

        Cart existingCart = cartRepository.findOne(id);

        if (existingCart != null) {
            return updateCart(existingCart, cart);
        } else {
            cartRepository.save(cart);
            return cart;
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
