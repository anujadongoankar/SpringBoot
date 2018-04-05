package com.inrhythm.supermarket.controller;

import com.inrhythm.supermarket.exceptions.CartNotFound;
import com.inrhythm.supermarket.exceptions.InvalidItemFoundException;
import com.inrhythm.supermarket.exceptions.ItemNotFoundException;
import com.inrhythm.supermarket.model.Cart;
import com.inrhythm.supermarket.model.Item;
import com.inrhythm.supermarket.model.Product;
import com.inrhythm.supermarket.repository.CartRepository;
import com.inrhythm.supermarket.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CartController {

    @Autowired
    CartRepository cartRepository;

    @Autowired
    ProductRepository productRepository;

    @RequestMapping(method = RequestMethod.POST, value = "/cart")
    public Cart create(@RequestBody List<Item> items) {
        if (validCartItems(items)) {
            Cart newCart = new Cart(items);
            cartRepository.save(newCart);
            return newCart;
        } else {
            throw new InvalidItemFoundException("Invalid item found");
        }
    }

    private boolean validCartItems(@RequestBody List<Item> items) {
        List<Product> products = productRepository.findAll();

        for (Item item : items) {
            if (!item.checkValidItem(products)) throw new InvalidItemFoundException("Invalid item found");
        }

        return true;
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/cart/{id}")
    public Cart update(@PathVariable String id, @RequestBody Cart cart) {
        if (validCartItems(cart.getItems())) {
            Cart existingCart = cartRepository.findOne(id);
            if (existingCart != null) {
                return updateCart(existingCart, cart);
            } else {
                cartRepository.save(cart);
                return cart;
            }
        } else {
            throw new InvalidItemFoundException("Invalid item found");
        }
    }


    @RequestMapping(method = RequestMethod.DELETE, value = "/cart/{id}")
    public Cart delete(@PathVariable String id, @RequestBody List<Item> items) {
        Cart existingCart = cartRepository.findOne(id);
        if (existingCart != null) {
            return deleteItems(existingCart, items);
        } else {
            throw new CartNotFound("Cart not found");

        }
    }

    private Cart deleteItems(Cart existingCart, List<Item> items) {
        List<Item> existingCartItems = existingCart.getItems();

        for (Item item : items) {
            if (existingCartItems.contains(item)) {
                Item existingItem = item.getExistingItem(existingCartItems).get();
                removeExistingItem(existingCartItems, item, existingItem);
            }else {
              throw new ItemNotFoundException("Item :" + item.getName() + " not found");
            }
        }
        cartRepository.save(existingCart);
        return existingCart;
    }

    private void removeExistingItem(List<Item> existingCartItems, Item item, Item existingItem) {
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
            if (existingCartItems.contains(newItem)) {
                Item existingItem = newItem.getExistingItem(existingCartItems)
                        .orElseThrow(() -> new ItemNotFoundException("Item :" + newItem.getName() + " not found"));
                existingItem.updateQuantity(newItem.getQuantity());
            } else {
                existingCartItems.add(newItem);
            }
        }
        cartRepository.save(existingCart);
        return existingCart;
    }


}
