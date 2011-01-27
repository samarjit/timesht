//alert("I am coming from test.js");
 var updateCompositeField = function(obj,dfid){
     var str = $(dfid).val();
     var json = {};
     if(str != null && str != "") 
      json = JSON.parse(str);
     json[obj.name] = obj.value;
     var myJSONText = JSON.stringify(json, replacer,"");
     $(dfid).val(myJSONText);
   };
						    
function replacer(key, value) {
	if (typeof value === 'number'  && !isFinite(value)) {
		return String(value);
	}
	return value;
}
 
function submitcallback(form){
	var idlist = eval("panel_"+form.id);
	alert(idlist);
} 

/**
 * rule is a global object created dynamically by java and appended to generated page, and it is extended here.
 * Extra initialization options are provided through var options; variable inside this function
 * @returns ruleobj which is used to initilalize validator
 */
function initRule_Callback(rule) {
	var options = {
			errorElement:"label",
			errorLabelContainer:"#alertmessage", 
			submitHandler: function(form){
			      			  var result =	submitcallback(form);
			      			  if(result == true)
			      				  form.submit();
						     }
				};
	var  ruleobj = $.extend(rule,options);
	return ruleobj;
}

function createMenu(){
 
	
	$.ajax({
  url: 'menuHeader.html',
 
  type: 'GET',
  async: false,
  success: function(data){ // grab content from another page
		$('.menuheader').html(data);
	},
  dataType: 'html'
});
	 
	
	// BUTTONS
    	$('.fg-button').hover(
    		function(){ $(this).addClass('ui-state-focus'); },
    		function(){ $(this).removeClass('ui-state-focus'); }
    	);
    //MENUS DYNAMIC
	$.get('menu1Content.html', function(data){ // grab content from another page
		$('#menubuttonUser').menu({ 
					content: data,//$('#menu1').html(), // grab content from this page
					showSpeed: 400,
					width: 300
				});
		});
	

	 //MENUS DYNAMIC END
  };

  function copyTxttohidden(obj, hid){
	  //document.getElementById(hid).value = obj.value;
  }
  var screenMode = "addrow";
  
  function copySeltohidden(obj,spanid, txtid, populateFieldsToModify){
	  if(obj.value != 'New'){
		  
		  document.getElementById(txtid).value = obj.value;
		  $('#'+spanid).hide();
		 
		   
	  }else{
		  document.getElementById(txtid).value = '';
		  //document.getElementById(hid).value = '';
		  $('#'+spanid).show();
		  
		  $('.field ,.field2').each(function(){
				$(this).val("");
			});
		   
		  obj.value = 'New';
	  }
	  
	  if(populateFieldsToModify!= null)
		  populateFieldsToModify();
  }
  
  function refreshHoverIcon () {
		 //hover states on the static widgets
		$('span[id=icons]').hover(
			function() { $(this).addClass('ui-state-error'); }, 
			function() { $(this).removeClass('ui-state-error'); }
		);
	}
  
  function showMessage (message) { 
		var str = "<div  style='width:100%'>"+message+"<span id='icons'><span class='ui-icon ui-icon-closethick' style='float:right' onclick='hideMessage(this)'></span></span></div>";
		$('.infoBar').append(str);
		$('.infoBar').slideDown('slow');
		refreshHoverIcon ();
	}

  function hideMessage (objSpanIcon) {
		$(objSpanIcon).parent().parent().remove();
		if ($('.infoBar').find('span').length == 0) {
			$('.infoBar').slideUp('slow');
		}
  }
  
  function populateToFields(trobj,divlist, divheaderlist, radClickCallback) {
		var arList = $.trim($(divlist+' '+divheaderlist).text()).split(",");
		//tr:input 
		var dataList =  $(trobj).find(":input");
		$.each(arList,function (i,v){
			var idname = v.substring(0,v.indexOf(":"));
			//first input is radio , skip that
			  $("#"+idname).val(dataList[i+1].value);
		});
		
		setScreenMode("updaterow");
		
		var radioObj = $('input[type=radio]',trobj);
		
		if(radClickCallback != null)
		radClickCallback(radioObj);
	}
  
  /**
   * Adds row to the list table
 * @param formid
 * @param divlist
 * @param divheaderlist
 */
function addrow(formid, divlist, divheaderlist){
		var arList = $.trim($(divlist+' '+divheaderlist).text()).split(",");

		var rowcount = $(divlist+' table').get(0).rows.length;
		//var str = "<tr class='even'><td><input type='radio' value='"+rowcount+"' name='check' id='check"+rowcount+"' onclick='radClick(this,\""+divlist+"\",\""+divheaderlist+"\");' />"+
		//"&nbsp;<span id='icons'><span class='ui-icon ui-icon-pencil' onclick='editMe(this,\""+divlist+"\",\""+divheaderlist+"\", radClickCallback);' ></span></span>"+
		//"<span id='icons'><span class='ui-icon ui-icon-closethick' onclick='deleteMe(this)' ></span></span></td>"; 
		var listtemplate = $(' #listtemplate').html();
		var datamodel = {rowcount: rowcount,divlist: divlist, divheaderlist: divheaderlist}; 
		$.each(arList,function (i,v){
			
			var idname = v.substring(0,v.indexOf(":"));
			var ishidden = v.indexOf("hidden") >1?"none":"table-cell";
			var val = $("#"+idname).val();
			//str+="<td style='display:"+ishidden+"'>"+val+"<input type='hidden' value='"+val+"' id='"+idname+"__"+rowcount+"' name='"+idname+"__"+rowcount+"'/></td>";
			 datamodel[idname]=val;
		});
		//str+="</tr>";
		 var templateResult = 	$.tmpl(listtemplate,datamodel);
		$(divlist+' table').append($('tr',templateResult).parent().html()); 
		refreshHoverIcon ();
	}

	/**
	 * Modifies a row in list table
	 * @param formid
	 * @param divlist
	 * @param divheaderlist
	 */
	function updaterow(formid, divlist, divheaderlist){

		var arList = $.trim($(divlist+' '+divheaderlist).text()).split(",");
		var selectedRadio =$(divlist+' input[type=radio]:checked');
		var selectedIndex = selectedRadio.val(); 
		if (typeof(selectedIndex) == 'undefined') {
			showMessage("Please select a record to modify");
			return;
		}
		//remove all <td> tags
		var TRref = selectedRadio.parent().parent();
		selectedRadio.parent().parent().empty();
		var rowcount =  selectedIndex;
		//var str = "<td><input type='radio' value='"+rowcount+"' name='check' id='check"+rowcount+"' onclick='radClick(this,\""+divlist+"\",\""+divheaderlist+"\",radClickCallback );' />"+
		//"&nbsp;<span id='icons'><span class='ui-icon ui-icon-pencil' onclick='editMe(this,\""+divlist+"\",\""+divheaderlist+"\");' ></span></span>"+
		//"<span id='icons'><span class='ui-icon ui-icon-closethick' onclick='deleteMe(this)' ></span></span></td>"; 
	 	var str = "<td>"+
	       "<input type='radio' value='${rowcount}' name='check' id='check${rowcount}' onclick='radClick(this,\"${divlist}\",\"${divheaderlist}\",radClickCallback );' />"+
		   " &nbsp;<span id='icons'><span class='ui-icon ui-icon-pencil' onclick='editMe(this,\"${divlist}\",\"${divheaderlist}\");' ></span></span>"+
		   "       <span id='icons'><span class='ui-icon ui-icon-closethick' onclick='deleteMe(this)' ></span></span>"+
	       "</td>"+
	      "	<td style='display:none'>${programname}<input type='hidden' value='${programname}' id='programname__${rowcount}' name='programname__{rowcount}'/></td>"+
	  		"<td style='display:table-cell'>${txtnewprogname}<input type='hidden' value='${txtnewprogname}' id='txtnewprogname__${rowcount}' name='txtnewprogname__{rowcount}'/></td>"+
	       " <td style='display:table-cell'>${issuername}<input type='hidden' value='${issuername}' id='issuername__${rowcount}' name='issuername__{rowcount}'/></td>"+
	       " <td style='display:table-cell'>${countryofissue}<input type='hidden' value='${countryofissue}' id='countryofissue__${rowcount}' name='countryofissue__{rowcount}'/></td>";
			
		var listtemplate = $(' #listtemplate').html();
		/*$.each(arList,function (i,v){
			var idname = v.substring(0,v.indexOf(":"));
			var ishidden = v.indexOf("hidden") >1?"none":"table-cell";
			var val = $("#"+idname).val();
			str+="<td style='display:"+ishidden+"'>"+val+"<input type='hidden' value='"+val+"' id='"+idname+"__"+rowcount+"' name='"+idname+"__"+rowcount+"'/></td>";
		});*/
		
		var datamodel = {rowcount: rowcount,divlist: divlist, divheaderlist: divheaderlist}; 
		$.each(arList,function (i,v){
			var idname = v.substring(0,v.indexOf(":"));
			var ishidden = v.indexOf("hidden") >1?"none":"table-cell";
			var val = $("#"+idname).val();
			datamodel[idname]=val;
		});
	    var templateResult = 	$.tmpl(listtemplate,datamodel);
	 
		//replace newly created tags inside <tr> 
		 TRref.append(templateResult.find('tr').html());
		refreshHoverIcon ();
	}
	
  
  
	
	
  