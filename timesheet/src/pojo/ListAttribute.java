package pojo;

public class ListAttribute{
	//lblname,fname,idname,dbcol,datatype,classname,prkey
private String 	lblname;
private String fname;
private String idname;
private String datatype;
private String dbcol;
private String classname;
private String prkey;
public String getLblname() {
	return lblname;
}
public void setLblname(String lblname) {
	this.lblname = lblname;
}
public String getFname() {
	return fname;
}
public void setFname(String fname) {
	this.fname = fname;
}
public String getIdname() {
	return idname;
}
public void setIdname(String idname) {
	this.idname = idname;
}
public String getDatatype() {
	return datatype;
}
public void setDatatype(String datatype) {
	this.datatype = datatype;
}
public String getDbcol() {
	return dbcol;
}
public void setDbcol(String dbcol) {
	this.dbcol = dbcol;
}
public String getClassname() {
	return classname;
}
public void setClassname(String classname) {
	this.classname = classname;
}
public String getPrkey() {
	return prkey;
}
public void setPrkey(String prkey) {
	this.prkey = prkey;
}

public String toString(){
	return lblname+","+fname+","+idname+","+dbcol+","+datatype+","+classname+","+prkey;
}
}