/**
 * jQuery("#panelFields input[type='text']").each(function(){console.log(this.id+": "+$(this).val());})
 * @author Samarjit
 */

var screenMode = "select";
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
	createDynTable("TSID,EMPID,EMPNAME,TSDATE,TSHRS,TSKID",json);
	
}

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
	
	addSelectEvents();
}



function addSelectEvents() {

	var srchdv = document.getElementById("list").getElementsByTagName("TR");

	for ( var i = 0; i < srchdv.length; i++) {
		if (srchdv[i].childNodes[0].tagName != "TH") {
			srchdv[i].onclick = function(p) {
				cleanUp();
				selectedIdx = this.rowIndex;
				this.style.backgroundColor = "#D6F1A3";
			}
		}
	}
} 

var selectedIdx = -1;

function cleanUp() {
	var arobj = document.getElementById("list").getElementsByTagName("TR");

	for ( var i = 1; i < arobj.length; i++) {
		arobj[i].style.backgroundColor = "#E7FFBF";
	}
} 
