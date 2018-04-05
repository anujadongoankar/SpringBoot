package com.inrhythm.supermarket.controller;

import com.inrhythm.supermarket.Application;
import com.inrhythm.supermarket.model.Cart;
import com.inrhythm.supermarket.model.Item;
import com.inrhythm.supermarket.model.Product;
import com.inrhythm.supermarket.repository.CartRepository;
import org.json.JSONException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CartControllerItTest {

    @LocalServerPort
    private int port;

    @Autowired
    private CartRepository cartRepository;

    TestRestTemplate restTemplate = new TestRestTemplate();

    HttpHeaders headers = new HttpHeaders();

    @Before
    public void setup() {
        cartRepository.deleteAll();
    }

    @Test
    public void itShouldAddItemToCart() {

        //given
        List<Item> items = new ArrayList<>();
        items.add(new Item("Item 1", 2));

        HttpEntity<List<Item>> entity = new HttpEntity<List<Item>>(items, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/cart"),
                HttpMethod.POST, entity, String.class);

        String expected = "{\"items\":[{\"name\":\"Item 1\",\"quantity\":2}]}\n";
        HttpStatus statusCode = response.getStatusCode();

        try {
            JSONAssert.assertEquals(expected, response.getBody(), JSONCompareMode.LENIENT);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Assert.assertEquals(HttpStatus.OK, statusCode);
    }

    @Test
    public void itShouldUpdateCardWithNewItem() {

        //given
        List<Item> items = new ArrayList<>();
        items.add(new Item("Item 1", 2));
        Cart cart = new Cart(items);
        cartRepository.save(cart);

        List<Item> newItems = new ArrayList<>();
        newItems.add(new Item("new item", 1));

        Cart requestCart = new Cart(newItems);

        HttpEntity<Cart> entity = new HttpEntity<Cart>(requestCart, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/cart/"+cart.getId()),
                HttpMethod.PUT, entity, String.class);

        String expected = "{\"id\":"+cart.getId()+",\"items\":[{\"name\":\"Item 1\",\"quantity\":2},{\"name\":\"new item\",\"quantity\":1}]}";
        HttpStatus statusCode = response.getStatusCode();

        try {
            JSONAssert.assertEquals(expected, response.getBody(), JSONCompareMode.LENIENT);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Assert.assertEquals(HttpStatus.OK, statusCode);
    }


    @Test
    public void itShouldUpdateQuantityOfExistingItem() {

        //given
        List<Item> items = new ArrayList<>();
        items.add(new Item("Item 1", 2));
        Cart cart = new Cart(items);
        cartRepository.save(cart);

        List<Item> newItems = new ArrayList<>();
        newItems.add(new Item("Item 1", 3));

        Cart requestCart = new Cart(newItems);

        HttpEntity<Cart> entity = new HttpEntity<Cart>(requestCart, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/cart/"+cart.getId()),
                HttpMethod.PUT, entity, String.class);

        
        String expected = "{\"id\":"+cart.getId()+",\"items\":[{\"name\":\"Item 1\",\"quantity\":5}]}\n";
        HttpStatus statusCode = response.getStatusCode();

        try {
            JSONAssert.assertEquals(expected, response.getBody(), JSONCompareMode.LENIENT);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Assert.assertEquals(HttpStatus.OK, statusCode);
    }

    @Test
    public void itShouldRemoveItemFromCart() {

        //given
        List<Item> items = new ArrayList<>();
        items.add(new Item("Item 1", 2));
        Cart cart = new Cart(items);
        cartRepository.save(cart);

        List<Item> newItems = new ArrayList<>();
        newItems.add(new Item("Item 1", 2));

        HttpEntity< List<Item> > entity = new HttpEntity< List<Item> >(newItems, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/cart/"+cart.getId()),
                HttpMethod.DELETE, entity, String.class);

        String expected = "{\"id\":"+cart.getId()+",\"items\":[]}\n";
        HttpStatus statusCode = response.getStatusCode();

        try {
            JSONAssert.assertEquals(expected, response.getBody(), JSONCompareMode.LENIENT);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Assert.assertEquals(HttpStatus.OK, statusCode);
    }

    @Test
    public void itShouldReduceQuantityOfItemFromCart() {
        //given
        List<Item> items = new ArrayList<>();
        items.add(new Item("Item 1", 5));
        Cart cart = new Cart(items);
        cartRepository.save(cart);

        List<Item> newItems = new ArrayList<>();
        newItems.add(new Item("Item 1", 2));
        HttpEntity< List<Item> > entity = new HttpEntity< List<Item> >(newItems, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/cart/"+cart.getId()),
                HttpMethod.DELETE, entity, String.class);

        String expected = "{\"id\":"+cart.getId()+",\"items\":[{\"name\":\"Item 1\",\"quantity\":3}]}\n";
        HttpStatus statusCode = response.getStatusCode();

        try {
            JSONAssert.assertEquals(expected, response.getBody(), JSONCompareMode.LENIENT);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Assert.assertEquals(HttpStatus.OK, statusCode);
    }


    @Test
    public void itShouldThrowErrorIfItemNotPresentInCardForDeletion() {
        //given
        List<Item> items = new ArrayList<>();
        items.add(new Item("Item 1", 5));
        Cart cart = new Cart(items);
        cartRepository.save(cart);

        List<Item> newItems = new ArrayList<>();
        newItems.add(new Item("Item 2", 2));
        HttpEntity< List<Item> > entity = new HttpEntity< List<Item> >(newItems, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/cart/"+cart.getId()),
                HttpMethod.DELETE, entity, String.class);

        String expected =  "{\"message\":\"Item :Item 2not found\"}\n";
        HttpStatus statusCode = response.getStatusCode();

        try {
            JSONAssert.assertEquals(expected, response.getBody(), JSONCompareMode.LENIENT);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Assert.assertEquals(HttpStatus.NOT_FOUND, statusCode);
    }
    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
}