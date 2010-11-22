window.onload = refresh;

function refresh() {
}

var New_Hardware = 1;
var New_Software = 2;
var Hardware_Upgrade = 3;
var Software_Upgrade = 4;
var AMC_Renewal = 5;
var Pc_Transfer = 6;
var Release_Resource = 7;
var Software_Transfer = 8;


function populate()
{
	
	
	if((!(whereClause == ""))){
		//whereclause = decodeURIComponent(whereClause);
		var url = jsrpcurlpart+"?screenName="+screenName+"&rpcid=createRequest";		
		sendAjaxGet(url, approvalRequestCallBack);
		
		var url=retriveurlpart+"?panelName=searchPanel&screenName="+screenName;	
		url=url+"&whereClause="+ whereClause;		
		//alert("In message: whereClause=" + screenName);
		// prompt("url",url);

		sendAjaxGet(url, approvalRequestCallBackview);
		
	}	
	else {
		// var url =		
		// prepopulateurlpart+"?panelName=searchPanel&prepopulate=true&screenName="+screenName;
		var url = jsrpcurlpart+"?screenName="+screenName+"&rpcid=createRequest";
		sendAjaxGet(url, approvalRequestCallBack);
		// alert("This alert box was called with the onload event");
	}
	
}
//var screenMode = "insert";


function approvalRequestCallBack(parm){
	var jobj = JSON.parse(parm);
	if(jobj.error != null ){
		alert(jobj.error);return;
	} 
	document.getElementById("empid").value = jobj.empid;

	document.getElementById("empname").value = jobj.empname;
	document.getElementById("reqdate").value = jobj.reqdate;

	var mgrid = (jobj.mgrid).split(",");
	for(var i=0;i<mgrid.length;i++){

		AddSelectOption(document.getElementById("mgrid"),mgrid[i],mgrid[i],false);
	}


	var ref_reqid = (jobj.REF_REQID).split(",");
	for(var i=0;i<ref_reqid.length;i++){

		AddSelectOption(document.getElementById("refreqid"),ref_reqid[i],ref_reqid[i],false);
	}
	var transferType = (jobj.transfertype).split(",");
	for(var i=0;i<transferType.length;i++){
		AddSelectOption(document.getElementById("transfertypesel"),transferType[i],transferType[i],false);
	}
	fnAdjustTableWidth();
}

function approvalRequestCallBackview(p){
	
	//alert("Got from ajax:"+p);
	var json = JSON.parse(p);
	//alert(json.message);
	if(json.error !=null ){
		showerror(json.error);
	}
	else {
		//alert(jason.message);
		p = decodeURIComponent(json.message);
		//alert(p);
		document.getElementById("retreivedetailsdiv").innerHTML = p;	
		panelsTable = document.getElementById("panelsdiv").getElementsByTagName("table");
		// alert(panelsTable.length);


		detailTable    = document.getElementById("retreivedetailsdiv").getElementsByTagName("table");

		for ( var i=0; i<detailTable.length ; i++)
		{
			if (detailTable[i].id == 'buttonPanel' || detailTable[i].rows.length==0)
				continue;
			for(var k = 0; k<detailTable[i].rows[0].cells.length; k++) {			
			// detailTable[i].rows[0].cells[k].childNodes[0].innerText.split(',')[2];

				comStr=jQuery.trim(jQuery(detailTable[i].rows[0].cells[k]).find("div").text()).split(',')[2];
				// alert(jQuery(detailTable[i].rows[0].cells[k]).find("div").text());
				comVal = jQuery.trim(jQuery(detailTable[i].rows[1].cells[k]).text());	  

				// comVal = detailTable[i].rows[1].cells[k].innerText;
				for(var l = 0; l<panelsTable.length; l++)
				{ 
					// alert(panelsTable[i].id);
					if (panelsTable[l].id == 'buttonPanel')
						continue;
					if(detailTable[i].id == panelsTable[l].id)
					{ 
						/* var input = panelsTable[l].getElementsByTagName("input"); */
						var query = jQuery(panelsTable[l]).find(" :input");
						var elem = 	jQuery(query);

						jQuery.each(elem, function(index, item) {

							if(item.id == comStr){
								jQuery(item).val(comVal);
							}
						});
//						for( var m = 0 ; m < input.length; m++)
//						{
//						if(input[m].id == comStr)
//						{
//						//alert(comStr +" "+comVal +" " +panelsTable[l].id + " " +
//						detailTable[i].id);
//						input[m].value = comVal;
//						}
//						}
					}
				}
			}
		}
		var selType = document.getElementById("requesttype").selectedIndex;
		if(selType==New_Hardware){
			document.getElementById("HardwareTypes").style.display = "block";
			if(document.getElementById("newhardwaretype").value=="PC" || document.getElementById("newhardwaretype").value=="LAP" ){
				document.getElementById("NewHardware").style.display = "block";
			}
			else
				{
				document.getElementById("GeneralHardware").style.display = "block"; 
				}

			document.getElementById("Software").style.display = "none";
			document.getElementById("HardwareUpgrade").style.display = "none"; 
			document.getElementById("AmcRenewal").style.display = "none"; 
			document.getElementById("PcTransferType").style.display = "none";
			document.getElementById("PcOneTransfer").style.display = "none";
			document.getElementById("PcSwapTransfer").style.display = "none";
			document.getElementById("ReleaseResource").style.display = "none";
			document.getElementById("SoftwareTransfer").style.display = "none";


		} 

		if(selType == New_Software || selType == Software_Upgrade){

			document.getElementById("Software").style.display = "block";
			document.getElementById("NewHardware").style.display = "none";
			document.getElementById("HardwareUpgrade").style.display = "none";
			document.getElementById("HardwareTypes").style.display = "none";
			document.getElementById("GeneralHardware").style.display = "none"; 
			document.getElementById("AmcRenewal").style.display = "none"; 
			document.getElementById("PcTransferType").style.display = "none";
			document.getElementById("PcSwapTransfer").style.display = "none";
			document.getElementById("PcOneTransfer").style.display = "none";
			document.getElementById("ReleaseResource").style.display = "none";
			document.getElementById("SoftwareTransfer").style.display = "none";

		}

		if(selType==Hardware_Upgrade){

			document.getElementById("NewHardware").style.display = "none";
			document.getElementById("Software").style.display = "none";
			document.getElementById("HardwareUpgrade").style.display = "block";
			document.getElementById("HardwareTypes").style.display = "none";
			document.getElementById("GeneralHardware").style.display = "none"; 
			document.getElementById("AmcRenewal").style.display = "none"; 
			document.getElementById("PcTransferType").style.display = "none";
			document.getElementById("PcSwapTransfer").style.display = "none";
			document.getElementById("PcOneTransfer").style.display = "none";
			document.getElementById("ReleaseResource").style.display = "none";
			document.getElementById("SoftwareTransfer").style.display = "none";

		}

		if(selType==AMC_Renewal){

			document.getElementById("AmcRenewal").style.display = "block";
			document.getElementById("NewHardware").style.display = "none";
			document.getElementById("Software").style.display = "none";
			document.getElementById("HardwareUpgrade").style.display = "none";
			document.getElementById("HardwareTypes").style.display = "none";
			document.getElementById("GeneralHardware").style.display = "none"; 
			document.getElementById("PcTransferType").style.display = "none";
			document.getElementById("PcSwapTransfer").style.display = "none";
			document.getElementById("PcOneTransfer").style.display = "none";
			document.getElementById("ReleaseResource").style.display = "none";
			document.getElementById("SoftwareTransfer").style.display = "none";
		}

		if(selType==Pc_Transfer){
			if(document.getElementById("transfertypeswap").value=="One Way"){
				document.getElementById("PcOneTransfer").style.display = "block";
			}else
			{
				document.getElementById("PcSwapTransfer").style.display = "block";

			}

			document.getElementById("PcTransferType").style.display = "block";
			document.getElementById("NewHardware").style.display = "none";
			document.getElementById("Software").style.display = "none";
			document.getElementById("HardwareUpgrade").style.display = "none";
			document.getElementById("HardwareTypes").style.display = "none";
			document.getElementById("GeneralHardware").style.display = "none"; 
			document.getElementById("AmcRenewal").style.display = "none"; 
			document.getElementById("ReleaseResource").style.display = "none";
			document.getElementById("SoftwareTransfer").style.display = "none";
		}

		if(selType==Release_Resource){

			document.getElementById("ReleaseResource").style.display = "block";
			document.getElementById("NewHardware").style.display = "none";
			document.getElementById("Software").style.display = "none";
			document.getElementById("HardwareUpgrade").style.display = "none";
			document.getElementById("HardwareTypes").style.display = "none";
			document.getElementById("GeneralHardware").style.display = "none"; 
			document.getElementById("AmcRenewal").style.display = "none"; 
			document.getElementById("PcTransferType").style.display = "none";
			document.getElementById("PcSwapTransfer").style.display = "none";
			document.getElementById("PcOneTransfer").style.display = "none";
			document.getElementById("SoftwareTransfer").style.display = "none";

		}

		if(selType==Software_Transfer){
			document.getElementById("SoftwareTransfer").style.display = "block";
			document.getElementById("NewHardware").style.display = "none";
			document.getElementById("Software").style.display = "none";
			document.getElementById("HardwareUpgrade").style.display = "none";
			document.getElementById("HardwareTypes").style.display = "none";
			document.getElementById("GeneralHardware").style.display = "none"; 
			document.getElementById("AmcRenewal").style.display = "none"; 
			document.getElementById("PcTransferType").style.display = "none";
			document.getElementById("PcSwapTransfer").style.display = "none";
			document.getElementById("PcOneTransfer").style.display = "none";
			document.getElementById("ReleaseResource").style.display = "none";


		}
	}


	fnAdjustTableWidth();
	disable_fields();
	


}





function generatename(obj){
	var selIndex = obj.selectedIndex;
	var empid = obj.options[selIndex].value;
	if(selIndex!=0){
		var url = jsrpcurlpart+"?screenName="+screenName+"&empid="+empid+"&rpcid=getManager";
		sendAjaxGet(url, generaterequestname);
	}
}

function generaterequestname(p){
	var jobj = JSON.parse(p);
	document.getElementById("mgrname").value = jobj.mgrname;	
}
/*
 * function generatecall(obj){ var selIndex = obj.selectedIndex;
 * alert(selIndex); scrName = obj.options[selIndex].value; if(selIndex!=0){
 * var url = generateurlpart+"?screenName="+scrName+"&ajaxPopulate=true";
 * sendAjaxGet(url, generaterequest); } }
 * 
 * function generaterequest(p){
 * 
 * document.getElementById("retreivedetailschilddiv").innerHTML = p; }
 */

function generatesubcallforhardware(obj){

	selIndexx = obj.selectedIndex;
	typee = obj.options[selIndexx].value;
	if(selIndexx!=0) {
		if(typee=='PC' || typee=='LAP'){
			document.getElementById("NewHardware").style.display = "block";
			document.getElementById("Software").style.display = "none";
			document.getElementById("HardwareUpgrade").style.display = "none"; 
			document.getElementById("GeneralHardware").style.display = "none";


		} 

		else{

			document.getElementById("GeneralHardware").style.display = "block";
			document.getElementById("NewHardware").style.display = "none";
			document.getElementById("Software").style.display = "none";
			document.getElementById("HardwareUpgrade").style.display = "none"; 


		}

	}
}


function generatesubcallfortransfer(obj){

	selIndextrans = obj.selectedIndex;
	typetrans = obj.options[selIndextrans].value;
	if(selIndextrans!=0) {
		if(typetrans=='One Way'){

			document.getElementById("PcOneTransfer").style.display = "block";
			document.getElementById("PcSwapTransfer").style.display = "none";
			document.getElementById("NewHardware").style.display = "none";
			document.getElementById("Software").style.display = "none";
			document.getElementById("HardwareUpgrade").style.display = "none";
			document.getElementById("HardwareTypes").style.display = "none";
			document.getElementById("GeneralHardware").style.display = "none"; 
			document.getElementById("AmcRenewal").style.display = "none"; 

		} 

		if(typetrans=='Two Way'){

			document.getElementById("PcSwapTransfer").style.display = "block";
			document.getElementById("PcOneTransfer").style.display = "none";
			document.getElementById("NewHardware").style.display = "none";
			document.getElementById("Software").style.display = "none";
			document.getElementById("HardwareUpgrade").style.display = "none";
			document.getElementById("HardwareTypes").style.display = "none";
			document.getElementById("GeneralHardware").style.display = "none"; 
			document.getElementById("AmcRenewal").style.display = "none"; 

		}
	}
}



function generatecall(obj){
	selIndex = obj.selectedIndex;
	type = obj.options[selIndex].value;
	if(selIndex==0) {

		document.getElementById("HardwareTypes").style.display = "none";
		document.getElementById("NewHardware").style.display = "none";
		document.getElementById("Software").style.display = "none";
		document.getElementById("HardwareUpgrade").style.display = "none"; 
		document.getElementById("GeneralHardware").style.display = "none"; 
		document.getElementById("AmcRenewal").style.display = "none"; 
		document.getElementById("PcTransferType").style.display = "none";
		document.getElementById("PcOneTransfer").style.display = "none";
		document.getElementById("PcSwapTransfer").style.display = "none";
		document.getElementById("ReleaseResource").style.display = "none";
		document.getElementById("SoftwareTransfer").style.display = "none";
	}



	if(selIndex==New_Hardware){

		document.getElementById("HardwareTypes").style.display = "block";
		document.getElementById("NewHardware").style.display = "none";
		document.getElementById("Software").style.display = "none";
		document.getElementById("HardwareUpgrade").style.display = "none"; 
		document.getElementById("GeneralHardware").style.display = "none"; 
		document.getElementById("AmcRenewal").style.display = "none"; 
		document.getElementById("PcTransferType").style.display = "none";
		document.getElementById("PcOneTransfer").style.display = "none";
		document.getElementById("PcSwapTransfer").style.display = "none";
		document.getElementById("ReleaseResource").style.display = "none";
		document.getElementById("SoftwareTransfer").style.display = "none";


	} 

	if(selIndex == New_Software || selIndex == Software_Upgrade){

		document.getElementById("Software").style.display = "block";
		document.getElementById("NewHardware").style.display = "none";
		document.getElementById("HardwareUpgrade").style.display = "none";
		document.getElementById("HardwareTypes").style.display = "none";
		document.getElementById("GeneralHardware").style.display = "none"; 
		document.getElementById("AmcRenewal").style.display = "none"; 
		document.getElementById("PcTransferType").style.display = "none";
		document.getElementById("PcSwapTransfer").style.display = "none";
		document.getElementById("PcOneTransfer").style.display = "none";
		document.getElementById("ReleaseResource").style.display = "none";
		document.getElementById("SoftwareTransfer").style.display = "none";

	}

	if(selIndex==Hardware_Upgrade){

		document.getElementById("NewHardware").style.display = "none";
		document.getElementById("Software").style.display = "none";
		document.getElementById("HardwareUpgrade").style.display = "block";
		document.getElementById("HardwareTypes").style.display = "none";
		document.getElementById("GeneralHardware").style.display = "none"; 
		document.getElementById("AmcRenewal").style.display = "none"; 
		document.getElementById("PcTransferType").style.display = "none";
		document.getElementById("PcSwapTransfer").style.display = "none";
		document.getElementById("PcOneTransfer").style.display = "none";
		document.getElementById("ReleaseResource").style.display = "none";
		document.getElementById("SoftwareTransfer").style.display = "none";

	}

	if(selIndex==AMC_Renewal){

		document.getElementById("AmcRenewal").style.display = "block";
		document.getElementById("NewHardware").style.display = "none";
		document.getElementById("Software").style.display = "none";
		document.getElementById("HardwareUpgrade").style.display = "none";
		document.getElementById("HardwareTypes").style.display = "none";
		document.getElementById("GeneralHardware").style.display = "none"; 
		document.getElementById("PcTransferType").style.display = "none";
		document.getElementById("PcSwapTransfer").style.display = "none";
		document.getElementById("PcOneTransfer").style.display = "none";
		document.getElementById("ReleaseResource").style.display = "none";
		document.getElementById("SoftwareTransfer").style.display = "none";
	}

	if(selIndex==Pc_Transfer){

		document.getElementById("PcTransferType").style.display = "block";
		document.getElementById("NewHardware").style.display = "none";
		document.getElementById("Software").style.display = "none";
		document.getElementById("HardwareUpgrade").style.display = "none";
		document.getElementById("HardwareTypes").style.display = "none";
		document.getElementById("GeneralHardware").style.display = "none"; 
		document.getElementById("AmcRenewal").style.display = "none"; 
		document.getElementById("ReleaseResource").style.display = "none";
		document.getElementById("SoftwareTransfer").style.display = "none";
	}

	if(selIndex==Release_Resource){

		document.getElementById("ReleaseResource").style.display = "block";
		document.getElementById("NewHardware").style.display = "none";
		document.getElementById("Software").style.display = "none";
		document.getElementById("HardwareUpgrade").style.display = "none";
		document.getElementById("HardwareTypes").style.display = "none";
		document.getElementById("GeneralHardware").style.display = "none"; 
		document.getElementById("AmcRenewal").style.display = "none"; 
		document.getElementById("PcTransferType").style.display = "none";
		document.getElementById("PcSwapTransfer").style.display = "none";
		document.getElementById("PcOneTransfer").style.display = "none";
		document.getElementById("SoftwareTransfer").style.display = "none";

	}

	if(selIndex==Software_Transfer){
		document.getElementById("SoftwareTransfer").style.display = "block";
		document.getElementById("NewHardware").style.display = "none";
		document.getElementById("Software").style.display = "none";
		document.getElementById("HardwareUpgrade").style.display = "none";
		document.getElementById("HardwareTypes").style.display = "none";
		document.getElementById("GeneralHardware").style.display = "none"; 
		document.getElementById("AmcRenewal").style.display = "none"; 
		document.getElementById("PcTransferType").style.display = "none";
		document.getElementById("PcSwapTransfer").style.display = "none";
		document.getElementById("PcOneTransfer").style.display = "none";
		document.getElementById("ReleaseResource").style.display = "none";


	}
}


function clearWhereClause(){
	document.getElementById("panelFieldsWhereClause").Value = "";
}


function AddSelectOption(selectObj, text, value, isSelected) 
{
	if (selectObj != null && selectObj.options != null)
	{
		selectObj.options[selectObj.options.length] = 
			new Option(text, value, false, isSelected);
	}
}

function disable_fields(){

	panelsTable = document.getElementById("panelsdiv").getElementsByTagName("table");
	for(var i =0; i<panelsTable.length;i++){
		if (panelsTable[i].id != 'buttonPanel'){

			var query = jQuery(panelsTable[i]).find(" :input");
			var elem = 	jQuery(query);

			jQuery.each(elem, function(index, item) {
				item.disabled = true;
			});

		}
	}
	document.getElementById("reqid").style.visibility = 'visible'; 
	document.getElementById("status").style.visibility = 'visible'; 
	
	if(document.getElementById("status").value=="APPROVED" || document.getElementById("status").value=="REJECTED"){
		
		document.getElementById("btnApprove").disabled = true;
		document.getElementById("btnReject").disabled = true;		
	}
	else if (document.getElementById("status").value=="PENDAPPROVAL"){
		document.getElementById("remarks").disabled = false;
		
	}
	

}

function enable_fields(){
	screenMode = "modify";

	var updateonar = "mgrid,mgrname,departmentname,remarks,user1,assetidhu,transfertypeone,userid1,assetid1,userid2,assetid2,transfertypeswap,assetidsw,description,assetname,userid,assetidamc,hardwaretype,assetidpco,assettype,ramnh,harddisk,descriptionnh,processor,refreqid,assetidrr,software,descriptionhu,processorhu,ramhu,hdd,make,quantity,descriptiongh,hardwaretype,quantity,hardwaretype,descriptionsw,assetidst,deliverynote,descriptionamc,transfertypesel,assetid,descriptionrr,ram,processornh,btnupdate".split(",");
	for ( var i = 0; i < updateonar.length; i++) {
		var arelm = updateonar[i];
		jQuery("#"+ arelm).attr('disabled','');
	}
	document.getElementById("btnmodify").disabled = "true";
	document.getElementById("btndelete").disabled = "true";
	document.getElementById("btnsubmit").disabled = "true";


}

function insertData() {
	var dataTable = document.getElementById("panelsdiv").getElementsByTagName("table");
}


function reqSubmit() {

	prepareInsertData();
}

function reqApproval() {

	var applicationid = document.getElementById("reqid").value;
//	alert(applicationid);
	var actionid =  document.getElementById("wflactiondesc").value;
	var wflid= document.getElementById("wflid").value;
	var reqid= document.getElementById("reqid").value;
	var comments= document.getElementById("remarks").value;
////	
//	
//	alert(actionid); 
//	alert(wflid);
//	alert(comments); 
//	alert(reqid);
//	//exit();
	
	var url = "scrworkflow.action?doString="+actionid+"&wflid="+wflid+"&appid="+applicationid+"&screenName=frmApproval&reqcomments="+comments+"&reqid="+ reqid + "&approve=true&ajaxflag=true"  ;
	sendAjaxGet(url, approvalCallBack);
}

function approvalCallBack(val) {
	//show success message
	//alert(workflowurl);
	//showalert("The record is approved successfully");
	alert("The record is approved successfully");
	location.href = ctxpath+"/template1.action?screenName=frmApprovalList";
}


function reqReject() {
	//alert("In reject");
	var applicationid = document.getElementById("reqid").value;
	//alert(applicationid);	
	var actionid =  document.getElementById("wflactiondesc").value;
	var wflid= document.getElementById("wflid").value;
	var reqid= document.getElementById("reqid").value;
	var comments= document.getElementById("remarks").value;
	//alert(rrfid);
	var url = "scrworkflow.action?doString="+actionid+"&wflid="+wflid+"&appid="+applicationid+"&screenName=frmApproval&reqcomments="+comments+"&reqid="+ reqid + "&approve=false&ajaxflag=false"  ;
	//alert(url);
	//exit();
	sendAjaxGet(url, rejectCallBack);
	
}

function backToList(){
	location.href= ctxpath+"/template1.action?screenName=frmApprovalList"
}


function rejectCallBack(val) {
	//show success message
	//alert("in save PO");
	//alert(workflowurl);
	//showalert("The record is rejected successfully");
	alert("The record is rejected successfully");
	location.href = ctxpath+"/template1.action?screenName=frmApprovalList";

}


function KeyValue(a,b) {
	this.key=a;
	this.value=b;
}

function panelClass(a,b) {
	this.name = a;
	this.valuesar = b;
}

function replacer(key, value) {
	if (typeof value === 'number' && !isFinite(value)) {
		return String(value);
	}
	return value;
}



function makeWhereClause(){
	 
	// alert("in make url,selectedIdx:"+selectedIdx);
	//There will be only one table in search screen 'search div'
	
	listTable = document.getElementById("retreivedetailsdiv").getElementsByTagName("table")[0];

	whereClause = "panelFields1WhereClause=";
	if(listTable != null){
		//poplate wher clause url
		var j=0;
		requestar = new Array();
		for (i = 0; i <listTable.rows[0].cells.length ; i++ )
		{  
			//alert(listTable.rows[0].cells[i].childNodes[0].innerText.split(',')[6]);
			if(jQuery("#retreivedetailsdiv").find(" table tbody tr th").eq(i).find(" div").text().split(',')[6]  == "Y") {
				name = jQuery("#retreivedetailsdiv").find(" table  tbody tr th").eq(i).find("div").text().split(',')[2];	 
				name = jQuery.trim(name);
				value = jQuery("#retreivedetailsdiv").find(" table tbody tr").eq(1).find(" td").eq(i).text();
				value = jQuery.trim(value);
				whereClause = whereClause + name + "!" + value + "~#";
				requestar[j] = new KeyValue(name, value);				
				j++;		
				//alert(jQuery("#retreivedetailsdiv table th:eq("+i+") div").text());
			}
		}
		var k = new Object();
		k.json = requestar;
		var myJSONText = JSON.stringify(k, replacer,"");
		//alert(myJSONText);
		whereClause = encodeURIComponent(myJSONText);//whereClause.replace(/(~#)$/, '');
		 
		 
		
	}
	
	return whereClause;	 

}


function fnAdjustTableWidth() {
	var tdwidthar = new Array();
	var alertstr="";
	jQuery.each(jQuery("#panelsdiv  table"),function(idx,elem){	
		 
		var query = jQuery(elem).eq(0).find("tr").eq(0).find("td ");
		jQuery.each(query, function(index, item) {
			//alertstr += (elem.id+" tdwidthar["+index+"]"+tdwidthar[index] + " "+jQuery(item).width())+"<br/>";
			if(!tdwidthar[index])tdwidthar[index]  = jQuery(item).width();
			else if(   tdwidthar[index] < jQuery(item).width())			{
				tdwidthar[index]  = jQuery(item).width();
			}
			 
				
		});
	});
	var j = 0 ;
	var maxtd = tdwidthar.length;
	//showerror(alertstr);

	var tblar = document.getElementById("panelsdiv").getElementsByTagName("table") ;
	for (var i=0; i<tblar.length; i++) {
		query = jQuery(tblar[i]).find("tr").eq(0).find("td");
		elem = 	jQuery(query);

		jQuery.each(query, function(index, item) {
			jQuery(item).width(tdwidthar[j]);
			j++;
			if(maxtd == j)j=0;

		});

	}



}

