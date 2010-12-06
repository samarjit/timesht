package pojo.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the SCREEN_PANEL database table.
 * 
 */
@Embeddable
public class ScreenPanelPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="SCR_NAME",insertable=false, updatable=false)
	private String scrName;

	@Column(name="PANEL_NAME",insertable=false, updatable=false)
	private String panelName;

    public ScreenPanelPK() {
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

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof ScreenPanelPK)) {
			return false;
		}
		ScreenPanelPK castOther = (ScreenPanelPK)other;
		return 
			this.scrName.equals(castOther.scrName)
			&& this.panelName.equals(castOther.panelName);

    }
    
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.scrName.hashCode();
		hash = hash * prime + this.panelName.hashCode();
		
		return hash;
    }
}