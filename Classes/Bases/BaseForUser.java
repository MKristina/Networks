package Classes.Bases;

import Classes.InfoAboutUser;
import Field.Snakes.SnakesBase;
import MyProtoClass.SnakesProto;

public class BaseForUser {
   InfoAboutUser Head=null;

    synchronized public void AddUser(InfoAboutUser Us){
        InfoAboutUser part=Head;
        if(Head==null){
            Head=Us;
        }else {
            while (part.next!=null){
                part=part.next;
            }
            part.next=Us;
        }
    }

    synchronized public void PrintAll(){
        InfoAboutUser part=Head;
        while (part!=null){
            System.out.println(part.IP+"  "+part.Socket+"  "+part.Name+"  "+part.id);
            part=part.next;
        }
    }

    synchronized public InfoAboutUser GetSwapToView(){
        InfoAboutUser part=Head;
        InfoAboutUser HeadSwaper=null;
        InfoAboutUser Swaper=null;
        while (part!=null){
            if(part.Die&&!part.HasAns){
                if(Swaper==null){
                    Swaper=DoCopy(part);
                    HeadSwaper=Swaper;
                }else{
                    Swaper.next=DoCopy(part);
                    Swaper=Swaper.next;
                }
            }
            part=part.next;
        }
        return HeadSwaper;
    }

    synchronized public void SetSwapToView(InfoAboutUser in){
        InfoAboutUser part=Head;
        while (part!=null){
            if(part.id==in.id){
                part.SwapMes=in.SwapMes;
                part.LastSendMes=System.currentTimeMillis();
                return;
            }
            part=part.next;
        }
    }

    synchronized public void ReTimeViewMes(InfoAboutUser in){
        InfoAboutUser part=Head;
        while (part!=null){
            if(part.id==in.id){
                part.LastSendMes=System.currentTimeMillis();
                return;
            }
            part=part.next;
        }
    }

    synchronized public void GetAscViewMes(InfoAboutUser in){
        InfoAboutUser part=Head;
        while (part!=null){
            if(part.id==in.id){
                part.HasAns=true;
                return;
            }
            part=part.next;
        }
    }

    synchronized public InfoAboutUser GetCopy(){
        InfoAboutUser part=Head;
        InfoAboutUser CopyHead=null;
        InfoAboutUser CopyPart=null;
        if(Head!=null){
            CopyHead=DoCopy(Head);
            CopyPart=CopyHead;
            part=Head.next;
            while (part!=null){
                CopyPart.next=DoCopy(part);
                CopyPart=CopyPart.next;
                part=part.next;
            }
        }
        return CopyHead;
    }

    synchronized public void ReTime(){
        InfoAboutUser part=Head;
        while (part!=null){
            part.LastSeen=System.currentTimeMillis();
            part=part.next;
        }
    }

    synchronized public int GetMaxId(){
        int a=0;
        InfoAboutUser part=Head;
        while (part!=null){
            if(a< part.id){
                a= part.id;;
            }
            part=part.next;
        }
        return a;
    }

    synchronized public void delI(int what){
        InfoAboutUser part=Head;
        if(Head!=null){
            while (part.next!=null){
                if(part.next.id==what){
                    part.next=part.next.next;
                    break;
                }
            }
        }
    }

    synchronized public InfoAboutUser GetById(int id){
        InfoAboutUser part=Head;
        while (part!=null){
            if(part.id==id){
                return part;
            }
            part=part.next;
        }
        return null;
    }

    synchronized public InfoAboutUser GetByIPAndSocket(int Socket, String IP){
        InfoAboutUser part=Head;
        while (part!=null){
            if (part.IP.equals(IP) &&part.Socket==Socket) {
                return part;
            }
            part=part.next;
        }
        return null;
    }

    synchronized public InfoAboutUser GetNext(){
        InfoAboutUser part=Head;
        InfoAboutUser Next=null;
        while (part!=null){
            if(part.Role==2){
                return null;
            }else if(part.Role==0){
                Next=part;
            }
            part=part.next;
        }
        return Next;
    }

    synchronized public SnakesProto.GamePlayers DoPlayers(){
        SnakesProto.GamePlayers.Builder players= SnakesProto.GamePlayers.newBuilder();
        SnakesProto.GamePlayer.Builder player= SnakesProto.GamePlayer.newBuilder();
        InfoAboutUser one=Head;
        while (one!=null){
            player.setId(one.id);
            player.setName(one.Name);
            switch (one.Role) {
                case (0) -> player.setRole(SnakesProto.NodeRole.NORMAL);
                case (1) -> player.setRole(SnakesProto.NodeRole.MASTER);
                case (2) -> player.setRole(SnakesProto.NodeRole.DEPUTY);
                case (3) -> player.setRole(SnakesProto.NodeRole.VIEWER);
            }
            player.setIpAddress(one.IP);
            player.setPort(one.Socket);
            switch (one.Type) {
                case (0) -> player.setType(SnakesProto.PlayerType.HUMAN);
                case (1) -> player.setType(SnakesProto.PlayerType.ROBOT);
            }
            player.setScore(one.Score);
            players.addPlayers(player.build());
            player= SnakesProto.GamePlayer.newBuilder();
            one=one.next;
        }
        return players.build();
    }

    synchronized public void SetMeMaster(int id){
        InfoAboutUser part=Head;
        while (part!=null){
            if (part.id==id) {
                part.Role=1;
            }
            part=part.next;
        }
    }

    synchronized public void SetMaster(String IP,int Socket){
        InfoAboutUser part=Head;
        while (part!=null){
            if (part.IP.equals(" ") ) {
                part.Socket=Socket;
                part.IP=IP;
                part.Role=3;
                break;
            }else if(part.IP.equals(IP)&&part.Socket==Socket){
                part.Role=3;
            }
            part=part.next;
        }
    }

    synchronized public void DelByTime(int MyId, int DieTime, SnakesBase Pit){
        boolean ok=false;
        if(Head!=null) {
            while (Head.LastSeen < System.currentTimeMillis() - DieTime && Head.id != MyId && !ok) {
                Head=Head.next;
                if(Head==null){
                    ok=true;
                }
            }
        }
        InfoAboutUser part=Head;
        if(part!=null) {
            while (part.next != null) {
                if(part.next.LastSeen < System.currentTimeMillis() - DieTime && part.next.id != MyId){
                    if(Pit.GetById(part.next.id)!=null){
                        Pit.GetById(part.next.id).Status=1;
                    }
                    part.next=part.next.next;
                }
                else {

                    part=part.next;
                }
            }
        }
    }

    InfoAboutUser DoCopy(InfoAboutUser in){
        InfoAboutUser one=new InfoAboutUser();
        one.LastSeen=in.LastSeen;
        one.Score=in.Score;
        one.Name=in.Name;
        one.Type=in.Type;
        one.Socket=in.Socket;
        one.IP=""+in.IP;
        one.Role=in.Role;
        one.id=in.id;
        one.LastSend=in.LastSend;
        one.next=null;
        one.HasAns= in.HasAns;;
        one.SwapMes=in.SwapMes;
        one.LastSendMes=in.LastSendMes;
        return one;
    }

    synchronized public InfoAboutUser GetPingList(int MyId, int PingTime){
        InfoAboutUser Pinger=null;
        InfoAboutUser PingerLast=null;
        InfoAboutUser part=Head;
        while (part!=null){
            if(part.LastSeen < System.currentTimeMillis() - PingTime && part.id != MyId){
                if(Pinger==null){
                    Pinger=DoCopy(part);
                    PingerLast=Pinger;
                }
                else{
                    PingerLast.next=DoCopy(part);
                    PingerLast=PingerLast.next;
                }
            }
            part=part.next;
        }
        return Pinger;
    }

    synchronized public void ISeeAndSend(int id){
        InfoAboutUser part=Head;
        while (part!=null){
            if(part.id==id){
                part.LastSend=System.currentTimeMillis();
                part.LastSeen=System.currentTimeMillis();
                break;
            }
            part=part.next;
        }
    }

    synchronized public void ISee(int id){
        InfoAboutUser part=Head;
        while (part!=null){
            if(part.id==id){
                part.LastSeen=System.currentTimeMillis();
                break;
            }
            part=part.next;
        }
    }

    synchronized public void ISend(int id){
        InfoAboutUser part=Head;
        while (part!=null){
            if(part.id==id){
                part.LastSend=System.currentTimeMillis();
                break;
            }
            part=part.next;
        }
    }

    synchronized public void ToViewer(int id, SnakesBase Pit){
        InfoAboutUser part=Head;
        while (part!=null){
            if(part.id==id){
                part.Role = 3;
                part.LastSend = System.currentTimeMillis();
                if(Pit.GetById(part.id)!=null) {
                    Pit.GetById(part.id).Status = 1;
                }
                part.LastSeen = System.currentTimeMillis();
                break;
            }
            part=part.next;
        }
    }

    synchronized public void SetNext(int id){
        InfoAboutUser part=Head;
        while (part!=null){
            if(part.id==id){
                part.Role=2;
                break;
            }
            part=part.next;
        }
    }

    synchronized public void SetScore(int id, int S){
        InfoAboutUser part=Head;
        while (part!=null){
            if(part.id==id){
                part.Score=S;
                break;
            }
            part=part.next;
        }
    }

    synchronized public InfoAboutUser GetInfoNext(){
        InfoAboutUser part=Head;
        while (part!=null){
            if(part.Role==2){
                return part;
            }
            part=part.next;
        }
        return null;
    }

}
