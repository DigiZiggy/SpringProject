package servlet.util;

import java.util.Properties;

public class DbUtil {

    public static ConnectionInfo loadConnectionInfo() {
        Properties properties = PropertyLoader.loadApplicationProperties();

        return new ConnectionInfo(
                properties.getProperty("dbUrl"),
                properties.getProperty("dbUser"),
                properties.getProperty("dbPassword"));
    }
}
