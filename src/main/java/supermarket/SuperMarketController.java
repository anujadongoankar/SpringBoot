package supermarket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SuperMarketController {

    @Autowired
    ProductRepository productRepository;


    @RequestMapping(method = RequestMethod.POST, value = "/products")
    public String products(@RequestBody Product product) {
        productRepository.save(product);
        return "you have added " + product.getName() + " with category: " + product.getCategory() + " Price: " + product.getPrice();
    }


    @RequestMapping(method = RequestMethod.GET, value = "/products")
    public Iterable<Product> products() {
        return productRepository.findAll();
    }


    @RequestMapping(method=RequestMethod.GET, value="/products/{category}")
    public List<Product> getByCategory(@PathVariable String category) {
        return productRepository.findByCategory(category);
    }
}
