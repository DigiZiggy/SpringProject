package servlet.jdbc;

import servlet.util.ConnectionInfo;
import servlet.util.DataSourceProvider;
import servlet.util.DbUtil;

import java.sql.SQLException;

public class BasicDataSourceExample {
    public static void main(String[] args) throws SQLException {

        ConnectionInfo connectionInfo = DbUtil.loadConnectionInfo();

        DataSourceProvider.setConnectionInfo(connectionInfo);
    }
}
