<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags" %>   
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Task Master</title>
<%String ctxstr = request.getContextPath(); %>

<script language="JavaScript" src="../js/jquery.js"></script>
<script language="JavaScript" src="../js/json2.js"></script>
<script language="JavaScript" src="../js/jquery-ui-1.7.2.custom.min.js"></script> 
<script language="JavaScript" src="../js/timesheet.js"></script>

<style>
@import "<%=ctxstr %>/css/timesheet.css"; 
@import "<%=ctxstr %>/css/button.css";
@import "<%=ctxstr %>/css/header.css"; 
.panelfields {
	border:1px solid #618c04;
	border-width:1px 1px 0px;
	margin: 0px;
	border-spacing:0px;
	 
}

.panelfields td{
	border-collapse:collapse;

}
</style>
<script>
var formName = "frmTaskMaster";	
var ctxstr = "<%=ctxstr %>";
var tasklist= {};
var userSessiondata_userid = "${userSessionData.userid }";
function populate(){
	var today = new Date();
	var todayStr = today.getDate() + "/" + (parseInt(today.getMonth())+1) + "/" + today.getFullYear();

	//prpopulate
	$("#searchFields input[id='tskstartdate']").val(todayStr);
	$("#panelFields input[id='tsdate']").val(todayStr);
	$("#searchFields input[id='tskdate']").val(todayStr);
	filter();
	
}

</script>

</head>
<body onload="populate();">
<%@include file="header.jsp"%>
<div id=variables>

</div>

<div id="page">
<table>
    <tr>
        <td>
            <div id="searchFields">
            	<form action="timesheet.aciton" method="GET" accept-charset="utf-8">
	  <table id="taskDetails" class="panelfields" border="1">
                <tbody>
                    <tr>
                        <td style="width: 150px;">
                            Task ID
                        </td>
                        <td style="width: 150px;">
                            <input  name="tskid" id="tskid" value="" class="null" type="text">
                        </td>
                        <td style="width: 150px;">
                            Project
                        </td>
                        <td style="width: 150px;">
                            <input  name="project" id="project" value="" class="null" type="text">
                        </td>
                    </tr>
                    <tr>
                        <td>
                            Task Description
                        </td>
                        <td>
                            <input  name="taskdesc" id="taskdesc" value="" class="null" type="text">
                        </td>
                        <td>
                            Task Hours
                        </td>
                        <td>
                            <input  name="tskhours" id="tskhours" value="" class="null" type="text">
                        </td>
                    </tr>
                    <tr>
                        <td>
                            Task(%) Completed
                        </td>
                        <td>
                            <input  name="tskpercentcpl" id="tskpercentcpl" value="" class="null" type="text">
                        </td>
                        <td>
                            Task Start Date
                        </td>
                        <td>
                            <input  name="tskstartdate" id="tskstartdate" value="" class="null" type="text">
                        </td>
                    </tr>
                    <tr>
                        <td>
                            Task End Date
                        </td>
                        <td>
                            <input  name="tskenddate" id="tskenddate" value="" class="null" type="text">
                        </td>
                         <td>
                            Task Date
                        </td>
                        <td>
                            <input  name="tskdate" id="tskdate" value="" class="null" type="text">
                        </td>
                    </tr>
                </tbody>
            </table>
</form>
            </div>
        </td>
    </tr>
    <tr>
        <td>
            <div id="panelsdiv">


            

		<table id="panelFields" class="panelfields" border="1">
			<tbody>
				<tr>
					<td>Task ID</td>
					<td><select   name="tskid" id="tskid"
						class="null" >
							<option value="">--select--</option>
							</select>
						</td>
					<td>Task hours Allocated</td>
					<td><input type="text"  name="tshouralloc" id="tshouralloc"
						class="null" /></td>
				</tr>
				<tr>			
					<td>Task hours Remaining</td>
					<td><input type="text"  name="tshoursrem" id="tshoursrem"
						class="null" /></td>
				
					<td style="width: 150px;">Timesheet ID</td>
					<td style="width: 185px;"><input 
						name="tsid" id="tsid" value="" class="null" type="text"></td>
				</tr>
				<tr>
					<td style="width: 150px;">Employee ID</td>
					<td style="width: 150px;"><input 
						name="empid" id="empid" value="${userSessionData.userid }" class="null" type="text"></td>
				
					<td>Timesheet Date</td>
					<td><input  name="tsdate" id="tsdate"
						value="" class="null" type="text"></td>
				</tr>
				<tr>
					<td>Timesheet Hours</td>
					<td><input  name="tshours" id="tshours"
						value="" class="null" type="text"></td>
				</tr>
				
			</tbody>
		</table>






		<table id="statusFields" class="paneltable" border="1">
			<tbody>
				<tr>
					<td style="width: 150px;">Status</td>
					<td style="width: 185px;"><input 
						name="Status" id="Status" value="" class="null" type="text"></td>
					<td style="width: 150px;">Wfl ActionDesc</td>
					<td style="width: 150px;"><input 
						name="wflactiondesc" id="wflactiondesc" value="" class="null"
						type="text"></td>
				</tr>
				<tr>
					<td>Wfl App ID</td>
					<td><input  name="wflappid" id="wflappid"
						value="" class="null" type="text"></td>
					<td>Wfl ID</td>
					<td><input  name="wflid" id="wflid"
						value="" class="null" type="text"></td>
				</tr>

			</tbody>
		</table>























		</div>
        </td>
        <td>
            <div id="buttonPanel">
            	 <table>
                <tr><td><input type="submit" value="Filter Task" class="button" onclick="filter()"/></td></tr>
				<tr><td><input type="submit" value="Search" class="button" onclick="searchTimesheet()"/></td></tr>
				<tr><td><button onclick="deleteTs()" class="button"> Delete </button></td></tr>
				<tr><td><button onclick="update()" class="button"> Update </button></td></tr>
				<tr><td><button onclick="addTs()" class="button"> Add </button></td></tr>
				<tr><td><button onclick="selectAll()" class="button"> Select All </button></td></tr>
				</table>
            </div>
        </td>
    </tr>
</table>

<div id="weekview">
<table class="list"><tr><th>Task</th><th>Date</th><th>Date</th><th>Date</th><th>Date</th><th>Date</th><th>Date</th><th>Date</th></tr>

</table> 
</div>
<div id="list" class="list">
	
	
</div>

</div> <!-- div page -->
</body>
</html>