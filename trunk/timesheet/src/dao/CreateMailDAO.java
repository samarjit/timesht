package dao;

import java.util.HashMap;

import javax.sql.rowset.CachedRowSet;

import dbconn.DBConnector;

public class CreateMailDAO {

	public HashMap<String,String> findMailDetailsByTemplate(String template) {
		String SQL = " SELECT  SENDTOQRY,     SENDTOQWHERE, SUBJECT,FROMTEXT, BODYTEXT FROM EMAIL_TEMPLATE  where TEMPLATENAME ='"+template+"'";
		HashMap<String,String> hmret = new HashMap<String,String>();
		CachedRowSet crs = null;
		try {
			DBConnector db = new DBConnector();
			crs = db.executeQuery(SQL);
			while(crs.next()){
				hmret.put("SENDTOQRY", crs.getString("SENDTOQRY"));
				//hmret.put("SENDTOTABLE", crs.getString("SENDTOTABLE"));
				hmret.put("SENDTOQWHERE", crs.getString("SENDTOQWHERE"));
				hmret.put("SUBJECT", crs.getString("SUBJECT"));
				hmret.put("FROMTEXT", crs.getString("FROMTEXT"));
				hmret.put("BODYTEXT", crs.getString("BODYTEXT"));
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
		return hmret;
	}

	public String getSendto(String sql) {
		CachedRowSet crs = null;
		String sendto = "";
		try {
			DBConnector db = new DBConnector();
			crs = db.executeQuery(sql);
			if(crs.next()){
				sendto = crs.getString(1);
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
		return sendto;
	}

}
