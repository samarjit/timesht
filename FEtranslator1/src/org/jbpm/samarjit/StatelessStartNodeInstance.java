package org.jbpm.samarjit;

public class StatelessStartNodeInstance extends StatelessNodeInstanceImpl{
	 public void internalTrigger(final NodeInstance from, String type) {
	        if (type != null) {
	            throw new IllegalArgumentException(
	                "A StartNode does not accept incoming connections!");
	        }
	        if (from != null) {
	            throw new IllegalArgumentException(
	                "A StartNode can only be triggered by the process itself!");
	        }
	        triggerCompleted();
	 }
	 
}
