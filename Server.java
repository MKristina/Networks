import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    static ExecutorService executorService;
    private int listenPort;
    private int countOfClients;

    public Server(int listenPort){
        this.listenPort = listenPort;
        this.executorService = Executors.newCachedThreadPool();
    }
    public void start(){
        countOfClients = 0;
        System.out.println(">>Server is starting...");
        try (ServerSocket serverSocket = new ServerSocket(listenPort)){
            while (!serverSocket.isClosed()){
                System.out.println(">>Server waiting for connections...");
                Socket client = serverSocket.accept();
                countOfClients++;
                executorService.execute(new ServerThread(client));
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        finally {
            System.out.println(">>Server finished.");
        }
    }
    public static void main(String args[]){
        try {
            if (args.length < 1)
                throw new Exception(">>Need more arguments...");
            Server server = new Server(Integer.parseInt(args[0]));
            server.start();
        }catch (Exception e){
            System.out.println(e);
        }
    }
}
