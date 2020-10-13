import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MobileSocketConnect implements Runnable {
    private ServerSocket mobileSocketServer;
    private boolean isLoop = true;

    @Override
    public void run() {
        MetaData metaData = MetaData.getInstance();

        try {
            mobileSocketServer = new ServerSocket(metaData.getMobileConnectPort());

            while (isLoop) {
                Socket mobileSocket = mobileSocketServer.accept();
                MobileConnect mobileConnect = new MobileConnect(mobileSocket);
                (new Thread(mobileConnect)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }   //run()

    void close() {
        isLoop = false;
        try {
            if(mobileSocketServer != null) {
                mobileSocketServer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }   //close()

}
