import java.io.*;
import java.net.Socket;

public class Client {
    private Socket socket;

    public Client(String addr,int port)throws IOException {
        socket = new Socket(addr,port);
    }
    public void loadFile(String fileName,DataOutputStream dataOutputStream)throws IOException{
        File file = new File(fileName);
        if (file.length() != 0) {
            dataOutputStream.writeLong(file.length());
            dataOutputStream.writeUTF(file.getName());
            FileInputStream inputFile = new FileInputStream(file);
            byte[] buffer = new byte[64 * 1024];
            int count;
            while ((count = inputFile.read(buffer)) != -1) {
                dataOutputStream.write(buffer, 0, count);
            }
            dataOutputStream.flush();
            inputFile.close();
            dataOutputStream.close();
            System.out.println(">>File transferred " + fileName);
        } else{
            System.out.println(">>You try:(");
        }
    }
    public void work(){
        try(
                DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in))){
            String fileName = "";
            System.out.println(">>Enter file name: ");
            fileName = bufferedReader.readLine();
            if (fileName.isEmpty() || fileName.getBytes().length > 4096){
                System.out.println(">>Incorrect fileName try again.");
            }
            loadFile(fileName,dataOutputStream);
        } catch(Exception e){
            System.out.println(e);
        }
    }
    public static void main(String[] args) throws Exception {
        try {
            if (args.length < 2)
                throw new Exception(">>Need more arguments");
            Client client = new Client(args[0],Integer.parseInt(args[1]));
            client.work();
        }catch (Exception e){
            System.out.println(e);
        }
    }
}
