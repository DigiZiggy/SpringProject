package main;

import org.hsqldb.Server;

public class HsqlServer {

    public static void run() {
        Server server = new Server();
        server.setDatabasePath(0, "mem:db;sql.syntax_pgs=true");
        server.setDatabaseName(0, "db");
        server.start();
    }
}
