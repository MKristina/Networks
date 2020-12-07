package View.Types.ErrorWindow;

import javax.swing.*;
import java.awt.*;

public class OpenError {
    JFrame MyWindow;
    public void work(String ToPrint){
        MyWindow=new JFrame();
        MyWindow.setSize(500, 300);
        MyWindow.setLocationRelativeTo(null);
        MyWindow.setLayout(new GridBagLayout());
        MyWindow.setTitle("Error");
        JButton OK=new JButton("OK");
        JLabel Text=new JLabel(ToPrint);
        Container container=MyWindow.getContentPane();
        container.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        container.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.CENTER;
        constraints.gridy= 0;
        constraints.gridx =0;
        container.add(Text, constraints);
        constraints.gridy= 1;
        container.add(OK, constraints);
        OK.addActionListener(e->{
            MyWindow.setVisible(false);
            MyWindow=null;
        });
        MyWindow.setVisible(true);
    }
}
