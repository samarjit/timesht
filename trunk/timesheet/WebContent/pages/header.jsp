<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>   
<%@ page import="java.util.Date,java.text.DateFormat,dto.UserDTO" %>     
<html>  
<head>

</head>
<body>
<div id="logo">    
<h1 >Asset Management System</h1>

<script>
function fnCreateActivity(ac) { 
	var ctxpath = '<%=request.getContextPath() %>';
	var url = ctxpath+'/template1.action?';
	var url1= ctxpath+'/workflow.action?';
	var url2= ctxpath+'/scrworkflow.action?';
	if(ac== 'CR'){
      url =url + "screenName=frmRequest";
	}else if(ac=='CRFQ'){
	  url =url+ "screenName=frmRFQ&activityname=CRFQ&create=true&screenMode=create";	
	}else if(ac=='CRAST'){
	  url =url+ "screenName=frmAsset&activityname=CRAST&create=true&screenMode=create";	
	}else if(ac=='CRALLOC'){
		  url =url+ "screenName=frmAllocation&activityname=CRALOC&create=true&screenMode=create";	
	}else if(ac=='CDN'){
	  url =url+ "screenName=frmDeliveryNote&activityname=CDN&create=true&screenMode=create";
	}
//	alert(url);
//document.getElementById("frmmenu").action = url;
location.href=url;
}

</script>
<% UserDTO userDTO = (UserDTO) session.getAttribute("userSessionData"); 
String userId = "";
String roleId = "";
 if(userDTO==null || userDTO.getUserid() == null){
	 String url = request.getContextPath()+"/pages/login.jsp?errormsg=Session Expired";
	 response.sendRedirect(url);
	 
 }else{
	 userId = userDTO.getUserid(); 
	 roleId = userDTO.getRoleid();
 }
%>
<script>
var userId =  '<%= userId %>';
var roleId =  '<%= roleId %>';

</script>

Welcome ${userSessionData.username }, <%= DateFormat.getDateTimeInstance(
        DateFormat.MEDIUM, DateFormat.SHORT).format(new Date()) %>( ${userSessionData.roleid } )
</div>
<hr/>
<div id="header">
<div id="menu">
<ul>
${sessionScope.menu}
<li><a href="logout.action">Logout</a></li> 
</ul>

<br/>
</div>
</div>
<br/>
<jsp:include page="/pages/wflproto.jsp"></jsp:include>


<div style="display:block">
<jsp:include page="/pages/actionbutton.jsp"></jsp:include>
</div>
</body>
</html>