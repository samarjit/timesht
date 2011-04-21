package org.jbpm.samarjit;

import org.drools.definition.process.Process;

public interface WorkFlowProcessInstance {
	  public static final int STATE_PENDING = 0;
	  public static final int STATE_ACTIVE = 1;
	  public static final int STATE_COMPLETED = 2;
	  public static final int STATE_ABORTED = 3;
	  public static final int STATE_SUSPENDED = 4;

	  public abstract String getProcessId();

	  public abstract Process getProcess();

	  public abstract long getId();

	  public abstract String getProcessName();

	  public abstract int getState();
	  
}
