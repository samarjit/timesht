<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/struts-jquery-tags" prefix="sj"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
 
 
<%String ctxstr = request.getContextPath(); %>

 
</head>
<body>
<s:actionerror/>
<s:actionmessage/>
<h1>Login</h1>
<form action="login.action">
User Id: <input type="text" name="userid" /><br/>
Passwd : <input type="password" name="passwd" /><br/>
<button type="submit">submit</button>
</form>
</body>
</html>
