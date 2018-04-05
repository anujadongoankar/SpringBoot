package com.inrhythm.supermarket.controller;

import com.inrhythm.supermarket.model.Product;
import com.inrhythm.supermarket.repository.ProductRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(MockitoJUnitRunner.class)
public class ProductControllerTest {

    @Mock
    ProductRepository productRepository;

    @InjectMocks
    ProductController productController;

    @Test
    public void itShouldSaveProduct() {
        //given
        Product brush = new Product("Brush", "Personal Care", 5.0);

        //when
        productController.create(brush);

        //then
        Mockito.verify(productRepository, Mockito.times(1)).save(Matchers.any(Product.class));

    }

    @Test
    public void itShouldReturnAllTheProducts() {
        //given

        Product brush = new Product("Brush", "Personal Care", 5.0);
        Product ipad = new Product("ipad", "Electronics", 350.0);

        List<Product> products = new ArrayList<>();
        products.add(brush);
        products.add(ipad);

        Mockito.when(productRepository.findAll()).thenReturn(products);

        List<Product> actualProducts = productController.getProducts();

        Mockito.verify(productRepository, Mockito.times(1)).findAll();
        assertEquals(actualProducts.size(), products.size());

    }

    @Test
    public void itShouldReturnAllTheProductsByCategory() {
        //given

        String category = "Electronics";
        Product brush = new Product("Brush", "Personal Care", 5.0);
        Product ipad = new Product("ipad", category, 350.0);

        List<Product> products = new ArrayList<>();
        products.add(brush);
        products.add(ipad);

        List<Product> expectedProducts = new ArrayList<>();
        expectedProducts.add(ipad);


        Mockito.when(productRepository.findByCategory(category)).thenReturn(expectedProducts);

        List<Product> actualProducts = productController.getByCategory(category);

        Mockito.verify(productRepository, Mockito.times(1)).findByCategory(category);
        assertEquals(actualProducts.get(0).getName(), products.get(1).getName());
        assertNotEquals(actualProducts.size(), products.size());


    }


}