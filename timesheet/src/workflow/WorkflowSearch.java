package workflow;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.opensymphony.workflow.Workflow;
import com.opensymphony.workflow.basic.BasicWorkflow;
import com.opensymphony.workflow.loader.WorkflowDescriptor;

import dao.WorkflowDAO;
import dto.UserDTO;

/**
 * Servlet implementation class WorkflowSearch
 */
public class WorkflowSearch extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7175855967006838476L;

	private void debug(String s){
		System.out.println(s);
	}
	 
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public WorkflowSearch() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 PrintWriter out = response.getWriter();
		 HttpSession session = request.getSession(true);
		String appid = request.getParameter("appId");		
		 		ArrayList<String> arwflId= null;
				String htmlStr = "";
				String temphtmlStr = "";
				UserDTO usr = (UserDTO) session.getAttribute("userSessionData");
				
				WorkflowDAO wflDAO = new WorkflowDAO();
				
				System.out.println("Searching workflow!");
				HashMap<String, ArrayList<String>> hmwflsess = wflDAO.getAvailableAction(usr.getUserid(),appid);
				
				
				Iterator<String> itr = hmwflsess.keySet().iterator();
				while(itr.hasNext()){
					String strwflsess = (String) itr.next(); //wfl session = appid
					temphtmlStr="<br>"+strwflsess;
					arwflId = hmwflsess.get(strwflsess);
					Workflow wf = new BasicWorkflow(strwflsess);
					Iterator wflIdItr = arwflId.iterator();
					try {
						while (wflIdItr.hasNext()) {
							String wflId = (String) wflIdItr.next(); //wfl id
							temphtmlStr += "|" + wflId+"|";
							if (wflId != null) {
								long id = Long.parseLong(wflId);
								int[] actions = wf.getAvailableActions(id, null); //actions
								WorkflowDescriptor wd = wf.getWorkflowDescriptor(wf.getWorkflowName(id));
								for (int s : actions) {
									String name = wd.getAction(s).getName();
									htmlStr +=temphtmlStr+ String.valueOf(s) + "," + name;
								}
							}

						}
					} catch (Exception e) {
						debug("workflowSearch:"+e.getMessage());
					}
				}
				System.out.println(htmlStr);
				if(htmlStr==null || htmlStr.length() < 1)htmlStr="No data found";
				out.print(htmlStr);
			 
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request,response);
	}

}
