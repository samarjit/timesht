package businesslogic;

import java.util.HashMap;
import java.util.Map;

import pojo.Login;

public class LoginBL implements BaseBL {

 
	/**
	 * For testing
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	@Override
	public HashMap processRequest(Map buslogHm) {
		//String s[]= (String[]) hm.get("username");
		String error=null;
		String userid=null;
		String password = null;
		String[] usrnamearr = (String[]) buslogHm.get("userid");
		if(usrnamearr == null)
			error="Invalid user ID";
		else{					
			userid = (String)(usrnamearr[0]);
			if(userid.equals("")){
				error="Invalid User ID";
			}
			else{
				String[] passwordarr = (String[]) buslogHm.get("password");		
				if(passwordarr == null)
					error= "Invalid password";	
				else{
					String enterpassword = (String)(passwordarr[0]);
					if(enterpassword.equals("")){
						error="Invalid password";
					}
					else{
						Login lin = new Login();
						password = lin.getPassword(userid);						
						if(password == null || (!password.equals(enterpassword))){
							error="Invalid user name or password";
						}
					}
				}
			}
		}
				
		buslogHm.put("error", error);		
				
		return (HashMap) buslogHm;
	}

	@Override
	public HashMap preSubmitProcessBL(Map hm) {
		 
		return null;
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
	public HashMap jsrpcProcessBL(Map buslogHm, String rpcid) {
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
