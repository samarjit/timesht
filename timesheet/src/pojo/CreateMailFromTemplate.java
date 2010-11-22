package pojo;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import util.AMSProperties;

import dao.CreateMailDAO;

/**
 * Mandatory field
 * 
 * template
 * Apart from this propertySet HashMap variable should contain
 * sendto:
 * from:
 * subject:
 * msgbody:
 * 
 * @author SAMARJIT
 *
 */
public class CreateMailFromTemplate {
	
	private void debug(int priority, String s){
		if(priority > 0){
			System.out.println("CreateMail:"+s);
		}
	}
	
	public CreateMailFromTemplate(){
		cmDAO = new CreateMailDAO();
	}
	
private HashMap propertySet;
private String sendto;
private String from;
private String msgbody;
private String template;
private String subject;
CreateMailDAO cmDAO;

public HashMap getPropertySet() {
	return propertySet;
}


public void setPropertySet(HashMap propertySet) {
	this.propertySet = propertySet;
}

private String createSendto(HashMap maildetails){

	String qry1 = (String) maildetails.get("SENDTOQRY");
	String where = (String)maildetails.get("SENDTOQWHERE");
	where = replaceRefWhereClause(where, propertySet);

	String SQL = qry1 + " ";
	if(where!=null && where.length() > 0){
		SQL+=" WHERE "+where;
	}
	//first parameter will be returned assuming that it has sendto value
	String sendtotmp="";
	// System.out.println(SQL+ "  "+SQL.matches(".*(?ims:SELECT).*(?ims:FROM).+"));
	if(SQL != null && !"".equals(SQL) && SQL.matches(".*(?ims:SELECT).*(?ims:FROM).+")){
		//System.out.println("calling with SQL:"+SQL);
		sendtotmp = cmDAO.getSendto(SQL);
	}

	return sendtotmp;
}

private String createBodyTxt(HashMap maildetails){
	String body = (String) maildetails.get("BODYTEXT");	
	return replaceRefWhereClause(body,propertySet);
}

private String getFrom(HashMap maildetails){
	String from = AMSProperties.get("adminemail");
	from = ((String) maildetails.get("FROMTEXT")==null?from:(String) maildetails.get("FROMTEXT"));
	return replaceRefWhereClause(from,propertySet);
}
 		



public void createEmail(String templatename,HashMap propertySet) throws Exception{
	this.propertySet = propertySet;
	
	HashMap maildetails = null;
	try {
		maildetails = cmDAO.findMailDetailsByTemplate(templatename);
	} catch (Exception e) {
		debug(5,"Exception while email creation");
		throw e;
	}
	
	subject = (String) maildetails.get("SUBJECT");
	if(propertySet.containsKey("subject") && ( subject==null || "".equals(subject))){
		subject = (String) propertySet.get("subject"); 
	} 
	
	sendto = createSendto(maildetails);
	if(propertySet.containsKey("sendto") && (sendto == null || "".equals(sendto)) ){
		sendto = (String) propertySet.get("sendto"); 
	} 
	
	from = getFrom(maildetails);
	if(propertySet.containsKey("from") && (from == null || "".equals(from))){
		from = (String) propertySet.get("from"); 
	} 
	msgbody =  createBodyTxt(maildetails);
	if(propertySet.containsKey("msgbody") && (msgbody == null || "".equals(msgbody))){
		msgbody = (String) propertySet.get("msgbody"); 
	} 
	
	
}

public String getSendto() {
	return sendto;
}

public String getFrom() {
	return from;
}

public String getMsgbody() {
	return msgbody;
}

public String getSubject() {
	return subject;
}

public String toString(){
	String str="To:"+sendto+";From:"+from+";Subject:"+subject+";Msgbody:"+msgbody;
	
	return str;
}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		CreateMailFromTemplate cm = new CreateMailFromTemplate();
		HashMap hm = new HashMap();
		hm.put("mgrname", "john");
		hm.put("mgrid", "jh123");
		hm.put("empname", "karthick");
		hm.put("subject", "asd");
		hm.put("sendto", "asd");
		hm.put("msgbody", "sdf");
		try {
			 cm.createEmail("requestNotification",hm);
			 System.out.println(cm);
		} catch (Exception e) {
			e.printStackTrace();
		}
		

	}
	
	/**
	 * This function creates whereclause for the retrieve query 
	 * @param sg
	 * @param tempData
	 * @return where clause
	 */
	private String replaceRefWhereClause(String sg,HashMap<String, String> tempData) {
		debug(0,"in replace where parts tempData hashmap:"+tempData);
		if(sg == null || "".equals(sg))return sg;
		Pattern pattern = Pattern.compile("\\{(.*?)\\}");
	    Matcher matcher  = pattern.matcher(sg);
	    String resultStr = new String(sg);
		while(matcher.find()){
			String str = matcher.group();
			String g = str;
			str = g.substring(1,g.length()-1);  	
			String parts[] = str.split("\\.");
			if(parts.length > 0){
				String val = tempData.get(parts[0]);
			StringBuffer charseq=new StringBuffer(g);
			if(val != null)
			resultStr =  resultStr.replace(charseq, val);
			}
			 
		}
		debug(0,"resultStr:"+resultStr);
		return resultStr;
	}

}
