public class Client {
    public static void main(String[] args) {
        String host = "localhost";
        int port = 1234;
        //new Thread(new MenuAndPlay()).start();
        
        try  {
            ConnectionManager connectionManager = new ConnectionManager(host, port);
            System.err.println("ligou ao server " +host+ " "+ port);

            new Thread(new MenuAndPlay(connectionManager)).start();

        } catch (Exception e) {
            System.err.println("nao ligou server " + host+" "+ port);
            e.printStackTrace();
            System.exit(0);
        }
    }
}