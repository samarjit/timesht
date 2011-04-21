package org.jbpm.samarjit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.drools.definition.process.Connection;
import org.drools.definition.process.Node;
import org.drools.event.ProcessEventSupport;
import org.drools.process.core.Work;
import org.drools.process.instance.impl.WorkItemImpl;
import org.drools.runtime.process.NodeInstance;
import org.drools.runtime.process.NodeInstanceContainer;
import org.drools.runtime.process.WorkItem;
import org.drools.runtime.process.WorkflowProcessInstance;
import org.jbpm.process.core.Context;
import org.jbpm.process.core.ContextContainer;
import org.jbpm.process.core.context.exclusive.ExclusiveGroup;
import org.jbpm.process.core.context.variable.VariableScope;
import org.jbpm.process.instance.ContextInstance;
import org.jbpm.process.instance.ContextInstanceContainer;
import org.jbpm.process.instance.ProcessInstance;
import org.jbpm.process.instance.context.exclusive.ExclusiveGroupInstance;
import org.jbpm.process.instance.context.variable.VariableScopeInstance;
import org.jbpm.workflow.core.impl.NodeImpl;
import org.jbpm.workflow.core.node.WorkItemNode;
import org.jbpm.workflow.instance.impl.NodeInstanceResolverFactory;
import org.mvel2.MVEL;
 
public class StatelessNodeInstanceImpl implements org.jbpm.workflow.instance.NodeInstance {

	private static final long serialVersionUID = 511l;
	private static final Pattern PARAMETER_MATCHER = Pattern.compile("#\\{(\\S+)\\}", Pattern.DOTALL);
	private long id;
    private long nodeId;
    private StatelessProcessInstance processInstance;
    private org.jbpm.workflow.instance.NodeInstanceContainer nodeInstanceContainer;
    private transient WorkItem workItem;

	 
	
	public final void trigger(NodeInstance from, String type) {
    	boolean hidden = false;
    	if (getNode().getMetaData().get("hidden") != null) {
    		hidden = true;
    	}
    	if (!hidden) {
    		getProcessEventSupport().fireBeforeNodeTriggered(this, null /*kruntime*/);
    	}
        internalTrigger(from, type);
        if (!hidden) {
        	getProcessEventSupport().fireAfterNodeTriggered(this,null /*kruntime*/);
        }
    }
	
	
	private ProcessEventSupport getProcessEventSupport() {
		return StatelessRuntime.eINSTANCE.getEventSupport();
	}


	private   void internalTrigger(NodeInstance from, String type) {
		throw new UnsupportedOperationException(" internamTrigger(): StatelessNodeInstance ");
	}


	protected void triggerCompleted(String type, boolean remove) {
        if (remove) {
            ((org.jbpm.workflow.instance.NodeInstanceContainer) getNodeInstanceContainer())
            	.removeNodeInstance(this);
        }
        Node node = getNode();
        List<Connection> connections = null;
        if (node != null) {
        	connections = node.getOutgoingConnections(type);
        }
        if (connections == null || connections.isEmpty()) {
        	((org.jbpm.workflow.instance.NodeInstanceContainer) getNodeInstanceContainer())
        		.nodeInstanceCompleted(this, type);
        } else {
	        for (Connection connection: connections) {
	        	// stop if this process instance has been aborted / completed
	        	if (getProcessInstance().getState() != ProcessInstance.STATE_ACTIVE) {
	        		return;
	        	}
	    		triggerConnection(connection);
	        }
        }
    }	
	
	
	protected void triggerConnection(Connection connection) {
    	boolean hidden = false;
    	if (getNode().getMetaData().get("hidden") != null) {
    		hidden = true;
    	}
    	if (!hidden) {
    		 getProcessEventSupport().fireBeforeNodeLeft(this, null);
    	}
    	// check for exclusive group first
    	NodeInstanceContainer parent = getNodeInstanceContainer();
    	if (parent instanceof ContextInstanceContainer) {
    		List<ContextInstance> contextInstances = ((ContextInstanceContainer) parent).getContextInstances(ExclusiveGroup.EXCLUSIVE_GROUP);
    		if (contextInstances != null) {
    			for (ContextInstance contextInstance: new ArrayList<ContextInstance>(contextInstances)) {
    				ExclusiveGroupInstance groupInstance = (ExclusiveGroupInstance) contextInstance;
    				if (groupInstance.containsNodeInstance(this)) {
    					for (NodeInstance nodeInstance: groupInstance.getNodeInstances()) {
    						if (nodeInstance != this) {
    							((org.jbpm.workflow.instance.NodeInstance) nodeInstance).cancel();
    						}
    					}
    					((ContextInstanceContainer) parent).removeContextInstance(ExclusiveGroup.EXCLUSIVE_GROUP, contextInstance);
    				}
    				
    			}
    		}
    	}
    	// trigger next node
        ((org.jbpm.workflow.instance.NodeInstance) ((org.jbpm.workflow.instance.NodeInstanceContainer) getNodeInstanceContainer())
        	.getNodeInstance(connection.getTo())).trigger(this, connection.getToType());
        if (!hidden) {
        	 getProcessEventSupport().fireAfterNodeLeft(this, null);
        }
    }

	public void setId(final long id) {
        this.id = id;
    }
	
	@Override
	public long getId() {
		 return this.id;
	}

	public void setNodeId(final long nodeId) {
		this.nodeId = nodeId;
	}

	@Override
	public long getNodeId() {
		return this.nodeId;
	}


	@Override
	public String getNodeName() {
		Node node = getNode();
    	return node == null ? "" : node.getName();
	}

	public void setProcessInstance(final StatelessProcessInstance processInstance) {
	    this.processInstance = processInstance;
	}

	@Override
	public StatelessProcessInstance getProcessInstance() {
		  return this.processInstance;
	}


	@Override
	public NodeInstanceContainer getNodeInstanceContainer() {
		 return this.nodeInstanceContainer;
	}

	public void setNodeInstanceContainer(NodeInstanceContainer nodeInstanceContainer) {
        this.nodeInstanceContainer = (org.jbpm.workflow.instance.NodeInstanceContainer) nodeInstanceContainer;
        if (nodeInstanceContainer != null) {
            this.nodeInstanceContainer.addNodeInstance(this);
        }
    }
	
	@Override
	public Object getVariable(String paramString) {
		return null;
	}


	@Override
	public void setVariable(String paramString, Object paramObject) {
		// TODO Auto-generated method stub
		
	}




	@Override
	public void cancel() {
		nodeInstanceContainer.removeNodeInstance(this);
	}


	@Override
	public Node getNode() {
		 return ((org.jbpm.workflow.core.NodeContainer)
		    		this.nodeInstanceContainer.getNodeContainer()).internalGetNode( this.nodeId );
	}

	public Context resolveContext(String contextId, Object param) {
        return ((NodeImpl) getNode()).resolveContext(contextId, param);
    }
	
	private ContextInstanceContainer getContextInstanceContainer(ContextContainer contextContainer) {
    	ContextInstanceContainer contextInstanceContainer = null; 
		if (this instanceof ContextInstanceContainer) {
        	contextInstanceContainer = (ContextInstanceContainer) this;
        } else {
        	contextInstanceContainer = getEnclosingContextInstanceContainer(this);
        }
        while (contextInstanceContainer != null) {
    		if (contextInstanceContainer.getContextContainer() == contextContainer) {
    			return contextInstanceContainer;
    		}
    		contextInstanceContainer = getEnclosingContextInstanceContainer(
				(NodeInstance) contextInstanceContainer);
    	}
        return null;
    }
	
	 private ContextInstanceContainer getEnclosingContextInstanceContainer(NodeInstance nodeInstance) {
	    	NodeInstanceContainer nodeInstanceContainer = nodeInstance.getNodeInstanceContainer();
	    	while (true) {
	    		if (nodeInstanceContainer instanceof ContextInstanceContainer) {
	    			return (ContextInstanceContainer) nodeInstanceContainer;
	    		}
	    		if (nodeInstanceContainer instanceof NodeInstance) {
	    			nodeInstanceContainer = ((NodeInstance) nodeInstanceContainer).getNodeInstanceContainer();
	    		} else {
	    			return null;
	    		}
	    	}
	    }
	 
	@Override
	public ContextInstance resolveContextInstance(String contextId, Object param) {
		 Context context = resolveContext(contextId, param);
	        if (context == null) {
	            return null;
	        }
	        ContextInstanceContainer contextInstanceContainer
	        	= getContextInstanceContainer(context.getContextContainer());
	        if (contextInstanceContainer == null) {
	        	throw new IllegalArgumentException(
	    			"Could not find context instance container for context");
	        }
	        return contextInstanceContainer.getContextInstance(context);
	}


	protected WorkItem createWorkItem(WorkItemNode workItemNode) {
		Work work = workItemNode.getWork();
        workItem = new WorkItemImpl();
        ((org.drools.process.instance.WorkItem) workItem).setName(work.getName());
        ((org.drools.process.instance.WorkItem) workItem).setProcessInstanceId(getProcessInstance().getId());
        ((org.drools.process.instance.WorkItem) workItem).setParameters(new HashMap<String, Object>(work.getParameters()));
        for (Iterator<Map.Entry<String, String>> iterator = workItemNode.getInMappings().entrySet().iterator(); iterator.hasNext(); ) {
            Map.Entry<String, String> mapping = iterator.next();
            Object parameterValue = null;
            VariableScopeInstance variableScopeInstance = (VariableScopeInstance)
                resolveContextInstance(VariableScope.VARIABLE_SCOPE, mapping.getValue());
            if (variableScopeInstance != null) {
            	parameterValue = variableScopeInstance.getVariable(mapping.getValue());
            } else {
            	try {
            		parameterValue = MVEL.eval(mapping.getValue(), new NodeInstanceResolverFactory(this));
            	} catch (Throwable t) {
	                System.err.println("Could not find variable scope for variable " + mapping.getValue());
	                System.err.println("when trying to execute Work Item " + work.getName());
	                System.err.println("Continuing without setting parameter.");
            	}
            }
            if (parameterValue != null) {
            	((org.drools.process.instance.WorkItem) workItem).setParameter(mapping.getKey(), parameterValue);
            }
        }
        for (Map.Entry<String, Object> entry: workItem.getParameters().entrySet()) {
        	if (entry.getValue() instanceof String) {
        		String s = (String) entry.getValue();
        		Map<String, String> replacements = new HashMap<String, String>();
        		Matcher matcher = PARAMETER_MATCHER.matcher(s);
                while (matcher.find()) {
                	String paramName = matcher.group(1);
                	if (replacements.get(paramName) == null) {
		            	VariableScopeInstance variableScopeInstance = (VariableScopeInstance)
		                	resolveContextInstance(VariableScope.VARIABLE_SCOPE, paramName);
		                if (variableScopeInstance != null) {
		                    Object variableValue = variableScopeInstance.getVariable(paramName);
		                	String variableValueString = variableValue == null ? "" : variableValue.toString(); 
			                replacements.put(paramName, variableValueString);
		                } else {
		                	try {
		                		Object variableValue = MVEL.eval(paramName, new NodeInstanceResolverFactory(this));
			                	String variableValueString = variableValue == null ? "" : variableValue.toString();
			                	replacements.put(paramName, variableValueString);
		                	} catch (Throwable t) {
			                    System.err.println("Could not find variable scope for variable " + paramName);
			                    System.err.println("when trying to replace variable in string for Work Item " + work.getName());
			                    System.err.println("Continuing without setting parameter.");
		                	}
		                }
                	}
                }
                for (Map.Entry<String, String> replacement: replacements.entrySet()) {
                	s = s.replace("#{" + replacement.getKey() + "}", replacement.getValue());
                }
                ((org.drools.process.instance.WorkItem) workItem).setParameter(entry.getKey(), s);
        	}
        }
        return workItem;
	}
	
	
	
}
