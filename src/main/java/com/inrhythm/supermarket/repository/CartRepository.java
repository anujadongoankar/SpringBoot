package com.inrhythm.supermarket.repository;

import com.inrhythm.supermarket.model.Cart;
import org.springframework.data.repository.CrudRepository;

public interface CartRepository extends CrudRepository<Cart, String> {

    @Override
    Cart findOne(String id);

    @Override
    void deleteAll();
}
