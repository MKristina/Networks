package Field;

import Classes.Bases.Base;
import Field.PartOfOb.Coords;
import Field.Snakes.Snake;
import Field.Snakes.SnakesBase;
import Field.PartOfOb.SCoords;

public class GameFieldBaze {
    Coords[][] CoordMass;
    int maxx;
    int maxy;
    int constfood;
    float coeffood;
    Base baze;
    SnakesBase ForDell=new SnakesBase();

    public GameFieldBaze(Base inbaze){
        baze=inbaze;
        maxx=baze.GetConfig().width;
        maxy=baze.GetConfig().height;
        constfood=baze.GetConfig().food_static;
        coeffood=baze.GetConfig().food_per_player;
        CoordMass=new Coords[maxx][maxy];
        for (int i=0;i<maxx;i++){
            for(int j=0;j<maxy;j++){
                CoordMass[i][j]=new Coords(i,j,0);
            }
        }
    }

    public Coords[][] retField(){
        return CoordMass;
    }

    public void setField(Coords[][] a){
        CoordMass=a;
    }

    public void tic(){
        SnakesTic();
        foodtic();
    }

    void SnakesTic(){
        Coords next;
        for (Snake one : baze.GetSnakes().GetSnakes()) {
            next = one.next(maxx, maxy);
            if (CoordMass[next.x][next.y].have_food != 1) {
                one.turn(maxx, maxy);
            } else if (CoordMass[next.x][next.y].have_food == 1) {
                CoordMass[next.x][next.y].have_food = 0;
                one.Score++;
                one.eatturn(maxx, maxy);
            }
        }
        LoadSnakes();
    }

    public void OnlyPlaceSnakes(Snake[] Snakes){
        for (int i=0;i<maxx;i++){
            for(int j=0;j<maxy;j++){
                if(CoordMass[i][j].have_food>1) {
                    CoordMass[i][j].have_food=0;
                }
            }
        }

        SCoords part;
        int x;
        int y;
        int color;
        for(Snake one: Snakes ){
            part=one.Head;
            x=part.headcoord.x;
            y=part.headcoord.y;
            if(one.id==baze.MeId){
                color=3;
            }
            else {
                color=2;
            }
            part=part.next;
            CoordMass[x][y]=new Coords(x,y,color*10);
            while (part!=null){
                if(part.vector==1||part.vector==-1){
                    x=x+part.vector;
                    if(x<0){
                        x=maxx-1;
                    }
                    else if(x>maxx-1){
                        x=0;
                    }
                }
                else {
                    y=y+part.vector/2;
                    if(y<0){
                        y=maxy-1;
                    }
                    else if(y>maxy-1){
                        y=0;
                    }
                }
                CoordMass[x][y]=new Coords(x,y,color);
                CoordMass[x][y].id=part.headcoord.have_food;
                part=part.next;
            }
        }
    }

    void LoadSnakes(){
        for (int i=0;i<maxx;i++){
            for(int j=0;j<maxy;j++){
                if(CoordMass[i][j].have_food>1) {
                    CoordMass[i][j].have_food=0;
                }
            }
        }

        SCoords part;
        int x;
        int y;
        int color;
        for(Snake one: baze.GetSnakes().GetSnakes() ){
            part=one.Head;
            x=part.headcoord.x;
            y=part.headcoord.y;
            if(one.id==baze.MeId){
                color=3;
            }
            else {
                color=2;
            }
            part=part.next;
            while (part!=null){
                if(part.vector==1||part.vector==-1){
                    x=x+part.vector;
                    if(x<0){
                        x=maxx-1;
                    }
                    else if(x>maxx-1){
                        x=0;
                    }
                }
                else {
                    y=y+part.vector/2;
                    if(y<0){
                        y=maxy-1;
                    }
                    else if(y>maxy-1){
                        y=0;
                    }
                }
                CoordMass[x][y]=new Coords(x,y,color);
                CoordMass[x][y].id=part.headcoord.have_food;
                part=part.next;
            }
        }
        int forwhat=baze.GetSnakes().GetSnakes().length;
        for(int i=0;i<forwhat;){
            if(!HeadSmashes(i)){
                if(CoordMass[baze.GetSnakes().GetSnakes()[i].Head.headcoord.x][baze.GetSnakes().GetSnakes()[i].Head.headcoord.y].have_food>1){
                    if(baze.GetSnakes().GetById(CoordMass[baze.GetSnakes().GetSnakes()[i].Head.headcoord.x][baze.GetSnakes().GetSnakes()[i].Head.headcoord.y].id)!=null) {
                        baze.GetSnakes().GetById(CoordMass[baze.GetSnakes().GetSnakes()[i].Head.headcoord.x][baze.GetSnakes().GetSnakes()[i].Head.headcoord.y].id).Score++;
                    }
                    deadI(baze.GetSnakes().GetSnakes()[i],1);
                    baze.DelSnakeFromBaze(baze.GetSnakes().GetSnakes()[i].id);
                }
                else {
                    i++;
                }
            }
            forwhat=baze.GetSnakes().GetSnakes().length;
        }
        deadI(null,2);
        for (Snake snake : baze.GetSnakes().GetSnakes()) {
            if(snake.id==baze.MeId){
                color=3;
            }
            else {
                color=2;
            }
            CoordMass[snake.Head.headcoord.x][snake.Head.headcoord.y] = new Coords(snake.Head.headcoord.x, snake.Head.headcoord.y, color * 10);
        }

    }

    boolean HeadSmashes( int i){
        boolean dead=false;
        int forwhat=baze.GetSnakes().GetSnakes().length;
        for(int j=i+1;j<forwhat;){
            if(baze.GetSnakes().GetSnakes()[i].Head.headcoord.x==baze.GetSnakes().GetSnakes()[j].Head.headcoord.x&&baze.GetSnakes().GetSnakes()[i].Head.headcoord.y==baze.GetSnakes().GetSnakes()[j].Head.headcoord.y){
                deadI(baze.GetSnakes().GetSnakes()[j],1);
                baze.DelSnakeFromBaze(baze.GetSnakes().GetSnakes()[j].id);
                dead=true;
                forwhat--;
            }
            else {
                j++;
            }
        }
        if(dead) {
            deadI(baze.GetSnakes().GetSnakes()[i],1);
            baze.DelSnakeFromBaze(baze.GetSnakes().GetSnakes()[i].id);
        }
        return dead;

    }

    void deadI(Snake DeadBody, int todo){
        if(todo==0){
            ForDell=new SnakesBase();
        }else if(todo==1){
            ForDell.addSnake(DeadBody);
        }
        else {
            SCoords part;
            for(Snake snake : ForDell.GetSnakes()) {
                if(baze.MeId== snake.id){
                    baze.MeId=-1;
                }
                if(baze.GetUsers().GetById(snake.id)!=null) {
                    baze.GetUsers().GetById(snake.id).Role = 3;
                    baze.GetUsers().GetById(snake.id).Die = true;
                    part = snake.Head;
                    int x = part.headcoord.x;
                    int y = part.headcoord.y;
                    part = part.next;
                    while (part != null) {
                        if (part.vector == 1 || part.vector == -1) {
                            x = x + part.vector;
                        } else {
                            y = y + part.vector / 2;
                        }
                        if (x < 0) {
                            x = maxx - 1;
                        } else if (x > maxx - 1) {
                            x = 0;
                        }
                        if (y < 0) {
                            y = maxy - 1;
                        } else if (y > maxy - 1) {
                            y = 0;
                        }
                        if (Math.random() * 100 <= baze.GetConfig().dead_food_prob) {
                            CoordMass[x][y] = new Coords(x, y, 1);
                        } else {
                            CoordMass[x][y] = new Coords(x, y, 0);
                        }
                        part = part.next;
                    }
                }
            }
            ForDell=new SnakesBase();
        }
    }

    void foodtic(){
        int food=0;
        int free=0;
        for (int i=0;i<maxx;i++){
            for(int j=0;j<maxy;j++){
                if(CoordMass[i][j].have_food==1){
                    food++;
                }
                else if(CoordMass[i][j].have_food==0){
                    free++;
                }
            }
        }
        int dowhile=0;
        if(food<constfood+coeffood*baze.NowPlay&&free>0){
            if(free<constfood+coeffood*baze.NowPlay-food){
                dowhile=free;
            }
            else {
                dowhile= (int) (constfood+coeffood*baze.NowPlay-food);
            }
        }
        int x;
        int y;
        x= (int) (Math.random()*maxx);
        y= (int) (Math.random()*maxy);
        while (dowhile>0){
            while (CoordMass[x][y].have_food!=0) {
                x = (int) (Math.random() * maxx);
                y = (int) (Math.random() * maxy);
            }
            CoordMass[x][y].have_food=1;
            dowhile--;
        }
    }
    boolean isVoid(int x,int y){
       if(CoordMass[x][y].have_food == 0&&(CoordMass[x-1][y].have_food == 0||CoordMass[x+1][y].have_food == 0||CoordMass[x][y-1].have_food == 0||CoordMass[x][y+1].have_food == 0)){
           if(CoordMass[x-2][y-2].have_food <=1&&CoordMass[x-2][y-1].have_food <=1&&CoordMass[x-2][y].have_food <=1&&CoordMass[x-2][y+1].have_food <=1&&CoordMass[x-2][y+2].have_food <=1){
               if(CoordMass[x-1][y-2].have_food <=1&&CoordMass[x-1][y-1].have_food <=1&&CoordMass[x-1][y+1].have_food <=1&&CoordMass[x-1][y+2].have_food <=1) {
                   if (CoordMass[x + 2][y - 2].have_food <= 1 && CoordMass[x + 2][y - 1].have_food <= 1 && CoordMass[x + 2][y].have_food <= 1 && CoordMass[x + 2][y + 1].have_food <= 1 && CoordMass[x + 2][y + 2].have_food <= 1) {
                       if (CoordMass[x + 1][y - 2].have_food <= 1 && CoordMass[x +1][y - 1].have_food <= 1 && CoordMass[x + 1][y + 1].have_food <= 1 && CoordMass[x + 1][y + 2].have_food <= 1) {
                            if(CoordMass[x][y - 2].have_food <= 1 && CoordMass[x ][y +2].have_food <= 1){
                                return true;
                            }
                       }
                   }
               }
           }
       }

        return false;
    }

    public boolean canPlace(){
            int x = 2;
            int y = 2;
            while (y<maxy-2) {
                if(isVoid(x,y)){
                    return true;
                } else {
                    x++;
                    if (x == maxx - 2) {
                        y++;
                        x = 2;
                    }
                }
            }
            return false;
    }


    public int GetX(){return maxx;}
    public int GetY(){return maxy;}
}
