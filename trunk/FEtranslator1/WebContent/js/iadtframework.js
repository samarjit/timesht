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