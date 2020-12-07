package SendAndReceive.Other;

import Classes.Bases.Base;
import Classes.Bases.SendBazeOther;
import Classes.MessGetter;
import MyProtoClass.*;
import SendAndReceive.Help.InfoInWork;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class SendOther extends Thread {
    InfoInWork tread;
    public boolean end=false;
    DatagramSocket Me;
    public int MasterSocet;
    public String MasterIp;
    Base baze;
    long LastSendVec;
    SendBazeOther Chek;
    long PingTime;

    public SendOther(DatagramSocket Me, InfoInWork InInfo, Base baze, SendBazeOther inChek, int ping){
        tread = InInfo;
        this.Me=Me;
        this.baze=baze;
        Chek=inChek;
        PingTime=ping;
    }

    @Override
    public void run(){
        byte[] Mess;
        DatagramPacket sendPacket;
        SnakesProto.GameMessage OutMes;
        LastSendVec=System.currentTimeMillis()-50;
        while (!end){
            if(System.currentTimeMillis()-LastSendVec>PingTime&&!tread.SendViewerMsg) {
                try {
                    LastSendVec=System.currentTimeMillis();
                    if(tread.Head!=0) {
                        OutMes=new CreateSteerMsg().work(baze.MeId, baze.GetNum(), tread.Head);
                        Mess = OutMes.toByteArray();
                        sendPacket = new DatagramPacket(Mess, Mess.length, InetAddress.getByName(MasterIp), MasterSocet);
                        Me.send(sendPacket);
                        Chek.SteerMeg=new MessGetter(OutMes);
                        Chek.LastSendToMaster=System.currentTimeMillis();
                    }
                } catch (IOException e) {
                    end=true;
                    e.printStackTrace();
                }

            }
            if(tread.SendViewerMsg&&Chek.RoleChangeViewer==null){
                try {
                    OutMes = new CreateRoleChangeMsg().work(true,false,false, false,false, baze.MeId,baze.MasterId,baze.GetNum());
                    Chek.RoleChangeViewer=new MessGetter(OutMes);
                    Mess =OutMes.toByteArray();
                    sendPacket = new DatagramPacket(Mess, Mess.length, InetAddress.getByName(MasterIp), MasterSocet);
                    Me.send(sendPacket);
                    Chek.RoleChangeViewer.LastSend = System.currentTimeMillis();
                    Chek.LastSendToMaster = System.currentTimeMillis();
                    baze.MeId=-1;
                } catch (IOException e) {
                    end = true;
                    e.printStackTrace();
                }
            }else if(Chek.RoleChangeViewer!=null){
                if(System.currentTimeMillis() - PingTime > Chek.RoleChangeViewer.LastSend&&!Chek.RoleChangeViewer.HasAsk){
                    try {
                        OutMes = Chek.RoleChangeViewer.MyMess;
                        Mess = OutMes.toByteArray();
                        sendPacket = new DatagramPacket(Mess, Mess.length, InetAddress.getByName(MasterIp), MasterSocet);
                        Me.send(sendPacket);
                        Chek.RoleChangeViewer.LastSend = System.currentTimeMillis();
                        Chek.LastSendToMaster = System.currentTimeMillis();
                    } catch (IOException e) {
                        end = true;
                        e.printStackTrace();
                    }
                }
            }
            if(Chek.ErrorMsg!=null) {
                if (System.currentTimeMillis() - PingTime > Chek.ErrorMsg.LastSend && !Chek.ErrorMsg.HasAsk) {
                    try {
                        OutMes = Chek.ErrorMsg.MyMess;
                        Mess = OutMes.toByteArray();
                        sendPacket = new DatagramPacket(Mess, Mess.length, InetAddress.getByName(MasterIp), MasterSocet);
                        Me.send(sendPacket);
                        Chek.ErrorMsg.LastSend = System.currentTimeMillis();
                        Chek.LastSendToMaster = System.currentTimeMillis();
                    } catch (IOException e) {
                        end = true;
                        e.printStackTrace();
                    }

                }
            }
            if(System.currentTimeMillis()-PingTime> Chek.LastSendToMaster){
                try {
                    OutMes = new CreatePingMsg().work(baze.MeId, baze.GetNum());
                    Mess = OutMes.toByteArray();
                    sendPacket = new DatagramPacket(Mess, Mess.length, InetAddress.getByName(MasterIp), MasterSocet);
                    Me.send(sendPacket);
                    Chek.LastSendToMaster = System.currentTimeMillis();
                } catch (IOException e) {
                    end=true;
                    e.printStackTrace();
                }
            }
        }

    }
}
