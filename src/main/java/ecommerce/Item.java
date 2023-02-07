package ecommerce;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import java.util.List;

@DynamoDBTable(tableName="Item")
public class Item {
    private String itemName;
    private int itemCategory;
    private Long itemId;
    private List<String> keywords;
    private boolean condition; // true for new, false for used
    private double salePrice;

    private Long sellerId;

    private int itemQuantity;

    public int getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(int itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public Item(String itemName, int itemCategory, Long itemId, List<String> keywords, boolean condition, double salePrice, Long sellerId, int itemQuantity) {
        this.itemName = itemName;
        this.itemCategory = itemCategory;
        this.itemId = itemId;
        this.keywords = keywords;
        this.condition = condition;
        this.salePrice = salePrice;
        this.sellerId = sellerId;
        this.itemQuantity = itemQuantity;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setItemCategory(int itemCategory) {
        this.itemCategory = itemCategory;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    public void setCondition(boolean condition) {
        this.condition = condition;
    }

    public void setSalePrice(double salePrice) {
        this.salePrice = salePrice;
    }

    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }

    public String getItemName() {
        return itemName;
    }

    public int getItemCategory() {
        return itemCategory;
    }

    public Item(Long itemId) {
        this.itemId = itemId;
    }

    public Item() {
    }

    @DynamoDBHashKey(attributeName="itemId")
    public Long getItemId() {
        return itemId;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public boolean isCondition() {
        return condition;
    }

    public double getSalePrice() {
        return salePrice;
    }

    public Long getSellerId() {
        return sellerId;
    }
}
