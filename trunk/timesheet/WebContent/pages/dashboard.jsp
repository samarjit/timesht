<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="dto.UserDTO" %>    
<% response.setHeader("Pragma","no-cache");
  response.setDateHeader("Expires",0);
  response.setHeader("Cache-Control","no-cache");
%>   
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Dashboard</title>
</head>
<STYLE type="text/css">
li{
float:  left;
background-color:fuchsia;
}
</STYLE>

<SCRIPT LANGUAGE="JavaScript">
var ctxpath = '<%=request.getContextPath() %>';
function fnsubmit(ac) {
	
	var url=ctxpath+'<%= response.encodeURL("/WorkflowController.do") %>?';
	if(ac== 'CR'){
	url+="activityname=CR&create=true";
	}else if(ac=='RFQ'){
		
	}
	//alert(url);
document.getElementById("frmmenu").action = url;
location.href=url;
}

function fnsearch() {
	//document.getElementById("frmsearch").submit();
	var url=ctxpath+'<%= response.encodeURL("/WorkflowSearch.do") %>?';
	url+="appid="+document.getElementsByName("appId")[0].value;
	//prompt("url",url);
	sendAjaxGet(url,searchcallback);
	}
function searchcallback(param){
	document.getElementById("wflitems").innerHTML=param;
}


</SCRIPT>
<script language="JavaScript" src="<%=request.getContextPath() %>/js/commonjs.js" ></script>
<body>
<%UserDTO usrDTO = (UserDTO)session.getAttribute("userSessionData"); %>
User Name:<%=usrDTO.getUserid() %>
<br/>
menu:
<FORM METHOD="GET" ACTION="/WorkflowController.do" id="frmmenu">
<input type="button" value="Create Request" onclick="fnsubmit('CR');" /> 
<input type="button" value="Create RFQ" onclick="fnsubmit('RFQ');" /> 
</FORM>
<hr>
Search:
<FORM METHOD="POST" ACTION="/WorkflowSearch.do" id="frmsearch">
Application Id: <input type=text name="appId"  />  
<input type="button" value="search" onclick="fnsearch();" /> 

</FORM>

<hr>
Workflow Items:
<dir id="wflitems">
</dir>


</body>
</html>