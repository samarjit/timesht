<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%String ctxstr = request.getContextPath(); %>

<body>
<h2>Index</h2><br/>
<a href="<%=ctxstr%>/engine.action?retrievename=retrvnam" >engine.action</a>	<br/>
<a href="<%=ctxstr%>/testftl.action" >testftl.action</a><br/>
<a href="<%=ctxstr%>/templatetest.action" >embedded ftl test</a>	<br/>
<a href="<%=ctxstr%>/third.jsp" >XSLT Filter</a>	<br/>
<a href="<%=ctxstr%>/testxsl1.action" >test xsl xml</a> <br/>
<a href="<%=ctxstr%>/testxsl.action" >test xsl</a> <br/>
<hr/>
<a href="<%=ctxstr%>/config-browser/actionNames.action?namespace=%2Fconfig-browser" >config browser</a>|
<a href="<%=ctxstr%>/engine.action?debug=browser" >debug browser</a>	

</body>
</html>
