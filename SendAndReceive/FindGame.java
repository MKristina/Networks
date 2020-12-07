package SendAndReceive;

import MyProtoClass.SnakesProto;
import SendAndReceive.Help.ForFind;
import com.google.protobuf.InvalidProtocolBufferException;

import java.io.IOException;
import java.net.*;

public class FindGame {
    MulticastSocket MyMSocket;
    SocketAddress MyMAddress;
    public FindGame() throws IOException {
        MyMAddress= new InetSocketAddress("239.192.0.4", 9192);
        MyMSocket=new MulticastSocket(9192);
        NetworkInterface ThisI = NetworkInterface.getByInetAddress(InetAddress.getLocalHost());
        MyMSocket.setSoTimeout(1500);
        MyMSocket.joinGroup(MyMAddress, ThisI);
    }

    public boolean receive(ForFind now){
        byte[] Mess=new byte[2048];
        DatagramPacket receivePacket=new DatagramPacket(Mess,Mess.length);
        try {
            System.out.println("Here");
            MyMSocket.receive(receivePacket);
            System.out.println("Here");
        } catch (IOException e) {
            return false;
        }
        byte[] arr = new byte[receivePacket.getLength()];
        for (int i =0; i <receivePacket.getLength(); i++){
            arr[i] = receivePacket.getData()[i];
        }
        try {
            now.mes= SnakesProto.GameMessage.parseFrom(arr);
        } catch (InvalidProtocolBufferException e) {
            System.out.println("Error");
            e.printStackTrace();
        }
        now.IP=receivePacket.getAddress();
        now.Socket=receivePacket.getPort();
        return true;
    }
}
