package Field.Snakes;

import Field.PartOfOb.Coords;
import Field.PartOfOb.SCoords;
import MyProtoClass.SnakesProto;

import java.util.List;

public class Snake {
    public int Status=0;
    public int id;
    public SCoords Head=new SCoords();
    public int Score=0;

    public Snake(int inid,int ihead, int body, int me,int headx,int heady){
        id=inid;
        SCoords part;
        Head.isHead=true;
        Head.vector=ihead;
        Head.headcoord=new Coords(headx,heady,me);
        Head.next=new SCoords();
        part=Head.next;
        part.vector=body;
        part.headcoord=new Coords(0,0,id);

    }

    public Snake(int inid, SnakesProto.Direction invector, int headx, int heady){
        id=inid;
        Head.isHead=true;
        switch ( invector){
            case UP -> Head.vector=-2;
            case DOWN -> Head.vector=2;
            case LEFT -> Head.vector=-1;
            case RIGHT -> Head.vector=1;
        }
        Head.headcoord=new Coords(headx,heady,2);
        Head.next=null;
    }

    public Coords next(int maxx,int maxy){
        int x=Head.headcoord.x;
        int y=Head.headcoord.y;
        if(Head.vector==1||Head.vector==-1){
            x=Head.headcoord.x+Head.vector;
            if(x<0){
                x=maxx-1;
            }
            else if(x>maxx-1){
                x=0;
            }
        }
        else {
            y=Head.headcoord.y+(Head.vector/2);
            if(y<0){
                y=maxy-1;
            }
            else if(y>maxy-1){
                y=0;
            }
        }
        return new Coords(x,y,id);
    }

    public void turn(int maxx, int maxy){
        int swap;
        int swap2;
        SCoords part=Head;
        if(Head.vector==1||Head.vector==-1){
            Head.headcoord.x=Head.headcoord.x+Head.vector;
            if(Head.headcoord.x<0){
                Head.headcoord.x=maxx-1;
            }
            else if(Head.headcoord.x>maxx-1){
                Head.headcoord.x=0;
            }
        }
        else {
            Head.headcoord.y=Head.headcoord.y+(Head.vector/2);
            if(Head.headcoord.y<0){
                Head.headcoord.y=maxy-1;
            }
            else if(Head.headcoord.y>maxy-1){
                Head.headcoord.y=0;
            }
        }
        swap=-Head.vector;
        part=Head.next;
        while (part!=null){
            swap2=part.vector;
            part.vector=swap;
            swap=swap2;
            part=part.next;
        }

    }

    public void eatturn(int maxx, int maxy){
        SCoords a=new SCoords();
        a.vector=-Head.vector;
        a.next=Head.next;
        Head.next=a;
        a.headcoord=new Coords(0,0,id);
        if(Head.vector==1||Head.vector==-1){
            Head.headcoord.x=Head.headcoord.x+Head.vector;
            if(Head.headcoord.x<0){
                Head.headcoord.x=maxx-1;
            }
            else if(Head.headcoord.x>maxx-1){
                Head.headcoord.x=0;
            }
        }
        else {
            Head.headcoord.y=Head.headcoord.y+(Head.vector/2);
            if(Head.headcoord.y<0){
                Head.headcoord.y=maxy-1;
            }
            else if(Head.headcoord.y>maxy-1){
                Head.headcoord.y=0;
            }
        }
    }

    public void AddPart(int vector){
        SCoords a=new SCoords();
        SCoords part=Head;
        while (part.next!=null){
            part=part.next;
        }
        a.next=null;
        a.vector=vector;
        a.headcoord=new Coords(0,0,id);
        part.next=a;
    }

    public int getId(){return id;}

}
