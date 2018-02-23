package supermarket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class SuperMarketController {

    @Autowired
    ProductRepository productRepository;


    @RequestMapping(method = RequestMethod.POST, value = "/products")
    public String addNewProduct(@RequestBody Product product) {
        productRepository.save(product);
        return "you have added " + product.getName() + " with category: " + product.getCategory() + " Price: " + product.getPrice();
    }

}
