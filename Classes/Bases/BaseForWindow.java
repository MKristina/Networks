package Classes.Bases;

import SendAndReceive.MultiSocket;
import View.Types.FieldView.FieldControl.FieldParent;

import javax.swing.*;
import java.awt.*;

public class BaseForWindow {
    public boolean HaveVField=false;
    public JFrame MyWindow=new JFrame();
    public Base base;
    public FieldParent VField;
    public MultiSocket SendAboutGame;

    public BaseForWindow(Base InBaze){
        MyWindow.setSize(1200, 750);
        MyWindow.setLocationRelativeTo(null);
        MyWindow.setLayout(new GridBagLayout());
        MyWindow.setTitle("Snakes");
        MyWindow.setVisible(true);
        base =InBaze;
    }

}
