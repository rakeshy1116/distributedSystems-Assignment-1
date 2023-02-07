package ecommerce;


public class Connector {
    public static void main(String[] args) {
        Database db = Database.getInstance();
        SellerServer server =  new SellerServer();
        server.start(5557);
        BuyerServer server1 = new BuyerServer();
        server1.start(5558);
    }
}
