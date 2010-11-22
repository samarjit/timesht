package actionclass;

import com.opensymphony.xwork2.ActionSupport;

/**
 * This is a action class that inherits Strut's Framework's ActionSupport class and is used for sending mails and printing documents for RFQ. 
 *
 */
public class RFQDispatcherAC extends ActionSupport{
private String vendorid;
private String rfqid;
private String itemtype;
private String itemdesc;
private String quantity;
private String vendoremail;
private String vendorName;


public String getRfqid() {
	return rfqid;
}

public void setRfqid(String rfqid) {
	this.rfqid = rfqid;
}

	public String getVendorid() {
	return vendorid;
}

public void setVendorid(String vendorid) {
	this.vendorid = vendorid;
}

/**
 * This method is executed for sending mail. 
 * @returns String 
 *
 */
	public String sendemailrfqAction(){
		System.out.println("sendemailrfqAction");
		return SUCCESS;
	}
	/**
	 * This method is executed for printing RFQ document. 
	 * @returns String 
	 *
	 */
	public String printrfqAction(){
		
		return SUCCESS;
	}
}
