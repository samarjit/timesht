package pojo.entities;

import java.io.Serializable;
import javax.persistence.*;
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
	private String scrName;

	private String businesslogic;

	private String cssname;

	private String jsname;

	private String relatedpanel;

	@Column(name="SCREEN_TITLE")
	private String screenTitle;

	private String splwhereclause;

	@Column(name="TEMPLATE_NAME")
	private String templateName;

	//bi-directional many-to-one association to ScreenPanel
	@OneToMany(mappedBy="screen")
	private List<ScreenPanel> screenPanels;

    public Screen() {
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

	public List<ScreenPanel> getScreenPanels() {
		return this.screenPanels;
	}

	public void setScreenPanels(List<ScreenPanel> screenPanels) {
		this.screenPanels = screenPanels;
	}
	
}