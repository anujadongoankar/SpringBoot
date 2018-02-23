package supermarket;

import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;


public class Cart {

    @Id
    private String id;

    private List<Item> items = new ArrayList<>();

    public Cart(){}

    public Cart(List<Item> items){

        this.items = items;
    }

    public List<Item> getItems() {
        return items;
    }

    public String getId() {
        return id;
    }
}
