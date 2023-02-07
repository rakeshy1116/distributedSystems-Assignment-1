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

    private ServerSocket serverSocket;

    private static long BuyeridCounter = 0;

    public static synchronized long createBuyerID()
    {
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
                        out.println(addToShoppingCart(components));
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

        public String createBuyerAccount(String[] components) {
            Database db = Database.getInstance();
            Map<Long,Buyer> buyers = db.getBuyers();
            synchronized (buyers) {
                buyers = db.getBuyers();
                for(Map.Entry<Long,Buyer> mp:buyers.entrySet()) {
                    if (mp.getValue().getBuyerName().equals(components[1])) {
                        return "Buyer Already created acc";
                    }
                }
                Long buyerID = createBuyerID();
                Buyer buyer = new Buyer(components[1],buyerID,0,components[2],false);
                buyers.put(buyerID,buyer);
                db.setBuyers(buyers);
            }
            return "Buyer Account created";
        }

        public String loginBuyer(String[] components) {
            Database db = Database.getInstance();
            Map<Long,Buyer> buyers = db.getBuyers();
            synchronized (buyers) {
                boolean flag = false;
                Buyer buyerInstance = null;
                for(Map.Entry<Long,Buyer> mp:buyers.entrySet()) {
                    if (mp.getValue().getBuyerName().equals(components[1])) {
                        flag=true;
                        buyerInstance = mp.getValue();
                    }
                }
                if(!flag) return "No Buyer found.. create account";
                else
                {
                    if(buyerInstance.getPassword().equals(components[2])){
                        buyerInstance.setLoggedin(true);
                        return "Correct Password.. Logged in";
                    }
                    else {
                        return "Wrong Password";
                    }
                }
            }

        }

        public String logoutBuyer(String[] components) {
            Database db = Database.getInstance();
            Map<Long,Buyer> buyers = db.getBuyers();
            synchronized (buyers) {
                buyers = db.getBuyers();
                Buyer buyerInstance = null;
                for(Map.Entry<Long,Buyer> mp:buyers.entrySet()) {
                    if (mp.getValue().getBuyerName().equals(components[1])) {
                        buyerInstance = mp.getValue();
                        buyerInstance.setLoggedin(false);
                        break;
                    }
                }
                }
            return "Logged out.. Log in back";
        }

        public String addToShoppingCart(String[] components) {
            Database db = Database.getInstance();
            Map<Long,Item> items = db.getItems();
            Map<Long,Seller> sellers = db.getSellers();
            Item currentItem = null;
            synchronized (items) {
                items = db.getItems();
                Long reqItemId = Long.parseLong(components[1]);
                if (!items.containsKey(reqItemId)) {
                    return "No item found with itemId " + String.valueOf(items.size()) + String.valueOf(sellers.size());
                } else {
                    currentItem = items.get(reqItemId);
                }
            }
            Map<Long,List<Item>> shoppingCart = db.getShoppingCart();
            synchronized (shoppingCart) {
                if(!shoppingCart.containsKey(Long.parseLong(components[3])))
                {
                    shoppingCart.put(Long.parseLong(components[3]),new ArrayList<>());
                }

                currentItem.setItemQuantity(Integer.parseInt(components[2]));
                ArrayList<Item> temp = new ArrayList<>();
                temp.add(currentItem);
                shoppingCart.put(Long.parseLong(components[3]),temp);
                db.setShoppingCart(shoppingCart);
            }

            return "Logged out.. Log in back";
        }





    }

    public static void main(String[] args) {
        BuyerServer server =  new BuyerServer();
        server.start(6666);
    }

}