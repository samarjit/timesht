package pojo.entity;

import java.io.Serializable;
import javax.persistence.*;

import org.json.JSONObject;


/**
 * The persistent class for the PANEL_FIELDS database table.
 * 
 */
@Entity
@Table(name="PANEL_FIELDS")
public class PanelField implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private PanelFieldPK id;

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

	private String prkey;

	@Column(name="STORE_FLG")
	private String storeFlg;

	private String strquery;

	@Column(name="VALID_MSG")
	private String validMsg;

	@Column(name="VALID_RULE")
	private String validRule;

	private String validation;

    public PanelField() {
    }

	public PanelField(JSONObject json) {
		try {
			System.out.println("Constructor PanelFields:json"+json.get("id_scrName"));
			if(id == null)id = new PanelFieldPK();
			id.setScrName((String)json.get("id_scrName"));
			id.setPanelName((String)json.get("id_panelName"));
			id.setOrderno((String)json.get("id_orderno"));
			autogen = (String)json.get("autogen");
			classname = (String)json.get("classname");
			datatype = (String)json.get("datatype");
			dbcol = (String)json.get("dbcol");
			dbcolSiz = (String)json.get("dbcolSiz");
			elemAttrib = (String)json.get("elemAttrib");
			fname = (String)json.get("fname");
			htmlelm = (String)json.get("htmlelm");
			idname = (String)json.get("idname");
			lblname = (String)json.get("lblname");
			ncol = (String)json.get("ncol");
			nrow = (String)json.get("nrow");
			prkey = (String)json.get("prkey");
			storeFlg = (String)json.get("storeFlg");
			strquery = (String)json.get("strquery");
			validMsg = (String)json.get("validMsg");
			validRule = (String)json.get("validRule");
			validation = (String)json.get("validation");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public PanelFieldPK getId() {
		return this.id;
	}

	public void setId(PanelFieldPK id) {
		this.id = id;
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
	
	public String toString(){
		return "<tr><td>"+id.getScrName()+"</td><td>"+id.getPanelName()+"</td><td>"+id.getOrderno()+"</td><td>"+autogen+"</td><td>"+classname+"</td><td>"+datatype+"</td><td>"+dbcol+"</td><td>"+dbcolSiz+"</td><td>"+elemAttrib+"</td><td>"+fname+"</td><td>"+htmlelm+"</td><td>"+idname+"</td><td>"+lblname+"</td><td>"+ncol+"</td><td>"+nrow+"</td><td>"+prkey+"</td><td>"+storeFlg+"</td><td>"+strquery+"</td><td>"+validMsg+"</td><td>"+validRule+"</td><td>"+validation+"</td></tr>";
	}
	
	public String getHeaderNames(){
		return "<tr><td class='id_scrName'>Screen Name</td><td class='id_panelName'>Panel Name</td><td class='id_orderno'>Order No</td><td class='autogen'>Autogen</td><td class='classname'>Classname</td><td class='datatype'>Datatype</td><td class='dbcol'>Dbcol</td><td class='dbcolSiz'>Dbcol Siz</td><td class='elemAttrib'>Elem Attrib</td><td class='fname'>Fname</td><td class='htmlelm'>Htmlelm</td><td class='idname'>Idname</td><td class='lblname'>Lblname</td><td class='ncol'>Ncol</td><td class='nrow'>Nrow</td><td class='prkey'>Prkey</td><td class='storeFlg'>Store Flg</td><td class='strquery'>Strquery</td><td class='validMsg'>Valid Msg</td><td class='validRule'>Valid Rule</td><td class='validation'>Validation</td></tr>";
	}
	
	public String getListAttr(){
		return "id_scrName, id_panelName,id_orderno, autogen,classname,datatype,dbcol,dbcolSiz,elemAttrib,fname,htmlelm,idname,lblname,ncol,nrow,prkey,storeFlg,strquery,validMsg,validRule,validation";
	}
	public String getPrimaryKeys(){
		return "id";
	}
}