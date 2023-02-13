package ecommerce;


import java.io.*;
import java.net.Socket;

public class BuyerClient {
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

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


        public static void main(String[] args) throws IOException {
            BuyerClient client1 = new BuyerClient();
            client1.startConnection("127.0.0.1", 6666);
            final long startTime = System.currentTimeMillis();
            File file = new File(args[0]);
            BufferedReader br = new BufferedReader(new FileReader(file));
            String st;
            String finalMessage = "";
            while ((st = br.readLine()) != null) {
                String msg1 = client1.sendMessage(st);
                finalMessage = msg1;
                System.out.println(msg1);
            }
            final long endTime = System.currentTimeMillis();
            System.out.println(finalMessage);
            System.out.println("Buyer client execution time: " + (endTime - startTime));
            String filename = String.valueOf(startTime)+"_buyer"+"_"+String.valueOf(args[1]);
            try {
                FileWriter myWriter = new FileWriter(filename);
                myWriter.write(finalMessage + "\n" + "Buyer client execution time: " + (endTime - startTime));
                myWriter.close();
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }

        }
}
