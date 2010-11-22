package dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.sql.rowset.CachedRowSet;

import org.json.JSONException;

import pojo.ListAttribute;



import dbconn.DBConnector;
import dto.PrepstmtDTO;
import dto.PrepstmtDTOArray;

/**
 * This class generates queries for CRUD operations.
 *
 */
public class CrudDAO {
	private void debug(int priority,String s){
		if(priority > 1)
		System.out.println("CrudDAO:"+s);
	}
	
	/**
	 * This function creates special whereclause for each panel
	 * @param screenName
	 * @param panelName
	 * @return String of whereclause
	 */
	
	
	public String findSplWhereClsOfPanels(String screenName,String panelName){
 		String SQL = "SELECT splwhereclause   FROM  screen_panel where scr_name='"+screenName+"' and panel_name='"+panelName+"' ";
		DBConnector db = new DBConnector();
		String tableNames = "";
		CachedRowSet crs = null;
		try {
			crs = db.executeQuery(SQL);
			while(crs.next()){
				tableNames = crs.getString("splwhereclause");
			}
			crs.close();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(crs != null){
				try {
					crs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
			}
		}
		return 	tableNames;
	}
	
	/**
	 * This function retrieves panels by screen name
	 * @param screenName
	 * @return String array of panels
	 */
	
	
	public List<String> findPanelByScrname(String screenName){
		String SQL = 
			"SELECT panel_name   FROM  screen_panel where scr_name='"+screenName+"' order by SORTORDER ASC";
		DBConnector db = new DBConnector();
		List<String> panelNames = new ArrayList<String>();
		CachedRowSet crs = null;
		try {
			crs = db.executeQuery(SQL);
			while(crs.next()){
				panelNames.add( crs.getString("panel_name"));
			}
			crs.close();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(crs != null){
				try {
					crs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
			}
		}
		return 	panelNames;
	}
	
	
	/**
	 * This function retrieves table by panels
	 * @param screenName
	 * @param panelName
	 * @return tables associated with the panel
	 */
	
	public String findTableByPanels(String screenName,String panelName){
		String tableNames = "";
		String SQL = 
			"SELECT table_name   FROM  screen_panel where scr_name='"+screenName+"' and panel_name='"+panelName+"' ";
		DBConnector db = new DBConnector();
		//log("findTableName:"+SQL);
		CachedRowSet crs = null;
		try {
			crs = db.executeQuery(SQL);
			while(crs.next()){
				tableNames = crs.getString("table_name");
			}
			crs.close();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(crs != null){
				try {
					crs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
			}
		}
		return 	tableNames;
	}
	
	/**
	 * Creates "DBCOL alias fname," - part of the query
	 * @param metadata [output] gets filled with details of a column
	 * @param scrname  [input]
	 * @param panelName [input]
	 * @return a part of the retrieve query
	 */
	public String createRetrieveQueryPart1(HashMap metadata,String scrname,String panelName) {
		DBConnector db = new DBConnector();
		String searchQuery = "";
		String colquery ="";
		//metadata = new HashMap();
		CachedRowSet crs = null;
		
		String SQL2 = 
			"select lblname,fname,idname,dbcol,datatype,classname,prkey,strquery from panel_fields where  scr_name='"+scrname+"' and panel_name='"+panelName+"'";
			debug(1,"createRetrieveQueryPart1:"+SQL2); 
		 try {
				 crs = db.executeQuery(SQL2);
			
				
				while(crs.next()){ 
					ListAttribute ls = new ListAttribute();
					ls.setClassname(crs.getString("classname"));
					ls.setDatatype(crs.getString("datatype"));
					ls.setDbcol(crs.getString("dbcol"));
					ls.setFname(crs.getString("fname"));
					ls.setIdname(crs.getString("idname"));
					ls.setLblname(crs.getString("lblname"));
					ls.setPrkey(crs.getString("prkey"));
					colquery = crs.getString("strquery");
					metadata.put(crs.getString("fname"), ls) ;
					if(false && colquery !=null && colquery.length() > 1){
						searchQuery +="("+colquery+") "+crs.getString("fname")+",";
					}else{
						String datatype = crs.getString("datatype");
						if("DATE".equals(datatype)){
							searchQuery += "TO_CHAR("+crs.getString("dbcol")+",'dd/mm/yyyy') "+crs.getString("fname")+",";
						}else{
							searchQuery +=crs.getString("dbcol")+" "+crs.getString("fname")+",";
						}
					}
				}
				if(searchQuery.length() > 0){
					searchQuery = searchQuery.substring(0, searchQuery.length()-1);
				}
				crs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}finally{
				if(crs != null){
					try {
						crs.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
					
				}
			}
			debug(0,metadata.toString());
			return searchQuery;
	}

	/**
	 * Creates joiner + DBCOL like '%value%' + joiner ... in loop
	 * @param joiner [input/output modified] 
	 * @param scrname [input]
	 * @param panelName [input]
	 * @param hmWherePanel [input] whereclause of a planel key/value pair
	 * @return whereclause
	 */
	public String createWhereClause(String joiner, String scrname,String panelName,HashMap<String, String> hmWherePanel,boolean exactMatch) {
		String strWhereQuery = "";
		DBConnector db = new DBConnector(); 
		Iterator itr = hmWherePanel.keySet().iterator();
        
       
        while(itr.hasNext()){
        	String fname = (String) itr.next();
        	String val =  hmWherePanel.get(fname);
        	String SQL = 
            	"select dbcol,datatype from panel_fields where  scr_name='"+scrname+"' and " +
            			"panel_name='"+panelName+"' " +
            			"and UPPER(fname)=UPPER('"+fname+"')";
        	debug(1,"createWhereClause:"+SQL);
        	CachedRowSet crs = null;
			try {
				crs = db.executeQuery(SQL);
			
	        	if(crs.next())	        	{
	        		String dbcol = crs.getString("dbcol");
	        		String datatype = crs.getString("datatype");
	        		
	        		if(null != datatype && !"".equalsIgnoreCase(datatype)){
	        			if("DATE".equalsIgnoreCase(datatype)){
	        				//MySQL????
	        				//Oracle 
	        				
	        			}
	        		}
	        		if(!"".equals(strWhereQuery)){
	        			joiner = " AND ";
	        		}
	        		
	        		if(exactMatch) {
	        			if(!val.equalsIgnoreCase(""))
	        			strWhereQuery +=joiner+dbcol+" like '"+val+"'";
	        		}
	        		else{
	        			if(!val.equalsIgnoreCase(""))
	        			strWhereQuery +=joiner+dbcol+" like '%"+val+"%'";
	        		}//if crs.next()
	        	crs.close();
			}} catch (SQLException e) {
				e.printStackTrace();
			}
			 catch (NullPointerException n) {
				 continue;
			}
			 catch (Exception e) {
				 debug(5,SQL);
					e.printStackTrace();
			}
			 
			 finally{
					if(crs != null){
						try {
							crs.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
						
					}
				}
        }
		
		return strWhereQuery;
		 
		
	}

	/**
	 * This function executes retrieve query
	 * @param query
	 * @return CachedRowSet result
	 */
	
	public CachedRowSet executeRetrieveQuery(String sg) throws Exception {
		DBConnector db = new DBConnector();
		CachedRowSet crs = null;
		try {
			crs = db.executeQuery(sg);
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}
		return crs;
	}
	
	 /** 
	 * This function executes insert query
	 * @param query
	 * @return CachedRowSet result
	 */
	public int  executeInsertQuery(String sg) throws Exception {
		DBConnector db = new DBConnector();
		int result = 0;
		try {
			result  = db.executeUpdate(sg);
			
		} catch (SQLException e) {
			//e.printStackTrace();
			throw e;
		}		
		return result  ;
	}
	
	 /** 
	 * This function finds a pre defined query
	 * @param screenName
	 * @param panelName
	 * @return query
	 */
	public String findPreDefQuery(String screenName, String panelName) {
		String preDefQuery = "";
		String SQL = "select SELQUERY from screen_panel  where scr_name='"+screenName+"' and panel_name='"+panelName+"' ";
		DBConnector db = new DBConnector();
		CachedRowSet crs = null;
		try{
			crs = db.executeQuery(SQL);
			while(crs.next()){
				preDefQuery = crs.getString("SELQUERY");
			}
		}catch(SQLException e){
			debug(5,e.getMessage());
		}
		return preDefQuery;
	}

	 /** 
	 * This function creates update query
	 * @param metadata
	 * @param updateClause
	 * @param screenName
	 * @param panelName
	 * @return query
	 */
	public String createUpdateQueryPart1(HashMap metadata, String scrname, String panelName,HashMap updateClause) {
		DBConnector db = new DBConnector();
		String strQuery="";
		String updateQuery = "";
		String colquery ="";
		//metadata = new HashMap();
		CachedRowSet crs = null;
		debug(0,updateClause.toString());
		String SQL2 = 
			"select lblname,fname,idname,dbcol,datatype,classname,prkey,strquery from panel_fields where  scr_name='"+scrname+"' and panel_name='"+panelName+"'";
	debug(1,"createUpdateQueryPart1:"+SQL2); 
		 try {
				 crs = db.executeQuery(SQL2);
			
				
				while(crs.next()){ 
					ListAttribute ls = new ListAttribute();
					ls.setClassname(crs.getString("classname"));
					ls.setDatatype(crs.getString("datatype"));
					ls.setDbcol(crs.getString("dbcol"));
					ls.setFname(crs.getString("fname"));
					ls.setIdname(crs.getString("idname"));
					ls.setLblname(crs.getString("lblname"));
					ls.setPrkey(crs.getString("prkey"));
					colquery = crs.getString("strquery");
					
					String fname= crs.getString("fname");
					String datatype = crs.getString("datatype");
					if(updateClause.get(fname.toLowerCase())!= null)
						{
						
						if("DATE".equals(datatype)){
							updateQuery +=crs.getString("dbcol")+"= TO_DATE('"+updateClause.get(fname.toLowerCase())+"','dd/mm/yyyy') ,";	
						}else{
							updateQuery +=crs.getString("dbcol")+"= '"+updateClause.get(fname.toLowerCase())+"' ,";
						}
						
						 
						metadata.put(crs.getString("fname"), ls) ;
						}
					debug(0,"list attribuite********* " +ls+"  #fname="+fname.toLowerCase()+" #"+updateClause.get(fname.toLowerCase()));
					 
				}
				if(updateQuery.length() > 0){
					updateQuery = updateQuery.substring(0, updateQuery.length()-1);
				}
				
				crs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}finally{
				if(crs != null){
					try {
						crs.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
					
				}
			}
			debug(0,metadata.toString());
			debug(1,"Update query: "+updateQuery);
			return updateQuery;
		
	}
	
	 /** 
	 * This function creates insert query
	 * @param metadata
	 * @param updateClause
	 * @param screenName
	 * @param panelName
	 * @return query
	 */

	public HashMap createInsertQueryPart1(HashMap metadata, String scrname, String panelName,HashMap insertClause) {
			DBConnector db = new DBConnector();
			String strQuery="";
			HashMap inesrtqryPart = new HashMap();
			String dbcolStr = "";
			String valueStr = "";
			String colquery ="";
			//metadata = new HashMap();
			CachedRowSet crs = null;
			debug(0,insertClause.toString());
			String SQL2 = 
				"select lblname,fname,idname,dbcol,datatype,classname,prkey,strquery from panel_fields where  scr_name='"+scrname+"' and panel_name='"+panelName+"'";
			debug(1,"createInsertQueryPart1:"+SQL2); 
			 try {
					 crs = db.executeQuery(SQL2);
				
					
					while(crs.next()){ 
						ListAttribute ls = new ListAttribute();
						ls.setClassname(crs.getString("classname"));
						ls.setDatatype(crs.getString("datatype"));
						ls.setDbcol(crs.getString("dbcol"));
						ls.setFname(crs.getString("fname"));
						ls.setIdname(crs.getString("idname"));
						ls.setLblname(crs.getString("lblname"));
						ls.setPrkey(crs.getString("prkey"));
						colquery = crs.getString("strquery");
						
						String fname= crs.getString("fname");
						String datatype = crs.getString("datatype");
						if(insertClause.get(fname.toLowerCase())!= null)
							{
							dbcolStr +=crs.getString("dbcol")+",";
							if("DATE".equals(datatype)){
								valueStr += "TO_DATE('"+insertClause.get(fname.toLowerCase())+"','dd/mm/yyyy') ,";
							}else{
								valueStr += "'"+insertClause.get(fname.toLowerCase())+"' ,";
							}
							
							metadata.put(crs.getString("fname"), ls) ;
							}
						 
					}
					if(dbcolStr.length() > 1){
						dbcolStr = dbcolStr.replaceAll(",$","");
						valueStr = valueStr.replaceAll(",$", "");
						inesrtqryPart.put("dbcolstr", dbcolStr);
						inesrtqryPart.put("valuestr", valueStr);
					}
					crs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}finally{
					if(crs != null){
						try {
							crs.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
						
					}
				}
				
				return inesrtqryPart;
	}
	 /** 
	 * This function get business logic name based on screen name
	 * @param screenName
	 * @return businesslogic Name
	 */
	public String getBusinessLogicName(String screenName) {
		DBConnector db = new DBConnector();
		CachedRowSet crs = null;
		String businesslogicname = null;
		String SQL = "select BUSINESSLOGIC from screen where scr_name = '"+screenName+"' ";
		try {
			crs = db.executeQuery(SQL);
			while(crs.next()){
				businesslogicname = crs.getString("BUSINESSLOGIC");	
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
			
		return businesslogicname;
	}
	
	 /** 
	 * This function returns new App ID
	 * @return App ID
	 */
	public String getNewAppId() {
		DBConnector db = new DBConnector();
		CachedRowSet crs = null;
		String SQL = "select seqappid.nextval appid from dual ";
		String appid=""; 
		try {
				crs = db.executeQuery(SQL);
				while(crs.next()){
					appid=  crs.getString("appid");
				}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try{
				if(crs!=null)crs.close();
			}catch(SQLException e){
				debug(5, e.getMessage());
			}
		}
		return appid;
	}

	 /** 
	 * This finds a storeFlag for a field
     * @param screenName
	 * @param panelName
	 * @return storeflag status
	 */
	public String getStoreFlag(String screenName, String panelName) throws SQLException {
		DBConnector db = new DBConnector();
		CachedRowSet crs = null;
		String SQL = "select rw_flg from  screen_panel where scr_name =? and panel_name = ?";
		String storeflag=""; 
		try {
			PrepstmtDTOArray  arPrepStmt = new PrepstmtDTOArray();
			arPrepStmt.add(PrepstmtDTO.DataType.STRING, screenName);
			arPrepStmt.add(PrepstmtDTO.DataType.STRING, panelName);
				crs = db.executePreparedQuery(SQL,arPrepStmt);
				while(crs.next()){
					storeflag=  crs.getString("rw_flg");
				}
				if(storeflag == null)storeflag="";
		} catch (SQLException e) {
			throw e;
		}finally{
			try{
				if(crs!=null)crs.close();
			}catch(SQLException e){
				debug(5, e.getMessage());
			}
		}
		return storeflag;
	}

}
