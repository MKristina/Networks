import Classes.Bases.BaseForWindow;
import Classes.Bases.Base;
import View.Window.MyVBase;

public class Starter {
    public static void main(String[] args)
    {
        Base base=new Base();
        base.Name="111";
        base.Socket=50888;
        BaseForWindow BaseFW=new BaseForWindow(base);
        MyVBase VBase=new MyVBase(BaseFW);
    }
}