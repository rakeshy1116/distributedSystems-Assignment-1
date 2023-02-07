package ecommerce;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import java.util.ArrayList;
import java.util.List;

@DynamoDBTable(tableName="Buyer")
public class Buyer {

    private String buyerName;
    private Long buyerId;
    private List<Long> itemsPurchase;

    private String password;

    private boolean isLoggedin;

    public Buyer(String buyerName, Long buyerId, List<Long> itemsPurchase, String password, Boolean isLoggedin) {
        this.buyerName = buyerName;
        this.buyerId = buyerId;
        this.itemsPurchase = new ArrayList<>();
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

    @DynamoDBHashKey(attributeName="buyerId")
    public Long getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(Long buyerId) {
        this.buyerId = buyerId;
    }

    public List<Long> getItemsPurchase() {
        return itemsPurchase;
    }

    public void setItemsPurchase(List<Long> itemsPurchase) {
        this.itemsPurchase = itemsPurchase;
    }

    public String getPassword() {
        return password;
    }

    public Buyer(Long buyerId) {
        this.buyerId = buyerId;
    }

    public Buyer() {
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
