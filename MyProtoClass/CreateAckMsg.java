package MyProtoClass;

public class CreateAckMsg {

    public SnakesProto.GameMessage work(int myid, int recid,long k){
        SnakesProto.GameMessage.AckMsg.Builder a=SnakesProto.GameMessage.AckMsg.newBuilder();
        SnakesProto.GameMessage.Builder b=SnakesProto.GameMessage.newBuilder();
        b.setAck(a.build());
        b.setMsgSeq(k);
        b.setSenderId(myid);
        b.setReceiverId(recid);
        return b.build();
    }

}
