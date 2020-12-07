package Classes.Bases;

import Field.Snakes.Snake;
import Info.MyConfig;
import Field.Snakes.SnakesBase;

import java.net.DatagramSocket;

public class Base {
    MyConfig Conf;
    SnakesBase MySnakes;
    BaseForUser MyUsers;

    public int MeId=-1;
    public int NextId=10;
    public int NowPlay=0;
    public boolean can=true;
    public boolean IsMaster=false;
    public String Name=" ";
    public int Socket;
    public int NumMes=0;
    public int iter=0;
    public boolean INext=false;
    public DatagramSocket MySocket=null;
    public int MasterId=-1;

    public void ReWind(){
        can=true;
        IsMaster=false;
        INext=false;
    }

    public int GetNum(){
        NumMes++;
        return NumMes;
    }
    public void addconf(MyConfig InConfig){
        Conf=InConfig;
    }
    public MyConfig GetConfig(){
        return Conf;
    }
    public void addsnakes(SnakesBase InSnakes){MySnakes=InSnakes;}
    public SnakesBase GetSnakes(){return MySnakes;}
    public void AddSnakeToBaze(Snake in){
        NowPlay++;
        MySnakes.addSnake(in);
    }
    public void DelSnakeFromBaze(int what){
        NowPlay--;
        MySnakes.delI(what);
    }
    public void addUsers(BaseForUser InUsers){MyUsers=InUsers;}
    public BaseForUser GetUsers(){return MyUsers;}
}
