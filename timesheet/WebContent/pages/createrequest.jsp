<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="dto.ApplicationDTO" %>    
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<% ApplicationDTO appDTO = (ApplicationDTO)session.getAttribute("applicationDTO"); %>
<c:set var="appdto"  value="applicationDTO"
scope="session" />
<% String url=response.encodeURL(request.getContextPath()+"/WorkflowController.do"); %>
App Id:<c:out value="${sessionScope.applicationDTO.appid}"></c:out>
<jsp:include page="/pages/wflproto.jsp"></jsp:include>
<br/>

<a href="<%=url %>?action=true&id=<%=appDTO.getWflid() %>&do=<%=appDTO.getCurrentActionId() %>" ><%=appDTO.getCurrentAction() %></a>
</body>
</html>