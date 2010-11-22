package dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.sql.rowset.CachedRowSet;
import dbconn.DBConnector;
import dto.PrepstmtDTOArray;
import dto.PrepstmtDTO.DataType;

/**
 * VendorMapDAO does all the data access functionality for AMS_RFQ_VENDOR_MAP
 * @author SAMARJIT
 *
 */
public class VendorMapDAO {
	public void debug(int priority, String s){
		s="VendorMapDAO:"+s;
		if(priority >0-1)
		System.out.println(s);
	}

	/**
	 * Inserts a new mapping entry in AMS_RFQ_VENDOR_MAP
	 * @param rfqid
	 * @param vendorid
	 * @param typenotify
	 * @param suggestdelvtime
	 * @return
	 */
	public String insert(String rfqid, String vendorid, String typenotify, String suggestdelvtime) {
		DBConnector db = new DBConnector();
		String retStr = "SUCCESS";
//		String SQL = "insert into AMS_RFQ_VENDOR_MAP(rfqid,vendorid,type_notify,INDV_STATUS,SUGGEST_DLV_TIME) " +
//		"values('"+rfqid+"','"+vendorid+"','"+typenotify+"','NotAttended',"+suggestdelvtime+") ";
		String SQL = "insert into AMS_RFQ_VENDOR_MAP(rfqid,vendorid,type_notify,INDV_STATUS,SUGGEST_DLV_TIME) " +
		"values(?,?,?,'NotAttended',?) ";
		PrepstmtDTOArray prepar =  new PrepstmtDTOArray();
		prepar.add(DataType.STRING, rfqid);
		prepar.add(DataType.STRING, vendorid);
		prepar.add(DataType.STRING, typenotify);
		prepar.add(DataType.INT, suggestdelvtime);
		debug(1,prepar.toString(SQL));
		CachedRowSet crs = null;
		try {
			 debug(0,SQL);
			db.executePreparedUpdate(SQL, prepar );
			debug(0,"insert did not fail");
		} catch (Exception e) {
			e.printStackTrace();
			retStr = "FAILED";
			debug(0,"insert failed");
		} finally {
			if (crs != null) {
				try {
					crs.close();
				} catch (Exception e) {
				}
			}
		}

		
		return retStr;		
	}

	/**
	 * Selects Vendors that can be mapped to RFQs related to a particular department
	 * @param department
	 * @return
	 */
	public Map getVendorList(String department) {
		
		CachedRowSet crs = null;
		String vendoridlist="";
		String SQL2 = "select vendor_id from ams_dept_vendor_map where dept_id ='"+department+"' ";
		
		if(department != null && !"".equals(department)){
			try {
				DBConnector db = new DBConnector();
				crs = db.executeQuery(SQL2);
				while(crs.next()){
					vendoridlist=vendoridlist+",'"+crs.getString("vendor_id")+"'";
				}
				if(vendoridlist.length() > 0 && vendoridlist.charAt(0) == ',')
				vendoridlist = vendoridlist.substring(1);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (crs != null) {
					try {
						crs.close();
					} catch (Exception e) {
					}
				}
			}
		}
		
		String SQL = "select vendor_id, vendor_name, vendor_rating, vendor_status from AMS_VENDOR ";
		debug(0,"specific vendoridlist :"+vendoridlist);
		if(vendoridlist.length() > 0){
			
			SQL = SQL + " where vendor_id in ("+vendoridlist+")";
		}
		Map vlist = new HashMap();
		try {
			DBConnector db = new DBConnector();
			debug(0,SQL);
			crs = db.executeQuery(SQL);
			while(crs.next()){
				vlist.put(crs.getString("vendor_id"),crs.getString("vendor_name"));
			 
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (crs != null) {
				try {
					crs.close();
				} catch (Exception e) {
				}
			}
		}
		return vlist;
	}

	/**
	 * Selects all the entries of a mapping between RFQ and vendors that are currently there. It also
	 * gets type of notify information and email and print status
	 * @param rfqid
	 * @return
	 */
	public ArrayList<HashMap<String, String>> selectAll(String rfqid) {
		CachedRowSet crs = null;
		String SQL = "select vendor_id, vendor_name,b.TYPE_NOTIFY,b.INDV_STATUS,b.SUGGEST_DLV_TIME, vendor_rating,vendor_email, vendor_status from AMS_VENDOR a, AMS_RFQ_VENDOR_MAP b  where a.VENDOR_ID = b.VENDORID " +
				"AND RFQID='"+rfqid+"'";
		HashMap<String,String> vlist = null;//new HashMap<String, String>();
		ArrayList<HashMap<String,String>> arvlist = new ArrayList<HashMap<String,String>>();
		try {
			DBConnector db = new DBConnector();
			debug(0,SQL);
			crs = db.executeQuery(SQL);
			while(crs.next()){
				vlist = new HashMap<String, String>();
				vlist.put("vendor_id", crs.getString("vendor_id"));
				vlist.put("vendor_name",crs.getString("vendor_name"));
				vlist.put("TYPE_NOTIFY",crs.getString("TYPE_NOTIFY"));
				vlist.put("INDV_STATUS",crs.getString("INDV_STATUS"));
				vlist.put("SUGGEST_DLV_TIME",crs.getString("SUGGEST_DLV_TIME"));
				vlist.put("vendor_rating",crs.getString("vendor_rating"));
				vlist.put("vendor_email",crs.getString("vendor_email"));
				vlist.put("vendor_status",crs.getString("vendor_status"));
				
				arvlist.add(vlist);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (crs != null) {
				try {
					crs.close();
				} catch (Exception e) {
				}
			}
		}
		debug(0,arvlist.toString());
		return arvlist;
	}

	/**
	 * deletes a vendor mapping
	 * @param rfqid
	 * @param vendorid
	 * @return
	 */
	public String delete(String rfqid, String vendorid) {
		DBConnector db = new DBConnector();
		String retStr = "SUCCESS";
		String SQL = "delete from AMS_RFQ_VENDOR_MAP where rfqid = ? AND vendorid = ? ";

		PrepstmtDTOArray prepar =  new PrepstmtDTOArray();
		prepar.add(DataType.STRING, rfqid);
		prepar.add(DataType.STRING, vendorid);
		debug(1,prepar.toString(SQL));
		CachedRowSet crs = null;
		try {
			 debug(0,SQL);
			db.executePreparedUpdate(SQL, prepar );
			debug(0,"delete did not fail");
		} catch (Exception e) {
			e.printStackTrace();
			retStr = "FAILED";
			debug(0,"delete failed");
		} finally {
			if (crs != null) {
				try {
					crs.close();
				} catch (Exception e) {
				}
			}
		}

		
		return retStr;
	}

	/**
	 * Maps all the vendors that are possible to map to RFQ of a particular department. User can delete the unwanted 
	 * mappings after this
	 * @param rfqid
	 * @param department
	 * @param typenotify
	 * @param suggestdelvtime
	 * @return
	 */
	public String initialMap(String rfqid, String department, String typenotify, String suggestdelvtime) {
		String retStr = "SUCCESS";
		DBConnector db = new DBConnector();
		PrepstmtDTOArray prepar =  new PrepstmtDTOArray();
		CachedRowSet crs = null;
		CachedRowSet crs2 = null;
		String SQLvendorid= " select vendor_id from ams_dept_vendor_map where dept_id =?  " +
				"minus " +
				"select  a.VENDOR_ID from AMS_VENDOR a, AMS_RFQ_VENDOR_MAP b  where a.VENDOR_ID = b.VENDORID AND rfqid =?";
			
		prepar.add(DataType.STRING, department);
		prepar.add(DataType.STRING, rfqid);
		
		try {debug(1,prepar.toString(SQLvendorid));
			crs = 	db.executePreparedQuery(SQLvendorid, prepar );
			while(crs.next()){
			
			String vendorid = crs.getString("vendor_id");
			
		String SQL = "insert into AMS_RFQ_VENDOR_MAP(rfqid,vendorid,type_notify,INDV_STATUS,SUGGEST_DLV_TIME) values( ?, " +
		"?, " +
		"?,'NotAttended', ?) ";
		
		
		PrepstmtDTOArray prepar2 =  new PrepstmtDTOArray();
		prepar2.add(DataType.STRING, rfqid);
		prepar2.add(DataType.STRING, vendorid);
		prepar2.add(DataType.STRING, typenotify);
		prepar2.add(DataType.INT, suggestdelvtime);
		
		
			
			debug(1,prepar2.toString(SQL));
			 db.executePreparedUpdate(SQL, prepar2 );
			
		
			}
		} catch (Exception e) {
			e.printStackTrace();
			retStr = "FAILED";
		} finally {
			if (crs2 != null) {
				try {
					crs2.close();
				} catch (Exception e) {
				}
			}
			if (crs != null) {
				try {
					crs.close();
				} catch (Exception e) {
				}
			}
		}
		return retStr;
	}

	/**
	 * Type of Notification is updated like email or print and number of times the vendor has been notified.
	 * The information is kept is a textual format so that later new modes of notification eg.SMS can be added 
	 * without any change in java code
	 * @param rfqid
	 * @param vendorid
	 * @param typenotify
	 * @param indvstatus
	 * @return
	 */
	public String updateTypeNotify(String rfqid, String vendorid,
			String typenotify,String indvstatus) {
		CachedRowSet crs = null;
		String retStr = "SUCCESS";
		String SQL = "update ams_rfq_vendor_map set TYPE_NOTIFY=?" ;
				SQL += ", INDV_STATUS=? " ;
				SQL += "where rfqid =? and vendorid=? ";

		try {
			DBConnector db = new DBConnector();
			
			PrepstmtDTOArray prepar =  new PrepstmtDTOArray();
			prepar.add(DataType.STRING, typenotify);
			prepar.add(DataType.STRING, indvstatus);
			prepar.add(DataType.STRING, rfqid);
			prepar.add(DataType.STRING, vendorid);
			
			debug(1,prepar.toString(SQL));
			db.executePreparedUpdate(SQL, prepar);
			
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
			retStr = "FAILED";
		} finally {
			if (crs != null) {
				try {
					crs.close();
				} catch (Exception e) {
				}
			}
		}
		
		///To check if all vendors of RFQ has been processed
		String SQL2 = "select  count( indv_status) num  from ams_rfq_vendor_map where indv_status=? and rfqid = ? ";
		CachedRowSet crs2 = null;
		CachedRowSet crs3 = null;
		String rfqstatus = "CREATE";
		try {
			DBConnector db = new DBConnector();
			PrepstmtDTOArray prepar =  new PrepstmtDTOArray();
			prepar.add(DataType.STRING, "Attended");
			prepar.add(DataType.STRING, rfqid);
			crs2 = db.executePreparedQuery(SQL2,prepar);
			if(crs2.next()){
				String count = crs2.getString("num");
				if(count !=null && Integer.parseInt(count) > 0 ){
					PrepstmtDTOArray prepar2 =  new PrepstmtDTOArray();
					prepar2.add(DataType.STRING, "NotAttended");
					prepar2.add(DataType.STRING, rfqid);
					crs3 = db.executePreparedQuery(SQL2,prepar2);
					if(crs3.next()){
						String count2 = crs3.getString("num");
						if(count2 !=null && Integer.parseInt(count2) == 0 ){
							//There are conditions of attended but none are left unattended update RFQ staus
							rfqstatus = "SEND";
							
						}else{ //Records still with NotAttended
							rfqstatus = "CREATE";
						}
					}
				}else{ //no record with Attended
					rfqstatus = "CREATE";
				}
			}
			String SQL3 = "update AMS_RFQ set RFQ_STATUS=? where rfq_id= ? ";
			PrepstmtDTOArray prepar3 =  new PrepstmtDTOArray();
			prepar3.add(DataType.STRING, rfqstatus);
			prepar3.add(DataType.STRING, rfqid);
			db.executePreparedUpdate(SQL3,prepar3);
			retStr = "RFQSTATUSUPDATE:"+rfqstatus;
		} catch (Exception e) {
			e.printStackTrace();
			retStr = "FAILED:RFQ Insert failed";
		} finally {
			if (crs2 != null) {
				try {
					crs2.close();
				} catch (Exception e) {
				}
			}
		}
		debug(1,retStr);
		return retStr;
	}
	
	
}
