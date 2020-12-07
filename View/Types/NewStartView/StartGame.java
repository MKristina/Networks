package View.Types.NewStartView;

import Classes.Bases.BaseForUser;
import Classes.Bases.BaseForWindow;
import Field.GameFieldBaze;
import Field.Snakes.NewSnake;
import Field.Snakes.SnakesBase;
import Info.MyConfig;
import View.Types.FieldView.FieldControl.MasterFieldThread;
import View.Window.ReCreateWindow;

import javax.swing.*;
import java.awt.*;

public class StartGame {
    JTextField[] ConfText = new JTextField[8];
    JButton[] ConfButton = new JButton[9];
    BaseForWindow BaseFW;
    ReCreateWindow ReCreator;

    public StartGame(BaseForWindow InBaze, ReCreateWindow InC){
        BaseFW =InBaze;
        ReCreator=InC;
        Container container = BaseFW.MyWindow.getContentPane();
        container.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        container.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.CENTER;
        for (int i = 0; i < 9; i++) {
            constraints.gridy = i;
            switch (i) {
                case (0) -> {
                    ConfButton[i] = new JButton("Width( >=10 and >=100. Default: 40)                ");
                    ConfText[i] = new JTextField("40      ");
                    ConfText[i].setSize(100, 100);
                    constraints.gridx = 0;
                    container.add(ConfButton[i], constraints);
                    constraints.gridx = 1;
                    container.add(ConfText[i], constraints);
                }
                case (1) -> {
                    ConfButton[i] = new JButton("Height( >=10 and >=100. Default: 30)              ");
                    ConfText[i] = new JTextField("30      ");
                    constraints.gridx = 0;
                    container.add(ConfButton[i], constraints);
                    constraints.gridx = 1;
                    container.add(ConfText[i], constraints);
                }
                case (2) -> {
                    ConfButton[i] = new JButton("Food Static(>=0 and >=100. Default: 1)          ");
                    ConfText[i] = new JTextField("1        ");
                    constraints.gridx = 0;
                    container.add(ConfButton[i], constraints);
                    constraints.gridx = 1;
                    container.add(ConfText[i], constraints);
                }
                case (3) -> {
                    ConfButton[i] = new JButton("(Food Per Player Chance/10) % (Default: 1%)");
                    ConfText[i] = new JTextField("10      ");
                    constraints.gridx = 0;
                    container.add(ConfButton[i], constraints);
                    constraints.gridx = 1;
                    container.add(ConfText[i], constraints);
                }
                case (4) -> {
                    ConfButton[i] = new JButton("Turn time in ms(<=10000. Default: 1000)         ");
                    ConfText[i] = new JTextField("1000  ");
                    constraints.gridx = 0;
                    container.add(ConfButton[i], constraints);
                    constraints.gridx = 1;
                    container.add(ConfText[i], constraints);
                }
                case (5) -> {
                    ConfButton[i] = new JButton("Chance for food in dead in %(Default: 10%) ");
                    ConfText[i] = new JTextField("10      ");
                    constraints.gridx = 0;
                    container.add(ConfButton[i], constraints);
                    constraints.gridx = 1;
                    container.add(ConfText[i], constraints);
                }
                case (6) -> {
                    ConfButton[i] = new JButton("Ping mess in ms(<=10000. Default: 100)          ");
                    ConfText[i] = new JTextField("100    ");
                    constraints.gridx = 0;
                    container.add(ConfButton[i], constraints);
                    constraints.gridx = 1;
                    container.add(ConfText[i], constraints);
                }
                case (7) -> {
                    ConfButton[i] = new JButton("Death time in ms(<=10000. Default: 800)         ");
                    ConfText[i] = new JTextField("800    ");
                    constraints.gridx = 0;
                    container.add(ConfButton[i], constraints);
                    constraints.gridx = 1;
                    container.add(ConfText[i], constraints);
                }
                case (8) -> {
                    ConfButton[i] = new JButton("Start");
                    constraints.gridx = 0;
                    container.add(ConfButton[i], constraints);
                    ConfButton[i].addActionListener((e) -> {
                        ReCreator.DellAll();
                        ApplyConfig();
                        CreateGame();
                    });
                }
            }


        }
        BaseFW.MyWindow.setVisible(true);
    }

    void ApplyConfig(){
        MyConfig conf=new MyConfig();
        String a;
        int i;
        for (i=0;i<3;i++){
            a=ConfText[i].getText().split(" ")[0];
            if(i==0&&Integer.parseInt(a)>=10&&Integer.parseInt(a)<=100){
                conf.width=Integer.parseInt(a);
            }
            else if(i==1&&Integer.parseInt(a)>=10&&Integer.parseInt(a)<=100) {
                conf.height=Integer.parseInt(a);
            }
            else if(i==2&&Integer.parseInt(a)>=0&&Integer.parseInt(a)<=100){
                conf.food_static=Integer.parseInt(a);
            }
        }
        a=ConfText[i].getText().split(" ")[0];
        if(Integer.parseInt(a)>=0&&Integer.parseInt(a)<=1000) {
            conf.food_per_player = Integer.parseInt(a) / 1000;
        }
        i++;
        a=ConfText[i].getText().split(" ")[0];
        if(Integer.parseInt(a)>=1&&Integer.parseInt(a)<=1000){
            conf.state_delay_ms= Integer.parseInt(a);
        }
        i++;
        a=ConfText[i].getText().split(" ")[0];
        if(Integer.parseInt(a)>=0&&Integer.parseInt(a)<=100){
            conf.dead_food_prob= Integer.parseInt(a)/1;
        }
        i++;
        for(;i<8;i++){
            a=ConfText[i].getText().split(" ")[0];
            if(i==6&&Integer.parseInt(a)>=1&&Integer.parseInt(a)<=1000){
                conf.ping_delay_ms=Integer.parseInt(a);
            }
            else if(i==7&&Integer.parseInt(a)>=1&&Integer.parseInt(a)<=1000){
                conf.node_timeout_ms=Integer.parseInt(a);
            }
        }
        BaseFW.base.addconf(conf);
    }

    void CreateGame(){
        GameFieldBaze ThisField=new GameFieldBaze(BaseFW.base);
        BaseFW.HaveVField=true;
        BaseFW.base.addsnakes(new SnakesBase());
        BaseFW.base.IsMaster=true;
        BaseFW.VField=new MasterFieldThread(BaseFW, ThisField);
        BaseFW.base.addUsers(new BaseForUser());
        new NewSnake(BaseFW.base,ThisField.retField(),true, true);
        BaseFW.base.iter=0;
        BaseFW.VField.start();

    }
}
