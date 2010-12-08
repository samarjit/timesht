package actionclass;

import com.opensymphony.xwork2.ActionSupport;

public class TestactionAC extends ActionSupport{

	public String execute(){
		System.out.println("Inside Action");
		return SUCCESS;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
