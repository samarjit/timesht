<%@ page language="java" pageEncoding="ISO-8859-1"%>
<% response.setHeader("Pragma","no-cache");
  response.setDateHeader("Expires",0);
  response.setHeader("Cache-Control","no-cache");
%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ page import="java.util.Iterator,java.util.LinkedHashMap" %>  
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%String ctxstr = request.getContextPath(); %>
<title>${screenTitle }</title>
<style>
@import "<%=ctxstr %>/css/button.css";
@import "<%=ctxstr %>/css/header.css"; 
</style>
</head>

<s:property value="jsname" escape="false"/>
<script language="JavaScript" src="<%=ctxstr %>/js/commonjs.js"></script>
<link rel="stylesheet" href="<%=ctxstr %>/css/jquery-ui-1.7.2.custom.css" type="text/css" />
<s:url var="url" value="/searchlist.action" />
<script language="javascript" >
var urlpart='<s:property value="%{#url}"/>';
var screenName= '<s:property value="%{#parameters.screenName}"/>' ;
</script>
<body onload="search()">
<%@ include file="header.jsp" %>
<div id="screen_title">${screenTitle }</div>
<div id="page">
 <div id="errormsgdiv" class="ui-state-error ui-corner-all"  class="ui-state-error ui-corner-all"  style="display:none;padding: 0pt 0.7em;">

</div>
<div id="alertmsgdiv" class="ui-state-highlight ui-corner-all"  class="ui-state-error ui-corner-all"  style="display:none;padding: 0pt 0.7em;">

</div>


<table>
	<tr>
		<td>
		<div id='<s:property value="extraFields.searchPanel.key"/>'><s:property
			value="extraFields.searchPanel" escape="false" /></div>
		</td>
		<td><s:property value="extraFields.searchPanelBtn" escape="false" />
		</td>
	</tr>
	<tr>
		<td colspan="2"><div id="searchdiv" class="searchdiv"></div>
		</td>	
	</tr>
	<tr>
		<td>
		<div id='<s:property value="extraFields.searchPanelAsset.key"/>'><s:property
			value="extraFields.searchPanelAsst" escape="false" /></div>
		</td>
		<td><c:out value="${extraFields.searchPanelAsstBtn}" escapeXml="false"/>
			</td>
	</tr>
	<tr>
		<td colspan="2"><div id="searchdiv2" class="searchdiv"></div>
		</td>	
	</tr>
	<tr>
		<td>
		<div id='<s:property value="extraFields.searchPanelAlloc.key"/>'><s:property
			value="extraFields.searchPanelAlloc" escape="false" /></div>
		</td>
		<td><s:property value="extraFields.searchPanelAllocBtn" escape="false" />
		</td>
	</tr>
	<tr>
		<td colspan="2"><div id="searchdiv3" class="searchdiv"></div>
		</td>	
	</tr>
</table>

<form action="template1.action" id="formwhere" style="display:none">
<input type="hidden" name="screenName" id="screenName" />
<input type="hidden" id="screenMode" name="screenMode" value="insert">
<input type="hidden" id="panelFieldsWhereClause" name="panelFieldsWhereClause" value="">
<input type="hidden" id="passedonvalues" name="passedonvalues" value="" />
</form>




</div> <!-- id = page -->
</body>
</html>