package MyProtoClass;

public class CreateSteerMsg {
    public SnakesProto.GameMessage work(int id,long k, int where){
        SnakesProto.GameMessage.SteerMsg.Builder a=SnakesProto.GameMessage.SteerMsg.newBuilder();
        switch (where){
            case (-2)->{a.setDirection(SnakesProto.Direction.UP);}
            case (-1)->{a.setDirection(SnakesProto.Direction.LEFT);}
            case (2)->{a.setDirection(SnakesProto.Direction.DOWN);}
            case (1)->{a.setDirection(SnakesProto.Direction.RIGHT);}
        }
        SnakesProto.GameMessage.Builder b=SnakesProto.GameMessage.newBuilder();
        b.setSteer(a.build());
        b.setMsgSeq(k);
        b.setSenderId(id);
        return b.build();
    }
}
