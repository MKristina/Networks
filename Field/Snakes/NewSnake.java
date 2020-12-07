package Field.Snakes;

import Classes.Bases.Base;
import Classes.InfoAboutUser;
import Field.PartOfOb.Coords;
import Info.MyConfig;


public class NewSnake {
    Coords[][] CoordMass;
    int maxx;
    int maxy;
    boolean Real;
    Base baze;
    boolean IsMe;
    public Snake ret;
    public NewSnake(Base baze, Coords[][] InCoordMass, boolean IsReal, boolean IsMe){
        CoordMass=InCoordMass;
        this.IsMe=IsMe;
        MyConfig a=baze.GetConfig();
        maxx=a.width;
        maxy=a.height;
        Real=IsReal;
        this.baze=baze;
        ret=GetBody();
        if(!Real){
            //new Bot(ret.Head, baze.GetConfig().state_delay_ms).start();
        }
        baze.AddSnakeToBaze(ret);
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

    public Snake GetBody(){
        int x= (int) (2+(Math.random()*(maxx-5)));
        int y=(int) (2+(Math.random()*(maxy-5)));
        boolean ok=false;
        while (!ok){
                if(isVoid(x,y)){
                    ok=true;
                }
            else {
                x= (int) (2+(Math.random()*(maxx-5)));
                y=(int) (2+(Math.random()*(maxy-5)));
            }
        }
        ok=false;
        int where=0;
        while (!ok) {
            where =1 + (int) (Math.random() * 4);
            if(where==5){
                where=4;
            }
            if (where == 4 || where == 3) {
                where = where - 2;
                if((where==2&&CoordMass[x+1][y].have_food==0)||(where==1&&CoordMass[x][y+1].have_food==0)){
                    ok=true;
                }
            } else {
                where = where - 3;
                if((where==-2&&CoordMass[x-1][y].have_food==0)||(where==-1&&CoordMass[x][y-1].have_food==0)){
                    ok=true;
                }
            }
        }
        baze.NextId++;
        if(Real) {
            if(IsMe) {
                baze.MeId = baze.NextId;
                InfoAboutUser Me=new InfoAboutUser();
                Me.Name=baze.Name;
                Me.Socket=baze.Socket;
                Me.Role=1;
                Me.id=baze.MeId;
                baze.GetUsers().AddUser(Me);
            }
            return new Snake(baze.NextId, -where, where, 3, x, y);
        }
        else{
            return new Snake(baze.NextId, -where, where, 2, x, y);
        }
    }

}
