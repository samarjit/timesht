package dao;

import java.sql.SQLException;

import javax.sql.rowset.CachedRowSet;

import dbconn.DBConnector;

public class SearchListDAO {
	
	private DBConnector db = new DBConnector();
	
	 
	private void debug(int priority, String s){
		if(priority > 1){
			System.out.println("SearchListDAO:"+s);
		}
	} 
	public CachedRowSet executeSearchQuery(String query) throws SQLException  {
		
		CachedRowSet crs;
		try {
			crs = db.executeQuery(query);
		} catch (SQLException e) {
			debug(5,"Search List DAO failed:"+query);
			throw new SQLException("Search List DAO failed");
		}
		
		
		return crs;
	}
	public CachedRowSet getDBColDatatype(String scrname, String panelName, String fname) throws SQLException {
		
		CachedRowSet crs;
		String SQL = 
        	"select dbcol,datatype from panel_fields where  scr_name='"+scrname+"' and " +
        			"panel_name='"+panelName+"' " +
        			"and fname='"+fname+"'";
    	debug(1,SQL);
    	try {
			crs = db.executeQuery(SQL);
		} catch (SQLException e) {
			throw e;
		}
		
		return crs;
	}
	public String getRelatedPanelFrmScrPanel(String scrname, String panelName) {
		 
		 String SQL = 
	        	"select relatedpanel from screen_panel  where   scr_name='"+scrname+"' and panel_name='"+panelName+"' ";
	        debug(1,SQL);
	        String relatedPanel = "";
	        try {
				CachedRowSet crs = db.executeQuery(SQL);
				if(crs.next()){
					relatedPanel = crs.getString("relatedpanel");
					
				}
				crs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		return relatedPanel;
	}
	public CachedRowSet getTableSplwhereclauseFrmRelatedPanel(String scrname,String relatedPanel) throws SQLException {
		
		CachedRowSet crs;
		String SQL1 = 
			"select TABLE_NAME,splwhereclause from screen_panel where   scr_name='"+scrname+"' and panel_name='"+relatedPanel+"'";
		debug(1,SQL1);
    	try {
			crs = db.executeQuery(SQL1);
		} catch (SQLException e) {
			throw e;
		}
		
		return crs;
	}
	public CachedRowSet getDBmetadataFromRelatedPanel(String scrname, String relatedPanel) throws SQLException {
		
		CachedRowSet crs;
		String SQL2 = 
			"select lblname,fname,idname,dbcol,datatype,classname,prkey,strquery from panel_fields where  scr_name='"+scrname+"' and panel_name='"+relatedPanel+"' order by ORDERNO";
		 debug(1,SQL2); 
    	try {
			crs = db.executeQuery(SQL2);
		} catch (SQLException e) {
			throw e;
		}
		
		return crs;
	}

}
