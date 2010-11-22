<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<% response.setHeader("Pragma","no-cache");
  response.setDateHeader("Expires",0);
  response.setHeader("Cache-Control","no-cache");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Login</title>
<%String ctxstr = request.getContextPath(); %>
<link rel="stylesheet" href="<%=ctxstr %>/css/login.css" type="text/css" />
<link rel="stylesheet" href="<%=ctxstr %>/css/jquery-ui-1.7.2.custom.css" type="text/css" />
<STYLE>
@import "<%=ctxstr %>/css/button.css";
</STYLE>
</head>
<SCRIPT LANGUAGE="JavaScript">
function fnsubmit() {
document.getElementById("formId0").submit();
}
</SCRIPT>
<body>
<br />
 
		 
<c:if test="${not empty param.errormsg}">		
<div id="errormsgdiv" class="ui-state-error ui-corner-all"  style="padding: 0pt 0.7em;">
<p><span style="float: left;margin-left: 0.3em;  margin-right: 0.3em;" class="ui-icon ui-icon-alert"></span>
${param.errormsg}</p>
</div> 
</c:if>
<br/>
<FORM METHOD="GET" ACTION="${pageContext.servletContext.contextPath}/ScreenFlowControllerServlet" id="formId0">
<input type="hidden" name="screenflowname" value="loginflow" />
<input type="hidden" name="currentaction" value="start"/> 
<div id="itsthetable" style="width:auto"> 
<TABLE id="login">
<thead>
<TR><TH colspan="2" >Login</TH></TR></thead>
<tr><td>User ID</td><td><input type="text" name="userid"  /></td></tr>
<tr><td>Password </td><td>
<input type="password" value="" name="password" /> </td></tr>
<tr><td><button href="#" class="button"   onclick="javascript:document.forms[0].reset();this.blur();" ><span>Reset</span></button> 
</td><td><button href="#" class="button"   onclick="fnsubmit();this.blur();" ><span>Login</span></button></td></tr>
</TABLE> 
</div>
</FORM>

|<a href ="${pageContext.servletContext.contextPath}/index2.jsp">index2 for debug</a>|
</body>
</html>