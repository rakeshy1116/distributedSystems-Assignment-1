package ecommerce;

// import com.amazonaws.services.dynamodbv2.datamodeling.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
// import java.util.ArrayList;
// import java.util.Iterator;
// import java.util.List;
// import java.util.Map;

public class BuyerServerFrontEnd {

    private ServerSocket serverSocket;

    private static long BuyeridCounter = 0;

    private static Client client1;

    public static String convertToString(String[] comp, String delimiter){
        StringBuilder sb = new StringBuilder();
        for(String s: comp){
            sb.append(s).append(delimiter);
        }
        return sb.substring(0, sb.length()-1);
    }

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
                        out.println("Buyer Server startTime time: " + startTime + " end time: " + endTime);
                        break;
                    } else
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
            String comp = convertToString(components, " ");
            String msg = client1.sendMessage(comp);
            return msg;

        }

        public String loginBuyer(String[] components) {
            String comp = convertToString(components, " ");
            String msg = client1.sendMessage(comp);
            return msg;

        }


        public String logoutBuyer(String[] components) {
            String comp = convertToString(components, " ");
            String msg = client1.sendMessage(comp);
            return msg;

        }


        public String addToShoppingCart(String[] components) {
            String comp = convertToString(components, " ");
            String msg = client1.sendMessage(comp);
            return msg;
        }

        public String removeFromShoppingCart(String[] components) {
            String comp = convertToString(components, " ");
            String msg = client1.sendMessage(comp);
            return msg;
        }



    public String clearShoppingCart(String[] components) {
        String comp = convertToString(components, " ");
        String msg = client1.sendMessage(comp);
        return msg;
     }

    public String displayShoppingCart(String[] components) {
            String comp = convertToString(components, " ");
            String msg = client1.sendMessage(comp);
            return msg;
        }

        public String sellerRating(String[] components) { //specify name in the query as well
            String comp = convertToString(components, " ");
            String msg = client1.sendMessage(comp);
            return msg;

        }
        public String feedBackSeller(String[] components) { //specify name in the query as well
            String comp = convertToString(components, " ");
            String msg = client1.sendMessage(comp);
            return msg;
           }

        public String searchItems(String[] components) {
            String comp = convertToString(components, " ");
            String msg = client1.sendMessage(comp);
            return msg;
            // return "searched Items";
        }
        public String purchaseHistory(String[] components) {
            String comp = convertToString(components, " ");
            String msg = client1.sendMessage(comp);
            return msg;
         }

        }

    public static void main(String[] args) {
        client1 = new Client();
        client1.startConnection(args[0], 6667);
        BuyerServerFrontEnd server =  new BuyerServerFrontEnd();
        server.start(6666);
    }

}