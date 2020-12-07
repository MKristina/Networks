package Classes.Bases;

import Classes.MessGetter;

public class SendBazeOther {
    public MessGetter SteerMeg=null;
    public MessGetter ErrorMsg=null;
    public MessGetter RoleChangeViewer=null;
    public long LastSenMaster=System.currentTimeMillis();
    public long LastSendToMaster=System.currentTimeMillis();
}
