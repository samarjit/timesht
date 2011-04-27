package org.jbpm.samarjit;

import java.util.Map;

import org.drools.SessionConfiguration;
import org.drools.event.ProcessEventSupport;
import org.drools.event.process.ProcessEventListener;
import org.drools.runtime.process.WorkItemHandler;
import org.drools.time.impl.JDKTimerService;
import org.jbpm.process.instance.ProcessInstanceManager;
import org.jbpm.process.instance.impl.DefaultProcessInstanceManager;
import org.jbpm.process.instance.timer.TimerManager;
import org.drools.definition.process.Process;


public class StatelessRuntime {

	public static StatelessRuntime eINSTANCE = new StatelessRuntime();
	private StatelessSignalManager signalManager; 
	private TimerManager timerManager = new TimerManager(null/*kruntime*/, new JDKTimerService());
	private StatelessProcessInstance processInstance = null;
	
	
	private ProcessEventSupport eventSupport;
	private StatelessWorkItemManager workItemManager = new StatelessWorkItemManager();
	private ProcessInstanceManager processInstanceManager  = null; // we are stateless
	
	private StatelessRuntime(){
		eventSupport = new ProcessEventSupport();
	}

	public void setProcessInstanceManager(ProcessInstanceManager processInstanceManager) {
		System.out.println("We are stateless this should not come!!");
		this.processInstanceManager = processInstanceManager;
	}

	public ProcessEventSupport getEventSupport() {
		return eventSupport;
	}

	public void setEventSupport(ProcessEventSupport eventSupport) {
		this.eventSupport = eventSupport;
	}
	public void addEventListener(ProcessEventListener l){
		eventSupport.addEventListener(l);
	}
	public void removeEventListener(ProcessEventListener l){
		eventSupport.removeEventListener(l);
	}
	public TimerManager getTimerManager() {
		return  timerManager;
	}

	public void setNodeInstance(StatelessNodeInstanceImpl statelessNodeInstanceImpl) {
		 //Part of Runtime ProcessContext which has Runtime
	}

	public StatelessWorkItemManager getWorkItemManager(){
//		if ( workItemManager == null ) {
//            workItemManager = ((SessionConfiguration) sessionConfiguration).getWorkItemManagerFactory().createWorkItemManager(this);
//            Map<String, WorkItemHandler> workItemHandlers = ((SessionConfiguration) sessionConfiguration).getWorkItemHandlers();
//            if (workItemHandlers != null) {
//                for (Map.Entry<String, WorkItemHandler> entry: workItemHandlers.entrySet()) {
//                    workItemManager.registerWorkItemHandler(entry.getKey(), entry.getValue());
//                }
//            }
//        }
        return      workItemManager;
//		return this.workItemMananger ;
	}

	public void setSignalManager(StatelessSignalManager signalManager) {
		this.signalManager = signalManager;
	}

	public StatelessSignalManager getSignalManager() {
		return signalManager;
	}

	public ProcessInstanceManager getProcessInstanceManager() {
		return (DefaultProcessInstanceManager )processInstanceManager ;
	}
	//use factory
	@Deprecated 
	public void createProcessInstance(Process process){
	//I think registry is required;
		processInstance = new StatelessProcessInstance(process);
	}
	
	public void startProcess(Process process){
		processInstance = new StatelessProcessInstance(process); //later on make it static to have only one instance
		processInstance.start();
	}
}
