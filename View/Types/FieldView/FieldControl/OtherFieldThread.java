package View.Types.FieldView.FieldControl;

import Also.KeyL;
import Classes.Bases.BaseForUser;
import Classes.Bases.BaseForWindow;
import Classes.Bases.SendBazeOther;
import Classes.InfoAboutUser;
import Field.GameFieldBaze;
import Field.PartOfOb.Coords;
import Field.Snakes.Snake;
import Field.Snakes.SnakesBase;
import MyProtoClass.SnakesProto;
import SendAndReceive.Help.InfoInWork;
import SendAndReceive.Other.ReceiveOther;
import SendAndReceive.Other.SendIMasterOther;
import SendAndReceive.Other.SendOther;
import View.Types.FieldView.FieldRepaint.FieldRepaint;

import java.net.DatagramSocket;
import java.net.SocketException;

public class OtherFieldThread extends FieldParent {
    Snake[] Snakes=null;
    String MasterIp;
    int iter;
    int MasterSocket;
    public FieldRepaint paint=null;
    int lastField =0;

    public OtherFieldThread(BaseForWindow InBase, GameFieldBaze FieldBase) {
        super(InBase, FieldBase);
    }

    public OtherFieldThread(BaseForWindow InBase, GameFieldBaze FieldBase, String MasterIp, int MasterSocket){
        BaseFW =InBase;
        MyFieldBase =FieldBase;
        this.MasterIp=MasterIp;
        this.MasterSocket=MasterSocket;
    }

    @Override
    public void run() {
        BaseFW.VField=this;
        BaseFW.HaveVField=true;
        InfoInWork trade=new InfoInWork();
        try{
            if(BaseFW.base.MySocket!=null) {
                if (!BaseFW.base.MySocket.isClosed()) {
                    BaseFW.base.MySocket.close();
                }
            }
            BaseFW.base.MySocket = new DatagramSocket(BaseFW.base.Socket);
        }catch (SocketException e) {
            e.printStackTrace();
        }
        SendBazeOther Chek=new SendBazeOther();
        Chek.LastSenMaster=System.currentTimeMillis();
        ReceiveOther receiver=new ReceiveOther(trade, BaseFW.base.MySocket,Chek, BaseFW.base);
        KeyL Board=null;
        boolean HaveMe=false;
        SendOther sender=new SendOther(BaseFW.base.MySocket,trade, BaseFW.base,Chek, BaseFW.base.GetConfig().ping_delay_ms);
        sender.MasterIp=MasterIp;
        sender.MasterSocet=MasterSocket;
        receiver.MasterIp=MasterIp;
        receiver.MasterSocKet=MasterSocket;
        sender.start();
        receiver.start();
        while (!end) {
            if (trade.Field != null) {
                print(trade);
                if (paint==null) {
                    paint = new FieldRepaint(BaseFW, MyFieldBase);
                } else {

                    paint.turn(BaseFW, MyFieldBase, Snakes);
                }

            }
            if(!HaveMe&& BaseFW.base.MeId!=-1){
                Board=new KeyL();
                BaseFW.MyWindow.addKeyListener(Board);
                HaveMe=true;
            }else if(HaveMe){
                trade.Head=Board.getVector();
                trade.NewVec=true;
                trade.SendViewerMsg=Board.isViewer;

            }
            try {
                sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(System.currentTimeMillis()-Chek.LastSenMaster> BaseFW.base.GetConfig().node_timeout_ms){
                if(BaseFW.base.INext){
                    BaseFW.base.MasterId=-1;
                    BaseFW.base.IsMaster=true;
                }else{
                    InfoAboutUser NewMaster= BaseFW.base.GetUsers().GetInfoNext();
                    if(NewMaster!=null){
                        BaseFW.base.MasterId=NewMaster.id;
                        sender.MasterIp=NewMaster.IP;
                        sender.MasterSocet= NewMaster.Socket;
                        receiver.MasterIp=NewMaster.IP;
                        receiver.MasterSocKet=NewMaster.Socket;
                    }
                }
            }
            else if( !sender.MasterIp.equals(receiver.MasterIp)||receiver.MasterSocKet!=sender.MasterSocet){
                sender.MasterSocet=receiver.MasterSocKet;
                receiver.MasterIp=sender.MasterIp;
            }

            if(BaseFW.base.IsMaster&& BaseFW.base.INext){
                end=true;
            }

        }
        if(BaseFW.base.IsMaster&& BaseFW.base.INext){
            BaseFW.base.ReWind();
            BaseFW.HaveVField=true;
            BaseFW.base.IsMaster=true;
            BaseFW.base.iter=iter;
            int MaxS= BaseFW.base.GetSnakes().GetMaxId();
            int MaxU= BaseFW.base.GetUsers().GetMaxId();
            BaseFW.base.GetUsers().SetMeMaster(BaseFW.base.MeId);
            BaseFW.base.NumMes= lastField;
            new SendIMasterOther(BaseFW.base.GetUsers().GetCopy(), BaseFW.base.MySocket, BaseFW.base);
            if(MaxS>MaxU){
                BaseFW.base.NextId=MaxS+1;
            }else{
                BaseFW.base.NextId=MaxU+1;
            }
            MasterFieldThread NewThread=new MasterFieldThread(BaseFW, MyFieldBase);
            NewThread.WindowField=paint;
            BaseFW.VField=NewThread;
        }
        receiver.end=true;
        sender.end=true;
        if(Board!=null) {
            Board.end = true;
        }
        assert BaseFW.base.MySocket != null;
        BaseFW.base.MySocket.close();
        if(BaseFW.base.IsMaster) {
            BaseFW.VField.start();
        }
    }

    void print(InfoInWork trade){
        SnakesProto.GameMessage a=trade.Field;
        iter=trade.Field.getState().getState().getStateOrder();
        lastField = (int) trade.Field.getMsgSeq();
        trade.Field=null;
        MyFieldBase =new GameFieldBaze(BaseFW.base);
        Coords[][] field= MyFieldBase.retField();
        for(SnakesProto.GameState.Coord What: a.getState().getState().getFoodsList()){
            field[What.getX()][What.getY()].have_food=1;
        }
        Snakes=new  Snake[ a.getState().getState().getSnakesCount()];
        boolean isFirst;
        int i=0;
        BaseForUser Us= new BaseForUser();
        for(SnakesProto.GamePlayer one: a.getState().getState().getPlayers().getPlayersList()){
            InfoAboutUser UOne=new InfoAboutUser();
            UOne.Name=one.getName();
            UOne.id= one.getId();
            UOne.IP= one.getIpAddress();
            if(UOne.IP.equals(" ")){
                UOne.IP=MasterIp;
            }
            UOne.Socket=one.getPort();
            UOne.Score=one.getScore();
            switch (one.getRole()){
                case NORMAL -> UOne.Role=0;
                case MASTER -> UOne.Role=1;
                case DEPUTY -> UOne.Role=2;
                case VIEWER -> UOne.Role=3;
            }
            switch (one.getType()) {
                case HUMAN -> UOne.Type = 0;
                case ROBOT -> UOne.Type = 1;
            }
            Us.AddUser(UOne);
        }
        BaseFW.base.addUsers(Us);
        for(SnakesProto.GameState.Snake one:a.getState().getState().getSnakesList()){
            isFirst=true;
            for(SnakesProto.GameState.Coord What: one.getPointsList()){
                if(isFirst) {
                    Snakes[i] = new Snake(one.getPlayerId(), one.getHeadDirection(), What.getX(), What.getY());
                    if (BaseFW.base.GetUsers().GetById(one.getPlayerId()) != null){
                        Snakes[i].Score = BaseFW.base.GetUsers().GetById(one.getPlayerId()).Score;
                        switch (one.getState()){

                            case ALIVE -> Snakes[i].Status=0;
                            case ZOMBIE -> Snakes[i].Status=1;
                        }
                    }
                    isFirst=false;
                }else {
                    if(What.getX()!=0){
                        Snakes[i].AddPart(What.getX());
                    }else{
                        Snakes[i].AddPart(What.getY()*2);
                    }
                }
            }
            i++;
        }
        SnakesBase Pit=new SnakesBase();
        Pit.snakeMas=Snakes;
        BaseFW.base.addsnakes(Pit);
        MyFieldBase.OnlyPlaceSnakes(Snakes);
    }
}
