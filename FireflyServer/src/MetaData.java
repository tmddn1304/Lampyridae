public class MetaData {
    //--------------------------- 싱글톤 코드 시작
    private static MetaData ourInstance = new MetaData();

    public static MetaData getInstance() {
        return ourInstance;
    }

    private MetaData() { }
    //--------------------------- 싱글톤 코드 끝

    private int mobileConnectPort;
    //private int streetLampConnectPort;

    private String sqlUrl;
    private String dbUser;
    private String dbPwd;

    int getMobileConnectPort() {
        return mobileConnectPort;
    }

    void setMobileConnectPort(int mobileConnectPort) {
        this.mobileConnectPort = mobileConnectPort;
    }

    /*
    int getStreetLampConnectPort() {
        return streetLampConnectPort;
    }

    void setStreetLampConnectPort(int streetLampConnectPort) {
        this.streetLampConnectPort = streetLampConnectPort;
    }
    */

    String getSqlUrl() {
        return sqlUrl;
    }

    void setSqlUrl(String sqlUrl) {
        this.sqlUrl = sqlUrl;
    }

    String getDbUser() {
        return dbUser;
    }

    void setDbUser(String dbUser) {
        this.dbUser = dbUser;
    }

    String getDbPwd() {
        return dbPwd;
    }

    void setDbPwd(String dbPwd) {
        this.dbPwd = dbPwd;
    }

}
