package View.Types.FindGame;

import Info.MyConfig;
import MyProtoClass.SnakesProto.GamePlayer;
import SendAndReceive.FindGame;
import SendAndReceive.Help.ForFind;
import View.Window.ReCreateWindow;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.List;

public class ViewFoundedGames {
    ReCreateWindow BaseRC;
    int k=0;
    public ViewFoundedGames(ReCreateWindow Rec){
        BaseRC =Rec;
        start();
    }
    void start(){
        ForFind Help=new ForFind();
        Container container= BaseRC.BaseFW.MyWindow.getContentPane();
        container.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        container.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.CENTER;
        JLabel a=new JLabel();
        constraints.gridy= 0;
        constraints.gridx =0;
        container.add(a, constraints);
        a.setText("Wait ");
        boolean haveany;
        try {
            haveany = new FindGame().receive(Help);
        } catch (IOException e) {
            haveany = false;
        }
        if (haveany) {
            HaveAny(Help);
        } else {
            NoAny();
        }
        k++;
    }



    void HaveAny(ForFind a){
        BaseRC.DellAll();
        Container container= BaseRC.BaseFW.MyWindow.getContentPane();
        container.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        container.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.CENTER;
        JLabel player=new JLabel();
        List<GamePlayer> PlayerList=a.mes.getAnnouncement().getPlayers().getPlayersList();
        StringBuilder Names= new StringBuilder();
        Names.append("Players:");
        for(GamePlayer one:PlayerList) {
           Names.append(one.getName()).append(";");
        }
        String AboutGame="";

        MyConfig conf=new MyConfig();
        conf.dead_food_prob=a.mes.getAnnouncement().getConfig().getDeadFoodProb();
        conf.food_per_player=a.mes.getAnnouncement().getConfig().getFoodPerPlayer();
        conf.food_static=a.mes.getAnnouncement().getConfig().getFoodStatic();
        conf.height=a.mes.getAnnouncement().getConfig().getHeight();
        conf.width=a.mes.getAnnouncement().getConfig().getWidth();
        conf.node_timeout_ms=a.mes.getAnnouncement().getConfig().getNodeTimeoutMs();
        conf.ping_delay_ms=a.mes.getAnnouncement().getConfig().getPingDelayMs();
        conf.state_delay_ms=a.mes.getAnnouncement().getConfig().getStateDelayMs();
        BaseRC.BaseFW.base.addconf(conf);

        AboutGame=AboutGame+a.mes.getAnnouncement().getConfig().getWidth() +"*"+a.mes.getAnnouncement().getConfig().getHeight()+" Field Size"+".";
        AboutGame=AboutGame+a.mes.getAnnouncement().getConfig().getStateDelayMs()/1000 +" sec to turn";
        JLabel about=new JLabel();
        JButton Join =new JButton();
        constraints.gridy= 0;
        constraints.gridx =0;
        container.add(player, constraints);
        constraints.gridy= 1;
        container.add(about, constraints);
        constraints.gridy= 2;
        container.add(Join, constraints);
        about.setText(AboutGame);
        Join.setText("Join the game");
        player.setText(Names+" ");
        Join.addActionListener(e->{
            new TryToJoin(BaseRC,a.mes.getAnnouncement().getPlayers(), a.IP);
        });

    }

    void  NoAny(){
        BaseRC.DellAll();
        Container container= BaseRC.BaseFW.MyWindow.getContentPane();
        container.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        container.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.CENTER;
        JLabel a=new JLabel();
        constraints.gridy= 0;
        constraints.gridx =0;
        container.add(a, constraints);
        a.setText("No game now ");

    }
}
