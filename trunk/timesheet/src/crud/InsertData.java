package crud;



import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.sql.rowset.CachedRowSet;

import org.json.JSONException;

import util.Utility;
import dao.CrudDAO;

public class InsertData {
	public void debug(int priority, String s){
		if(priority >0)
		System.out.println("InsertData:"+s);
	}
	
	/**
	 * This function performs the delete operation 
	 * @param screenName
	 * @param insertClause
	 * @param autogenId
	 * @return result of Insert operation
	 */
	
	public String doInsert(String screenName, String insertClause, String autogenId){
		CrudDAO cd = new CrudDAO();
		HashMap metadata = null;
		String storeflag = null;
		String scrName=screenName;
		List <String> lstPanelName = cd.findPanelByScrname(scrName);
		//HashMap<String, String> hmWhere = Utility.extractWhereClause( insertClause/*String whereStringOfPanel, String panelName */);
		String html = ""; //outer
		String htmlTemp = "";
		CachedRowSet crs = null;
		int insertResult = 0;
		debug(0, "lstPanelName:"+lstPanelName);
		Iterator itrPanel = lstPanelName.iterator();
		
		while (itrPanel.hasNext())
		{ 
			String panelName = (String) itrPanel.next();
			
			
			try {
				storeflag = cd.getStoreFlag(screenName, panelName);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if( storeflag.indexOf('W') > -1){
				debug(0, "******** calling creteInsertQuery panel name#"+panelName + "insertClause" + insertClause);
			    //if you allocate the HashMap inside createRetrieveQuery1 then it returns null by the time it comes here
				metadata = new HashMap();
				//column metadata should get populated here
				String sg = createInsertQuery(metadata, scrName, panelName,insertClause );
				if(sg != null && !("".equals(sg))){
					try {
	
						String query = sg.replaceAll("AUTOGEN_SEQUENCE_ID", autogenId);
						debug(1,"inserted successfully:"+query);
						insertResult  = cd.executeInsertQuery(query);
						//debug(1,"inserted successfully:"+query);
					} catch (Exception e) {
						debug(5,"Failed in insert"+e);
						//e.printStackTrace();
						insertResult  = -1;
					}
					debug(0, "Insert query:" + sg);
					if(insertResult <0 ){
						html+= ","+panelName;
					}
				}
				debug(0, "Insert query:" + sg);
				if(insertResult <0 ){
					html+= ","+panelName;
				}
			}						
		}
		if(html.length() > 0){
			html = html.substring(1); 
			html = "Insert failed in "+html;
		}
		return html;
	}
	
	/**
	 * This function creates a insert query that performs the insert operation 
	 * @param metadata
	 * @param scrname
	 * @param panelName
	 * @param insertClause
	 * @return Insert Query
	 */
	
	public String createInsertQuery(HashMap metadata,String scrname,String panelName, String insertClause) {
		
		System.out.println("*****************panelName*************** "+panelName);
		String insertQry = "";
		
		CrudDAO cd = new CrudDAO();
	 
		HashMap insertMap = null;
		try {
			HashMap<String, HashMap<String,String>> keyvvaltemp = Utility.extractKeyValPair(insertClause);
			if(keyvvaltemp.containsKey(panelName))
				insertMap = keyvvaltemp.get(panelName);
			else
				return "";	
		} catch (JSONException e) {
			debug(5,"Failed to extract insertClause");
			e.printStackTrace();
		}
		
		HashMap qryPart1 = cd.createInsertQueryPart1(metadata,scrname,panelName,insertMap );
		String tableName =  cd.findTableByPanels(scrname,panelName);
		String splWhereClause = cd.findSplWhereClsOfPanels(scrname,panelName);
		  		
		debug(0,"table name:"+tableName);
		
		if(tableName!= null && tableName.length() >0 && qryPart1 !=null && qryPart1.size() > 0  && qryPart1.get("valuestr") != null){
			insertQry ="INSERT INTO "+tableName+"("+qryPart1.get("dbcolstr")+") VALUES ("+qryPart1.get("valuestr")+")"; 
		}else {
			debug(0, "Incomplete query was:"+"INSERT INTO "+tableName+"("+qryPart1.get("dbcolstr")+") VALUES ("+qryPart1.get("valuestr")+")");
		}
		System.out.println("*****************insert query*************** "+insertQry);
		return insertQry;
	}
	
	/**
	 * This function gets a new APPID from database
	 * @return new ID
	 */
	
	public String getNewAppId() {
		CrudDAO cd = new CrudDAO();
		return cd.getNewAppId() ;
	
	}
	
}
