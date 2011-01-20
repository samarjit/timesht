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