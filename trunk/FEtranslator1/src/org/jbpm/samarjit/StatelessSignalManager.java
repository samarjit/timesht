package org.jbpm.samarjit;

import org.drools.runtime.process.EventListener;
import org.jbpm.process.instance.event.SignalManager;

public class StatelessSignalManager implements SignalManager{

	@Override
	public void signalEvent(String type, Object event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void signalEvent(long processInstanceId, String type, Object event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addEventListener(String type, EventListener eventListener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeEventListener(String type, EventListener eventListener) {
		// TODO Auto-generated method stub
		
	}

}
