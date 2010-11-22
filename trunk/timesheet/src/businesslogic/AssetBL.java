package businesslogic;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.sql.rowset.CachedRowSet;

import util.Utility;

import dbconn.DBConnector;
import dto.PrepstmtDTOArray;
import dto.PrepstmtDTO.DataType;

public class AssetBL implements BaseBL{
	private void debug(int priority, String s) {
		if(priority > -1){
			System.out.println("RfqBL:"+s);
		}
	}
	@Override
	public HashMap jsrpcProcessBL(Map buslogHm, String rpcid) {
		
		return null;
	}

	@Override
	public HashMap postDeleteProcessBL(Map buslogHm) {
		
		return null;
	}

	@Override
	public HashMap postInsertProcessBL(Map buslogHm) {
		
		return null;
	}

	@Override
	public HashMap postRetreiveProcessBL(Map buslogHm) {
		return null;
	}

	@Override
	public HashMap postSubmitProcessBL(Map hm) {
		
		return null;
	}

	@Override
	public HashMap preDeleteProcessBL(Map buslogHm) {
		
		return null;
	}

	@Override
	public HashMap preInsertProcessBL(Map buslogHm) {
		
		return null;
	}

	@Override
	public HashMap preRetreiveProcessBL(Map buslogHm) {
		
		return null;
	}

	@Override
	public HashMap preSubmitProcessBL(Map hm) {
		
		return null;
	}

	@Override
	public HashMap processRequest(Map buslogHm) {
		
		return null;
	}

	@Override
	public HashMap postUpdateProcessBL(Map buslogHm) {
		debug(1,"postUpdateProcessBL:"+buslogHm);
		    HashMap resulthm = new HashMap();
			String SQL = "update ams_dn set DN_STATUS = 'ASSETUPD',dn_asset_id=? where dn_id=? ";
			CachedRowSet crs = null;
			DBConnector db = new DBConnector();
			
			String[] tmp = (String[]) buslogHm.get("insertKeyValue");
			String keyvaljson = (String)(tmp[0]);
			try {
			HashMap<String, HashMap<String, String>>  keyvalallpanel = Utility.extractKeyValPair(keyvaljson);
			HashMap<String, String> keyvalDN = keyvalallpanel.get("dnPanel");
			 String dnid =   keyvalDN.get("dnid");
			
			 HashMap<String, String> keyvalAsset = keyvalallpanel.get("panelFields");
			 String assetid =   keyvalAsset.get("assetid");
			 //debug(1,keyvalallpanel.toString());
				PrepstmtDTOArray arPrepArray = new PrepstmtDTOArray();
				arPrepArray.add(DataType.STRING, assetid);
				arPrepArray.add(DataType.STRING, dnid);
				System.out.println(arPrepArray.toString(SQL));
				int i = db.executePreparedUpdate(SQL,arPrepArray);
				
			} catch (Exception e) {
				e.printStackTrace();
				resulthm.put("error", "error occured updating DN Status");
			} finally {
				if (crs != null) {
					try {
						crs.close();
					} catch (Exception e) {
					}
				}
			}
			
			return resulthm;
	}

	@Override
	public HashMap preUpdateProcessBL(Map buslogHm) {
		// TODO Auto-generated method stub
		return null;
	}

}
