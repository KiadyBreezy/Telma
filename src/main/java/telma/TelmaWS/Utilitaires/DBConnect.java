package telma.TelmaWS.Utilitaires;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnect {
	
	static String url = "jdbc:postgresql://localhost:5432/telma";
    static String user = "postgres";
    static String password = "123456";
    
     public static Connection getConnectionPostgres()throws Exception{
        Connection connection = DriverManager.getConnection(url, user, password);
        connection.setAutoCommit(false);
        return connection;
     }
     
     
     
     

}
