// default package
// Generated May 16, 2011 12:47:08 PM by Hibernate Tools 3.2.2.GA

package org.jbpm.samarjit.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

/**
 * ActRuExecution generated by hbm2java
 */
@Entity
@Table(name="ACT_RU_EXECUTION"
    ,schema="PUBLIC"
    ,catalog="ACTIVITI"
    , uniqueConstraints = @UniqueConstraint(columnNames={"PROC_DEF_ID_", "BUSINESS_KEY_"}) 
)
public class ActRuExecution  implements java.io.Serializable {


     private String id;
//     private ActRuExecution actRuExecutionByParentId;
//     private ActRuExecution actRuExecutionBySuperExec;
//     private ActRuExecution actRuExecutionByProcInstId;
     private String procInstId;
     private String parentId;
     private String superExec;
     
     private Integer rev;
     private String businessKey;
     private String procDefId;
     private String actId;
     private Boolean isActive;
     private Boolean isConcurrent;
     private Boolean isScope;
     private Date startTime;
//     private Set<ActRuVariable> actRuVariablesForProcInstId = new HashSet<ActRuVariable>(0);
//     private Set<ActRuVariable> actRuVariablesForExecutionId = new HashSet<ActRuVariable>(0);
//     private Set<ActRuExecution> actRuExecutionsForProcInstId = new HashSet<ActRuExecution>(0);
//     private Set<ActRuExecution> actRuExecutionsForSuperExec = new HashSet<ActRuExecution>(0);
//     private Set<ActRuExecution> actRuExecutionsForParentId = new HashSet<ActRuExecution>(0);
     private List<ActRuTask> actRuTasksForProcInstId = new ArrayList<ActRuTask>(0);
//     private Set<ActRuTask> actRuTasksForExecutionId = new HashSet<ActRuTask>(0);

    public ActRuExecution() {
    }

	
    public ActRuExecution(String id) {
        this.id = id;
    }
   /* public ActRuExecution(String id, ActRuExecution actRuExecutionByParentId, ActRuExecution actRuExecutionBySuperExec, ActRuExecution actRuExecutionByProcInstId, Integer rev, String businessKey, String procDefId, String actId, Boolean isActive, Boolean isConcurrent, Boolean isScope, Date startTime, Set<ActRuVariable> actRuVariablesForProcInstId, Set<ActRuVariable> actRuVariablesForExecutionId, Set<ActRuExecution> actRuExecutionsForProcInstId, Set<ActRuExecution> actRuExecutionsForSuperExec, Set<ActRuExecution> actRuExecutionsForParentId, Set<ActRuTask> actRuTasksForProcInstId, Set<ActRuTask> actRuTasksForExecutionId) {
       this.id = id;
       this.actRuExecutionByParentId = actRuExecutionByParentId;
       this.actRuExecutionBySuperExec = actRuExecutionBySuperExec;
       this.actRuExecutionByProcInstId = actRuExecutionByProcInstId;
       this.rev = rev;
       this.businessKey = businessKey;
       this.procDefId = procDefId;
       this.actId = actId;
       this.isActive = isActive;
       this.isConcurrent = isConcurrent;
       this.isScope = isScope;
       this.startTime = startTime;
       this.actRuVariablesForProcInstId = actRuVariablesForProcInstId;
       this.actRuVariablesForExecutionId = actRuVariablesForExecutionId;
       this.actRuExecutionsForProcInstId = actRuExecutionsForProcInstId;
       this.actRuExecutionsForSuperExec = actRuExecutionsForSuperExec;
       this.actRuExecutionsForParentId = actRuExecutionsForParentId;
       this.actRuTasksForProcInstId = actRuTasksForProcInstId;
       this.actRuTasksForExecutionId = actRuTasksForExecutionId;
    }*/
   
     @Id 
    
    @Column(name="ID_", unique=true, nullable=false, length=64)
    public String getId() {
        return this.id;
    }
    
     

	public String getParentId() {
		return parentId;
	}


	public void setParentId(String parentId) {
		this.parentId = parentId;
	}


	public String getSuperExec() {
		return superExec;
	}


	public void setSuperExec(String superExec) {
		this.superExec = superExec;
	}


	public void setId(String id) {
        this.id = id;
    }

    
    @Column(name="REV_")
    public Integer getRev() {
        return this.rev;
    }
    
    public void setRev(Integer rev) {
        this.rev = rev;
    }
    
    @Column(name="BUSINESS_KEY_")
    public String getBusinessKey() {
        return this.businessKey;
    }
    
    public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
    }
    
    @Column(name="PROC_DEF_ID_", length=64)
    public String getProcDefId() {
        return this.procDefId;
    }
    
    public void setProcDefId(String procDefId) {
        this.procDefId = procDefId;
    }
    
    @Column(name="ACT_ID_")
    public String getActId() {
        return this.actId;
    }
    
    public void setActId(String actId) {
        this.actId = actId;
    }
    
    @Column(name="IS_ACTIVE_")
    public Boolean getIsActive() {
        return this.isActive;
    }
    
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
    
    @Column(name="IS_CONCURRENT_")
    public Boolean getIsConcurrent() {
        return this.isConcurrent;
    }
    
    public void setIsConcurrent(Boolean isConcurrent) {
        this.isConcurrent = isConcurrent;
    }
    
    @Column(name="IS_SCOPE_")
    public Boolean getIsScope() {
        return this.isScope;
    }
    
    public void setIsScope(Boolean isScope) {
        this.isScope = isScope;
    }
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="START_TIME_", length=23)
    public Date getStartTime() {
        return this.startTime;
    }
    
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }


	@Override
	public String toString() {
		return "ActRuExecution [id=" + id + ", procInstId=" + procInstId + ", parentId=" + parentId + ", superExec=" + superExec + ", rev=" + rev + ", businessKey=" + businessKey + ", procDefId="
				+ procDefId + ", actId=" + actId + ", isActive=" + isActive + ", isConcurrent=" + isConcurrent + ", isScope=" + isScope + ", startTime=" + startTime + "]";
	}
 
    /*
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="PARENT_ID_")
    public ActRuExecution getActRuExecutionByParentId() {
        return this.actRuExecutionByParentId;
    }
    
    public void setActRuExecutionByParentId(ActRuExecution actRuExecutionByParentId) {
        this.actRuExecutionByParentId = actRuExecutionByParentId;
    }
@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="SUPER_EXEC_")
    public ActRuExecution getActRuExecutionBySuperExec() {
        return this.actRuExecutionBySuperExec;
    }
    
    public void setActRuExecutionBySuperExec(ActRuExecution actRuExecutionBySuperExec) {
        this.actRuExecutionBySuperExec = actRuExecutionBySuperExec;
    }
@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="PROC_INST_ID_")
    public ActRuExecution getActRuExecutionByProcInstId() {
        return this.actRuExecutionByProcInstId;
    }
    
    public void setActRuExecutionByProcInstId(ActRuExecution actRuExecutionByProcInstId) {
        this.actRuExecutionByProcInstId = actRuExecutionByProcInstId;
    }
    
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="actRuExecutionByProcInstId")
    public Set<ActRuVariable> getActRuVariablesForProcInstId() {
        return this.actRuVariablesForProcInstId;
    }
    
    public void setActRuVariablesForProcInstId(Set<ActRuVariable> actRuVariablesForProcInstId) {
        this.actRuVariablesForProcInstId = actRuVariablesForProcInstId;
    }
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="actRuExecutionByExecutionId")
    public Set<ActRuVariable> getActRuVariablesForExecutionId() {
        return this.actRuVariablesForExecutionId;
    }
    
    public void setActRuVariablesForExecutionId(Set<ActRuVariable> actRuVariablesForExecutionId) {
        this.actRuVariablesForExecutionId = actRuVariablesForExecutionId;
    }
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="actRuExecutionByProcInstId")
    public Set<ActRuExecution> getActRuExecutionsForProcInstId() {
        return this.actRuExecutionsForProcInstId;
    }
    
    public void setActRuExecutionsForProcInstId(Set<ActRuExecution> actRuExecutionsForProcInstId) {
        this.actRuExecutionsForProcInstId = actRuExecutionsForProcInstId;
    }
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="actRuExecutionBySuperExec")
    public Set<ActRuExecution> getActRuExecutionsForSuperExec() {
        return this.actRuExecutionsForSuperExec;
    }
    
    public void setActRuExecutionsForSuperExec(Set<ActRuExecution> actRuExecutionsForSuperExec) {
        this.actRuExecutionsForSuperExec = actRuExecutionsForSuperExec;
    }
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="actRuExecutionByParentId")
    public Set<ActRuExecution> getActRuExecutionsForParentId() {
        return this.actRuExecutionsForParentId;
    }
    
    public void setActRuExecutionsForParentId(Set<ActRuExecution> actRuExecutionsForParentId) {
        this.actRuExecutionsForParentId = actRuExecutionsForParentId;
    }
    */
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="actRuExecutionByProcInstId")
    public List<ActRuTask> getActRuTasksForProcInstId() {
        return this.actRuTasksForProcInstId;
    }
    
    public void setActRuTasksForProcInstId(List<ActRuTask> actRuTasksForProcInstId) {
        this.actRuTasksForProcInstId = actRuTasksForProcInstId;
    }
    /*
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="actRuExecutionByExecutionId")
    public Set<ActRuTask> getActRuTasksForExecutionId() {
        return this.actRuTasksForExecutionId;
    }
    
    public void setActRuTasksForExecutionId(Set<ActRuTask> actRuTasksForExecutionId) {
        this.actRuTasksForExecutionId = actRuTasksForExecutionId;
    }


*/

}


