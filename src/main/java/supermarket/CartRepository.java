package supermarket;

import org.springframework.data.repository.CrudRepository;

public interface CartRepository extends CrudRepository<Cart, String> {

    @Override
    Cart findOne(String id);
}
