package org.jbpm.samarjit;

import org.drools.runtime.process.NodeInstance;
import org.jbpm.workflow.core.node.StartNode;

public class StatelessStartNodeInstance extends StatelessNodeInstanceImpl {
	public void internalTrigger(final NodeInstance from, String type) {
		if (type != null) {
			throw new IllegalArgumentException("A StartNode does not accept incoming connections!");
		}
		if (from != null) {
			throw new IllegalArgumentException("A StartNode can only be triggered by the process itself!");
		}
		triggerCompleted();
	}

	public StartNode getStartNode() {
		return (StartNode) getNode();
	}

	public void triggerCompleted() {
		triggerCompleted(org.jbpm.workflow.core.Node.CONNECTION_DEFAULT_TYPE, true);
	}

}
