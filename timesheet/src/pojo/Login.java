package pojo;

import dao.LoginDAO;

public class Login {
	private void debug(int priority,String s){
		if(priority > 1)
		System.out.println("Login:"+s);
	}
	public boolean loginMethod(String userId,String password){
		
		return true;
	}
	/**
	 * This function gets role
	 * @param userId
	 * @return role
	 */
	public String getUserRole(String userId){
		LoginDAO ldao = new LoginDAO();
		return ldao.getUserRole(userId);
	}
	/**
	 * Function gets username
	 * @param userId
	 * @return username
	 */
	public String getUserName(String userId) {
		LoginDAO ldao = new LoginDAO();
		return ldao.getUserName(userId);
		
	}
	
	/**
	 * This Function gets the password
	 * @param userId
	 * @return password
	 */
	public String getPassword(String userId) {
		LoginDAO ldao = new LoginDAO();
		return ldao.getPassword(userId);
		
	}
	
}
