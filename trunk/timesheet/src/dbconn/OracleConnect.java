package dbconn;
import java.sql.*;
//import oracle.jdbc.*;
//import oracle.jdbc.OracleDriver;
 
  public class OracleConnect
  {
    public static void main(String[] args)throws SQLException
    {
      try
      {
        Class.forName("oracle.jdbc.OracleDriver");
        //DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
        Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE","ams","ams");
		if(conn != null)
		{ System.out.println("I am Connected to oracle database");
			}
        conn.close();
        conn = null;
      } catch (Exception e) {
        System.out.println("ERROR : " + e);
        e.printStackTrace(System.out);
      }
    }
  }