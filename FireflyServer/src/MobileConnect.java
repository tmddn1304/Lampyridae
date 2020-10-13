import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MobileConnect implements Runnable {
    private Socket moblieSocket;

    private InputStreamReader mobileStreamReader;

    private BufferedReader mobileReader;

    private PrintWriter mobileWriter;

    private ResultSet resultSet;

    private Connection connection;

    private Statement statement;

    MobileConnect(Socket mobileSocket) {
        super();
        this.moblieSocket = mobileSocket;
    }   //MobileConnect(Socket mobileSocket)

    @Override
    public void run() {

        try {
            mobileStreamReader = new InputStreamReader(moblieSocket.getInputStream());

            mobileWriter = new PrintWriter(moblieSocket.getOutputStream(), true);

            mobileReader = new BufferedReader(mobileStreamReader);

            ConnectDB connectDB = new ConnectDB();
            connection = connectDB.getConnection();

            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            String[] inputDataArray = mobileReader.readLine().split(",");

            switch (inputDataArray[0]) {
                case "admin1":
                    resultSet = statement.executeQuery("SELECT * FROM STREETLAMP WHERE LIGHTNUM LIKE '" + (inputDataArray.length == 2 ? inputDataArray[1] : "") + "%' ORDER BY LIGHTNUM ASC");
                    resultSet.last();

                    mobileWriter.println(resultSet.getRow());
                    resultSet.beforeFirst();

                    while (resultSet.next()) {
                        //System.out.println("가로등명 : "+ rs.getString(1) + "  ADDRESS : " + rs.getString(2) + " MANAGER : " + rs.getString(3) + " MANAGERNUMBER : " + rs.getString(4));
                        mobileWriter.println(resultSet.getString(1));
                        mobileWriter.println(resultSet.getString(2));
                        mobileWriter.println(resultSet.getString(3));
                        mobileWriter.println(resultSet.getString(4));
                    }

                    break;

                case "admin2":
                    resultSet = statement.executeQuery("SELECT LIGHTNUM, COUNT FROM (SELECT LIGHTNUM, COUNT FROM STREETLAMP WHERE LIGHTNUM like '" + inputDataArray[1] + "%' ORDER BY COUNT DESC) WHERE ROWNUM <=5");

                    while (resultSet.next()) {
                        //System.out.println("가로등명 : " + resultSet.getString(1) + "  count : " + resultSet.getString(2));

                        mobileWriter.println(resultSet.getString(1));
                        mobileWriter.println(resultSet.getString(2));
                    }

                    break;

                case "client":
                    resultSet = statement.executeQuery("UPDATE STREETLAMP SET COUNT = COUNT + 1 WHERE LIGHTNUM ='" + inputDataArray[1] + "'");

                    //FireflyServer.streetLampAction(inputDataArray[1]);

                    break;
            }

        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }

        close();

    }   //run()

    private void close() {
        try {
            if(resultSet != null) {
                resultSet.close();
            }

            if(statement != null) {
                statement.close();
            }

            if(connection != null) {
                connection.close();
            }

            if(mobileStreamReader != null) {
                mobileStreamReader.close();
            }

            if(mobileReader != null) {
                mobileReader.close();
            }

            if(mobileWriter != null) {
                mobileWriter.close();
            }

            if(moblieSocket != null) {
                moblieSocket.close();
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }

    }   //close()

}
