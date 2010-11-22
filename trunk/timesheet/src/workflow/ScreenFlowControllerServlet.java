package workflow;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import pojo.GenerateMenu;
import pojo.Login;

import dto.UserDTO;

import businesslogic.BaseBL;

/**
 * Servlet implementation class ScreenFlowControllerServlet
 * This class authenticates the user and generates the menu based on the role of the user.
 */
public class ScreenFlowControllerServlet extends HttpServlet {
	private void debug(String s){
		System.out.println(s);
	}
	private static final long serialVersionUID = 1L;
    private ScreenFlow scrfl=null;   
    
    public ScreenFlowControllerServlet() {
        super();
        scrfl = new ScreenFlow();
    }

	//currentPageName
    //flowname

    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	String pageaction = request.getParameter("currentaction");
    	String flowName = request.getParameter("screenflowname");
    	String businessLogic = scrfl.getBusinessLogic(flowName,pageaction);
    	
    	Class aclass = null;
    	String url = "";
    	HashMap  retBLhm = null;
    	try {
    		UserDTO usrDTO = new UserDTO();
    		HttpSession session = request.getSession(true);
    		Login lin = new Login();
    	//	if(session.getAttribute("userSessionData") ==null){
    			if (businessLogic != null && !"".equals(businessLogic)) {
    				aclass = Class.forName(businessLogic);
    				BaseBL basebl = (BaseBL) aclass.newInstance();
    				Map  buslogHm = new HashMap ();

    				Map map = request.getParameterMap();
    				Iterator iter = map.entrySet().iterator();
    				while (iter.hasNext()) {
    					Entry n = (Entry)iter.next();
    					String key = n.getKey().toString();
    					String values[] = (String[]) n.getValue();
    					buslogHm.put(key,values);
    				} 

    				retBLhm = basebl.processRequest(buslogHm);
    			}

    			ArrayList<String> nextaction = scrfl.getNextActions("loginflow", pageaction, null);
    			ScrFlowNode scrflow = scrfl.populateScrFlowNode(flowName, pageaction);

    			if(retBLhm.get("error")!= null){
    				url = "/pages/login.jsp?errormsg="+(String) retBLhm.get("error");
    			}else{
    				
    				String userid= request.getParameter("userid");
    		    	//String password= request.getParameter("password");
    		    	    				
    				usrDTO.setRoleid(lin.getUserRole(userid));
    				usrDTO.setUsername(lin.getUserName(userid));
    				usrDTO.setUserid(userid);

    				debug("roleid:"+usrDTO.getRoleid());
    				debug("UserId:"+usrDTO.getUserid());
    				debug("User Name:"+usrDTO.getUsername());

    				session.setAttribute("userSessionData", usrDTO);
    				GenerateMenu gen = new GenerateMenu();
    				StringBuffer buf = gen.retrieveMenu(request.getContextPath(),usrDTO.getRoleid());
    				System.out.println("roleeeeee "+usrDTO.getRoleid());
    				String menu = buf.toString();
    				session.setAttribute("menu", menu);
    				url = scrflow.getDescription();	
    			}
    		/*}else{
    			ArrayList<String> nextaction = scrfl.getNextActions("loginflow", pageaction, null);
    			ScrFlowNode scrflow = scrfl.populateScrFlowNode(flowName, pageaction);
    			url = scrflow.getDescription();	
    		}*/

    	} catch (ClassNotFoundException e) {
    		debug(this.getServletName()+" "+e.toString());
    		e.printStackTrace();
    	} catch (InstantiationException e) {
    		debug(this.getServletName()+" "+e.toString());
    		e.printStackTrace();
    	} catch (IllegalAccessException e) {
    		debug(this.getServletName()+" "+e.toString());
    		e.printStackTrace();
    	}

    	url = request.getContextPath() + "/"+ url;
    	response.sendRedirect(url) ;
    }

	 
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request,response);
	}

}
