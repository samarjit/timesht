package workflow;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import pojo.CreateMailFromTemplate;

import bsh.EvalError;
import bsh.Interpreter;

import com.opensymphony.workflow.InvalidActionException;
import com.opensymphony.workflow.InvalidEntryStateException;
import com.opensymphony.workflow.InvalidInputException;
import com.opensymphony.workflow.InvalidRoleException;
import com.opensymphony.workflow.Workflow;
import com.opensymphony.workflow.WorkflowException;
import com.opensymphony.workflow.basic.BasicWorkflow;
import com.opensymphony.workflow.config.DefaultConfiguration;
import com.opensymphony.workflow.loader.StepDescriptor;
import com.opensymphony.workflow.loader.WorkflowDescriptor;
import com.opensymphony.workflow.spi.Step;

import dao.WorkflowDAO;



public class  WorkflowBean
{
	private void debug(int priority, String s){
		if(priority>-1){
			System.out.println("WorkflowAC:"+s);
		}
	}
	  Workflow workflow =  null;
	long workflowId = 0;
	public void printState(){
		WorkflowDescriptor wd =
			workflow.getWorkflowDescriptor(workflow.getWorkflowName(workflowId));
		
		List steps = workflow.getCurrentSteps(workflowId);
		System.out.println("Expected number of current steps: " + steps.size());
		System.out.print("current step   ");
		for (Iterator iterator = steps.iterator(); iterator.hasNext();)
		{
		Step step = (Step) iterator.next();
		StepDescriptor sd = wd.getStep(step.getStepId());
		System.out.print("#"  + sd.getId() +" step Name:"+ sd.getName()+" ");
		}
		int[] availableActions = workflow.getAvailableActions(workflowId,null);
		//verify we only have one available action
		
			for (int i = 0; i < availableActions.length; i++)
			{
			int name = wd.getAction(availableActions[i]).getId();
			System.out.print("  Action:"+name);
			}
			System.out.println();
		
	}
	
	public String getNewApplicationId(){
		WorkflowDAO wflDAO = new WorkflowDAO();
		return wflDAO.getNewAppId();
		
	}
	
	public String getSuitableWorkflowName(String activity){
		WorkflowDAO wflDAO = new WorkflowDAO();
		return wflDAO.getSuitableWorkflowName(activity);
	}
	/**
	 * @param wlfSessionNameAppId is appId
	 * @param wflName newwfl based on which workflow to choose
	 * @param propSet parameter to workflow
	 * @return workflow id
	 */
	public long workflowInit(String wlfSessionNameAppId, String wflName,HashMap propSet){
		Workflow wf = new BasicWorkflow(wlfSessionNameAppId);
		long id = 0;
		try {
			
			 id = wf.initialize(wflName,0, null);
		} catch (InvalidActionException e) {
			e.printStackTrace();
		} catch (InvalidRoleException e) {
			e.printStackTrace();
		} catch (InvalidInputException e) {
			e.printStackTrace();
		} catch (InvalidEntryStateException e) {
			e.printStackTrace();
		} catch (WorkflowException e) {
			e.printStackTrace();
		}
		return id;
	}
	public static void main(String[] args) throws InvalidInputException, WorkflowException 
	{
		new WorkflowBean().execueWorkflow();
/*HashMap inputs = new HashMap();
inputs.put("docTitle", "title");
long id = Long.parseLong("1");
wf.doAction(id, 1, inputs);*/
		System.out.println("Hello World!");
	}
	private void execueWorkflow() throws InvalidInputException, WorkflowException {
		 workflow = new BasicWorkflow("testuser");
			DefaultConfiguration config = new DefaultConfiguration();
			workflow.setConfiguration(config);
				//////////
			HashMap inputs = new HashMap();
			inputs.put("docTitle", "some_title");
		 
			//////////// 
			workflowId = workflow.initialize("newwfl", 0, null);
			printState();
			
			workflow.doAction(workflowId, 6, null);
			printState();  
			
		
			workflow.doAction(workflowId,35,null );
			printState();

			workflow.doAction(workflowId, 63, null);
			printState();	
			 
			
				
			workflow.doAction(workflowId, 59, null);
			
			printState(); 
				
			workflow.doAction(workflowId, 63, null);
			printState();	 
		
	}

	public void createApplicationWfl(String userid, long id, String appid,String status, HashMap hmActions) {
		WorkflowDAO wflDAO = new WorkflowDAO();
		wflDAO.createApplicationWfl( userid,  id, appid, status,hmActions);
		
	}

	public String getScreenId(String activityname) {
		WorkflowDAO wflDAO = new WorkflowDAO();
		return wflDAO.getScreenId( activityname);
	}

	public void changeStageApplicationWfl(String userName, long id, String wflSession,
			String status, int action) {
		WorkflowDAO wflDAO = new WorkflowDAO();
		 wflDAO.changeStageApplicationWfl(  userName,   id,   wflSession,
				  status,   action);
	}

	public void updateApplicationWfl(String userid, long id, String wflSession,
			String status, HashMap hmActions) {
		WorkflowDAO wflDAO = new WorkflowDAO();
		 wflDAO.updateApplicationWfl(  userid, id,   wflSession,
				  status,   hmActions);
		
	}
	public HashMap<String, Integer> getAvailableActions(String appid, long wflid){
		 HashMap<String, Integer> hmActions = new HashMap<String, Integer>();
		Workflow wf = new BasicWorkflow(appid);
		int[] actions = wf.getAvailableActions(wflid, null);
	    WorkflowDescriptor wd =  wf.getWorkflowDescriptor(wf.getWorkflowName(wflid));
	    for (int i = 0; i < actions.length; i++) {
	        String name = wd.getAction(actions[i]).getName();
	        System.out.print(actions[i]+" "+name +" \n");
	       
			hmActions.put( name,actions[i]);
	        //<a href="wflview.jsp?id=<%=id%>&do=<%= actions[i] %>"><%= name %></a>
	       
	       
	    }
	    return hmActions;
	}

	public void doAction(String wflSession, long id, int action  ) throws InvalidInputException, WorkflowException {
		Workflow wf = new BasicWorkflow(wflSession);
		  wf.doAction(id, action, Collections.EMPTY_MAP);
		
	}

	public ArrayList<String> getNextScrFlowActions(String wflName, String currentAction, String decision) {
		ScreenFlow scrfl = new ScreenFlow();
		//System.out.println("wflName, currentAction" + wflName + currentAction);
		return scrfl.getNextActions(wflName, currentAction, decision);
		 
	}

	public void createApplicationScrWfl(String userid, String wflName,String appid, String status, ArrayList<String> hmActions) {
		WorkflowDAO wflDAO = new WorkflowDAO();
		wflDAO.createApplicationScrWfl(  userid,   wflName,  appid,   status,   hmActions);
	}

	public void changeStageApplicationScrWfl(String userid, String wflid,String appid, String status, String doString) {
		WorkflowDAO wflDAO = new WorkflowDAO();
		wflDAO.changeStageApplicationScrWfl(  userid,   wflid,  appid,   status,   doString);
		ScreenFlow scrfl = new ScreenFlow();
		ScrFlowNode sfn = scrfl.populateScrFlowNode(wflid,doString);
		 String script = sfn.getEventscript();
		 Interpreter i = new Interpreter(); 
			try {
				if(script!=null) {
					System.out.println("=========Executing BSH script============:\n"+script+" :::");
					i.eval(script);
				}
				if(i.get("propertySet")!=null){
					scrfl.setPropertyset((HashMap)i.get("propertySet"));
				}
				debug(0,"Eval="+ i.get("propertySet") );
			} catch (EvalError e) {
				System.out.print("Exception in Script evaluation:"+e.getMessage()+" "+e.getErrorLineNumber());
				 e.printStackTrace();
			}
		 
		
	}
	
	public void updateApplicationScrWfl(String userid, String wflid,String appid, String status, ArrayList<String> hmActions) {
		WorkflowDAO wflDAO = new WorkflowDAO();
		wflDAO.updateApplicationScrWfl(  userid,   wflid,  appid,   status,   hmActions);
	}

	public ScrFlowNode populateScreenflowNode(String wflid, String doString) {
		ScreenFlow scrfl = new ScreenFlow();
		ScrFlowNode sfn = scrfl.populateScrFlowNode(wflid,doString);
		return sfn;
	}
}