package ecommerce;

import java.util.*;

public class Database {

    private Map<Long,Seller> sellers ;
    private Map<Long,Item> items;
    private Map<Long,Buyer> buyers;
    private Map<Long,List<Item>> shoppingCart;

    private static Database single_instance = null;

    public static Database getInstance()
    {
        if (single_instance == null)
            single_instance = new Database(Collections.synchronizedMap(new HashMap<>()),
                    Collections.synchronizedMap(new HashMap<>()),
                    Collections.synchronizedMap(new HashMap<>()),
                    Collections.synchronizedMap(new HashMap<>()));

        return single_instance;
    }

    public Database(Map<Long,Seller> sellers, Map<Long,Item>  items, Map<Long,Buyer> buyers, Map<Long,List<Item>>  shoppingCart) {
        this.sellers = sellers;
        this.items = items;
        this.buyers = buyers;
        this.shoppingCart = shoppingCart;
    }

    public Map<Long, Seller> getSellers() {
        return sellers;
    }

    public void setSellers(Map<Long, Seller> sellers) {
        this.sellers = sellers;
    }

    public Map<Long, Item> getItems() {
        return items;
    }

    public void setItems(Map<Long, Item> items) {
        this.items = items;
    }

    public Map<Long, Buyer> getBuyers() {
        return buyers;
    }

    public void setBuyers(Map<Long, Buyer> buyers) {
        this.buyers = buyers;
    }

    public Map<Long, List<Item>> getShoppingCart() {
        return shoppingCart;
    }

    public void setShoppingCart(Map<Long, List<Item>> shoppingCart) {
        this.shoppingCart = shoppingCart;
    }

    public static Database getSingle_instance() {
        return single_instance;
    }

    public static void setSingle_instance(Database single_instance) {
        Database.single_instance = single_instance;
    }
}
