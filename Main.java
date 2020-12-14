public class Main
{
    public static void main(String[] args)
    {
        try
        {
            Proxy proxy = new Proxy(ArgResolver.resolve(args));
            proxy.start();
        } catch (IllegalArgumentException exc) {
            System.out.println("Usage: " + ArgResolver.getUsage());
        }
    }
}
