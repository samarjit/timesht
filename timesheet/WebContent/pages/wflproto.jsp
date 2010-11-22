<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>   
<% String url=response.encodeURL(request.getContextPath()+"/WorkflowController.do"); %>
   
<c:forEach var="action" items="${sessionScope.applicationDTO.wflactions}">
 
  ${action.key}  : ${action.value} |
 <a href="<%=url %>?navigateto=${action.key}" >${action.key}</a>
</c:forEach>

 