
function search(){
		 
		var url=urlpart+"?panelName=searchPanel&screenName="+screenName;
		if(document.getElementById("srrfid"))
			url=url+'&srrfid='+document.getElementById("srrfid").value;
		if(document.getElementById("squotationid"))
			url=url+'&squotationid='+document.getElementById("squotationid").value;
		if(document.getElementById("sstatus"))
			url=url+'&sstatus='+document.getElementById("sstatus").value;
		if(document.getElementById("srrfdate"))
			url=url+'&srrfdate='+document.getElementById("srrfdate").value;
					
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
}


jQuery(function() {
	jQuery('#srrfdate').datepicker({
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


function viewdetails(){
 
	// alert("in make url,selectedIdx:"+selectedIdx);
	//There will be only one table in search screen 'search div'
	
	if(selectedIdx == -1)
	{
		showerror("Please select a record");
	}
	
	listTable = document.getElementById("searchdiv").getElementsByTagName("table")[0];
	screenMode = "viewdetails";
	
	whereClause = "panelFields1WhereClause=";
	if(listTable != null && selectedIdx != -1){
		//poplate wher clause url
		var j=0;
		requestar = new Array();
		for (i = 0; i <listTable.rows[0].cells.length ; i++ )
		{  
			//alert(listTable.rows[0].cells[i].childNodes[0].innerText.split(',')[6]);
			if(jQuery("#searchdiv").find(" table tbody tr th").eq(i).find(" div").text().split(',')[6]  == "Y") {
				name = jQuery("#searchdiv").find(" table  tbody tr th").eq(i).find("div").text().split(',')[2];	 
				name = jQuery.trim(name);
				value = jQuery("#searchdiv").find(" table tbody tr").eq(selectedIdx).find(" td").eq(i).text();
				value = jQuery.trim(value);
				whereClause = whereClause + name + "!" + value + "~#";
				requestar[j] = new KeyValue(name, value);				
				j++;		
				//alert(jQuery("#searchdiv table th:eq("+i+") div").text());
			}
		}
		var k = new Object();
		k.json = requestar;
		var myJSONText = JSON.stringify(k, replacer,"");
		
		whereClause = encodeURIComponent(myJSONText);//whereClause.replace(/(~#)$/, '');
		 
		 
		document.getElementById("panelFieldsWhereClause").value=whereClause;
		document.getElementById("screenMode").Value=screenMode;
		document.getElementById("formwhere").screenName.value = "frmRRF";
		document.getElementById("formwhere").submit();
	}
	else {
		return false;
	}
	return true;	 

}

//create url with where clause
function clearWhereClause(){
	document.getElementById("panelFieldsWhereClause").value="";
}


