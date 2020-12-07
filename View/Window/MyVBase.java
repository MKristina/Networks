package View.Window;

import Classes.Bases.BaseForWindow;

public class MyVBase {
    BaseForWindow BaseFW;

    public MyVBase(BaseForWindow InBase){
        BaseFW =InBase;
        ReCreateWindow ReCreator = new ReCreateWindow(BaseFW);
    }
}
