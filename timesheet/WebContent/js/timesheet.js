/**
 * jQuery("#panelFields input[type='text']").each(function(){console.log(this.id+": "+$(this).val());})
 * @author Samarjit
 */

var screenMode = "select";

$(document).ready(function (){

	$("#panelFields select[id='tskid']").bind("change",function(event){
		var today = new Date();
		var seltaskid = $(this).val();
		$("#panelFields input[id='tshoursrem']").val("0");
		$.each(tasklist,function(index,val){ 
			if (seltaskid == val.TSKID) {
				$("#panelFields input[id='tsid']").val(today.getFullYear()+""+(parseInt(today.getMonth())+1)+""+today.getDate()+"_"+userSessiondata_userid+val.TSKID);
				$("#panelFields input[id='tshouralloc']").val(val.TSKHOURS);
				var sqlstring1 = "select sum(tshrs) tshrs from ts_emp_timesheet where tskid= '"+val.TSKID+"'"; 
				var strURL = ctxstr+"/timesheetajax.action?returnajax=true&command=query&data=&sqlstring="+sqlstring1;
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
	
function filter(){	
	var sqlstring1 =  "select tskid,tskhours,tskstartdate,tskenddate,taskdesc from TS_taskmaster ";
	var  filterjson = {
			project: $("#searchFields input[id='project']").val() ,
			tskstartdate: $("#searchFields input[id='tskstartdate']").val(),
			tskdate: $("#searchFields input[id='tskdate']").val(),
			taskdesc:  $("#searchFields input[id='taskdesc']").val(),
			sqlstring:sqlstring1
		};
	var joiner =" WHERE ";	
	if (filterjson.project != "") {
		sqlstring1 += joiner +" project = '" + filterjson.project+"'";
		joiner = " AND ";
	} 
	if (filterjson.tskstartdate != "") {
		sqlstring1 += joiner +" tskenddate >= to_date('" + filterjson.tskdate+"', 'dd/mm/yyyy')";
		joiner = " AND ";
	} 
	if (filterjson.taskdesc != "") {
		sqlstring1 += joiner + " taskdesc like '%" + filterjson.taskdesc+ "%'" ;
		joiner = " AND ";
	} 

	 sqlstring1 = escape(sqlstring1);  
	var strURL= ctxstr+"/timesheetajax.action?returnajax=true&command=query&data="+JSON.stringify(filterjson, replacer,"")+"&sqlstring="+sqlstring1;
	jQuery.ajax({
			   type: "GET",
			   url: strURL,
			   success:  callbaktasklist

			 });
	 
		
}
function callbaktasklist (args) {
	 
	
	//	var args = '[{TSKID:"1",TSKHOURS:"2"} ] ]';
	var tasklistinqrypart = "";
		 tasklist = JSON.parse(args);	
		  $("#panelFields select[id='tskid'] option").remove();
		  $("#panelFields select[id='tskid']").append( $('<option></option>').val("").html("--select--"));
	$.each(tasklist,function(index,val){  
		tasklistinqrypart +="'"+val.TSKID+"',"
		$("#panelFields select[id='tskid']").append( $('<option></option>').val(val.TSKID).html(val.TASKDESC+"#"+val.TSKID ));
	} );	

	if(tasklistinqrypart.length > 0)tasklistinqrypart = tasklistinqrypart.substring(0,tasklistinqrypart.length -1);
	
	var today = new Date();
	var curday = today.getDay();
	var weekstartdate = today.getDate() - curday; 
	var weekenddate = weekstartdate +7;
	
	var weekstartdatestr = "";
	var weekenddatestr = "";
	curday = 0; 
	var dates =[];
	var daysofweek = ["Sun","Mon","Tue","Wed","Thu","Fir","Sat"];
	while(curday < 7){
		today = new Date();
		today.setDate( weekstartdate +curday);
		todayStr = daysofweek[curday]+" " + today.getDate() + "/" + (parseInt(today.getMonth())+1) + "/" + today.getFullYear();
		if(curday == 0)weekstartdatestr = today.getDate() + "/" + (parseInt(today.getMonth())+1) + "/" + today.getFullYear();
		if(curday == 6)weekenddatestr = today.getDate() + "/" + (parseInt(today.getMonth())+1) + "/" + today.getFullYear();
		dates.push(todayStr+" ");
		curday++ ;
	}
	
	$("#weekview tr th").each(function(index, val){
		if(index > 0)$(val).text(dates[index-1]);
	}); 
	
	$("#weekview table tr td").parent().remove();
	
	for ( var i = 0; i < tasklist.length; i++) {
		$("#weekview table").append($("<tr><td>hi</td><td></td><td></td><td></td><td></td><td></td><td></td><td></td></tr>"));
	}
	 //tskid in("+tasklistinqrypart+")  and
	var sqlstring1 ="select sum(tshrs) tshrs,tskid  from ts_emp_timesheet where tskid in("+tasklistinqrypart+")  and TSDATE between TO_DATE('"+weekstartdatestr+"','dd/mm/yyyy') and TO_DATE('"+weekenddatestr+"','dd/mm/yyyy')" +
		 	" group by tskid ";  
	
	if(tasklistinqrypart != ""){
	var strURL = ctxstr+"/timesheetajax.action?returnajax=true&command=query&data=&sqlstring="+sqlstring1;
		$.ajax( {
			type: "GET",
			url: strURL,
			success: callbakweekseltask
		});
	}
	
}

function callbakweekseltask(args) {
	var json = JSON.parse(args);
	alert(args);
}
function addTs() {
	screenMode = "insert";
	var sqlstring1 = " insert into TS_EMP_TIMESHEET (TSID,EMPID,EMPNAME,TSDATE,TSHRS,TSKID) values ("+
	"'"+jQuery("#panelFields input[id='tsid']").val()+"',"+
	"'"+jQuery("#panelFields input[id='empid']").val()+"',"+
	"'"+jQuery("#panelFields input[id='empname']").val()+"',"+
	"to_date('"+jQuery("#panelFields input[id='tsdate']").val()+"','dd/mm/yyyy'),"+
	" "+jQuery("#panelFields input[id='tshours']").val()+","+
	"'"+jQuery("#panelFields select[id='tskid']").val()+"')";
	var strURL = ctxstr+"/timesheetajax.action?returnajax=true&command=update&data=&sqlstring="+sqlstring1;
	$.ajax( {
		type: "GET",
		url: strURL,
		success: callbakaddts
	});
}

function callbakaddts(args) {
	var json = JSON.parse(args);
	if(screenMode == "insert" ){
		alert("records inserted successfully SQL result was:"+json.result);
	}else if(screenMode == "update"){
		alert(json.result+" records updated successfully");
	}
}
function update() {
	screenMode = "update";
	var sqlstring1 = " update TS_EMP_TIMESHEET "+
	"set TSDATE='"+jQuery("#panelFields input[id='tsdate']").val()+"', "+
	"TSHRS ='"+jQuery("#panelFields input[id='tshours']").val()+"' "+
	" where TSID='"+jQuery("#panelFields input[id='tsid']").val()+"'" +
			" and EMPID='"+jQuery("#panelFields input[id='empid']").val()+"'";
	
	var strURL = ctxstr+"/timesheetajax.action?returnajax=true&command=update&data=&sqlstring="+sqlstring1;
	$.ajax( {
		type: "GET",
		url: strURL,
		success: callbakaddts
	} );
}

function deleteTs() {
	var sqlstring1 = " delete from TS_EMP_TIMESHEET where TSID='"+jQuery("#panelFields input[id='tsid']").val()+"'" +
			" and EMPID='"+jQuery("#panelFields input[id='empid']").val()+"'";
	
	var strURL = ctxstr+"/timesheetajax.action?returnajax=true&command=update&data=&sqlstring="+sqlstring1;
	$.ajax( {
		type: "GET",
		url: strURL,
		success: callbakaddts
	} );
}

function selectAll(){ 
	var sqlstring1 = "select TSID,EMPID,EMPNAME,TSDATE,TSHRS,TSKID from TS_EMP_TIMESHEET";
	var strURL = ctxstr+"/timesheetajax.action?returnajax=true&command=query&data=nothing&sqlstring="+sqlstring1;
	
	$.ajax( {
		type: "GET",
		url: strURL,
		success: callbakselallts
	} );
}


function callbakselallts(args) {
	 
	var json = JSON.parse(args);
	if( typeof(json.result ) != 'undefined' && json.result.indexOf("ERROR") )alert("Error:"+json.result);
	
	createDynTable("TSID,EMPID,EMPNAME,TSDATE,TSHRS,TSKID",json);
	
}

/**
 * Creates list all timesheet table
 * @param cols
 * @param jsondata
 */
function createDynTable(cols, jsondata){
	var colar = cols.split(",");
	var htmlstr = "<table>";
	
	htmlstr +="<tr>";
	for(var j=0 ; j< colar.length; j++){
		htmlstr +="<th>"+colar[j]+"</th>";
	}
	htmlstr +="</tr>";
	
	for(var i =0 ; i < jsondata.length;i++){
		htmlstr +="<tr>";
		for(var j=0 ; j< colar.length; j++){
			htmlstr +="<td>"+eval("jsondata["+i+"]."+colar[j])+"</td>";
		}
		htmlstr +="</tr>";
	}
	htmlstr +="</table>";
	
	$("#list").html(htmlstr);
	
	addSelectEvents("list");
}



function addSelectEvents(param){
	 
	var srchdv = document.getElementById(param).getElementsByTagName("TR");

	for (var i =0;i< srchdv.length;i++) {
	if(srchdv[i].childNodes[0].tagName != "TH"){ 
			srchdv[i].onclick=function(p){
			 cleanUp(this);
			 selectedIdx[param]  = this.rowIndex;
			  this.style.backgroundColor= "#D6F1A3";
				}
			}
		}
}

var selectedIdx = new Array();

function cleanUp(obj) {
	/*var arobj = document.getElementById("searchdiv").getElementsByTagName("TR");

	for (var i =1 ; i < arobj.length; i++) {
		arobj[i].style.backgroundColor= "#E7FFBF";
	}*/
	query = jQuery(obj.parentNode).find("tr");
	jQuery.each(query,function(index,item){
		item.style.backgroundColor= "#E7FFBF";
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



function replacer(key, value) {
	if (typeof value === 'number' && !isFinite(value)) {
		return String(value);
	}
	return value;
}

