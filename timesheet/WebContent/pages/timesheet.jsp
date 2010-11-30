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
function populate(){
	var today = new Date();
	var todayStr = today.getDay() + "/" + today.getMonth() + "/" + today.getFullYear();
	//prpopulate
	$("#searchFields input[id='tskstartdate']").val(todayStr);
	$("#panelFields input[id='tsdate']").val(todayStr);
	
	filter();
	
}

function filter(){	
var sqlstring1 =  "select tskid,tskhours,tskstartdate,tskenddate,taskdesc from TS_taskmaster ";
var  filterjson = {
		project: $("#searchFields input[id='project']").val() ,
		tskstartdate: $("#searchFields input[id='tskstartdate']").val(),
		taskdesc:  $("#searchFields input[id='taskdesc']").val(),
		sqlstring:sqlstring1
	};
var joiner =" WHERE ";	
if (filterjson.project != "") {
	sqlstring1 += joiner +" project = '" + filterjson.project+"'";
	joiner = " AND ";
} 
if (filterjson.tskstartdate != "") {
	sqlstring1 += joiner +" tskstartdate >= to_date('" + filterjson.tskstartdate+"', 'dd/mm/yyyy')";
	joiner = " AND ";
} 
if (filterjson.taskdesc != "") {
	sqlstring1 += joiner + " taskdesc like '%" + filterjson.taskdesc+ "%'" ;
	joiner = " AND ";
} 

 sqlstring1 = escape(sqlstring1);  
var strURL= "<%=ctxstr %>/timesheetajax.action?returnajax=true&command=query&data="+JSON.stringify(filterjson, replacer,"")+"&sqlstring="+sqlstring1;
jQuery.ajax({
		   type: "GET",
		   url: strURL,
		   success:  callbaktasklist

		 });
 
	
}

function fnAdjustTableWidth(){
	var tdwidthar = new Array();
	jQuery.each(jQuery("#panelsdiv  table"), function(idx, elem){
	
		var query = jQuery(elem).eq(0).find("tr").eq(0).find("td ");
		jQuery.each(query, function(index, item){
			//	alert(elem.id+" tdwidthar["+index+"]"+tdwidthar[index] + " "+jQuery(item).width());
			if (!tdwidthar[index]) 
				tdwidthar[index] = jQuery(item).width();
			else 
				if (tdwidthar[index] < jQuery(item).width()) {
					tdwidthar[index] = jQuery(item).width();
				}
			
			
		});
	});
	var j = 0;
	var maxtd = tdwidthar.length;
	
	
	var tblar = document.getElementById("panelsdiv").getElementsByTagName("table");
	for (var i = 0; i < tblar.length; i++) {
		query = jQuery(tblar[i]).find("tr").eq(0).find("td");
		elem = jQuery(query);
		
		jQuery.each(query, function(index, item){
			jQuery(item).width(tdwidthar[j]);
			j++;
			if (maxtd == j) 
				j = 0;
			
		});
		
	}
}
	
function reviver (args) {
	alert("args"+args);
	return args;
}
function callbaktasklist (args) {
 
	 //	var args = '[{TSKID:"1",TSKHOURS:"2"} ] ]';
		 tasklist = JSON.parse(args);	
		  $("#panelFields select[id='tskid'] option").remove();
		  $("#panelFields select[id='tskid']").append( $('<option></option>').val("").html("--select--"));
	$.each(tasklist,function(index,val){  
		$("#panelFields select[id='tskid']").append( $('<option></option>').val(val.TSKID).html(val.TASKDESC+"#"+val.TSKID ));
	} );	

}

function replacer(key, value) {
	if (typeof value === 'number' && !isFinite(value)) {
		return String(value);
	}
	return value;
}


$(document).ready(function (){

$("#panelFields select[id='tskid']").bind("change",function(event){
	var today = new Date();
	var seltaskid = $(this).val();
	$("#panelFields input[id='tshoursrem']").val("0");
	$.each(tasklist,function(index,val){ 
		if (seltaskid == val.TSKID) {
			$("#panelFields input[id='tsid']").val(today.getFullYear()+""+today.getMonth()+""+today.getDay()+"_${userSessionData.userid }"+val.TSKID);
			$("#panelFields input[id='tshouralloc']").val(val.TSKHOURS);
			var sqlstring1 = "select sum(tshrs) tshrs from ts_emp_timesheet where tskid= '"+val.TSKID+"'"; 
			var strURL = "<%=ctxstr %>/timesheetajax.action?returnajax=true&command=query&data=&sqlstring="+sqlstring1;
			$.ajax( {
				type: "GET",
				url: strURL,
				success: callbakseltask
			});
		//	debugger;
		}
	});
});
fnAdjustTableWidth() ;
});


function callbakseltask (args) {
	 
	var json = JSON.parse(args);
	if(json[0].TSHRS =="null")json[0].TSHRS = 0;
	 
	var hrsRemaining = $("#panelFields input[id='tshouralloc']").val() - json[0].TSHRS;
	$("#panelFields input[id='tshoursrem']").val(hrsRemaining);
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
                            &nbsp;
                        </td>
                        <td>
                            &nbsp;
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
<div id="list" class="list">
	
	
</div>

</div> <!-- div page -->
</body>
</html>