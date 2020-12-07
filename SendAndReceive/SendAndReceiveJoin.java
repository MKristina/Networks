package SendAndReceive;

import Classes.Bases.Base;
import MyProtoClass.CreateJoinMsg;
import MyProtoClass.SnakesProto;
import View.Window.ReCreateWindow;
import com.google.protobuf.InvalidProtocolBufferException;

import java.io.IOException;
import java.net.*;

public class SendAndReceiveJoin {
    public int ToWho;

    public SnakesProto.GameMessage work(ReCreateWindow BaseRc, SnakesProto.GamePlayers playersList, InetAddress IP, Base base){
        SnakesProto.GameMessage message=new CreateJoinMsg().work(BaseRc.BaseFW.base.Name,BaseRc.BaseFW.base.GetNum());
        ToWho=0;
        for(SnakesProto.GamePlayer one: playersList.getPlayersList()){
            if(one.getRole()== SnakesProto.NodeRole.MASTER){
                ToWho=one.getPort();
                break;
            }
        }
        try {
            if(base.MySocket!=null) {
                if (!base.MySocket.isClosed()) {
                    base.MySocket.close();
                }
            }
            base.MySocket= new DatagramSocket(base.Socket);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        byte[] forSend= message.toByteArray();
        byte[] forget=new byte[2048];
        DatagramPacket b=new DatagramPacket(forSend,forSend.length, IP,ToWho);
        boolean HaveAny=false;
        try {
            base.MySocket.send(b);
            TimerToDontParentSend t=new TimerToDontParentSend(base.MySocket);
            b=new DatagramPacket(forget,forget.length, IP,ToWho);
            base.MySocket.receive(b);
            t.interrupt();
            base.MySocket.close();
            HaveAny=true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(HaveAny) {
            byte[] lya = new byte[b.getLength()];
            for (int i =0; i < b.getLength(); i++){
                lya[i] = b.getData()[i];
            }
            try {
                return  SnakesProto.GameMessage.parseFrom(lya);
            } catch (InvalidProtocolBufferException e) {
                return null;
            }
        }
        return null;
    }
}
