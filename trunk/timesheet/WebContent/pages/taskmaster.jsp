<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags" %>   
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Task Master</title>
<%String ctxstr = request.getContextPath(); %>
<style>
@import "<%=ctxstr %>/css/button.css";
@import "<%=ctxstr %>/css/header.css"; 
</style>
<script>
var formName = "frmTaskMaster";	
</script>

</head>
<body>
<%@include file="header.jsp"%>
<table>
    <tr>
        <td>
            <div id="searchFields">
            	<form action="" method="" accept-charset="utf-8">
	  
</form>
            </div>
        </td>
    </tr>
    <tr>
        <td>
            <div id="panelFields">
            </div>
        </td>
        <td>
            <div id="buttonPanel">
                <input type="submit" value="Search" />
				<button onclick="">
                </button>
            </div>
        </td>
    </tr>
</table>
<div id="list">
	
	
</div>

</body>
</html>