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
var Miscellaneous = 9;



function populate()
{

	if((!(whereClause == ""))){
		//whereclause = decodeURIComponent(whereClause);
		var url = jsrpcurlpart+"?screenName="+screenName+"&rpcid=createRequest";
		sendAjaxGet(url, requestCallBack);
		var url=retriveurlpart+"?panelName=searchPanel&screenName="+screenName;	
		url=url+"&whereClause="+ whereClause;		
		// alert("In message: whereClause=" + whereClause);
		// prompt("url",url);
		sendAjaxGet(url, requestCallBackview);

	}	
	else {
		// var url =
		// prepopulateurlpart+"?panelName=searchPanel&prepopulate=true&screenName="+screenName;
		var url = jsrpcurlpart+"?screenName="+screenName+"&rpcid=createRequest";
		sendAjaxGet(url, requestCallBack);
		// alert("This alert box was called with the onload event");
	}


}
var screenMode = "insert";


function requestCallBack(parm){
	fnAdjustTableWidth();

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

}

function requestCallBackview(p){
	fnAdjustTableWidth();
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
			document.getElementById("Misc").style.display = "none";



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
			document.getElementById("Misc").style.display = "none";


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
			document.getElementById("Misc").style.display = "none";


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
			document.getElementById("Misc").style.display = "none";

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
			document.getElementById("Misc").style.display = "none";

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
			document.getElementById("Misc").style.display = "none";


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
			document.getElementById("Misc").style.display = "none";



		}
		
		if(selType==Miscellaneous){
			document.getElementById("Misc").style.display = "block";
			document.getElementById("SoftwareTransfer").style.display = "none";
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

function sameScreen(){
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

function generatedepartmentname(p){
	var jobj = JSON.parse(p);
	document.getElementById("departmentname").value = jobj.deptname;	

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
		document.getElementById("Misc").style.display = "none";

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
		document.getElementById("Misc").style.display = "none";



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
		document.getElementById("Misc").style.display = "none";


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
		document.getElementById("Misc").style.display = "none";


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
		document.getElementById("Misc").style.display = "none";

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
		document.getElementById("Misc").style.display = "none";

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
		document.getElementById("Misc").style.display = "none";


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
		document.getElementById("Misc").style.display = "none";

	}
	
	
	if(selIndex==Miscellaneous){
		document.getElementById("Misc").style.display = "block";
		document.getElementById("SoftwareTransfer").style.display = "none";
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
	
	if(selIndex!=0){
		var url = jsrpcurlpart+"?screenName="+screenName+"&requestType="+selIndex+"&rpcid=getDepartment";
		sendAjaxGet(url, generatedepartmentname);
	}
	
}


function clearWhereClause(){
	document.getElementById("panelFieldsWhereClause").Value = "";
}
/*
 * function requestCallBack(p){ alert("Got from ajax:"+p);
 * 
 * document.getElementById("retreivedetailsdiv").innerHTML = p; panelsTable =
 * document.getElementById("panelsdiv").getElementsByTagName("table");
 * //alert(panelsTable.length);
 * 
 * 
 * detailTable =
 * document.getElementById("retreivedetailsdiv").getElementsByTagName("table");
 * 
 * for ( var i=0; i<detailTable.length ; i++) { //alert(detailTable[i].id); if
 * (detailTable[i].id == 'buttonPanel') continue; for(var k = 0; k<detailTable[i].rows[0].cells.length;
 * k++) { //comStr =
 * detailTable[i].rows[0].cells[k].childNodes[0].innerText.split(',')[2];
 * 
 * comStr=jQuery.trim(jQuery(detailTable[i].rows[0].cells[k]).find("div").text()).split(',')[2];
 * //alert(jQuery(detailTable[i].rows[0].cells[k]).find("div").text()); comVal =
 * jQuery.trim(jQuery(detailTable[i].rows[1].cells[k]).text()); //comVal =
 * detailTable[i].rows[1].cells[k].innerText; for(var l = 0; l<panelsTable.length;
 * l++) { //alert(panelsTable[i].id); if (panelsTable[l].id == 'buttonPanel')
 * continue; if(detailTable[i].id == panelsTable[l].id) { var elms =
 * document.getElementById("panelsdiv").getElementsByTagName("*");
 * 
 * //var input = panelsTable[l].getElementsByTagName("input");
 * //alert(input.length);
 * 
 * for( var m = 0 ; m < elms.length; m++) { if(elms[m].id == comStr) {
 * switch(elms[m].type) {
 * 
 * case "text": elms[m].value = comVal;
 * 
 * case "select-one": { AddSelectOption(elms[m],comVal,comVal,false); for(var
 * f=2;f<detailTable[i].rows.length;f++){ comVal =
 * (jQuery.trim(jQuery(detailTable[i].rows[f].cells[k]).text()));
 * AddSelectOption(elms[m],comVal,comVal,false);
 *  } }
 *  } } } } } } }
 * 
 * //disable_fields();
 *  }
 */

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

		// alert("panels "+ panelsTable[i].id);
		if (panelsTable[i].id != 'buttonPanel'){

//			fields = panelsTable[i].getElementsByTagName("input");
//			for(var k = 0; k<fields.length; k++){

//			fields[k].disabled = true;

//			}
			var query = jQuery(panelsTable[i]).find(" :input");
			var elem = 	jQuery(query);

			jQuery.each(elem, function(index, item) {
				item.disabled = true;
			});

		}
	}
	document.getElementById("reqid").style.visibility = 'visible'; 
	document.getElementById("status").style.visibility = 'visible'; 



	if(document.getElementById("status").value=="NEW"){

		document.getElementById("btnmodify").disabled = false;
		document.getElementById("btnSave").disabled = true;
		document.getElementById("btnsubmit").disabled = false;
		document.getElementById("btndelete").disabled = false;
		document.getElementById("btnclose").disabled = true;


	}

	if(document.getElementById("status").value=="PENDAPPROVAL"){

		document.getElementById("btnmodify").disabled = true;
		document.getElementById("btnSave").disabled = true;
		document.getElementById("btnsubmit").disabled = true;
		document.getElementById("btndelete").disabled = true;
		document.getElementById("btncancel").disabled = false;
		document.getElementById("btnclose").disabled = true;

	}

	if(document.getElementById("status").value=="REJECTED" || document.getElementById("status").value=="CLOSED" || document.getElementById("status").value=="CANCELLED"){

		document.getElementById("btnmodify").disabled = true;
		document.getElementById("btnSave").disabled = true;
		document.getElementById("btnsubmit").disabled = true;
		document.getElementById("btndelete").disabled = true;
		document.getElementById("btncancel").disabled = true;
		document.getElementById("btnclose").disabled = true;
	}
	
	if(document.getElementById("status").value=="APPROVED"){

		document.getElementById("btnmodify").disabled = true;
		document.getElementById("btnSave").disabled = true;
		document.getElementById("btnsubmit").disabled = true;
		document.getElementById("btndelete").disabled = true;
		document.getElementById("btncancel").disabled = false;
		document.getElementById("btnclose").disabled = true;
	}
	
	if(document.getElementById("status").value=="RESOLVED"){

		document.getElementById("btnmodify").disabled = true;
		document.getElementById("btnSave").disabled = true;
		document.getElementById("btnsubmit").disabled = true;
		document.getElementById("btndelete").disabled = true;
		document.getElementById("btncancel").disabled = true;
		document.getElementById("btnclose").disabled = false;
	}
	
	

}

function enable_fields(){
	screenMode = "modify";

	var updateonar = "mgrid,remarks,itemname,user1,assetidhu,transfertypeone,userid1,assetid1,userid2,assetid2,transfertypeswap,assetidsw,description,assetname,userid,assetidamc,hardwaretype,assetidpco,assettype,ramnh,harddisk,descriptionnh,processor,refreqid,assetidrr,software,descriptionhu,descriptionmisc,processorhu,ramhu,hdd,make,quantitynh,descriptiongh,hardwaretype,quantitygh,quantitymi,hardwaretype,descriptionsw,assetidst,deliverynote,descriptionamc,transfertypesel,assetid,descriptionrr,ram,processornh".split(",");
	for ( var i = 0; i < updateonar.length; i++) {
		var arelm = updateonar[i];
		jQuery("#"+ arelm).attr('disabled','');
	}
	document.getElementById("btnmodify").disabled = true;
	document.getElementById("btndelete").disabled = true;
	document.getElementById("btnsubmit").disabled = true;
	document.getElementById("btnSave").disabled = false;



}

function insertData() {
	var dataTable = document.getElementById("panelsdiv").getElementsByTagName("table");
}


function reqSubmit() {

	prepareInsertData();
}

function reqSave() {

	if(screenMode == "modify"){
		whereclause  = makeWhereClause();
		var url=updateurlpart+"?wclause="+whereclause+"&screenName=frmRequest";
		url = url+ "&insertKeyValue="+ prepareInsertData();

		// prompt("url",url);
		// add key:vlaue to url


		sendAjaxGet(url, saveCallBack);
	} else {


		// alert("in savesdkgf ");
		// var
		// url=urlpart+"?panelName=searchPanel&screenName=frmRequest"+screenName;

		var url=inserturlpart+"?panelName=searchPanel&screenName=frmRequest";
		url = url+ "&insertKeyValue="+ prepareInsertData()+"&invokewfl=scrflow&activityname=CR&create=";
		//alert(url);
		// add key:vlaue to url
		sendAjaxGet(url, saveCallBack);

	}



}

function deleteData(){

	whereclause  = makeWhereClause();
	//alert(decodeURIComponent(whereClause));

	var url=deleteurlpart+"?wclause="+whereclause+"&screenName=frmRequest";
	//alert(decodeURIComponent(url));	
	// prompt("url",url);
	// add key:vlaue to url
	sendAjaxGet(url, saveCallBack);

}
function saveCallBack(val) {
	//show success message 

	var json = JSON.parse(val);
	if(json.error !=null ){
		showerror(json.error);
	}else {
		showalert(json.message);
		if(json.workflowurl != null){
			location.href = json.workflowurl ;
			
		}else{
			populate();
		}
	}

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


function prepareInsertData() {
	// var array =
	// {"panelFields1":{"empid":"9002","empname":"tutu","bdate":"12-10-2009"},"panelFields":{"empid":"9001","empname":"samarjit","bdate":"12-10-2009"}};
	var dataTable = document.getElementById("panelsdiv").getElementsByTagName("table");
	var pclass = new Array();
	var panelDataTable = new Array();
	var selIndex = document.getElementById("requesttype").selectedIndex;
	var typee = document.getElementById("hardwaretype").value;
	var typetrans = document.getElementById("transfertypesel").value;

	if(selIndex == New_Hardware && (typee=='PC' || typee=='LAP')){
		panelDataTable[0] = 'panelFields';
		panelDataTable[1] = 'NewHardware';
	}

	if(selIndex == New_Hardware && (typee!='PC' && typee!='LAP')){
		panelDataTable[0] = 'panelFields';
		panelDataTable[1] = 'GeneralHardware';
	}

	if(selIndex == Hardware_Upgrade){
		panelDataTable[0] = 'panelFields';
		panelDataTable[1] = 'HardwareUpgrade';
	}

	if(selIndex == AMC_Renewal){
		panelDataTable[0] = 'panelFields';
		panelDataTable[1] = 'AmcRenewal';
	}

	if(selIndex == Release_Resource){
		panelDataTable[0] = 'panelFields';
		panelDataTable[1] = 'ReleaseResource';

	}

	if(selIndex == Pc_Transfer && typetrans == 'One Way'){
		panelDataTable[0] = 'panelFields';
		panelDataTable[1] = 'PcOneTransfer';

	}

	if(selIndex == Pc_Transfer && typetrans == 'Two Way'){
		panelDataTable[0] = 'panelFields';
		panelDataTable[1] = 'PcSwapTransfer';

	}

	if(selIndex == New_Software || selIndex == Software_Upgrade){
		panelDataTable[0] = 'panelFields';
		panelDataTable[1] = 'Software';
	}

	if(selIndex == Software_Transfer){
		panelDataTable[0] = 'panelFields';
		panelDataTable[1] = 'SoftwareTransfer';
	}
	
	if(selIndex == Miscellaneous){
		panelDataTable[0] = 'panelFields';
		panelDataTable[1] = 'Misc';
	}


	panelDataTable[panelDataTable.length] = 'statusFields';


	// alert(dataTable.length);
	for (var i=0; i<panelDataTable.length; i++) {


		query = "#panelsdiv #" + panelDataTable[i] + " :input";
		//alert(query);
		var requestar = new Array();

		var elem = 	jQuery(query); 
		var j = 0;
		jQuery.each(elem, function(index, item) {	

			var val = item.id;

			if(screenMode != "modify"){

				if(val=='reqid'){
					item.value = "AUTOGEN_SEQUENCE_ID";}
			}
			if(item.type=="hidden" && ((val=='hardwaretype') || (val=='hardwaretypegeneral') || (val=='newhardwaretype'))){
				item.value = typee;}
			if(item.type=="hidden" && ((val=='transfertypeone') || (val=='transfertypeswap'))){
				item.value = typetrans;}
			if(val=='status'){
				item.value = 'NEW';
			}



			requestar[j] = new KeyValue(item.id, item.value);				
			j++;						
		});

		pclass[i] = new panelClass(panelDataTable[i],requestar);					
	}	

	var k = new Object();
	k.json = pclass;
	var myJSONText = JSON.stringify(k, replacer,"");
	//alert("myjson "+myJSONText);
	return myJSONText;			
}


function updateData(){
		// obj.disabled = true;
	screenMode = "modify";
	// There will be only one table in search screen 'search div'
	// document.requestFrm.submit();
	listTable = document.getElementById("retreivedetailsdiv").getElementsByTagName("table")[0];

	panelsTable = document.getElementById("panelsdiv").getElementsByTagName("table");

	for(var m =0; m<panelsTable.length;m++){

		if (panelsTable[m].id == 'panelFields'){

			fields = panelsTable[m].getElementsByTagName("input");
			// alert("inside update panel panels " + fields.length);
			for(var k = 0; k<fields.length; k++){
				// alert("inside panel panels " + fields[k].id);
				for (i = 0; i <listTable.rows[0].cells.length ; i++ )
				{
					// alert(fields[k].id);
					// alert(jQuery(listTable.rows[0].cells[i]).text());
					if(!(jQuery(listTable.rows[0].cells[i]).text().split(',')[6]  == 'Y')) {

						if(jQuery(listTable.rows[0].cells[i]).text().split(',')[3] == fields[k].id){

							fields[k].disabled = false;
						}
					}

					// for date
					if((jQuery(listTable.rows[0].cells[i]).text().split(',')[4] == 'DATE')) {

						if(jQuery(listTable.rows[0].cells[i]).text().split(',')[3] == fields[k].id){
							fields[k].disabled = true;
						}
					} 


				}

			}
		}

	}

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
	jQuery.each(jQuery("#panelsdiv  table"),function(idx,elem){	

		var query = jQuery(elem).eq(0).find("tr").eq(0).find("td ");
		jQuery.each(query, function(index, item) {
			//	alert(elem.id+" tdwidthar["+index+"]"+tdwidthar[index] + " "+jQuery(item).width());
			if(!tdwidthar[index])tdwidthar[index]  = jQuery(item).width();
			else if(   tdwidthar[index] < jQuery(item).width())			{
				tdwidthar[index]  = jQuery(item).width();
			}


		});
	});
	var j = 0 ;
	var maxtd = tdwidthar.length;


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



function submitScreenFlowactivity(){
	//alert("here in submit activity")
	//alert(wflcontrollerurl);
	/*
	var url = jsrpcurlpart+"?screenName="+screenName+"&rpcid=getEmail&mgrid="+document.getElementById("mgrid").options[document.getElementById("mgrid").selectedIndex].value;
	sendAjaxGet(url, beforeMail); */

	//document.getElementById("submitanchor").href //stealing from actionbutton.jsp its not the right way, if its coming from viewDetails this will be wrong anyway! 	
	if(validation()){
	var applicationid = jQuery("#panelsdiv #panelFields  input[id=reqid]").attr("value");
	var actionid =  jQuery("#panelsdiv #statusFields input[id=wflactiondesc]").attr("value");
	var wflid=jQuery("#panelsdiv #statusFields input[id=wflid]").attr("value");
	var url = "scrworkflow.action?cancel=false&action=true&doString="+actionid+"&wflid="+wflid+"&appid="+applicationid+"&screenName="+screenName+"&mgrid="+document.getElementById("mgrid").value+"&mgrname="+document.getElementById("mgrname").value+"&empname="+document.getElementById("empname").value;
	location.href = url;
	}
}

function cancelReq(){
	var applicationid = jQuery("#panelsdiv #panelFields  input[id=reqid]").attr("value");
	var actionid =  jQuery("#panelsdiv #statusFields input[id=wflactiondesc]").attr("value");
	var wflid=jQuery("#panelsdiv #statusFields input[id=wflid]").attr("value");
	var url = "scrworkflow.action?cancel=true&doString="+actionid+"&wflid="+wflid+"&appid="+applicationid+"&screenName="+screenName+"&reqid="+document.getElementById("reqid").value;

	sendAjaxGet(url, afterCancelclose);

}

function afterCancelclose(){
	window.location = "template1.action?screenName=frmRequestList";
}

function closeReq(){
	var url = jsrpcurlpart+"?screenName="+screenName+"&rpcid=close&reqid="+document.getElementById("reqid").value;
	sendAjaxGet(url, afterCancelclose);

}


function validation(){


	if(document.getElementById("mgrid").selectedIndex==0)
	{
		showerror("Select A Manager ID");
		return false;
	}

	var requestType = document.getElementById("requesttype").selectedIndex;
	if(requestType==0)
	{
		showerror("Select A Request Type");
		return false;
	}

	var requestType = document.getElementById("requesttype").selectedIndex;
	if(requestType==New_Hardware)
	{
		if(document.getElementById("hardwaretype").selectedIndex==0){
			showerror("Select A Hardware Type");
			return false;
		}

		if(document.getElementById("hardwaretype").selectedIndex==1 || document.getElementById("hardwaretype").selectedIndex==2){
			if(document.getElementById("processornh").length == 0 && document.getElementById("hdd").length == 0 && document.getElementById("quantitynh").length == 0 && document.getElementById("descriptionnh").length == 0 && document.getElementById("ramnh").length == 0 && document.getElementById("make").length == 0){

				showerror("Enter Hardware Details");
				return false;

			}
			
			if(document.getElementById("hardwaretype").selectedIndex!=1 && document.getElementById("hardwaretype").selectedIndex!=2){
				
				if(document.getElementById("quantitygh").length == 0){
					showerror("Enter Hardware Quantity");
					return false;
				}

				if(document.getElementById("quantitygh").length == 0 && document.getElementById("quantitygh").length == 0 ){
					showerror("Enter Hardware Details");
					return false;
				}

		}

		if(document.getElementById("quantitynh").length == 0){
			showerror("Enter Quantity");
			return false;
		}
	}
	}

	if(requestType==New_Software || requestType==Software_Upgrade)
	{
		if(document.getElementById("assetidamc").length==0){
			showerror("Select A Software");
			return false;
		}
	}

	if(requestType==AMC_Renewal)
	{

		if(document.getElementById("assetidamc").length==0 && document.getElementById("descriptionamc").length==0 && document.getElementById("deliverynote").length==0 ){
			showerror("Enter AMC Renewal Details");
			return false;
		}

	}

	if(requestType==Pc_Transfer)
	{
		if(document.getElementById("transfertypesel").selectedIndex==0){
			showerror("Select A Transfer Type");
			return false;
		}

		if(document.getElementById("transfertypesel").selectedIndex==1){

			if(document.getElementById("userid1").length==0 || document.getElementById("assetidpco").length==0){
				showerror("Enter One Way Transfer Details");
				return false;
			}
		}

		if(document.getElementById("transfertypesel").selectedIndex==2){

			if(document.getElementById("userid1").length==0 || document.getElementById("assetidpco").length==0 || document.getElementById("userid2").length==0 || document.getElementById("assetid2").length==0){
				showerror("Enter Two Way Transfer Details");
				return false;
			}
		}
	}

	if(requestType==Release_Resource)
	{
		if(document.getElementById("assetidrr").length==0 && document.getElementById("descriptionrr").length==0){
			showerror("Enter Release Resource Details");
			return false;
		}
	}

	if(requestType==Software_Transfer)
	{

		if(document.getElementById("assetname").length==0 && document.getElementById("userid").length==0 && document.getElementById("assetidst").length==0){
			showerror("Enter Software Transfer Details");
			return false;
		}
	}

	if(requestType==Miscellaneous)
	{

		if(document.getElementById(("itemname").length==0 || document.getElementById("userid").length==0) && document.getElementById("descriptionmisc").length==0){
			showerror("Enter Software Transfer Details");
			return false;
		}
	}

	return true;




}