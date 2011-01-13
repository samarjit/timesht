package com.ycs.fe.dao;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.rowset.CachedRowSet;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.util.ValueStack;
import com.ycs.fe.dto.PrepstmtDTO.DataType;
import com.ycs.fe.dto.PrepstmtDTOArray;

public class FETranslatorDAO {
	private Logger logger = Logger.getLogger(this.getClass());
	/**
	 * Executes select box query and puts in valueStack 
	 * 
	 * CREATE TABLE query_table(QUERYID varchar2(20), QUERY varchar2(500), active varchar2(1));
	 * 
	 * @param sqlquery or queryid value which will be used for lookup
	 * @param stackid contains the value stack key against which value is going to be stored
	 * @param type can be SQL for normal sql or QUERYID for referenced queries from QUERY_TABLE 
	 */
	public void executequery(String sqlquery, String stackid, String type) {
		ValueStack stack = ActionContext.getContext().getValueStack();
		DBConnector dbconn = new DBConnector();
		if( "SQL".equals(type)){
			//proceed with normal sqlquery without modification
		}else if("QUERYID".equals(type)){
			//proceed with SQL QUERY instead of sqlquery, sqlquery gets new value
			String SQL = "select QUERY from QUERY_TABLE where QUERYID=? and ACTIVE='Y'" ;
			CachedRowSet crs = null;
			try {
				PrepstmtDTOArray arprep = new PrepstmtDTOArray();
				arprep.add(DataType.STRING, sqlquery);
				crs = dbconn.executePreparedQuery(SQL,arprep );
				String query = null;
				while (crs.next()) {
					query = crs.getString("QUERY");
				}
				logger.debug("Query found ="+query);
				if(query == null || query.length() ==0)throw new Exception("QueryID #"+sqlquery+"# not found");
				
				sqlquery = query;
			} catch (Exception e) {
				logger.debug("DAO Exception QUERY retreive failure by QUERYID("+sqlquery+"):",e);
			} finally {
				if (crs != null) {
					try {
						crs.close();
					} catch (SQLException e1) {
						logger.debug("DAO Exception closing connection",e1);
					}
					crs = null;
				}
			}	
		}
		if(sqlquery!= null && sqlquery.trim().length() >0 ){
	    	Map<String, Object> values = new HashMap<String, Object>();
	    	Map<String, Object> context = new HashMap<String, Object>();
			
			CachedRowSet crs = null;
			try {
				//case i insensitive m multiline s doall  
				if(sqlquery.matches("(?ims:select).*(?ims:from).*")){
					logger.debug("valid query processing...");
				}else{
					logger.debug("invalid query skipping...");
					return;
				}
					
				crs = dbconn.executeQuery(sqlquery);
				ResultSetMetaData md = crs.getMetaData();
				int colcount = md.getColumnCount();
				if(colcount == 2){
					while (crs.next()) {
						values.put(crs.getString(1),crs.getString(2));
					}
					 
				}else{
					while (crs.next()) {
						values.put("value",crs.getString(1));
					}
				}
			} catch (Exception e) {
				logger.debug("DAO Exception:"+e);
			} finally {
				if (crs != null) {
					try {
						crs.close();
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
					crs = null;
				}
			}
			context.put(stackid, values);
	    	stack.push(context);	
	    }
	    
	}

}
