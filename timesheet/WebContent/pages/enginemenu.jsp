<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%String ctxstr = request.getContextPath(); %>
<link rel="stylesheet" href="<%=ctxstr %>/css/button.css" type="text/css" />
<link rel="stylesheet" href="<%=ctxstr %>/css/header.css" type="text/css" />

<script language="JavaScript" src="../js/jquery.js"></script>
<script language="JavaScript" src="../js/json2.js"></script>
<script language="JavaScript" src="../js/jquery-ui-1.7.2.custom.min.js"></script> 
<style type="text/css">
@import "<%=ctxstr %>/css/timesheet.css"; 
@import "<%=ctxstr %>/css/button.css";
@import "<%=ctxstr %>/css/header.css"; 

</style>

<script>
var ctxstr = "<%=ctxstr %>";
function getMenuList() {
	var strURL= ctxstr+"/engineajax.action?command=selectallmenu";
			
$.ajax({type:"GET", url: strURL, success:  function (val){
var json  = JSON.parse(val);
$('#menu_list table').html(json.listtabledata);
} });	
}

function add(){
	
}
</script>

</head>
<body>
<%@ include file="header.jsp" %>

<div id="screen_title">${screenTitle }</div>

<div id="page">
Engine
<input type="button" value="getList" onclick="getMenuList()" />
<input type="button" value="add" onclick="add()" />
<input type="button" value="save" onclick="save()" />
<input type="button" value="update" onclick="update()" />
<input type="button" value="delete" onclick="delete()" />

<div id="panelFieldsmenu">
<form action="">
Menu Id <input name="menuId"/><br/>
Menu Name <input name="menuName"/><br/>
Menu Role Id <input name="menuRoleId"/><br/>
Menu Action <input name="menuAction"/><br/>
</form>
</div>
<div id="menu_list" class="list"> <table></table></div>
</div>
</body>
</html>