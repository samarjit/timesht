package dto;

import java.io.Serializable;

public class UserDTO implements Serializable{
private String userid;
private String roleid;
private String username;

public String getUsername() {
	return username;
}

public void setUsername(String username) {
	this.username = username;
}

public String getRoleid() {
	return roleid;
}

public void setRoleid(String roleid) {
	this.roleid = roleid;
}

public String getUserid() {
	return userid;
}

public void setUserid(String userid) {
	this.userid = userid;
}

}
