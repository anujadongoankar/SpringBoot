package com.inrhythm.supermarket.controller;

import com.inrhythm.supermarket.Application;
import com.inrhythm.supermarket.model.Product;
import com.inrhythm.supermarket.repository.ProductRepository;
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

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductControllerItTest {

    @LocalServerPort
    private int port;

    @Autowired
    private ProductRepository productRepository;

    TestRestTemplate restTemplate = new TestRestTemplate();

    HttpHeaders headers = new HttpHeaders();

    @Before
    public void setup() {
        productRepository.deleteAll();
    }

    @Test
    public void itShouldRetrieveProducts() {

        //Given
        Product brush = new Product("Brush", "Personal Care", 5.0);
        Product ipad = new Product("ipad", "Electronics", 350.0);
        productRepository.save(brush);
        productRepository.save(ipad);

        HttpEntity<String> entity = new HttpEntity<String>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/products"),
                HttpMethod.GET, entity, String.class);

        String expected = "Body:[{\"name\":\"Brush\",\"category\":\"Personal Care\",\"price\":5.0},{\"name\":\"ipad\",\"category\":\"Electronics\",\"price\":350.0}]\n";
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());

        try {
            JSONAssert.assertEquals(expected, response.getBody(), JSONCompareMode.LENIENT);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    @Test
    public void itShouldAddProduct() {

        Product brush = new Product("Brush", "Personal Care", 5.0);

        HttpEntity<Product> entity = new HttpEntity<Product>(brush, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/products"),
                HttpMethod.POST, entity, String.class);

        String expected = "you have added Brush with category: Personal Care Price: 5.0";
        HttpStatus statusCode = response.getStatusCode();

        Assert.assertEquals(expected, response.getBody());
        Assert.assertEquals(HttpStatus.OK, statusCode);

    }


    @Test
    public void itShouldRetrieveProductByCategory() {
        String category = "Electronics";
        Product brush = new Product("Brush", "Personal Care", 5.0);
        Product ipad = new Product("ipad", "Electronics", 350.0);
        productRepository.save(brush);
        productRepository.save(ipad);

        HttpEntity<String> entity = new HttpEntity<String>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/products/" + category),
                HttpMethod.GET, entity, String.class);

        String expected = "[{\"name\":\"ipad\",\"category\":\"Electronics\",\"price\":350.0}]\n";
        try {
            JSONAssert.assertEquals(expected, response.getBody(), JSONCompareMode.LENIENT);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());

    }


    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
}
