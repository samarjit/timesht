package actionclass;

import java.io.InputStream;
import java.io.StringBufferInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.json.JSONObject;

import businesslogic.BaseBL;

import com.opensymphony.xwork2.ActionSupport;

import crud.InsertData;
import dao.CrudDAO;




/**
 * This is a action class that inherits Strut's Framework's ActionSupport class and is used to perform the insert operation. 
 *
 */
public class InsertDataAC extends ActionSupport implements ServletRequestAware{

	private InputStream inputStream;
	private String insertKeyValue;
	private String screenName;
	private HttpServletRequest servletRequest;
	private String invokewfl = "false";
	private String redirectUrl = null;
	private String activityname = "";
	private HashMap retBLhm = new HashMap();
	

	private boolean create = false;
	private String wflid;
	
	
	public HashMap getRetBLhm() {
		return retBLhm;
	}

	public void setRetBLhm(HashMap retBLhm) {
		this.retBLhm = retBLhm;
	}

    public String getWflid() {
		return wflid;
	}

	public void setWflid(String wflid) {
		this.wflid = wflid;
	}

	public String getInvokewfl() {
		return invokewfl;
	}

	public void setInvokewfl(String invokewfl) {
		this.invokewfl = invokewfl;
	}

	public String getRedirectUrl() {
		return redirectUrl;
	}

	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}

	public InputStream getInputStream() {
        return inputStream;
    }
    
    public HttpServletRequest getServletRequest() {
		return servletRequest;
	}
    
	@Override
	public void setServletRequest(HttpServletRequest request) {
			servletRequest = request;
	}
	
	public String getInsertKeyValue() {
		return insertKeyValue;
	}

	public void setInsertKeyValue(String insertKeyValue) {
		this.insertKeyValue = insertKeyValue;
	}

	public String getScreenName() {
		return screenName;
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}

	public String getActivityname() {
		return activityname;
	}

	public void setActivityname(String activityname) {
		this.activityname = activityname;
	}

	private void debug( int priority,String s){
		if(priority > 0)
		System.out.println("InsertDataAC:"+s);
	}
	

	/**
	 * execute() method is executed by default. 
	 * @param insertKeyValue 
	 * @param screeName 
	 * @param invokewfl
	 * @returns String 
	 *
	 */
	public String execute()  {
		HashMap metadata = new HashMap();
    	InsertData insert = new InsertData();
    	String autogenId = null;
    	String resultHtml = "No Data found";
    	HttpServletRequest request1 =  ServletActionContext.getRequest();
    	try{
		    	if(servletRequest!=null)
		    	System.out.println("query string RD : " + servletRequest.getQueryString());
		    	
		    	
		    	//System.out.println(request1.getQueryString());
		    	
		    	
		    	//panelFields1WhereClause = request1.getParameter("panelFields1WhereClause");
		    	insertKeyValue = request1.getParameter("insertKeyValue");
		    	screenName = request1.getParameter("screenName");
		    	invokewfl = request1.getParameter("invokewfl");
		    	
		    	System.out.println("InserKeyValue = " + insertKeyValue);
		    	System.out.println("Screen Name  = " + screenName);
		    	
		    	
		    	String insertresult ="";
		    	ArrayList errorList = new ArrayList();
		    	JSONObject jobj = new JSONObject();
		    	
		    	  try {
					autogenId =insert.getNewAppId();
					if(insertKeyValue != null || (!"".equals(insertKeyValue)))
						 insertresult    = insert.doInsert(screenName, insertKeyValue, autogenId);
				} catch (Exception e) {
					insertresult = "Insert failed";
					e.printStackTrace();
				}
		    	if(insertresult.length() >1){
					errorList.add(insertresult);
				}
		    	
		    	HashMap retBL =  postInsertProcessBL(screenName);
		    	debug(5,"business logic ");
		    	if(retBL.get("error")!= null){
					errorList.add("Business Logic error occured");
				}
		    	 debug(5,errorList.toString());
		        
		        try {
		        	jobj.put("message", resultHtml);
					if (errorList.size() > 0) {
						
						jobj.put("error", errorList);
						jobj.put("message", "Record saved successfully");
						resultHtml = jobj.toString();
					}else{
						HashMap blres =   (HashMap) retBL.get("BusinessLogicRESULT");
						if(blres!=null)jobj.put("BusinessLogicRESULT",blres);
						jobj.put("message", "Record saved successfully");
						resultHtml = jobj.toString();
					}
				} catch (Exception e) {
					debug(5,e.toString());
				}
				
		       // String resXML  = getResultXML (qry,metadata); 
		        inputStream = new StringBufferInputStream(resultHtml);
		        debug(5,errorList.toString());
		        System.out.println("in view details");
		        if(errorList.size() > 0){
		        	 return SUCCESS;
		        }
			}catch(Exception e){
				debug(5,"Unknown Exception:"+e);
				e.printStackTrace();
			}
		debug(1,"Redirecting to workflow with invokewfl="+invokewfl);
        if(invokewfl != null){
        	if(invokewfl.equals("worlkflow")){
	        
	        	redirectUrl = request1.getContextPath()+"/workflow.action?ajaxflag=true&appid="+autogenId+"&activityname="+activityname+"&create="+create+"&passeddownerror="+resultHtml;
	        	debug(1,redirectUrl);
	        	return "workflow";
	        	
	        }else if(invokewfl.equals("scrflow")){
	        	redirectUrl = request1.getContextPath()+"/scrworkflow.action?ajaxflag=true&appid="+autogenId+"&activityname="+activityname+"&create="+create+"&passeddownerror="+resultHtml;
	        	debug(1,redirectUrl);
	        	return "workflow";
	        }else{
	        	//unknown workflow request
	        	return SUCCESS;
	        }
        }else{
        	//no workflow associated with this save action
        	return SUCCESS;
        }
	}

	
	/**
	 * This functions instantiates the appropriate business logic class defined in Database and calls the 
	 * postInsertProcessBL function of the class.
	 * @param screenName
	 * @return HashMap
	 */
	private HashMap postInsertProcessBL(String screenName) {
	
		Class aclass = null;
		CrudDAO cd = new CrudDAO();
		HashMap retBLhmtmp = new HashMap();
		String businessLogic = cd.getBusinessLogicName(screenName);
		try {
			if (businessLogic != null && !"".equals(businessLogic)) {
				aclass = Class.forName(businessLogic);
				BaseBL basebl = (BaseBL) aclass.newInstance();
				Map buslogHm = new HashMap();

				Map map = servletRequest.getParameterMap();//parameters;
				
				Iterator iter = map.entrySet().iterator();
				while (iter.hasNext()) {
					Entry n = (Entry) iter.next();
					String key = n.getKey().toString();
					String values[] = (String[]) n.getValue();
					buslogHm.put(key, values);
				}	
				//buslogHm = map;
				retBLhmtmp = basebl.postInsertProcessBL(buslogHm);
				if(retBLhmtmp == null){
					retBLhm.put("message","Unimplemented business logic");
				}else{
					retBLhm.put("BusinessLogicRESULT",retBLhmtmp);
				}
					
			}
			else{
				retBLhm.put("message", "Business logic not defined");
				debug(1," BL Class from DB not defined");
			}
		} catch (Exception e) {
			debug(1,"Businesslogic not found");
			e.printStackTrace();
			retBLhm.put("error","Error executing business logic");
		}
		return retBLhm;
	}
	
	
	public void setParameters(Map<String, String[]> parameters) {
		//this.parameters = parameters;
	}
}
