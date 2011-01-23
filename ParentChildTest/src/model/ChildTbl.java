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
	
	private String childid ;
	
	
	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	public String getChildid() {
		return childid;
	}

	public void setChildid(String childid) {
		this.childid = childid;
	}
	
	private String childdesc;
	private ParentTbl parentTbl;

    public ChildTbl() {
    }


	@Column(length=20)
	public String getChilddesc() {
		return this.childdesc;
	}

	public void setChilddesc(String childdesc) {
		this.childdesc = childdesc;
	}


	//bi-directional many-to-one association to ParentTbl
    @ManyToOne
	@JoinColumn(name="PARENTFK", referencedColumnName="CHILDMAPKEY", insertable=false, updatable=false)
	public ParentTbl getParentTbl() {
		return this.parentTbl;
	}

	public void setParentTbl(ParentTbl parentTbl) {
		this.parentTbl = parentTbl;
	}
	
}