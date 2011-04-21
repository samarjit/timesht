package org.jbpm.samarjit;

import org.drools.definition.process.Process;
 

import org.jbpm.workflow.core.Node;
import org.jbpm.workflow.core.node.StartNode;
import org.jbpm.workflow.instance.NodeInstance;
import org.jbpm.workflow.instance.impl.NodeInstanceFactoryRegistry;


public class StatelessProcessInstance  implements WorkflowProcessInstance, org.drools.runtime.process.ProcessInstance {
	private Process currentProcess;



	public StatelessProcessInstance(Process p){
		currentProcess = p;
	}
	
	public void internalStart() {
    	StartNode startNode = getRuleFlowProcess().getStart();
    	if (startNode != null) {
    		((NodeInstance) getNodeInstance(startNode)).trigger(null, null);
    	}
    }
	

	
	private StatelessProcessInstance getRuleFlowProcess() {
		return getProcess();
	}



	public StartNode getStart() {
        Node[] nodes = getNodes();
        for (int i = 0; i < nodes.length; i++) {
            if (nodes[i] instanceof StartNode) {
                return (StartNode) nodes[i];
            }
        }
        return null;
    }
	
	private Node[] getNodes() {
		// TODO Auto-generated method stub
		return null;
	}



	public NodeInstance getNodeInstance(final Node node) {
		StatelessNodeInstanceFactory conf = StatelessNodeInstanceFactoryRegistry.INSTANCE.getProcessNodeInstanceFactory(node);
		if (conf == null) {
			throw new IllegalArgumentException("Illegal node type: "
					+ node.getClass());
		}
		StatelessNodeInstanceImpl nodeInstance = (StatelessNodeInstanceImpl) conf
				.getNodeInstance(node, this, this);
		if (nodeInstance == null) {
			throw new IllegalArgumentException("Illegal node type: "
					+ node.getClass());
		}
		 
		return nodeInstance;
	}



	@Override
	public void signalEvent(String paramString, Object paramObject) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public String[] getEventTypes() {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public String getProcessId() {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public Process getProcess() {
		return currentProcess;
	}



	@Override
	public long getId() {
		// TODO Auto-generated method stub
		return 0;
	}



	@Override
	public String getProcessName() {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public int getState() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	
}
