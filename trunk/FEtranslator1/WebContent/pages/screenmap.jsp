<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/struts-jquery-tags" prefix="sj"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<s:set name="theme" value="'simple'" scope="page" />
<s:head/>
 
<%String ctxstr = request.getContextPath(); %>

 
</head>
<body>

  
 Context()=<s:property value="#resultDTO.data.form1[0].countryofissue" /> <br/>
 set()=<s:property value="resultDTO.data.form1[0].countryofissue" /> <br/>
 stack.push() <s:property value="data.form1[0].countryofissue" /> <br/>

Screen Name=<s:property value="screenroot.screen.name"/><br/>
Form:<s:form action="jaxbtest.action">
Screen Name:<s:textfield name="screenroot.screen.name"/><br/>
HTML Template Path:<s:textfield name="screenroot.screen.htmltemplate"/><br/>
Include JSP:<s:textfield name="screenroot.screen.includedjsp"/><br/>
Callback Class:<s:textfield name="screenroot.screen.callbackclass"/><br/>
Scripts:<s:textfield name="screenroot.screen.scriptsOrStylesheets[0]"  maxlength="200"/><br/>
Scriptinclude: <s:textfield name="screenroot.screen.scriptsOrStylesheets" maxlength="200"/><br/>
<s:iterator value="screenroot.screen.scriptsOrStylesheets[0].Scripts"><br/>
  <p>day is: <s:property value ="scriptinclude"  /></p>
  <s:iterator value="scriptinclude"><br/>
  <p>included script: <s:property /></p>
</s:iterator>
</s:iterator><br/>
<s:property value="screenroot.panels.panel[0].id"/><br/>
<button>Submit Change</button>
</s:form>
</body>
</html>
