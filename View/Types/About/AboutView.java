package View.Types.About;

import javax.swing.*;
import java.awt.*;

public class AboutView {
    public AboutView(JFrame MyWindow){
        Container container= MyWindow.getContentPane();
        container.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        container.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.CENTER;
        constraints.gridy   = 0  ;
        constraints.gridx = 0;
        JTextArea table=new JTextArea("Ha-ha");
        table.setFont(new Font("Dialog", Font.PLAIN, 15));
        container.add(table, constraints);
        MyWindow.setVisible(true);
    }
}
