package pojo.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the TS_PROJECT database table.
 * 
 */
@Entity
@Table(name="TS_PROJECT")
public class TsProject implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	private String project;
	
	
	

	//bi-directional many-to-one association to TsTaskmaster
	@OneToMany(mappedBy="tsProject")
	private List<TsTaskmaster> tsTaskmasters;

    public TsProject() {
    }

	public List<TsTaskmaster> getTsTaskmasters() {
		return this.tsTaskmasters;
	}

	public void setTsTaskmasters(List<TsTaskmaster> tsTaskmasters) {
		this.tsTaskmasters = tsTaskmasters;
	}
	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}
}