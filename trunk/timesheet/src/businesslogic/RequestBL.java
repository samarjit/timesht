package businesslogic;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.sql.rowset.CachedRowSet;

import dbconn.DBConnector;
import dto.PrepstmtDTOArray;
import dto.UserDTO;
import dto.PrepstmtDTO.DataType;

public class RequestBL implements BaseBL{


	private void debug(int priotiry, String s){
		if(priotiry > 0)
			System.out.println("RequestBL:"+s);
	}

	@Override
	public HashMap postRetreiveProcessBL(Map buslogHm) {
		debug(1,"Post Request Business logic");
		return (HashMap) buslogHm;
	}

	@Override
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
			
			
			if(rpcid.equals("close")){
				String[] tmp = (String[]) buslogHm.get("reqid");
				String reqid = (String)(tmp[0]);
				String SQL = "update ams_request set status='CLOSED' where reqid=? ";

				
				try {
					DBConnector db = new DBConnector();
					PrepstmtDTOArray arPrepstmt = new PrepstmtDTOArray();
					arPrepstmt.add(DataType.STRING, reqid);			
					debug(1,arPrepstmt.toString(SQL));

					db.executePreparedUpdate(SQL, arPrepstmt);
				}catch (Exception e) {
					e.printStackTrace();
				}finally {
				
				}
				
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
			
			if(rpcid.equals("getDepartment")){
				String[] tmp = (String[]) buslogHm.get("requestType");
				String requestType = (String)(tmp[0]);
				String queryy = "SELECT DEPT_ID FROM ams_request_type where REQUESTTYPEID=?";
				CachedRowSet crs = null;
				DBConnector db = new DBConnector();
				PrepstmtDTOArray arPrepstmt4 = new PrepstmtDTOArray();
				arPrepstmt4.add(DataType.STRING, requestType);
				try {
					crs = db.executePreparedQuery(queryy, arPrepstmt4);
					while(crs.next()){
						result.put("deptname", (String)crs.getString("DEPT_ID"));
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
			
			if(rpcid.equals("getEmail")){
				
				String[] tmp = (String[]) buslogHm.get("mgrid");
				String mgrid1 = (String)(tmp[0]);
				String queryy = "SELECT empemail mgremail FROM ams_employee where empid=?";
				CachedRowSet crs = null;
				DBConnector db = new DBConnector();
				PrepstmtDTOArray arPrepstmt4 = new PrepstmtDTOArray();
				arPrepstmt4.add(DataType.STRING, mgrid1);
				try {
					crs = db.executePreparedQuery(queryy, arPrepstmt4);
					while(crs.next()){
						result.put("mgremail", (String)crs.getString("mgremail"));
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
				
				
				String query = "SELECT empemail FROM ams_employee where empid=?";
				CachedRowSet crs1 = null;
				DBConnector db1 = new DBConnector();
				PrepstmtDTOArray arPrepstmt = new PrepstmtDTOArray();
				arPrepstmt.add(DataType.STRING, empid);
				try {
					crs1 = db1.executePreparedQuery(query, arPrepstmt);
					while(crs1.next()){
						result.put("empemail", (String)crs1.getString("empemail"));
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



		}
		return (HashMap) result;
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
		return null;
		
	}

	@Override
	public HashMap preRetreiveProcessBL(Map buslogHm) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HashMap postSubmitProcessBL(Map hm) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HashMap preSubmitProcessBL(Map buslogHm) {
		
		String[] cancel = (String[]) buslogHm.get("cancel");
		if(cancel[0].toString().equals("true")){
			String[] tmp = (String[]) buslogHm.get("reqid");
			String reqid = (String)(tmp[0]);
			String SQL = "update ams_request set status='CANCELLED' where reqid=? ";
			try {
				DBConnector db = new DBConnector();
				PrepstmtDTOArray arPrepstmt = new PrepstmtDTOArray();
				arPrepstmt.add(DataType.STRING, reqid);			
				debug(1,arPrepstmt.toString(SQL));

				 db.executePreparedUpdate(SQL, arPrepstmt );
			}catch (Exception e) {
				e.printStackTrace();
			}finally {
				buslogHm.put("nextAction", "Approve");

			}
		}
		else{
		String[] delquotarr = (String[]) buslogHm.get("appid");
		if(delquotarr == null)
			return (HashMap) buslogHm;
		String appid = (String)(delquotarr[0]);
		debug(1, appid);
		String SQL = "update ams_request set status='PENDAPPROVAL' where reqid=? ";
		System.out.println("*************************INSIDE SUBMIT");

		try {
			DBConnector db = new DBConnector();
			PrepstmtDTOArray arPrepstmt = new PrepstmtDTOArray();
			arPrepstmt.add(DataType.STRING, appid);			
			debug(1,arPrepstmt.toString(SQL));

			db.executePreparedUpdate(SQL, arPrepstmt);
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
		}
		}
		return (HashMap)buslogHm;
	}

	@Override
	public HashMap postUpdateProcessBL(Map buslogHm) {
		return null;
	}

	@Override
	public HashMap preUpdateProcessBL(Map buslogHm) {
		// TODO Auto-generated method stub
		return null;
	}

}
