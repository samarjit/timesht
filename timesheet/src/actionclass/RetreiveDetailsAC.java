package actionclass;

import java.io.InputStream;
import java.io.StringBufferInputStream;
import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.json.JSONObject;

import businesslogic.BaseBL;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

import crud.RetreiveData;

import dao.CrudDAO;
import dto.UserDTO;

/**
 * This is a action class that inherits Strut's Framework's ActionSupport class and is used to retrieve details. 
 *
 */
public class RetreiveDetailsAC extends ActionSupport implements ServletRequestAware{
	private void debug( int priority,String s){
		if(priority > 0)
		System.out.println("RetreiveDetailsAC:"+s);
	}
	private InputStream inputStream;
	private String whereClause;
	private HttpServletRequest servletRequest;
	private String screenName = null;
	private HashMap retBLhm = new HashMap();
	
    public String getScreenName() {
		return screenName;
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}

	public InputStream getInputStream() {
        return inputStream;
    }
    
    public HttpServletRequest getServletRequest() {
		return servletRequest;
	}
 
	
	 

    /**
     * execute() method is executed by default. 
     * @param whereclause 
     * @param screeName
     * @returns String 
     *
     */
	public String execute()   {
    	HashMap metadata = new HashMap();
    	RetreiveData retrive = new RetreiveData();
    	
    	if(servletRequest!=null)
    	debug(0,"query string RD : " + servletRequest.getQueryString());
    	
    	HttpServletRequest request1 =  ServletActionContext.getRequest();
    	debug(0,request1.getQueryString());
    	
    	
    	//panelFields1WhereClause = request1.getParameter("panelFields1WhereClause");
    	URLDecoder decoder =  new URLDecoder();
			    	
			    	
	    	String resultHtml;
	    	ArrayList errorList = new ArrayList();
	    	resultHtml = "No Records found";
			
	    	JSONObject jobj = new JSONObject();
			    	try {
						whereClause = decoder.decode(request1
								.getParameter("whereClause"));
						debug(0, whereClause);
						HashMap retPreBL = preRetreiveProcessBL(screenName);
						
						debug(2,whereClause);
						if (whereClause != null || (!"".equals(whereClause)))
							resultHtml = retrive.doRetrieveData(screenName,
									whereClause);
						HashMap retPostBL = postRetreiveProcessBL(screenName);
						//System.out.println("-----------------"+retPostBL.get("error"));
						if (retPostBL.get("error") != null) {
							System.out.println("-----------------"
									+ retPostBL.get("error"));
							errorList.add("Post Business Logic error occured");
						}
					} catch (Exception e) {
						e.printStackTrace();
						debug(5,e.getMessage());
						errorList.add("error:"+e.getMessage());
					}
		try {
        	
			if (errorList.size() > 0) {
				
				jobj.put("error", errorList);
				jobj.put("message", "Error in retrive");
				resultHtml = jobj.toString();
			}else{
				jobj.put("message",  URLEncoder.encode( resultHtml,"UTF-8").replaceAll("\\+","%20"));
				//jobj.put("message", resultHtml);
				resultHtml = jobj.toString();
				debug(1, resultHtml );
			}
			
		} catch (Exception e) {
			debug(5,e.toString());
		}
		
		/* if(errorList.size() > 0){
        	 return SUCCESS;
        }*/
		
       // String resXML  = getResultXML (qry,metadata); 
        inputStream = new StringBufferInputStream(resultHtml);
    	//inputStream = new StringBufferInputStream("in view details");
        debug(1,"in view details");
        
        //clearing the where clause after use
        return SUCCESS;
    }
	
	/**
	 * This functions instantiates the appropriate business logic class defined in Database and calls the 
	 * preRetreiveProcessBL function of the class.
	 * @param screenName
	 * @return HashMap
	 */
	
    public HashMap preRetreiveProcessBL(String screenName) {
		Class aclass = null;
		CrudDAO cd = new CrudDAO();
		HashMap retBLhmtmp = new HashMap();
		String businessLogic = cd.getBusinessLogicName(screenName);
		try {
			if (businessLogic != null && !"".equals(businessLogic)) {
				aclass = Class.forName(businessLogic);
				BaseBL basebl = (BaseBL) aclass.newInstance();
				Map buslogHm = new HashMap();

				Map map = servletRequest.getParameterMap();
				Iterator iter = map.entrySet().iterator();
				while (iter.hasNext()) {
					
					Entry n = (Entry) iter.next();
					String key = n.getKey().toString();
					String values[] = (String[]) n.getValue();
					buslogHm.put(key, values);
				}
 					
					HttpSession session = servletRequest.getSession();
					UserDTO usr = (UserDTO) (session.getAttribute("userSessionData"));
					String id = usr.getUserid();
					System.out.println("ID"+id);
					buslogHm.put("userDTO", usr);
					
					retBLhmtmp = basebl.preRetreiveProcessBL(buslogHm);
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


	/**
	 * This functions instantiates the appropriate business logic class defined in Database and calls the 
	 * postRetreiveProcessBL function of the class.
	 * @param screenName
	 * @return HashMap
	 */
	public HashMap postRetreiveProcessBL(String screenName) {
		Class aclass = null;
		CrudDAO cd = new CrudDAO();
		HashMap retBLhmtmp = new HashMap();
		String businessLogic = cd.getBusinessLogicName(screenName);
		try {
			if (businessLogic != null && !"".equals(businessLogic)) {
				aclass = Class.forName(businessLogic);
				BaseBL basebl = (BaseBL) aclass.newInstance();
				Map buslogHm = new HashMap();

				Map map = servletRequest.getParameterMap();
				Iterator iter = map.entrySet().iterator();
				while (iter.hasNext()) {
					Entry n = (Entry) iter.next();
					String key = n.getKey().toString();
					String values[] = (String[]) n.getValue();
					buslogHm.put(key, values);
				}
				
				retBLhmtmp = basebl.postRetreiveProcessBL(buslogHm);
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

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}


	@Override
	public void setServletRequest(HttpServletRequest request) {
		servletRequest = request;
		
	}



}
