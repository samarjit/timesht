package actionclass;

import com.opensymphony.xwork2.ActionSupport;

public class TestactionAC extends ActionSupport{
 private String name;
 
	public String execute(){
		System.out.println("Inside Action");
		name ="My name";
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

}
