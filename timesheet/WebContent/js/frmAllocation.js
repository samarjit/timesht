function populate()
{
	//alert("This alert box was called with the onload event");	
	fnAdjustTableWidth();
	if(vpassedonvalues != null && vpassedonvalues.trim() != ""){
		prepopulate(vpassedonvalues);
	}
	//alert(unescape(whereClause))
	if((!(whereClause == ""))){
		var url=retriveurlpart+"?panelName=searchPanel&screenName="+screenName;	
		url=url+"&whereClause="+ whereClause;		
		//alert("In message: whereClause=" + whereClause);
		//prompt("url",url);	
		sendAjaxGet(url, requestCallBack);
	}
	disable_fields();
}
//var screenMode = "insert";

function prepopulate(param){
	var json = JSON.parse(param);
	var reqid= json.reqid;
	var empid= json.empid;
	var assetid = json.assetid;
	 
	jQuery("#panelsdiv #allocid").val(reqid.trim());
	jQuery("#panelsdiv #username").val(empid.trim());
	jQuery("#panelsdiv #assetid").val(assetid.trim());
 
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
			if(detailTable[0].rows && detailTable[0].rows.length==0){
				clearFields(detailTable[0]);
				return;
			}
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
function clearFields(tab){
	panelsTable = document.getElementById("panelsdiv").getElementsByTagName("table");
	var query = jQuery("#panelsdiv #"+ tab.id).find(":input");
	jQuery.each(query,function(index,item){item.value="";});
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
		if (panelsTable[i].id == 'panelFields'){
			var query = jQuery(panelsTable[i]).find(" :input");
			var elem = 	jQuery(query);
			jQuery.each(elem, function(index, item) {
				item.disabled = true;
			});
		}
		 
		
	}//btnRead,btnSave,btnModify,btnDelete,btnModify,btnSubmitselAll
	var updateonar = "dnid,Status,wflactionid,wflactiondesc,wflid,btnSave".split(",");
	for ( var i = 0; i < updateonar.length; i++) {
		var arelm = updateonar[i];
		jQuery("#"+ arelm).attr('disabled','disabled');
	}
	
	if(screenMode == "create"){
		var updateonar = "assetid,assetno,assethost,username,assetip,btnSave".split(",");
		for ( var i = 0; i < updateonar.length; i++) {
			var arelm = updateonar[i];
			jQuery("#" + arelm).removeAttr('disabled');
		}
		jQuery('#btnModify').attr('disabled','disabled');
		jQuery('#btnDelete').attr('disabled','disabled');
		jQuery('#btnSubmit').attr('disabled','disabled');
		jQuery("#btnSave").removeAttr('disabled');
	}
	
	if(screenMode == "view"){
		updateonar = "btnModify,btnSubmit,btnDelete".split(",");
		for ( var i = 0; i < updateonar.length; i++) {
			var arelm = updateonar[i];
			jQuery("#"+ arelm).removeAttr('disabled');
		}
	}
	if(screenMode == "modify"){
		var updateonar = "assetid,assetno,assethost,username,assetip,btnSave".split(",");
		for ( var i = 0; i < updateonar.length; i++) {
			var arelm = updateonar[i];
			jQuery("#" + arelm).removeAttr('disabled');
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
	//var url=urlpart+"?panelName=searchPanel&screenName=frmAllocation"+screenName;	
	 
	if(screenMode == "create"  ){ //If the ApplicationID is already decided-say this screen doesnot start a workflow
		//then application Id need not be generated use the prepopulated one to continue with application
		if(document.getElementById("allocid").value == null || document.getElementById("allocid").value ==""){
		   document.getElementById("allocid").value = "AUTOGEN_SEQUENCE_ID";
	     }
		var assetid = jQuery("#panelsdiv #panelFields #assetid").val();
	var url=inserturlpart+"?panelName=searchPanel&screenName=frmAllocation&assetid="+assetid;
	
	var applicationid = jQuery("#panelsdiv #panelFields  input[id=allocid]").attr("value");
	var actionid =  jQuery("#panelsdiv #statusFields input[id=wflactiondesc]").attr("value");
	var wflid=jQuery("#panelsdiv #statusFields input[id=wflid]").attr("value");
	var actionid =  jQuery("#panelsdiv #statusFields input[id=wflactiondesc]").attr("value");
	//prompt("url","action=true&doString="+actionid+"&wflid="+wflid+"&appid="+applicationid);
	//workflow for create activityname=CRAST&create=true
	//workflow for continuation &invokewfl=scrflow&action=true&doString="+actionid+"&wflid="+wflid+"&appid="+applicationid
	url = url+ "&insertKeyValue="+ prepareInsertData()+"&invokewfl=false&action=true&doString="+actionid+"&wflid="+wflid+"&appid="+applicationid;
	//prompt("url",url);
	//add key:vlaue to url
	sendAjaxGet(url, saveCallBack);

	}
	
	if(screenMode == "modify"){
		whereclause  = makeWhereClause();
		var url=updateurlpart+"?wclause="+whereclause+"&screenName=frmAllocation";
		//prompt("url",url);	
		url = url+ "&insertKeyValue="+ prepareInsertData();

		//prompt("url",url);
		//add key:vlaue to url
		

		sendAjaxGet(url, saveCallBack);
		}
	
		
}

function deleteData(){
	
	whereclause  = makeWhereClause();
	var assetid = jQuery("#panelsdiv #panelFields #assetid").val();
	
	var url=deleteurlpart+"?wclause="+whereclause+"&screenName=frmAllocation&assetid="+assetid;
// 	url = prompt("url",url);	
//	alert("in update!!!!!!! url" +url);
	//prompt("url",url);
	//add key:vlaue to url
	screenMode ="delete";

	sendAjaxGet(url, saveCallBack);
	
}

function saveCallBack(val) {
	//show success message 
	// alert(val);
	
	var json = JSON.parse(val);
	
	if(json.error !=null ){
		showerror(json.error);
	}else {
		showalert(json.message);
		if(screenMode =="delete") return;
		
		screenMode="view";
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
var updateonar = "assetno,assethost,username,assetip,btnSave".split(",");
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
	
	var applicationid = jQuery("#panelsdiv #panelFields  input[id=rfqid]").attr("value");
	alert(applicationid);
	var actionid =  jQuery("#panelsdiv #statusFields input[id=wflactionid]").attr("value");
	var wflid=jQuery("#panelsdiv #statusFields input[id=wflid]").attr("value");
	
	//document.getElementById("submitanchor").href //stealing from actionbutton.jsp its not the right way, if its coming from viewDetails this will be wrong anyway! 	
	var url  = wflcontrollerurl+"?action=true&doString="+actionid+"&wflid="+wflid+"&appid="+applicationid;
	alert(url);
	location.href = url;
		
	}

function submitScreenFlowactivity(){
	//alert("submit screenflow activity")
	
	var applicationid = jQuery("#panelsdiv #panelFields  input[id=allocid]").attr("value");
	 
	var actionid =  jQuery("#panelsdiv #statusFields input[id=wflactiondesc]").attr("value");
	var wflid=jQuery("#panelsdiv #statusFields input[id=wflid]").attr("value");
	if(actionid == null || actionid == "" || wflid==null || wflid =="" ){
	 showalert("This activity is performed out of workflow, so workflow wont be invoked");
	 return;
	}
	var assetid = jQuery("#panelsdiv #panelFields #assetid").val();
	//document.getElementById("submitanchor").href //stealing from actionbutton.jsp its not the right way, if its coming from viewDetails this will be wrong anyway! 	
	var url = "scrworkflow.action?action=true&doString="+actionid+"&wflid="+wflid+"&appid="+applicationid+"&screenName="+screenName+"&assetid="+assetid;
	 
	location.href = url;
		
	}
 