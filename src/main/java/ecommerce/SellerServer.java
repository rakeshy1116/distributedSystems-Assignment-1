package ecommerce;

import java.net.*;
import java.io.*;
import java.util.*;

public class SellerServer {

//    private static SellerServer single_instance = null;
//
//    public static SellerServer getInstance()
//    {
//        if (single_instance == null)
//            single_instance = new SellerServer();
//
//        return single_instance;
//    }
//    List<Seller> sellers ;
//    Map<Integer,Item> items;

//    public List<Seller> getSellers() {
//        return sellers;
//    }
//
//    public void setSellers(List<Seller> sellers) {
//        this.sellers = sellers;
//    }

    private static long SelleridCounter = 0;
    private static long ItemidCounter = 0;

    public static synchronized long createSellerID()
    {
        return SelleridCounter++;
    }

    public static synchronized long createItemID()
    {
        return ItemidCounter++;
    }

    private ServerSocket serverSocket;

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
                   // sleep(2000);
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

//            }
//             catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
        }

        public  String createSellerAccount(String[] components) {
            Database db = Database.getInstance();
            Map<Long,Seller> sellers = db.getSellers();
            synchronized (sellers) {
                sellers = db.getSellers();
                for(Map.Entry<Long,Seller> mp:sellers.entrySet())
                {
                    if(mp.getValue().getSellerName().equals(components[1]))
                        return "Seller Already created acc";
                }
                Long sellerId = createSellerID();
                Seller seller = new Seller(components[1],sellerId,new ArrayList<>(List.of(0,0)),0,components[2]);
                sellers.put(sellerId,seller);
                db.setSellers(sellers);
            }
            return "Seller Account created";
        }

        public String loginSeller(String[] components) {
            Database db = Database.getInstance();
            Map<Long,Seller> sellers = db.getSellers();
            synchronized (sellers) {
                sellers = db.getSellers();
                boolean flag = false;
                Seller sellerInstance = null;
                for(Map.Entry<Long,Seller> mp:sellers.entrySet())
                {
                    if(mp.getValue().getSellerName().equals(components[1]))
                    {
                        flag=true;
                        sellerInstance = mp.getValue();
                    }
                }
                if(!flag) return "No seller found.. create account";
                else
                {
                    if(sellerInstance.getPassword().equals(components[2])){
                        sellerInstance.setLoggedin(true);
                        return "Correct Password.. Logged in";
                    }
                    else {
                        return "Wrong Password";
                    }
                }
            }

        }

        public String logoutSeller(String[] components) {
            Database db = Database.getInstance();
            Map<Long,Seller> sellers = db.getSellers();
            synchronized (sellers) {
                sellers = db.getSellers();
                Seller sellerInstance = null;
                for(Map.Entry<Long,Seller> mp:sellers.entrySet())
                {
                    if(mp.getValue().getSellerName().equals(components[1]))
                    {
                        sellerInstance = mp.getValue();
                        sellerInstance.setLoggedin(false);
                        break;
                    }
                }


                }
            return "Logged out.. Log in back";
        }

        public String sellerRating(String[] components) { //specify name in the query as well
            Database db = Database.getInstance();
            Map<Long,Seller> sellers = db.getSellers();
            int rating = 0;
            synchronized (sellers) {
                for(Map.Entry<Long,Seller> mp:sellers.entrySet())
                {
                    if(mp.getValue().getSellerName().equals(components[1]))
                    {
                        List<Integer> feedback = mp.getValue().getFeedback();
                        if((feedback.get(0)+feedback.get(1))!=0)
                        rating = (feedback.get(0)-feedback.get(1))/(feedback.get(0)+feedback.get(1));

                        break;
                    }
                }

            }
            return "Seller Rating is: " + String.valueOf(rating);
        }

        public String putItem(String[] components) { //specify name in the query as well
            Database db = Database.getInstance();
            Map<Long,Seller> sellers = db.getSellers();
            Long sellerId = Long.valueOf(0);
            synchronized (sellers) {
                for(Map.Entry<Long,Seller> mp:sellers.entrySet())
                {
                    if(mp.getValue().getSellerName().equals(components[10]))
                    {
                        sellerId = mp.getValue().getSellerId();
                        break;
                    }
                }
            }
            Map<Long,Item> items = db.getItems();
            Long itemId = createItemID();
            synchronized (items) {
                items = db.getItems();
                String itemName = components[1];
                int itemCategory = Integer.parseInt(components[2]);
                List<String> keywords = new ArrayList<>();
                for (int i = 3; i <= 7; i++) {
                    keywords.add(components[i]);
                }
                boolean condition = Boolean.parseBoolean(components[8]);
                double salePrice = Double.parseDouble(components[9]);
                int itemQuantity = Integer.parseInt(components[11]);
                items.put(itemId, new Item(itemName, itemCategory, itemId, keywords, condition, salePrice, sellerId,itemQuantity));
                db.setItems(items);
            }
            return "placed Item for sale with ItemId: " + String.valueOf(itemId);
        }

        public String updateItemSalePrice(String[] components) { //assuming same seller is doing the update
            Database db = Database.getInstance();
            Map<Long,Item> items = db.getItems();
            synchronized (items) {
                items = db.getItems();
               if(items.containsKey(Long.parseLong(components[1])))
               {
                   Item currentItem = items.get(Long.parseLong(components[1]));
                   currentItem.setSalePrice(Double.parseDouble(components[2]));
                   items.put(Long.parseLong(components[1]),currentItem);
               }
               else
               {
                   return "No item found with given ItemId ";
               }
               db.setItems(items);
            }
            return "Item price updated to: " + components[2];
        }

        public String removeItem(String[] components) { //assuming same seller is doing the update
            Database db = Database.getInstance();
            Map<Long,Item> items = db.getItems();
            synchronized (items) {
                items = db.getItems();
                if(items.containsKey(Long.parseLong(components[1])))
                {
                    Item currentItem = items.get(Long.parseLong(components[1]));
                    //Item currentItem = items.get(Integer.parseInt(components[1]));
                    if(currentItem.getItemQuantity()<=Integer.parseInt(components[2]))
                    items.remove(Long.parseLong(components[1]));
                    else
                    {
                        currentItem.setItemQuantity(currentItem.getItemQuantity()-Integer.parseInt(components[2]));
                        items.put(Long.parseLong(components[1]),currentItem);
                    }

                }
                else
                {
                    return "No item found with given ItemId ";
                }
                db.setItems(items);
            }
            return "Item removed with following ItemID: " + components[1];
        }

        public String displayItemsOnSale(String[] components) { //assuming same seller is doing the update
            Database db = Database.getInstance();
            Map<Long,Item> items = db.getItems();
            Map<Long,Seller> sellers = db.getSellers();
            Long sellerId = Long.valueOf(0);
            synchronized (sellers) {
                sellers = db.getSellers();
                for(Map.Entry<Long,Seller> mp:sellers.entrySet())
                {
                    if(mp.getValue().getSellerName().equals(components[1]))
                    {
                        sellerId = mp.getValue().getSellerId();
                        break;
                    }
                }
            }
            StringBuilder ans = new StringBuilder();
            synchronized (items) {

                for(Map.Entry<Long,Item> mp: items.entrySet()) {
                    if(mp.getValue().getSellerId()==sellerId)
                    {
                        ans.append(mp.getValue().getItemName());
                        ans.append(", ");
                    }
                }
            }
            return "Following items are for sale: " + ans;
        }




    }

    public static void main(String[] args) {
        SellerServer server =  new SellerServer();
        server.start(5555);
    }

}