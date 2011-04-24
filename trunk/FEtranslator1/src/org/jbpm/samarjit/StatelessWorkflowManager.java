package org.jbpm.samarjit;

import org.jbpm.workflow.core.impl.WorkflowProcessImpl;

public class StatelessWorkflowManager {
	public void getNextTask(){
		
	}
	
	public void startProcess(WorkflowProcessImpl procc){
		StatelessRuntime.eINSTANCE.startProcess(procc);
	}
	
	public void signalEvent(){
		
	}
	
}
