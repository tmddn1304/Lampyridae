import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class StreetLampSocketConnect implements Runnable {
    private ServerSocket streetLampServerServerSocket;
    private Socket streetLampServerSocket;

    private PrintWriter streetLampServerWriter;

    private boolean isLoop = true;

    @Override
    public void run() {
        /*
        MetaData metaData = MetaData.getInstance();

        try {
            streetLampServerServerSocket = new ServerSocket(metaData.getStreetLampConnectPort());
            streetLampServerSocket = streetLampServerServerSocket.accept();

            streetLampServerWriter = new PrintWriter(streetLampServerSocket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (isLoop) {

        }

        */
    }   //run()

    void sendData(String streetLampId) {
        if(streetLampServerSocket != null) {
            streetLampServerWriter.println(streetLampId);
        }
    }   //sendData(String streetLampId)

    void close() {
        isLoop = false;

        try {
            if (streetLampServerServerSocket != null) {
                streetLampServerServerSocket.close();
            }

            if (streetLampServerSocket != null) {
                streetLampServerSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }   //close()


}
