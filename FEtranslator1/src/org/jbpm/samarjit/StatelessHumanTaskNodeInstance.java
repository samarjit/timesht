package org.jbpm.samarjit;
 
import org.drools.runtime.process.WorkItem;
import org.jbpm.process.core.context.swimlane.SwimlaneContext;
import org.jbpm.process.instance.context.swimlane.SwimlaneContextInstance;
import org.jbpm.workflow.core.node.HumanTaskNode;
import org.jbpm.workflow.core.node.WorkItemNode;

 

public class StatelessHumanTaskNodeInstance extends StatelessNodeInstanceImpl {
	 
	private transient SwimlaneContextInstance swimlaneContextInstance;
	
	protected WorkItem createWorkItem(WorkItemNode workItemNode) {
	        WorkItem workItem = super.createWorkItem(workItemNode);
	        String actorId = assignWorkItem(workItem);
	        if (actorId != null) {
	            ((org.drools.process.instance.WorkItem) workItem).setParameter("ActorId", actorId);
	        }
	        return workItem;
	    }
	 
	 protected String assignWorkItem(WorkItem workItem) {
	        String actorId = null;
	        // if this human task node is part of a swimlane, check whether an actor
	        // has already been assigned to this swimlane
	        String swimlaneName = getHumanTaskNode().getSwimlane();
	        SwimlaneContextInstance swimlaneContextInstance = getSwimlaneContextInstance(swimlaneName);
	        if (swimlaneContextInstance != null) {
	            actorId = swimlaneContextInstance.getActorId(swimlaneName);
	        }
	        // if no actor can be assigned based on the swimlane, check whether an
	        // actor is specified for this human task
	        if (actorId == null) {
	        	actorId = (String) workItem.getParameter("ActorId");
	        	if (actorId != null && swimlaneContextInstance != null) {
	        		swimlaneContextInstance.setActorId(swimlaneName, actorId);
	        	}
	        }
	        return actorId;
	    }
	 
	 public HumanTaskNode getHumanTaskNode() {
	        return (HumanTaskNode) getNode();
	    }
	 
	 private SwimlaneContextInstance getSwimlaneContextInstance(String swimlaneName) {
	        if (this.swimlaneContextInstance == null) {
	            if (swimlaneName == null) {
	                return null;
	            }
	            SwimlaneContextInstance swimlaneContextInstance =
	                (SwimlaneContextInstance) resolveContextInstance(
	                    SwimlaneContext.SWIMLANE_SCOPE, swimlaneName);
	            if (swimlaneContextInstance == null) {
	                throw new IllegalArgumentException(
	                    "Could not find swimlane context instance");
	            }
	            this.swimlaneContextInstance = swimlaneContextInstance;
	        }
	        return this.swimlaneContextInstance;
	    }
	 
}
