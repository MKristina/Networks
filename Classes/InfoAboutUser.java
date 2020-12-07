package Classes;


import MyProtoClass.SnakesProto;

public class InfoAboutUser {
    public String Name="None";
    public int id=0;
    public String IP=" ";
    public int Socket=0;
    public int Role=0;
    public int Type=1;
    public int Score=0;
    public long LastSeen=System.currentTimeMillis();
    public long LastSend=System.currentTimeMillis();
    public InfoAboutUser next=null;
    public boolean Die=false;
    public SnakesProto.GameMessage SwapMes=null;
    public boolean HasAns=false;
    public long LastSendMes=0;
}
