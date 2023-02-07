package ecommerce;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import ecommerce.Seller;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SellerServer {

    private static long SelleridCounter = 0;
    private static long ItemidCounter = 0;
    private ServerSocket serverSocket;

    public static synchronized long createSellerID()
    {
        return SelleridCounter++;
    }

    public static synchronized long createItemID()
    {
        return ItemidCounter++;
    }

    public static void main(String[] args) {
        SellerServer server =  new SellerServer();
        server.start(7777);
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

                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
//                    sleep(2000);
                    String[] components = inputLine.split(" ");
                    if(components[0].equals("1")) {
                        out.println(createSellerAccount(components));
                    }
                    else if(components[0].equals("2")) {
                        out.println(loginSeller(components));
                    }
                    else if(components[0].equals("3")) {
                        out.println(logoutSeller(components));
                    }
                    else if(components[0].equals("4")) {
                        out.println(sellerRating(components));
                    }
                    else if(components[0].equals("5")) {
                        out.println(putItem(components));
                    }
                    else if(components[0].equals("6")) {
                        out.println(updateItemSalePrice(components));
                    }
                    else if(components[0].equals("7")) {
                        out.println(removeItem(components));
                    }
                    else if(components[0].equals("8")) {
                        out.println(displayItemsOnSale(components));
                    }
                    else if (".".equals(inputLine)) {
                        out.println("bye");
                        break;
                    }
                    else
                    out.println(inputLine);
                }
                in.close();
                out.close();
                clientSocket.close();

            } catch (IOException e) {
            }


//             catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
        }


        public synchronized String createSellerAccount(String[] components) {
            DynamoDBMapper db = DynamoDBSample.getInstance().getMapper();
            DynamoDBQueryExpression<Seller> query = new DynamoDBQueryExpression<>();
//            PaginatedQueryList<Seller> list = db.query(Seller.class, query);
//
            DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();

// Change to your model class
            PaginatedScanList<Seller> list = db.scan(Seller.class, scanExpression);

            Iterator<Seller> iter = list.iterator();

// Check the count and iterate the list and perform as desired.

            while (iter.hasNext()) {
                Seller currentUser = iter.next();
                if (currentUser.getSellerName().equals(components[1])) {
                    return "Already Created Account";

                }
            }
            Long sellerId = createSellerID();
            Seller seller = new Seller(components[1], sellerId, new ArrayList<>(List.of(0, 0)), 0, components[2]);
            db.save(seller);
            return "Seller Account Created with sellerId: " + String.valueOf(sellerId);
        }

        public synchronized String loginSeller(String[] components) {
            DynamoDBMapper db = DynamoDBSample.getInstance().getMapper();

            DynamoDBQueryExpression<Seller> query = new DynamoDBQueryExpression<>();
            query.setHashKeyValues(new Seller(Long.parseLong(components[1])));

            PaginatedQueryList<Seller> list = db.query(Seller.class, query);

            Iterator<Seller> iter = list.iterator();
            if(!iter.hasNext()) return "No seller found.. create account";
            else {
                Seller currentUser = iter.next();
                if(currentUser.getPassword().equals(components[2])){
                    currentUser.setLoggedin(true);
                    db.save(currentUser);
                    return "Correct Password.. Logged in";
                }
                else {
                    return "Wrong Password";
                }
            }

        }



        public synchronized String logoutSeller(String[] components) {
            DynamoDBMapper db = DynamoDBSample.getInstance().getMapper();
            DynamoDBQueryExpression<Seller> query = new DynamoDBQueryExpression<>();
            query.setHashKeyValues(new Seller(Long.parseLong(components[1])));
            PaginatedQueryList<Seller> list = db.query(Seller.class, query);

            Iterator<Seller> iter = list.iterator();
            while(iter.hasNext()) {
                Seller currentUser = iter.next();
                    currentUser.setLoggedin(false);
                    db.save(currentUser);
                   return "Logged out.. Log in back";
                }
                    return "No user";
        }

        public synchronized String sellerRating(String[] components) { //specify name in the query as well
            DynamoDBMapper db = DynamoDBSample.getInstance().getMapper();
            DynamoDBQueryExpression<Seller> query = new DynamoDBQueryExpression<>();
            query.setHashKeyValues(new Seller(Long.parseLong(components[1])));
            PaginatedQueryList<Seller> list = db.query(Seller.class, query);
            Iterator<Seller> iter = list.iterator();
            int rating = 0;
            if(!iter.hasNext()) return "No seller found.. create account";
            else {
                Seller currentUser = iter.next();
                List<Integer> feedback = currentUser.getFeedback();
                if((feedback.get(0)+feedback.get(1))!=0)
                    rating = (feedback.get(0)-feedback.get(1))/(feedback.get(0)+feedback.get(1));

                }
            return "Seller Rating is: " + String.valueOf(rating);
        }

        public synchronized String putItem(String[] components) { //specify name in the query as well
            DynamoDBMapper db = DynamoDBSample.getInstance().getMapper();
            Long sellerId = Long.parseLong(components[10]);
            Long itemId = createItemID();
            String itemName = components[1];
            int itemCategory = Integer.parseInt(components[2]);
            List<String> keywords = new ArrayList<>();
            for (int i = 3; i <= 7; i++) {
                    keywords.add(components[i]);
                }
            boolean condition = Boolean.parseBoolean(components[8]);
            double salePrice = Double.parseDouble(components[9]);
            int itemQuantity = Integer.parseInt(components[11]);
            Item currentItem = new Item(itemName, itemCategory, itemId, keywords, condition, salePrice, sellerId,itemQuantity);
            db.save(currentItem);

            return "placed Item for sale with ItemId: " + String.valueOf(itemId);
        }

        public synchronized String updateItemSalePrice(String[] components) { //assuming same seller is doing the update
            DynamoDBMapper db = DynamoDBSample.getInstance().getMapper();
            DynamoDBQueryExpression<Item> query = new DynamoDBQueryExpression<>();
            query.setHashKeyValues(new Item(Long.parseLong(components[1])));
            PaginatedQueryList<Item> list = db.query(Item.class, query);
            Iterator<Item> iter = list.iterator();
            if(!iter.hasNext()) return "No item found with given ItemId";
            else {
                Item currentItem = iter.next();
                currentItem.setSalePrice(Double.parseDouble(components[2]));
                db.save(currentItem);
            }
            return "Item price updated to: " + components[2];
        }

        public synchronized String removeItem(String[] components) { //assuming same seller is doing the update
            DynamoDBMapper db = DynamoDBSample.getInstance().getMapper();
            DynamoDBQueryExpression<Item> query = new DynamoDBQueryExpression<>();
            query.setHashKeyValues(new Item(Long.parseLong(components[1])));
            PaginatedQueryList<Item> list = db.query(Item.class, query);
            Iterator<Item> iter = list.iterator();
            if(!iter.hasNext()) return "No item found with given ItemId";
            else {
                Item currentItem = iter.next();
                if(currentItem.getItemQuantity()<=Integer.parseInt(components[2]))
                db.delete(currentItem);
                else
                {
                    currentItem.setItemQuantity(currentItem.getItemQuantity()-Integer.parseInt(components[2]));
                    db.save(currentItem);
                }
            }
            return components[2] + " quantities of Item with following ItemID: " + components[1] + " are removed.";
        }

        public synchronized String displayItemsOnSale(String[] components) { //assuming same seller is doing the update
            DynamoDBMapper db = DynamoDBSample.getInstance().getMapper();
            DynamoDBQueryExpression<Item> query = new DynamoDBQueryExpression<>();
            DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();

            PaginatedScanList<Item> list = db.scan(Item.class, scanExpression);

            Iterator<Item> iter = list.iterator();

            Long sellerId = Long.parseLong(components[1]);
            StringBuilder ans = new StringBuilder();
            while(iter.hasNext())
            {
                Item currentItem = iter.next();
                if(currentItem.getSellerId()==sellerId)
                {
                    ans.append(currentItem.getItemName());
                    ans.append(", ");
                }
            }
            return "Following items are for sale: " + ans;
        }




    }

}