package businesslogic;

import java.util.HashMap;
import java.util.Map;

import javax.sql.rowset.CachedRowSet;

import dbconn.DBConnector;
import dto.PrepstmtDTOArray;
import dto.PrepstmtDTO.DataType;

public class AllocationBL implements BaseBL{
	
 
	private void debug(int priority, String s){
		if(priority>-1){
			System.out.println("AllocationBL:"+s);
		}
	}
	public String getParameter(Map buslogHm, String paramName){
		String[] tmp = (String[]) buslogHm.get(paramName);
		String parameter = "";
		if (tmp != null)
			parameter = (String) (tmp[0]);
		return parameter;
	}
	
	@Override
	public HashMap jsrpcProcessBL(Map buslogHm, String rpcid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HashMap postDeleteProcessBL(Map hm) {
		int res = -1;
		HashMap hmRes= new HashMap();
		try {
			DBConnector db = new DBConnector();
			String appid = getParameter(hm,"appid"); 
			String data2 = getParameter(hm,"allocid"); 
			String assetid = getParameter(hm,"assetid"); 
			debug(1,"postDeleteProcessBL appid:"+appid+"   "+data2+ "  assetid"+assetid);
			
			 
			
		String	SQL = " update AMS_ASSET set ASSET_QTY_AVAILABLE = ASSET_QTY_AVAILABLE+1 where ASSET_ID=? ";
			PrepstmtDTOArray  prepar2 = new PrepstmtDTOArray(); 
			prepar2.add(DataType.STRING, assetid);
			debug(1,prepar2.toString(SQL));
			res = db.executePreparedUpdate(SQL, prepar2);
			
			
			SQL = " update AMS_ASSET set ASSET_ALLOC_STATUS='FREE' where ASSET_QTY_AVAILABLE >0 AND ASSET_ID=? ";
			PrepstmtDTOArray  prepar3 = new PrepstmtDTOArray(); 
			prepar3.add(DataType.STRING, assetid);
			debug(1,prepar3.toString(SQL));
			res = db.executePreparedUpdate(SQL, prepar3);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			 
		}
		hmRes.put("result", res);
		return hmRes;
	}

	@Override
	public HashMap postInsertProcessBL(Map hm) {
		int res = -1;
		HashMap hmRes= new HashMap();
		try {
			DBConnector db = new DBConnector();
			String appid = getParameter(hm,"appid"); 
			String data2 = getParameter(hm,"allocid"); 
			String assetid = getParameter(hm,"assetid"); 
			debug(1,"postDeleteProcessBL appid:"+appid+"   "+data2+ "  assetid"+assetid);
			
			 
			
			String	SQL = " update AMS_ASSET set ASSET_QTY_AVAILABLE = ASSET_QTY_AVAILABLE-1 where ASSET_ID=? ";
			PrepstmtDTOArray  prepar2 = new PrepstmtDTOArray(); 
			prepar2.add(DataType.STRING, assetid);
			debug(1,prepar2.toString(SQL));
			res = db.executePreparedUpdate(SQL, prepar2);
			
			
			SQL = " update AMS_ASSET set ASSET_ALLOC_STATUS='NOT_FREE' where ASSET_QTY_AVAILABLE <1 AND ASSET_ID=? ";
			PrepstmtDTOArray  prepar3 = new PrepstmtDTOArray(); 
			prepar3.add(DataType.STRING, assetid);
			debug(1,prepar3.toString(SQL));
			res = db.executePreparedUpdate(SQL, prepar3);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			 
		}
		if(res >0)
		hmRes.put("result", res);
		return hmRes;
	}

	@Override
	public HashMap postRetreiveProcessBL(Map buslogHm) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HashMap postSubmitProcessBL(Map hm) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HashMap postUpdateProcessBL(Map buslogHm) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HashMap preDeleteProcessBL(Map hm) {
		return null;
	}

	@Override
	public HashMap preInsertProcessBL(Map buslogHm) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HashMap preRetreiveProcessBL(Map buslogHm) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HashMap preSubmitProcessBL(Map hm) {
		int res = -1;
		HashMap h = new HashMap();
		try {
			DBConnector db = new DBConnector();
			String appid = getParameter(hm,"appid"); 
			String data2 = getParameter(hm,"allocid"); 
			String assetid = getParameter(hm,"assetid"); 
			debug(1,"preSubmitProcessBL :"+appid+"   "+data2+ "  "+assetid);
			
			String SQL = " update AMS_REQUEST set STATUS='RESOLVED'  where reqid=? ";
			PrepstmtDTOArray  prepar = new PrepstmtDTOArray(); 
			prepar.add(DataType.STRING, appid);
			debug(1,prepar.toString(SQL));
			res = db.executePreparedUpdate(SQL, prepar);
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			 
		}
		return h;
	}

	@Override
	public HashMap preUpdateProcessBL(Map buslogHm) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HashMap processRequest(Map buslogHm) {
		// TODO Auto-generated method stub
		return null;
	}

}
