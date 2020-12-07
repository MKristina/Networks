package Field.Snakes;

import Field.Snakes.Snake;

public class SnakesBase {
    public Snake[] snakeMas=new Snake[0];

    public void addSnake(Snake any){
        Snake[] a=new Snake[snakeMas.length+1];
        int i=0;
        for(;i<snakeMas.length;i++){
            a[i]=snakeMas[i];
        }
        a[i]=any;
        snakeMas=a;
    }

    public void delI(int what){
        Snake[] a=new Snake[snakeMas.length-1];
        int o=0;
        for(int i=0;i<snakeMas.length;i++){
            if(snakeMas[i].getId()==what){
                o=-1;
            }
            else {
                a[i+o]=snakeMas[i];
            }
        }
        snakeMas=a;
    }

    public int GetMaxId(){
        int a=0;
        for(Snake one:snakeMas){
            if(a<one.id){
                a=one.id;
            }
        }
        return a;
    }

    public Snake[] GetSnakes(){
        return snakeMas;
    }

    public Snake GetById(int id){
        for (Snake snakeMa : snakeMas) {
            if (snakeMa.id == id) {
                return snakeMa;
            }
        }
        return null;
    }
}
