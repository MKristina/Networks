package SendAndReceive.Master;

import Classes.Bases.Base;
import Classes.Bases.DMess;
import Classes.InfoAboutUser;
import Field.GameFieldBaze;
import Field.Snakes.NewSnake;
import Field.Snakes.Snake;
import MyProtoClass.CreateAckMsg;
import MyProtoClass.CreateErrorMsg;
import MyProtoClass.SnakesProto;
import SendAndReceive.Help.GetFieldAsc;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ReceiveMaster extends Thread{
    Base baze;
    GameFieldBaze MyFieldBaze;
    public boolean end=false;
    public DatagramSocket Me;
    DMess DSend;
    GetFieldAsc GetASC;
    public ReceiveMaster(Base INBaze, GameFieldBaze InField, DatagramSocket Me, DMess D, GetFieldAsc asc){
        baze=INBaze;
        MyFieldBaze=InField;
        this.Me=Me;
        DSend=D;
        GetASC=asc;
    }

    @Override
    public void run() {
        byte[] Mess=new byte[2048];
        DatagramPacket recievePacket=new DatagramPacket(Mess,Mess.length);
        SnakesProto.GameMessage InMes;
        SnakesProto.GameMessage SetMes;
        while (!end) {
            try {
                Me.receive(recievePacket);
                byte[] lya = new byte[recievePacket.getLength()];
                for (int i =0; i < recievePacket.getLength(); i++){
                    lya[i] = recievePacket.getData()[i];
                }
                InMes= SnakesProto.GameMessage.parseFrom(lya);
                System.out.println("HaveAny");
                if(InMes.getTypeCase()== SnakesProto.GameMessage.TypeCase.JOIN){
                    System.out.println("Vso ok");
                    if(MyFieldBaze.canPlace()) {
                        Snake one = new NewSnake(baze, MyFieldBaze.retField(), true, false).ret;
                        SendAck(InMes.getMsgSeq(), recievePacket.getAddress().getHostAddress(), one.id, recievePacket.getPort());
                        InfoAboutUser NewUser = new InfoAboutUser();
                        NewUser.id = one.id;
                        NewUser.Socket = recievePacket.getPort();
                        NewUser.IP = recievePacket.getAddress().getHostAddress();
                        NewUser.Name = InMes.getJoin().getName();
                        NewUser.LastSeen = System.currentTimeMillis();
                        NewUser.LastSend = System.currentTimeMillis();
                        baze.GetUsers().AddUser(NewUser);
                    }else {
                        SetMes=new CreateErrorMsg().work("Cant plase", baze.GetNum());
                        Mess=SetMes.toByteArray();
                        DatagramPacket Send=new DatagramPacket(Mess, Mess.length,recievePacket.getAddress(),recievePacket.getPort());
                        Me.send(Send);
                    }

                }else if(InMes.getTypeCase()==SnakesProto.GameMessage.TypeCase.STEER){
                    int want = 0;
                    if(baze.GetUsers().GetByIPAndSocket(recievePacket.getPort(),recievePacket.getAddress().getHostAddress())!=null) {
                        int WhoId = baze.GetUsers().GetByIPAndSocket(recievePacket.getPort(), recievePacket.getAddress().getHostAddress()).id;
                        switch (InMes.getSteer().getDirection()) {
                            case UP -> {
                                want = -2;
                            }
                            case DOWN -> {
                                want = 2;
                            }
                            case LEFT -> {
                                want = -1;
                            }
                            case RIGHT -> {
                                want = 1;
                            }
                        }
                        if (baze.GetSnakes().GetById(WhoId) != null) {
                            if (want != -baze.GetSnakes().GetById(WhoId).Head.vector) {
                                baze.GetSnakes().GetById(WhoId).Head.vector = want;
                            }
                            baze.GetUsers().ISeeAndSend(WhoId);
                            SendAck(InMes.getMsgSeq(), recievePacket.getAddress().getHostAddress(), WhoId, recievePacket.getPort());
                        }
                    }

                }else if(InMes.getTypeCase()==SnakesProto.GameMessage.TypeCase.PING){
                    if(baze.GetUsers().GetByIPAndSocket(recievePacket.getPort(),recievePacket.getAddress().getHostAddress())!=null) {
                        int WhoId = baze.GetUsers().GetByIPAndSocket(recievePacket.getPort(), recievePacket.getAddress().getHostAddress()).id;
                        baze.GetUsers().ISeeAndSend(WhoId);
                        SendAck(InMes.getMsgSeq(), recievePacket.getAddress().getHostAddress(), WhoId, recievePacket.getPort());
                    }
                }else if(InMes.getTypeCase()==SnakesProto.GameMessage.TypeCase.ROLE_CHANGE) {
                    if(InMes.getRoleChange().hasSenderRole()){
                        if(InMes.getRoleChange().getSenderRole()== SnakesProto.NodeRole.VIEWER){
                            baze.GetUsers().ToViewer(InMes.getSenderId(),baze.GetSnakes());
                            SendAck(InMes.getMsgSeq(), recievePacket.getAddress().getHostAddress(), InMes.getSenderId(), recievePacket.getPort());

                        }
                    }
                }else if (InMes.getTypeCase()==SnakesProto.GameMessage.TypeCase.ACK) {
                    InfoAboutUser one=baze.GetUsers().GetSwapToView();
                    while (one!=null){
                        if(one.SwapMes!=null) {
                            if (one.SwapMes.getMsgSeq() == InMes.getMsgSeq() && one.id != baze.MeId) {
                                baze.GetUsers().GetAscViewMes(one);
                            }
                        }
                        one=one.next;
                    }
                    if(DSend.Mess!=null){
                        if(InMes.getMsgSeq()==DSend.Mess.MyMess.getMsgSeq()) {
                            baze.GetUsers().SetNext(InMes.getSenderId());
                        }
                    }
                    if(GetASC.MyMess!=null){
                        if(GetASC.MyMess.getMsgSeq()==InMes.getMsgSeq()){
                            GetASC.SetAsc(recievePacket.getAddress().getHostAddress(),recievePacket.getPort());
                        }
                    }
                    baze.GetUsers().ISee(InMes.getSenderId());
                }
            } catch (IOException ignored) {
                end=true;
            }


        }


    }
    void SendAck(long Seq, String IP, int towho,int Socket){
        byte[] MessO;
        DatagramPacket sendPacket;
        SnakesProto.GameMessage OutMes;
        try {
            OutMes=new CreateAckMsg().work(baze.MeId, towho, Seq);
            MessO = OutMes.toByteArray();
            sendPacket = new DatagramPacket(MessO, MessO.length, InetAddress.getByName(IP), Socket);
            Me.send(sendPacket);
        } catch (IOException e) {
            end=true;
            e.printStackTrace();
        }
    }

}
