package MyProtoClass;

public class CreateErrorMsg {
    public SnakesProto.GameMessage work(String Text,long k){
        SnakesProto.GameMessage.ErrorMsg.Builder a=SnakesProto.GameMessage.ErrorMsg.newBuilder();
        a.setErrorMessage(Text);
        SnakesProto.GameMessage.Builder b=SnakesProto.GameMessage.newBuilder();
        b.setError(a.build());
        b.setMsgSeq(k);
        return b.build();
    }
}
