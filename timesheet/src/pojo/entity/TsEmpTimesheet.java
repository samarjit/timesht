package pojo.entity;

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

	@Id
	private String tsid;


	//bi-directional many-to-one association to TsTaskmaster
    @ManyToOne
	@JoinColumn(name="TSKID")
	private TsTaskmaster tsTaskmaster1;


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

	public String getTsid() {
		return this.tsid;
	}

	public void setTsid(String tsid) {
		this.tsid = tsid;
	}

	public TsTaskmaster getTsTaskmaster1() {
		return this.tsTaskmaster1;
	}

	public void setTsTaskmaster1(TsTaskmaster tsTaskmaster1) {
		this.tsTaskmaster1 = tsTaskmaster1;
	}
	

}