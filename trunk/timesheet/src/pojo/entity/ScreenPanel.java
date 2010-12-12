package pojo.entity;

import java.io.Serializable;
import javax.persistence.*;

import org.json.JSONObject;


/**
 * The persistent class for the SCREEN_PANEL database table.
 * 
 */
@Entity
@Table(name="SCREEN_PANEL")
public class ScreenPanel implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private ScreenPanelPK id;

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

    public ScreenPanel() {
    }

	public ScreenPanel(JSONObject json) {
		try {
			if(id == null)id = new ScreenPanelPK();
			id.setScrName((String)json.get("id_scrName"));
			id.setPanelName((String)json.get("id_panelName"));
		cssClass = (String)json.get("cssClass");
		paneltype = (String)json.get("paneltype");
		pkName = (String)json.get("pkName");
		relatedpanel = (String)json.get("relatedpanel");
		rwFlg = (String)json.get("rwFlg");
		selquery = (String)json.get("selquery");
		sortorder = (String)json.get("sortorder");
		splwhereclause = (String)json.get("splwhereclause");
		tableName = (String)json.get("tableName");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ScreenPanelPK getId() {
		return this.id;
	}

	public void setId(ScreenPanelPK id) {
		this.id = id;
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
	public String toString(){
		return "<tr><td>"+id.getScrName()+"</td><td>"+id.getPanelName()+"</td><td>"+cssClass+"</td><td>"+paneltype+"</td><td>"+pkName+"</td><td>"+relatedpanel+"</td><td>"+rwFlg+"</td><td>"+selquery+"</td><td>"+sortorder+"</td><td>"+splwhereclause+"</td><td>"+tableName+"</td></tr>";
	}
	
	public String getHeaderNames(){
		return "<tr><td class='id_scrName'>Screen Name</td><td class='id_panelName'>Panel Name</td><td class='cssClass'>Css Class</td><td class='paneltype'>Paneltype</td><td class='pkName'>Pk Name</td><td class='relatedpanel'>Relatedpanel</td><td class='rwFlg'>Rw Flg</td><td class='selquery'>Selquery</td><td class='sortorder'>Sortorder</td><td class='splwhereclause'>Splwhereclause</td><td class='tableName'>Table Name</td></tr>";
	}
	
	public String getListAttr(){
		return "id_scrName, id_panelName,cssClass,paneltype,pkName,relatedpanel,rwFlg,selquery,sortorder,splwhereclause,tableName";
	}
	public String getPrimaryKeys(){
		return "id";
	}
}