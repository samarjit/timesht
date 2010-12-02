package pojo.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the PANEL_FIELDS database table.
 * 
 */
@Entity
@Table(name="PANEL_FIELDS")
public class PanelField implements Serializable {
	private static final long serialVersionUID = 1L;

	private String autogen;

	private String classname;

	private String datatype;

	private String dbcol;

	@Column(name="DBCOL_SIZ")
	private String dbcolSiz;

	@Column(name="ELEM_ATTRIB")
	private String elemAttrib;

	private String fname;

	private String htmlelm;

	private String idname;

	private String lblname;

	private String ncol;

	private String nrow;

	private String orderno;

	private String prkey;

	@Column(name="STORE_FLG")
	private String storeFlg;

	private String strquery;

	@Column(name="VALID_MSG")
	private String validMsg;

	@Column(name="VALID_RULE")
	private String validRule;

	private String validation;

	//bi-directional many-to-one association to ScreenPanel
    @ManyToOne
	@JoinColumns({
		@JoinColumn(name="PANEL_NAME", referencedColumnName="PANEL_NAME"),
		@JoinColumn(name="SCR_NAME", referencedColumnName="SCR_NAME")
		})
	private ScreenPanel screenPanel;

    public PanelField() {
    }

	public String getAutogen() {
		return this.autogen;
	}

	public void setAutogen(String autogen) {
		this.autogen = autogen;
	}

	public String getClassname() {
		return this.classname;
	}

	public void setClassname(String classname) {
		this.classname = classname;
	}

	public String getDatatype() {
		return this.datatype;
	}

	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}

	public String getDbcol() {
		return this.dbcol;
	}

	public void setDbcol(String dbcol) {
		this.dbcol = dbcol;
	}

	public String getDbcolSiz() {
		return this.dbcolSiz;
	}

	public void setDbcolSiz(String dbcolSiz) {
		this.dbcolSiz = dbcolSiz;
	}

	public String getElemAttrib() {
		return this.elemAttrib;
	}

	public void setElemAttrib(String elemAttrib) {
		this.elemAttrib = elemAttrib;
	}

	public String getFname() {
		return this.fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	public String getHtmlelm() {
		return this.htmlelm;
	}

	public void setHtmlelm(String htmlelm) {
		this.htmlelm = htmlelm;
	}

	public String getIdname() {
		return this.idname;
	}

	public void setIdname(String idname) {
		this.idname = idname;
	}

	public String getLblname() {
		return this.lblname;
	}

	public void setLblname(String lblname) {
		this.lblname = lblname;
	}

	public String getNcol() {
		return this.ncol;
	}

	public void setNcol(String ncol) {
		this.ncol = ncol;
	}

	public String getNrow() {
		return this.nrow;
	}

	public void setNrow(String nrow) {
		this.nrow = nrow;
	}

	public String getOrderno() {
		return this.orderno;
	}

	public void setOrderno(String orderno) {
		this.orderno = orderno;
	}

	public String getPrkey() {
		return this.prkey;
	}

	public void setPrkey(String prkey) {
		this.prkey = prkey;
	}

	public String getStoreFlg() {
		return this.storeFlg;
	}

	public void setStoreFlg(String storeFlg) {
		this.storeFlg = storeFlg;
	}

	public String getStrquery() {
		return this.strquery;
	}

	public void setStrquery(String strquery) {
		this.strquery = strquery;
	}

	public String getValidMsg() {
		return this.validMsg;
	}

	public void setValidMsg(String validMsg) {
		this.validMsg = validMsg;
	}

	public String getValidRule() {
		return this.validRule;
	}

	public void setValidRule(String validRule) {
		this.validRule = validRule;
	}

	public String getValidation() {
		return this.validation;
	}

	public void setValidation(String validation) {
		this.validation = validation;
	}

	public ScreenPanel getScreenPanel() {
		return this.screenPanel;
	}

	public void setScreenPanel(ScreenPanel screenPanel) {
		this.screenPanel = screenPanel;
	}
	
}