package servlet.jdbc;

import servlet.util.ConnectionInfo;
import servlet.util.DbUtil;
import servlet.util.FileUtil;

import java.sql.*;

public class Main {

    public static void main(String[] args) throws Exception {
        createSchema();
    }

    private static void createSchema() throws SQLException {
        ConnectionInfo connectionInfo = DbUtil.loadConnectionInfo();

        Connection conn = DriverManager.getConnection(
                connectionInfo.getUrl(),
                connectionInfo.getUser(),
                connectionInfo.getPass());

        try (conn; Statement stmt = conn.createStatement()) {

            String sql1 = FileUtil.readFileFromClasspath("schema.sql");
            String sql2 = FileUtil.readFileFromClasspath("data.sql");

            stmt.executeUpdate(sql1);
            stmt.executeUpdate(sql2);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
