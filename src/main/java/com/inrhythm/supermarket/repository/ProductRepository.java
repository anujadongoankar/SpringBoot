package com.inrhythm.supermarket.repository;


import com.inrhythm.supermarket.model.Product;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProductRepository extends CrudRepository<Product, String> {

    List<Product> findByCategory(String category);

}