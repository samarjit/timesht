function populate()
{
	//alert("This alert box was called with the onload event");
	fnAdjustTableWidth();
	if((!(whereClause == ""))){
		var url=retriveurlpart+"?panelName=searchPanel&screenName="+screenName;	
		url=url+"&whereClause="+ whereClause;		
		//alert(url);
		//alert("In message: whereClause=" + whereClause);
		// prompt("url",url);	
		sendAjaxGet(url, rrfCallBack);
	}	
	//alert("In populate");
}
var screenAction = "insert";

function clearWhereClause(){
	document.getElementById("panelFieldsWhereClause").Value = "";
}

jQuery(function() {
	jQuery('#rrfdate').datepicker({
		changeMonth: true,
		changeYear: true
	});
	
});


function rrfCallBack(p){
	//alert("Got from ajax:");	
	
	var json = JSON.parse(p);	
	if(json.error !=null ){
		showerror(json.error);
	}
	else {
		p = decodeURIComponent(json.message);
	//prompt('sam',p);
		document.getElementById("retreivedetailsdiv").innerHTML = p;	
		panelsTable = document.getElementById("panelsdiv").getElementsByTagName("table");
		//alert(panelsTable.length);

		detailTable    = document.getElementById("retreivedetailsdiv").getElementsByTagName("table");

		for ( var i=0; i<detailTable.length ; i++)
		{
			//alert(detailTable[i].id);			
			if (detailTable[i].id == 'buttonPanel')
				continue;
			if(detailTable[i].id == 'panelFields' && screenMode == "createrrf")
			{
				document.getElementById("status").value='NEW';
				document.getElementById("rrfdate").value='10/02/2010';
				document.getElementById("status").disabled=true;
				continue;
			}
			//alert(detailTable[i].rows[0].cells.length);
			for(var k = 0; k<detailTable[i].rows[0].cells.length; k++) {			
				//comStr = detailTable[i].rows[0].cells[k].childNodes[0].innerText.split(',')[2];	 			

				comStr=jQuery.trim(jQuery(detailTable[i].rows[0].cells[k]).find("div").text()).split(',')[2];
				//alert(jQuery(detailTable[i].rows[0].cells[k]).find("div").text());
				comVal = jQuery.trim(jQuery(detailTable[i].rows[1].cells[k]).text());	  
				//alert(comStr);
				//alert(comVal);
				if(comStr == "status" && (comVal == "APPROVED" || comVal == "PENDAPPROVAL")){
					document.getElementById("modify").disabled=true;
					document.getElementById("delete").disabled=true;
					document.getElementById("submit").disabled=true;				
					document.getElementById("save").disabled=true;				
				}
				if(comStr == "status" && comVal == "NEW"){
					document.getElementById("save").disabled=true;
					document.getElementById("cancel").disabled=true;				
				}			

				if(comStr == "status" && comVal == "CANCELLED"){
					document.getElementById("save").disabled=true;
					document.getElementById("cancel").disabled=true;	
					document.getElementById("modify").disabled=true;
					document.getElementById("delete").disabled=true;
					document.getElementById("submit").disabled=true;
				}	

				//comVal = detailTable[i].rows[1].cells[k].innerText;	  
				for(var l = 0; l<panelsTable.length; l++)
				{
					//alert(panelsTable[i].id);
					if (panelsTable[l].id == 'buttonPanel')
						continue;
					if (panelsTable[l].id == 'panelFields' && screenMode=="createrrf")
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
					}
				}
			}
		}
	}
	if(screenMode == "createrrf")
	{
		document.getElementById("modify").disabled=true;
		document.getElementById("delete").disabled=true;
		document.getElementById("cancel").disabled=true;
		document.getElementById("submit").disabled=true;
		disable_quot_fields();
	}
	else
	{		
		disable_fields();
	}
	
}


function fnAdjustTableWidth() {
	var tdwidthar = new Array();
	jQuery.each(jQuery("#panelsdiv  table"),function(idx,elem){	
		 
		var query = jQuery(elem).eq(0).find("tr").eq(0).find("td ");
		jQuery.each(query, function(index, item) {
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
		if (panelsTable[i].id == 'panelFields' || panelsTable[i].id == 'quotationFields' ){
			var query = jQuery(panelsTable[i]).find(" :input");
			var elem = 	jQuery(query);
		
			jQuery.each(elem, function(index, item) {
				item.disabled = true;
			});		
		}
	}
}

function disable_quot_fields(){
	panelsTable = document.getElementById("panelsdiv").getElementsByTagName("table");

	for(var i =0; i<panelsTable.length;i++){
		
	//	alert("panels "+ panelsTable[i].id);
		if (panelsTable[i].id == 'quotationFields' ){
			var query = jQuery(panelsTable[i]).find(" :input");
			var elem = 	jQuery(query);		
			jQuery.each(elem, function(index, item) {
				item.disabled = true;
			});
		
		}
	}
}

function insertData() {
	var dataTable = document.getElementById("panelsdiv").getElementsByTagName("table");
}


function rrfSubmit() {
	//alert("In rrfsubmit  !!");

	//var applicationid = jQuery("#panelsdiv #panelFields  input[id=rfqid]").attr("value");
	var applicationid = document.getElementById("rfqid").value;
	//alert(applicationid);
	
	//var actionid =  jQuery("#panelsdiv #statusFields input[id=wflactiondesc]").attr("value");
	//var wflid=jQuery("#panelsdiv #statusFields input[id=wflid]").attr("value");
	var actionid =  document.getElementById("wflactiondesc").value;
	var wflid= document.getElementById("wflid").value;
	var rrfid= document.getElementById("rrfid").value;
	//alert(rrfid);
	//document.getElementById("submitanchor").href //stealing from actionbutton.jsp its not the right way, if its coming from viewDetails this will be wrong anyway! 	
	var url = "scrworkflow.action?action=true&doString="+actionid+"&wflid="+wflid+"&appid="+applicationid+"&screenName=frmRRF"+"&rrfid="+ rrfid + "&action=submit"  ;
	//alert(url);
	location.href = url;
	//prepareInsertData();
}

function rrfCancel() {
	//alert("in rrf cancel");
	var applicationid = document.getElementById("rfqid").value;
	//alert(applicationid);
	
	//var actionid =  jQuery("#panelsdiv #statusFields input[id=wflactiondesc]").attr("value");
	//var wflid=jQuery("#panelsdiv #statusFields input[id=wflid]").attr("value");
	var actionid =  document.getElementById("wflactiondesc").value;
	var wflid= document.getElementById("wflid").value;
	var rrfid= document.getElementById("rrfid").value;
	var qtid= document.getElementById("rrfquotationid").value;
	//alert(rrfid);
	//document.getElementById("submitanchor").href //stealing from actionbutton.jsp its not the right way, if its coming from viewDetails this will be wrong anyway! 	
	var url = "scrworkflow.action?cancel=true&doString="+actionid+"&wflid="+wflid+"&appid="+applicationid+"&screenName=frmRRF"+"&rrfid="+ rrfid + "&qtid="+qtid  ;
	//alert(url);
	location.href = url;
	//prepareInsertData();
}


function backToList(){
	location.href= ctxpath+"/template1.action?screenName=frmRRFList"
}

function rrfSave() {
	
	if(document.getElementById("requestid").value == 'select'){		
		document.getElementById("requestid").value=" ";		
	}
	
	if(document.getElementById("approvarid").value == 'select'){		
		alert("Please enter Approver ID");
		exit();
	}
	//alert("in save ");	
	//alert(screenAction);
	//alert("in savesdkgf ");	
	//var url=urlpart+"?panelName=searchPanel&screenName=frmRequest"+screenName;
	if(screenAction == "insert"){
		document.getElementById("rrfid").value = "AUTOGEN_SEQUENCE_ID";
		var url=inserturlpart+"?panelName=searchPanel&screenName=frmRRF";
		//prompt("url",url);
		
		url = url + "&quotationid=" + document.getElementById("rrfquotationid").value;;
		
		url = url+ "&insertKeyValue="+ prepareInsertData();
		//prompt("url",url);
		//alert(url);
		//add key:vlaue to url		
	}
	
	if(screenAction == "modify"){
		whereclause  = makeWhereClause();
		var url=updateurlpart+"?wclause="+whereclause+"&screenName=frmRRF";
		//prompt("url",url);	
		url = url+ "&insertKeyValue="+ prepareInsertData();
		//prompt("url",url);
		//alert(url);
	}
	sendAjaxGet(url, saveCallBack);
}

function deleteData(){
	//alert("in delete Data");
	whereclause  = makeWhereClause();
	var url=deleteurlpart+"?wclause="+whereclause+"&screenName=frmRRF";
	url = url + "&quotationid=" + document.getElementById("rrfquotationid").value;
	//prompt("url",url);	
	//alert("in delete!!!!!!! url" +url);
	//prompt("url",url);
	//add key:vlaue to url	
	sendAjaxGet(url, deleteCallBack);
	
}

function saveCallBack(val) {
	//show success message 
	if(val < 0){
		
		alert("Could not save : Error Occured! ");
	}
	else{
		if(screenAction == "modify"){

			location.href= ctxpath+"/template1.action?screenName=frmRRFList"
			alert("Successfully modified your rrf! ");
		}
		if(screenAction == "insert"){

			location.href= ctxpath+"/template1.action?screenName=frmRRFList"
			alert("Successfully created your rrf! ");
		}
	}
}


function deleteCallBack(val) {
	//show success message 
	if(val < 0){
		
		showerror("Could not delete : Error Occured! ");
	}
	else{
		location.href= ctxpath+"/template1.action?screenName=frmRRFList"
		alert("Successfully deleted your rrf! ");
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

	if(document.getElementById("approvarid").value == 'select'){		
		showerror("Please enter Approver ID");
		exit();
	}
	
	if(document.getElementById("requestid").value == 'select'){		
		document.getElementById("requestid").value=" ";
	}
	
	//alert("in prepare");
	//var array = {"panelFields1":{"empid":"9002","empname":"tutu","bdate":"12-10-2009"},"panelFields":{"empid":"9001","empname":"samarjit","bdate":"12-10-2009"}};
	var dataTable = document.getElementById("panelsdiv").getElementsByTagName("table");
	var pclass = new Array();
	//alert(document.getElementById("rrfquotationid").value);
	//exit();
	//get the quotation id from quotation fileds
	document.getElementById("quotationid").value=document.getElementById("rrfquotationid").value;
	//document.getElementById("rrfrfqid").value=document.getElementById("rfqid").value;
	

	
	//need only panelfieds data for insert.
	
	var query = "#panelsdiv #" + dataTable[0].id + " :input";
	var requestar = new Array();
	//alert(query);
	var elem = 	jQuery(query); 
	var j = 0;
	jQuery.each(elem, function(index, item) {	
		//alert(j);
		//alert(item.id);
		//alert(item.value);		
		
		requestar[j] = new KeyValue(item.id, item.value);				
		j++;						
	});
	//exit();
	pclass[0] = new panelClass(dataTable[0].id,requestar);	
	var k = new Object();
	k.json = pclass
	var myJSONText = JSON.stringify(k, replacer,"");
	//alert(myJSONText );	
	return myJSONText;			
}


function updateData(obj){
	//obj.disabled = true;
	screenAction = "modify";
	//There will be only one table in search screen 'search div'
	//document.requestFrm.submit();
	
	document.getElementById("save").disabled=false;
	document.getElementById("modify").disabled=true;
	
		
	listTable = document.getElementById("retreivedetailsdiv").getElementsByTagName("table")[0];

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
					
					if(jQuery(listTable.rows[0].cells[i]).text().split(',')[2] == fields.id){
						
						//alert(jQuery(listTable.rows[0].cells[i]).find("div").text().split(',')[6]);
						if(!(jQuery(listTable.rows[0].cells[i]).find("div").text().split(',')[6]  == 'Y')) {

							fields.disabled = false;
						}
					}

					//for date
					if(jQuery(listTable.rows[0].cells[i]).text().split(',')[2] == fields.id){
						if((jQuery(listTable.rows[0].cells[i]).text().split(',')[4] == 'DATE')) {


							fields.disabled = true;
						}
					} 
				}

			});

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


function submitactivity(){
	//alert("here in submit activity")
	alert(wflcontrollerurl);
	var applicationid = jQuery("#panelsdiv #panelFields  input[id=reqid]").attr("value");
	//alert(applicationid);
	var actionid =  jQuery("#panelsdiv #statusFields input[id=wflactionid]").attr("value");
	var wflid=jQuery("#panelsdiv #statusFields input[id=wflid]").attr("value");
	
	//document.getElementById("submitanchor").href //stealing from actionbutton.jsp its not the right way, if its coming from viewDetails this will be wrong anyway! 	
	location.href = wflcontrollerurl+"?action=true&doString="+actionid+"&wflid="+wflid+"&appid="+applicationid;
		
	}

function submitScreenFlowactivity(){
	//alert("here in submit activity")
	//alert(wflcontrollerurl);
	var applicationid = jQuery("#panelsdiv #panelFields  input[id=reqid]").attr("value");
	//alert(applicationid);
	var actionid =  jQuery("#panelsdiv #statusFields input[id=wflactiondesc]").attr("value");
	var wflid=jQuery("#panelsdiv #statusFields input[id=wflid]").attr("value");
	
	//document.getElementById("submitanchor").href //stealing from actionbutton.jsp its not the right way, if its coming from viewDetails this will be wrong anyway! 	
	location.href = wflcontrollerurl+"?action=true&doString="+actionid+"&wflid="+wflid+"&appid="+applicationid;		
	}

