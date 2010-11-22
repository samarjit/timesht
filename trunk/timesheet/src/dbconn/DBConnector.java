package dbconn;

import java.io.File;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.sql.rowset.CachedRowSet;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import util.AMSProperties;

import com.sun.rowset.CachedRowSetImpl;

import dto.PrepstmtDTOArray;
import dto.PrepstmtDTO;


/**
 * This class is used to connect to the database and execute queries.
 *
 */
public class DBConnector { 
private void debug(int priority, String s){
	if(priority>0){
		System.out.println("DBConnecctor:"+s);
	}
}
/**
 * This function is used to return a connection with the database.
 * 
 */
private Connection getConnection()
{
	 
	
	Connection conn = null;
	try
    {
        
      // String driverName ="com.mysql.jdbc.Driver";
        String driverName ="oracle.jdbc.OracleDriver";
      // String url = "jdbc:mysql://localhost:3306/ams";
       //"jdbc:oracle:thin:@127.0.0.1:1521:XE"; 
        String userName = AMSProperties.get("databaseuser");
        String password = AMSProperties.get("databasepassword");
        String url = AMSProperties.get("databaseserverurl");
         
		
      /*  
        //Main xml config to get hibernate config file path
       Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File("src/cbrca/databaseconfig.xml"));
       //hibernate config file 
       Document doc2 = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File("src/"+doc.getElementsByTagName("HibernateConfigFile").item(0).getTextContent()));
       
       NodeList nl = doc2.getDocumentElement().getElementsByTagName("property");
      
        for(int i=0;i<nl.getLength();i++){
        	if("connection.driver_class".equals(nl.item(i).getAttributes().getNamedItem("name").getNodeValue())) 
        	driverName = nl.item(i).getTextContent();
        	
        	if("connection.url".equals(nl.item(i).getAttributes().getNamedItem("name").getNodeValue())) 
        	url = nl.item(i).getTextContent();
        	
        	if("connection.username".equals(nl.item(i).getAttributes().getNamedItem("name").getNodeValue())) 
        	userName = nl.item(i).getTextContent();
        	
        	if("connection.password".equals(nl.item(i).getAttributes().getNamedItem("name").getNodeValue())) 
        	password = nl.item(i).getTextContent();
        
        }
       */
        Class.forName (driverName).newInstance ();
        conn = DriverManager.getConnection (url, userName, password);
        
        if(conn == null){
        	System.err.print("Some thing wrong with connecting with database!");
        }
        
        debug (0, "Database connection established");
        //CachedRowSet crs;
        
    }
    catch (Exception e)
    {
        System.err.println ("Cannot connect to database server:"+e);
    }
    
    return conn;
}

/**
 * This functions executes a query
 * @param qry
 * @return result
 * @throws SQLException
 */
public CachedRowSet executeQuery(String qry) throws SQLException{
	CachedRowSetImpl crs;
	Connection conn =null;
	try {
		
		conn = getConnection();
		Statement stmt = conn.createStatement(); 
		debug(0, qry);
        ResultSet rs =  stmt.executeQuery(qry);
        
         
        crs = new CachedRowSetImpl();
        crs.populate(rs); 
        rs.close(); 
        stmt.close();
        
         
	} catch (SQLException e) {
		debug(5,"Exception:"+qry);
		e.printStackTrace();
		throw e;
	}
	finally
    {
        if (conn != null)
        {
            try
            {
                conn.close ();
                conn =null;
               debug (0, "Database connection terminated");
            }
            catch (Exception e) {
            e.printStackTrace();	
            /* ignore close errors */ }
        }
    }
	
	return crs;
}

/**
 * This function is used to execute update query.
 * @param qry
 * @return
 * @throws SQLException
 */
public int executeUpdate(String qry) throws SQLException{
	Connection conn =null;
	int retval =0;
	try {
		
		conn = getConnection();
		Statement stmt = conn.createStatement(); 
        retval  = stmt.executeUpdate(qry);
	}catch(SQLException  e){
		debug(5,"Exception:"+qry);
		e.printStackTrace(); 
		throw e;
	}
	finally
    {
        if (conn != null)
        {
            try
            {
                conn.close ();
                //log ("Database connection terminated");
            }
            catch (Exception e) { /* ignore close errors */ }
        }
    }
	return retval;
}

/**
 * Executes prepared statements
 * @param qry
 * @param arPrepstmt
 * @return result
 * @throws SQLException
 */
public CachedRowSet executePreparedQuery(String qry,PrepstmtDTOArray arPrepstmt) throws SQLException{
	CachedRowSetImpl crs = null;
	Connection conn =null;
	try {
		
		conn = getConnection();
		PreparedStatement  stmt = conn.prepareStatement(qry); 
        Iterator itr = arPrepstmt.getArdto().iterator();
        int count = 1;
        while(itr.hasNext()){
        	PrepstmtDTO pd = (PrepstmtDTO)itr.next();
        	if(pd.getType() == PrepstmtDTO.DataType.DATE){
        		Date newDate = new Date( ( new SimpleDateFormat("DD/MM/yyyy")).parse(pd.getData()).getTime());
        		stmt.setDate(count,  newDate);
        	}else if(pd.getType() == PrepstmtDTO.DataType.DOUBLE){
        		String in = pd.getData();
        		if(in == null || "".equals(in))in = "0.0D";
        		stmt.setDouble(count, Double.parseDouble(in));    		
			}else if(pd.getType() == PrepstmtDTO.DataType.FLOAT){
				String in = pd.getData();
				if(in == null || "".equals(in))in = "0.0f";
				stmt.setFloat(count, Float.parseFloat(in));
			}else if(pd.getType() == PrepstmtDTO.DataType.INT){
				String in = pd.getData();
				if(in == null || "".equals(in))in = "0";
				stmt.setInt(count, Integer.parseInt(in));
			}else if(pd.getType() == PrepstmtDTO.DataType.STRING){
				stmt.setString(count, pd.getData());
			}
        	count ++;
        }
		debug(0, qry);
        ResultSet rs =  stmt.executeQuery();
        
         
        crs = new CachedRowSetImpl();
        crs.populate(rs); 
        rs.close(); 
        stmt.close();
        
         
	} catch (SQLException e) {
		debug(5,"Exception:"+qry);
		e.printStackTrace();
		throw e;
	} catch (ParseException e) {
		debug(5,"Exception:"+qry);
		e.printStackTrace();
	}
	finally
    {
        if (conn != null)
        {
            try
            {
                conn.close ();
                conn =null;
               debug (0, "Database connection terminated");
            }
            catch (Exception e) {
            e.printStackTrace();	
            /* ignore close errors */ }
        }
    }
	
	return crs;
}

/**
 * Executes prepared update statements.
 * @param qry
 * @param arPrepstmt
 * @return result
 * @throws SQLException
 */
public int executePreparedUpdate(String qry,PrepstmtDTOArray arPrepstmt) throws SQLException{
	Connection conn =null;
	int retval =0;
	try {
		
		conn = getConnection();
		PreparedStatement  stmt = conn.prepareStatement(qry); 
        Iterator itr = arPrepstmt.getArdto().iterator();
        int count = 1;
        while(itr.hasNext()){
        	PrepstmtDTO pd = (PrepstmtDTO)itr.next();
        	if(pd.getType() == PrepstmtDTO.DataType.DATE){
        		Date newDate = new Date( ( new SimpleDateFormat("DD/MM/yyyy")).parse(pd.getData()).getTime());
        		stmt.setDate(count,  newDate);
        	}else if(pd.getType() == PrepstmtDTO.DataType.DOUBLE){
        		String in = pd.getData();
        		if(in == null || "".equals(in))in = "0.0D";
        		stmt.setDouble(count, Double.parseDouble(in));    		
			}else if(pd.getType() == PrepstmtDTO.DataType.FLOAT){
				String in = pd.getData();
				if(in == null || "".equals(in))in = "0.0f";
				stmt.setFloat(count, Float.parseFloat(in));
			}else if(pd.getType() == PrepstmtDTO.DataType.INT){
				String in = pd.getData();
				if(in == null || "".equals(in))in = "0";
				stmt.setInt(count, Integer.parseInt(in));
			}else if(pd.getType() == PrepstmtDTO.DataType.STRING){
				stmt.setString(count, pd.getData());
			}
        	count ++;
        }
         
		retval  = stmt.executeUpdate();
	}catch(SQLException  e){
		debug(5,"Exception:"+qry);
		e.printStackTrace(); 
		throw e;
	} catch (ParseException e) {
		debug(5,"Exception:"+qry);
		e.printStackTrace();
	}
	finally
    {
        if (conn != null)
        {
            try
            {
                conn.close ();
                //log ("Database connection terminated");
            }
            catch (Exception e) { /* ignore close errors */ }
        }
    }
	return retval;
}

	/*
	 * testing 
	 */
/*
	public static void main(String[] args) {
		try{
		CachedRowSet crs = new DBConnector().executeQuery("select empid,empname from ams_request");
		while(crs.next()){
	        System.out.print("Name:"+crs.getString("empid"));
	        System.out.print(",Breanch:"+crs.getString("empname"));	     
	        }
		crs.close();
		}catch(SQLException e){
			e.printStackTrace();
		}
	} */

}
