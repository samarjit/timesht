package crud;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sql.rowset.CachedRowSet;

import org.json.JSONException;

import pojo.ListAttribute;

import businesslogic.BaseBL;

import util.Utility;

import dao.CrudDAO;
import dbconn.DBConnector;




public class RetreiveData  {
	
	private void debug(int priotiry, String s){
		if(priotiry > 0)
			System.out.println("RetreiveData:"+s);
	}
	
	
	/**
	 * This function performs the retrieve details operation 
	 * @param screenName
	 * @param whereClause
	 * @throws JSONException 
	 * @return result of the retrieve operation
	 */
	public String doRetrieveData(String scrName, String whereClause) throws Exception{
		CrudDAO cd = new CrudDAO();
		HashMap metadata = null;
		List <String> lstPanelName = cd.findPanelByScrname(scrName);
		HashMap<String, String> hmWhere = Utility.extractWhereClause( whereClause/*String whereStringOfPanel, String panelName */);
		HashMap<String,HashMap<String,String>> tempData = new HashMap<String, HashMap<String,String>>();
		HashMap<String,String> tempPanelData = null;
		String html = ""; //outer
		String htmlTemp = "";
		CachedRowSet crs = null;
		debug(0,"lstPanelName:"+lstPanelName);
		Iterator itrPanel = lstPanelName.iterator();
		
		//For every panel in the screen create an XML table 
		while (itrPanel.hasNext())
		{ 
			String panelName = (String) itrPanel.next();
			String storeflag = cd.getStoreFlag(scrName, panelName);
			if( storeflag.indexOf('R') > -1){
				debug(0,"******** calling creteRetreiveQuery panel name#"+panelName+ " hmWhere:"+hmWhere);
			    //if you allocate the HashMap inside createRetrieveQuery1 then it returns null by the time it comes here
				metadata = new HashMap();
				
				//Fill this for every Panel
				tempPanelData = new HashMap();
				
				//column metadata should get populated here
				String sg = createRetrieveQuery(metadata, scrName,	panelName, hmWhere);
				debug(1,"Retrieve query:" + sg);
				
				String tableHeader = "No data found";
				
				if (sg != null && sg.length() > 0) 	{
					try {
						//replace referenced whereClause
						sg  = replaceRefWhereClause(sg,tempData);
						debug(1,"Retrieve query ("+panelName+"):" + sg);
						crs = cd.executeRetrieveQuery(sg);
						htmlTemp = "";
						boolean firstItr = true;
						String data = "";
						String fname="";
						//Ideally this loop should run once in case of detail-data retrieval
						while (crs.next()) {
							htmlTemp += "\n<tr >";
							if (firstItr) {
								tableHeader = "\n<tr >";
							}
	
							if (metadata == null)
								throw new Exception(
										" Retreive Data : metadata null");
							Iterator itrmetadata = metadata.keySet().iterator();
							while (itrmetadata.hasNext()) {
							    fname = (String) itrmetadata.next();
								ListAttribute ls = (ListAttribute) metadata
										.get(fname);
								debug(0,"Fname=" + fname);
								data = crs.getString(fname);
								if(data == null)data="";
								if (firstItr) {
									tableHeader += "<th><div id=" + fname
											+ " style='display:none'>" + ls
											+ "</div>" + ls.getLblname() + "</th>";
								}
								htmlTemp += "<td id=" + fname + "> " + data + "</td>";
								tempPanelData.put(fname, data);
							}
							if (firstItr) {
								tableHeader += "</tr>";
								firstItr = false;
							}
							htmlTemp += "</tr>";
							
							
							
							debug(0,htmlTemp);
						} //while crs.next()
						tempData.put(panelName, tempPanelData);
					} catch (Exception e) {
						 e.printStackTrace();
						htmlTemp = "";
						tableHeader = "No data found";
					} finally {
						try {
							if(crs!=null)
							crs.close();
						} catch (Exception e) {
							debug(5,e.getMessage());
							e.printStackTrace();
						}
					}
					html += "<table border=1 id='"+panelName+"'>" + tableHeader + htmlTemp + "</table>\n";
				}else{// if sg.length >0
					
				}
		   }
			
			
		}
		
		return html;
	}
	/**
	 * This function creates whereclause for the retrieve query 
	 * @param sg
	 * @param tempData
	 * @return where clause
	 */
	private String replaceRefWhereClause(String sg,HashMap<String, HashMap<String, String>> tempData) {
		debug(1,"in replace where parts tempData hashmap:"+tempData);
		Pattern pattern = Pattern.compile("\\{(.*?)\\}");
        Matcher matcher  = pattern.matcher(sg);
        String resultStr = new String(sg);
		while(matcher.find()){
			String str = matcher.group();
			String g = str;
			str = g.substring(1,g.length()-1);  	
			String parts[] = str.split("\\.");
			if(parts.length > 1){
			HashMap<String,String> hm = tempData.get(parts[0]);
			String val = hm.get(parts[1]);
			StringBuffer charseq=new StringBuffer(g);
			resultStr =  resultStr.replace(charseq, val);
			}
			 
		}
		debug(0,"resultStr:"+resultStr);
		return resultStr;
	}



	/**
	 * Expecting whereClause[PanelName]=field!value~#field1!value1
	 * @param args
	 */
/*	public static void main(String[] args) throws Exception{
		
		RetreiveData rd = new RetreiveData();
		rd.doRetrieveData("frmRequest","empid!0~#empname!sam samanta");
	} */
	
	/**
	 * predifined query should be upto table(+)
	 * splWhereClause will only contain TBLCOLUMN like '%%' 
	 * @param metadata [passed on]empty initially passed on to be filled createRetrieveQueryPart1 
	 * @param scrname [input]
	 * @param panelName [input]
	 * @param hmWherePanel [passed to createWhereClause]
	 * @return retrieve query
	 */
	private String createRetrieveQuery(HashMap metadata,String scrname,String panelName, HashMap<String,String> hmWherePanel) {
		String retrieveQuery ="";
		String joiner = " WHERE ";
		
		CrudDAO cd = new CrudDAO();
		String predefQuery = cd.findPreDefQuery(scrname,panelName);
//		if(predefQuery!=null && predefQuery.length() > 0 ){
//			joiner =" AND ";
//		}
		String qryPart1 = cd.createRetrieveQueryPart1(metadata,scrname,panelName);
		String tableName =  cd.findTableByPanels(scrname,panelName);
		String splWhereClause = cd.findSplWhereClsOfPanels(scrname,panelName);
		  
		
		if(splWhereClause != null && ! "".equals(splWhereClause) ){
			splWhereClause  = joiner + splWhereClause+" ";
			joiner =" AND ";
		}else{
			splWhereClause =" ";
		}
		//process where clause
		debug(0,"hmWherePanel"+hmWherePanel);
		String strWhereQuery  = cd.createWhereClause(joiner,scrname,panelName,hmWherePanel,true);
		debug(0,"splWhereClause:"+splWhereClause+";strWhereQuery="+strWhereQuery+";table name:"+tableName);
		
		if(tableName!= null && tableName.length() >0 && ((strWhereQuery!=null && strWhereQuery.length()>0) || ( splWhereClause!=null && splWhereClause.length() >0)))
			if(predefQuery!=null && predefQuery.length() > 0 ){
				retrieveQuery =predefQuery+" "+splWhereClause+strWhereQuery;
			}else{
				retrieveQuery ="SELECT  "+qryPart1 +" FROM "+tableName+splWhereClause+strWhereQuery; 
			}
		else {
			debug(5,"Incomplete query was:"+"SELECT  "+qryPart1 +" FROM "+tableName+splWhereClause+strWhereQuery);
		}
		
		return retrieveQuery;
	}
	
	/**
	 * This function retrieves data in html based on the query
	 * @param scrname
	 * @param query
	 * @return data
	 */
	public String doRetrieveDatabyQuery(String scrName, String query) throws Exception{
		CrudDAO cd = new CrudDAO();
		List <String> lstPanelName = cd.findPanelByScrname(scrName);
		HashMap<String,HashMap<String,String>> tempData = new HashMap<String, HashMap<String,String>>();
		HashMap<String,String> tempPanelData = null;
		HashMap metadata = null;
		String tableHeader = "No data found";
		String html = ""; //outer
		String htmlTemp = "";
		CachedRowSet crs = null;

		//Fill this for every Panel
			tempPanelData = new HashMap();
			metadata = new HashMap();
			Iterator itrPanel = lstPanelName.iterator();
			
			//For every panel in the screen create an XML table 
			while (itrPanel.hasNext()){
	
		
				String panelName = (String) itrPanel.next();
				if(panelName.equalsIgnoreCase("buttonpanel"))continue;
				cd.createRetrieveQueryPart1(metadata,scrName,panelName);
			if (query != null && query.length() > 0) 	{
				try {
				
					crs = cd.executeRetrieveQuery(query);
					htmlTemp = "";
					boolean firstItr = true;
					String data = "";
					String fname="";
					
					//Ideally this loop should run once in case of detail-data retrieval
					while (crs.next()) {
						htmlTemp += "\n<tr >";
						if (firstItr) {
							tableHeader = "\n<tr >";
						}
						System.out.println("hiii i am here");
						if (metadata == null)
							throw new Exception(
									" Retreive Data : metadata null");
						Iterator itrmetadata = metadata.keySet().iterator();
						while (itrmetadata.hasNext()) {
						    fname = (String) itrmetadata.next();
							ListAttribute ls = (ListAttribute) metadata
									.get(fname);
							debug(0,"Fname=" + fname);
							System.out.println("fname "+fname);
						try{	data = crs.getString(fname);
						
						}catch(Exception e){
							continue;
						}
							if(data == null)data="";
							if (firstItr) {
								tableHeader += "<th><div id=" + fname
										+ " style='display:none'>" + ls
										+ "</div>" + ls.getLblname() + "</th>";
							}
							htmlTemp += "<td id=" + fname + ">      " + data + "</td>";
							tempPanelData.put(fname, data);
						}
						if (firstItr) {
							tableHeader += "</tr>";
							firstItr = false;
						}
						htmlTemp += "</tr>";
						
						
						
						debug(0,htmlTemp);
					} //while crs.next()
					tempData.put(panelName, tempPanelData);
				} catch (Exception e) {
					 e.printStackTrace();
					htmlTemp = "";
					tableHeader = "No data found";
				} finally {
					try {
						if(crs!=null)
						crs.close();
					} catch (Exception e) {
						debug(5,e.getMessage());
						e.printStackTrace();
					}
				}
				html += "<table border=1 id='"+panelName+"'>" + tableHeader + htmlTemp + "</table>\n";
			}else{// if sg.length >0
				
			}
			}
		return html;
	}

	/**
	 * This function retrieves data based on the query
	 * @param query
	 * @return data
	 */
	public String doretrievedatabyname(String query){
		CachedRowSet crs = null;
		try {
			DBConnector db = new DBConnector();
			CrudDAO cd = new CrudDAO();
			 crs =  cd.executeRetrieveQuery(query);
			 if(crs.next()){
				 return crs.getString(1);
			 }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	
	

}
