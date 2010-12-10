<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%String ctxstr = request.getContextPath(); %>

<body>
	Hello test action
<a href="<%=ctxstr%>/engine.action" >engine.action</a>	
<a href="<%=ctxstr%>/testftl.action" >testftl.action</a>	
<a href="<%=ctxstr%>/config-browser/actionNames.action?namespace=%2Fconfig-browser" >config browser</a>
<a href="<%=ctxstr%>/engine.action?debug=browser" >debug browser</a>	

</body>
</html>
