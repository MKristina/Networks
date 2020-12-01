package Main;

import Master.*;
import Slave.*;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.Timer;

public class ChatTree {

    private Thread senderS;
    private Thread recvS;
    private Thread receiverM;
    private TreeNode node;

    public void chatStart() {
        receiverM = new Thread(new RecvMaster(node));
        receiverM.start();
        senderS = new Thread(new SendSlave(node));
        senderS.start();
        recvS = new Thread(new RecvSlave(node));
        recvS.start();
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new PingMaster(node),7000,7000);
        timer.scheduleAtFixedRate(new MessageBufferMaster(node),10000,15000);
    }

    public void chatStop() {
        receiverM.interrupt();
        senderS.interrupt();
        recvS.interrupt();
    }

    public ChatTree(TreeNode node) {
        this.node = node;
    }
}