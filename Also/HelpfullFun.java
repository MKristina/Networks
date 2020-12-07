package Also;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class HelpfullFun {
    ImageIcon BarrierSmall;
    ImageIcon freeSmall;
    ImageIcon foodSmall;
    ImageIcon MeSmall;
    ImageIcon MeHeadSmall;
    ImageIcon EnemySmall;
    ImageIcon EnemyHeadSmall;
    ImageIcon BarrierMed;
    ImageIcon freeMed;
    ImageIcon foodMed;
    ImageIcon MeMed;
    ImageIcon MeHeadMed;
    ImageIcon EnemyMed;
    ImageIcon EnemyHeadMed;
    ImageIcon BarrierBig;
    ImageIcon freeBig;
    ImageIcon foodBig;
    ImageIcon MeBig;
    ImageIcon MeHeadBig;
    ImageIcon EnemyBig;
    ImageIcon EnemyHeadBig;

    {
        try {
            BarrierSmall= new ImageIcon(ImageIO.read(new File("src/Resources/png/BarrierSmall.png")));
            freeSmall = new ImageIcon(ImageIO.read(new File("src/Resources/png/FreeSmall.png")));
            foodSmall = new ImageIcon(ImageIO.read(new File("src/Resources/png/FoodSmall.png")));
            MeSmall = new ImageIcon(ImageIO.read(new File("src/Resources/png/Snake2Small.png")));
            MeHeadSmall = new ImageIcon(ImageIO.read(new File("src/Resources/png/Snake2HeadSmall.png")));
            EnemySmall = new ImageIcon(ImageIO.read(new File("src/Resources/png/Snake1Small.png")));
            EnemyHeadSmall = new ImageIcon(ImageIO.read(new File("src/Resources/png/Snake1HeadSmall.png")));
            BarrierMed= new ImageIcon(ImageIO.read(new File("src/Resources/png/BarrierMed.png")));
            freeMed = new ImageIcon(ImageIO.read(new File("src/Resources/png/FreeMed.png")));
            foodMed = new ImageIcon(ImageIO.read(new File("src/Resources/png/FoodMed.png")));
            MeMed = new ImageIcon(ImageIO.read(new File("src/Resources/png/Snake2Med.png")));
            MeHeadMed = new ImageIcon(ImageIO.read(new File("src/Resources/png/Snake2HeadMed.png")));
            EnemyMed = new ImageIcon(ImageIO.read(new File("src/Resources/png/Snake1Med.png")));
            EnemyHeadMed = new ImageIcon(ImageIO.read(new File("src/Resources/png/Snake1HeadMed.png")));
            BarrierBig= new ImageIcon(ImageIO.read(new File("src/Resources/png/BarrierBig.png")));
            freeBig = new ImageIcon(ImageIO.read(new File("src/Resources/png/FreeBig.png")));
            foodBig = new ImageIcon(ImageIO.read(new File("src/Resources/png/FoodBig.png")));
            MeBig = new ImageIcon(ImageIO.read(new File("src/Resources/png/Snake2Big.png")));
            MeHeadBig = new ImageIcon(ImageIO.read(new File("src/Resources/png/Snake2HeadBig.png")));
            EnemyBig = new ImageIcon(ImageIO.read(new File("src/Resources/png/Snake1Big.png")));
            EnemyHeadBig = new ImageIcon(ImageIO.read(new File("src/Resources/png/Snake1HeadBig.png")));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void SetSmallPng(JLabel button, int what){
        if(what==0){
            button.setIcon(freeSmall);
        }
        else if(what==-1){
            button.setIcon(BarrierSmall);
        }
        else if(what==1){
            button.setIcon(foodSmall);
        }
        else if(what==2){
            button.setIcon(EnemySmall);
        }
        else if(what==3){
            button.setIcon(MeSmall);
        }
        else if(what==20){
            button.setIcon(EnemyHeadSmall);
        }
        else if(what==30){
            button.setIcon(MeHeadSmall);
        }
    }

    void SetMedPng(JLabel button, int what){
        if(what==0){
            button.setIcon(freeMed);
        }
        else if(what==-1){
            button.setIcon(BarrierMed);
        }
        else if(what==1){
            button.setIcon(foodMed);
        }
        else if(what==2){
            button.setIcon(EnemyMed);
        }
        else if(what==3){
            button.setIcon(MeMed);
        }
        else if(what==20){
            button.setIcon(EnemyHeadMed);
        }
        else if(what==30){
            button.setIcon(MeHeadMed);
        }
    }

    void SetBigPng(JLabel button, int what){
        if(what==0){
            button.setIcon(freeBig);
        }
        else if(what==-1){
            button.setIcon(BarrierBig);
        }
        else if(what==1){
            button.setIcon(foodBig);
        }
        else if(what==2){
            button.setIcon(EnemyBig);
        }
        else if(what==3){
            button.setIcon(MeBig);
        }
        else if(what==20){
            button.setIcon(EnemyHeadBig);
        }
        else if(what==30){
            button.setIcon(MeHeadBig);
        }
    }

    public void setpng(JLabel button, int what, int x, int y) throws IOException {
        if(x>61||y>51){
            SetSmallPng(button,what);
        }
        else if(x>40||y>30){
            SetMedPng(button,what);
        }
        else{
            SetBigPng(button,what);
        }
    }
}
