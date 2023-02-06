package ecommerce;

public class Buyer {

    private String buyerName;
    private int buyerId;
    private int itemsPurchase;

    public Buyer(String buyerName, int buyerId, int itemsPurchase) {
        this.buyerName = buyerName;
        this.buyerId = buyerId;
        this.itemsPurchase = itemsPurchase;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public int getBuyerId() {
        return buyerId;
    }

    public int getItemsPurchase() {
        return itemsPurchase;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public void setBuyerId(int buyerId) {
        this.buyerId = buyerId;
    }

    public void setItemsPurchase(int itemsPurchase) {
        this.itemsPurchase = itemsPurchase;
    }
}
