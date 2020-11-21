package Main;
import Master.*;

import java.net.InetSocketAddress;
import java.net.SocketException;

public class Main {

    public static void main(String[] args) throws SocketException {
        TreeNode node;
        int argsLength = args.length;
        if (argsLength != 3 && argsLength != 5) {
            System.err.println("usage: name, loss_percentage, port [, parent_address, parent_port]");
            System.exit(-1);
        }
        String name = args[0];
        int loss_percentage = Integer.parseInt(args[1]);
        int port = Integer.parseInt(args[2]);
        if (argsLength == 3) {
            node = new TreeNode(name, port, loss_percentage);
        } else {
            InetSocketAddress parent = new InetSocketAddress(args[3], Integer.parseInt(args[4]));
            node = new TreeNode(name, port, loss_percentage, parent);
        }
        var ChatTree = new ChatTree(node);
        var console = new InputMaster(node);
        try {
            ChatTree.chatStart();
            console.run();
        } finally {
            try {
                console.stop();
                ChatTree.chatStop();
            } catch (Exception e) {}
        }
    }
}