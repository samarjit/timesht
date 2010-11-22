function search(obj){
	try{//will cause error while loading
		if(obj.parentNode.id == 'searchdiv')search1();
	if(obj.parentNode.id == 'searchdiv2')search2();
	if(obj.parentNode.id == 'searchdiv3')search3();
	}catch(e){}
}
jQuery(document).ready(function($) { 
	jQuery('.paneltable').find('tr:first').find('td').css("border-bottom","1px #618C04 solid");
search1();
//search2();
//search3();
});
function search1(){
	 
	var url=urlpart+"?panelName=searchPanel&screenName="+screenName;
	if(document.getElementById("reqid"))
		url=url+'&reqid='+document.getElementById("reqid").value;
	if(document.getElementById("reqtype"))
		url=url+'&reqtype='+document.getElementById("reqtype").value;
	if(document.getElementById("empid"))
		url=url+'&empid='+document.getElementById("empid").value;
	var status = jQuery('#searchdiv #status').val();
	if(status)
		url=url+'&status='+status;
	 
	var pagesize = jQuery('#searchdiv .pagesize').val();
	var pageno = jQuery('#searchdiv .pageno').val();
	
	if(pagesize)
		url=url+'&pagesize='+pagesize;
	if(pageno)
		url=url+'&pageno='+pageno;
	
	sendAjaxGet(url,mycall);
}
function mycall(p){
	//alert("Got from ajax:"+p);
	document.getElementById("searchdiv").innerHTML = p;
	addSelectEvents("searchdiv");
	search2();
}

function search2(){
	 
	var url=urlpart+"?panelName=searchPanelAsst&screenName="+screenName;
	 
	var assetid = jQuery('#searchdiv2 #assetid').val();
	if(assetid)
		url=url+'&assetid='+assetid;
	
	if(document.getElementById("assetname"))
		url=url+'&assetname='+document.getElementById("assetname").value;
	if(document.getElementById("assettype"))
		url=url+'&assettype='+document.getElementById("assettype").value;
	if(document.getElementById("make"))
		url=url+'&make='+document.getElementById("make").value;

	var status = jQuery('#searchdiv2 #status').val();
	if(status)
		url=url+'&status='+status;
	
	var pagesize = jQuery('#searchdiv2 .pagesize').val();
	var pageno = jQuery('#searchdiv2 .pageno').val();
	
	if(pagesize)
		url=url+'&pagesize='+pagesize;
	if(pageno)
		url=url+'&pageno='+pageno;
	
	sendAjaxGet(url,mycall2);
}
function mycall2(p){
	//alert("Got from ajax:"+p);
	document.getElementById("searchdiv2").innerHTML = p;
	addSelectEvents("searchdiv2");
	search3();
}
function search3(){
	 
	var url=urlpart+"?panelName=searchPanelAlloc&screenName="+screenName;
	var assetid = jQuery('#searchPanelAlloc #assetid').val();
	if(assetid)url=url+'&assetid='+assetid;
	if(document.getElementById("assetno"))
		url=url+'&assetno='+document.getElementById("assetno").value;
	if(document.getElementById("assethost"))
		url=url+'&assethost='+document.getElementById("assethost").value;
	if(document.getElementById("username"))
		url=url+'&username='+document.getElementById("username").value;
	if(document.getElementById("assetip"))
		url=url+'&assetip='+document.getElementById("assetip").value;	
	if(document.getElementById("allocid"))
		url=url+'&allocid='+document.getElementById("allocid").value;
	var pagesize = jQuery('#searchdiv3 .pagesize').val();
	var pageno = jQuery('#searchdiv3 .pageno').val();
	
	if(pagesize)
		url=url+'&pagesize='+pagesize;
	if(pageno)
		url=url+'&pageno='+pageno;
	
	sendAjaxGet(url,mycall3);
}
function mycall3(p){
	//alert("Got from ajax:"+p);
	document.getElementById("searchdiv3").innerHTML = p;
	addSelectEvents("searchdiv3");
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
 

//create url with where clause
function KeyValue(a,b) {
	this.key=a;
	this.value=b;
}

function replacer(key, value) {
	if (typeof value === 'number' && !isFinite(value)) {
		return String(value);
	}
	return value;
}


function viewdetails(btnname){
 
	// alert("in make url,selectedIdx:"+selectedIdx);
	//There will be only one table in search screen 'search div'
	//alert(btnname.id);
	
	listTable = document.getElementById("searchdiv3").getElementsByTagName("table")[0];

	whereClause = "panelFields1WhereClause=";
	if(listTable != null && selectedIdx["searchdiv3"] != -1){
		//poplate wher clause url
		var j=0;
		requestar = new Array();
		for (i = 0; i <listTable.rows[0].cells.length ; i++ )
		{  
			//alert(listTable.rows[0].cells[i].childNodes[0].innerText.split(',')[6]);
			if(jQuery("#searchdiv3").find(" table tbody tr th").eq(i).find(" div").text().split(',')[6]  == "Y") {
				name = jQuery("#searchdiv3").find(" table  tbody tr th").eq(i).find("div").text().split(',')[2];	 
				name = jQuery.trim(name);
				value = jQuery("#searchdiv3").find(" table tbody tr").eq(selectedIdx["searchdiv3"]).find(" td").eq(i).text();
				value = jQuery.trim(value);
				whereClause = whereClause + name + "!" + value + "~#";
				requestar[j] = new KeyValue(name, value);				
				j++;		
				//alert(jQuery("#searchdiv table th:eq("+i+") div").text());
			}
		}
		var k = new Object();
		k.json = requestar;
		var myJSONText = JSON.stringify(k, replacer,"");
		//alert(myJSONText);
		whereClause = encodeURIComponent(myJSONText);//whereClause.replace(/(~#)$/, '');
		document.getElementById("panelFieldsWhereClause").value=whereClause;
		
		{
			//alert("in view details");			
			document.getElementById("formwhere").screenName.value = "frmAllocation";
			document.getElementById("screenMode").value= "view";
			//document.getElementById("formwhere").submit();
		}
		
		document.getElementById("formwhere").submit();
	}
	else {
		showalert("select a record first");
		return false;
	}
	return true;	 

}

function createAllocfromSel(){
	document.getElementById("formwhere").screenName.value = "frmAllocation";
	document.getElementById("screenMode").value= "create";
	
	if(typeof selectedIdx["searchdiv"] == 'undefined' ||typeof  selectedIdx["searchdiv2"] == 'undefined'){
		alert("please select request and asset");
		return;
	}
	var reqid = getSelectedRowData("searchdiv","reqid");
	var reqstatus = getSelectedRowData("searchdiv","status");
	var empid = getSelectedRowData("searchdiv","empid");
	var assetid = getSelectedRowData("searchdiv2","assetid");
	var k = new Object();
	k.reqid = reqid;
	k.empid = empid;
	k.assetid = assetid;
 
	if(reqstatus != "APPROVED"){
		showalert("Request Status must be APPROVED");
		return;
	}
	var myJSONText = JSON.stringify(k, replacer,"");
	jQuery('#passedonvalues').val(myJSONText);
	document.getElementById("formwhere").submit();
}
function getSelectedRowData(searchdivId){
	var tablerowdataar = new Array();
	//jQuery("#searchdiv1 table th div").attr("id");
	var query = jQuery("#"+searchdivId+" table th");
	jQuery.each(query,function(index,item){
		var id = jQuery(item).find("div").attr("id");
		tablerowdataar[index] = new KeyValue(id,jQuery("#"+searchdivId+" table tr").eq(selectedIdx[searchdivId]).find("td").eq(index).text());
	});
	return tablerowdataar;
}
function getSelectedRowData(searchdivId,colname){
	var celldata="";
	//jQuery("#searchdiv1 table th div").attr("id");
	var query = jQuery("#"+searchdivId+" table th");
	jQuery.each(query,function(index,item){
		if(colname == jQuery(item).find("div").attr("id"))
			celldata  =  jQuery("#"+searchdivId+" table tr").eq(selectedIdx[searchdivId]).find("td").eq(index).text();
	});
	celldata = jQuery.trim(celldata);
	return celldata;
}
//create url with where clause
function clearWhereClause(){
	document.getElementById("panelFieldsWhereClause").value="";
}


