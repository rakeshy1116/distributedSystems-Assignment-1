package ecommerce;

public class Buyer {

    private String buyerName;
    private Long buyerId;
    private int itemsPurchase;

    private String password;

    private boolean isLoggedin;

    public Buyer(String buyerName, Long buyerId, int itemsPurchase, String password, Boolean isLoggedin) {
        this.buyerName = buyerName;
        this.buyerId = buyerId;
        this.itemsPurchase = 0;
        this.password = password;
        this.isLoggedin = false;
    }

    public boolean isLoggedin() {
        return isLoggedin;
    }

    public void setLoggedin(boolean loggedin) {
        isLoggedin = loggedin;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public Long getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(Long buyerId) {
        this.buyerId = buyerId;
    }

    public int getItemsPurchase() {
        return itemsPurchase;
    }

    public void setItemsPurchase(int itemsPurchase) {
        this.itemsPurchase = itemsPurchase;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
