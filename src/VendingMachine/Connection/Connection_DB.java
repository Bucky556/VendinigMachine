package VendingMachine.Connection;

import java.sql.Connection;
import java.sql.DriverManager;

public class Connection_DB {
    public static Connection getConnection() throws Exception {
        String url = "jdbc:postgresql://localhost:5432/postgres";
        String user = "********";
        String password = "********";

        return DriverManager.getConnection(url, user, password);
    }
}
