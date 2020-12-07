package MyProtoClass;

import java.io.*;

public class ToByteAndBack {


    public byte[] GameMessageToByte(SnakesProto.GameMessage in){
        ByteArrayOutputStream baos = new ByteArrayOutputStream(2048);
        ObjectOutputStream oos = null;
        byte[] a=null;
        try {
            oos = new ObjectOutputStream(baos);
            oos.writeObject(in);
            a=baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return a;

    }

    public SnakesProto.GameMessage ByteToGameMessage(byte[] in){
        ObjectInputStream ois = null;
        SnakesProto.GameMessage recvMessage=null;
        try {
            ois = new ObjectInputStream(new ByteArrayInputStream(in));
            recvMessage = (SnakesProto.GameMessage) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return  recvMessage;
    }

}
