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
		sendAjaxGet(url, poCallBack);
	}	
	//alert("In populate");
}
var screenAction = "insert";

function clearWhereClause(){
	document.getElementById("panelFieldsWhereClause").Value = "";
}

function sendPO(){

	url = url+"/scrworkflow.action?action=true&doString="+actionid+"&vendorid="+vendorid+"&wflid="+wflid+"&appid="+applicationid+"&screenName="+screenName+"&poid="+jQuery("#poid").text()+"&vendoraddr="+jQuery("#vendoraddr").text()+"&curdate="+jQuery("#curdate").text()+"&qid="+jQuery("#quotationid").text()+"&qref="+jQuery("#quotationref").text()+"&itemid="+jQuery("#itemid").text()+"&desc="+jQuery("#desc").text()+"&qty="+jQuery("#qty").text()+"&unitprice="+jQuery("#unitprice").text()+"&linetotal="+jQuery("#linetotal").text()+"&curtype="+jQuery("#curtype").text()+"&discount="+jQuery("#discount").text()+"&misc="+jQuery("#misc").text()+"&totalamt="+jQuery("#totalamt").text()+"&comments="+jQuery("#comments").text(); 
	window.close();
	opener.location=(url);
}



function poCallBack(p){
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
			if (detailTable[i].id == 'buttonPanel' || detailTable[i].rows.length==0)
				continue;
			/*if(detailTable[i].id == 'panelFields' && screenMode == "createrrf")
			{
				document.getElementById("status").value='NEW';
				document.getElementById("rrfdate").value='10/02/2010';
				document.getElementById("status").disabled=true;
				continue;
			}*/
			//alert(detailTable[i].rows[0].cells.length);
			for(var k = 0; k<detailTable[i].rows[0].cells.length; k++) {			
				//comStr = detailTable[i].rows[0].cells[k].childNodes[0].innerText.split(',')[2];	 			

				comStr=jQuery.trim(jQuery(detailTable[i].rows[0].cells[k]).find("div").text()).split(',')[2];
				//alert(jQuery(detailTable[i].rows[0].cells[k]).find("div").text());
				comVal = jQuery.trim(jQuery(detailTable[i].rows[1].cells[k]).text());	  
				//alert(comStr);
				//alert(comVal);
				/*if(comStr == "status" && (comVal == "APPROVED" || comVal == "PENDAPPROVAL")){
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
				}	*/

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
	/*if(screenMode == "createrrf")
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
	}*/
	disable_fields();
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
	
	if(document.getElementById("postatus").value=="DNCREATED"){
		
		document.getElementById("modify").disabled = true;

	}

if(document.getElementById("postatus").value=="SEND"){
		
		document.getElementById("modify").disabled = true;

	}

	
	
}


function enable_fields(){
	screenMode = "modify";

	var updateonar = "pocomments".split(",");
	for ( var i = 0; i < updateonar.length; i++) {
		var arelm = updateonar[i];
		jQuery("#"+ arelm).attr('disabled','');
	}
	document.getElementById("delete").disabled = true;
	document.getElementById("viewpo").disabled = true;
	document.getElementById("cancelpo").disabled = true;
	document.getElementById("modify").disabled = true;
	document.getElementById("save").disabled = false;




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



function backToList(){
	location.href= ctxpath+"/template1.action?screenName=frmPOList"
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



function posave(){
	
	document.getElementById("save").disabled=false;
	document.getElementById("modify").disabled=true;
	whereclause  = makeWhereClause();
	var url=updateurlpart+"?wclause="+whereclause+"&screenName=frmPO";
	//prompt("url",url);	
	url = url+ "&insertKeyValue="+ prepareInsertData();
	sendAjaxGet(url, poCallBack);
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

function prepareInsertData() {

	
	
	
	
	//alert("in prepare");
	//var array = {"panelFields1":{"empid":"9002","empname":"tutu","bdate":"12-10-2009"},"panelFields":{"empid":"9001","empname":"samarjit","bdate":"12-10-2009"}};
	var dataTable = document.getElementById("panelsdiv").getElementsByTagName("table");
	var pclass = new Array();
	//alert(document.getElementById("rrfquotationid").value);
	//exit();
	//get the quotation id from quotation fileds
	
	

	
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

function updateData(){
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
	//alert(wflcontrollerurl);
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

function viewPO(){
	
	var applicationid = jQuery("#panelsdiv #statusFields  input[id=wflappid]").attr("value");
	var actionid =  jQuery("#panelsdiv #statusFields input[id=wflactiondesc]").attr("value");
	var wflid=jQuery("#panelsdiv #statusFields input[id=wflid]").attr("value");
	var POurl = poFormpart + "?poid="+document.getElementById("poid").value+"&qid="+document.getElementById("qid").value+"&vendorid="+document.getElementById("vendorid").value+"&doString="+actionid+"&wflid="+wflid+"&appid="+applicationid;
	poptastic(POurl);
	return false;
	
}

var newwindow;

function poptastic(url)
{  
	newwindow = window.open(url,'name','height=700,width=1000,resizable=yes,scrollbars=yes');
	if (window.focus) {newwindow.focus();}
}


function loadPO(){

	var url = jsrpcurlpart+"?screenName="+screenName+"&rpcid=viewPO"+"&poid="+poid+"&qid="+qid;
	
	sendAjaxGet(url, requestCallBackPO);
	
}

function requestCallBackPO(p){
	var jobj = JSON.parse(p);
	
	document.getElementById("vendorname").innerHTML = jobj.vendorName;
	document.getElementById("totalamt").innerHTML = jobj.totalprice;
	document.getElementById("quotationref").innerHTML = jobj.qref;
	document.getElementById("quotationid").innerHTML = jobj.qid;
	document.getElementById("itemid").innerHTML = jobj.itemid;
	document.getElementById("curtype").innerHTML = jobj.currency;
	document.getElementById("discount").innerHTML = jobj.discount;
	document.getElementById("curdate").innerHTML = jobj.podate;
	document.getElementById("linetotal").innerHTML = jobj.uprice * jobj.qty;
	document.getElementById("vendoraddr").innerHTML = jobj.vendorAddr;
	document.getElementById("poid").innerHTML = jobj.poid;
	document.getElementById("comments").innerHTML = jobj.comments;
	document.getElementById("misc").innerHTML = jobj.misc;
	document.getElementById("qty").innerHTML = jobj.qty;
	document.getElementById("unitprice").innerHTML = jobj.uprice;



	
}

