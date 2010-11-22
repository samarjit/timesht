package actionclass;

import java.io.InputStream;
import java.io.StringBufferInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import pojo.VendorMap;

import com.opensymphony.xwork2.ActionSupport;

public class VendorMapAC extends ActionSupport{
	public void debug(int priority, String s){
		s="VendorMapAC:"+s;
		if(priority >0)
		System.out.println(s);
	}
	
private String mode;
private String command;
private String rfqid;
private String vendorid;
private String typenotify;
private String suggestdelvtime;
private Map vendorList;
private String department;
private String indvstatus;
private String rfqstatus;
 
public String getIndvstatus() {
	return indvstatus;
}
public void setIndvstatus(String indvstatus) {
	this.indvstatus = indvstatus;
}
public String getDepartment() {
	return department;
}
public void setDepartment(String department) {
	this.department = department;
}
public String getTypenotify() {
	return typenotify;
}
public void setTypenotify(String typenotify) {
	this.typenotify = typenotify;
}
public String getSuggestdelvtime() {
	return suggestdelvtime;
}
public void setSuggestdelvtime(String suggestdelvtime) {
	this.suggestdelvtime = suggestdelvtime;
}
public String getVendorid() {
	return vendorid;
}
public void setVendorid(String vendorid) {
	this.vendorid = vendorid;
}
private InputStream inputStream;


public String getMode() {
	return mode;
}
public void setMode(String mode) {
	this.mode = mode;
}
public String getCommand() {
	return command;
}
public void setCommand(String command) {
	this.command = command;
}
public String getRfqid() {
	return rfqid;
}
public void setRfqid(String rfqid) {
	this.rfqid = rfqid;
}
public InputStream getInputStream() {
    return inputStream;
}



/* (non-Javadoc)
 * @see com.opensymphony.xwork2.ActionSupport#execute()
 */
public String execute(){
	
	String resultHtml = null;
	rfqstatus = "";
	debug(1,command);
	if(command.equals("insert")){
		resultHtml = insert();
	}else if(command.equals("delete")){
		resultHtml = delete();
	}else if(command.equals("initialmap")){	
		resultHtml = initialMap();
	}else if(command.equals("selectall")){	
		resultHtml = selectAll();
	}else if(command.equals("vendorlist")){
		resultHtml = getVendorList();
	}else if(command.equals("updatetypenotify")){
		resultHtml = updateTypeNotify();
	}else if(command.equals("enableprint")){
		resultHtml = updateTypeNotify();
	}else if(command.equals("sendemail")){
		resultHtml = updateTypeNotify();
	}else if(command.equals("sendprint")){
		resultHtml = updateTypeNotify();
	}
	
	inputStream = new StringBufferInputStream(resultHtml);
	
	return SUCCESS;
}

/**
 * updates type of notification and finally calls selectAll(){@link VendorMapAC.selectAll} to select the latest status
 * @return json containing ERROR if any or RFQSTATUSUPDATE if all the vendors have been mapped and RFQ status needs to change
 * 
 */
private String updateTypeNotify() {
	VendorMap vendor = new VendorMap();
	
	String str = vendor.updateTypeNotify(rfqid,vendorid,typenotify,indvstatus);
	if("SUCCESS".equals(str)){
		return selectAll();
	}else if(str.indexOf("RFQSTATUSUPDATE") >-1 ){
		rfqstatus = str.substring(16);
		return selectAll();
	}else {
		return "{\"ERROR\":\"Record Insert failed\"}";
	}
}


/**
 * Inserts a record for mapping and later selects the latest mapping status to be displayed
 * @return json ERROR if any otherwise selectALL results
 */
private String insert(){
	VendorMap vendor = new VendorMap();
	debug(1,"type notify:"+typenotify+"suggestdelvtime:"+suggestdelvtime);
	String str = vendor.insert(rfqid,vendorid,typenotify,suggestdelvtime);
	if("SUCCESS".equals(str)){
		return selectAll();
	}else{
		return "{\"ERROR\":\"Record Insert failed\"}";
	}
}

/**
 * Deletes a mapping and calls selectAll()
 * @return json ERROR if any otherwise selectALL results
 */
private String delete(){
	VendorMap vendor = new VendorMap();
	debug(1,"delete: rfqid"+rfqid+"vendorid:"+vendorid);
	String str = vendor.delete(rfqid,vendorid);
	if("SUCCESS".equals(str)){
		return selectAll();
	}else{
		return "{\"ERROR\":\"Record Delete failed\"}";
	}
}

/**
 * Maps all the vendors initially to RFQ of a particular department so valid department must be in object scope
 * while calling this method
 * @return json ERROR if any otherwise selectALL results
 */
private String initialMap(){
	VendorMap vendor = new VendorMap();
	debug(1,"initialMap: rfqid"+rfqid+"department:"+department+"type notify:"+typenotify+"suggestdelvtime:"+suggestdelvtime);
	String str = vendor.initialMap(rfqid,department,typenotify,suggestdelvtime);
	if("SUCCESS".equals(str)){
		return selectAll();
	}else{
		return "{\"ERROR\":\"Initial Mapping failed\"}";
	}
}
/**
 * @return json with all the status of selected vendors 
 */
private String selectAll(){
	VendorMap vendor = new VendorMap();
	ArrayList<HashMap<String,String>>  ar = vendor.setlectAll(rfqid);
	JSONObject jobj = new JSONObject();
	try {
		jobj.put("SELECTDATA", ar);
		jobj.put("ERROR", "");
		jobj.put("RFQSTATUSUPDATE", rfqstatus);
		rfqstatus = "";
	} catch (Exception e) {
		debug(5,"Return JSON creation error");
	}
	String retval = (jobj).toString();
	debug(1,"selectAll:"+retval);
	return retval;
 
}

/**
 * Used to fill in dependent dropdown based on department selected 
 * @return XML with vendor list based on a particular department
 */
private String getVendorList(){
	VendorMap vendor = new VendorMap();
	String retStr = "";
	 Map vlist = vendor.getVendorList(department);
	 JSONObject jobj = new JSONObject(vlist);
	 debug(1,jobj.toString());
	 Iterator itr = vlist.keySet().iterator();
	 while(itr.hasNext()){
		 String k = (String)itr.next();
		 String v = (String)vlist.get(k);
		 retStr+="<tr><key>"+k+"</key><val>"+v+"</val></tr>";
	 } 
	 return "<data>"+retStr+"</data>";
}

 
}
