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
	@Column(unique=true, nullable=false, length=10)
	private String childid;

	@Column(length=20)
	private String childdesc;

	//uni-directional many-to-one association to ParentTbl
    @ManyToOne
	@JoinColumn(name="PARENTFK", referencedColumnName="CHILDMAPKEY")
	private ParentTbl parentTbl;

    public ChildTbl() {
    }

	public String getChildid() {
		return this.childid;
	}

	public void setChildid(String childid) {
		this.childid = childid;
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