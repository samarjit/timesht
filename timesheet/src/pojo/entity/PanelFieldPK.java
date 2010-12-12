package pojo.entity;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the PANEL_FIELDS database table.
 * 
 */
@Embeddable
public class PanelFieldPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="SCR_NAME")
	private String scrName;

	@Column(name="PANEL_NAME")
	private String panelName;

	private String orderno;

    public PanelFieldPK() {
    }
	public String getScrName() {
		return this.scrName;
	}
	public void setScrName(String scrName) {
		this.scrName = scrName;
	}
	public String getPanelName() {
		return this.panelName;
	}
	public void setPanelName(String panelName) {
		this.panelName = panelName;
	}
	public String getOrderno() {
		return this.orderno;
	}
	public void setOrderno(String orderno) {
		this.orderno = orderno;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof PanelFieldPK)) {
			return false;
		}
		PanelFieldPK castOther = (PanelFieldPK)other;
		return 
			this.scrName.equals(castOther.scrName)
			&& this.panelName.equals(castOther.panelName)
			&& this.orderno.equals(castOther.orderno);

    }
    
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.scrName.hashCode();
		hash = hash * prime + this.panelName.hashCode();
		hash = hash * prime + this.orderno.hashCode();
		
		return hash;
    }
}