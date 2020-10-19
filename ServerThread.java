import java.io.*;
import java.net.Socket;

public class ServerThread extends Thread {
    private Socket clientSocket;
    DataInputStream inStream;
    DataOutputStream outStream;
    public ServerThread(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        inStream = new DataInputStream(clientSocket.getInputStream());
        outStream = new DataOutputStream(clientSocket.getOutputStream());
    }
    private void receiveFile(File file, long fileSize) {
        try {
            byte[] buffer = new byte[64 * 1024];
            FileOutputStream outputFile = new FileOutputStream(file);
            int count, countBytesForSpeed = 0;
            long total = 0;
            long currentTime, lastTime, endTime, startTime = System.currentTimeMillis();
            lastTime = startTime;       //когда последний раз производился вывод скорости
            while ((count = inStream.read(buffer)) != -1) {
                total += count;
                countBytesForSpeed += count;
                currentTime = System.currentTimeMillis();
                if(currentTime - lastTime > 3000){
                    System.out.println(">>Downloading speed: " + Math.round(countBytesForSpeed / (((currentTime - lastTime) * 1.0) / 1000) / 1024) + "кб/c" + "| FileName: " + file.getName()+ "| from Client: #" + this.getId());
                    lastTime = currentTime;
                    countBytesForSpeed = 0;
                }
                outputFile.write(buffer, 0, count);
            }
            endTime = System.currentTimeMillis();       //для total скорости передачи файла
            System.out.println(">>Time spend on file " + Math.round((total / (((endTime - startTime) * 1.0) / 1000)) / 1024) + "кб/c"+ " FileName: " + file.getName()+ "| from Client: #" + this.getId());
            outputFile.flush();
            System.out.println(">>File " + file.getName() +" delivered "+ " from # " + this.getId());
            if (total != fileSize){
                System.out.println(">>File integrity error: " + file.getName()+ " from # " + this.getId());
                file.delete();
            }
            outputFile.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    private File checkFileName(File file){
        if (file.exists()){
            File file1 = new File("("+this.getId()+")" + file.getAbsoluteFile());
            return file1;
        }
        return file;
    }
    public void run(){
        try {
            System.out.println(">>Connected with host: " + clientSocket.getInetAddress() + "\nPort: " + clientSocket.getPort() + "\nNumber: " + this.getId() + " :D");
            long fileSize = inStream.readLong();
            String fileName = inStream.readUTF();
            System.out.println(">>Starting receiving: " + fileName + " from " + this.getId());
            File file = new File("C:\\Users\\inet\\Desktop\\3 курс\\Seti\\lab2\\Uploads\\" + fileName);
            file = checkFileName(file);
            receiveFile(file,fileSize);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }finally {
            try {
                clientSocket.close();
                inStream.close();
                outStream.close();
            }catch (IOException e){
            }
            System.out.println("---> Client # "+this.getId()+" disconnected :«");
        }

    }
}
