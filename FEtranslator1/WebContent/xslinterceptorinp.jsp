<%@ taglib uri="/struts-tags" prefix="struts2"%>
<html>
<head>
</head>
<body>
<struts2:form method="post" action="testxsl.action" >
<table>
<tr><td><struts2:textfield key="testHeader"
                   label="Enter Header :"/></td></tr>
<tr><td><struts2:textfield key="testFooter"
                   label="Enter Footer :"/></td></tr>
<tr><td colspan="2"><struts2:submit value="Register"/></td></tr>
</table>
</struts2:form>
</body>
</html>