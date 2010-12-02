package pojo.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the SCREEN_PANEL database table.
 * 
 */
@Entity
@Table(name="SCREEN_PANEL")
public class ScreenPanel implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="CSS_CLASS")
	private String cssClass;

	private String paneltype;

	@Column(name="PK_NAME")
	private String pkName;

	private String relatedpanel;

	@Column(name="RW_FLG")
	private String rwFlg;

	private String selquery;

	private String sortorder;

	private String splwhereclause;

	@Column(name="TABLE_NAME")
	private String tableName;

	//bi-directional many-to-one association to PanelField
	@OneToMany(mappedBy="screenPanel")
	private List<PanelField> panelFields;

	//bi-directional many-to-one association to Screen
    @ManyToOne
	@JoinColumn(name="SCR_NAME")
	private Screen screen;

    public ScreenPanel() {
    }

	public String getCssClass() {
		return this.cssClass;
	}

	public void setCssClass(String cssClass) {
		this.cssClass = cssClass;
	}

	public String getPaneltype() {
		return this.paneltype;
	}

	public void setPaneltype(String paneltype) {
		this.paneltype = paneltype;
	}

	public String getPkName() {
		return this.pkName;
	}

	public void setPkName(String pkName) {
		this.pkName = pkName;
	}

	public String getRelatedpanel() {
		return this.relatedpanel;
	}

	public void setRelatedpanel(String relatedpanel) {
		this.relatedpanel = relatedpanel;
	}

	public String getRwFlg() {
		return this.rwFlg;
	}

	public void setRwFlg(String rwFlg) {
		this.rwFlg = rwFlg;
	}

	public String getSelquery() {
		return this.selquery;
	}

	public void setSelquery(String selquery) {
		this.selquery = selquery;
	}

	public String getSortorder() {
		return this.sortorder;
	}

	public void setSortorder(String sortorder) {
		this.sortorder = sortorder;
	}

	public String getSplwhereclause() {
		return this.splwhereclause;
	}

	public void setSplwhereclause(String splwhereclause) {
		this.splwhereclause = splwhereclause;
	}

	public String getTableName() {
		return this.tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public List<PanelField> getPanelFields() {
		return this.panelFields;
	}

	public void setPanelFields(List<PanelField> panelFields) {
		this.panelFields = panelFields;
	}
	
	public Screen getScreen() {
		return this.screen;
	}

	public void setScreen(Screen screen) {
		this.screen = screen;
	}
	
}