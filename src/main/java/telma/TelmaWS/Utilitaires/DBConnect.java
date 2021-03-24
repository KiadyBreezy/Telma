package telma.TelmaWS.Utilitaires;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DBConnect {
	
	/*static String url = "jdbc:postgresql://localhost:5432/telma";
    static String user = "postgres";
    static String password = "123456";
    
     public static Connection getConnectionPostgres()throws Exception{
        Connection connection = DriverManager.getConnection(url, user, password);
        connection.setAutoCommit(false);
        return connection;
     }*/
	
	 public static Connection getConnectionPostgres()throws Exception{
		String url = "jdbc:postgresql://ec2-34-195-233-155.compute-1.amazonaws.com:5432/df1n7dvg83ve1c";
		Properties props = new Properties();
		props.setProperty("user", "cumyyevdqjvynx");
		props.setProperty("password", "075017bd902ab1368ae9bccd2201e3666a71d3ce636af2f2bf44155eae83b7fc");
		Connection con = DriverManager.getConnection(url, props);
		con.setAutoCommit(false);
		return con;
	 }
	
}
