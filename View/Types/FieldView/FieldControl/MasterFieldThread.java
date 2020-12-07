package View.Types.FieldView.FieldControl;

import Also.KeyL;
import Classes.Bases.BaseForWindow;
import Classes.Bases.DMess;
import Classes.InfoAboutUser;
import Field.GameFieldBaze;
import Field.Snakes.Snake;
import MyProtoClass.CreateRoleChangeMsg;
import MyProtoClass.SnakesProto;
import SendAndReceive.Help.GetFieldAsc;
import SendAndReceive.Master.PingMaster;
import SendAndReceive.Master.ReceiveMaster;
import SendAndReceive.Master.SendFieldMaster;
import SendAndReceive.MultiSocket;
import View.Types.FieldView.FieldRepaint.FieldRepaint;

import java.io.IOException;
import java.net.*;

public class MasterFieldThread extends FieldParent {

    public FieldRepaint WindowField=null;
    public MasterFieldThread(BaseForWindow InBase, GameFieldBaze FieldBase) {
        super(InBase, FieldBase);
    }

    @Override
    public void run() {
        BaseFW.base.GetUsers().PrintAll();
        if(WindowField==null) {
            WindowField = new FieldRepaint(BaseFW, MyFieldBase);
        }
        boolean HaveMe=false;
        KeyL Board=null;
        try {
            if(BaseFW.base.MySocket!=null) {
                if (!BaseFW.base.MySocket.isClosed()) {
                    BaseFW.base.MySocket.close();
                }
            }
            BaseFW.base.MySocket = new DatagramSocket(BaseFW.base.Socket);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        GetFieldAsc FieldSendChek=new GetFieldAsc();
        SendFieldMaster FieldSender=new SendFieldMaster(BaseFW.base, MyFieldBase, BaseFW.base.MySocket, BaseFW.base.GetConfig().ping_delay_ms,FieldSendChek);
        DMess D=new DMess();
        PingMaster PingM=new PingMaster(BaseFW.base.GetConfig().ping_delay_ms, BaseFW.base.GetUsers(), BaseFW.base.MySocket, BaseFW.base, BaseFW.base.GetConfig().node_timeout_ms,D);
        ReceiveMaster MesTaker=new ReceiveMaster(BaseFW.base, MyFieldBase, BaseFW.base.MySocket,D,FieldSendChek);
        MesTaker.start();
        PingM.start();
        try {
            BaseFW.SendAboutGame= new MultiSocket(BaseFW.base);
            BaseFW.SendAboutGame.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (true){
            BaseFW.base.iter++;
            if(!HaveMe&& BaseFW.base.MeId!=-1){
                Board=new KeyL(BaseFW.base.GetSnakes().GetById(BaseFW.base.MeId).Head);
                BaseFW.MyWindow.addKeyListener(Board);
                HaveMe=true;
            }else if(HaveMe){
                Board.getTurn();
            }
            MyFieldBase.tic();
            WindowField.turn(BaseFW, MyFieldBase, BaseFW.base.GetSnakes().GetSnakes());
            BaseFW.base.can= MyFieldBase.canPlace();
            if(BaseFW.base.MeId==-1){
                HaveMe=false;
            }
            Snake[] a= BaseFW.base.GetSnakes().GetSnakes();
            for(Snake one: a){
                BaseFW.base.GetUsers().SetScore(one.id, one.Score);
            }
            FieldSender.end=true;
            FieldSendChek=new GetFieldAsc();
            FieldSender=new SendFieldMaster(BaseFW.base, MyFieldBase, BaseFW.base.MySocket, BaseFW.base.GetConfig().ping_delay_ms,FieldSendChek);
            FieldSender.start();
            try {
                sleep(TimeToTurn);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(Board.isViewer){
                end=true;
            }
            if(end){
                BaseFW.base.MeId=-1;
                if (Board != null) {
                    Board.end=true;
                }
                MesTaker.end=true;
                PingM.end=true;
                try {
                    if ((Board.isViewer && BaseFW.base.GetUsers().GetInfoNext() != null) || BaseFW.base.GetUsers().GetById(BaseFW.base.MeId).Die) {
                        InfoAboutUser us = BaseFW.base.GetUsers().GetInfoNext();
                        BaseFW.base.ReWind();
                        BaseFW.base.MasterId=us.id;
                        try {
                            SnakesProto.GameMessage OutMess = new CreateRoleChangeMsg().work(false, false, true, false, false, BaseFW.base.MeId, us.id, BaseFW.base.GetNum());
                            byte[] Mess = OutMess.toByteArray();
                            DatagramPacket sendPacket = new DatagramPacket(Mess, Mess.length, InetAddress.getByName(us.IP), us.Socket);
                            BaseFW.base.MySocket.send(sendPacket);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        BaseFW.SendAboutGame.end = true;
                        OtherFieldThread NewThread = new OtherFieldThread(BaseFW, MyFieldBase, us.IP, us.Socket);
                        NewThread.paint = WindowField;
                        BaseFW.VField = NewThread;
                        BaseFW.VField.start();
                    }
                }catch (NullPointerException ignored){}
                MesTaker.Me.close();
                BaseFW.base.MySocket.close();
                return;
            }
        }
    }
}