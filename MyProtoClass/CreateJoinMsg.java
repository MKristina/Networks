package MyProtoClass;

public class CreateJoinMsg {
    public SnakesProto.GameMessage work(String MyName, int k){
        SnakesProto.GameMessage.JoinMsg.Builder a=SnakesProto.GameMessage.JoinMsg.newBuilder();
        a.setPlayerType(SnakesProto.PlayerType.HUMAN);
        a.setName(MyName);
        SnakesProto.GameMessage.Builder b=SnakesProto.GameMessage.newBuilder();
        b.setJoin(a.build());
        b.setMsgSeq(k);
        return b.build();
    }
}
