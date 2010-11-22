package businesslogic;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.rowset.CachedRowSet;

import dbconn.DBConnector;
import dto.PrepstmtDTOArray;
import dto.UserDTO;
import dto.PrepstmtDTO.DataType;

public class ApprovalReqBL implements BaseBL{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public HashMap preSubmitProcessBL(Map buslogHm) {
		// TODO Auto-generated method stub
		System.out.println("In req submit");
		
		String[] apprarr = (String[]) buslogHm.get("approve");
		if(apprarr != null){			
			String approve = (String)(apprarr[0]);
			if(approve.equals("true")){
							
				String[] reqidarr = (String[]) buslogHm.get("reqid");
				if(reqidarr == null)
					return (HashMap) buslogHm;
				String reqid = (String)(reqidarr[0]);
				
				String[] reqcomarr = (String[]) buslogHm.get("reqcomments");
				if(reqcomarr == null)
					return (HashMap) buslogHm;
				String reqcomment = (String)(reqcomarr[0]);
				
				String RRFSQL = "update ams_request set status='APPROVED', REMARKS=? where reqid=? ";

				
				try {
					DBConnector db = new DBConnector();
					PrepstmtDTOArray arPrepstmt = new PrepstmtDTOArray();
					arPrepstmt.add(DataType.STRING, reqcomment);
					arPrepstmt.add(DataType.STRING, reqid);			
					debug(1,arPrepstmt.toString(RRFSQL));

					db.executePreparedUpdate(RRFSQL, arPrepstmt );
				}catch (Exception e) {
					e.printStackTrace();
				}finally {
					
				}
			}//buslogHm.put("nextAction", "CreateRRF");
													
			else if(approve.equals("false")){
							
				String[] reqidarr = (String[]) buslogHm.get("reqid");
				if(reqidarr == null)
					return (HashMap) buslogHm;
				String reqid = (String)(reqidarr[0]);
				
				String[] reqcomarr = (String[]) buslogHm.get("reqcomments");
				if(reqcomarr == null)
					return (HashMap) buslogHm;
				String reqcomment = (String)(reqcomarr[0]);
				
				String REQSQL = "update ams_request set status='REJECTED', REMARKS=? where reqid=? ";

				
				try {
					DBConnector db = new DBConnector();
					PrepstmtDTOArray arPrepstmt = new PrepstmtDTOArray();
					arPrepstmt.add(DataType.STRING, reqcomment);
					arPrepstmt.add(DataType.STRING, reqid);			
					debug(1,arPrepstmt.toString(REQSQL));

					db.executePreparedUpdate(REQSQL, arPrepstmt );
				}catch (Exception e) {
					e.printStackTrace();
				}finally {
					
				}
				//buslogHm.put("nextAction", "CreateRRF");
			}
		}
		
			
		return (HashMap)buslogHm;	
	}
	
	private void debug(int priotiry, String s){
		if(priotiry > 0)
			System.out.println("RequestBL:"+s);
	}
	
	@Override
	public HashMap postRetreiveProcessBL(Map buslogHm) {
		debug(1,"Post Retrieve Business logic");
		return (HashMap) buslogHm;
	}

	@Override
	public HashMap preRetreiveProcessBL(Map buslogHm) {
		debug(1,"Pre Retrieve Business logic");
		return (HashMap) buslogHm;
	}

	@Override
	public HashMap processRequest(Map buslogHm) 
	{
		// TODO Auto-generated method stub
		debug(1,"Process Request Business logic");
		return (HashMap) buslogHm;
	}

	public HashMap jsrpcProcessBL(Map buslogHm, String rpcid) {

		HashMap result = new HashMap();
		UserDTO usr = (UserDTO) buslogHm.get("userDTO");
		String empid = usr.getUserid();
		debug(1,"Pre Request Business logic");

		if(buslogHm.containsKey("screenName")){

			if(rpcid.equals("createRequest")){

				String query = "SELECT EMPID, EMPNAME FROM AMS_EMPLOYEE where empid=?";

				CachedRowSet crs = null;
				DBConnector db = new DBConnector();
				PrepstmtDTOArray arPrepstmt = new PrepstmtDTOArray();
				arPrepstmt.add(DataType.STRING, empid);
				try {
					crs = db.executePreparedQuery(query, arPrepstmt);
					while(crs.next()){
						result.put("empid", (String)crs.getString("EMPID"));
						result.put("empname", (String)crs.getString("EMPNAME"));
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
				String query1 = "SELECT to_char(sysdate,'dd/mm/yyyy') reqdate from dual";
				CachedRowSet crs1 = null;
				DBConnector db1 = new DBConnector();

				try {
					crs1 = db1.executeQuery(query1);
					while(crs1.next()){
						result.put("reqdate", (String)crs1.getString("REQDATE"));
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


				String query2 = "select empid mgrid from ams_employee where roleid='MANAGER'";
				CachedRowSet crs2 = null;
				DBConnector db2 = new DBConnector();
				int count = 0;
				try {
					crs2 = db2.executeQuery(query2);
					StringBuffer sb = new StringBuffer();
					while(crs2.next()){
						if(count==0){
							sb = sb.append(crs2.getString("MGRID"));
							count++;
						}
						else{
							sb = sb.append(",");
							sb = sb.append(crs2.getString("MGRID"));
						}
					}
					result.put("mgrid", sb);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				finally {
					System.out.println("*********COUNT** "+count);
					if (crs != null) {
						try {
							crs.close();
						} catch (Exception e) {
						}

					}
				}






				String query5 = "select REQID REF_REQID from AMS_REQUEST where empid=? AND reqdate > (SELECT to_char(sysdate - numtoyminterval(6, 'MONTH')) FROM dual)";

				CachedRowSet crs5 = null;
				DBConnector db5 = new DBConnector();
				PrepstmtDTOArray arPrepstmt5 = new PrepstmtDTOArray();
				arPrepstmt5.add(DataType.STRING, empid);

				int count1 = 0;
				try {
					crs5 = db5.executePreparedQuery(query5, arPrepstmt5);
					StringBuffer sb1 = new StringBuffer();
					while(crs5.next()){
						if(count1==0){
							sb1 = sb1.append(crs5.getString("REF_REQID"));
							count1++;
						}
						else{
							sb1 = sb1.append(",");
							sb1 = sb1.append(crs5.getString("REF_REQID"));
						}
					}
					result.put("REF_REQID", sb1);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				finally {
					System.out.println("*********COUNT** "+count);
					if (crs != null) {
						try {
							crs.close();
						} catch (Exception e) {
						}

					}
				}

				String transferType = "One Way,Two Way";
				result.put("transfertype", transferType);


			}
			if(rpcid.equals("getManager")){
				String[] tmp = (String[]) buslogHm.get("empid");
				String empid1 = (String)(tmp[0]);
				String queryy = "SELECT empname mgrname FROM ams_employee where empid=?";
				CachedRowSet crs = null;
				DBConnector db = new DBConnector();
				PrepstmtDTOArray arPrepstmt4 = new PrepstmtDTOArray();
				arPrepstmt4.add(DataType.STRING, empid1);
				try {
					crs = db.executePreparedQuery(queryy, arPrepstmt4);
					while(crs.next()){
						result.put("mgrname", (String)crs.getString("mgrname"));
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


			} 



		}
		return (HashMap) result;
	}


	@Override
	public HashMap postDeleteProcessBL(Map buslogHm) {
		// TODO Auto-generated method stub
		debug(1,"Post Delete Business logic");
		return (HashMap) buslogHm;
	}

	@Override
	public HashMap postInsertProcessBL(Map buslogHm) {
		// TODO Auto-generated method stub
		debug(1,"Post Insert Business logic");
		return (HashMap) buslogHm;
	}

	@Override
	public HashMap preDeleteProcessBL(Map buslogHm) {
		// TODO Auto-generated method stub
		debug(1,"Pre Delete Business logic");
		return (HashMap) buslogHm;
	}

	@Override
	public HashMap preInsertProcessBL(Map buslogHm) {
		// TODO Auto-generated method stub
		debug(1,"Pre Insert Business logic");
		return (HashMap) buslogHm;
	}

	@Override
	public HashMap postSubmitProcessBL(Map hm) {
		// TODO Auto-generated method stub
		debug(1,"Post Submit Business logic");
		return (HashMap) hm;
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
