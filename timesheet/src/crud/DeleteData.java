package crud;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.sql.rowset.CachedRowSet;

import org.json.JSONException;

import util.Utility;
import dao.CrudDAO;

public class DeleteData {
	private void debug(int priority, String s){
		if(priority >-1)
		System.out.println(s);
	}
	/**
	 * This function performs the delete operation 
	 * @param screenName
	 * @param whereClause
	 * @throws JSONException 
	 * @return result of the delete operation
	 */
	
	public String doDelete(String screenName, String whereClause) throws JSONException{
		CrudDAO cd = new CrudDAO();
		HashMap metadata = null;
		String sg = null;
		String scrName=screenName;
		List <String> lstPanelName = cd.findPanelByScrname(scrName);
		HashMap<String, String> hmWhere = Utility.extractWhereClause(whereClause);
		String html = ""; //outer
		String htmlTemp = "";
		int queryResult = 0 ;
		CachedRowSet crs = null;
		debug(0, "lstPanelName:"+lstPanelName);
		Iterator itrPanel = lstPanelName.iterator();
		if(itrPanel.hasNext())
		{ 
			String panelName = (String) itrPanel.next();
			debug(0, "******** calling creteDeleteQuery panel name#"+panelName+ " hmWhere:"+hmWhere);
		    //if you allocate the HashMap inside createRetrieveQuery1 then it returns null by the time it comes here
			metadata = new HashMap();
			//column metadata should get populated here
			 sg = createDeleteQuery(metadata, scrName,	panelName, hmWhere);
			 debug(1, "Delete query:" + sg);
				if(sg != null && !("".equals(sg))){
					try {
						queryResult = cd.executeInsertQuery(sg);
					} catch (Exception e) {
						//debug(5,"Failed in update");
						e.printStackTrace();
						queryResult = -1;
					}
					 
				}		
				
			}
			
			html = String.valueOf(queryResult);
			return html;
	}

	/**
	 * This function creates a delete query that performs the delete operation 
	 * @param metadata
	 * @param scrname
	 * @param panelName
	 * @param hmWherePanel
	 * @return delete query
	 */
	
	public String createDeleteQuery(HashMap metadata,String scrname,String panelName, HashMap<String,String> hmWherePanel) {
		String delQuery = "";
		String joiner = " WHERE ";
		
		CrudDAO cd = new CrudDAO();
		
		//String qryPart1 = cd.createRetrieveQueryPart1(metadata,scrname,panelName);
		String tableName =  cd.findTableByPanels(scrname,panelName);
		String splWhereClause = cd.findSplWhereClsOfPanels(scrname,panelName);
		  
		
		if(splWhereClause != null && ! "".equals(splWhereClause) ){
			splWhereClause += joiner + splWhereClause+" ";
			joiner =" AND ";
		}else{
			splWhereClause =" ";
		}
		
		debug(0,"joiner:"+joiner+";scrname:"+scrname+";panelName:"+panelName+";hmWherePanel:"+hmWherePanel);
		//process where clause
		String strWhereQuery  = cd.createWhereClause(joiner,scrname,panelName,hmWherePanel,true);
		debug(0,"strWhereQuery="+strWhereQuery+";table name:"+tableName);
		
		if(tableName!= null && tableName.length() >0 && strWhereQuery!=null && strWhereQuery.length()>0)
		{
				delQuery ="DELETE  " + " FROM "+tableName+splWhereClause+strWhereQuery; 
		}
		else {
			debug(0, "Incomplete query was:"+"DELETE " + " FROM "+tableName+splWhereClause+strWhereQuery);
		}
		return delQuery;
	}
	/*
	public static void main(String[] args) throws JSONException {
		CrudDAO cd = new CrudDAO();
		String whereClause="{\"json\":[{\"key\":\"username\",\"value\":\"samarjit3\"},{\"key\":\"assethost\",\"value\":\"234\"},{\"key\":\"assetid\",\"value\":\"1\"}]}";
		System.out.println("whereClause:"+whereClause);
		HashMap hmWhere = Utility.extractWhereClause(whereClause);
		System.out.println("hmWhere:"+hmWhere);
		String strWhereQuery  = cd.createWhereClause(" WHERE ","frmAllocation","panelFields",hmWhere,true);
		System.out.println("strWhereQuery="+strWhereQuery+";table name:");
	} */
}
