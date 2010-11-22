function populate()
{
	//alert("This alert box was called with the onload event");	

	if((!(whereClause == ""))){
		var url=retriveurlpart+"?panelName=searchPanel&screenName="+screenName;	
		url=url+"&whereClause="+ whereClause;		
		//alert("In message: whereClause=" + whereClause);
		// prompt("url",url);	
		sendAjaxGet(url, requestCallBack);
	}	
	//alert("In populate");
}

function clearWhereClause(){
	document.getElementById("panelFieldsWhereClause").Value = "";
}

function requestCallBackview(p){
	//alert("Got from ajax:"+p);

	document.getElementById("retreivedetailsdiv").innerHTML = p;	
	panelsTable = document.getElementById("panelsdiv").getElementsByTagName("table");
	//alert(panelsTable.length);


	detailTable = document.getElementById("retreivedetailsdiv").getElementsByTagName("table");

	for ( var i=0; i<detailTable.length ; i++)
	{
		//alert(detailTable[i].id);			
		if (detailTable[i].id == 'buttonPanel')
			continue;
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
	
	disable_fields();

}

function disable_fields(){
	panelsTable = document.getElementById("panelsdiv").getElementsByTagName("table");

	for(var i =0; i<panelsTable.length;i++){
		
	//	alert("panels "+ panelsTable[i].id);
		if (panelsTable[i].id == 'panelFields'){
			
		fields = panelsTable[i].getElementsByTagName("input");
		//	alert("inside panel panels " + fields.length);
			for(var k = 0; k<fields.length; k++){
			//	alert("inside panel panels " + fields[k].id);
				fields[k].disabled = true;
			
			}
		
		}
	}
}