package SendAndReceive;


import java.net.DatagramSocket;

//Таймер для подключения к родителю. Если за 11 секунд не подключился, то
//сообщить об этом и законччить
public class TimerToDontParentSend extends Thread {
    DatagramSocket Me;

    public TimerToDontParentSend(DatagramSocket in){
       Me=in;
    }

    @Override
    public void run() {
        boolean endIt=true;
        try {
            sleep(3000);
        } catch (InterruptedException e) {
            endIt=false;
        }
        if(endIt){

           Me.close();
        }
    }
}
