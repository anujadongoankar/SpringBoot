package com.inrhythm.supermarket.controller;

import com.inrhythm.supermarket.model.Product;
import com.inrhythm.supermarket.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductController {

    @Autowired
    ProductRepository productRepository;


    @RequestMapping(method = RequestMethod.POST, value = "/products")
    public String create(@RequestBody Product product) {
        productRepository.save(product);
        return "you have added " + product.getName() + " with category: " + product.getCategory() + " Price: " + product.getPrice();
    }


    @RequestMapping(method = RequestMethod.GET, value = "/products")
    public List<Product> getProducts() {

        return productRepository.findAll();
    }


    @RequestMapping(method = RequestMethod.GET, value = "/products/{category}")
    public List<Product> getByCategory(@PathVariable String category) {
        return productRepository.findByCategory(category);
    }
}
