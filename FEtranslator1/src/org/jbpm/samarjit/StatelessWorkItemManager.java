package org.jbpm.samarjit;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.drools.process.instance.WorkItem;
import org.drools.process.instance.WorkItemManager;
import org.drools.process.instance.impl.WorkItemImpl;
import org.drools.runtime.process.WorkItemHandler;

public class StatelessWorkItemManager implements WorkItemManager{
	private Map<String, WorkItemHandler> workItemHandlers = new HashMap();
	//DefaultWorkItemManager
	@Override
	public void internalExecuteWorkItem(WorkItem workItem) {
		/*  65 */     ((WorkItemImpl)workItem).setId(++this.workItemCounter);
		/*  66 */     internalAddWorkItem(workItem);
		/*  67 */     WorkItemHandler handler = (WorkItemHandler)this.workItemHandlers.get(workItem.getName());
		/*  68 */     if (handler != null)
		/*  69 */       handler.executeWorkItem(workItem, this);
		/*     */     else throw new WorkItemHandlerNotFoundException("Could not find work item handler for " + workItem.getName(), workItem.getName());
		/*     */   }
		/*     */ 
		/*     */   public void internalAddWorkItem(WorkItem workItem)
		/*     */   {
		/*  75 */     this.workItems.put(new Long(workItem.getId()), workItem);
		/*     */ 
		/*  77 */     if (workItem.getId() > this.workItemCounter)
		/*  78 */       this.workItemCounter = workItem.getId();
		/*     */   }
		@Override
		public void completeWorkItem(long paramLong, Map<String, Object> paramMap) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void abortWorkItem(long paramLong) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void registerWorkItemHandler(String paramString, WorkItemHandler paramWorkItemHandler) {
			// TODO Auto-generated method stub
			
		}
		 
		 
		@Override
		public void internalAbortWorkItem(long paramLong) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public Set<WorkItem> getWorkItems() {
			// TODO Auto-generated method stub
			return null;
		}
		@Override
		public WorkItem getWorkItem(long paramLong) {
			// TODO Auto-generated method stub
			return null;
		}
		@Override
		public void clear() {
			// TODO Auto-generated method stub
			
		}	
}
