function populate()
{
	//alert("This alert box was called with the onload event");	
	//alert("In populate");
	if((!(whereClause == ""))){
		var url=retriveurlpart+"?panelName=searchPanel&screenName="+screenName;	
		url=url+"&whereClause="+ whereClause;		
		//alert("In message: whereClause=" + whereClause);
		 //prompt("url",url);	
		
		sendAjaxGet(url, approvalRRFCallBack);
	}	
	
}

function clearWhereClause(){
	//alert(">>>Calling clearWhereClause...");
	document.getElementById("panelFieldsWhereClause").value = "";
}

var screenMode = "insert";

function approvalRRFCallBack(p){
	//alert("Got from ajax:"+p);
	//disable_fields();
	var json = JSON.parse(p);	
	if(json.error !=null ){
		showerror(json.error);
	}
	else {
		p = decodeURIComponent(json.message);
		disable_fields();
		document.getElementById("rrfcomments").disabled=false;
	
		document.getElementById("retreivedetailsdiv").innerHTML = p;	
		panelsTable = document.getElementById("panelsdiv").getElementsByTagName("table");
		//alert(panelsTable.length);
		detailTable    = document.getElementById("retreivedetailsdiv").getElementsByTagName("table");

		for ( var i=0; i<detailTable.length ; i++)
		{
			//alert(detailTable[i].id);			
			if (detailTable[i].id == 'buttonPanel')	continue;
			for(var k = 0; k<detailTable[i].rows[0].cells.length; k++) {			
				//comStr = detailTable[i].rows[0].cells[k].childNodes[0].innerText.split(',')[2];	 			

				comStr=jQuery.trim(jQuery(detailTable[i].rows[0].cells[k]).find("div").text()).split(',')[2];
				//alert(jQuery(detailTable[i].rows[0].cells[k]).find("div").text());
				comVal = jQuery.trim(jQuery(detailTable[i].rows[1].cells[k]).text());	  

				if(comStr == "rrfstatus" && comVal == "PENDAPPROVAL"){
					document.getElementById("btnforwardtonextlevel").disabled=true;
					
				}
				if(comStr == "rrfstatus" && (comVal == "APPROVED" || comVal == "REJECTED")){
					
					document.getElementById("btnforwardtonextlevel").disabled=true;
					document.getElementById("btnapprove").disabled=true;
					document.getElementById("btnreject").disabled=true;				
					document.getElementById("rrfcomments").disabled=true;
				}
				
				//comVal = detailTable[i].rows[1].cells[k].innerText;	  
				for(var l = 0; l<panelsTable.length; l++)
				{
					//alert(panelsTable[i].id);
					if (panelsTable[l].id == 'buttonPanel')
						continue;
					if(detailTable[i].id == panelsTable[l].id)
					{
						var input = panelsTable[l].getElementsByTagName("input");
						//alert(input.length);

						for( var m = 0 ; m < input.length; m++)
						{
							if(input[m].id == comStr)
							{
								//alert(comStr +"   "+comVal +"   " +panelsTable[l].id + " " + detailTable[i].id);
								input[m].value = comVal;
							}
						}
					}
				}
			}
		}
	}
}

function disable_fields(){
	paneltables = document.getElementById("panelsdiv").getElementsByTagName("table");

	for(var i =0; i<paneltables.length;i++){
		
	//	alert("panels "+ panelsTable[i].id);
		if (paneltables[i].id == 'panelFields' || paneltables[i].id == 'rfqFields'){
			
		fields = paneltables[i].getElementsByTagName("input");
		//	alert("inside panel panels " + fields.length);
			for(var k = 0; k<fields.length; k++){
			//	alert("inside panel panels " + fields[k].id);
				fields[k].disabled = true;
			
			}		
		}
	}
	
	
	
}


function backToList(){
	location.href= ctxpath+"/template1.action?screenName=frmApprovalRRFList"
}



function insertData() {
	var dataTable = document.getElementById("panelsdiv").getElementsByTagName("table");
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
				//alert("requestar"+requestar[j]);
				j++;						
			});
			pclass[i] = new panelClass(dataTable[i].id,requestar);	
			
		}	
		var k = new Object();
		k.json = pclass;
		
		var myJSONText = JSON.stringify(k, replacer,"");
		//alert(myJSONText);	
		return myJSONText;			
}

var rrfid= null;

function makeWhereClause(){
	 
	// alert("in make url,selectedIdx:"+selectedIdx);
	//There will be only one table in search screen 'search div'
	
	listTable = document.getElementById("retreivedetailsdiv").getElementsByTagName("table")[0];

	whereClause = "panelFields1WhereClause=";
	//alert(">>>>>whereClause in makeWhereClause method ::"+whereClause);
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
				rrfid=value;
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
		//alert(">>>whereClause in MakeWhereClause::"+whereClause);
		 		
	}	
	return whereClause;	 

}

function approvalRrf() 
{
	//alert("In approval");
	
	var applicationid = document.getElementById("rrfrfq").value;
	//alert(applicationid);
	
	var actionid =  document.getElementById("wflactiondesc").value;
	var wflid= document.getElementById("wflid").value;
	var rrfid= document.getElementById("rrfid").value;
	var comments= document.getElementById("rrfcomments").value;
	
	//alert(rrfid); 	
	var url = "scrworkflow.action?doString="+actionid+"&wflid="+wflid+"&appid="+applicationid+"&screenName=frmApprovalRRF&rrfcomments="+comments+"&rrfid="+ rrfid + "&approve=true&ajaxflag=true"  ;
	//location.href = url;
	
	sendAjaxGet(url, approvalCallBack);
	
}

var workflowurl;

function approvalCallBack(val)
{
	//show success message 
	//alert("RRF approved");
	//alert(val);
	var json = JSON.parse(val);
	//alert(json.workflowurl);
	if(json.error !=null ){
		showerror(json.error);
	}else {
		//showalert(json.message);
		workflowurl = json.workflowurl;
		if(json.workflowurl != null){
			
			var  pclass = new Array();
			
			var requestar = new Array();
			//alert(query);
			//var elem = 	jQuery(query); 
			//var j = 0;
			//jQuery.each(elem, function(index, item) {	
				//alert(j);
				//alert(item.id);
				//alert(item.value);		
				
			requestar[0] = new KeyValue("poid", "AUTOGEN_SEQUENCE_ID");
			requestar[1] = new KeyValue("porrfid", document.getElementById("rrfid").value);
			var date = new Date();
			var curr_date = date.getDate();
			var curr_month = date.getMonth();
			curr_month = curr_month + 1;
			var curr_year = date.getFullYear();
			date= curr_date + '/'+ curr_month + '/'+ curr_year;
			//alert(date);
			requestar[2] = new KeyValue("podate", date);
			requestar[3] = new KeyValue("postatus", "NEW");

			pclass[0] = new panelClass("panelFields",requestar);	
			var k = new Object();
			k.json = pclass
			var myJSONText = JSON.stringify(k, replacer,"");
			//alert(myJSONText );	
			//return myJSONText;	
			var url=inserturlpart+"?panelName=searchPanel&screenName=frmPO";
			//prompt("url",url);	
			//url = url + "&poid=AUTOGEN_SEQUENCE_ID&porrfid="+document.getElementById("rrfid").value;
			url = url+ "&insertKeyValue="+ myJSONText;
			//alert(url);
			//exit();
			sendAjaxGet(url, savePOCallBack);
			
		}
	}
}

function savePOCallBack(val) {
	//show success message
	//alert("in save PO");
	//alert(workflowurl);
	alert("The record is approved successfully");
	location.href = ctxpath+"/template1.action?screenName=frmApprovalRRFList";

}


function rejectRrf() 
{ 
	//alert("In reject");
	var applicationid = document.getElementById("rrfrfq").value;
	//alert(applicationid);	
	var actionid =  document.getElementById("wflactiondesc").value;
	var wflid= document.getElementById("wflid").value;
	var rrfid= document.getElementById("rrfid").value;
	var comments= document.getElementById("rrfcomments").value;
	//alert(rrfid); 	
	var url = "scrworkflow.action?doString="+actionid+"&wflid="+wflid+"&appid="+applicationid+"&screenName=frmApprovalRRF&rrfcomments="+comments+"&rrfid="+ rrfid + "&approve=false&ajaxflag=true"  ;
	//alert("The record is rejected successfully");
	//location.href = url;
	sendAjaxGet(url, rejectCallBack);
	
}

function rejectCallBack(val) {
	//show success message
	//alert("in save PO");
	//alert(workflowurl);
	//showalert("The record is rejected successfully");
	alert("The record is rejected successfully");
	location.href = ctxpath+"/template1.action?screenName=frmApprovalRRFList";

}



