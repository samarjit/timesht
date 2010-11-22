package workflow;

import java.io.InputStream;
import java.io.StringBufferInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ParameterAware;
import org.apache.struts2.interceptor.RequestAware;
import org.apache.struts2.interceptor.SessionAware;
import org.json.JSONException;
import org.json.JSONObject;

import pojo.CreateMailFromTemplate;

import actionclass.SendMailAC;
import businesslogic.BaseBL;

import com.opensymphony.workflow.InvalidInputException;
import com.opensymphony.workflow.Workflow;
import com.opensymphony.workflow.WorkflowException;
import com.opensymphony.workflow.basic.BasicWorkflow;
import com.opensymphony.workflow.loader.WorkflowDescriptor;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

import dao.CrudDAO;
import dto.ApplicationDTO;
import dto.UserDTO;

/**
 *This Action class is used to get the subsequent actions for the rquested request.
 *
 */
public class WorkflowAC extends ActionSupport implements SessionAware, RequestAware, ParameterAware{
	private void debug(int priority, String s){
		if(priority>-1){
			System.out.println("WorkflowAC:"+s);
		}
	}
private String forwardtourl;
private Map<String, Object> session;
//private Map<String, Object> request;
private WorkflowBean wflBean;
private String redirecturl;
private String screenName;
private Map<String, String[]> parameter;

private String create;
private String cancel;

private String approve;
private String activityname;
private HashMap retBLhm = new HashMap();
private String navigateto;

private String action;
private String ajaxflag;

private String wflid;
private String appid;
private String doString;
private InputStream inputStream;
private String passeddownerror;


public InputStream getInputStream() {
    return inputStream;
}

public String getAjaxflag() {
	return ajaxflag;
}

public void setAjaxflag(String ajaxflag) {
	this.ajaxflag = ajaxflag;
}
public String getCreate() {
	return create;
}

public void setCreate(String create) {
	this.create = create;
}

public String getActivityname() {
	return activityname;
}

public String getApprove() {
	return approve;
}

public void setApprove(String approve) {
	this.approve = approve;
}

public void setActivityname(String activityname) {
	this.activityname = activityname;
}

public String getScreenName() {
	return screenName;
}

public void setScreenName(String screenName) {
	this.screenName = screenName;
}

public String getRedirecturl() {
	return redirecturl;
}

public void setRedirecturl(String redirecturl) {
	this.redirecturl = redirecturl;
}

public WorkflowAC() {
	wflBean = new WorkflowBean();
}

public String getForwardtourl() {
	return forwardtourl;
}
 
public void setForwardtourl(String forwardtourl) {
	this.forwardtourl = forwardtourl;
}



public String getPasseddownerror() {
	return passeddownerror;
}

public void setPasseddownerror(String passeddownerror) {
	this.passeddownerror = passeddownerror;
}

/*
 * /workflow.action?activityname=CR&create=true
 * /workflow.action?action=true&doString="+actionid+"&wflid="+wflid+"&appid="+applicationid+"&screenName="+screenName;
 */ 
public String executeScrflow(){
	String returnStr=SUCCESS;
	UserDTO usrDTO = (UserDTO) session.get("userSessionData");	
	ArrayList<String> errorList = new ArrayList<String>();
	String url=""; 
	String decision=null;
	boolean emailsent = false;
	ArrayList<String>hmActions = new ArrayList<String>();
	try {
		do{		emailsent = false;
				if (create != null) {
					if (activityname != null && !"".equals(activityname)) {
						ApplicationDTO appdto = new ApplicationDTO();
						debug(0, "activityname:" + activityname);
						String WflName = wflBean.getSuitableWorkflowName(activityname);
						wflid = WflName;
						hmActions = wflBean.getNextScrFlowActions(WflName, "",decision); //rest of the places wflid = WflName
						if ("".equals(url) && hmActions.size() > 0) {
							String actionname = (String) hmActions.get(0);
							url = wflBean.getScreenId(actionname);
						}
						wflBean.createApplicationScrWfl(usrDTO.getUserid(),WflName, appid, "S", hmActions);//'S' for started
					}
				} else if (action != null) {
					if (doString != null && !doString.equals("")) {
						preSubmitProcessBL(screenName);
						wflBean.changeStageApplicationScrWfl(usrDTO.getUserid(),wflid, appid, "C", doString);//'C' for close
						 postSubmitProcessBL(screenName);
					} else {
						debug(5, "WorkflowAC:doString is null");
					}
					
					debug(1, "AppId:" + appid+ "  screenflowid:" + wflid+ " doString:(expecting CreateRequest, RFQ etc..)" + doString + "  ");
		
					hmActions = wflBean.getNextScrFlowActions(wflid, doString, decision);
					if ("".equals(url) && hmActions.size() > 0) {
						String actionname = (String) hmActions.get(0);
						url = wflBean.getScreenId(actionname);
					}
					wflBean.updateApplicationScrWfl(usrDTO.getUserid(),wflid, appid, "S", hmActions);//'S' for started
		
				} else if (navigateto != null) {
					String pageName = navigateto;
					url = wflBean.getScreenId(pageName);
				} else if(cancel != null){
					
					wflBean.changeStageApplicationScrWfl(usrDTO.getUserid(),wflid, appid, "C", doString);//'C' for close
					preSubmitProcessBL(screenName); 
					hmActions = new ArrayList<String>();
					if(retBLhm.get("nextAction") != null){
						doString = (String) retBLhm.get("nextAction");
						System.out.println("doString ##" + doString);
						hmActions.add(doString);
						url = wflBean.getScreenId(doString);
					}
					else{
						hmActions = wflBean.getNextScrFlowActions(wflid, doString, decision);
						if ("".equals(url) && hmActions.size() > 0) {
							String actionname = (String) hmActions.get(0);
							url = wflBean.getScreenId(actionname);
						}
					}
					if( hmActions.size() > 0)
					wflBean.updateApplicationScrWfl(usrDTO.getUserid(),wflid, appid, "S", hmActions);//'S' for started
					
				}
				else if(approve != null){
					
					wflBean.changeStageApplicationScrWfl(usrDTO.getUserid(),wflid, appid, "C", doString);//'C' for close
					preSubmitProcessBL(screenName); 
					hmActions = new ArrayList<String>();
					if(approve.equals("true")){
						decision="approved";
					}else{
						decision="rejected";
					}
					hmActions = wflBean.getNextScrFlowActions(wflid, doString, decision);
					String actionname = null;
					if ("".equals(url) && hmActions.size() > 0) {	
						actionname = (String) hmActions.get(0);
						url = wflBean.getScreenId(actionname);
					}
					wflBean.updateApplicationScrWfl(usrDTO.getUserid(),wflid, appid, "S", hmActions);//'S' for started
					if(actionname.equals("End"))
					{				
						wflBean.changeStageApplicationScrWfl(usrDTO.getUserid(),wflid, appid, "C", "End");//'C' & End for close
					}			
				}
				
				
				if(hmActions != null && hmActions.size()>0){
					ScrFlowNode sfn = null;
					try {
						sfn = wflBean.populateScreenflowNode(wflid, hmActions
								.get(0));
						HashMap<String, String> emailProp = sfn.getEmail();
						if (emailProp != null && emailProp.size() > 0) {
							sendEmail(emailProp);
							emailsent = true;
						}
						doString = hmActions.get(0);
					} catch (Exception e) {
						debug(5,"ScreenFlowNode population failed"); 
					}
				}
				
				//Doing another activity right away here of Sending mails.
				
				debug(5,"emailsent="+emailsent);
		}while(emailsent == true);
			
		
	} catch (Exception e) {
	debug(5,"Some Error has occured:"+e.getMessage());
			e.printStackTrace();
			url=url+"&workflowerror=workflow error occured";
			errorList.add("Workflow Error");
			addActionError("Workflow error occured");
		}
		
		redirecturl =url;
		if("".equals(redirecturl)){	redirecturl ="/pages/workflowcompleted.jsp";}
		
		returnStr = "flowcontroller";
		
		String ajaxresultHtml = "";
		 if("true".equals(ajaxflag)){
			 returnStr = "ajaxwflsuccess";
			  try {
				 JSONObject jobjpasseddownerror = null;
				 String msg= "";
				 if(passeddownerror ==null){
					 jobjpasseddownerror = new JSONObject();
					  
				 }else{
					 jobjpasseddownerror = new JSONObject(passeddownerror);
					  msg = jobjpasseddownerror.getString("message");
				 }
				 if(null!=msg && msg.length() > 0 )
					 ajaxresultHtml = msg+ ", workflow completed successfully";
				 else
					 ajaxresultHtml = " workflow completed successfully";
				 
				jobjpasseddownerror.put("message",ajaxresultHtml);
				jobjpasseddownerror.put("workflowurl",ServletActionContext.getRequest().getContextPath()+ url);
				if(errorList.size() > 0)
				jobjpasseddownerror.put("error",errorList);
				 ajaxresultHtml = jobjpasseddownerror.toString();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
		 }
		 	 
		 inputStream = new StringBufferInputStream(ajaxresultHtml);
	return returnStr;	
}



/**
 * 
 * @param emailProp is populated from screen flow xml emailProp {template:"",sendto:"",msgbody:""}
 * Requires more parameters like where clause and other replacer text for template operation
 * 
 * basically all subsequent writes will overwrite previous property thus screen properties is given more importance.
 */
private void sendEmail(HashMap<String,String> emailProp) {

	CreateMailFromTemplate  cm = new CreateMailFromTemplate();
	try {
		
		Map map = parameter;
 		Iterator iter = map.keySet().iterator();
		HashMap<String,String> buslogHm = new HashMap<String, String>();
		buslogHm.putAll(emailProp); 
		while (iter.hasNext()) {
			String key = (String) iter.next();
			if(map.get(key) instanceof String[]){
			 String values[] =  (String[]) map.get(key);
			if(values.length >0) 
			buslogHm .put(key, values[0]);
			}
		}
		
		UserDTO usr = (UserDTO) (session.get("userSessionData"));
		String id = usr.getUserid();
		buslogHm.put("roleid", usr.getRoleid());
		buslogHm.put("username", usr.getUsername());
		buslogHm.put("userid", id);	
		cm.createEmail(emailProp.get("template"), buslogHm	);
		SendMailAC smAC = new SendMailAC();
		smAC.setSendto(cm.getSendto());
		smAC.setFrom(cm.getFrom());
		smAC.setSubject(cm.getSubject());
		smAC.setMsgbody(cm.getMsgbody());
		smAC.sendJavaMail();
	} catch (Exception e) {
		e.printStackTrace();
	}
}

private void postSubmitProcessBL(String screenName2) {
	// TODO Auto-generated method stub
	
}

public String getCancel() {
	return cancel;
}

public void setCancel(String cancel) {
	this.cancel = cancel;
}

private void preSubmitProcessBL(String screenName) {

	Class aclass = null;
	CrudDAO cd = new CrudDAO();
	HashMap retBLhmtmp = new HashMap();
	String businessLogic = cd.getBusinessLogicName(screenName);
	System.out.println("BusinessLogic*************************************************** "+businessLogic);
	HttpServletRequest servletRequest =  ServletActionContext.getRequest();
	try {
		if (businessLogic != null && !"".equals(businessLogic)) {
			aclass = Class.forName(businessLogic);
			BaseBL basebl = (BaseBL) aclass.newInstance();					
			Map buslogHm = new HashMap();
			Map map = parameter;			
			buslogHm = map;
			UserDTO usr = (UserDTO) (session.get("userSessionData"));
			String id = usr.getUserid();
			//System.out.println("ID"+id);
			buslogHm.put("userDTO", usr);			
			retBLhmtmp = basebl.preSubmitProcessBL(buslogHm);
			if(retBLhmtmp == null){
				retBLhm.put("message","Unimplemented business logic");
			}else{
				retBLhm.put("BusinessLogicRESULT",retBLhmtmp);
			}
		}
		else{
			retBLhm.put("message", "Business logic not defined");
			debug(1," BL Class from DB not defined");		}
	} catch (Exception e) {
		debug(1,"Businesslogic not found");
		e.printStackTrace();
		retBLhm.put("error","Error executing business logic");
	}
	
}


/**
 * /workflow.action?activityname=CR&create=true
 * /workflow.action?action=true&doString="+actionid+"&wflid="+wflid+"&appid="+applicationid;
 */
//Original using OSworkflow
	public String execute(){
		String returnStr=SUCCESS;
		UserDTO usrDTO = (UserDTO) session.get("userSessionData");	
		ArrayList<String> errorList = new ArrayList<String>();
		String url=""; 
		long worflowid = -1;
		
		if(wflid != null && !"".equals(wflid))
		worflowid = Long.parseLong(wflid);
		
		try {
			
			
			if (create != null) {
				if (activityname != null && !"".equals(activityname)) {
					ApplicationDTO appdto = new ApplicationDTO();
					debug(0, "activityname:" + activityname);
				//	String appid = wflBean.getNewApplicationId();
					String WflName = wflBean.getSuitableWorkflowName(activityname);
					worflowid = wflBean.workflowInit(appid, WflName, null);
					HashMap<String, Integer> hmActions = wflBean.getAvailableActions(appid, worflowid);
					if ("".equals(url) && hmActions.size() > 0) {
						String actionname = (String) hmActions.keySet().toArray()[0];
						url = wflBean.getScreenId(actionname);
						appdto.setCurrentActionId(hmActions.get(actionname));//used by actionbutton 
						appdto.setCurrentAction(actionname);
					}
					wflBean.createApplicationWfl(usrDTO.getUserid(), worflowid,appid, "S", hmActions);//'S' for started
					appdto.setWflactions(hmActions);
					appdto.setWflid(worflowid);
					session.put("applicationDTO", appdto);
				}
			} else if (action != null) {
				//uses appid, wflid, doString
				ApplicationDTO appdto = (ApplicationDTO) session.get("applicationDTO");
				if (appdto == null)
					appdto = new ApplicationDTO();
				if (appid != null && appid.length() > 0) {
					appdto.setAppid(appid);
				}
				if (worflowid != -1 && worflowid > 0) {
					appdto.setWflid(worflowid);
				}

				String wflSession = appdto.getAppid();

				if (doString != null && !doString.equals("")) {
					int actionid = Integer.parseInt(doString);
					try {
						wflBean.doAction(wflSession/*appid*/, worflowid, actionid);

					} catch (InvalidInputException e) {
						errorList.add("Workflow Error");
						e.printStackTrace();
					} catch (WorkflowException e) {
						errorList.add("Workflow Error");
						e.printStackTrace();
					}
					wflBean.changeStageApplicationWfl(usrDTO.getUserid(),worflowid, wflSession /*appid*/, "C", Integer.parseInt(doString));//'S' for started
				} else {
					debug(5, "WorkflowAC:doString is null");
				}
				
				debug(1, "WflSession:" + wflSession + "  wflId:" + worflowid+ " do:" + doString + "  ");

				HashMap<String, Integer> hmActions = wflBean.getAvailableActions(appid, worflowid);
				if ("".equals(url) && hmActions.size() > 0) {
					String actionname = (String) hmActions.keySet().toArray()[0];
					url = wflBean.getScreenId(actionname);
					appdto.setCurrentActionId(hmActions.get(actionname));//used by actionbutton 
					appdto.setCurrentAction(actionname);
				}
				wflBean.updateApplicationWfl(usrDTO.getUserid(), worflowid,wflSession/*appid*/, "S", hmActions);//'S' for started
				appdto.setWflid(worflowid);
				appdto.setWflactions(hmActions);
				session.put("applicationDTO", appdto);
			} else if (navigateto != null) {
				ApplicationDTO appdto = (ApplicationDTO) session.get("applicationDTO");
				HashMap<String, Integer> hmactions = appdto.getWflactions();
				String pageName = navigateto;
				url = wflBean.getScreenId(pageName);
				appdto.setCurrentAction(pageName);
				appdto.setCurrentActionId(hmactions.get(pageName));
				appdto.setWflid(worflowid);
				
			}
			
			
			 if("true".equals(ajaxflag)){
				
				
			 }
			 
			 
		} catch (Exception e) {
			debug(5,"Some Error has occured:"+e.getMessage());
			e.printStackTrace();
			url=url+"&workflowerror=workflow error occured";
			errorList.add("Workflow Error");
			addActionError("Workflow error occured");
		}
		
		redirecturl =url;
		if("".equals(redirecturl)){	redirecturl ="/pages/workflowcompleted.jsp";}
		
		returnStr = "flowcontroller";
		
		String ajaxresultHtml = "";
		
		 if("true".equals(ajaxflag)){
			 returnStr = "ajaxwflsuccess";
			 try {
				 JSONObject jobjpasseddownerror = new JSONObject(passeddownerror);
				 String msg = jobjpasseddownerror.getString("message");
				 if(msg.length() > 0 )
					 ajaxresultHtml = msg+ ", workflow completed successfully";
				jobjpasseddownerror.put("message",ajaxresultHtml);
				jobjpasseddownerror.put("workflowurl",ServletActionContext.getRequest().getContextPath()+ url);
				if(errorList.size() > 0)
				jobjpasseddownerror.put("error",errorList);
				 ajaxresultHtml = jobjpasseddownerror.toString();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
		 }
		 	 
		 inputStream = new StringBufferInputStream(ajaxresultHtml);
		 
		return returnStr;
	}

	public String getNavigateto() {
		return navigateto;
	}

	public void setNavigateto(String navigateto) {
		this.navigateto = navigateto;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getWflid() {
		return wflid;
	}

	public void setWflid(String wflid) {
		this.wflid = wflid;
	}

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getDoString() {
		return doString;
	}

	public void setDoString(String doString) {
		this.doString = doString;
	}

	@Override
	public void setSession(Map<String, Object> sess) {
		session = sess;
	}

	@Override
	public void setRequest(Map<String, Object> req) {
	//	request = req;
	}

	@Override
	public void setParameters(Map<String, String[]> param) {
		parameter  = param;
		
	}
}
