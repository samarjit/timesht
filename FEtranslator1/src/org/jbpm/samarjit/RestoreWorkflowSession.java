package org.jbpm.samarjit;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.drools.definition.process.Process;
import org.jbpm.samarjit.dao.DBActivitiMapper;
import org.jbpm.samarjit.dao.MybatisSessionHelper;
import org.jbpm.samarjit.dto.ActRuExecution;
import org.jbpm.samarjit.dto.ActRuTask;
import org.jbpm.workflow.core.impl.WorkflowProcessImpl;

public class RestoreWorkflowSession {
	private static Logger logger = Logger.getLogger(RestoreWorkflowSession.class);
	public void restoreWorkflowSession(List<Process> processes){
		logger.debug(" workflow restoring from session, started..");
		SqlSession sqlSession = MybatisSessionHelper.eINSTANCE.openSession();
		DBActivitiMapper dbActivitiMapper = sqlSession.getMapper(DBActivitiMapper.class);
		List<ActRuExecution> runningActivities = dbActivitiMapper.selectRunningActivities();
		logger.debug(runningActivities.get(0).getActRuTasksForProcInstId().get(0).getName());
		for (ActRuExecution actRuExecution : runningActivities) {
			String processId = actRuExecution.getProcDefId();
			for (Process process : processes) {
				WorkflowProcessImpl wp = (WorkflowProcessImpl)process;
				if(wp.getId().equals(processId)){
					StatelessProcessInstance processInstance = new StatelessProcessInstance(process);
					//copy process instance data from entity to real runnable class
					processInstance.setId(Long.parseLong(actRuExecution.getActId()));
					
					//
					StatelessRuntime.eINSTANCE.getProcessInstanceManager().addProcessInstance(processInstance);
					
					List<ActRuTask> dbNodeInstance = actRuExecution.getActRuTasksForProcInstId();
					for (ActRuTask actRuTask : dbNodeInstance) {
						//copy entity data to runnable nodeInstance
						 
						//
						try {
							StatelessNodeInstanceImpl nodeInstance = null;
							Class<?> statelessNodeInstClazz = Class.forName(actRuTask.getClassName());
							nodeInstance = (StatelessNodeInstanceImpl) statelessNodeInstClazz.newInstance();
							nodeInstance.setNodeId(Long.parseLong(actRuTask.getTaskDefKey()));
							nodeInstance.setNodeInstanceContainer(processInstance);
							nodeInstance.setProcessInstance(processInstance);
							nodeInstance.setId(Long.parseLong(actRuTask.getId()));

							processInstance.addNodeInstance(nodeInstance );
							
							logger.debug("New reloaded activity="+nodeInstance);
						} catch (InstantiationException e) {
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						} catch (ClassNotFoundException e) {
							e.printStackTrace();
						}
					} 
				}
			}
		}
		
		throw new RuntimeException("Not implemented yet");
	}
}
