package Also;

import Field.PartOfOb.SCoords;

public class Bot extends Thread{
    SCoords Me;
    int time;
    public Bot(SCoords insnake, int TurnTime ) {
        Me = insnake;
        time =TurnTime;
    }

    @Override
    public void run() {
        int where;
        while (Me!=null){
            where=(int) (-3+Math.random()*5);
            while (where==0||-where==Me.vector){
                where=(int) (-3+Math.random()*5);
            }
            Me.vector=where;
            try {sleep(time);} catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


}
