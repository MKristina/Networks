package MyProtoClass;

import Classes.Bases.Base;
import Field.GameFieldBaze;
import Field.PartOfOb.Coords;
import Field.PartOfOb.SCoords;
import Field.Snakes.Snake;
import Info.MyConfig;

public class CreateStateMsg {
    public SnakesProto.GameMessage work(Base baze, GameFieldBaze MyFieldBaze, int k, int iter){
        SnakesProto.GameMessage.StateMsg.Builder c= SnakesProto.GameMessage.StateMsg.newBuilder();
        SnakesProto.GameState.Builder a= SnakesProto.GameState.newBuilder();

        a.setStateOrder(iter);

        SnakesProto.GameConfig.Builder conf= SnakesProto.GameConfig.newBuilder();
        MyConfig Con=baze.GetConfig();
        conf.setWidth(Con.width);
        conf.setHeight(Con.height);
        conf.setFoodStatic(Con.food_static);
        conf.setFoodPerPlayer(Con.food_per_player);
        conf.setStateDelayMs(Con.state_delay_ms);
        conf.setDeadFoodProb(Con.dead_food_prob);
        conf.setPingDelayMs(Con.ping_delay_ms);
        conf.setNodeTimeoutMs(Con.node_timeout_ms);
        a.setConfig(conf.build());
        a.setPlayers(baze.GetUsers().DoPlayers());
        SnakesProto.GameState.Snake.Builder OneSnake = SnakesProto.GameState.Snake.newBuilder();
        SnakesProto.GameState.Coord.Builder Where= SnakesProto.GameState.Coord.newBuilder();
        SCoords part;
        for(Snake one:baze.GetSnakes().GetSnakes()){
            part= one.Head;
            if(one.Status==0) {
                OneSnake.setState(SnakesProto.GameState.Snake.SnakeState.ALIVE);
            }else {
                OneSnake.setState(SnakesProto.GameState.Snake.SnakeState.ZOMBIE);
            }
            OneSnake.setPlayerId(one.id);
            Where.setX(part.headcoord.x);
            Where.setY(part.headcoord.y);
            switch (part.vector){
                case (-2)->{OneSnake.setHeadDirection(SnakesProto.Direction.UP);}
                case (-1)->{OneSnake.setHeadDirection(SnakesProto.Direction.LEFT);}
                case (2)->{OneSnake.setHeadDirection(SnakesProto.Direction.DOWN);}
                case (1)->{OneSnake.setHeadDirection(SnakesProto.Direction.RIGHT);}
            }
            OneSnake.addPoints(Where.build());
            while (part.next!=null){
                part=part.next;
                Where= SnakesProto.GameState.Coord.newBuilder();
                Where.setX(0);
                Where.setY(0);
                switch (part.vector){
                    case (-2)->{Where.setY(-1);}
                    case (-1)->{Where.setX(-1);}
                    case (2)->{Where.setY(1);}
                    case (1)->{Where.setX(1);}
                }
                OneSnake.addPoints(Where.build());
            }
            a.addSnakes(OneSnake.build());
            OneSnake = SnakesProto.GameState.Snake.newBuilder();
        }
        Coords[][] Field=MyFieldBaze.retField();
        for(int i=0;i<MyFieldBaze.GetX();i++){
            for (int j=0;j<MyFieldBaze.GetY();j++){
                if(Field[i][j].have_food==1){
                    Where.setX(i);
                    Where.setY(j);
                    a.addFoods(Where.build());
                }
            }
        }
        SnakesProto.GameMessage.Builder b=SnakesProto.GameMessage.newBuilder();
        b.setState(c.setState(a.build()));
        b.setMsgSeq(k);
        return b.build();
    }
}
