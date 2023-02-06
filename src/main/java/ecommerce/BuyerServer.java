package ecommerce;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BuyerServer {

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
                  //  sleep(5000);
                    String[] components = inputLine.split(" ");
                    if(components[0].equals("1")) {
                        out.println(createBuyerAccount(components));
                    }
                    else if(components[0].equals("2")) {
                        out.println(loginBuyer(components));
                    }
                    else if(components[0].equals("3")) {
                        out.println(logoutBuyer(components));
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
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
        }

        public synchronized String createSellerAccount(String[] components) {
            Database db = Database.getInstance();
            List<Seller> sellers = db.getSellers();
//            synchronized (sellers) {
                for(int i=0;i<sellers.size();i++)
                {
                    if(sellers.get(i).getSellerName().equals(components[1]))
                        return "Seller Already created acc";
                }
                int id = sellers.size();
                Seller seller = new Seller(components[1],id+1,new ArrayList<>(List.of(0,0)),0,components[2]);
                sellers.add(seller);
                db.setSellers(sellers);
//            }
            return "Seller Account created";
        }

        public String loginSeller(String[] components) {
            Database db = Database.getInstance();
            List<Seller> sellers = db.getSellers();
            synchronized (sellers) {
                boolean flag = false;
                Seller sellerInstance = null;
                for(int i=0;i<sellers.size();i++)
                {
                    if(sellers.get(i).getSellerName().equals(components[1]))
                    {
                        flag=true;
                        sellerInstance = sellers.get(i);
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
            List<Seller> sellers = db.getSellers();
            synchronized (sellers) {

                Seller sellerInstance = null;
                for (int i = 0; i < sellers.size(); i++) {
                    if (sellers.get(i).getSellerName().equals(components[1])) {
                        sellerInstance = sellers.get(i);
                        sellerInstance.setLoggedin(false);
                        break;
                    }
                }


                }
            return "Logged out.. Log in back";
        }

        public String sellerRating(String[] components) { //specify name in the query as well
            Database db = Database.getInstance();
            List<Seller> sellers = db.getSellers();
            int rating = 0;
            synchronized (sellers) {


                for (int i = 0; i < sellers.size(); i++) {
                    if (sellers.get(i).getSellerName().equals(components[1])) {
                        List<Integer> feedback = sellers.get(i).getFeedback();
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
            List<Seller> sellers = db.getSellers();
            int sellerId = 0;
            synchronized (sellers) {
                for (int i = 0; i < sellers.size(); i++) {
                    if (sellers.get(i).getSellerName().equals(components[10])) {
                        sellerId = sellers.get(i).getSellerId();
                        break;
                    }
                }
            }
            Map<Integer,Item> items = db.getItems();
            int itemId = items.size()+1;
            synchronized (items) {
                String itemName = components[1];
                int itemCategory = Integer.parseInt(components[2]);

                List<String> keywords = new ArrayList<>();
                for (int i = 3; i <= 7; i++) {
                    keywords.add(components[i]);
                }
                boolean condition = Boolean.parseBoolean(components[8]);
                double salePrice = Double.parseDouble(components[9]);
                items.put(itemId, new Item(itemName, itemCategory, itemId, keywords, condition, salePrice, sellerId));
                db.setItems(items);
            }
            return "placed Item for sale with ItemId: " + String.valueOf(itemId);
        }

        public String updateItemSalePrice(String[] components) { //assuming same seller is doing the update
            Database db = Database.getInstance();
            Map<Integer,Item> items = db.getItems();
            synchronized (items) {
               if(items.containsKey(Integer.parseInt(components[1])))
               {
                   Item currentItem = items.get(Integer.parseInt(components[1]));
                   currentItem.setSalePrice(Double.parseDouble(components[2]));
                   items.put(Integer.parseInt(components[1]),currentItem);
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
            Map<Integer,Item> items = db.getItems();
            synchronized (items) {
                if(items.containsKey(Integer.parseInt(components[1])))
                {
                    //Item currentItem = items.get(Integer.parseInt(components[1]));
                    items.remove(Integer.parseInt(components[1]));
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
            Map<Integer,Item> items = db.getItems();
            List<Seller> sellers = db.getSellers();
            int sellerId = 0;
            synchronized (sellers) {
                for (int i = 0; i < sellers.size(); i++) {
                    if (sellers.get(i).getSellerName().equals(components[1])) {
                        sellerId = sellers.get(i).getSellerId();
                        break;
                    }
                }
            }
            StringBuilder ans = new StringBuilder();
            synchronized (items) {

                for(Map.Entry<Integer,Item> mp: items.entrySet()) {
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
        BuyerServer server =  new BuyerServer();
        server.start(5555);
    }

}