package View.Window;

import Classes.Bases.BaseForWindow;
import View.Types.About.AboutView;
import View.Types.FindGame.ViewFoundedGames;
import View.Types.NewStartView.StartGame;

import javax.swing.*;

public class ReCreateWindow {
    public BaseForWindow BaseFW;

    public ReCreateWindow(BaseForWindow InBase){
        BaseFW =InBase;
        AddMenBar();
    }


    void AddMenBar(){
        JMenuBar MyMenBar=new JMenuBar();
        JMenuItem start=new JMenuItem();
        JMenuItem exit=new JMenuItem();
        JMenuItem about=new JMenuItem();
        JMenuItem found=new JMenuItem();

        about.setText("About");
        exit.setText("Exit");
        found.setText("Find game");
        start.setText("Start Game");

        MyMenBar.add(start);
        MyMenBar.add(found);
        MyMenBar.add(about);
        MyMenBar.add(exit);

        about.addActionListener((e) -> {
            if(BaseFW.HaveVField){
                BaseFW.VField.end=true;
                if(BaseFW.base.IsMaster) {
                    BaseFW.base.IsMaster=false;
                    BaseFW.SendAboutGame.end = true;
                }
                BaseFW.HaveVField=false;
            }

            DellAll();
            new AboutView(BaseFW.MyWindow);
        });
        exit.addActionListener((e) -> {
            if(BaseFW.HaveVField){
                BaseFW.VField.end=true;
                if(BaseFW.base.IsMaster) {
                    BaseFW.base.IsMaster=false;
                    BaseFW.SendAboutGame.end = true;
                }
                BaseFW.HaveVField=false;
            }
            DellAll();
            MyExit();
        });
        start.addActionListener((e) -> {
            if(BaseFW.HaveVField){
                BaseFW.VField.end=true;
                if(BaseFW.base.IsMaster) {
                    BaseFW.base.IsMaster=false;
                    BaseFW.SendAboutGame.end = true;
                }
                BaseFW.HaveVField=false;
            }
            DellAll();
            new StartGame(BaseFW, this);
        });
        found.addActionListener((e) -> {
            if(BaseFW.HaveVField){
                BaseFW.VField.end=true;
                if(BaseFW.base.IsMaster) {
                    BaseFW.base.IsMaster=false;
                    BaseFW.SendAboutGame.end = true;
                }
                BaseFW.HaveVField=false;
            }
            DellAll();
            new ViewFoundedGames(this);
        });
        BaseFW.MyWindow.setJMenuBar(MyMenBar);
        refresh();

    }

    public void refresh(){
        BaseFW.MyWindow.repaint();
        BaseFW.MyWindow.setVisible(true);
    }

    public void DellAll(){
        BaseFW.MyWindow.getContentPane().removeAll();
        AddMenBar();
    }

    void MyExit(){
        BaseFW.MyWindow.dispose();
    }
}
