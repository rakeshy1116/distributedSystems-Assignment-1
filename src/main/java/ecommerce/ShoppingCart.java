package ecommerce;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import java.util.List;

@DynamoDBTable(tableName="ShoppingCart")
public class ShoppingCart {
    private Long buyerId;

    private List<Item> userItems;

    public ShoppingCart(Long buyerId, List<Item> userItems) {
        this.buyerId = buyerId;
        this.userItems = userItems;
    }

    public ShoppingCart() {
    }

    public ShoppingCart(Long buyerId) {
        this.buyerId = buyerId;
    }
    @DynamoDBHashKey(attributeName="buyerId")
    public Long getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(Long buyerId) {
        this.buyerId = buyerId;
    }

    public List<Item> getUserItems() {
        return userItems;
    }

    public void setUserItems(List<Item> userItems) {
        this.userItems = userItems;
    }
}
