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
var screenMode = "insert";

function replacer(key, value) {
	if (typeof value === 'number' && !isFinite(value)) {
		return String(value);
	}
	return value;
}

function getMenuList(paramElm) {
	var data={};
	data.pageno = $('#'+paramElm+' .pageno').val();
	data.pagesize = $('#'+paramElm+' .pagesize').val();
	var strURL= ctxstr+"/engineajax.action?command=selectall"+paramElm+"&data="+JSON.stringify(data, replacer,"");;
			alert(strURL);
$.ajax({type:"GET", url: strURL, success:  function (val){
var json  = JSON.parse(val);
$('#'+paramElm+' .page').html(json.paging);
$('#'+paramElm+' table').html(json.listtabledata);
$('#'+paramElm+' .idlist').text(json.listattrs);
$('#'+paramElm+' .pklist').text(json.primarykeys);
addSelectEvents(paramElm);
} });	
}

function searchList(paramElm) {
	 
	var data={};
	data.pageno = $('#'+paramElm+' .pageno').val();
	data.pagesize = $('#'+paramElm+' .pagesize').val();
	 var whereclauseStr="";
	 var whereclause = {}
	 $("#panelFields"+paramElm ).find(" form :input").each(function(){
			var elmname = $(this).attr("name");
			var elmval = this.value;
			if(elmval != null && elmval != ""){
				whereclauseStr += '"'+elmname+'":"'+escape(elmval)+'",';
			} 
		});
	 
	 if(whereclauseStr!=null || whereclauseStr != "")
	 whereclauseStr = whereclauseStr.substring(0,whereclauseStr.length -1);
	 if(whereclauseStr.length > 0)whereclauseStr = "{"+whereclauseStr;
	 whereclauseStr +="}";
	 
	 whereclause = JSON.parse(whereclauseStr);
	 data.whereclause = whereclause;
	 
	var strURL= ctxstr+"/engineajax.action?command=selectall"+paramElm+"&data="+JSON.stringify(data, replacer,"");;
			alert(strURL);
$.ajax({type:"GET", url: strURL, success:  function (val){
		var json  = JSON.parse(val);
		$('#'+paramElm+' .page').html(json.paging);
		$('#'+paramElm+' table').html(json.listtabledata);
		$('#'+paramElm+' .idlist').text(json.listattrs);
		$('#'+paramElm+' .pklist').text(json.primarykeys);
		addSelectEvents(paramElm);
		} 
	});	
}

function add(paramElm){
	var json = '{"subcmd":"add","entitydata":{';
	$("#panelFields"+paramElm ).find("input").each(function(){
		json +='"'+$(this).attr("name")+'":"'+ this.value+'",';
	});
	json = json.substring(0,json.length -1);
	json +='}}';
	var strURL= ctxstr+"/engineajax.action?command="+paramElm+"crud&data="+json;
	alert(strURL);
	$.ajax({type:"GET", url: strURL, success:  function (val){
			alert("add result:"+val)
		}
	});
}

function deleteRec(paramElm){
	var json = '{"subcmd":"delete","entitydata":{';
	$("#panelFields"+paramElm ).find("input").each(function(){
		json +='"'+$(this).attr("name")+'":"'+ this.value+'",';
	});
	json = json.substring(0,json.length -1);
	json +='}}';
	var strURL= ctxstr+"/engineajax.action?command="+paramElm+"crud&data="+json;
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
		 
		$("#panelFields"+paramElm ).find("input[name="+idlist[index]+"]").val($(val).text());
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
<input type="button" value="getList" onclick="getMenuList('menu_list')" />
<input type="button" value="search" onclick="searchList('menu_list')" />
<input type="button" value="add" onclick="add('menu_list')" />
<input type="button" value="save" onclick="save()" />
<input type="button" value="update" onclick="update()" />
<input type="button" value="delete" onclick="deleteRec('menu_list')" />

<div id="panelFieldsmenu_list">
<form action="">
Menu Id <input name="menuId"/><br/>
Menu Name <input name="menuName"/><br/>
Menu Role Id <input name="menuRoleId"/><br/>
Menu Action <input name="menuAction"/><br/>
</form>
</div>
<div id="menu_list" class="list"><div   class="idlist"></div><div class="pklist"></div> 
<div class="page"><input class='pagesize' style="width:18px" value="10"><select class='pageno'><option selected>0</option></select></div>
<table></table></div>


<input type="button" value="getList" onclick="getMenuList('screen_list')" />
<input type="button" value="search" onclick="searchList('screen_list')" />
<input type="button" value="add" onclick="add('screen_list')" />
<input type="button" value="save" onclick="save()" />
<input type="button" value="update" onclick="update()" />
<input type="button" value="delete" onclick="deleteRec('screen_list')" />
<input type="button" value="reset" onclick="$('#panelFieldsscreen_list form').get(0).reset()" />
<div id="panelFieldsscreen_list">
<form action="">
Screen Name <input name="scrName"/><br/>
Businesslogic <input name="businesslogic"/><br/>
cssname <input name="cssname"/><br/>
JSname <input name="jsname"/><br/>
Relatedpanel <input name="relatedpanel"/><br/>
ScreenTitle <input name="screenTitle"/><br/>
SplWhereclause <input name="splwhereclause"/><br/>
templateName <input name="templateName"/><br/>
</form>
</div>
<div id="screen_list" class="list"><div   class="idlist"></div><div class="pklist"></div>
<div class="page"><input class='pagesize' style="width:18px" value="10"><select class='pageno'><option selected>0</option></select></div>
<table></table></div>

<input type="button" value="getList" onclick="getMenuList('scrpanel_list')" />
<input type="button" value="search" onclick="searchList('scrpanel_list')" />
<input type="button" value="add" onclick="add('scrpanel_list')" />
<input type="button" value="save" onclick="save()" />
<input type="button" value="update" onclick="update()" />
<input type="button" value="delete" onclick="deleteRec('scrpanel_list')" />
<input type="button" value="reset" onclick="$('#panelFieldsscrpanel_list form').get(0).reset()" />
<div id="panelFieldsscrpanel_list">
<form action="">
Screen Name <input name='id_scrName'/>
Panel Name <input name='id_panelName'/>
Css Class <input name='cssClass'/>
Paneltype <input name='paneltype'/>
Pk Name <input name='pkName'/>
Relatedpanel <input name='relatedpanel'/>
Rw Flg <input name='rwFlg'/>
Selquery <input name='selquery'/>
Sortorder <input name='sortorder'/>
Splwhereclause <input name='splwhereclause'/>
Table Name <input name='tableName'/>


</form>
</div>
<div id="scrpanel_list" class="list"><div   class="idlist"></div><div class="pklist"></div>
<div class="page"><input class='pagesize' style="width:18px" value="10"><select class='pageno'><option selected>0</option></select></div>
<table></table></div>



<input type="button" value="getList" onclick="getMenuList('panelfield_list')" />
<input type="button" value="search" onclick="searchList('panelfield_list')" />
<input type="button" value="add" onclick="add('panelfield_list')" />
<input type="button" value="save" onclick="save()" />
<input type="button" value="update" onclick="update()" />
<input type="button" value="delete" onclick="deleteRec('panelfield_list')" />
<input type="button" value="reset" onclick="$('#panelFieldspanelfield_list form').get(0).reset()" />
<div id="panelFieldspanelfield_list">
<form action="">
Screen Name <input name='id_scrName'/>
Panel Name <input name='id_panelName'/>
Order No <input name='id_orderno'/>
Autogen <input name='autogen'/>
Classname <input name='classname'/>
Datatype <input name='datatype'/>
Dbcol <input name='dbcol'/>
Dbcol Siz <input name='dbcolSiz'/>
Elem Attrib <input name='elemAttrib'/>
Fname <input name='fname'/>
Htmlelm <input name='htmlelm'/>
Idname <input name='idname'/>
Lblname <input name='lblname'/>
Ncol <input name='ncol'/>
Nrow <input name='nrow'/>
Prkey <input name='prkey'/>
Store Flg <input name='storeFlg'/>
Strquery <input name='strquery'/>
Valid Msg <input name='validMsg'/>
Valid Rule <input name='validRule'/>
Validation <input name='validation'/>



</form>
</div>
<div id="panelfield_list" class="list"><div   class="idlist"></div><div class="pklist"></div>
<div class="page"><input class='pagesize' style="width:18px" value="10"><select class='pageno'><option selected>0</option></select></div>
<table></table></div>

</div><!-- page -->

</body>
</html>