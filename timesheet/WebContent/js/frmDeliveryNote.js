function populate()
{
	fnAdjustTableWidth();
	
	if(screenMode=="create")
	{
		if(vpassedonvalues != null && vpassedonvalues.trim() != ""){
			prepopulate(vpassedonvalues);
			disable_fields();
		}
		
		if(!(whereClause == ""))
		{
			var url=retriveurlpart+"?panelName=searchPanel&screenName="+screenName;	
			url=url+"&whereClause="+ whereClause;		
			sendAjaxGet(url, dnCallBack); //call this method in commonjs.js.
		}
	}
	else if(screenMode=="viewdetails")
	{
		if(!(whereClause == ""))
		{
			var url=retriveurlpart+"?panelName=searchPanel&screenName="+screenName;	
			url=url+"&whereClause="+ whereClause;		
			sendAjaxGet(url, dnCallBack); //call this method in commonjs.js.
		}
		
	}
}


function prepopulate(param)
{
	var json = JSON.parse(param);
	var poid= json.poid;
	var postatus= json.postatus;
	jQuery("#popanel #poid").val(poid.trim());
	jQuery("#popanel #postatus").val(postatus.trim());
}

function dnCallBack(p)
{
	disable_fields();
	var json = JSON.parse(p);	
	if(json.error !=null )
	{
		showerror(json.error);
	}
	else 
	{
		p = decodeURIComponent(json.message);
		document.getElementById("retreivedetailsdiv").innerHTML = p;	
		panelsTable = document.getElementById("panelsdiv").getElementsByTagName("table");
		detailTable = document.getElementById("retreivedetailsdiv").getElementsByTagName("table");
		for ( var i=0; i<detailTable.length ; i++)
		{
			if (detailTable[i].id == 'buttonPanel' || detailTable[i].rows.length==0)
				continue;
			for(var k = 0; k<detailTable[i].rows[0].cells.length; k++) {			
			// detailTable[i].rows[0].cells[k].childNodes[0].innerText.split(',')[2];

				comStr=jQuery.trim(jQuery(detailTable[i].rows[0].cells[k]).find("div").text()).split(',')[2];
				// alert(jQuery(detailTable[i].rows[0].cells[k]).find("div").text());
				comVal = jQuery.trim(jQuery(detailTable[i].rows[1].cells[k]).text());	  
				
				if(comStr == "dnstatus" && comVal == "SUBMITTED" ){
					jQuery('#btnModify').attr('disabled','disabled');
					jQuery('#btnDelete').attr('disabled','disabled');
					jQuery('#btnSave').attr('disabled','disabled');
					jQuery('#btnSubmit').attr('disabled','disabled');
				}
				
								
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
	}//else
	
}

//***making disable textboxes ***

function disable_fields()
{
panelsTable = document.getElementById("panelsdiv").getElementsByTagName("table");

for(var i =0; i<panelsTable.length;i++){
	
	if (panelsTable[i].id == 'panelFields')
	{
		var query = jQuery(panelsTable[i]).find(" :input");
		var elem = 	jQuery(query);
		jQuery.each(elem, function(index, item) {
			item.disabled = true;
		});
	}

}
if(screenMode == "create")
{
		jQuery('#btnModify').attr('disabled','disabled');
		jQuery('#btnDelete').attr('disabled','disabled');
		jQuery('#btnSubmit').attr('disabled','disabled');
		document.getElementById("dnstatus").value = "CREATE";
		var updateonar ="deliverydate,itemname,receiveddate,itemquantity,warrantydate,invoiceno,chqno,orderno,itemid,Status,applicationid,wflactionid,wflactiondesc,wflid".split(",");
		for ( var i = 0; i < updateonar.length; i++) 
		{
			var arelm = updateonar[i];
			jQuery("#" + arelm).removeAttr('disabled');
		}
		var updateonar1 ="poid,postatus".split(",");
		for ( var j = 0; j < updateonar.length; j++) 
		{
			var arelm1 = updateonar1[j];
			jQuery("#"+ arelm1).attr('disabled','disabled');
		}
}
if (screenMode=="viewdetails")
{
		

		jQuery('#btnSave').attr('disabled','disabled');	
		jQuery('#btnModify').removeAttr('disabled');
		jQuery('#btnCancel').removeAttr('disabled');
	
		
		var updateonar ="deliverydate,itemname,receiveddate,itemquantity,warrantydate,chqno,invoiceno,dnstatus,orderno,itemid,poid,postatus,Status,applicationid,wflactionid,wflactiondesc,wflid".split(",");
		for ( var i = 0; i < updateonar.length; i++) 
		{
			var arelm = updateonar[i];
			jQuery("#"+ arelm).attr('disabled','disabled');
			
		}
		
}


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
//*** date-time picker ***

jQuery(function(){	
	jQuery('#receiveddate').datepicker({
		changeMonth: true,
		changeYear: true,
		dateFormat:'dd/mm/yy'
	});
	jQuery('#deliverydate').datepicker({
		changeMonth: true,
		changeYear: true,
		dateFormat:'dd/mm/yy'
	});
	jQuery('#warrantydate').datepicker({
		changeMonth: true,
		changeYear: true,
		dateFormat:'dd/mm/yy'
	});
});

//*** save() for inserting the new record and updating the existing record ***

function dnSave() 
{
	//alert("Calling dnsave method>>"+screenMode);
	if(screenMode == "create")
	{
		if(document.getElementById("itemname").selectedIndex == 0){		
			showerror("Please choose an item name.");
		}
		else
		{
		document.getElementById("deliverynoteid").value = "AUTOGEN_SEQUENCE_ID";
		var url=inserturlpart+"?panelName=searchPanel&screenName=frmDeliveryNote";
		//url = url + "&itemname=" + document.getElementById("itemname").value;
		//url = url + "&itemquantity=" + document.getElementById("itemquantity").value;
		
		url = url + "&dnpoid=" + document.getElementById("poid").value;
		//alert(url);
		//url = url + "&dnpurchaseorderid=" + document.getElementById("dnpurchaseorderid").value;
		//alert(document.getElementById("deliverynoteid").value);
		//action=true&doString="+actionid+"&wflid="+wflid+"&appid="+applicationid;
		//url = url+ "&insertKeyValue="+ prepareInsertData()+"&invokewfl=scrflow&activityname=CDN&create=true";
		//url = url+ "&insertKeyValue="+ prepareInsertData()+"&invokewfl=scrflow&activityname=CDN&create=true";
		url = url+ "&insertKeyValue="+ prepareInsertData();
		//alert("URL>>>"+url);
		sendAjaxGet(url, saveCallBack);
		}
	}
	
	else if(screenMode == "modify")
	{
		whereclause  = makeWhereClause();
		var url=updateurlpart+"?wclause="+whereclause+"&screenName=frmDeliveryNote";
		prompt("url",url);	
		url = url+ "&insertKeyValue="+ prepareInsertData();
		sendAjaxGet(url, saveCallBack); //call this method in commonjs.js.
	}
}
//***for successful and error message ***
//Error/Successful Message for deleting
function saveCallBack(val) 
{
	//show success message 
	var json = JSON.parse(val);
	if(json.error !=null )
	{
		showerror(json.error);
	}
	else 
	{
		showalert(json.message);
		
		jQuery('#btnSave').attr('disabled','disabled');	
		jQuery('#btnModify').removeAttr('disabled');
		jQuery('#btnCancel').removeAttr('disabled');
		var updateonar ="deliverydate,itemname,receiveddate,itemquantity,warrantydate,chqno,invoiceno,dnstatus,orderno,itemid,poid,postatus,Status,applicationid,wflactionid,wflactiondesc,wflid".split(",");
		for ( var i = 0; i < updateonar.length; i++) 
		{
			var arelm = updateonar[i];
			jQuery("#"+ arelm).attr('disabled','disabled');
			
		}		
		var allfields = prepareInsertData();
		var json1 = JSON.parse(allfields);
		var valuesar = json1.json1[0].valuesar;
		var k = new Object();
		k.json1 = valuesar;
		var myJSONText = JSON.stringify(k, replacer,"");
		whereClause = myJSONText.replace("AUTOGEN_SEQUENCE_ID","");
		//alert(whereClause);
		//populate();
		//location.href= ctxpath+"/template1.action?screenName=frmDeliveryNoteList";
	}
}

//***delete the existing delivery note***
function deleteData()
{
	whereclause  = makeWhereClause();
	var url=deleteurlpart+"?wclause="+whereclause+"&screenName=frmDeliveryNote";
	sendAjaxGet(url, cancelCallBack);//call this method in commonjs.js.
}

//Error/Successful Message for deleting
function cancelCallBack(val) {
	//show success message 
	if(val < 0)
	{
		showerror("Could not delete : Error Occured! ");
	}
	else
	{
		location.href= ctxpath+"/template1.action?screenName=frmDeliveryNoteList";
		showalert("Successfully deleted your deliverynote! ");
	}
}

//Error/Successful Message for saving

function backtolist()
{
	location.href= ctxpath+"/template1.action?screenName=frmDeliveryNoteList";
}

function KeyValue(a,b) 
{
	this.key=a;
	this.value=b;
}

function panelClass(a,b) 
{
	this.name = a;
	this.valuesar = b;
}

function replacer(key, value) {
	if (typeof value === 'number' && !isFinite(value)) 
	{
		return String(value);
	}
	return value;
}

function prepareInsertData() 
{
	
	document.getElementById("dnpoid").value=document.getElementById("poid").value;
	var dataTable = document.getElementById("panelsdiv").getElementsByTagName("table");
	var pclass = new Array();
	for (var i=0; i<dataTable.length; i++) 
	{
		var query = "#panelsdiv #" + dataTable[i].id + " :input";
		var requestar = new Array();
		var elem = 	jQuery(query); 
		var j = 0;
		jQuery.each(elem,function(index,item)
		{
			requestar[j] = new KeyValue(item.id, item.value);
			j++;						
		});
		pclass[i] = new panelClass(dataTable[i].id,requestar);					
	}	
	var k = new Object();
	k.json = pclass;
	var myJSONText = JSON.stringify(k,replacer,"");
	return myJSONText;			
}

function updateData(obj)
{
	screenMode = "modify";
	document.getElementById("btnSave").disabled=false;
	document.getElementById("btnModify").disabled=true;
	document.getElementById("btnDelete").disabled=true;
	var updateonar ="deliverydate,itemname,receiveddate,itemquantity,warrantydate,itemid".split(",");
	for ( var i = 0; i < updateonar.length; i++) 
	{
		var arelm = updateonar[i];
		jQuery("#" + arelm).removeAttr('disabled');
	}
}
		
var dnid= null;
function makeWhereClause()
{
	//There will be only one table in search screen 'search div'
	listTable = document.getElementById("retreivedetailsdiv").getElementsByTagName("table")[0];
	whereClause = "panelFields1WhereClause=";
	if(listTable != null)
	{
		var j=0;
		requestar = new Array();
		for (var i = 0; i <listTable.rows[0].cells.length ; i++)
		{  
			if(jQuery("#retreivedetailsdiv").find(" table tbody tr th").eq(i).find(" div").text().split(',')[6]  == "Y") 
			{
				name = jQuery("#retreivedetailsdiv").find(" table  tbody tr th").eq(i).find("div").text().split(',')[2];	 
				name = jQuery.trim(name);
				value = jQuery("#retreivedetailsdiv").find(" table tbody tr").eq(1).find(" td").eq(i).text();
				value = jQuery.trim(value);
				dnid=value;
				alert("delivery note id>>"+dnid);
				whereClause = whereClause + name + "!" + value + "~#";
				requestar[j] = new KeyValue(name, value);				
				j++;		
			}
		}
		var k = new Object();
		k.json = requestar;
		var myJSONText = JSON.stringify(k, replacer,"");
		whereClause = encodeURIComponent(myJSONText);//whereClause.replace(/(~#)$/, '');
	}
	return whereClause;	 
}

function clearWhereClause()
{
	document.getElementById("panelFieldsWhereClause").Value = "";
}

function insertData() 
{
	var dataTable = document.getElementById("panelsdiv").getElementsByTagName("table");
}	

function dnSubmit() 
{
	
	var applicationid = document.getElementById("applicationid").value;
	alert("applicationid"+applicationid);
	var actionid =  document.getElementById("wflactiondesc").value;
	var wflid= document.getElementById("wflid").value;
	var dnid= document.getElementById("deliverynoteid").value;
	//document.getElementById("submitanchor").href //stealing from actionbutton.jsp its not the right way, if its coming from viewDetails this will be wrong anyway! 	
	var url = "scrworkflow.action?action=true&doString="+actionid+"&wflid="+wflid+"&appid="+applicationid+"&screenName=frmDeliveryNote"+"&deliverynoteid="+ dnid + "&action=submit"  ;
	//alert(url);
	location.href = url;
	//prepareInsertData();
}

