package actionclass;
import java.io.InputStream;
import java.io.StringBufferInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.struts2.ServletActionContext;

import pojo.Createhtml;


import com.opensymphony.xwork2.ActionSupport;

/**
 * This is a action class that inherits Strut's Framework's ActionSupport class and is used to generate HTML required to be shown on screen. 
 */
public class GenerateHtmlAC extends ActionSupport{
	private void debug( int priority,String s){
		if(priority > 0)
		System.out.println("GenerateHtml:"+s);
	}

	
public String user;
public String password;
public String dataPanel;
private String buttonPanel;
private String leftPanel;
private String bottomPanel;
private String topPanel;
private HashMap extraFields;
private String screenName;
private String errorMessage; 
private String screenTitle;


private String cssname;
private String jsname;
private String panelFieldsWhereClause;
private String screenMode;
private String ajaxPopulate = "";
private InputStream inputStream;
private String passedonvalues;
private String redirectjspname;





public String getPassedonvalues() {
	return passedonvalues;
}


public void setPassedonvalues(String passedonvalues) {
	this.passedonvalues = passedonvalues;
}


public String getScreenTitle() {
	return screenTitle;
}
public void setScreenTitle(String screenTitle) {
	this.screenTitle = screenTitle;
}
public String getAjaxPopulate() {
	return ajaxPopulate;
}
public void setAjaxPopulate(String ajaxPopulate) {
	this.ajaxPopulate = ajaxPopulate;
}
public InputStream getInputStream() {
	return inputStream;
}
public void setInputStream(InputStream inputStream) {
	this.inputStream = inputStream;
}

 
public String getPanelFieldsWhereClause() {
	return panelFieldsWhereClause;
}

public String getScreenMode() {
	return screenMode;
}
public void setScreenMode(String screenMode) {
	this.screenMode = screenMode;
}

public void setPanelFieldsWhereClause(String panelFieldsWhereClause) {
	this.panelFieldsWhereClause = panelFieldsWhereClause;
}

public String getCssname() {
	return cssname;
}

public void setCssname(String cssname) {
	this.cssname = cssname;
}

public String getJsname() {
	return jsname;
}

public void setJsname(String jsname) {
	this.jsname = jsname;
}

public String getErrorMessage() {
	return errorMessage;
}

public void setErrorMessage(String errorMessage) {
	this.errorMessage = errorMessage;
}

public String getScreenName() {
	return screenName;
}

public void setScreenName(String screenName) {
	this.screenName = screenName;
}

public HashMap getExtraFields() {
	return extraFields;
}

public void setExtraFields(HashMap extraFields) {
	this.extraFields = extraFields;
}

public String getButtonPanel() {
	return buttonPanel;
}

public void setButtonPanel(String buttonPanel) {
	this.buttonPanel = buttonPanel;
}

public String getLeftPanel() {
	return leftPanel;
}

public void setLeftPanel(String leftPanel) {
	this.leftPanel = leftPanel;
}

public String getBottomPanel() {
	return bottomPanel;
} 

public void setBottomPanel(String bottomPanel) {
	this.bottomPanel = bottomPanel;
}

public String getTopPanel() {
	return topPanel;
}

public void setTopPanel(String topPanel) {
	this.topPanel = topPanel;
}

public String getDataPanel() {
	return dataPanel;
}

public void setDataPanel(String part1) {
	this.dataPanel = part1;
}

	public String getUser() {
	return user;
}

public void setUser(String user) {
	this.user = user;
}

public String getPassword() {
	return password;
}

public void setPassword(String password) {
	this.password = password;
}

public String getRedirectjspname() {
	return redirectjspname;
}


public void setRedirectjspname(String redirectjsp) {
	this.redirectjspname = redirectjsp;
}
/*
	public static void main(String[] args) {
		 Createhtml htmlc = new Createhtml();
		 List<String> lstPanels =  htmlc.getPanels("frmRequest");
		 List<String> arPanelData = new ArrayList<String>();
		 for(String panelName:lstPanels){
			 arPanelData.add(htmlc.makehtml("frmRequest",panelName));
		 }
		System.out.println("Extrs fields:"+arPanelData);

	} */




/**
 * execute() method is executed by default. 
 * @param screeName
 * @returns String
 */
	public String execute() {
		  
		 String templateName;
		 Createhtml htmlc = new Createhtml();
		 setScreenTitle(htmlc.findScreenTitle(screenName));
		 setJsname(htmlc.getJsCsshtml(screenName));
		 //setDataPanel(htmlc.makehtml("panelFields"));
		 //setButtonPanel(htmlc.makehtml("buttonPanel"));
		 List<String> lstPanels =  htmlc.getPanels(screenName);
		 
		 if(ajaxPopulate.equalsIgnoreCase("true")){
			 String result =  htmlc.makehtml(screenName, lstPanels.get(0));
		        inputStream = new StringBufferInputStream(result);
		        return "populate";

			 
		 }
		 LinkedHashMap<String,String> arPanelData = new LinkedHashMap<String, String>();
		 for(String panelName:lstPanels){
			debug(0,"##"+panelName);
			 arPanelData.put(panelName,htmlc.makehtml(screenName,panelName));
		 } 
		 templateName = htmlc.getTemplateName(screenName);
		 setExtraFields(arPanelData);
		  debug(0,"Extrs fields:"+getExtraFields());
		 // setLeftPanel(leftPanel);
		// setTopPanel(topPanel);
		// setBottomPanel(bottomPanel);
		 
		 
		if(templateName.equalsIgnoreCase("")){
			setErrorMessage("The template was not found for this page!");
			addActionError("The template was not found for this page!");
			templateName = "failure"; //redirects to pages/unknownerror.jsp
		}
		if(templateName.matches(".*(?ims:\\.jsp)")  || templateName.matches(".*(?ims:\\.html)") || templateName.matches(".*(?ims:\\.htm)") ){
			debug(1,"redirecting to jsp:"+ServletActionContext.getServletContext().getContextPath()+"/"+templateName);
			redirectjspname = "/"+templateName;
			return "redirecttojsp";
		}
//		try {
//		 	workflow.main(null);
//		} catch (InvalidInputException e) {
//			e.printStackTrace();
//		} catch (WorkflowException e) {
//			e.printStackTrace();
//		}
		
		return templateName;
	}

}
