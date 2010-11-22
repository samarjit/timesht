package actionclass;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import java.io.InputStream;
import java.io.StringBufferInputStream;
import java.util.*;
import javax.activation.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.naming.*; 

import com.opensymphony.xwork2.ActionSupport;

/**
 * This is a action class that inherits Strut's Framework's ActionSupport class and is used to perform the send mail operation. 
 *
 */

public class SendMailAC extends ActionSupport{
private String sendto;
private String from;
private String subject;
private String msgbody;
private InputStream inputStream;


public InputStream getInputStream() {
    return inputStream;
}

private void debug(int priority, String s){
	if(priority > 1){
		System.out.println("SendMail:"+s);
	}
}
	
public String getSendto() {
	return sendto;
}



public void setSendto(String sendto) {
	this.sendto = sendto;
}



public String getFrom() {
	return from;
}



public void setFrom(String from) {
	this.from = from;
}



public String getSubject() {
	return subject;
}



public void setSubject(String subject) {
	this.subject = subject;
}


public String getMsgbody() {
	return msgbody;
}

public void setMsgbody(String msgbody) {
	this.msgbody = msgbody;
}
/**
 * This function is executed by default
 * 
 * @return JSON object containing {'ERROR':errm or 'SUCCESS':''} 
 * if 'ERROR' is returned ignore 'SUCCESS' 
 * 
 *
 */
public String execute(){
	 String resultHtml = "{\"SUCCESS\":\"Email Sent\"}";
	 String errorm = "";
	
	//Validation of fields
	if(from == null){
		errorm += "From(Sender) missing\n";
	}else if(sendto == null){
		errorm += "Send To (Recepient) missing\n";
	}else if(msgbody == null){
		errorm += "Email text is missing\n";
	}else if(subject == null){
		errorm += "Subject) missing\n";
	}
	
	 
	if ("".equals(errorm)) {
		if(sendJavaMail() == false)
		resultHtml = "{\"ERROR\":\"Email Sending Failed\"}";
	}else{//Error: validation failed!
		resultHtml = "{\"ERROR\":\""+errorm+"\"}";
	}
	inputStream = new StringBufferInputStream(resultHtml);
	return SUCCESS;
}

/**
 * This function is used to send mail.
 * @return boolean
 * @param sendto, from, subject, msgtext
 */
public boolean sendJavaMail(){
	boolean res = true;
	InitialContext ic;
	String snName = "java:comp/env/mail/MyMailSession";
	Session session = null;
	
	try {
		ic = new InitialContext();
		session = (Session) ic.lookup(snName);
	} catch (Exception e) {
		debug(5, "Exception: JNDI failed!");
	}
	if (session == null) {
		debug(0, "Using non JNDI way");
		Properties props = System.getProperties();
		props.put("mail.from", from);
		session = Session.getInstance(props, null);
	}
	Message msg = new MimeMessage(session);
	try {
		msg.setSubject(subject);
		msg.setSentDate(new Date());
		msg.setFrom();
		 
		msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(
				sendto, false));
		msg.setContent(msgbody,"text/html");
		Transport.send(msg);

		debug(1, "Email send to:" + sendto + msg.getSubject() + " from:"
				+ from + " sub:" + msg.getSubject());
	} catch (Exception e) {
		debug(5, "Exception in sending mail!");
		e.printStackTrace();
		res = false;
	}
	return res;
}
	/*
	public static void main(String[] args) {
		InitialContext ic;
		String snName = "java:comp/env/mail/MyMailSession";
		
		Session session = null;
		try {
			ic = new InitialContext();
			session = (Session) ic.lookup(snName);
		} catch (Exception e) {
			System.out.println("Exception: JNDI failed!");
		}  
		if(session == null){
			System.out.println("Using non JNDI way");
			Properties props = System.getProperties();
			props.put("mail.from", "admin@mydomain.com"); 
			session = Session.getInstance(props, null);
		}
		
		Message msg = new MimeMessage(session);
		String msgSubject = "Some arbtirary subject";
		String msgRecipient = "sam@mydomain.com";
		String msgTxt = "Hello this is my custom email";
		
		try {
			msg.setSubject(msgSubject);
			msg.setSentDate(new Date());
			msg.setFrom();
			msg.setRecipients(Message.RecipientType.TO, InternetAddress
					.parse(msgRecipient, false));
			msg.setText(msgTxt);
			Transport.send(msg);
		} catch (Exception e) {
			System.out.println("Exception in sending mail!");
		}
	} */

}
