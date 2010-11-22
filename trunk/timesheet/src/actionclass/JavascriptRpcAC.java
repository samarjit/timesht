package actionclass;

import java.io.InputStream;
import java.io.StringBufferInputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.struts2.interceptor.ParameterAware;
import org.apache.struts2.interceptor.SessionAware;
import org.json.JSONObject;

import businesslogic.BaseBL;

import com.opensymphony.xwork2.ActionSupport;

import dao.CrudDAO;
import dto.UserDTO;

/**
 * This is a action class that inherits Strut's Framework's ActionSupport class and is used to for invoking Business Logic classes directly from Javascript. 
 */
public class JavascriptRpcAC extends ActionSupport implements  ParameterAware,SessionAware{
	private void debug( int priority,String s){
		if(priority > 0)
		System.out.println("JavascriptRpcAC:"+s);
	}
	
	private InputStream inputStream;
	private String screenName = null;
	private Map parameters;
	private String rpcid;
	private Map session;
	
    public String getRpcid() {
		return rpcid;
	}

	public void setRpcid(String rpcid) {
		this.rpcid = rpcid;
	}

	public String getScreenName() {
		return screenName;
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}
	public InputStream getInputStream() {
        return inputStream;
    }
	
	/**
	 * This functions instantiates the appropriate business logic class defined in Database and calls the 
	 * jsrpcProcessBL function of the class.
	 * @param screenName
	 * @return HashMap
	 */
	public HashMap jsrpcProcessBL(String screenName) {
		Class aclass = null;
		HashMap retBLhm = new HashMap();
		CrudDAO cd = new CrudDAO();
		
		try {
			String businessLogic = cd.getBusinessLogicName(screenName);
			debug(1,businessLogic);
			if (businessLogic != null && !"".equals(businessLogic)) {
				aclass = Class.forName(businessLogic);
				BaseBL basebl = (BaseBL) aclass.newInstance();
				Map buslogHm = new HashMap();

				Map map = parameters;//servletRequest.getParameterMap();
//				Iterator iter = map.entrySet().iterator();
//				while (iter.hasNext()) {
//					Entry n = (Entry) iter.next();
//					String key = n.getKey().toString();
//					String values[] = (String[]) n.getValue();
//					buslogHm.put(key, values);
//				}
				buslogHm = map;
				UserDTO usr = (UserDTO) (session.get("userSessionData"));
				String id = usr.getUserid();
				System.out.println("ID"+id);
				buslogHm.put("userDTO", usr);
				retBLhm = basebl.jsrpcProcessBL(buslogHm, rpcid);
			}else{
				retBLhm.put("error", "Method not found");
			}
		} catch (Exception e) {
			debug(1,"Businesslogic not found");
			retBLhm.put("error", "Method not found");
			e.printStackTrace();
		}
		
		return retBLhm;
	}
	
	/**
	 * execute() method is executed by default. 
	 * @param  rpcid 
	 * @param screenName 
	 * @returns String 
	 */
	public String execute(){
		
		String resultHtml = null;
		debug(1,"jsrpc start..."+screenName+" "+rpcid); 
		HashMap hm =  jsrpcProcessBL(screenName);
		JSONObject jobj = new JSONObject(hm);
		resultHtml = jobj.toString();
		debug(1,"json result:"+resultHtml);
		inputStream = new StringBufferInputStream(resultHtml);
		return SUCCESS;
	}

	@Override
	public void setParameters(Map<String, String[]> parameters) {
		this.parameters = parameters;
	}

	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
		
	}
}
