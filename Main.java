
public class Main
{
    public static void main(String[] args)
    {
        Proxy proxy = new Proxy(ArgResolver.resolve(args));
        try
        {
            proxy.start();
        } catch (IllegalArgumentException exc) {
            proxy.stop();
            System.out.println("Usage: " + ArgResolver.getUsage());
        }
    }
}
