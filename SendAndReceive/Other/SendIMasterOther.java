package SendAndReceive.Other;

import Classes.Bases.Base;
import Classes.InfoAboutUser;
import MyProtoClass.CreateRoleChangeMsg;
import MyProtoClass.SnakesProto;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class SendIMasterOther {
    public SendIMasterOther(InfoAboutUser us, DatagramSocket Me, Base baze){
        byte[] Mess=null;
        DatagramPacket sendPacket;
        SnakesProto.GameMessage OutMes;
        while (us!=null){
            if(us.id!= baze.MeId) {
                try {
                    Mess = new CreateRoleChangeMsg().work(false, false, false, true,false, baze.MeId, us.id, baze.GetNum()).toByteArray();
                    sendPacket = new DatagramPacket(Mess, Mess.length, InetAddress.getByName(us.IP), us.Socket);
                    Me.send(sendPacket);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            us=us.next;
        }
    }
}
