package org.jbpm.samarjit;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.drools.common.InternalKnowledgeRuntime;
import org.drools.definition.process.NodeContainer;
import org.drools.definition.process.Process;
import org.drools.definition.process.WorkflowProcess;
import org.drools.runtime.process.EventListener;
import org.drools.runtime.process.WorkflowProcessInstance;
 

import org.jbpm.process.instance.InternalProcessRuntime;
import org.jbpm.process.instance.ProcessInstance;
import org.jbpm.process.instance.impl.ProcessInstanceImpl;
import org.jbpm.ruleflow.core.RuleFlowProcess;
import org.jbpm.workflow.core.Node;
import org.jbpm.workflow.core.impl.WorkflowProcessImpl;
import org.jbpm.workflow.core.node.StartNode;
import org.jbpm.workflow.instance.NodeInstance;
import org.jbpm.workflow.instance.NodeInstanceContainer;
import org.jbpm.workflow.instance.impl.NodeInstanceFactory;
import org.jbpm.workflow.instance.impl.NodeInstanceFactoryRegistry;
import org.jbpm.workflow.instance.impl.NodeInstanceImpl;
import org.jbpm.workflow.instance.node.EndNodeInstance;


public class StatelessProcessInstance  implements StatelessWorkflowEvent,WorkflowProcessInstance, org.drools.runtime.process.ProcessInstance, NodeInstanceContainer {
	private Process currentProcess;
	private Map<String, List<EventListener>> eventListeners = new HashMap<String, List<EventListener>>();
	private Map<String, List<EventListener>> externalEventListeners = new HashMap<String, List<EventListener>>();
	private int state = STATE_PENDING;
	private long nodeInstanceCounter = 0;
	private final List<NodeInstance> nodeInstances = new ArrayList<NodeInstance>();
	
	public StatelessProcessInstance(Process p){
		currentProcess = p;
	}
	
	public void internalStart() {
    	StartNode startNode = getRuleFlowProcess().getStart();
    	if (startNode != null) {
    		((NodeInstance) getNodeInstance(startNode))
    				.trigger(null, null);
    	}
    }
	
	public void start() {
    	synchronized (this) {
            if ( getState() != ProcessInstanceImpl.STATE_PENDING ) {
                throw new IllegalArgumentException( "A process instance can only be started once" );
            }
            setState( ProcessInstanceImpl.STATE_ACTIVE );
            internalStart();
		}
    }

	
	private RuleFlowProcess getRuleFlowProcess() {
		return (RuleFlowProcess) getProcess();
	}



	/*public StartNode getStart() {
        Node[] nodes = getNodes();
        for (int i = 0; i < nodes.length; i++) {
            if (nodes[i] instanceof StartNode) {
                return (StartNode) nodes[i];
            }
        }
        return null;
    }*/
	
	/*private Node[] getNodes() {
		// TODO Auto-generated method stub
		return null;
	}*/



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



	
	public void signalEvent(String paramString, Object paramObject) {
		// TODO Auto-generated method stub
		
	}



	
	public String[] getEventTypes() {
		// TODO Auto-generated method stub
		return null;
	}



	
	public String getProcessId() {
		// TODO Auto-generated method stub
		return null;
	}



	
	public Process getProcess() {
		return currentProcess;
	}



	
	public long getId() {
		// TODO Auto-generated method stub
		return 0;
	}



	
	public String getProcessName() {
		// TODO Auto-generated method stub
		return null;
	}



	
	public int getState() {
		 return this.state;
	}

	
	

	
	public org.drools.runtime.process.NodeInstance getNodeInstance(long nodeInstanceId) {
		for (NodeInstance nodeInstance: nodeInstances) {
			if (nodeInstance.getId() == nodeInstanceId) {
				return nodeInstance;
			}
		}
		return null;
	}

	
	public Object getVariable(String paramString) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public void setVariable(String paramString, Object paramObject) {
		// TODO Auto-generated method stub
		
	}

	//org.jbpm.workflow.instance.impl.WorkflowProcessInstanceImpl
	
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

	
	public void removeEventListener(String type, EventListener listener, boolean external) {
		Map<String, List<EventListener>> eventListeners = external ? this.externalEventListeners
				: this.eventListeners;
		List<EventListener> listeners = eventListeners.get(type);
		if (listeners != null) {
			listeners.remove(listener);
			if (listeners.isEmpty()) {
				eventListeners.remove(type);
				if (external) {
					StatelessRuntime.eINSTANCE.getSignalManager().removeEventListener(type, this);
				}
			}
		}
	}
	public void setState(int stateAborted) {
		 internalSetState(stateAborted);
		// TODO move most of this to ProcessInstanceImpl
		if (state == ProcessInstance.STATE_COMPLETED
				|| state == ProcessInstance.STATE_ABORTED) {
			StatelessRuntime.eINSTANCE.getEventSupport().fireBeforeProcessCompleted(this, null/*kruntime*/);
			// deactivate all node instances of this process instance
			while (!nodeInstances.isEmpty()) {
				NodeInstance nodeInstance = nodeInstances.get(0);
				((org.jbpm.workflow.instance.NodeInstance) nodeInstance)
						.cancel();
			}
			removeEventListeners();
			StatelessRuntime.eINSTANCE.getProcessInstanceManager().removeProcessInstance(this);
			StatelessRuntime.eINSTANCE.getEventSupport().fireAfterProcessCompleted(this, null/*kruntime*/);

			StatelessRuntime.eINSTANCE.getSignalManager().signalEvent("processInstanceCompleted:" + getId(), this);
		}
	}
	
	private void removeEventListeners() {
		for (String type : externalEventListeners.keySet()) {
			StatelessRuntime.eINSTANCE.getSignalManager().removeEventListener(type, this);
		}
	}
	
	
	    
	    public void internalSetState(final int state) {
	    	this.state = state;
	    }

	    
	    
	    
	    //jbpmn nodeInstanceContainer
	    public Collection<org.drools.runtime.process.NodeInstance> getNodeInstances() {
			return new ArrayList<org.drools.runtime.process.NodeInstance>(getNodeInstances(false));
		}

		public Collection<NodeInstance> getNodeInstances(boolean recursive) {
			Collection<NodeInstance> result = nodeInstances;
			if (recursive) {
				result = new ArrayList<NodeInstance>(result);
				for (Iterator<NodeInstance> iterator = nodeInstances.iterator(); iterator
						.hasNext();) {
					NodeInstance nodeInstance = iterator.next();
					if (nodeInstance instanceof NodeInstanceContainer) {
						result
								.addAll(((org.jbpm.workflow.instance.NodeInstanceContainer) nodeInstance)
										.getNodeInstances(true));
					}
				}
			}
			return Collections.unmodifiableCollection(result);
		}
		 

		public NodeInstance getFirstNodeInstance(final long nodeId) {
			for (final Iterator<NodeInstance> iterator = this.nodeInstances
					.iterator(); iterator.hasNext();) {
				final NodeInstance nodeInstance = iterator.next();
				if (nodeInstance.getNodeId() == nodeId) {
					return nodeInstance;
				}
			}
			return null;
		}

		public NodeInstance getNodeInstance(
				org.drools.definition.process.Node node) {
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

		public void addNodeInstance(NodeInstance nodeInstance) {
			((StatelessNodeInstanceImpl) nodeInstance).setId(nodeInstanceCounter++);
			this.nodeInstances.add(nodeInstance);
		}

		public void removeNodeInstance(NodeInstance nodeInstance) {
			this.nodeInstances.remove(nodeInstance);
		}

		public NodeContainer getNodeContainer() {
			return (WorkflowProcess) getProcess();
		}

		public void nodeInstanceCompleted(NodeInstance nodeInstance,
				String outType) {
			if (nodeInstance instanceof EndNodeInstance || 
	        		((org.jbpm.workflow.core.WorkflowProcess) getWorkflowProcess()).isDynamic()) {
	            if (((org.jbpm.workflow.core.WorkflowProcess) getProcess()).isAutoComplete()) {
	                if (nodeInstances.isEmpty()) {
	                    setState(ProcessInstance.STATE_COMPLETED);
	                }
	            }
	        } else {
	    		throw new IllegalArgumentException(
	    			"Completing a node instance that has no outgoing connection not suppoerted.");
	        }
		}

		private  WorkflowProcess getWorkflowProcess() {
			return (WorkflowProcess) getProcess();
		}
	
}
