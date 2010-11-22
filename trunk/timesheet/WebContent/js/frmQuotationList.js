
function search(){
		 
		var url=urlpart+"?panelName=searchPanel&screenName="+screenName;
		if(document.getElementById("squotationid"))
			url=url+'&squotationid='+document.getElementById("squotationid").value;
		if(document.getElementById("sitemid"))
			url=url+'&sitemid='+document.getElementById("sitemid").value;
		if(document.getElementById("svendorid"))
			url=url+'&svendorid='+document.getElementById("svendorid").value;
		if(document.getElementById("squotationdate"))
			url=url+'&squotationdate='+document.getElementById("squotationdate").value;
		if(document.getElementById("squotationref"))
			url=url+'&squotationref='+document.getElementById("squotationref").value;
		if(document.getElementById("sdateofdelivery"))
			url=url+'&sdateofdelivery='+document.getElementById("sdateofdelivery").value;
					
		var pagesize = jQuery('.searchdiv .pagesize').val();
		var pageno = jQuery('.searchdiv .pageno').val();
		
		if(pagesize)
			url=url+'&pagesize='+pagesize;
		if(pageno)
			url=url+'&pageno='+pageno;
		
		sendAjaxGet(url,mycall);
}

function mycall(p){
	//alert("Got from ajax:"+p);
	document.getElementById("searchdiv").innerHTML = p;
	addSelectEvents();
	//clearSearchFields();
}

/*function clearSearchFields()
{
	document.getElementById("squotationid").value = "";
	document.getElementById("sitemid").value = "";
	document.getElementById("svendorid").value = "";
	document.getElementById("squotationdate").value = "";
	document.getElementById("squotationref").value = "";
	document.getElementById("sdateofdelivery").value = "";
}*/

jQuery(function() {
	jQuery('#squotationdate').datepicker({
		changeMonth: true,
		changeYear: true
	});
	
	jQuery('#sdateofdelivery').datepicker({
		changeMonth: true,
		changeYear: true
	});
});

var selectedIdx = -1;

function cleanUp() {
	var arobj = document.getElementById("searchdiv").getElementsByTagName("TR");

	for (var i =1 ; i < arobj.length; i++) {
		arobj[i].style.backgroundColor= "#E7FFBF";
	}
}

function addSelectEvents(){
 
	var srchdv = document.getElementById("searchdiv").getElementsByTagName("TR");

	for (var i =0;i< srchdv.length;i++) {
	if(srchdv[i].childNodes[0].tagName != "TH"){ 
			srchdv[i].onclick=function(p){
			 cleanUp();
			 selectedIdx  = this.rowIndex;
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
 
	//alert("in make url,selectedIdx:"+selectedIdx);
	//alert(btnname.id);
	//There will be only one table in search screen 'search div'	
	listTable = document.getElementById("searchdiv").getElementsByTagName("table")[0];
	
	if(selectedIdx == -1)
	{
		showerror("Please select a record");
	}
	
	whereClause = "panelFields1WhereClause=";
	if(listTable != null && selectedIdx != -1){
		//poplate wher clause url
		var j=0;
		requestar = new Array();
		//alert(listTable.rows[0].cells.length);
		for (i = 0; i <listTable.rows[0].cells.length ; i++ )
		{  
			//alert(listTable.rows[0].cells[i].childNodes[0].innerText.split(',')[6]);
			//alert(jQuery("#searchdiv").find(" table tbody tr th").eq(i).find(" div").text().split(',')[6]);
			if(jQuery("#searchdiv").find(" table tbody tr th").eq(i).find(" div").text().split(',')[6]  == "Y") {
				name = jQuery("#searchdiv").find(" table  tbody tr th").eq(i).find("div").text().split(',')[2];	 
				name = jQuery.trim(name);
				value = jQuery("#searchdiv").find(" table tbody tr").eq(selectedIdx).find(" td").eq(i).text();
				value = jQuery.trim(value);
				whereClause = whereClause + name + "!" + value + "~#";	
				//alert(name);
				//alert(value);
				//alert(btnname.id);
				if(name == 'qtstatus' && value == 'RRFCREATED' && btnname.id=='createrrf' ){
					//alert("RRF Already created..");
					showerror("RRF Already created..");
					return false;
				}
				//exit();
				requestar[j] = new KeyValue(name, value);		
				j++;		
				//alert(jQuery("#searchdiv table th:eq("+i+") div").text());
			}
		}
		var k = new Object();
		k.json = requestar;
		
		var myJSONText = JSON.stringify(k, replacer,"");
		//alert("requestar"+requestar);
		whereClause = encodeURIComponent(myJSONText);//whereClause.replace(/(~#)$/, '');
		
		//alert(whereClause);
		document.getElementById("panelFieldsWhereClause").value=whereClause;
		if(btnname.id=='createrrf'){			
			
			document.getElementById("formwhere").screenName.value = "frmRRF";	
			document.getElementById("screenMode").value= "createrrf";
			//document.getElementById("formwhere").submit();
		}
		else {
			//alert("in view details");
			document.getElementById("formwhere").screenName.value = "frmQuotation";
			//document.getElementById("formwhere").submit();
		}
		document.getElementById("formwhere").submit();
	}
	else {
		
		return false;
	}
	
	return true;	 

}

//create url with where clause
function clearWhereClause(){
	//alert("QuotationList:clearWhereClause");
	document.getElementById("panelFieldsWhereClause").value="";
}



