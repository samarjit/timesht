package businesslogic;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.rowset.CachedRowSet;

import dbconn.DBConnector;
import dto.PrepstmtDTOArray;
import dto.PrepstmtDTO.DataType;

public class POBL implements BaseBL {

	@Override
	public HashMap jsrpcProcessBL(Map buslogHm, String rpcid) {
		HashMap result = new HashMap();
		String vendorid = null;
		if(rpcid.equals("viewPO")){
			

			String retPO = "SELECT * FROM AMS_PO where PO_ID=?";

			CachedRowSet crs = null;
			DBConnector db = new DBConnector();
			PrepstmtDTOArray arPrepstmt = new PrepstmtDTOArray();
			String[] tmp = (String[]) buslogHm.get("poid");
			String poid = (String)(tmp[0]);
			arPrepstmt.add(DataType.STRING, poid);
			try {
				crs = db.executePreparedQuery(retPO, arPrepstmt);
				while(crs.next()){
					result.put("poid", (String)crs.getString("PO_ID"));
					result.put("podate", (String)crs.getString("PO_DATE"));
					result.put("comments", (String)crs.getString("PO_COMMENTS"));

				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			finally {
				if (crs != null) {
					try {
						crs.close();
					} catch (Exception e) {
					}

				}
			}
			
			
			String retQ = "SELECT * FROM AMS_QUOTATION where QT_ID=?";

			CachedRowSet crs1 = null;
			DBConnector db1 = new DBConnector();
			PrepstmtDTOArray arPrepstmt1 = new PrepstmtDTOArray();
			String[] tmp1 = (String[]) buslogHm.get("qid");
			String qid = (String)(tmp1[0]);
			arPrepstmt1.add(DataType.STRING, qid);
			try {
				crs1 = db1.executePreparedQuery(retQ, arPrepstmt1);
				while(crs1.next()){
					result.put("qid", (String)crs1.getString("QT_ID"));
					result.put("qref", (String)crs1.getString("QT_REF"));
					result.put("itemid", (String)crs1.getString("QT_ITEM_ID"));
					result.put("qty", (String)crs1.getString("QT_ITEM_QTY"));
					result.put("currency", (String)crs1.getString("QT_CURR_ID"));
					result.put("uprice", (String)crs1.getString("QT_UNIT_PRICE"));
					result.put("discount", (String)crs1.getString("QT_DISCOUNT"));
					result.put("misc", (String)crs1.getString("QT_MISE"));
					result.put("totalprice", (String)crs1.getString("QT_TOTAL_AMT"));
					result.put("misc", (String)crs1.getString("QT_MISE"));

					vendorid = (String)crs1.getString("QT_RFQ_VENDOR_ID");


				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			finally {
				if (crs1 != null) {
					try {
						crs1.close();
					} catch (Exception e) {
					}

				}

			}

			
			String retven = "SELECT * FROM AMS_VENDOR where VENDOR_ID=?";

			CachedRowSet crs2 = null;
			DBConnector db2 = new DBConnector();
			PrepstmtDTOArray arPrepstmt2 = new PrepstmtDTOArray();
			
			arPrepstmt2.add(DataType.STRING, vendorid);
			try {
				crs2 = db2.executePreparedQuery(retven, arPrepstmt2);
				while(crs2.next()){
					result.put("vendorName", (String)crs2.getString("VENDOR_NAME"));
					result.put("vendorAddr", (String)crs2.getString("VENDOR_ADDRESS"));
								}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			finally {
				if (crs1 != null) {
					try {
						crs1.close();
					} catch (Exception e) {
					}

				}

			}
						
		}

		
		return result;
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
	public HashMap preSubmitProcessBL(Map buslogHm) {
		
		String[] delquotarr = (String[]) buslogHm.get("poid");
		
		if(delquotarr == null)
			return (HashMap) buslogHm;
		String poid = (String)(delquotarr[0]);
		String SQL = "update ams_po set po_status='SEND' where po_id=? ";

		try {
			DBConnector db = new DBConnector();
			PrepstmtDTOArray arPrepstmt = new PrepstmtDTOArray();
			arPrepstmt.add(DataType.STRING, poid);			
			

			db.executePreparedUpdate(SQL, arPrepstmt);
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			
		}
		return (HashMap)buslogHm;
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
