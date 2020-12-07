package View.Types.FindGame;

import MyProtoClass.SnakesProto;
import SendAndReceive.SendAndReceiveJoin;
import View.Types.ErrorWindow.OpenError;
import View.Types.FieldView.FieldControl.OtherFieldThread;
import View.Window.ReCreateWindow;

import javax.swing.*;
import java.awt.*;
import java.net.InetAddress;

public class TryToJoin {
    public TryToJoin(ReCreateWindow BaseRC, SnakesProto.GamePlayers PList, InetAddress IP){
        SendAndReceiveJoin receive=new SendAndReceiveJoin();
        SnakesProto.GameMessage mess= receive.work(BaseRC,PList,IP,BaseRC.BaseFW.base);
        if(mess==null){
            JLabel Text=new JLabel();
            BaseRC.DellAll();
            Text.setText("Can't connect to server");
            Container container=BaseRC.BaseFW.MyWindow.getContentPane();
            container.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
            container.setLayout(new GridBagLayout());
            GridBagConstraints constraints = new GridBagConstraints();
            constraints.fill = GridBagConstraints.CENTER;
            constraints.gridy= 0;
            constraints.gridx =0;
            container.add(Text, constraints);
        }else if(mess.getTypeCase()== SnakesProto.GameMessage.TypeCase.ERROR){
            new OpenError().work(mess.getError().getErrorMessage());
        }else if(mess.getTypeCase()== SnakesProto.GameMessage.TypeCase.ACK){
            BaseRC.BaseFW.base.MeId=mess.getReceiverId();
            BaseRC.BaseFW.base.MasterId=mess.getSenderId();
            BaseRC.DellAll();
            new OtherFieldThread(BaseRC.BaseFW, null, IP.getHostAddress(),receive.ToWho).start();
        }
    }
}
