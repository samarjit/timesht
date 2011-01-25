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
	  document.getElementById(hid).value = obj.value;
  }
  var screenMode = "view";
  
  function copySeltohidden(obj,txtid, hid){
	  if(obj.value != 'New'){
		  document.getElementById(hid).value = obj.value;
		  $('#'+txtid).hide();
		  try{
			  populateFieldsToModify();
		  }catch(e){ 
			  if(typeof(console) != 'undefined'){
				  console.log(e+"This should be defined for every page seperately");
			  }else{
				  alert("TODO: either there is error in populateFieldsToModify(), or it is not defined,it should be defined per page");
			  }
		  }
		  screenMode = "modify";
	  }else{
		  document.getElementById(txtid).value = '';
		  document.getElementById(hid).value = '';
		  $('#'+txtid).show();
		  
		  $('.field ,.field2').each(function(){
				$(this).attr("value","");
			});
		  screenMode = "addnew";
		  obj.value = 'New';
	  }
  }
  
  