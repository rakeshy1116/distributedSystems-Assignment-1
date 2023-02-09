package ecommerce;

// import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
// import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
// import com.amazonaws.services.dynamodbv2.datamodeling.*;
// import com.fasterxml.jackson.databind.ObjectMapper;
// import com.google.gson.Gson;
// import ecommerce.Seller;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
// import java.util.ArrayList;
// import java.util.Iterator;
// import java.util.List;
// import java.util.Map;

public class SellerServerFrontEnd {

    private static long SelleridCounter = 0;
    private static long ItemidCounter = 0;
    private ServerSocket serverSocket;
    private static Client client1;

    public static synchronized long createSellerID()
    {
        return SelleridCounter++;
    }

    public static synchronized long createItemID()
    {
        return ItemidCounter++;
    }

    public static String convertToString(String[] comp, String delimiter){
        StringBuilder sb = new StringBuilder();
        for(String s: comp){
            sb.append(s).append(delimiter);
        }
        return sb.substring(0, sb.length()-1);
    }


    public static void main(String[] args) {
        client1 = new Client();
        client1.startConnection(args[0], 7777);
        SellerServerFrontEnd server =  new SellerServerFrontEnd();
        server.start(7776);
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
                        final long endTime = System.currentTimeMillis();
                        out.println("Seller Server execution start time: " + startTime + " end time: " + endTime);

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
            String comp = convertToString(components, " ");
            String msg = client1.sendMessage(comp);
            return msg;
        }

        public synchronized String loginSeller(String[] components) {
            String comp = convertToString(components, " ");
            String msg = client1.sendMessage(comp);
            return msg;

        }



        public synchronized String logoutSeller(String[] components) {
            String comp = convertToString(components, " ");
            String msg = client1.sendMessage(comp);
            return msg;
         }

        public synchronized String sellerRating(String[] components) { //specify name in the query as well
            String comp = convertToString(components, " ");
            String msg = client1.sendMessage(comp);
            return msg;
         }

        public synchronized String putItem(String[] components) { //specify name in the query as well
            String comp = convertToString(components, " ");
            String msg = client1.sendMessage(comp);
            return msg;
         }

        public synchronized String updateItemSalePrice(String[] components) { //assuming same seller is doing the update
            String comp = convertToString(components, " ");
            String msg = client1.sendMessage(comp);
            return msg;
        }

        public synchronized String removeItem(String[] components) { //assuming same seller is doing the update
            String comp = convertToString(components, " ");
            String msg = client1.sendMessage(comp);
            return msg;
         }

        public synchronized String displayItemsOnSale(String[] components) { //assuming same seller is doing the update
            String comp = convertToString(components, " ");
            String msg = client1.sendMessage(comp);
            return msg;
        }




    }

}