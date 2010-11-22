<html>
<head>
<%String ctxstr = request.getContextPath(); %>
<link rel="stylesheet" href="<%=ctxstr %>/css/button.css" type="text/css" />
<link rel="stylesheet" href="<%=ctxstr %>/css/header.css" type="text/css" />
</head>
<body>
<%@ include file="header.jsp" %>
<div id="screen_title">${screenTitle }</div>
<div id="page">
Welcome to timesheet app
</div>
</body>
</html>