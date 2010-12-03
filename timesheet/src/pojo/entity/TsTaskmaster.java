package pojo.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the TS_TASKMASTER database table.
 * 
 */
@Entity
@Table(name="TS_TASKMASTER")
public class TsTaskmaster implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String tskid;

	private String taskdesc;

    @Temporal( TemporalType.DATE)
	private Date tskenddate;

	private BigDecimal tskhours;

	private String tskpercentcpl;

    @Temporal( TemporalType.DATE)
	private Date tskstartdate;

	//bi-directional many-to-one association to TsEmpTimesheet
	@OneToMany(mappedBy="tsTaskmaster")
	private List<TsEmpTimesheet> tsEmpTimesheets;

	//bi-directional many-to-one association to TsProject
	@ManyToOne(cascade={CascadeType.ALL})
	@JoinColumn(name="PROJECT", referencedColumnName="PROJECT")
	private TsProject tsProject;

    public TsTaskmaster() {
    }

	public String getTskid() {
		return this.tskid;
	}

	public void setTskid(String tskid) {
		this.tskid = tskid;
	}

	public String getTaskdesc() {
		return this.taskdesc;
	}

	public void setTaskdesc(String taskdesc) {
		this.taskdesc = taskdesc;
	}

	public Date getTskenddate() {
		return this.tskenddate;
	}

	public void setTskenddate(Date tskenddate) {
		this.tskenddate = tskenddate;
	}

	public BigDecimal getTskhours() {
		return this.tskhours;
	}

	public void setTskhours(BigDecimal tskhours) {
		this.tskhours = tskhours;
	}

	public String getTskpercentcpl() {
		return this.tskpercentcpl;
	}

	public void setTskpercentcpl(String tskpercentcpl) {
		this.tskpercentcpl = tskpercentcpl;
	}

	public Date getTskstartdate() {
		return this.tskstartdate;
	}

	public void setTskstartdate(Date tskstartdate) {
		this.tskstartdate = tskstartdate;
	}

	public List<TsEmpTimesheet> getTsEmpTimesheets() {
		return this.tsEmpTimesheets;
	}

	public void setTsEmpTimesheets(List<TsEmpTimesheet> tsEmpTimesheets) {
		this.tsEmpTimesheets = tsEmpTimesheets;
	}
	
	public TsProject getTsProject() {
		return this.tsProject;
	}

	public void setTsProject(TsProject tsProject) {
		this.tsProject = tsProject;
	}
	
}