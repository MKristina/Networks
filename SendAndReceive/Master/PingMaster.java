package SendAndReceive.Master;

import Classes.Bases.Base;
import Classes.Bases.BaseForUser;
import Classes.Bases.DMess;
import Classes.InfoAboutUser;
import Classes.MessGetter;
import MyProtoClass.*;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class PingMaster extends Thread {
    int TimeToPing;
    int DieTime;
    BaseForUser Users;
    public boolean end=false;
    DatagramSocket Me;
    Base base;
    DMess DSend;

    public PingMaster(int InTime, BaseForUser InUs, DatagramSocket InMe, Base inBase, int DeadNode, DMess D){
        TimeToPing=InTime;
        Users=InUs;
        Me=InMe;
        base =inBase;
        DieTime=DeadNode;
        DSend=D;
    }

    @Override
    public void run(){
        byte[] Mess;
        DatagramPacket sendPacket;
        SnakesProto.GameMessage OutMes;
        base.GetUsers().ReTime();
        while (!end){
            base.GetUsers().DelByTime(base.MeId, DieTime, base.GetSnakes());
            InfoAboutUser one= base.GetUsers().GetPingList(base.MeId, TimeToPing);
            while (one!=null){
                if(one.id!= base.MeId) {
                    try {
                        OutMes = new CreatePingMsg().work(base.MeId, base.GetNum());
                        Mess = OutMes.toByteArray();
                        sendPacket = new DatagramPacket(Mess, Mess.length, InetAddress.getByName(one.IP), one.Socket);
                        Me.send(sendPacket);
                    } catch (IOException e) {
                        end = true;
                        e.printStackTrace();
                    }
                }
                one=one.next;
            }
            InfoAboutUser Next= base.GetUsers().GetNext();
            if(Next!=null&&DSend.Mess==null){
                try {
                    OutMes=new CreateRoleChangeMsg().work(false,true,false,false,false, base.MeId, Next.id, base.GetNum());
                    DSend.Mess=new MessGetter(OutMes);
                    Mess=OutMes.toByteArray();
                    sendPacket=new DatagramPacket(Mess,Mess.length, InetAddress.getByName(Next.IP),Next.Socket);
                    Me.send(sendPacket);
                } catch (IOException e) {
                    end=true;
                    e.printStackTrace();
                }

            }else if(DSend.Mess!=null){
                if(!DSend.Mess.HasAsk&& base.GetUsers().GetById(DSend.Mess.MyMess.getReceiverId()) != null && System.currentTimeMillis() - DSend.Mess.LastSend > TimeToPing) {
                    try {
                        OutMes = DSend.Mess.MyMess;
                        Mess = OutMes.toByteArray();
                        sendPacket = new DatagramPacket(Mess, Mess.length, InetAddress.getByName(base.GetUsers().GetById(DSend.Mess.MyMess.getReceiverId()).IP), base.GetUsers().GetById(DSend.Mess.MyMess.getReceiverId()).Socket);
                        Me.send(sendPacket);
                    } catch (IOException e) {
                            end = true;
                            e.printStackTrace();
                    }
                } else {
                   DSend.Mess = null;
                }
            }
            one= base.GetUsers().GetSwapToView();
            while (one!=null){
                if(one.SwapMes==null&&one.id!= base.MeId){
                    try {
                        one.SwapMes=new CreateRoleChangeMsg().work(false,false,false,false,true, base.MeId, one.id, base.GetNum() );
                        Mess=one.SwapMes.toByteArray();
                        sendPacket=new DatagramPacket(Mess,Mess.length, InetAddress.getByName(one.IP),one.Socket);
                        Me.send(sendPacket);
                        base.GetUsers().SetSwapToView(one);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else if(System.currentTimeMillis()-one.LastSendMes>TimeToPing&&!one.HasAns&&one.id!= base.MeId){
                    try {
                        Mess=one.SwapMes.toByteArray();
                        sendPacket=new DatagramPacket(Mess,Mess.length, InetAddress.getByName(one.IP),one.Socket);
                        Me.send(sendPacket);
                        base.GetUsers().ReTimeViewMes(one);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                one=one.next;
            }
            try {
                sleep(10);
            } catch (InterruptedException e) {
                end=true;
                e.printStackTrace();
            }
        }
    }
}
