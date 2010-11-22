//***for searching according with the search criteria.***
function search()
{
	var url=urlpart+"?panelName=searchPanel&screenName="+screenName;
	
	if(document.getElementById("sdeliverynoteid"))
		url=url+'&sdeliverynoteid='+document.getElementById("sdeliverynoteid").value;
	if(document.getElementById("spurchaseorderid"))
		url=url+'&spurchaseorderid='+document.getElementById("spurchaseorderid").value;
	if(document.getElementById("sdeliverydate"))
		url=url+'&sdeliverydate='+document.getElementById("sdeliverydate").value;
	if(document.getElementById("sreceiveddate"))
		url=url+'&sreceiveddate='+document.getElementById("sreceiveddate").value;
	if(document.getElementById("swarrantydate"))
		url=url+'&swarrantydate='+document.getElementById("swarrantydate").value;
	if(document.getElementById("schqno"))
		url=url+'&schqno='+document.getElementById("schqno").value;
	if(document.getElementById("sinvoiceno"))
		url=url+'&sinvoiceno='+document.getElementById("sinvoiceno").value;
	if(document.getElementById("sorderno"))
		url=url+'&sorderno='+document.getElementById("sorderno").value;
	if(document.getElementById("sstatus"))
		url=url+'&sstatus='+document.getElementById("sstatus").value;
	
	var pagesize = jQuery('.searchdiv .pagesize').val();
	var pageno = jQuery('.searchdiv .pageno').val();
	if(pagesize)
		url=url+'&pagesize='+pagesize;
	if(pageno)
		url=url+'&pageno='+pageno;
	
	sendAjaxGet(url,mycall); //call this method in commonjs.js.
}

var selectedIdx =-1;
//***retrieve the data for the search criteria***
function mycall(p)
{
	document.getElementById("searchdiv").innerHTML = p;
	addSelectEvents();
}

//***get the index of selected record***
function addSelectEvents()
{
 	var srchdv = document.getElementById("searchdiv").getElementsByTagName("TR");
	for (var i =0;i< srchdv.length;i++) 
	{
		if(srchdv[i].childNodes[0].tagName != "TH")
		{ 
			srchdv[i].onclick=function(p)
			{
			  cleanUp();
			  selectedIdx  = this.rowIndex;
			  this.style.backgroundColor= "#D6F1A3";
			};
		}
	}
}

function cleanUp() 
{
	var arobj = document.getElementById("searchdiv").getElementsByTagName("TR");
	for (var i =1 ; i < arobj.length; i++) 
	{
		arobj[i].style.backgroundColor= "#E7FFBF";
	}
}

//***create url with where clause***
function KeyValue(a,b) 
{
	this.key=a;
	this.value=b;
}

function replacer(key, value) 
{
	if (typeof value == 'number' && !isFinite(value)) 
	{
		return String(value);
	}
	
	return value;
}

//***get the details of selected record from the list table.***
function viewdetails()
{
 	listTable = document.getElementById("searchdiv").getElementsByTagName("table")[0];
	whereClause = "panelFields1WhereClause=";
	
	if(selectedIdx<=0)//there is no record selected
	{
		showerror("Please select a record.");
		return false;
	}		
	else if(listTable != null && selectedIdx != -1)
	{
		var j=0;
		requestar = new Array();
		for (var i=0; i <listTable.rows[0].cells.length ; i++ )
		{  
			if(jQuery("#searchdiv").find(" table tbody tr th").eq(i).find(" div").text().split(',')[6]  == "Y") 
			{
				name = jQuery("#searchdiv").find(" table  tbody tr th").eq(i).find("div").text().split(',')[2];	 
				name = jQuery.trim(name);
				value = jQuery("#searchdiv").find(" table tbody tr").eq(selectedIdx).find(" td").eq(i).text();
				value = jQuery.trim(value);
				whereClause = whereClause + name + "!" + value + "~#";
				requestar[j] = new KeyValue(name, value);	
				j++;		
			}
		}
		var k = new Object();
		k.json = requestar;
		var myJSONText = JSON.stringify(k, replacer,"");//in json2.js
		
		whereClause = encodeURIComponent(myJSONText);//whereClause.replace(/(~#)$/, '');
		
		//***call the frmDeliveryNote for the details of selected record***
		document.getElementById("panelFieldsWhereClause").value=whereClause;
		document.getElementById("formwhere").screenName.value = "frmDeliveryNote";
		document.getElementById("screenMode").value="viewdetails";
		document.getElementById("formwhere").submit();
	}
	else {	return false; }
	
	return true;

}//view details

//***clear whereclause ***
function clearWhereClause()
{
	document.getElementById("panelFieldsWhereClause").value="";
}


