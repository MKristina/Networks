package View.Types.FieldView.FieldControl;

import Classes.Bases.BaseForWindow;
import Field.GameFieldBaze;


public class FieldParent extends Thread{
    BaseForWindow BaseFW;
    GameFieldBaze MyFieldBase;
    int TimeToTurn;
    public boolean end=false;

    public FieldParent(BaseForWindow InBase, GameFieldBaze FieldBase){
        BaseFW =InBase;
        MyFieldBase =FieldBase;
        TimeToTurn= BaseFW.base.GetConfig().state_delay_ms;
    }

    public FieldParent() {
    }
}
