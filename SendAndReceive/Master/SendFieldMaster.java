package SendAndReceive.Master;

import Classes.Bases.Base;
import Classes.InfoAboutUser;
import Field.GameFieldBaze;
import MyProtoClass.CreateStateMsg;
import MyProtoClass.SnakesProto;
import SendAndReceive.Help.GetFieldAsc;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class SendFieldMaster extends Thread{
    Base baze;
    GameFieldBaze MyFieldBaze;
    public boolean end=false;
    public DatagramSocket Me=null;
    GetFieldAsc GetASC;
    int PingTime;
    public SendFieldMaster(Base INBaze, GameFieldBaze InField, DatagramSocket Me, int time, GetFieldAsc asc){
        baze=INBaze;
        MyFieldBaze=InField;
        this.Me=Me;
        GetASC=asc;
        PingTime=time;
    }

    @Override
    public void run() {
        byte[] Mess;
        DatagramPacket sendPacket;
        SnakesProto.GameMessage OutMes;
        OutMes=new CreateStateMsg().work(baze,MyFieldBaze, baze.GetNum(), baze.iter);
        Mess=OutMes.toByteArray();
        InfoAboutUser one=baze.GetUsers().GetCopy();
        InfoAboutUser CopyOne=one;
        int k=0;
        while (one!=null) {
            k++;
            if (!one.IP.equals(" ")) {
                try {
                    sendPacket = new DatagramPacket(Mess, Mess.length, InetAddress.getByName(one.IP), one.Socket);
                    Me.send(sendPacket);
                    one.LastSend = System.currentTimeMillis();
                } catch (IOException e) {
                    end = true;
                    e.printStackTrace();
                }
            }
            one = one.next;
        }
        if(k>1){
            GetASC.HaveAscFrom=new boolean[k];
            GetASC.IP=new String[k];
            GetASC.LastSend=new long[k];
            GetASC.Socket=new int[k];
            GetASC.Id=new int[k];
            GetASC.MyMess=OutMes;
            one=CopyOne;
            for(int i=0;i<k;i++){
                GetASC.HaveAscFrom[i]=false;
                GetASC.IP[i]= ""+one.IP;
                GetASC.LastSend[i]=System.currentTimeMillis();
                GetASC.Socket[i]= one.Socket;
                GetASC.Id[i]=one.id;
                one=one.next;
            }
            while (!end){
                for(int i=0;i<k;i++){
                    if(!GetASC.IP[i].equals(" ")&&!GetASC.HaveAscFrom[i]&&System.currentTimeMillis()-GetASC.LastSend[i]>PingTime){
                        try {
                            sendPacket = new DatagramPacket(Mess, Mess.length, InetAddress.getByName(GetASC.IP[i]), GetASC.Socket[i]);
                            Me.send(sendPacket);
                            GetASC.LastSend[i] = System.currentTimeMillis();
                            baze.GetUsers().ISend( GetASC.Id[i]);
                        } catch (IOException e) {
                            end = true;
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }
}
