package model;

import java.io.Serializable;
import java.util.ArrayList;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


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

	@Column(length=10)
	private String childmapkey;

	@Column(length=20)
	private String description;

    public ParentTbl() {
    }

	public String getParentid() {
		return this.parentid;
	}

	public void setParentid(String parentid) {
		this.parentid = parentid;
	}

	public String getChildmapkey() {
		return this.childmapkey;
	}

	public void setChildmapkey(String childmapkey) {
		this.childmapkey = childmapkey;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void addChildTbls(ChildTbl childtbl) {
	 
	}

}