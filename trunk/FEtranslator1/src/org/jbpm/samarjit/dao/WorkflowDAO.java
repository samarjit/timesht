package org.jbpm.samarjit.dao;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import javax.sql.rowset.CachedRowSet;

import org.jbpm.samarjit.StatelessNodeInstance;
import org.jbpm.samarjit.dao.PrepstmtDTO.DataType;
import org.jbpm.workflow.instance.NodeInstance;

public class WorkflowDAO {
	public static void log(String s){
		System.out.println("WorkflowDAO:"+s);
	}
	public static int getNextId(){
		DBConnector db = new DBConnector();
		int value=-1;
		try {
			String qryNextId = "select value_  from ACT_GE_PROPERTY where name_='next.dbid'";
			CachedRowSet crs = db.executeQuery(qryNextId);
			if(crs.next()){
				value = crs.getInt("VALUE_");
			}
			value++;
			String qryUpdNextId = "update ACT_GE_PROPERTY set value_= "+value +" where name_='next.dbid'";
			log(qryUpdNextId);
			db.executeUpdate(qryUpdNextId);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return value;
	}
	public static void createProcessInstance(NodeInstance nodeInstance) {
		DBConnector db = new DBConnector();
		try {
//			int id = getNextId();
			String qryCreateProcInst = "insert into ACT_RU_EXECUTION (ID_,REV_,PROC_INST_ID_,PARENT_ID_,PROC_DEF_ID_ ,IS_ACTIVE_ ) values("
				+"'"+nodeInstance.getProcessInstance().getId()+"',0,'"+nodeInstance.getProcessInstance().getId()+"',null,'"+nodeInstance.getProcessInstance().getProcessId()+"'" +
						", "+nodeInstance.getProcessInstance().getState()+")";
			log(qryCreateProcInst);
			db.executeUpdate(qryCreateProcInst);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public static void completeProcessInstance(long Id) {
		DBConnector db = new DBConnector();
		try {
//			int id = getNextId();
			String qrySelProcCompleted = "select ID_,PROC_INST_ID_, BUSINESS_KEY_, PROC_DEF_ID_, ACT_ID_, START_TIME_ from ACT_RU_EXECUTION where PROC_INST_ID_='"+Id+"'";
			log(qrySelProcCompleted);
			CachedRowSet crs  = db.executeQuery(qrySelProcCompleted);
			while(crs.next()){
				String id = crs.getString("ID_");
				String processInstanceId = crs.getString("PROC_INST_ID_");
				String businessKey = crs.getString("BUSINESS_KEY_");
				String procDecId = crs.getString("PROC_DEF_ID_");
				String startActId = crs.getString("ACT_ID_");
				Timestamp startTime = crs.getTimestamp("START_TIME_");
			String sqlProcInsertHistNode = "insert into ACT_HI_PROCINST (ID_,PROC_INST_ID_, BUSINESS_KEY_, PROC_DEF_ID_, START_ACT_ID_, START_TIME_, END_TIME_, DURATION_)" +
				"values (?,?,?,?,?,?,?,?)";
				PrepstmtDTOArray prepStmtAr1 = new PrepstmtDTOArray();
				prepStmtAr1.add(DataType.STRING,id);
				prepStmtAr1.add(DataType.STRING,processInstanceId);
				prepStmtAr1.add(DataType.STRING,businessKey);
				prepStmtAr1.add(DataType.STRING,procDecId);
				prepStmtAr1.add(DataType.STRING,startActId);
			if(startTime == null)startTime= new Timestamp(new Date().getTime());
				prepStmtAr1.add(DataType.TIMESTAMP,startTime);
				prepStmtAr1.add(DataType.TIMESTAMP,new Timestamp(new Date().getTime()));
			Long duration = new Date().getTime() - startTime.getTime();
				prepStmtAr1.add(DataType.LONG,duration.toString());
				
				log(prepStmtAr1.toString(sqlProcInsertHistNode));
				db.executePreparedUpdate(sqlProcInsertHistNode, prepStmtAr1);
			}
			String qryCompleteProcInst = "delete from ACT_RU_EXECUTION where PROC_INST_ID_='"+Id+"'";
			log(qryCompleteProcInst);
			db.executeUpdate(qryCompleteProcInst);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public static void createNodeInstance(StatelessNodeInstance nodeInstance) {
		DBConnector db = new DBConnector();
		try {
//			int id = getNextId();
			String qryExistingNodeInst = "select ID_ from ACT_RU_TASK where ID_='"+nodeInstance.getId()+"'";
			log(qryExistingNodeInst);
			CachedRowSet crs = db.executeQuery(qryExistingNodeInst);
			if(crs.next()){
				return; //Do not create a node that already exisits .. like for Reused Join Nodes
			}
			
			String qryCreateNodeInst = "insert into ACT_RU_TASK (ID_,REV_,EXECUTION_ID_,PROC_INST_ID_,PROC_DEF_ID_,NAME_," +
					"PARENT_TASK_ID_,DESCRIPTION_,TASK_DEF_KEY_,OWNER_,ASSIGNEE_,DELEGATION_,PRIORITY_,CREATE_TIME_,DUE_DATE_, CLASS_NAME_" + 
					") values\n("
				+"'"+nodeInstance.getId()+"',0,'"+nodeInstance.getProcessInstance().getId()+"','"+nodeInstance.getProcessInstance().getId()+"','"+nodeInstance.getProcessInstance().getProcessId()+"','"+nodeInstance.getNodeName()+"'"
				+",null,'"+nodeInstance+"','"+nodeInstance.getNodeId()+"',null,null,null,null,'"+new Timestamp(new Date().getTime())+"',null, '"+nodeInstance.getClass().getCanonicalName()+"')";
			System.err.println("Starting node instande "+nodeInstance);
			log(qryCreateNodeInst);
			db.executeUpdate(qryCreateNodeInst	
			);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public static void completeNodeInstance(StatelessNodeInstance nodeInstance) {
		DBConnector db = new DBConnector();
		try {
//			int id = getNextId();
//			System.err.println("Completing node instande "+nodeInstance);//if(1==1)return;
			String qryCompleteNodeInst = "select ID_,EXECUTION_ID_,PROC_INST_ID_,PROC_DEF_ID_,NAME_,PARENT_TASK_ID_,DESCRIPTION_,TASK_DEF_KEY_ ,ASSIGNEE_,DUE_DATE_,PRIORITY_,OWNER_,CREATE_TIME_   from ACT_RU_TASK where ID_='"+nodeInstance.getId()+"'";
			log(qryCompleteNodeInst);
			CachedRowSet crs = db.executeQuery(qryCompleteNodeInst);
			while(crs.next()){
				String id = crs.getString("ID_");
				String executionId = crs.getString("EXECUTION_ID_");
				String processInstanceId = crs.getString("PROC_INST_ID_");
				String procDecId = crs.getString("PROC_DEF_ID_");
				String name = crs.getString("NAME_");
				String taskId = crs.getString("PARENT_TASK_ID_");
				String description = crs.getString("DESCRIPTION_");
				String taskDefKey = crs.getString("TASK_DEF_KEY_");
				String assignee = crs.getString("ASSIGNEE_");
				String dueDate = crs.getString("DUE_DATE_");
				Integer priority = crs.getInt("PRIORITY_");
				String owner = crs.getString("OWNER_");
				Timestamp createTime = crs.getTimestamp("CREATE_TIME_");
				
				String sqlInsertHistNode = "insert into ACT_HI_TASKINST (ID_,EXECUTION_ID_,PROC_INST_ID_,PROC_DEF_ID_,NAME_,PARENT_TASK_ID_,DESCRIPTION_,TASK_DEF_KEY_ ,ASSIGNEE_,DUE_DATE_,PRIORITY_,OWNER_,START_TIME_, END_TIME_, DURATION_)" +
						"values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				PrepstmtDTOArray prepStmtAr = new PrepstmtDTOArray();
				prepStmtAr.add(DataType.STRING,id);
				prepStmtAr.add(DataType.STRING,executionId);
				prepStmtAr.add(DataType.STRING,processInstanceId);
				prepStmtAr.add(DataType.STRING,procDecId);
				prepStmtAr.add(DataType.STRING,name);
				prepStmtAr.add(DataType.STRING,taskId);
				prepStmtAr.add(DataType.STRING,description);
				prepStmtAr.add(DataType.STRING,taskDefKey);
				prepStmtAr.add(DataType.STRING,assignee);
				prepStmtAr.add(DataType.STRING,dueDate);
				if(priority == null)priority=0;
				prepStmtAr.add(DataType.INT,priority.toString());
				prepStmtAr.add(DataType.STRING,owner);
				if(createTime == null)createTime= new Timestamp(new Date().getTime());
				prepStmtAr.add(DataType.TIMESTAMP,createTime);
				prepStmtAr.add(DataType.TIMESTAMP,new Timestamp(new Date().getTime()));
				Long duration = new Date().getTime() - createTime.getTime();
				prepStmtAr.add(DataType.LONG,duration.toString());
				
				log(prepStmtAr.toString(sqlInsertHistNode));
				db.executePreparedUpdate(sqlInsertHistNode, prepStmtAr);
				
				String qryDeleteCompletedNodeInst= "delete from ACT_RU_TASK where ID_='"+nodeInstance.getId()+"'";
				db.executeUpdate(qryDeleteCompletedNodeInst);
			}
//			if(saved)System.out.println("The completing instane has been saved.."+nodeInstance);
//			else System.out.println("The completing instane has failed to save.."+nodeInstance);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
