package View.Types.FieldView.FieldRepaint;

import Also.HelpfullFun;
import Classes.Bases.BaseForWindow;
import Field.GameFieldBaze;
import Field.PartOfOb.Coords;
import Field.Snakes.Snake;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class FieldRepaint {
    int maxx;
    int maxy;
    JLabel[][] LabelMass;
    HelpfullFun help=new HelpfullFun();
    Container container;
    GridBagConstraints constraints;
    public FieldRepaint(BaseForWindow BaseFW, GameFieldBaze MyFieldBase){
        maxx=MyFieldBase.GetX();
        maxy= MyFieldBase.GetY();
        container= BaseFW.MyWindow.getContentPane();
        container.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        container.setLayout(new GridBagLayout());
        constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.CENTER;
        LabelMass=new JLabel[maxx+3][maxy+2];
        for (int i=0;i<maxx+2;i++){
            for(int j=0;j<maxy+2;j++){
                constraints.gridy= j;
                constraints.gridx =i;
                LabelMass[i][j]=new JLabel();
                container.add(LabelMass[i][j], constraints);
                if((i==0||i==maxx+1)||(j==0||j==maxy+1)){
                    try {
                        help.setpng(LabelMass[i][j], -1,maxx,maxy);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        constraints.gridx=maxx+2;
        for(int i=0;i<maxy+2;i++){
            constraints.gridy= i;
            LabelMass[maxx+2][i]=new JLabel();
            if(maxx>61||maxy>51){
                LabelMass[maxx+2][i].setPreferredSize(new Dimension(400,6));
                LabelMass[maxx+2][i].setFont(new Font("Courier New", Font.PLAIN, 7));
            }
            else if(maxx>40||maxy>30){
                LabelMass[maxx+2][i].setPreferredSize(new Dimension(400,14));
                LabelMass[maxx+2][i].setFont(new Font("Courier New", Font.PLAIN, 15));
            }
            else{
                LabelMass[maxx+2][i].setPreferredSize(new Dimension(400,24));
                LabelMass[maxx+2][i].setFont(new Font("Courier New", Font.PLAIN, 25));
            }
            container.add(LabelMass[maxx+2][i], constraints);
        }
    }

    public void turn(BaseForWindow BaseFW, GameFieldBaze MyFieldBase, Snake[] Snakes){
        Coords[][] field=MyFieldBase.retField();
        for (int i=1;i<maxx+1;i++){
            for(int j=1;j<maxy+1;j++){
                try {
                    help.setpng(LabelMass[i][j], field[i-1][j-1].have_food,maxx,maxy);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        int y=0;
        for(int i=0;i<maxy+2;i++){
            LabelMass[maxx+2][i].setText("");
        }
        for(Snake one: Snakes){
            if(BaseFW.base.GetUsers().GetById(one.id)!=null&&BaseFW.base.GetUsers().GetById(one.id).Role!=3) {
                LabelMass[maxx + 2][y].setText(BaseFW.base.GetUsers().GetById(one.id).Name + ":" + one.Score+" Role:"+BaseFW.base.GetUsers().GetById(one.id).Role+". Type"+BaseFW.base.GetUsers().GetById(one.id).Type);
                y++;
            }
        }
    }
}
