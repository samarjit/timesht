package pojo.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the TS_EMP_TIMESHEET database table.
 * 
 */
@Entity
@Table(name="TS_EMP_TIMESHEET")
public class TsEmpTimesheet implements Serializable {
	private static final long serialVersionUID = 1L;

	private String empid;

	private String empname;

    @Temporal( TemporalType.DATE)
	private Date tsdate;

	private BigDecimal tshrs;

	private String tskid;

	//bi-directional many-to-one association to TsTaskmaster
    @ManyToOne
	@JoinColumn(name="TSID")
	private TsTaskmaster tsTaskmaster;

    public TsEmpTimesheet() {
    }

	public String getEmpid() {
		return this.empid;
	}

	public void setEmpid(String empid) {
		this.empid = empid;
	}

	public String getEmpname() {
		return this.empname;
	}

	public void setEmpname(String empname) {
		this.empname = empname;
	}

	public Date getTsdate() {
		return this.tsdate;
	}

	public void setTsdate(Date tsdate) {
		this.tsdate = tsdate;
	}

	public BigDecimal getTshrs() {
		return this.tshrs;
	}

	public void setTshrs(BigDecimal tshrs) {
		this.tshrs = tshrs;
	}

	public String getTskid() {
		return this.tskid;
	}

	public void setTskid(String tskid) {
		this.tskid = tskid;
	}

	public TsTaskmaster getTsTaskmaster() {
		return this.tsTaskmaster;
	}

	public void setTsTaskmaster(TsTaskmaster tsTaskmaster) {
		this.tsTaskmaster = tsTaskmaster;
	}
	
}