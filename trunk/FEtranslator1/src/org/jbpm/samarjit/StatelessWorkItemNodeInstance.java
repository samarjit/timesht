package org.jbpm.samarjit;

import org.drools.runtime.process.NodeInstance;

public class StatelessWorkItemNodeInstance extends StatelessNodeInstanceImpl{

	 public void internalTrigger(final StatelessNodeInstance from, String type) {
	    	super.internalTrigger(from, type);
	        // TODO this should be included for ruleflow only, not for BPEL
//	        if (!Node.CONNECTION_DEFAULT_TYPE.equals(type)) {
//	            throw new IllegalArgumentException(
//	                "A WorkItemNode only accepts default incoming connections!");
//	        }
	        WorkItemNode workItemNode = getWorkItemNode();
	        createWorkItem(workItemNode);
			if (workItemNode.isWaitForCompletion()) {
			    addWorkItemListener();
	        }
			if (isInversionOfControl()) {
				((ProcessInstance) getProcessInstance()).getKnowledgeRuntime()
					.update(((ProcessInstance) getProcessInstance()).getKnowledgeRuntime().getFactHandle(this), this);
			} else {
				try {
				    ((WorkItemManager) ((ProcessInstance) getProcessInstance())
			    		.getKnowledgeRuntime().getWorkItemManager()).internalExecuteWorkItem(
		    				(org.drools.process.instance.WorkItem) workItem);
			    } catch (WorkItemHandlerNotFoundException wihnfe){
			        getProcessInstance().setState( ProcessInstance.STATE_ABORTED );
			        throw wihnfe;
			    }
			}
	        if (!workItemNode.isWaitForCompletion()) {
	            triggerCompleted();
	        }
	    	this.workItemId = workItem.getId();
	    }
	 
	 //Extended Node instance trigger
		public void internalTrigger(NodeInstance from, String type) {
			super.internalTrigger(from, type);
			// activate timers
			Map<Timer, DroolsAction> timers = getEventBasedNode().getTimers();
			if (timers != null) {
				addTimerListener();
				timerInstances = new ArrayList<Long>(timers.size());
				TimerManager timerManager = ((InternalProcessRuntime) 
					getProcessInstance().getKnowledgeRuntime().getProcessRuntime()).getTimerManager();
				for (Timer timer: timers.keySet()) {
					TimerInstance timerInstance = createTimerInstance(timer); 
					timerManager.registerTimer(timerInstance, (ProcessInstance) getProcessInstance());
					timerInstances.add(timerInstance.getId());
				}
			}
		}

		@Override
		protected void internalTrigger(NodeInstance from, String type) {
			// TODO Auto-generated method stub
			
		}
		
}
