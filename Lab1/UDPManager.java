import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.*;

class UDPManager {

    private byte[] buf;
    private UDPMulticastSocket socket;
    private long timeout;
    private HashMap<InetAddress, Long> history = new HashMap<>();

    UDPManager(UDPMulticastSocket multicastSocket, long receiveTimeout, int bufSize) throws IOException {
        socket = multicastSocket;
        buf = new byte[bufSize];
        timeout = receiveTimeout;
    }

    void start() throws IOException {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            DatagramPacket datagramPacket = new DatagramPacket(buf, buf.length, socket.getGroup(), socket.getPort());
            socket.getSocket().send(datagramPacket);
            long start = currentTime();
            do {
                DatagramPacket recDatagramPacket = new DatagramPacket(new byte[buf.length], buf.length);
                try {
                    socket.getSocket().receive(recDatagramPacket);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                updateWith(recDatagramPacket.getAddress());
            } while(currentTime() - start < timeout);
            checkForDeath();
            if (System.in.available()!=0){
                String s = scanner.nextLine();
                if (s.equals("stop")) break;
            }
        }
    }

    public void stop() {
        socket.close();
    }

    private void updateWith(InetAddress ip) {
        if (!history.containsKey(ip)) {
            history.put(ip, currentTime());
            printChanges();
        }
    }

    private void checkForDeath() {
        Set<Map.Entry<InetAddress, Long>> historyEntry = history.entrySet();
        boolean hasChanges = false;
        long time = currentTime();
        for(Map.Entry<InetAddress, Long> copy : historyEntry) {
            if(isDead(time - copy.getValue())) {
                history.remove(copy.getKey());
                hasChanges = true;
            }
        }
        if (hasChanges) {
            printChanges();
        }
    }

    private void printChanges() {
        System.out.printf("\n\n\nUpdated %d alive copy's:\n", history.size());
        for(InetAddress key: history.keySet()) {
            String ip = "ip: " + key.getHostAddress() + "\n";
            System.out.print(ip);
        }
    }

    private boolean isDead(long time) {
        return (time > timeout * 2);
    }
    private long currentTime() {
        return System.currentTimeMillis();
    }
}


