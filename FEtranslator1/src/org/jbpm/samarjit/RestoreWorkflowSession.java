package org.jbpm.samarjit;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.drools.definition.process.Process;
import org.drools.runtime.process.ProcessInstance;
import org.jbpm.samarjit.dao.DBActivitiMapper;
import org.jbpm.samarjit.dao.MybatisSessionHelper;
import org.jbpm.samarjit.dto.ActRuExecution;
import org.jbpm.samarjit.dto.ActRuTask;
import org.jbpm.workflow.core.impl.WorkflowProcessImpl;
import org.jbpm.workflow.instance.NodeInstance;

public class RestoreWorkflowSession {
	private static Logger logger = Logger.getLogger(RestoreWorkflowSession.class);
	public void restoreWorkflowSession(List<Process> processes){
		logger.debug(" workflow restoring from session, started..");
		SqlSession sqlSession = MybatisSessionHelper.eINSTANCE.openSession();
		DBActivitiMapper dbActivitiMapper = sqlSession.getMapper(DBActivitiMapper.class);
		List<ActRuExecution> runningActivities = dbActivitiMapper.selectRunningWorkflows();
//		logger.debug(runningActivities.get(0).getActRuTasksForProcInstId().get(0).getName());
		for (ActRuExecution actRuExecution : runningActivities) {
			String processId = actRuExecution.getProcDefId();
			for (Process process : processes) {
				WorkflowProcessImpl wp = (WorkflowProcessImpl)process;
				if(wp.getId().equals(processId)){
					StatelessProcessInstance processInstance = new StatelessProcessInstance(process);
					//copy process instance data from entity to real runnable class
					processInstance.setId(Long.parseLong(actRuExecution.getId()));
					if(actRuExecution.getIsActive() == true)
						processInstance.setState(ProcessInstance.STATE_ACTIVE); 
					else
						processInstance.setState(ProcessInstance.STATE_ABORTED);
					
					//TODO testing after removing process instance
//					StatelessRuntime.eINSTANCE.getProcessInstanceManager().clearProcessInstances();
					((StatelessProcessInstanceManager)StatelessRuntime.eINSTANCE.getProcessInstanceManager()).addProcessInstanceWithOldId(processInstance);
					
					List<ActRuTask> dbNodeInstance = actRuExecution.getActRuTasksForProcInstId();
					
					for (ActRuTask actRuTask : dbNodeInstance) {
						//copy entity data to runnable nodeInstance
						
						//this will make sure the node instance ID keeps on increasing, last id should be highest ID
						processInstance.setNodeInstanceCounter(processInstance.getId()+1); 
						//
						try {
							StatelessNodeInstanceImpl nodeInstance = null;
							Class<?> statelessNodeInstClazz = Class.forName(actRuTask.getClassName());
							nodeInstance = (StatelessNodeInstanceImpl) statelessNodeInstClazz.newInstance();
							nodeInstance.setNodeId(Long.parseLong(actRuTask.getTaskDefKey()));
							nodeInstance.setNodeInstanceContainer(processInstance); //adds NodeInstance hence increment Id
							nodeInstance.setProcessInstance(processInstance);
							nodeInstance.setId(Long.parseLong(actRuTask.getId()));

//							processInstance.addNodeInstance(nodeInstance );
							
							logger.debug("New reloaded activity="+nodeInstance);
							
						} catch (InstantiationException e) {
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						} catch (ClassNotFoundException e) {
							e.printStackTrace();
						}
					} 
					restartWorkflows();
					
				}
			}
		}
//		logger.debug(StatelessRuntime.eINSTANCE.getProcessInstanceManager().getProcessInstances());
//		throw new RuntimeException("Not implemented yet");
	}
	
	/**
	 * Restart the instances. 
	 * 1. If the workflow was stopped in between auto executable tasks
	 * 2. If the workflow was stopped after proper exiting of autoexecutable tasks. Then manual tasks needs to be loaded properly.
	 * 3. TODO restore jobs
	 * 4. TODO restore signals
	 * 5. TODO restore event listeners
	 */
	public void restartWorkflows(){
		Collection<ProcessInstance> processInstances = StatelessRuntime.eINSTANCE.getProcessInstanceManager().getProcessInstances();
		for (ProcessInstance processInstance : processInstances) {
			StatelessProcessInstance statelessProcInst = (StatelessProcessInstance) processInstance;
			
			Collection<NodeInstance> nodeInstances = statelessProcInst.getNodeInstances(false);
			List<StatelessNodeInstanceImpl> sortedList = new ArrayList<StatelessNodeInstanceImpl>(); 
			for (NodeInstance nodeInstance : nodeInstances) {
				StatelessNodeInstance statelessNodeInst = (StatelessNodeInstance) nodeInstance;
				
				//this will make sure the node instance ID keeps on increasing, last id should be highest ID
				statelessProcInst.setNodeInstanceCounter(statelessNodeInst.getId()+1);
				if(statelessNodeInst instanceof  StatelessWorkItemNodeInstance){
					//make the work item node active
					
					StatelessWorkItemNodeInstance workItemNodeInst =   (StatelessWorkItemNodeInstance)statelessNodeInst;
					//This restarts the workitem instances 
					workItemNodeInst.internalTrigger(null, null);
//					workItemNodeInst.restartWorkItemInst();
//					  WorkItem workItem = (org.drools.process.instance.WorkItem) workItemNodeInst.getWorkItem();
//					  StatelessRuntime.eINSTANCE.getWorkItemManager().internalAddWorkItem( workItem);
					
//					WorkItemNode workItemNode = workItemNodeInst.getWorkItemNode();
//					workItemNodeInst.createWorkItem(workItemNode);
//						if (workItemNode.isWaitForCompletion()) {
//							workItemNodeInst.getProcessInstance().addEventListener("workItemCompleted", workItemNodeInst, false);
//							workItemNodeInst.getProcessInstance().addEventListener("workItemAborted", workItemNodeInst, false);
//				        } execute()
						
				}else{
					sortedList.add((StatelessNodeInstanceImpl) nodeInstance);
				}
 
			}
		 
			Collections.sort(sortedList);
			logger.debug("Sorted restartworkflows"+sortedList);
			if(!sortedList.isEmpty()) 
				sortedList.get(0)
					.triggerCompleted(org.jbpm.workflow.core.Node.CONNECTION_DEFAULT_TYPE, true);
		}
	}
}
