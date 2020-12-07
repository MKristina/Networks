package SendAndReceive.Other;

import Classes.Bases.Base;
import Classes.Bases.SendBazeOther;
import MyProtoClass.CreateAckMsg;
import MyProtoClass.SnakesProto;
import SendAndReceive.Help.InfoInWork;
import View.Types.ErrorWindow.OpenError;
import com.google.protobuf.InvalidProtocolBufferException;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ReceiveOther extends Thread {
    InfoInWork trade;
    DatagramSocket Me;
    public boolean end=false;
    SendBazeOther Chek;
    public String MasterIp;
    public int MasterSocKet;
    Base baze;

    public ReceiveOther(InfoInWork InInfo, DatagramSocket InMe, SendBazeOther inChek, Base baze){
        Me=InMe;
        trade=InInfo;
        Chek=inChek;
        this.baze=baze;
    }


    @Override
    public void run() {
        byte[] Mess=new byte[2048];
        DatagramPacket recievePacket=new DatagramPacket(Mess,Mess.length);
        SnakesProto.GameMessage InMes=null;
        while (!end) {
            try {
                Me.receive(recievePacket);
            } catch (IOException e) {
                end=true;
                e.printStackTrace();
            }
            byte[] lya = new byte[recievePacket.getLength()];
            for (int i =0; i < recievePacket.getLength(); i++){
                lya[i] = recievePacket.getData()[i];
            }
            try {
                InMes = SnakesProto.GameMessage.parseFrom(lya);
            } catch (InvalidProtocolBufferException e) {
                e.printStackTrace();
            }
            if(InMes.getTypeCase()== SnakesProto.GameMessage.TypeCase.STATE) {
                if (trade.Field != null) {
                    if (trade.Field.getState().getState().getStateOrder() < InMes.getState().getState().getStateOrder()) {
                        trade.Field = InMes;
                        SendAck( InMes.getMsgSeq(), baze.MasterId,MasterIp,MasterSocKet);
                    }
                }
                else{
                    trade.Field = InMes;
                    SendAck( InMes.getMsgSeq(), baze.MasterId,MasterIp,MasterSocKet);
                }
                Chek.LastSenMaster = System.currentTimeMillis();
                Chek.LastSendToMaster = System.currentTimeMillis();
            }else if(InMes.getTypeCase()== SnakesProto.GameMessage.TypeCase.ACK){
                if(Chek.ErrorMsg!=null) {
                    if (InMes.getMsgSeq() == Chek.ErrorMsg.MyMess.getMsgSeq()) {
                        Chek.ErrorMsg.HasAsk = true;
                    }
                }else if(Chek.SteerMeg!=null){
                    if(InMes.getMsgSeq()==Chek.SteerMeg.MyMess.getMsgSeq()) {
                        Chek.SteerMeg.HasAsk = true;
                    }
                }else if(Chek.RoleChangeViewer!=null){
                    if(InMes.getMsgSeq()==Chek.RoleChangeViewer.MyMess.getMsgSeq()) {
                        Chek.RoleChangeViewer.HasAsk = true;
                    }
                }
                Chek.LastSenMaster = System.currentTimeMillis();
            }else if(InMes.getTypeCase()== SnakesProto.GameMessage.TypeCase.PING){
                SendAck( InMes.getMsgSeq(),baze.MasterId,MasterIp,MasterSocKet);
                Chek.LastSenMaster = System.currentTimeMillis();
                Chek.LastSendToMaster = System.currentTimeMillis();
            }else if(InMes.getTypeCase()== SnakesProto.GameMessage.TypeCase.ERROR){
                SendAck( InMes.getMsgSeq(),baze.MasterId,MasterIp,MasterSocKet);
                new OpenError().work(InMes.getError().getErrorMessage());
                Chek.LastSenMaster = System.currentTimeMillis();
                Chek.LastSendToMaster = System.currentTimeMillis();
            }else if(InMes.getTypeCase()== SnakesProto.GameMessage.TypeCase.ROLE_CHANGE){
                if(InMes.getRoleChange().getReceiverRole()== SnakesProto.NodeRole.DEPUTY){
                    baze.INext=true;
                    SendAck( InMes.getMsgSeq(), baze.MasterId,MasterIp,MasterSocKet);
                }else if(InMes.getRoleChange().getReceiverRole()== SnakesProto.NodeRole.MASTER){
                    baze.IsMaster=true;
                    baze.MasterId=-1;
                    SendAck( InMes.getMsgSeq(),baze.MasterId,MasterIp,MasterSocKet);
                }else if(InMes.getRoleChange().getSenderRole()== SnakesProto.NodeRole.MASTER){
                    MasterSocKet=recievePacket.getPort();
                    baze.MasterId=InMes.getSenderId();
                    MasterIp=recievePacket.getAddress().getHostAddress();
                    SendAck( InMes.getMsgSeq(), baze.MasterId,MasterIp,MasterSocKet);
                }else if(InMes.getRoleChange().getReceiverRole()== SnakesProto.NodeRole.VIEWER){
                    trade.SendViewerMsg=true;
                    SendAck( InMes.getMsgSeq(),baze.MasterId,MasterIp,MasterSocKet);
                }
                Chek.LastSenMaster=System.currentTimeMillis();
                Chek.LastSendToMaster=System.currentTimeMillis();
            }
        }

    }

    void SendAck(long Seq, int towho, String IP, int Socket){
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
