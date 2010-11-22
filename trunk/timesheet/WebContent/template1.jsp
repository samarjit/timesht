<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<% response.setHeader("Pragma","no-cache");
  response.setDateHeader("Expires",0);
  response.setHeader("Cache-Control","no-cache");
%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ page import="java.util.Iterator,java.util.LinkedHashMap" %>  
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>${screenTitle }</title>
<%String ctxstr = request.getContextPath(); %>
<style>
@import "<%=ctxstr %>/css/button.css";
@import "<%=ctxstr %>/css/header.css";
</style>
<link rel="stylesheet" href="<%=ctxstr %>/css/button.css" type="text/css" />
<link rel="stylesheet" href="<%=ctxstr %>/css/header.css" type="text/css" />


<%--script language="javascript" src="<%=request.getContextPath() %>/js/jquery.js"></script>
<script language="javascript" src="<%=request.getContextPath() %>/js/json2.js"></script>
<script language="javascript" src="<%=request.getContextPath() %>/js/encoder.js"></script--%>
<!-- script language="JavaScript" type="text/javascript" src="js/jquery.form.js"></script --> 
<s:property value="jsname" escape="false"/>
<link rel="stylesheet" href="<%=ctxstr %>/css/jquery-ui-1.7.2.custom.css" type="text/css" />



<s:url var="retriveurl" value="/retreivedetails.action" />
<s:url var="prepopulateurl" value="/prepopulate.action" />
<s:url var="inserturl" value="/insertdata.action" />
<s:url var="updateurl" value="/updatedata.action" />
<s:url var="deleteurl" value="/deletedata.action" />
<s:url var="generateurl" value="/template1.action" />
<s:url var="rpcurl" value="/jsrpc.action" />
<s:url var="sendmail" value="/sendmail.action" />
<s:url var="poForm" value="/POForm.jsp" />



<%String ctxpath=request.getContextPath(); %>
<script language="javascript">
var mailurlpart='<s:property value="%{#sendmail}"/>';
var retriveurlpart='<s:property value="%{#retriveurl}"/>';
var jsrpcurlpart='<s:property value="%{#rpcurl}"/>';
var generateurlpart='<s:property value="%{#generateurl}"/>';
var prepopulateurlpart='<s:property value="%{#prepopulateurl}"/>';
var inserturlpart='<s:property value="%{#inserturl}"/>';
var screenName= '<s:property value="%{#parameters.screenName}"/>';
var updateurlpart='<s:property value="%{#updateurl}"/>';
var deleteurlpart='<s:property value="%{#deleteurl}"/>';
var whereClause= '<s:property value="%{#parameters.panelFieldsWhereClause}"/>';
var screenMode= '<s:property value="%{#parameters.screenMode}"/>';
var vpassedonvalues= '<s:property value="%{#parameters.passedonvalues}" escape="false" />';
var poFormpart = '<s:property value="%{#poForm}"/>';
var ctxpath = "<%= ctxpath %>";
</script>

</head>
<script language="JavaScript" src="<%=ctxstr%>/js/commonjs.js"></script>

<body onload="populate()">
<s:actionerror/>
<s:actionmessage/>
<%@ include file="pages/header.jsp" %>
<div id="screen_title">${screenTitle }</div>
<div id="page">

<div id="errormsgdiv" class="ui-state-error ui-corner-all"  class="ui-state-error ui-corner-all"  style="display:none;padding: 0pt 0.7em;">

</div>
<div id="alertmsgdiv" class="ui-state-highlight ui-corner-all"  class="ui-state-error ui-corner-all"  style="display:none;padding: 0pt 0.7em;">

</div>
<!-- The following part is filled using template and DB -->
<table> 
<tr>
<td>
<form id="form1"   method="post" action="" onsubmit="alert('form submit event');return false;" >
<div id="panelsdiv" > 
<s:property value="dataPanel" escape="false"/>
 <!-- Using Iterator -->
 <s:iterator  value="extraFields" >
 <s:set var="som" value="key" />
 <s:if test="#som != 'buttonPanel'">  
 <%--s:property value="key"/--%> <s:property value="value" escape="false"/>
 </s:if>
 </s:iterator>
 </div>
 </form>
</td>

<td> <s:property value="extraFields.buttonPanel" escape="false"/>
&nbsp;
</td>
</tr>
</table>

<!-- Alternate display of extra fields -->
<table> 
<tr>
<td>
 
<%
LinkedHashMap hm =(LinkedHashMap)( request.getAttribute("extraFields"));
 Iterator i = (Iterator)(hm).keySet().iterator();
%>
 


<%-- while(i.hasNext()) {
     String s = (String) i.next(); %>
      <%if(!s.equalsIgnoreCase("buttonPanel")){ %>  
     <%=s%>  <%=hm.get(s)%>
     <%}%>
       <BR/>
<%    }
--%>

</td>
</tr>
</table>


<table> 
<tr>
<td>
<div id="retreivedetailschilddiv" >
<s:set var="scn" value="%{screenName}" />
<s:if test='#scn eq "frmRFQ"'>
<jsp:include page="pages/rfqvendor.jsp" />
</s:if>
<%--if("frmRFQ".equals(request.getParameter("screenName"))){ %>
<jsp:include page="pages/rfqvendor.jsp" />
<%} --%>
</div> <!-- close retreivedetailschilddiv -->
</td>
</tr>
</table> 
<script>
function changestate(objthis){
	if(document.getElementById("retreivedetailsdiv").style.display == "none"){
		document.getElementById("retreivedetailsdiv").style.display = "block";
		 objthis.innerHTML = "-";
	}else{
		document.getElementById("retreivedetailsdiv").style.display = "none";
		 objthis.innerHTML = "+";
	}
}
</script>
<button onclick="changestate(this)">+</button>
<div id="retreivedetailsdiv" style="display:none">
</div> <!-- close retreivedetailsdiv -->
</div> 
</body>
</html>