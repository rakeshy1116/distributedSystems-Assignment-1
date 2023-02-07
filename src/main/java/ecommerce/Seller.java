package ecommerce;


import java.util.List;

public class Seller {
    private String sellerName;
    private Long sellerId;

    private List<Integer> feedback; //first int will be thumbs up, second will be thumbs down
    private int itemsSold;
    private String password;

    public boolean isLoggedin() {
        return isLoggedin;
    }

    public void setLoggedin(boolean loggedin) {
        isLoggedin = loggedin;
    }

    private boolean isLoggedin;

    public Seller(String sellerName, Long sellerId, List<Integer> feedback, int itemsSold, String password) {
        this.sellerName = sellerName;
        this.sellerId = sellerId;
        this.feedback = feedback;
        this.itemsSold = itemsSold;
        this.password = password;
        this.isLoggedin = false;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public Long getSellerId() {
        return sellerId;
    }

    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }

    public List<Integer> getFeedback() {
        return feedback;
    }

    public void setFeedback(List<Integer> feedback) {
        this.feedback = feedback;
    }

    public int getItemsSold() {
        return itemsSold;
    }

    public void setItemsSold(int itemsSold) {
        this.itemsSold = itemsSold;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
