package businesslogic;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.sql.rowset.CachedRowSet;
import dbconn.DBConnector;
import dto.PrepstmtDTOArray;
import dto.PrepstmtDTO.DataType;

public class DnBL implements BaseBL{

	private static final long serialVersionUID = 1L;

	private void debug(int priority, String s) 
	{
		if(priority > -1){
			System.out.println("DnBL:"+s);
		}
	}
	
	@Override
	public HashMap jsrpcProcessBL(Map buslogHm, String rpcid) 
	{		
		return (HashMap) buslogHm;
	}

	@Override
	public HashMap postRetreiveProcessBL(Map buslogHm) 
	{
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
	
	public HashMap preSubmitProcessBL(Map buslogHm) {
		System.out.println("Inside Pre Insert Process BL>>>>>");
		System.out.println("BuslogHm Value1>>>"+buslogHm.get("deliverynoteid"));
		
		
		String[] dnidarr = (String[])buslogHm.get("deliverynoteid");
		String dnpoid=(String)(dnidarr[0]);
				
		//String SQLSelPO="select po_id,po_status from AMS_PO";
		CachedRowSet crs=null;
						
			//DBConnector db = new DBConnector();
			//PrepstmtDTOArray arPrepstmt1 = new PrepstmtDTOArray();
			//debug(0,arPrepstmt1.toString(SQLSelPO));
					
		   // try 
		  //  {
		    	//crs = db.executePreparedQuery(SQLSelPO, arPrepstmt1);
				//while(crs.next())
				//{
						//if(dnpoid.equals((String)crs.getString("po_id")))
						//{
							String SQL = "update AMS_DN set DN_STATUS='SUBMITTED' where DN_ID=? ";
						    try 
							{
								DBConnector db1 = new DBConnector();
								PrepstmtDTOArray arPrepstmt2 = new PrepstmtDTOArray();
								arPrepstmt2.add(DataType.STRING, dnpoid);			
								debug(0,arPrepstmt2.toString(SQL));
								int i = db1.executePreparedUpdate(SQL, arPrepstmt2 );
																		
							}catch (Exception e) {
								e.printStackTrace();
							}finally {  }
							//continue;
						//}//for if statement
					
				//}// for while statement
					
			//} catch (SQLException e) {
			//	e.printStackTrace();
			//}
				
		//-----------------------------return------------------------------------
		return (HashMap) buslogHm;	
	}


	@Override
	
	public HashMap postDeleteProcessBL(Map buslogHm) 
	{
		return (HashMap)buslogHm;
	}

	@Override
	/**
	 * update the status of purchase order to 'DNCREATE'
	 * 
	 */
	public HashMap postInsertProcessBL(Map buslogHm) 
	{
        System.out.println("Inside Pre Insert Process BL>>>>>");
		System.out.println("BuslogHm Value1>>>"+buslogHm.get("dnpoid"));
		
		
		String[] dnidarr = (String[])buslogHm.get("dnpoid");
		String dnpoid=(String)(dnidarr[0]);
				
		String SQLSelPO="select po_id,po_status from AMS_PO";
		CachedRowSet crs=null;
						
			DBConnector db = new DBConnector();
			PrepstmtDTOArray arPrepstmt1 = new PrepstmtDTOArray();
			debug(0,arPrepstmt1.toString(SQLSelPO));
					
		    try 
		    {
		    	crs = db.executePreparedQuery(SQLSelPO, arPrepstmt1);
				while(crs.next())
				{
						if(dnpoid.equals((String)crs.getString("po_id")))
						{
							String SQL = "update AMS_PO set PO_STATUS='DNCREATED' where PO_ID=? ";
						    try 
							{
								DBConnector db1 = new DBConnector();
								PrepstmtDTOArray arPrepstmt2 = new PrepstmtDTOArray();
								arPrepstmt2.add(DataType.STRING, dnpoid);			
								debug(0,arPrepstmt2.toString(SQL));
								int i = db1.executePreparedUpdate(SQL, arPrepstmt2 );
																		
							}catch (Exception e) {
								e.printStackTrace();
							}finally {  }
							continue;
						}//for if statement
					
				}// for while statement
					
			} catch (SQLException e) {
				e.printStackTrace();
			}
				
		//-----------------------------return------------------------------------
		return (HashMap) buslogHm;
		
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
	public HashMap postSubmitProcessBL(Map buslogHm) {
		return null;
	}

	@Override
	public HashMap postUpdateProcessBL(Map buslogHm) {
		return null;
	}

	@Override
	public HashMap preUpdateProcessBL(Map buslogHm) {
		return null;
	}

}
