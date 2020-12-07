package Also;

import Field.PartOfOb.SCoords;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyL extends JFrame implements KeyListener {
    SCoords Me;
    public boolean end=false;
    int where;
    public boolean isViewer=false;
    public  KeyL(SCoords inSnake){
        Me=inSnake;
        where=Me.vector;
    }
    public KeyL(){}

    @Override
    public void keyTyped(KeyEvent e) {
        if(!end){
            if(e.getKeyChar()=='w'||e.getKeyChar()=='ц'){
                where=-2;
            }else if(e.getKeyChar()=='a'||e.getKeyChar()=='ф'){
                where=-1;
            }else if(e.getKeyChar()=='d'||e.getKeyChar()=='в'){
                where=1;
            }else if(e.getKeyChar()=='s'||e.getKeyChar()=='ы'){
                where=2;
            }else if(e.getKeyChar()=='v'||e.getKeyChar()=='м'){
                isViewer=true;
            }
        }


    }

    @Override
    public void keyPressed(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}

    public void getTurn(){
        if((Me.vector!=-where)) {
            Me.vector = where;
        }
    }

    public int getVector(){
        return where;
    }
}
