package dto;

import java.io.Serializable;

public class WflViewDTO implements Serializable {
private String wflsessionname;
private String wflid;
public String getWflsessionname() {
	return wflsessionname;
}
public void setWflsessionname(String wflsessionname) {
	this.wflsessionname = wflsessionname;
}
public String getWflid() {
	return wflid;
}
public void setWflid(String wflid) {
	this.wflid = wflid;
}
}
