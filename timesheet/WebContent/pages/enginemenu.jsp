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
$('#menu_list .idlist').text(json.listattrs);
$('#menu_list .pklist').text(json.primarykeys);
addSelectEvents("menu_list");
} });	
}

function add(){
	var json = '{"subcmd":"add","entitydata":{';
	$("#panelFieldsmenu_list" ).find("input").each(function(){
		json +='"'+$(this).attr("name")+'":"'+ this.value+'",';
	});
	json.substring(json.length -1);
	json +='}}';
	var strURL= ctxstr+"/engineajax.action?command=menucrud&data="+json;
	alert(strURL);
	$.ajax({type:"GET", url: strURL, success:  function (val){
			alert("add result:"+val)
		}
	});
}

function copyToFields(paramElm, selIdx) {
	
	var idlist = $("#"+paramElm+" .idlist").text().split(",");
	var pklist = $("#"+paramElm+" .pklist").text().split(",");
	var tableelm = $("#"+paramElm+" table").get(0);
	$.each(tableelm.rows[selIdx].cells,function (index, val){
		 
		$("#panelFieldsmenu_list" ).find("input[name="+idlist[index]+"]").val($(val).text());
	});
	
}
function addSelectEvents(param){
	 
	var srchdv = document.getElementById(param).getElementsByTagName("TR");

	for (var i =0;i< srchdv.length;i++) {
	if(srchdv[i].childNodes[0].tagName != "TH"){ 
			srchdv[i].onclick=function(p){
			 	cleanUp(this);
			 	selectedIdx[param]  = this.rowIndex;
			  	this.style.backgroundColor= "#D6F1A3";
				copyToFields(param,selectedIdx[param] );
				}
			}
		}
}

var selectedIdx = new Array();

function cleanUp(obj) {
	/*var arobj = document.getElementById("searchdiv").getElementsByTagName("TR");

	for (var i =1 ; i < arobj.length; i++) {
		arobj[i].style.backgroundColor= "#E7FFBF";
	}*/
	query = jQuery(obj.parentNode).find("tr");
	jQuery.each(query,function(index,item){
		item.style.backgroundColor= "#E7FFBF";
	});
	
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

<div id="panelFieldsmenu_list">
<form action="">
Menu Id <input name="menuId"/><br/>
Menu Name <input name="menuName"/><br/>
Menu Role Id <input name="menuRoleId"/><br/>
Menu Action <input name="menuAction"/><br/>
</form>
</div>
<div id="menu_list" class="list"><div   class="idlist"></div><div class="pklist"></div> <table></table></div>
</div>
</body>
</html>