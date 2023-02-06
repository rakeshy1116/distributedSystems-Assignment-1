package ecommerce;

import java.util.*;

public class Database {

    private List<Seller> sellers ;
    private Map<Integer,Item> items;
    private List<Buyer> buyers;
    private List<List<Integer>> shoppingCart;

    private static Database single_instance = null;

    public static Database getInstance()
    {
        if (single_instance == null)
            single_instance = new Database(Collections.synchronizedList(new ArrayList()),
                    Collections.synchronizedMap(new HashMap()),
                    Collections.synchronizedList(new ArrayList()),
                    Collections.synchronizedList(new ArrayList()));

        return single_instance;
    }

    public Database(List<Seller> sellers, Map<Integer, Item> items, List<Buyer> buyers, List<List<Integer>> shoppingCart) {
        this.sellers = sellers;
        this.items = items;
        this.buyers = buyers;
        this.shoppingCart = shoppingCart;
    }

    public List<Seller> getSellers() {
        return sellers;
    }

    public void setSellers(List<Seller> sellers) {
        this.sellers = sellers;
    }

    public Map<Integer, Item> getItems() {
        return items;
    }

    public void setItems(Map<Integer, Item> items) {
        this.items = items;
    }

    public List<Buyer> getBuyers() {
        return buyers;
    }

    public void setBuyers(List<Buyer> buyers) {
        this.buyers = buyers;
    }

    public List<List<Integer>> getShoppingCart() {
        return shoppingCart;
    }

    public void setShoppingCart(List<List<Integer>> shoppingCart) {
        this.shoppingCart = shoppingCart;
    }
}
