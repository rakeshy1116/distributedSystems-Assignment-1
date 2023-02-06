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
                    String[] components = inputLine.split(" ");
                    if(components[0].equals("1"))
                    {
                        out.println(createSellerAccount(components));
                    }
                    else if(components[0].equals("2"))
                    {
                        out.println(LoginSeller(components));
                    }
                    else if(components[0].equals("3"))
                    {
                        out.println(LogoutSeller(components));
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
        }

        public String createSellerAccount(String[] components) {
            Database db = Database.getInstance();
            List<Seller> sellers = db.getSellers();
            synchronized (sellers) {
                for(int i=0;i<sellers.size();i++)
                {
                    if(sellers.get(i).getSellerName().equals(components[1]))
                        return "Seller Already created acc";
                }
                int id = sellers.size();
                Seller seller = new Seller(components[1],id+1,new ArrayList<>(List.of(0,0)),0,components[2]);
                sellers.add(seller);
                db.setSellers(sellers);
            }
            return "Seller Account created";
        }

        public String LoginSeller(String[] components) {
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

        public String LogoutSeller(String[] components) {
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

    }

    public static void main(String[] args) {
        SellerServer server =  new SellerServer();
        server.start(5555);
    }

}