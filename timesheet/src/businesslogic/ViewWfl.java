package businesslogic;

import java.util.HashMap;
import java.util.Map;

import javax.sql.rowset.CachedRowSet;

import dbconn.DBConnector;

public class ViewWfl implements BaseBL {
	private void debug(int priority, String s) {
		if(priority > -1){
			System.out.println("ViewWfl:"+s);
		}
	}
	public String getParameter(Map buslogHm, String paramName){
		String[] tmp = (String[]) buslogHm.get(paramName);
		String parameter = (String)(tmp[0]);
		return parameter;
	}
	@Override
	public HashMap jsrpcProcessBL(Map buslogHm, String rpcid) {
		debug(0,"hash map argument:"+buslogHm);
		if("".equals(rpcid) ){
			buslogHm.put("error", "RPC not found");
		}else if("scrflowgraph".equals(rpcid)){
			String appid = getParameter(buslogHm, "appid");
			String wflid ="";
			String actiondes = "";
			String SQL = " select WFLACTIONDESC,WFLID,STATUS from  USER_WFLID_APPID where APPID = '"+appid+"' ";
			CachedRowSet crs = null;
			try {
				DBConnector db = new DBConnector();
				crs = db.executeQuery(SQL);
				while(crs.next()){
					actiondes += (String)crs.getString("WFLACTIONDESC")+"!"+(String)crs.getString("STATUS")+",";
					wflid = (String)crs.getString("WFLID");
					buslogHm.put("WFLID", wflid);
				}
				buslogHm.put("WFLACTIONDESC",actiondes);
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
		return (HashMap) buslogHm;
	}

	@Override
	public HashMap postDeleteProcessBL(Map buslogHm) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HashMap postInsertProcessBL(Map buslogHm) {
		// TODO Auto-generated method stub
		return null;
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
	public HashMap preDeleteProcessBL(Map buslogHm) {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		return null;
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
