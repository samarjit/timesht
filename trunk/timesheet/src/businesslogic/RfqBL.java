package businesslogic;

import java.util.HashMap;
import java.util.Map;

import javax.sql.rowset.CachedRowSet;

import dbconn.DBConnector;
import dto.PrepstmtDTOArray;
import dto.PrepstmtDTO.DataType;

public class RfqBL implements BaseBL {
	private void debug(int priority, String s) {
		if(priority > 0){
			System.out.println("RfqBL:"+s);
		}
	}
	public String getParameter(Map buslogHm, String paramName){
		String[] tmp = (String[]) buslogHm.get(paramName);
		String parameter = (String)(tmp[0]);
		return parameter;
	}
	@Override
	public HashMap preSubmitProcessBL(Map hm) {
		//System.out.println("In rfqsubmit()");
		String rfqid = getParameter(hm, "rfqid");
		String SQL = " update AMS_RFQ set RFQ_STATUS = 'CANCELLED' where RFQ_ID= '"+rfqid+"'";
		int res = 0;
		try {
			DBConnector db = new DBConnector();
			res = db.executeUpdate(SQL);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			 
		}	
		return null;
	}

	@Override
	public HashMap jsrpcProcessBL(Map buslogHm, String rpcid) {
		debug(0,"hash map argument:"+buslogHm);
		if("".equals(rpcid) ){
			buslogHm.put("error", "RPC not found");
		}else if("vendordata".equals(rpcid)){
			String[] tmp = (String[]) buslogHm.get("vendorid");
			String vendorid = (String)(tmp[0]);
			tmp = (String[]) buslogHm.get("rfqid");
			String rfqid=(String)tmp[0] ;
			String SQL = "select vendor_id, vendor_name,vendor_email, vendor_address,SUGGEST_DLV_TIME, ar.RFQ_ID,ar.RFQ_ITEM_TYPE,ar.RFQ_ITEM_DESC, ar.RFQ_ITEM_QTY " +
			"from ams_vendor av,ams_rfq_vendor_map b, AMS_RFQ ar where ar.RFQ_ID = b.RFQID AND av.vendor_id = b.vendorid and av.VENDOR_ID=? " +
			"AND b.RFQID = ? ";
			CachedRowSet crs = null;
			try {
				DBConnector db = new DBConnector();
				PrepstmtDTOArray arPrepstmt = new PrepstmtDTOArray();
				arPrepstmt.add(DataType.STRING, vendorid);
				arPrepstmt.add(DataType.STRING, rfqid);
				debug(0,arPrepstmt.toString(SQL));
				crs = db.executePreparedQuery(SQL, arPrepstmt );
				
				while(crs.next()){
					buslogHm.put("vendor_id", (String)crs.getString("vendor_id"));
					buslogHm.put("vendor_email", (String)crs.getString("vendor_email"));
					buslogHm.put("vendor_name", (String)crs.getString("vendor_name"));
					buslogHm.put("vendor_address", (String)crs.getString("vendor_address"));
					buslogHm.put("suggested_delivery_time", (String)crs.getString("SUGGEST_DLV_TIME"));
					buslogHm.put("rfq_item_type", (String)crs.getString("RFQ_ITEM_TYPE"));
					buslogHm.put("rfq_item_desc", (String)crs.getString("rfq_item_desc"));
					buslogHm.put("rfq_item_qty", (String)crs.getString("rfq_item_qty"));
					 
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
		}
		return (HashMap) buslogHm;
	}

	

	@Override
	public HashMap postRetreiveProcessBL(Map buslogHm) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HashMap preRetreiveProcessBL(Map buslogHm) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HashMap processRequest(Map buslogHm) {
		// TODO Auto-generated method stub
		return null;
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
	public HashMap preUpdateProcessBL(Map buslogHm) {
		// TODO Auto-generated method stub
		return null;
	}

}
