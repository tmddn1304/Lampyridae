import java.util.Scanner;

public class FireflyServer {
    private static MobileSocketConnect mobileSocketConnect;
    //private static StreetLampSocketConnect streetLampSocketConnect;

    private static boolean isLoop = true;

    public static void main(String[] args) {
        //DBUrl과 사용자id 비밀번호를 입력받아 작동
        //MetaData에 두개 서버와 통신할 소켓번호 입력
        Scanner scanner = new Scanner(System.in);
        MetaData metaData = MetaData.getInstance();

        System.out.print("모바일 연결 포트 번호 : ");
        metaData.setMobileConnectPort(Integer.parseInt(scanner.next()));

        //System.out.print("보안등 서버 연결 포트 번호 : ");
        //metaData.setStreetLampConnectPort(Integer.parseInt(scanner.next()));

        //System.out.print("연결할 서버 IP : ");
        metaData.setSqlUrl("jdbc:oracle:thin:@127.0.0.1:1521:xe");

        System.out.print("DB ID : ");
        metaData.setDbUser(scanner.next());

        System.out.print("DB 비밀번호 : ");
        metaData.setDbPwd(scanner.next());

        System.out.println("종료할 경우 exit 입력");

        mobileSocketConnect = new MobileSocketConnect();
        (new Thread(mobileSocketConnect)).start();

        //streetLampSocketConnect = new StreetLampSocketConnect();
        //(new Thread(streetLampSocketConnect)).start();

        do {
            String inputData = scanner.next();
            if(inputData.equals("exit")) {
                isLoop = false;
                close();
            }
        } while (isLoop);

    }   //main(String[] args)

    /*
    static void streetLampAction(String streetLampId) {
        streetLampSocketConnect.sendData(streetLampId);
    }   //streetLampAction(String streetLampId)
    */

    private static void close() {
        if(mobileSocketConnect != null) {
            mobileSocketConnect.close();
        }

        /*
        if(streetLampSocketConnect != null) {
            streetLampSocketConnect.close();
        }
        */
    }   //close()

}
