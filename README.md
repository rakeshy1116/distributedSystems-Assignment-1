<h2> Distributed Systems Assignment-1 </h2>

<h3>Team Members:</h3>
1. Rakesh Chowdary Yarlagadda
2. Gautam Gottipatti

<h3>System design:</h3>


Our system has 6 components: 
1. Client side Seller 
2. Client side Buyer
3. Server side Seller
4. Server side Buyer
5. Product and 
6. Customer Databases 

We have used dynamodb for storing item, buyer, seller and shopping cart data. We have implemented the tcp communication using socket programming in java. for performance measures we have deployed our front end and backend of seller and buyer servers in 4 different virtual machines in google cloud with 4 different ip addresses and port numbers.

<h3>Assumptions:</h3>

1. While creating account for buyer and seller, we are checking if already an account is present with given username, and creating the account only if it is not present.
2. For login, we are checking if user has already registered or not, and also checking if passwords matches with the one given at time of registration.
3. Seller rating is calculated based on average of feedbacks (+1 thumbs up and -1 thumbs down).
4. For creating item, unique id is generated in the serverprogram and along with other attributes, item is stored in database.
5. For updating item price and remove item methods, first check is done for given itemid, price is updated only if it is present and only given no of quantities are reduced
7. In display item functions, all the items with sellerid equal to given seller id are displayed.
8. Created a table for shopping cart, where buyer id is the primary key for this table and it has list of items.
9. For remove and clear shopping cart, the items list will be updated and removed to reflect the changes for the given buyer id.
10. When a buyer provides feedback, corresponding seller record will be updated.

<h4>Semantics for search function:</h4>

User will provide item category and five search keywords. then, All the items from this category will be filtered and check is done, if any of the search keywords match with the keywords given at the time of item creation. Only those items which have atleast one match will be returned.


<h3>What works</h3>

<h4>Seller functions:</h4>

1. Create an account
2. Login
3. Logout
4. Get seller rating
5. Put an item for sale
6. Change the sale price of an item
7. Remove an item from sale
8. Display items 

<h4>Buyer functions:</h4>

1. Create an account
2. Login
3. Logout
4. Search items for sale
5. Add item to the shopping cart
6. Remove item from the shopping cart
7. Clear the shopping cart
8. Display shopping cart
9. Provide feedback
10. Get seller rating

<h3> What doesn't works: </h3>

1. Get buyer purchase history (since make purchase is not implemented it returns empty output).


<h4> Performances </h4>

Refer perfomance-report.pdf file


<h4> Running the Application </h4>

1. Create jar file using mvn package.

2. Run the seller server and buyer server using following commands:

``` java -cp ecommerce.jar ecommerce.SellerServer ```
``` java -cp ecommerce.jar ecommerce.BuyerServer ```

3. Similarly for clients

``` java -cp ecommerce.jar ecommerce.SellerClient ipaddress_of_sellerServer seller1.txt ```

