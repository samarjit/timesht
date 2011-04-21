package org.jbpm.samarjit;

import org.drools.event.ProcessEventSupport;
import org.drools.time.impl.JDKTimerService;
import org.jbpm.process.instance.timer.TimerManager;

public class StatelessRuntime {

	public static StatelessRuntime eINSTANCE = new StatelessRuntime();
	private StatelessSignalManager signalManager; 
	private TimerManager timerManager = new TimerManager(null/*kruntime*/, new JDKTimerService());
	
	private StatelessRuntime(){}
	private ProcessEventSupport eventSupport;

	public ProcessEventSupport getEventSupport() {
		return eventSupport;
	}

	public void setEventSupport(ProcessEventSupport eventSupport) {
		this.eventSupport = eventSupport;
	}

	public TimerManager getTimerManager() {
		return  timerManager;
	}

	public void setNodeInstance(StatelessNodeInstanceImpl statelessNodeInstanceImpl) {
		 //Part of Runtime ProcessContext which has Runtime
	}

	 
	
}
