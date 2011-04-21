package org.jbpm.samarjit;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.drools.definition.process.Process;
import org.drools.runtime.process.EventListener;
import org.drools.runtime.process.WorkflowProcessInstance;
 

import org.jbpm.process.instance.InternalProcessRuntime;
import org.jbpm.workflow.core.Node;
import org.jbpm.workflow.core.node.StartNode;
import org.jbpm.workflow.instance.NodeInstance;
import org.jbpm.workflow.instance.impl.NodeInstanceFactoryRegistry;


public class StatelessProcessInstance  implements StatelessWorkflowEvent,WorkflowProcessInstance, org.drools.runtime.process.ProcessInstance {
	private Process currentProcess;
	private Map<String, List<EventListener>> eventListeners = new HashMap<String, List<EventListener>>();
	private Map<String, List<EventListener>> externalEventListeners = new HashMap<String, List<EventListener>>();


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
		return (StatelessProcessInstance) getProcess();
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

	@Override
	public Collection<org.drools.runtime.process.NodeInstance> getNodeInstances() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public org.drools.runtime.process.NodeInstance getNodeInstance(long paramLong) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getVariable(String paramString) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setVariable(String paramString, Object paramObject) {
		// TODO Auto-generated method stub
		
	}

	//org.jbpm.workflow.instance.impl.WorkflowProcessInstanceImpl
	@Override
	public void addEventListener(String type, EventListener listener, boolean external) {
		Map<String, List<EventListener>> eventListeners = 
			external ? this.externalEventListeners : this.eventListeners;
		List<EventListener> listeners = eventListeners.get(type);
		if (listeners == null) {
			listeners = new CopyOnWriteArrayList<EventListener>();
			eventListeners.put(type, listeners);
			if (external) { throw new UnsupportedOperationException("External Events ");
//				((InternalProcessRuntime) getKnowledgeRuntime().getProcessRuntime())
//					.getSignalManager().addEventListener(type, this);
			}
		}
		listeners.add(listener);
	}

	@Override
	public void removeEventListener(String type, EventListener listener, boolean external) {
		Map<String, List<EventListener>> eventListeners = external ? this.externalEventListeners
				: this.eventListeners;
		List<EventListener> listeners = eventListeners.get(type);
		if (listeners != null) {
			listeners.remove(listener);
			if (listeners.isEmpty()) {
				eventListeners.remove(type);
				if (external) {throw new UnsupportedOperationException("External Events ");
//					((InternalProcessRuntime) getKnowledgeRuntime().getProcessRuntime())
//						.getSignalManager().removeEventListener(type, this);
				}
			}
		}
	}
	
	
}
