import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            UDPMulticastSocket socket = new UDPMulticastSocket("230.0.0.0", 4446, 5000);
            UDPManager manager = new UDPManager(socket, 5000, 256);
            manager.start();
            manager.stop();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
