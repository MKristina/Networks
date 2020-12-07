package SendAndReceive.Help;

import MyProtoClass.SnakesProto;

public class GetFieldAsc {
    public boolean[] HaveAscFrom=null;
    public int[] Socket=null;
    public String[] IP=null;
    public long[] LastSend=null;
    public SnakesProto.GameMessage MyMess=null;
    public int[] Id=null;

    synchronized public void SetAsc(String inIP,int inSocket){
        if(HaveAscFrom!=null){
            for(int i=0;i<HaveAscFrom.length;i++){
                if(IP[i].equals(inIP)&&Socket[i]==inSocket){
                    HaveAscFrom[i]=true;
                    break;
                }
            }
        }
    }
}
