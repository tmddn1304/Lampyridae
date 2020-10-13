import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectDB {
    private Connection connection;

    public ConnectDB() {
        super();
        MetaData metaData = MetaData.getInstance();

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");

            connection = DriverManager.getConnection(metaData.getSqlUrl(), metaData.getDbUser(), metaData.getDbPwd());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }   //ConnectDB()

    public Connection getConnection() {
        return connection;
    }

}
