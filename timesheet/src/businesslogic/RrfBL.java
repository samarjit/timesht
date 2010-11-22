package businesslogic;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.naming.InitialContext;
import javax.sql.rowset.CachedRowSet;

import dbconn.DBConnector;
import dto.PrepstmtDTOArray;
import dto.UserDTO;
import dto.PrepstmtDTO.DataType;

public class RrfBL implements BaseBL{

	private void debug(int priority, String s) {
		if(priority > -1){
			System.out.println("RrfBL:"+s);
		}
	}
	
	
	@Override
	public HashMap jsrpcProcessBL(Map buslogHm, String rpcid) {		
		return (HashMap) buslogHm;
	}

	@Override
	public HashMap postRetreiveProcessBL(Map buslogHm) {
		debug(1, "I am in RRF post process");
		return (HashMap)buslogHm;
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
	/**
	 * update the status of rrf to PENDAPPROVAL
	 * 
	 */
	public HashMap preSubmitProcessBL(Map buslogHm) {
		System.out.println("In rrf submit");
		
		String[] cancelarr = (String[]) buslogHm.get("cancel");
		if(cancelarr != null){			
			String cancel = (String)(cancelarr[0]);
			if(cancel.equals("true")){
				
				String[] qtidarr = (String[]) buslogHm.get("qtid");
				if(qtidarr == null)
					return (HashMap) buslogHm;
				String qtid = (String)(qtidarr[0]);
				String QTSQL = "update ams_quotation set qt_status='RRFCANCELLED' where qt_id=? ";

				CachedRowSet qtcrs = null;
				try {
					DBConnector db = new DBConnector();
					PrepstmtDTOArray arPrepstmt = new PrepstmtDTOArray();
					arPrepstmt.add(DataType.STRING, qtid);			
					debug(1,arPrepstmt.toString(QTSQL));

					qtcrs = db.executePreparedQuery(QTSQL, arPrepstmt );
				}catch (Exception e) {
					e.printStackTrace();
				}finally {
					if (qtcrs != null) {
						try {
							qtcrs.close();
						} catch (Exception e) {
						}
					}
				}
				
				
				
				String[] rrfidarr = (String[]) buslogHm.get("rrfid");
				if(rrfidarr == null)
					return (HashMap) buslogHm;
				String rrfid = (String)(rrfidarr[0]);
				String RRFSQL = "update ams_rrf set rrf_status='CANCELLED' where rrf_id=? ";

				CachedRowSet rrfcrs = null;
				try {
					DBConnector db = new DBConnector();
					PrepstmtDTOArray arPrepstmt = new PrepstmtDTOArray();
					arPrepstmt.add(DataType.STRING, rrfid);			
					debug(1,arPrepstmt.toString(RRFSQL));

					rrfcrs = db.executePreparedQuery(RRFSQL, arPrepstmt );
				}catch (Exception e) {
					e.printStackTrace();
				}finally {
					if (rrfcrs != null) {
						try {
							rrfcrs.close();
						} catch (Exception e) {
						}
					}
				}
								
				
				buslogHm.put("nextAction", "CreateRRF");
			}						
		}					
		else{
			//update the status of rrf.
			String[] rrfidarr = (String[]) buslogHm.get("rrfid");
			if(rrfidarr == null)
				return (HashMap) buslogHm;
			String rrfid = (String)(rrfidarr[0]);
			debug(1, rrfid);
			String SQL = "update ams_rrf set rrf_status='PENDAPPROVAL' where rrf_id=? ";

			CachedRowSet crs = null;
			try {
				DBConnector db = new DBConnector();
				PrepstmtDTOArray arPrepstmt = new PrepstmtDTOArray();
				arPrepstmt.add(DataType.STRING, rrfid);			
				debug(1,arPrepstmt.toString(SQL));

				crs = db.executePreparedQuery(SQL, arPrepstmt );
			}catch (Exception e) {
				e.printStackTrace();
			}finally {
				if (crs != null) {
					try {
						crs.close();
					} catch (Exception e) {
					}
				}
			}
		}
		//send mail to approver - Not Working--
		//boolean res = true;
		/*InitialContext ic;
		String snName = "java:comp/env/mail/MyMailSession";
		Session session = null;		
		try {
			ic = new InitialContext();
			session = (Session) ic.lookup(snName);
		} catch (Exception e) {
			debug(5, "Exception: JNDI failed!");
		}
		if (session == null) {
			debug(0, "Using non JNDI way");
			Properties props = System.getProperties();
			props.put("mail.from", "admin@hp.com");
			session = Session.getInstance(props, null);
		}
		Message msg = new MimeMessage(session);
		try {
			msg.setSubject("RRF For approval");
			msg.setSentDate(new Date());
			msg.setFrom();
			 
			msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(
					"mgr@mydomain.com", false));
			msg.setContent("RRF for approval","text/html");
			Transport.send(msg);

			debug(1, "Email send to:" + "mgr@mydomain.com" + msg.getSubject() + " from:"
					+ "admin@mydomain.com" + " sub:" + msg.getSubject());
		} catch (Exception e) {
			debug(5, "Exception in sending mail!");
			e.printStackTrace();
			//res = false;
		}		*/
		
		return (HashMap)buslogHm;		
	}


	@Override
	/**
	 * update the status of quotation back to NEW
	 * 
	 */
	public HashMap postDeleteProcessBL(Map buslogHm) {
		
		String[] delquotarr = (String[]) buslogHm.get("quotationid");
		if(delquotarr == null)
			return (HashMap) buslogHm;
		String quotationid = (String)(delquotarr[0]);
		debug(1, quotationid);
		String SQL = "update ams_quotation set qt_status='NEW' where qt_id= ? ";

		
		try {
			DBConnector db = new DBConnector();
			PrepstmtDTOArray arPrepstmt = new PrepstmtDTOArray();
			arPrepstmt.add(DataType.STRING, quotationid);			
			debug(1,arPrepstmt.toString(SQL));

			db.executePreparedUpdate(SQL, arPrepstmt );
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			
		}
		return (HashMap)buslogHm;
	}

	@Override
	/**
	 * update the status of quotation to RRFCREATED
	 * 
	 */
	public HashMap postInsertProcessBL(Map buslogHm) {
		
		String[] quotarr = (String[]) buslogHm.get("quotationid"); 
		if(quotarr == null){
			return (HashMap)buslogHm;
		}
		String quotationid = (String)(quotarr[0]);
		debug(1, quotationid);
		String SQL = "update ams_quotation set qt_status='RRFCREATED' where qt_id= ? ";

		
		try {
			DBConnector db = new DBConnector();
			PrepstmtDTOArray arPrepstmt = new PrepstmtDTOArray();
			arPrepstmt.add(DataType.STRING, quotationid);			
			debug(1,arPrepstmt.toString(SQL));

			db.executePreparedUpdate(SQL, arPrepstmt );
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			
		}		
		return (HashMap) buslogHm;
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
