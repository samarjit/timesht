function populate()
{
	//alert("This alert box was called with the onload event");	
	fnAdjustTableWidth();
	if((!(whereClause == ""))){
		var url=retriveurlpart+"?panelName=searchPanel&screenName="+screenName;	
		url=url+"&whereClause="+ whereClause;		
		//alert("In message: whereClause=" + whereClause);
		// prompt("url",url);	
		sendAjaxGet(url, requestCallBack);
	}
	if(screenMode == "create"){
		jQuery('#btnModify').attr('disabled','disabled');
		jQuery('#btnDelete').attr('disabled','disabled');
		jQuery('#btnSubmit').attr('disabled','disabled');
		var updateonar = "assetid,qtyavailable".split(",");
		for ( var i = 0; i < updateonar.length; i++) {
			var arelm = updateonar[i];
			jQuery("#"+ arelm).attr('disabled','disabled');
		}
	}
}
//var screenMode = "insert";
function $F(p){
	if(document.getElementById(p))
	return document.getElementById(p).value;
	return "";
}
function clearWhereClause(){
	document.getElementById("panelFieldsWhereClause").value = "";
}

function requestCallBack(p){
	//alert("Got from ajax:"+p);

	var json = JSON.parse(p);	
	//alert("Got from ajax: message"+json.message);
	//alert("Got from ajax: error"+json.error);
	if(json.error !=null ){
		showerror(json.error);
	}
	else {
		//alert("Got from ajax:"+json.message);
		//showalert(json.message);
		//p = decodeURIComponent(json.message);
		p = decodeURIComponent(json.message);

		document.getElementById("retreivedetailsdiv").innerHTML = p;	
		panelsTable = document.getElementById("panelsdiv").getElementsByTagName("table");
		//alert(panelsTable.length);


		detailTable    = document.getElementById("retreivedetailsdiv").getElementsByTagName("table");

		for ( var i=0; i<detailTable.length ; i++)
		{
			//alert(detailTable[i].id);			
			if (detailTable[i].id == 'buttonPanel')
				continue;
			if(detailTable[i].rows[0])
				for(var k = 0; k<detailTable[i].rows[0].cells.length; k++) {			
					//comStr = detailTable[i].rows[0].cells[k].childNodes[0].innerText.split(',')[2];	 			

					comStr=jQuery.trim(jQuery(detailTable[i].rows[0].cells[k]).find("div").text()).split(',')[2];
					//alert(jQuery(detailTable[i].rows[0].cells[k]).find("div").text());
					comVal = jQuery.trim(jQuery(detailTable[i].rows[1].cells[k]).text());	  

					//comVal = detailTable[i].rows[1].cells[k].innerText;	  
					for(var l = 0; l<panelsTable.length; l++)
					{
						//alert(panelsTable[i].id);
						if (panelsTable[l].id == 'buttonPanel')
							continue;
						if(detailTable[i].id == panelsTable[l].id)
						{ 
							/*					var input = panelsTable[l].getElementsByTagName("input");*/
							var query = jQuery(panelsTable[l]).find(" :input");
							var elem = 	jQuery(query);

							jQuery.each(elem, function(index, item) {

								if(item.id == comStr){
									jQuery(item).val(comVal);
								}
							});
//							for( var m = 0 ; m < input.length; m++)
//							{
//							if(input[m].id == comStr)
//							{
//							//alert(comStr +"   "+comVal +"   " +panelsTable[l].id + " " + detailTable[i].id);
//							input[m].value = comVal;
//							}
//							}
						}
					}
				}	
		}
	}

	disable_fields();


	//createVendorDropdownAjax();
	
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

function disable_fields(){
	panelsTable = document.getElementById("panelsdiv").getElementsByTagName("table");

	for(var i =0; i<panelsTable.length;i++){
		
	//	alert("panels "+ panelsTable[i].id);
		if (panelsTable[i].id == 'panelFields'){
//			
//		fields = panelsTable[i].getElementsByTagName("input");
//			for(var k = 0; k<fields.length; k++){
//			
//				fields[k].disabled = true;
//			
//			}
			var query = jQuery(panelsTable[i]).find(" :input");
			var elem = 	jQuery(query);
		
			jQuery.each(elem, function(index, item) {
				item.disabled = true;
			});
		
		}
		 
		
	}//btnRead,btnSave,btnModify,btnDelete,btnSubmit,btnSubmitselAll
	var updateonar = "dnid,Status,wflactionid,wflactiondesc,wflid,btnSave".split(",");
	for ( var i = 0; i < updateonar.length; i++) {
		var arelm = updateonar[i];
		jQuery("#"+ arelm).attr('disabled','disabled');
	}
	if(screenMode == "view"){
		updateonar = "btnModify,btnSubmit,btnDelete".split(",");
		for ( var i = 0; i < updateonar.length; i++) {
			var arelm = updateonar[i];
			jQuery("#"+ arelm).removeAttr('disabled');
		}
	}
}

function insertData() {
	var dataTable = document.getElementById("panelsdiv").getElementsByTagName("table");
}


function reqSubmit() {
	
	prepareInsertData();
}

function reqSave() {
	//alert("in save ");	
	//alert(inserturlpart+" Mode:"+screenMode);
	//alert("in savesdkgf ");	
	//var url=urlpart+"?panelName=searchPanel&screenName=frmAsset"+screenName;	
	 
	if(screenMode == "create"){
		document.getElementById("qtyavailable").value = document.getElementById("quantity").value		
	document.getElementById("assetid").value = "AUTOGEN_SEQUENCE_ID";	
	var url=inserturlpart+"?panelName=searchPanel&screenName=frmAsset";
	//prompt("url","action=true&doString="+actionid+"&wflid="+wflid+"&appid="+applicationid);	
	url = url+ "&insertKeyValue="+ prepareInsertData()+"&invokewfl=false&activityname=CRAST&action=true";
	//prompt("url",url);
	//add key:vlaue to url
	sendAjaxGet(url, saveCallBack);

	}
	
	if(screenMode == "modify"){
		whereclause  = makeWhereClause();
		var url=updateurlpart+"?wclause="+whereclause+"&screenName=frmAsset";
		//prompt("url",url);	
		url = url+ "&insertKeyValue="+ prepareInsertData();

		//prompt("url",url);
		//add key:vlaue to url
		

		sendAjaxGet(url, saveCallBack);
		}
	
		
}

function deleteData(){
	
	whereclause  = makeWhereClause();
	var url=deleteurlpart+"?wclause="+whereclause+"&screenName=frmAsset";
//	prompt("url",url);	
	
	//prompt("url",url);
	//add key:vlaue to url
	

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
		}else{//extracting all fields whereclause form insert key value pair of each panel
			var allfields = prepareInsertData();
			var json = JSON.parse(allfields);
			var valuesar = json.json[0].valuesar;
			var k = new Object();
			k.json = valuesar;
			var myJSONText = JSON.stringify(k, replacer,"");
					//	In case this is callback after create, need to clear left over autogen_sequence
					//  so that this is not included in creating where clause
			whereClause = myJSONText.replace("AUTOGEN_SEQUENCE_ID",""); 
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

	//alert("in prepare");
	//var array = {"panelFields1":{"empid":"9002","empname":"tutu","bdate":"12-10-2009"},"panelFields":{"empid":"9001","empname":"samarjit","bdate":"12-10-2009"}};
	var dataTable = document.getElementById("panelsdiv").getElementsByTagName("table");
	var pclass = new Array();
	

		//alert(dataTable.length);		
		for (var i=0; i<dataTable.length; i++) {
				
			var query = "#panelsdiv #" + dataTable[i].id + " :input";
			var requestar = new Array();
			//alert(query);
			var elem = 	jQuery(query); 
			var j = 0;
			jQuery.each(elem, function(index, item) {	
				//alert(j);
				requestar[j] = new KeyValue(item.id, item.value);				
				j++;						
			});
			
			pclass[i] = new panelClass(dataTable[i].id,requestar);					
		}	
		var k = new Object();
		k.json = pclass
		var myJSONText = JSON.stringify(k, replacer,"");
		//alert(myJSONText );	
		return myJSONText;			
}


function updateData(obj){
	//obj.disabled = true;
	screenMode = "modify";
	//There will be only one table in search screen 'search div'
	//document.requestFrm.submit();
	/*listTable = document.getElementById("retreivedetailsdiv").getElementsByTagName("table")[0];

	panelsTable = document.getElementById("panelsdiv").getElementsByTagName("table");

	for(var m =0; m<panelsTable.length;m++){

		if (panelsTable[m].id == 'panelFields'){

				fields = panelsTable[m].getElementsByTagName("input");
			var query = jQuery(panelsTable[m]).find(" :input");
			var elem = 	jQuery(query);
			//alert("inside update panel panels " + fields.length);
			// for(var k = 0; k<fields.length; k++){
			jQuery.each(elem,function(k,fields){
				//alert("inside panel panels " + fields.id);
				for (i = 0; i <listTable.rows[0].cells.length ; i++ )
				{
					// alert(fields[k].id);
					
					if(jQuery(listTable.rows[0].cells[i]).find("div").text().split(',')[2] == fields.id){
						
						//alert(jQuery(listTable.rows[0].cells[i]).text())
						if(!(jQuery(listTable.rows[0].cells[i]).find("div").text().split(',')[6]  == 'Y')) {

							

							fields.disabled = false;
						}
					}

					//for date
					if(jQuery(listTable.rows[0].cells[i]).find("div").text().split(',')[2] == fields.id){
						if((jQuery(listTable.rows[0].cells[i]).find("div").text().split(',')[4] == 'DATE')) {


							fields.disabled = true;
						}
					} 
				}

			});

		}
	}*/
	
//dnid,hidspace,assetid,assetname,assettype,vendorid,make,tag,config,allocstatus,warranty,quantity,remarks,
	//Status,wflactionid,wflactiondesc,wflid,
var updateonar = "dnid,assetname,assettype,vendorid,make,tag,config,allocstatus,warranty,quantity,remarks,btnSave,qtyavailable".split(",");
for ( var i = 0; i < updateonar.length; i++) {
	var arelm = updateonar[i];
	jQuery("#" + arelm).removeAttr('disabled');
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
		
		whereClause = encodeURIComponent(myJSONText);//whereClause.replace(/(~#)$/, '');
		 
		 
		
	}
	
	return whereClause;	 

}


function submitactivity(){
	alert("here in submit activity")
	alert(wflcontrollerurl);
	var applicationid = jQuery("#panelsdiv #panelFields  input[id=rfqid]").attr("value");
	alert(applicationid);
	var actionid =  jQuery("#panelsdiv #statusFields input[id=wflactionid]").attr("value");
	var wflid=jQuery("#panelsdiv #statusFields input[id=wflid]").attr("value");
	
	//document.getElementById("submitanchor").href //stealing from actionbutton.jsp its not the right way, if its coming from viewDetails this will be wrong anyway! 	
	location.href = wflcontrollerurl+"?action=true&doString="+actionid+"&wflid="+wflid+"&appid="+applicationid;
		
	}

function submitScreenFlowactivity(){
	alert("here in submit activity")
	alert(wflcontrollerurl);
	var applicationid = jQuery("#panelsdiv #panelFields  input[id=rfqid]").attr("value");
	alert(applicationid);
	var actionid =  jQuery("#panelsdiv #statusFields input[id=wflactiondesc]").attr("value");
	var wflid=jQuery("#panelsdiv #statusFields input[id=wflid]").attr("value");
	
	//document.getElementById("submitanchor").href //stealing from actionbutton.jsp its not the right way, if its coming from viewDetails this will be wrong anyway! 	
	var url = "scrworkflow.action?action=true&doString="+actionid+"&wflid="+wflid+"&appid="+applicationid;
	location.href = url;
		
	}

 

function populateVendors(){
	var url = ctxpath+"/vendormap.action?command=selectall"+"&rfqid="+document.getElementById("rfqid").value+"";
	sendAjaxGet(url, popvendorcallback);
}
function deleteVendor(vendorid){
	var url = ctxpath+"/vendormap.action?command=delete"+"&rfqid="+document.getElementById("rfqid").value  +
			"&vendorid="+vendorid;
	//alert(url);
	sendAjaxGet(url, popvendorcallback);
}


/**
 * This method is called by cmd:selectall initial populate vendors and delete and insert mapall
 */
function popvendorcallback(parm){
	if(/^ERROR/.test(parm)){
		showerror(parm.substring(6));
		return;
	}
	jQuery(document.getElementById("vendorlist")).html(parm);
	try {
	//alert("creating dynamic table");	
	var jsonob  = JSON.parse(parm);
	var jsonobdata =  jsonob.SELECTDATA;
	var error  = jsonob.ERROR;
	var rfqStatus = jsonob.RFQSTATUSUPDATE;
	if(rfqStatus != "" && typeof( rfqStatus) != 'undefined')
		jQuery("#rfqstatus").val(rfqStatus);
		
	if(error != "" && typeof(error )!= 'undefined')	{
	alert("here");	showerror(error);
		return;
	}
	
	
	var strTable = "<table ><tr><th>Vendor ID</th>" +
			"<th>Vendor Name</th>" +
			"<th>Type of notfication</th>" +
			"<th>Individual Status</th>" +
			"<th>Vendor Rating</th>" +
			"<th>Suggested Delivery Time</th>" +
			"<th>Vendor Email</th>" +
			"<th>Delete Record</th></tr>";
	for ( var i = 0; i < jsonobdata.length; i++) {
		var obj = jsonobdata[i];
		strTable +="<tr><td>"+obj.vendor_id+"</td>";
		strTable +="<td>"+obj.vendor_name+"</td>";
		var type = obj.TYPE_NOTIFY.split("~");
		strTable +="<td>";
		if(type.length == 4){
			if(type[0]=='E' && isNumeric(type[1])){
				strTable +="<input type='checkbox' checked='checked' onclick='enableEmail(0,\""+obj.vendor_id+"\")'/>" +
						"<a href='javascript:sendEmail(\""+obj.vendor_id+"\")' >" +
						"<img src='"+ctxpath+"/css/images/email.gif' alt='email' />"+"</a>"+type[1];	
			}else{
				strTable +="<input type='checkbox' onclick='enableEmail(1,\""+obj.vendor_id+"\")'/>" +
						"<img src='"+ctxpath+"/css/images/email.gif' alt='email' />"+type[1];
			}
			if(type[2] == 'P' && isNumeric(type[3])){
				strTable +="<input type='checkbox' checked='checked' onclick='enablePrint(0,\""+obj.vendor_id+"\")'/><a href='javascript:sendPrint(\""+obj.vendor_id+"\")' ><img src='"+ctxpath+"/css/images/printer.gif' alt='print' />"+"</a>"+type[3];	
			}else{
				strTable +="<input type='checkbox' onclick='enablePrint(1,\""+obj.vendor_id+"\")'/><img src='"+ctxpath+"/css/images/printer.gif' alt='print' />"+type[3];
			}
		}
		strTable +="<input type='hidden' id='"+$F("rfqid")+"$#"+obj.vendor_id+"' value='"+obj.TYPE_NOTIFY+"' />";
		strTable +="</td>";
		strTable +="<td>"+obj.INDV_STATUS+"</td>";
		strTable +="<td>"+obj.vendor_rating+"</td>";
		strTable +="<td>"+obj.SUGGEST_DLV_TIME+"</td>";
		strTable +="<td>"+obj.vendor_email+"</td>";
		strTable +="<td><a href='javascript:deleteVendor(\""+obj.vendor_id+"\")' ><img src='"+ctxpath+"/css/images/delete.png' alt='delete' />"+"</a></td></tr>";
	}
	strTable +="</table>";
	jQuery(document.getElementById("vendorlist")).html(strTable);
	} catch (e) {
		alert(e);
	}
	
}

function calculateIndvStatus(url, type){ 
	try { 
		var ststus = 'NotAttended';
		var res1 = true,res2 = true, docare = false;
		if(type[0]=='E'){
			if (isNumeric(type[1]) && parseInt(type[1]) > 0 ) {
				 res1= true;
				 
			} else {
				res1= false;
			}
		docare  = true;
		}
		
		if(type[2]=='P'){
			if(isNumeric(type[3])	&& parseInt(type[3]) > 0){
				 res2= true;  
			} else {
				res2= false;
			}
		docare  = docare || true;
		}
		if(docare && res1 && res2 ){
			url += "&indvstatus=Attended";
		} else {
			url += "&indvstatus=NotAttended";
		}
		 
	} catch (e) {
		alert(e);
	}
	return url;
}
function enableEmail(status,vendorid){
	var url = ctxpath+"/vendormap.action?command=updatetypenotify"+"&rfqid="+document.getElementById("rfqid").value  +
	"&vendorid="+vendorid+"&status="+status;
	//get the hidden field's value corresponding to each row
	var str = $F($F("rfqid")+"$#"+vendorid);
	type  = str.split("~");
	if(status == 0){
		type[0]='x';
	}	
	else if(status == 1){
		if(!isNumeric(type[3]))type[3]="0";
		type[0]='E';
	}
	
	url = calculateIndvStatus(url, type);
	url+="&typenotify="+type.join("~");

	sendAjaxGet(url, popvendorcallback);
}
function enablePrint(status,vendorid){
	var url = ctxpath+"/vendormap.action?command=updatetypenotify"+"&rfqid="+document.getElementById("rfqid").value  +
	"&vendorid="+vendorid+"&status="+status;
	//get the hidden field's value corresponding to each row
	var str = $F($F("rfqid")+"$#"+vendorid);
	type  = str.split("~");
	if(status == 0){
		type[2]='x';
		
	}	
	else if(status == 1){
		if(!isNumeric(type[3]))type[3]="0";
		type[2]='P';
	}else{
		return;
	}
	
	
	url = calculateIndvStatus(url, type);
	url+="&typenotify="+type.join("~");
	
	sendAjaxGet(url, popvendorcallback);
}

var childwindow;
function sendEmail(vendorid){
  childwindow = window.open("rfqsendemail.action?vendorid="+vendorid+"&rfqid="+$F("rfqid"),"","width=800, height=500,scrollbars=1");
}
function populateEmailPage(parm){
 childwindow.populateCallbackValues($F("rfqid"),$F("itemtype"),$F("itemdesc"),$F("quantity"));
 
}
var childwindow2;
function sendPrint(vendorid){
 childwindow2 = window.open("rfqsendprint.action?vendorid="+vendorid+"&rfqid="+$F("rfqid"),"","width=800, height=500");
}
function populatePrintPage(parm){
	 childwindow2.populateCallbackValues($F("rfqid"),$F("itemtype"),$F("itemdesc"),$F("quantity"));
	 
}

function rfqemailSent(vendorid){
	var url = ctxpath+"/vendormap.action?command=updatetypenotify"+"&rfqid="+document.getElementById("rfqid").value  +
	"&vendorid="+vendorid+"&status="+status;
	//get the hidden field's value corresponding to each row
	var str = $F($F("rfqid")+"$#"+vendorid);
	type  = str.split("~");
	if(isNumeric(type[1])){
		type[1]=parseInt(type[1] )+1 ;
	}else{
		return;
	}
	
	url = calculateIndvStatus(url, type);
	url+="&typenotify="+type.join("~");
		
	sendAjaxGet(url, popvendorcallback);
}

function rfqPrinted(vendorid){
	var url = ctxpath+"/vendormap.action?command=updatetypenotify"+"&rfqid="+document.getElementById("rfqid").value  +
	"&vendorid="+vendorid+"&status="+status;
	//get the hidden field's value corresponding to each row
	var str = $F($F("rfqid")+"$#"+vendorid);
	type  = str.split("~");
	if(isNumeric(type[3])){
		type[3]=parseInt(type[3]) +1 ;
	}else{
		return;
	}
	
	url = calculateIndvStatus(url, type);
	url+="&typenotify="+type.join("~");
		
	sendAjaxGet(url, popvendorcallback);
}

function insertVendor(parm){
	var url = ctxpath+"/vendormap.action?command=insert"+"&rfqid="+document.getElementById("rfqid").value
	+"&vendorid="+document.getElementById("rfqvendorlist").value+"&typenotify="+
	escape($F("typenotify"))+"&suggestdelvtime="+$F("suggestdelvtime");
	
	//alert(url);
	sendAjaxGet(url, popvendorcallback);
}
function fnMapVendors(){
	var url = ctxpath+"/vendormap.action?command=initialmap"+"&rfqid="+$F("rfqid")+
	"&typenotify="+escape("E~0~x~0")+"&suggestdelvtime=10&department="+$F("department");
	 
	alert(url);
	sendAjaxGet(url, popvendorcallback);
}

function createVendorDropdownAjax(){
	var url = ctxpath+"/vendormap.action?command=vendorlist"+"&department="+document.getElementById("department").value+"";
	//alert("dropdown gtting called");
	sendAjaxGet(url, createVendorDropdownCallback);
}
function createVendorDropdownCallback(parm){

	try {

	var xmlDoc = parseXMLDocFromString(parm); 

		var elm = xmlDoc.getElementsByTagName("tr");
		document.getElementById("rfqvendorlist").options.length =0 ;
		for (var i=0; i<elm.length; i++) {
		document.getElementById("rfqvendorlist").options[i]= new Option();
		document.getElementById("rfqvendorlist").options[i].value = elm[i].childNodes[0].childNodes[0].nodeValue;
		document.getElementById("rfqvendorlist").options[i].text = elm[i].childNodes[1].childNodes[0].nodeValue;
		}
	} catch (e) {
		alert(e);
		// TODO: handle exception
	}	
	
}