package workflow;


import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.opensymphony.workflow.InvalidActionException;
import com.opensymphony.workflow.InvalidEntryStateException;
import com.opensymphony.workflow.InvalidInputException;
import com.opensymphony.workflow.InvalidRoleException;
import com.opensymphony.workflow.Workflow;
import com.opensymphony.workflow.WorkflowException;
import com.opensymphony.workflow.basic.BasicWorkflow;
import com.opensymphony.workflow.loader.WorkflowDescriptor;

import dto.ApplicationDTO;
import dto.UserDTO;

/**
 * Servlet implementation class WorkflowController.
 * @Author: Samarjit
 * @version: 1.0
 */
public class WorkflowController extends HttpServlet {
 
	private static final long serialVersionUID = -6869373705019616991L;

	/**
     * @see HttpServlet#HttpServlet()
     */
    public WorkflowController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String url ="";
		if(request.getParameter("create")!=null){
			System.out.println("creating new application");
			HttpSession session = request.getSession(false);
			//if( session.getAttribute("workflowSessName") == null || "".equals(session.getAttribute("workflowSessName")))
			UserDTO usrDTO = (UserDTO) session.getAttribute("userSessionData");	
			String activityname = request.getParameter("activityname");
			System.out.println("activityname:"+activityname);
			WorkflowBean wflBean = new WorkflowBean();
			long wflid = 0;
			if(activityname!=null && !"".equals(activityname)){
				ApplicationDTO appdto = new ApplicationDTO();
				
				String appid = wflBean.getNewApplicationId();
				String WflName = wflBean.getSuitableWorkflowName(activityname);
				wflid = wflBean.workflowInit(appid,WflName,null);
				
				//wflBean.createApplicationStatus(appid,activityname);
				
				HashMap hmActions= new HashMap();
				
				Workflow wf = new BasicWorkflow(appid);
				int[] actions = wf.getAvailableActions(wflid, null);
			    WorkflowDescriptor wd =  wf.getWorkflowDescriptor(wf.getWorkflowName(wflid));
			    for (int i = 0; i < actions.length; i++) {
			        String name = wd.getAction(actions[i]).getName();
			        System.out.print(actions[i]+" "+name +" \n");
			        hmActions.put( name,actions[i]);
			        //<a href="wflview.jsp?id=<%=id%>&do=<%= actions[i] %>"><%= name %></a>
			       
			        //get the first screen to display
			        if("".equals(url)){
			        	url = wflBean.getScreenId(name);
			        	appdto.setCurrentActionId(actions[i]);
			        	appdto.setCurrentAction(name);
			        }
			    }
				
			    wflBean.createApplicationWfl(usrDTO.getUserid(),wflid,appid,"S", hmActions);//'S' for started
			    
				appdto.setAppid(String.valueOf(appid));
				appdto.setCurrStage(activityname);
				appdto.setWorkflowName(WflName);
				appdto.setWflid(wflid);
				appdto.setWflactions(hmActions);
				session.setAttribute("applicationDTO",appdto);
				 
			}
			 //setting this for master workflowview will change it later
				session.setAttribute("username", usrDTO.getUserid());
		}else if(request.getParameter("action") != null){
			HashMap hmActions= new HashMap();
			String appid = request.getParameter("appid"); //appid
			String wflid = request.getParameter("wflid");
			String doString = request.getParameter("doString");
			System.out.println(appid+" "+wflid+" "+doString);
			WorkflowBean wflBean = new WorkflowBean();
			
			HttpSession session = request.getSession(false);
			ApplicationDTO appdto = (ApplicationDTO)session.getAttribute("applicationDTO");
			UserDTO usrDTO = (UserDTO)session.getAttribute("userSessionData");
			
			if(appdto == null)appdto = new ApplicationDTO();
			if(appid != null && appid.length() > 0){
				appdto.setAppid(appid);
			}
			if(wflid != null && wflid.length() > 0){
				appdto.setWflid(Long.parseLong(wflid));
			}
			
			String wflSession = appdto.getAppid();
			    Workflow wf = new BasicWorkflow(wflSession);
			    //id is wflId
			    long id = 0;
			    id = appdto.getWflid();
			      
			   
			    if (doString != null && !doString.equals("")) {
			        int action = Integer.parseInt(doString);
			        try {
						wf.doAction(id, action, Collections.EMPTY_MAP);
						wflBean.changeStageApplicationWfl(usrDTO.getUserid(),id,wflSession/*appid*/,"C", action);//'S' for started  
						
					} catch (InvalidInputException e) {
						e.printStackTrace();
					} catch (WorkflowException e) {
						e.printStackTrace();
					}
			    }
			    System.out.print("WflSession:"+wflSession+"  wflId:"+Long.toString(id)+ " do:"+doString+"  ");
			    
			    int[] actions = wf.getAvailableActions(id, null);
			    WorkflowDescriptor wd =  wf.getWorkflowDescriptor(wf.getWorkflowName(id));
			    for (int i = 0; i < actions.length; i++) {
			        String name = wd.getAction(actions[i]).getName();
			        System.out.print(actions[i]+" "+name +" \n");
			        hmActions.put( name,actions[i]);
			        //<a href="wflview.jsp?id=<%=id%>&do=<%= actions[i] %>"><%= name %></a>
			        
			        if("".equals(url)){
			        	url = wflBean.getScreenId(name);
			        	appdto.setCurrentActionId(actions[i]);
			        	appdto.setCurrentAction(name);
			        }
			    }
			    if(actions.length == 0){
			    	appdto.setCurrentActionId(-1);
		        	appdto.setCurrentAction("");
			    }
			    wflBean.updateApplicationWfl(usrDTO.getUserid(), id,wflSession/*appid*/,"S", hmActions);//'S' for started
			    
			    
			    appdto.setWflactions(hmActions);
			    session.setAttribute("applicationDTO",appdto);
			    //wflsubmit by chance if it gets called
			    session.setAttribute("wflId",id);
			    session.setAttribute("workflowSessName",wflSession);
			    request.setAttribute("hmActions", hmActions);
		}else if(request.getParameter("navigateto") != null){
			WorkflowBean wflBean = new WorkflowBean();
			HttpSession session = request.getSession(false);
			String pageName = request.getParameter("navigateto");
			
			ApplicationDTO appdto = (ApplicationDTO)session.getAttribute("applicationDTO");
		    HashMap<String,Integer> hmactions = appdto.getWflactions();
			
			System.out.print("navigate-WflSession:"+appdto.getAppid()+"  wflId:"+appdto.getWflid()+ " topage:"+pageName);
			    
			        	url = wflBean.getScreenId(pageName);
			        	appdto.setCurrentAction(pageName);
			        	appdto.setCurrentActionId( hmactions.get(pageName));
			        	session.setAttribute("applicationDTO",appdto);
		}
		System.out.println("url:"+url); 
		if("".equals(url))url =response.encodeURL("/wflsubmit.jsp"); 
		///getServletConfig().getServletContext().getRequestDispatcher(url);
		 
		url = request.getContextPath() + "/"+ url;
		response.sendRedirect(url) ;
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request,response);
	}

}
