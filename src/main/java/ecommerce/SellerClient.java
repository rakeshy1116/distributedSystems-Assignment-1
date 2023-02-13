package ecommerce;


import java.io.*;
import java.net.Socket;

public class SellerClient {
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

        public static void main(String[] args) throws IOException {
            SellerClient client1 = new SellerClient();
            client1.startConnection("34.172.214.182", 7776);
            final long startTime = System.currentTimeMillis();


            File file = new File(args[0]);
            BufferedReader br = new BufferedReader(new FileReader(file));
            String st;
            String finalMessage = "";
            while ((st = br.readLine()) != null) {
                String msg1 = client1.sendMessage(st);
                finalMessage=msg1;
                System.out.println(msg1);
            }
            final long endTime = System.currentTimeMillis();
            System.out.println(finalMessage);
            System.out.println("Total execution time: " + (endTime - startTime));
            String filename = String.valueOf(startTime)+"_seller"+"_"+String.valueOf(args[1]);
            try {
                FileWriter myWriter = new FileWriter(filename);
                myWriter.write(finalMessage +"\n"+ "Seller client execution time: " + (endTime - startTime));
                myWriter.close();
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }

    }

    public void startConnection(String ip, int port) {
        try {
            clientSocket = new Socket(ip, port);
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String sendMessage(String msg) {
        try {
            out.println(msg);
            return in.readLine();
        } catch (Exception e) {
            return null;
        }
    }

    public void stopConnection() {
        try {
            in.close();
            out.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
