package pojo.entity;

import java.io.Serializable;
import javax.persistence.*;

import org.json.JSONObject;

import java.util.List;


/**
 * The persistent class for the SCREEN database table.
 * 
 */
@Entity
public class Screen implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="SCR_NAME")
	private String scrName="";

	private String businesslogic="";

	private String cssname="";

	private String jsname="";

	private String relatedpanel="";

	@Column(name="SCREEN_TITLE")
	private String screenTitle="";

	private String splwhereclause="";

	@Column(name="TEMPLATE_NAME")
	private String templateName="";

	//bi-directional many-to-one association to PanelField
	@OneToMany(mappedBy="screen")
	private List<PanelField> panelFields;

	//bi-directional many-to-one association to ScreenPanel
	@OneToMany(mappedBy="screen")
	private List<ScreenPanel> screenPanels;

    public Screen() {
    }

	public Screen(JSONObject json) {
		 try {
			 scrName = (String) json.get("scrName");
			 businesslogic = (String) json.get("businesslogic");
			 cssname = (String) json.get("cssname");
			 jsname = (String) json.get("jsname");
			 relatedpanel = (String) json.get("relatedpanel");
			 screenTitle = (String) json.get("screenTitle");
			 splwhereclause = (String) json.get("splwhereclause");
			 templateName = (String) json.get("templateName");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String toString(){
		if(scrName ==null)scrName="";
		if(businesslogic ==null)businesslogic="";
		if(cssname ==null)cssname="";
		if(jsname ==null)jsname="";
		if(relatedpanel ==null)relatedpanel="";
		if(screenTitle ==null)screenTitle="";
		if(splwhereclause ==null)splwhereclause="";
		if(templateName ==null)templateName="";
		
		return "<tr><td>"+scrName+"</td><td>"+businesslogic+"</td><td>"+cssname+"</td><td>"+jsname+"</td><td>"+relatedpanel+"</td><td>"+screenTitle+"</td><td>"+splwhereclause+"</td><td>"+templateName+"</td></tr>";
	}
	
	public String getHeaderNames(){
		return "<tr><td class='scrName'>Screen Name</td><td class='businesslogic'>Business Logic</td><td class='cssname'>Css Name</td><td class='jsname'>JS Name</td><td class='relatedpanel'>Relatedpanel</td><td class='screenTitle'>Screen Title</td><td class='splwhereclause'>Spl Whereclause</td><td class='templateName'>TemplateName</td></tr>";
	}
	
	public String getListAttr(){
		return "scrName,businesslogic,cssname,jsname,relatedpanel,screenTitle,splwhereclause,templateName";
	}
	public String getPrimaryKeys(){
		return "scrName";
	}
	
	
	
	
	public String getScrName() {
		return this.scrName;
	}

	public void setScrName(String scrName) {
		this.scrName = scrName;
	}

	public String getBusinesslogic() {
		return this.businesslogic;
	}

	public void setBusinesslogic(String businesslogic) {
		this.businesslogic = businesslogic;
	}

	public String getCssname() {
		return this.cssname;
	}

	public void setCssname(String cssname) {
		this.cssname = cssname;
	}

	public String getJsname() {
		return this.jsname;
	}

	public void setJsname(String jsname) {
		this.jsname = jsname;
	}

	public String getRelatedpanel() {
		return this.relatedpanel;
	}

	public void setRelatedpanel(String relatedpanel) {
		this.relatedpanel = relatedpanel;
	}

	public String getScreenTitle() {
		return this.screenTitle;
	}

	public void setScreenTitle(String screenTitle) {
		this.screenTitle = screenTitle;
	}

	public String getSplwhereclause() {
		return this.splwhereclause;
	}

	public void setSplwhereclause(String splwhereclause) {
		this.splwhereclause = splwhereclause;
	}

	public String getTemplateName() {
		return this.templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public List<PanelField> getPanelFields() {
		return this.panelFields;
	}

	public void setPanelFields(List<PanelField> panelFields) {
		this.panelFields = panelFields;
	}
	
	public List<ScreenPanel> getScreenPanels() {
		return this.screenPanels;
	}

	public void setScreenPanels(List<ScreenPanel> screenPanels) {
		this.screenPanels = screenPanels;
	}
	
	
	
}