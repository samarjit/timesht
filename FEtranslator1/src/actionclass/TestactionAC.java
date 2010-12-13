package actionclass;

import com.opensymphony.xwork2.ActionSupport;

public class TestactionAC extends ActionSupport{
 private String name;
 private String retname;
 private String retrieveName;
 
 
	public String execute(){
		System.out.println("Inside Action..name="+name);
		name ="My name";
		System.out.println("Inside Action class [retrieveName]:"+retrieveName);
		return SUCCESS;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRetname() {
		return retname;
	}
	public void setRetname(String retname) {
		this.retname = retname;
	}
	public String getRetrieveName() {
		return retrieveName;
	}
	public void setRetrieveName(String retrieveName) {
		this.retrieveName = retrieveName;
	}
	 

}
