package Classes;

import MyProtoClass.SnakesProto;

public class MessGetter {
    public SnakesProto.GameMessage MyMess;
    public long LastSend=System.currentTimeMillis();
    public boolean HasAsk=false;
    public MessGetter(SnakesProto.GameMessage InMes){
        MyMess=InMes;
    }

}
