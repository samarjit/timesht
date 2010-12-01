package actionclass;

import java.io.InputStream;
import java.io.StringBufferInputStream;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.sql.rowset.CachedRowSet;

import com.opensymphony.xwork2.ActionSupport;

import dbconn.DBConnector;

public class TimesheetAC extends ActionSupport {

 
	private static final long serialVersionUID = 1L;
	private String command;
	private String data;
	private InputStream inputStream;
	private String sqlstring;
    
	
	public InputStream getInputStream() {
        return inputStream;
    }
	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}
	public String getSqlstring() {
		return sqlstring;
	}
	public void setSqlstring(String sqlstring) {
		this.sqlstring = sqlstring;
	}
	
	private static int getRowCount(ResultSet rs) throws SQLException {
		   int current = rs.getRow();
		   rs.last();
		   int count = rs.getRow();
		   if(count == -1)
		       count = 0;
		   if(current == 0)
		       rs.beforeFirst();
		   else
		       rs.absolute(current);
		   return count;
		}
	
	public String execute(){
		String resXML  = null;
	    String temp = null;
	      String colname;
	      String coltype;
	      StringBuffer htmlString = new StringBuffer(100);
		if("query".equals(command)){
			DBConnector dbconn = new DBConnector();
			
			
			 
			
			CachedRowSet crs = null;
			try {
				System.out.println("Executing SQL:"+command+":"+sqlstring);
				if(sqlstring!= null && sqlstring.length() > 0)
				crs = dbconn.executeQuery(sqlstring);
				 htmlString.append("[");
				while(crs!=null && crs.next()){
					ResultSetMetaData rsm = crs.getMetaData();
				     int columnCount = rsm.getColumnCount();
				     htmlString.append("{"); 
				     for (int j=0;j< columnCount;j++){
			             Object obj = crs.getObject(j+1);
			             
			             if(obj != null)
			             temp = obj.toString();
			              
			             colname = rsm.getColumnName(j+1);
			             coltype = rsm.getColumnTypeName(j+1);
			             
						htmlString.append("\"").append(colname).append("\":\"").append(temp).append("\",");
			             
			         }
				 	 htmlString.setCharAt(htmlString.length() -1, ' ');
				     htmlString.append("},");
				     
				}
				if(htmlString.length() > 2) 
				htmlString.setCharAt(htmlString.length() -1, ' ');
				 htmlString.append("]");
				 
				 resXML = htmlString.toString();
				 
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				if(crs !=null){
					try {
						crs.close();
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
					crs = null;
				}
			}
			
			 
			System.out.println(data);
		}
		
		if("update".equals(command)){
			DBConnector dbconn = new DBConnector();
			
			System.out.println("Executing SQL:"+command+":"+sqlstring);
			 
			
			int res =0;
			try {
				res = dbconn.executeUpdate(sqlstring);
				resXML = "{\"result\":\""+res+"\" }"; 
			} catch (Exception e) {
				resXML = "{\"result\":\"ERROR "+e.getMessage()+"\" }"; 
				e.printStackTrace();
			}finally{
				 
			}
			
			 
			System.out.println(data);
		}
		
			System.out.println("Returned XML:"+command+":"+resXML);
		 
	        inputStream = new StringBufferInputStream(resXML);
	        
		return SUCCESS;
	}
	
 
}
