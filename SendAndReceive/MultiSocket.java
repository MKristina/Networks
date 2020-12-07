package SendAndReceive;

import Classes.Bases.Base;
import MyProtoClass.CreateAnnouncementMsg;

import java.io.IOException;
import java.net.*;

public class MultiSocket extends Thread{
    MulticastSocket MyMSocket;
    SocketAddress MyMAddress;
    Base base;
    public boolean end= false;
    public MultiSocket(Base InBase) throws IOException {
        MyMAddress= new InetSocketAddress("239.192.0.4", 9192);
        MyMSocket=new MulticastSocket(9192);
        NetworkInterface ThisI = NetworkInterface.getByInetAddress(InetAddress.getLocalHost());
        MyMSocket.setSoTimeout(3000);
        MyMSocket.joinGroup(MyMAddress, ThisI);
        base =InBase;
    }


    @Override
    public void run() {
        CreateAnnouncementMsg a=new CreateAnnouncementMsg();

        byte[] Mess;
        DatagramPacket receivePacket;
        while (true) {

            Mess=a.work(base, base.can, base.GetNum()).toByteArray();
            receivePacket=new DatagramPacket(Mess,Mess.length,MyMAddress);
            try {
                MyMSocket.send(receivePacket);
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(end){
                return;
            }
        }
    }
}
