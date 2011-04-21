package org.jbpm.samarjit;

import java.util.Collection;
import java.util.Map;

import org.drools.event.ProcessEventSupport;
import org.drools.runtime.KnowledgeRuntime;
import org.drools.runtime.process.ProcessInstance;
import org.drools.runtime.process.ProcessRuntime;
import org.drools.runtime.process.WorkItemManager;

public class StatelessRuntime {

	public static StatelessRuntime eINSTANCE = new StatelessRuntime();
	
	private StatelessRuntime(){}
	private ProcessEventSupport eventSupport;

	public ProcessEventSupport getEventSupport() {
		return eventSupport;
	}

	public void setEventSupport(ProcessEventSupport eventSupport) {
		this.eventSupport = eventSupport;
	}
	
}
