package org.jbpm.samarjit;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.drools.WorkItemHandlerNotFoundException;
import org.drools.common.InternalKnowledgeRuntime;
import org.drools.process.instance.WorkItem;
import org.drools.process.instance.WorkItemManager;
import org.drools.process.instance.impl.WorkItemImpl;
import org.drools.runtime.process.WorkItemHandler;

public class StatelessWorkItemManager implements WorkItemManager{
	private static final long serialVersionUID = 510L;
	/*     */   private long workItemCounter;
	/*  41 */   private Map<Long, WorkItem> workItems = new ConcurrentHashMap();
	/*     */   private InternalKnowledgeRuntime kruntime;
	/*  43 */   private Map<String, WorkItemHandler> workItemHandlers = new HashMap();
	
	//DefaultWorkItemManager
	
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
		
		public void completeWorkItem(long paramLong, Map<String, Object> paramMap) {
			// TODO Auto-generated method stub
			
		}
		
		public void abortWorkItem(long paramLong) {
			// TODO Auto-generated method stub
			
		}
		
		public void registerWorkItemHandler(String paramString, WorkItemHandler paramWorkItemHandler) {
			// TODO Auto-generated method stub
			
		}
		 
		 
		
		public void internalAbortWorkItem(long paramLong) {
			// TODO Auto-generated method stub
			
		}
		
		public Set<WorkItem> getWorkItems() {
			// TODO Auto-generated method stub
			return null;
		}
		
		public WorkItem getWorkItem(long paramLong) {
			// TODO Auto-generated method stub
			return null;
		}
		
		public void clear() {
			// TODO Auto-generated method stub
			
		}	
}
