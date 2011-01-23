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

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(unique=true, nullable=false, length=10)
	private String parentid;

	@Column(length=20)
	private String description;

	//bi-directional many-to-one association to ChildTbl
	@OneToMany(mappedBy="parentTbl")
	private List<ChildTbl> childTbls;

	@Column(name="CHILDMAPKEY")
	private String childMapKey;
	
    public ParentTbl() {
    }

    
	public String getChildMapKey() {
		return childMapKey;
	}


	public void setChildMapKey(String childMapKey) {
		this.childMapKey = childMapKey;
	}


	public String getParentid() {
		return this.parentid;
	}

	public void setParentid(String parentid) {
		this.parentid = parentid;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<ChildTbl> getChildTbls() {
		return this.childTbls;
	}

	public void setChildTbls(List<ChildTbl> childTbls) {
		this.childTbls = childTbls;
	}
	
}