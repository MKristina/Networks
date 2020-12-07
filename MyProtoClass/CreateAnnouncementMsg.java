package MyProtoClass;

import Classes.Bases.Base;
import Info.MyConfig;

public class CreateAnnouncementMsg {

    public SnakesProto.GameMessage work(Base base, boolean can, int k){
        SnakesProto.GameMessage.AnnouncementMsg.Builder a= SnakesProto.GameMessage.AnnouncementMsg.newBuilder();
        a.setPlayers(base.GetUsers().DoPlayers());
        SnakesProto.GameConfig.Builder conf= SnakesProto.GameConfig.newBuilder();
        MyConfig Con=base.GetConfig();
        conf.setWidth(Con.width);
        conf.setHeight(Con.height);
        conf.setFoodStatic(Con.food_static);
        conf.setFoodPerPlayer(Con.food_per_player);
        conf.setStateDelayMs(Con.state_delay_ms);
        conf.setDeadFoodProb(Con.dead_food_prob);
        conf.setPingDelayMs(Con.ping_delay_ms);
        conf.setNodeTimeoutMs(Con.node_timeout_ms);
        a.setConfig(conf.build());
        a.setCanJoin(can);
        SnakesProto.GameMessage.Builder b=SnakesProto.GameMessage.newBuilder();
        b.setAnnouncement(a.build());
        b.setMsgSeq(k);
        return b.build();
    }
}
