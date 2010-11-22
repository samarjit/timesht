package dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.sql.rowset.CachedRowSet;

import dbconn.DBConnector;

/**
 * This class connects to the database and perform appropriate operations for workflow.
 *
 */
public class WorkflowDAO {
	private void debug(int priority, String s){
		if(priority >0)
		System.out.println("WorkflowDAO:"+s);
	}
	

	/**
	 * This function gets the next available action 
	 * @param userid
	 * @param applicationId
	 * @return Hashhmap consisting of actions
	 */
	public HashMap<String, ArrayList<String>> getAvailableAction(String userid,String applicationId) {
		DBConnector db = new DBConnector();
		CachedRowSet crs = null;
		CachedRowSet crs2 = null;
		String SQL = "SELECT  " +
		"U.APPID, U.USERID, U.STATUS  " +
		//"FROM  USER_WFLID_APPID U where U.STATUS='S' and U.USERID='"+userid+"'";
		"FROM  USER_WFLID_APPID U where U.STATUS='S'";
		
		String wflid="";
		String appid="";
		String SQL2="";
		ArrayList<String> arwflid= null;
		HashMap<String, ArrayList<String>> wflsesshm = new HashMap<String, ArrayList<String>>();
		
		try{
		//	crs = db.executeQuery(SQL);
			
		//	while(crs.next()){
				// appid= crs.getString("APPID");
				  
				
				SQL2 = "SELECT  " +
				"U.WFLID, U.USERID, U.STATUS, U.APPID " +
				//"FROM  USER_WFLID_APPID U where U.STATUS='S' and U.USERID='"+userid+"'";
				"FROM  USER_WFLID_APPID U where U.STATUS='S'";
				if(applicationId != null && !"".equals(applicationId)){
					SQL2+= " AND U.APPID='"+applicationId+"'";
				}
				crs2= db.executeQuery(SQL2);
				
				while(crs2.next()){
					arwflid = new ArrayList<String>();
					appid = crs2.getString("APPID");
					
					arwflid.add(crs2.getString("WFLID"));
					if(appid!=null && !"".equals(appid))
					wflsesshm.put(appid,arwflid);
				}
				
			//}
			
			
		}catch(SQLException e){
			debug(0, "Workflow DAO:"+e.getMessage()+":"+SQL+"\n"+SQL2);
		}finally{
			try {
					if(crs!=null){
						crs.close();
					}
					if(crs2!=null){
						crs2.close();
					}
			}catch (SQLException e) {
				debug(0, e.getMessage());
			}
		}
		return wflsesshm;
	}
	/**
	 * This functions gets a new APP ID
	 * @return APP ID
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
				debug(0, e.getMessage());
			}
		}
		return appid;
	}
	
	/**
	 * This function gets the workflow name based on the activity
	 * @param activity
	 * @return workflow name
	 */
	public String getSuitableWorkflowName(String activity) {
		DBConnector db = new DBConnector();
		CachedRowSet crs = null;
		String SQL = "SELECT W.WFLXML, W.WFLNAME, W.ACTIVITY FROM  WFLNAME_ACTIVITY_MAP W where W.ACTIVITY='"+activity+"'";
		String wflName=""; 
		try {
				crs = db.executeQuery(SQL);
				while(crs.next()){
					wflName=  crs.getString("WFLNAME");
				}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try{
				if(crs!=null)crs.close();
			}catch(SQLException e){
				debug(0, e.getMessage());
			}
		}
		return wflName;
	}
	/**
	 * Creates new status entry while creation of an workflow
	 * @param userid
	 * @param id
	 * @param appid
	 * @param status
	 * @param hmActions
		 */
	public void createApplicationWfl(String userid, long id,String appid,String status, HashMap hmActions) {
		DBConnector db = new DBConnector();
		int i =0;
		String wherein="";
		Iterator itr1  = hmActions.keySet().iterator();
		
		while (itr1.hasNext()) {
			String naction = (String) itr1.next();
			wherein = ","+hmActions.get(naction);
		} 
		String SQL1="";
		if(wherein.charAt(0)== ','   ){
			wherein = wherein.substring(1);
		}
		//SQL1 = "delete from USER_WFLID_APPID  where WFLID="+id+" AND  USERID='"+userid+"' AND APPID ='"+appid+"' AND WFLACTIONID in ("+wherein+") ";
		SQL1 = "delete from USER_WFLID_APPID  where WFLID="+id+" AND APPID ='"+appid+"' AND WFLACTIONID in ("+wherein+") ";
		try {
			if(wherein.length() > 0){
				debug(1,SQL1);
				db.executeUpdate(SQL1);
			}
			 
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		 
		 Iterator itr = hmActions.keySet().iterator();
		 while (itr.hasNext()) {
			String actoinName = (String) itr.next();
			int action =  (Integer) hmActions.get(actoinName);
			 
			 try {
			String SQL = "INSERT INTO  USER_WFLID_APPID (WFLID, USERID, STATUS,APPID,WFLACTIONID,  WFLACTIONDESC)  " +
					"VALUES ('" +String.valueOf(id)+"'"+
					" ,'" +userid+"'"+
					" ,'" +status+"'"+
					" ,'" +appid+"'"+
					",'"+action+"'"+
					",'"+actoinName+"'"+
					" ) ";
			debug(1,SQL);
				i += db.executeUpdate(SQL);
				
				} catch (SQLException e) {
					debug(0, e.getMessage());
				}finally{
					 
				}
		}
 
		
	}
	
	/**
	 * Function gets the screen ID
	 * @param activityname
	 * @return screen ID
	 */
	public String getScreenId(String activityname) {
		DBConnector db = new DBConnector();
		CachedRowSet crs = null;
		String SQL = "SELECT W.SCREENID FROM WFLACTION_SCREEN_MAP W WHERE W.WFLACTIVITY='"+activityname+"'";
		String screenName=""; 
		try {
			debug(0,SQL);
				crs = db.executeQuery(SQL);
				while(crs.next()){
					screenName=  crs.getString("SCREENID");
				}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try{
				if(crs!=null)crs.close();
			}catch(SQLException e){
				debug(0, e.getMessage());
			}
		}
		return screenName;	
	}
	/**
	 * This function is called to mark the end of a stage 
	 * @param userid
	 * @param id
	 * @param appid
	 * @param status
	 * @param action
	 */
	public void changeStageApplicationWfl(String userid, long id,
			String appid, String status, int action) {
		int i=0;
		 try {
			  DBConnector db = new DBConnector(); 
				String SQL = "update  USER_WFLID_APPID SET STATUS ='"+status+"' where   " +
						" WFLID = '" +String.valueOf(id)+"'"+
					//	" AND USERID = '" +userid+"'"+
						" AND APPID='" +appid+"'"+
						" AND WFLACTIONID='"+action+"'"+
						"   ";
					debug(1, SQL); 
					i += db.executeUpdate(SQL);
					
					} catch (SQLException e) {
						debug(0, e.getMessage());
					}finally{
						 
		 
					}
	}
	/**
	 * This method marks beginning of a new stage. If the stage is already in list it is updated to started once again(The stage got repeated due to workflow).
	 * If the stage was not in list then a new insert will be done with status as started
	 * @param userid
	 * @param id
	 * @param appid
	 * @param status
	 * @param hmActions
	 */
	public void updateApplicationWfl(String userid, long id, String appid,
			String status, HashMap hmActions) {
		CachedRowSet crs = null;
		DBConnector db = new DBConnector();
		int result=0;
		 Iterator itr = hmActions.keySet().iterator();
		 while (itr.hasNext()) {
			String actoinName = (String) itr.next();
			int action =  (Integer) hmActions.get(actoinName);

			try {
				String	 SQL1 = "select 'x' x from USER_WFLID_APPID  where WFLID="+id+" AND  USERID='"+userid+"' AND APPID ='"+appid+"' AND WFLACTIONID ='"+action +"' ";
				debug(1,SQL1);
				crs = db.executeQuery(SQL1);
				if(crs.next()){
					String SQL = "update  USER_WFLID_APPID SET STATUS ='"+status+"' where   " +
					" WFLID = '" +String.valueOf(id)+"'"+
				//	" AND USERID = '" +userid+"'"+
					" AND APPID='" +appid+"'"+
					" AND WFLACTIONID='"+action+"'"+
					"   ";
					debug(0, SQL); 
					db.executeUpdate(SQL);
					crs.close();
					crs = null;
				}else{
					String SQL = "INSERT INTO  USER_WFLID_APPID (WFLID, USERID, STATUS,APPID,WFLACTIONID,  WFLACTIONDESC)  " +
					"VALUES ('" +String.valueOf(id)+"'"+
					" ,'" +userid+"'"+
					" ,'" +status+"'"+
					" ,'" +appid+"'"+
					",'"+action+"'"+
					",'"+actoinName+"'"+
					" ) ";
					debug(1,SQL1);
					result += db.executeUpdate(SQL);
				}
			} catch (SQLException e) {
				debug(0, e.getMessage());
			}finally{
				if(crs!=null){
					try {
						crs.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
					
				}
			}
		}
		
	}
	/**
	 * This function creates a new application screen flow
	 * @param userid
	 * @param wflName
	 * @param appid
	 * @param status
	 * @param hmActions
	 */
	public void createApplicationScrWfl(String userid, String wflName,String appid, String status, ArrayList<String> hmActions) {
		DBConnector db = new DBConnector();
		int i =0;
		String wherein="";
		Iterator itr1  = hmActions.iterator();
		
		while (itr1.hasNext()) {
			String naction = (String) itr1.next();
			wherein = ",'"+naction+"'";
		} 
		String SQL1="";
		if(wherein.charAt(0)== ','   ){
			wherein = wherein.substring(1);
		}
		//SQL1 = "delete from USER_WFLID_APPID  where WFLID='"+wflName+"' AND  USERID='"+userid+"' AND APPID ='"+appid+"' AND WFLACTIONDESC in ("+wherein+") ";
		SQL1 = "delete from USER_WFLID_APPID  where WFLID='"+wflName+"'  AND APPID ='"+appid+"' AND WFLACTIONDESC in ("+wherein+") ";
		try {
			if(wherein.length() > 0){
				debug(1,SQL1);
				db.executeUpdate(SQL1);
			}
			 
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		 
		 Iterator itr = hmActions.iterator();
		 while (itr.hasNext()) {
			String actoinName = (String) itr.next();
			String action =  actoinName;
			 
			 try {
			String SQL = "INSERT INTO  USER_WFLID_APPID (WFLID, USERID, STATUS,APPID,  WFLACTIONDESC)  " +
					"VALUES ('" +wflName+"'"+
					" ,'" +userid+"'"+
					" ,'" +status+"'"+
					" ,'" +appid+"'"+
					//",'"+action+"'"+
					",'"+actoinName+"'"+
					" ) ";
			debug(1,SQL);
				i += db.executeUpdate(SQL);
				
				} catch (SQLException e) {
					debug(0, e.getMessage());
				}finally{
					 
				}
		}
		
	}
	/**
	 * This function changes the stage of the application screen flow
	 * @param userid
	 * @param wflid
	 * @param appid
	 * @param status
	 * @param doString
	 */
	public void changeStageApplicationScrWfl(String userid, String wflid,String appid, String status, String doString) {
		int i=0;
		 try {
			  DBConnector db = new DBConnector(); 
				String SQL = "update  USER_WFLID_APPID SET STATUS ='"+status+"' where   " +
						" WFLID = '" +wflid+"'"+
					//	" AND USERID = '" +userid+"'"+
						" AND APPID='" +appid+"'"+
						" AND WFLACTIONDESC='"+doString+"'"+
						"   ";
					debug(1, SQL); 
					i += db.executeUpdate(SQL);
					
					} catch (SQLException e) {
						debug(0, e.getMessage());
					}finally{
						 
		 
					}
		
	}
	/**
	 * This function updates the status of the application workflow
	 * @param userid
	 * @param wflid
	 * @param appid
	 * @param status
	 * @param hmActions
	 */
	public void updateApplicationScrWfl(String userid, String wflid,String appid, String status, ArrayList<String> hmActions) {
		CachedRowSet crs = null;
		DBConnector db = new DBConnector();
		int result=0;
		 Iterator itr = hmActions.iterator();
		 while (itr.hasNext()) {
			String actoinName = (String) itr.next();
			String action =  actoinName;

			try {
				String	 SQL1 = "select 'x' x from USER_WFLID_APPID  where WFLID='"+wflid+"' AND  USERID='"+userid+"' AND APPID ='"+appid+"' AND WFLACTIONDESC ='"+action +"' ";
				debug(1,SQL1);
				crs = db.executeQuery(SQL1);
				if(crs.next()){
					String SQL = "update  USER_WFLID_APPID SET STATUS ='"+status+"' where   " +
					" WFLID = '" +wflid+"'"+
				//	" AND USERID = '" +userid+"'"+
					" AND APPID='" +appid+"'"+
					" AND WFLACTIONDESC='"+action+"'"+
					"   ";
					debug(0, SQL); 
					db.executeUpdate(SQL);
					crs.close();
					crs = null;
				}else{
					String SQL = "INSERT INTO  USER_WFLID_APPID (WFLID, USERID, STATUS,APPID,   WFLACTIONDESC)  " +
					"VALUES ('" +wflid+"'"+
					" ,'" +userid+"'"+
					" ,'" +status+"'"+
					" ,'" +appid+"'"+
				//	",'"+action+"'"+
					",'"+actoinName+"'"+
					" ) ";
					debug(1,SQL1);
					result += db.executeUpdate(SQL);
				}
			} catch (SQLException e) {
				debug(0, e.getMessage());
			}finally{
				if(crs!=null){
					try {
						crs.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
					
				}
			}
		}
		
	}
}
