package dao;

import java.sql.SQLException;

import javax.sql.rowset.CachedRowSet;

import dbconn.DBConnector;

/**
 * This class is used to connect to the DB for authenticating the user.
 *
 */
public class LoginDAO {
	private void debug(int priority,String s){
		if(priority > 1)
		System.out.println("LoginDAO:"+s);
	}

	/**
	 * This function gets role for the userid
	 * @param userid
	 * @return role
	 */
	public String getUserRole(String userId) {
		String SQL = "select ROLEID from FW_USER where EMPID = '" + userId+"'";
		CachedRowSet crs  = null;
		DBConnector db = new DBConnector();
		String userRole = null;
		try {
			crs = db.executeQuery(SQL);
			if(crs.next())
			userRole  = crs.getString("ROLEID");
		} catch (SQLException e) {
			debug(5,"ROLE NOT FOUND:"+e.getMessage());
		}finally{
			if(crs != null){
				try {
					crs.close();
				} catch (SQLException e) {
					debug(5,"DB Close() failed:"+e.getMessage());
					e.printStackTrace();
				}
				crs = null;
			}
		}
		return userRole;
	}
	/**
	 * This function gets usernmae for the userid
	 * @param userid
	 * @return username
	 */
	public String getUserName(String userId) {
		String SQL = "select EMPNAME from FW_USER where EMPID = '" + userId+"'";
		CachedRowSet crs  = null;
		DBConnector db = new DBConnector();
		String userName = null;
		try {
			crs = db.executeQuery(SQL);
			if(crs.next())
			userName  = crs.getString("EMPNAME");
		} catch (SQLException e) {
			debug(5,"EMPNAME NOT FOUND:"+e.getMessage());
		}finally{
			if(crs != null){
				try {
					crs.close();
				} catch (SQLException e) {
					debug(5,"DB Close() failed:"+e.getMessage());
					e.printStackTrace();
				}
				crs = null;
			}
		}
		return userName;
	}
	/**
	 * This function gets password for the userid
	 * @param userid
	 * @return password
	 */
	public String getPassword(String userId) {
		String password = null;
		String SQL = "select PASSWORD from FW_USER where EMPID = '" + userId+"'";
		CachedRowSet crs  = null;
		DBConnector db = new DBConnector();
		try {
			crs = db.executeQuery(SQL);
			if(crs.next())
				password  = crs.getString("PASSWORD");
		} catch (SQLException e) {
			debug(5,"PASSWROD NOT FOUND:"+e.getMessage());
		}finally{
			if(crs != null){
				try {
					crs.close();
				} catch (SQLException e) {
					debug(5,"DB Close() failed:"+e.getMessage());
					e.printStackTrace();
				}
				crs = null;
			}
		}
		
		return password;
	}
	
}
