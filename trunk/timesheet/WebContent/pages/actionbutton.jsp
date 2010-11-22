<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>      
 
<script>
<%String url=response.encodeURL(request.getContextPath()+"/workflow.action"); %>
var wflcontrollerurl = "<%=url %>";
</script>
 
 
<a id="submitanchor" href="<%=url %>?action=true&doString=${sessionScope.applicationDTO.currentActionId}&wflid=${applicationDTO.wflid}"+>${sessionScope.applicationDTO.currentAction}</a>
 