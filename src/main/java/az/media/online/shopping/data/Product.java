package az.media.online.shopping.data;

import java.util.ArrayList;
import java.util.Comparator;

public class Product {
    private int id;
    private String name;
    private String price;
    private String category;
    private String featured;
    private String image;

    public boolean check(ArrayList<String> cartList, String productId) {
        for (String id : cartList) {
            if (id.equals(productId))
                return true;
        }
        return false;
    }

    public void remove(ArrayList<String> cartList, String productId) {
        for (String id : cartList) {
            if (id.equals(productId)) {
                cartList.remove(productId);
                break;
            }
        }
    }

    public void lowToHigh(ArrayList<Product> cartList) {
        Comparator<Product> comparator = (Comparator.comparingInt(o -> Integer.parseInt(o.getPrice())));
        cartList.sort(comparator);
    }

    public void highToLow(ArrayList<Product> cartList) {
        Comparator<Product> comparator = ((o1, o2) -> Integer.parseInt(o2.getPrice()) - Integer.parseInt(o1.getPrice()));
        cartList.sort(comparator);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getFeatured() {
        return featured;
    }

    public void setFeatured(String featured) {
        this.featured = featured;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
