# distributedSystems-Assignment-1




Semantics for search function:

user will provide item category and five search keywords.

all the items from this category will be filtered and check if any of the search keywords match with the keywords given at the time of item posting(creation). Only those items which have atleast one match will be returned.


System design:


Our system has 6 components: 1. seller and 2. buyer client side,  3. server side front end and backend for both seller and buyer. we have used dynamodb for storing item, buyer, seller and shopping cart data. We have implemented the tcp communication using socket programming in java. for performance measures we have deployed our front end and backend of seller and buyer servers in 4 different vms in google cloud with 4 different ip addresses and port numbers.

Assumptions:

1. while creating account for buyer and seller, we are checking if already an account is present with given username, and creating the account only if it is not present.
2. for login, we are checking if user has already registered or not, and also checking if passwords matches with the one given at time of registration.
3. seller rating is calculated based on average of (+1 thumbs up and -1 thumbs down).
4. for creating item, unique id is generated in the program and along with other attributes, item is stored in database.
5. for updating item price, first check is done for given itemid, price is updated if it is present.
6. similarly for remove item, and only given no of quantities are reduced.
7. all the items with sellerid equal to given seller id are displayed


Buyer

1. created a table for shopping cart, where buyer id is the primary key for this table and it has list of items.
2. for remove and clear shopping cart, those items list will be updated and removed to reflect the changes.
3. when a buyer provides feedback, seller record will be updated.


What works

Seller functions:

Create an account: sets up username and password
Login: provide username and password
Logout
Get seller rating
Put an item for sale: provide all item characteristics and quantity
Change the sale price of an item: provide item id and new sale price
Remove an item from sale: provide item id and quantity
Display items currently on sale put up by this seller


Buyer functions:

Create an account: sets up username and password
Login: provide username and password
Logout
Search items for sale: provide an item category and up to five keywords
Add item to the shopping cart: provide item id and quantity
Remove item from the shopping cart: provide item id and quantity
Clear the shopping cart
Display shopping cart
Provide feedback: thumbs up or down for each item purchased, at most one feedback per purchased item
Get seller rating: provide seller id

What doesnt works:

Get buyer purchase history (since make purchase is not implemented it returns empty output).


Performances

Seller

1. 1256
2. 2065
3. 1656
4. 1492
5. 1520
6. 1644
7. 973
8. 10101

13636

throughput - 1676227107835 end time: 1676227264118
156313

Buyer

1. 1213
2. 1425
3. 2524
4. 2604
5. 1647
6. 1266
7. 1231
8. 423
9. 985
10. 5406
11. 1917

throughput - 1676227111020 end time: 1676227277875
166889