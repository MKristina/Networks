package MyProtoClass;

public class CreateRoleChangeMsg {
    public SnakesProto.GameMessage work(boolean WantBeViewer,boolean SetDeputy,boolean MasterToViewer, boolean IMasterNow,boolean IDied, int myid, int recid,long k){
        SnakesProto.GameMessage.RoleChangeMsg.Builder a=SnakesProto.GameMessage.RoleChangeMsg.newBuilder();
        if(WantBeViewer){
            a.setSenderRole(SnakesProto.NodeRole.VIEWER);
        }
        else if(SetDeputy){
            a.setReceiverRole(SnakesProto.NodeRole.DEPUTY);
        }else if(MasterToViewer){
            a.setSenderRole(SnakesProto.NodeRole.VIEWER);
            a.setReceiverRole(SnakesProto.NodeRole.MASTER);
        }else if(IMasterNow){
            a.setSenderRole(SnakesProto.NodeRole.MASTER);
        }else if(IDied){
            a.setReceiverRole(SnakesProto.NodeRole.VIEWER);
        }
        SnakesProto.GameMessage.Builder b=SnakesProto.GameMessage.newBuilder();
        b.setRoleChange(a.build());
        b.setMsgSeq(k);
        b.setSenderId(myid);
        b.setReceiverId(recid);
        return b.build();
    }
}
