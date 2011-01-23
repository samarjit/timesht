package model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the PARENT_TBL database table.
 * 
 */
@Entity
@Table(name="PARENT_TBL")
public class ParentTbl implements Serializable {
	private static final long serialVersionUID = 1L;
	private String parentid;
	private String description;
	private List<ChildTbl> childTbls;
private String childmapkey;
 
    @Column(name="CHILDMAPKEY")
    public String getChildmapkey() {
	return childmapkey;
}


public void setChildmapkey(String childmapkey) {
	this.childmapkey = childmapkey;
}


	public ParentTbl() {
    }


	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="PARENTID",unique=true, nullable=false, length=10)
	public String getParentid() {
		return this.parentid;
	}

	public void setParentid(String parentid) {
		this.parentid = parentid;
	}


	@Column(length=20)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}


	//bi-directional many-to-one association to ChildTbl
	@OneToMany(mappedBy="parentTbl")
	public List<ChildTbl> getChildTbls() {
		return this.childTbls;
	}

	public void setChildTbls(List<ChildTbl> childTbls) {
		this.childTbls = childTbls;
	}
	
	public String toString(){
		return "parent tbl";
	}
	
}