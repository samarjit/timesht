<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>   
<html xmlns="http://www.w3.org/1999/xhtml">
 <head>
  <title> new document </title>
  <meta name="generator" content="editplus" />
  <meta name="author" content="" />
  <meta name="keywords" content="" />
  <meta name="description" content="" />
<%String ctxstr = request.getContextPath(); %> 
<link rel="stylesheet" href="<%=ctxstr %>/css/jquery-ui-1.7.2.custom.css" type="text/css" /> 
<style type="text/css"> 
</style>
<script type="text/javascript">
function fn() {
	window.history.go(-1);
}
</script>

 </head>




 <body>

<%if(null != request.getParameter("errormsg")){ %>		
<div id="errormsgdiv" class="ui-state-error ui-corner-all"  style="padding: 0pt 0.7em;">
<p><span style="float: left;margin-left: 0.3em;  margin-right: 0.3em;" class="ui-icon ui-icon-alert"></span>
${param.errormsg}</p>
</div> 
<%}else if(request.getAttribute("errorMessage") != null){ %>
<div id="errormsgdiv" class="ui-state-error ui-corner-all"  style="padding: 0pt 0.7em;">
<p><span style="float: left;margin-left: 0.3em;  margin-right: 0.3em;" class="ui-icon ui-icon-alert"></span>
<%=request.getAttribute("errorMessage")%></p>
</div> 
<%}else{ %>
 Unknown Error occured
<%}%>

 <input type="button" onclick="fn()" value="Back" />
  
 </body>
</html>
