package ecommerce;

import com.amazonaws.services.dynamodbv2.datamodeling.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class BuyerServer {

    private ServerSocket serverSocket;

    private static long BuyeridCounter = 0;

    public static synchronized long createBuyerID() {
        return BuyeridCounter++;
    }


    public void start(int port) {
        try {
//            sellers = Collections.synchronizedList(new ArrayList());
//            items = Collections.synchronizedMap(new HashMap());
            serverSocket = new ServerSocket(port);
            while (true)
                new EchoClientHandler(serverSocket.accept()).start();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            stop();
        }

    }

    public void stop() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static class EchoClientHandler extends Thread {
        private Socket clientSocket;
        private PrintWriter out;
        private BufferedReader in;

        public EchoClientHandler(Socket socket) {
            this.clientSocket = socket;
        }


        public void run() {
            try {
                final long startTime = System.currentTimeMillis();




                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    //  sleep(5000);
                    String[] components = inputLine.split(" ");
                    if (components[0].equals("1")) {
                        out.println(createBuyerAccount(components));
                    } else if (components[0].equals("2")) {
                        out.println(loginBuyer(components));
                    } else if (components[0].equals("3")) {
                        out.println(logoutBuyer(components));
                    } else if (components[0].equals("4")) {
                        out.println(addToShoppingCart(components));
                    } else if (components[0].equals("5")) {
                        out.println(removeFromShoppingCart(components));
                    } else if (components[0].equals("6")) {
                        out.println(clearShoppingCart(components));
                    }
                    else if (components[0].equals("7")) {
                        out.println(displayShoppingCart(components));
                    }
                    else if (components[0].equals("8")) {
                        out.println(feedBackSeller(components));
                    }
                    else if (components[0].equals("9")) {
                        out.println(sellerRating(components));
                    }
                    else if (components[0].equals("10")) {
                        out.println(searchItems(components));
                    }
                    else if (components[0].equals("11")) {
                        out.println(purchaseHistory(components));
                    }
                    else if (".".equals(inputLine)) {
                        final long endTime = System.currentTimeMillis();
                        out.println("Seller Server execution time: " + (endTime - startTime));
                        break;
                    }
                }
                in.close();
                out.close();
                clientSocket.close();

            } catch (IOException e) {
            }
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
        }

        public synchronized String createBuyerAccount(String[] components) {
            DynamoDBMapper db = DynamoDBSample.getInstance().getMapper();
            DynamoDBQueryExpression<Buyer> query = new DynamoDBQueryExpression<>();
//            PaginatedQueryList<Buyer> list = db.query(Buyer.class, query);
//
            DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();

// Change to your model class
            PaginatedScanList<Buyer> list = db.scan(Buyer.class, scanExpression);

            Iterator<Buyer> iter = list.iterator();

// Check the count and iterate the list and perform as desired.

            while (iter.hasNext()) {
                Buyer currentUser = iter.next();
                if (currentUser.getBuyerName().equals(components[1])) {
                    return "Already Created Account";

                }
            }
            Long buyerID = createBuyerID();
            Buyer buyer = new Buyer(components[1], buyerID, new ArrayList<>(), components[2], false);
            ;
            db.save(buyer);
            return "Buyer Account Created with sellerId: " + String.valueOf(buyerID);
        }

        public synchronized String loginBuyer(String[] components) {
            DynamoDBMapper db = DynamoDBSample.getInstance().getMapper();

            DynamoDBQueryExpression<Buyer> query = new DynamoDBQueryExpression<>();
            query.setHashKeyValues(new Buyer(Long.parseLong(components[1])));

            PaginatedQueryList<Buyer> list = db.query(Buyer.class, query);

            Iterator<Buyer> iter = list.iterator();
            if (!iter.hasNext()) return "No buyer found.. create account";
            else {
                Buyer currentUser = iter.next();
                if (currentUser.getPassword().equals(components[2])) {
                    currentUser.setLoggedin(true);
                    db.save(currentUser);
                    return "Correct Password.. Logged in";
                } else {
                    return "Wrong Password";
                }
            }

        }


        public synchronized String logoutBuyer(String[] components) {
            DynamoDBMapper db = DynamoDBSample.getInstance().getMapper();
            DynamoDBQueryExpression<Buyer> query = new DynamoDBQueryExpression<>();
            query.setHashKeyValues(new Buyer(Long.parseLong(components[1])));
            PaginatedQueryList<Buyer> list = db.query(Buyer.class, query);

            Iterator<Buyer> iter = list.iterator();
            while (iter.hasNext()) {
                Buyer currentUser = iter.next();
                currentUser.setLoggedin(false);
                db.save(currentUser);
                return "Logged out.. Log in back";
            }
            return "No user";
        }


        public synchronized String addToShoppingCart(String[] components) {
            DynamoDBMapper db = DynamoDBSample.getInstance().getMapper();

            DynamoDBQueryExpression<Item> query1 = new DynamoDBQueryExpression<>();
            query1.setHashKeyValues(new Item(Long.parseLong(components[2])));
            PaginatedQueryList<Item> list1 = db.query(Item.class, query1);
            Iterator<Item> iter1 = list1.iterator();
            Item currentItem = null;
            while (iter1.hasNext()) {
                currentItem = iter1.next();
            }
            currentItem.setItemQuantity(Integer.parseInt(components[3]));

            DynamoDBQueryExpression<ShoppingCart> query = new DynamoDBQueryExpression<>();
            query.setHashKeyValues(new ShoppingCart(Long.parseLong(components[1])));
            PaginatedQueryList<ShoppingCart> list = db.query(ShoppingCart.class, query);
            Iterator<ShoppingCart> iter = list.iterator();
            List<Item> currentCart = new ArrayList<>();
            while (iter.hasNext()) {
                ShoppingCart shoppingCart = iter.next();
                currentCart = shoppingCart.getUserItems();
            }
            List<Item> updatedCart = new ArrayList<>();
            boolean match = false;
            for (int i = 0; i < currentCart.size(); i++) {
                if (currentCart.get(i).getItemId() != Long.parseLong(components[2])) {
                    updatedCart.add(currentCart.get(i));
                } else {
                    Item currentItem1 = currentCart.get(i);
                    currentItem1.setItemQuantity(currentItem1.getItemQuantity() + Integer.parseInt(components[3]));
                    updatedCart.add(currentItem1);
                    match = true;
                }
            }
            if (!match)
                updatedCart.add(currentItem);
            ShoppingCart shoppingCart = new ShoppingCart(Long.parseLong(components[1]), updatedCart);
            db.save(shoppingCart);
            return "Added " + components[3] + " quantities of item with item id " + components[2] + " to shopping cart";
        }

        public synchronized String removeFromShoppingCart(String[] components) {
            DynamoDBMapper db = DynamoDBSample.getInstance().getMapper();

            DynamoDBQueryExpression<ShoppingCart> query = new DynamoDBQueryExpression<>();
            query.setHashKeyValues(new ShoppingCart(Long.parseLong(components[1])));
            PaginatedQueryList<ShoppingCart> list = db.query(ShoppingCart.class, query);
            Iterator<ShoppingCart> iter = list.iterator();
            List<Item> currentCart = new ArrayList<>();
            while (iter.hasNext()) {
                ShoppingCart shoppingCart = iter.next();
                currentCart = shoppingCart.getUserItems();
            }
            List<Item> updatedCart = new ArrayList<>();
            for (int i = 0; i < currentCart.size(); i++) {
                if (currentCart.get(i).getItemId() != Long.parseLong(components[2])) {
                    updatedCart.add(currentCart.get(i));
                } else {
                    if (currentCart.get(i).getItemQuantity() > Integer.parseInt(components[3])) {
                        Item currentItem = currentCart.get(i);
                        currentItem.setItemQuantity(currentCart.get(i).getItemQuantity() - Integer.parseInt(components[3]));
                        updatedCart.add(currentItem);
                    }
                }
            }
            ShoppingCart shoppingCart = new ShoppingCart(Long.parseLong(components[1]), updatedCart);
            db.save(shoppingCart);
            return "Removed " + components[3] + " quantities of item with item id " + components[2] + " from shopping cart";
        }



    public synchronized String clearShoppingCart(String[] components) {
        DynamoDBMapper db = DynamoDBSample.getInstance().getMapper();
        DynamoDBQueryExpression<ShoppingCart> query = new DynamoDBQueryExpression<>();
        query.setHashKeyValues(new ShoppingCart(Long.parseLong(components[1])));
        PaginatedQueryList<ShoppingCart> list = db.query(ShoppingCart.class, query);
        Iterator<ShoppingCart> iter = list.iterator();
        if (!iter.hasNext()) return "buyerId " + components[1] + " doesn't have shopping cart";
        ShoppingCart shoppingCart = null;
        while (iter.hasNext()) {
            shoppingCart = iter.next();
        }
        db.delete(shoppingCart);

        return "Shopping cart cleared for buyer id: " + components[1];
    }

    public synchronized String displayShoppingCart(String[] components) {
            DynamoDBMapper db = DynamoDBSample.getInstance().getMapper();
            DynamoDBQueryExpression<ShoppingCart> query = new DynamoDBQueryExpression<>();
            query.setHashKeyValues(new ShoppingCart(Long.parseLong(components[1])));
            PaginatedQueryList<ShoppingCart> list = db.query(ShoppingCart.class, query);
            Iterator<ShoppingCart> iter = list.iterator();
            if (!iter.hasNext()) return "buyerId " + components[1] + " doesn't have shopping cart";
            StringBuilder ans = new StringBuilder();
            ShoppingCart shoppingCart = null;
            while (iter.hasNext()) {
                shoppingCart = iter.next();
            }
            List<Item> currentItems = shoppingCart.getUserItems();
            for(int i=0;i<currentItems.size();i++)
            {
                ans.append(" Item Name: " + currentItems.get(i).getItemName() + ", its quantity: " + currentItems.get(i).getItemQuantity());
                ans.append("\n");
            }
            return "Shopping cart for buyer id: " + components[1] + " " + ans.toString();
        }

        public synchronized String sellerRating(String[] components) { //specify name in the query as well
            DynamoDBMapper db = DynamoDBSample.getInstance().getMapper();
            DynamoDBQueryExpression<Seller> query = new DynamoDBQueryExpression<>();
            query.setHashKeyValues(new Seller(Long.parseLong(components[1])));
            PaginatedQueryList<Seller> list = db.query(Seller.class, query);
            Iterator<Seller> iter = list.iterator();
            double rating = 0;
            if(!iter.hasNext()) return "No seller found.. provide correct id";
            else {
                Seller currentUser = iter.next();
                List<Integer> feedback = currentUser.getFeedback();
                if((feedback.get(0)+feedback.get(1))!=0)
                    rating = (double) (feedback.get(0)-feedback.get(1))/(double)(feedback.get(0)+feedback.get(1));

            }
            return "Seller Rating is: " + String.valueOf(rating);
        }
        public synchronized String feedBackSeller(String[] components) { //specify name in the query as well
            DynamoDBMapper db = DynamoDBSample.getInstance().getMapper();
            DynamoDBQueryExpression<Item> query = new DynamoDBQueryExpression<>();
            query.setHashKeyValues(new Item(Long.parseLong(components[1])));
            PaginatedQueryList<Item> list = db.query(Item.class, query);
            Iterator<Item> iter = list.iterator();
            Item currentItem = null;
            if(!iter.hasNext()) return "No item found.. provide correct id";
            else {
                currentItem = iter.next();
            }
            Long sellerId = currentItem.getSellerId();
            DynamoDBQueryExpression<Seller> query1 = new DynamoDBQueryExpression<>();
            query1.setHashKeyValues(new Seller(sellerId));
            PaginatedQueryList<Seller> list1 = db.query(Seller.class, query1);
            Iterator<Seller> iter1 = list1.iterator();
            Seller currentSeller = null;
            if(iter1.hasNext()) {
                currentSeller = iter1.next();
            }
            List<Integer> feedback = currentSeller.getFeedback();
            if(Integer.parseInt(components[2])>0)
            {
                int current = feedback.get(0);
                feedback.set(0,current+1);
            }
            else
            {
                int current = feedback.get(1);
                feedback.set(1,current+1);
            }
            currentSeller.setFeedback(feedback);
            db.save(currentSeller);
            return "feedback given for seller: " + String.valueOf(currentSeller.getSellerId());
        }

        public synchronized String searchItems(String[] components) {
            return "searched Items";
        }
        public synchronized String purchaseHistory(String[] components) {
            DynamoDBMapper db = DynamoDBSample.getInstance().getMapper();
            DynamoDBQueryExpression<Buyer> query1 = new DynamoDBQueryExpression<>();
            query1.setHashKeyValues(new Buyer(Long.parseLong(components[1])));
            PaginatedQueryList<Buyer> list1 = db.query(Buyer.class, query1);
            Iterator<Buyer> iter1 = list1.iterator();
            Buyer currentUser = null;
            if (!iter1.hasNext()) return "No buyer found.. create account";
            else {
                currentUser = iter1.next();
            }

            StringBuilder ans = new StringBuilder();
            List<Long> currentHistory = currentUser.getItemsPurchase();
            for(int i=0;i<currentHistory.size();i++)
            {
                ans.append(String.valueOf(currentHistory.get(i)));
                ans.append(", ");
            }
            return "your purchaseHistory is: " + ans.toString();
        }

        }

    public static void main(String[] args) {
        BuyerServer server =  new BuyerServer();
        server.start(6667);
    }

}