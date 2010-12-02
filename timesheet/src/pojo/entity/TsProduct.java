package pojo.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the TS_PRODUCT database table.
 * 
 */
@Entity
@Table(name="TS_PRODUCT")
public class TsProduct implements Serializable {
	private static final long serialVersionUID = 1L;

	//bi-directional many-to-one association to TsTaskmaster
	@OneToMany(mappedBy="tsProduct")
	private List<TsTaskmaster> tsTaskmasters;

    public TsProduct() {
    }

	public List<TsTaskmaster> getTsTaskmasters() {
		return this.tsTaskmasters;
	}

	public void setTsTaskmasters(List<TsTaskmaster> tsTaskmasters) {
		this.tsTaskmasters = tsTaskmasters;
	}
	
}