package model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the CHILD_TBL database table.
 * 
 */
@Entity
@Table(name="CHILD_TBL")
public class ChildTbl implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="CHILDID")
	private String childid;
	
	@Column(length=20)
	private String childdesc;

	//bi-directional many-to-one association to ParentTbl
    @ManyToOne
	@JoinColumn(name="PARENTFK", referencedColumnName="CHILDMAPKEY")
	private ParentTbl parentTbl;

    public String getChildid() {
		return childid;
	}

	public void setChildid(String childid) {
		this.childid = childid;
	}

	public ChildTbl() {
    }

	public String getChilddesc() {
		return this.childdesc;
	}

	public void setChilddesc(String childdesc) {
		this.childdesc = childdesc;
	}

	public ParentTbl getParentTbl() {
		return this.parentTbl;
	}

	public void setParentTbl(ParentTbl parentTbl) {
		this.parentTbl = parentTbl;
	}
	
}