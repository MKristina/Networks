package MyProtoClass;

public class CreatePingMsg {
    public SnakesProto.GameMessage work(int id,long k){
        SnakesProto.GameMessage.PingMsg.Builder a=SnakesProto.GameMessage.PingMsg.newBuilder();
        SnakesProto.GameMessage.Builder b=SnakesProto.GameMessage.newBuilder();
        b.setPing(a.build());
        b.setMsgSeq(k);
        b.setSenderId(id);
        return b.build();
    }
}
