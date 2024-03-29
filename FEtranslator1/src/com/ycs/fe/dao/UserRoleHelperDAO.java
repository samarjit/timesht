package com.ycs.fe.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.rowset.CachedRowSet;

import com.ycs.fe.dao.DBConnector;
import com.ycs.fe.dto.PrepstmtDTO.DataType;
import com.ycs.fe.dto.PrepstmtDTOArray;
import com.ycs.user.Role;
import com.ycs.user.RoleRightsMap;
import com.ycs.user.Task;

public class UserRoleHelperDAO {
 
	  
	public List<Role> getRolesForUser(String userId){
		DBConnector db = new DBConnector();
		
		String qry = "select roleid from user_role_map where userid=?";
		
		PrepstmtDTOArray arPrepstmt = new PrepstmtDTOArray();
		arPrepstmt.add(DataType.STRING, userId);
		CachedRowSet crs = null;
		List<Role> roleList = new ArrayList<Role>();
		Role tmpRole = null;
		try {
			crs = db.executePreparedQuery(qry , arPrepstmt );
			while(crs.next()){
				tmpRole = new Role(); 
				tmpRole.setRoleId(crs.getString("roleid") );
				roleList.add(tmpRole);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				if(crs!=null){
					crs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return roleList;
	}
	
	public RoleRightsMap getTaskList(String roleId){
		DBConnector db = new DBConnector();
		RoleRightsMap roletask = new RoleRightsMap();
		List<Task> tasklist = new ArrayList<Task>();
		String query = "SELECT MENU_ID FROM ROLE_RIGHTS_MAP WHERE ROLE=?";
		PrepstmtDTOArray arPrepstmt = new PrepstmtDTOArray();
		arPrepstmt.add(DataType.STRING, roleId);
		CachedRowSet crs = null;
		try {
			crs = db.executePreparedQuery(query , arPrepstmt );
			while (crs.next()) {
				Task task = new Task();
				task.setTaskid(crs.getString("MENU_ID"));
				tasklist.add(task);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				if(crs!=null){
					crs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		roletask.setRoleId(roleId);
		roletask.setTasks(tasklist);
		return roletask;
	}
	public static void main(String[] args) {

	}
	
}
