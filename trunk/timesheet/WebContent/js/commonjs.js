if(typeof (jQuery )!= "undefined")
jQuery(document).ready(function() { 
	// Setup the ajax indicator
	jQuery("body").append('<div id="ajaxBusy"><p><img src="css/images/ajax-loader.gif"></p></div>');
	jQuery('#ajaxBusy').css({
		display:"none",
		margin:"0px",
		paddingLeft:"20px",
		paddingRight:"0px",
		paddingTop:"0px",
		paddingBottom:"20px",
		position:"absolute",
		right:"700px",
		top:"350px",
		width:"auto"
	});
 
	// Ajax activity indicator bound 
	// to ajax start/stop document events
	jQuery(document).ajaxStart(function(){ 
		jQuery('#ajaxBusy').show(); 
	}).ajaxStop(function(){ 
		jQuery('#ajaxBusy').hide();
	});
 
	///// Ignore this, just for demo purposes \\\\\\
    // bind form using ajaxForm 
    
});

var xmlHttpReq = false;
function xmlhttpPost() 
{
	if(xmlHttpReq)return;
    xmlHttpReq = false;
    var self = this;
    // Mozilla/Safari
    if (window.XMLHttpRequest) {
        self.xmlHttpReq = new XMLHttpRequest();
    }
    // IE
    else if (window.ActiveXObject) {
        self.xmlHttpReq = new ActiveXObject("Microsoft.XMLHTTP");
    }
}

function sendAjaxPost(strURL,data,callbak)
{ 
	xmlhttpPost();
	if(strURL == null)
		strURL="searchlist.action";
	var rnum = Math.random()*1000;
	
	if(strURL.indexOf("?") >-1)strURL+="&rnum="+rnum;
	else strURL+="?rnum="+rnum;
	
	 if(data == null)data="";
	 if(typeof( callbak) !="undefined" )mycallback = callbak;
	self.xmlHttpReq.open('POST', strURL, true);
	self.xmlHttpReq.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
	self.xmlHttpReq.onreadystatechange = doworkcallback;
	self.xmlHttpReq.send(data);
}


function sendAjaxGet(strURL,callbak){ 
	/*xmlhttpPost();
	if(strURL == null)
	strURL="searchlist.action";
	 
	var rnum = Math.random()*1000;
	
	if(strURL.indexOf("?") >-1)strURL+="&rnum="+rnum;
	else strURL+="?rnum="+rnum;
	*/
	 if(typeof( callbak) !="undefined" )mycallback = callbak;
	/*self.xmlHttpReq.open('GET', strURL, true);
	self.xmlHttpReq.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
	self.xmlHttpReq.onreadystatechange = doworkcallback;
	self.xmlHttpReq.send();*/
	jQuery.ajax({
		   type: "GET",
		   url: strURL,
		   success:  callbak

		 });
}

function doworkcallback() {
    if (self.xmlHttpReq.readyState == 4) {
    	mycallback(xmlHttpReq.responseText);
    }
}

function mycallback(val){
	 alert("Using default Ajax callback function:"+ val );
}

function parseXMLDocFromString(parm){
	var xmlDoc;
	if (window.DOMParser)
	  {
	  var parser=new DOMParser();
	  xmlDoc=parser.parseFromString(parm,"text/xml");
	  }
	else // Internet Explorer
	  {
	  xmlDoc=new ActiveXObject("Microsoft.XMLDOM");
	  xmlDoc.async="false";
	  xmlDoc.loadXML(parm);
	  }
	return xmlDoc;
}

function isNumeric(form_value) 
{ 
    if (/^\d+$/.test(form_value) == false) 
        return false; 
    else 
        return true; 
} 

function showerror(p){
	//jQuery('#errormsgdiv').css("display","none");
	jQuery('#errormsgdiv').html("<p>Error:"+p+"</p>");
	var selectedEffect = 'clip';
	var options = {};
	jQuery("#errormsgdiv").show(selectedEffect,options,500,callback);
	function callback() {
//		setTimeout(function(){
//			jQuery("#errormsgdiv:visible").hide(selectedEffect,options,500,null);
//		}, 5000); 
	}
}
function showalert(p){
		//jQuery('#alertmsgdiv').css("display","none");
		jQuery('#alertmsgdiv').html("<p>Message:"+p+"</p>");
		var selectedEffect = 'clip';
		var options = {};
		jQuery("#alertmsgdiv").show(selectedEffect,options,500,callback);
		function callback() {
			setTimeout(function(){
				jQuery("#alertmsgdiv:visible").hide(selectedEffect,options,500,null);
			}, 5000); 
		}
	}

/**
 * jGlycy
 * (c) 2008 Semooh (http://semooh.jp/)
 * 
 */
(function($, prefix, jg){
  $[jg] = $({});
  $[jg].extend({
    invoke: function(nodes) {
      nodes.each(function(){
        var node = this;
        var funcs = $(node).attr(prefix).split(',');
        $(funcs).each(function(){
          var arg = $(node).attr(prefix + ":" + this);
          if(arg) {
            eval('var options = {' + arg + '}');
          } else {
            var options = {};
          }
          if($.fn[this]) {
            $(node)[this](options);
          }
        });
      });
    },
    invokeElement: function(node) {
      $[jg].invoke($("*[" + prefix + "]", node));
    }
  });
  $(document).ready(function(){
    $[jg].invokeElement(document);
  });
})(jQuery, "jg", "jg");