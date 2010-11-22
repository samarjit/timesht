<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags" %>    
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"> 
<html>
<head>
</head>
<style type="text/css">
 .headertxt{
 width:100%;
 }

 .bodytext{
 width:100%;	
 }
  .emailtext{
 width:690px;
 height:340px;
 }
</style>
 <link rel="stylesheet" href="<%=request.getContextPath() %>/css/images/jquery.wysiwyg.css" type="text/css" />
<script language="javascript" src="<%=request.getContextPath() %>/js/jquery.js"></script>
<script language="javascript" src="<%=request.getContextPath() %>/js/json2.js"></script>
<script language="javascript" src="<%=request.getContextPath() %>/js/encoder.js"></script>
<script language="Javascript">
var ctxpath="<%=request.getContextPath() %>";
</script>

  
 <script type="text/javascript" src="<%=request.getContextPath() %>/jHtmlArea/scripts/jHtmlArea-0.7.0.js"></script>
 <link rel="Stylesheet" type="text/css" href="<%=request.getContextPath() %>/jHtmlArea/style/jHtmlArea.css" />  
  
<script language="Javascript">

var vendorid= '${vendorid}';

var rfqid= '${rfqid}';
function $(p){
	if(document.getElementById(p))
	return document.getElementById(p);
	return null;
}

function $F(p){
	if(document.getElementById(p))
	return document.getElementById(p).value;
	return null;
}

</script>

<script language="javascript">
function populateData(){
	opener.populateEmailPage(this);
}
function populateCallbackValues(){
	alert("Args:"+arguments[0]);
	//$F("rfqid"),$F("itemtype"),$F("itemdesc"),$F("quantity")
	$("quotation").value 
	var strBody = "Request for Quotation ID:"+arguments[0]+"\n"+
	"Vendor ID:"+vendorid+"\n"+
	"Item Type:"+arguments[1]+"\n"+
	"Item Description:"+arguments[2]+"\n"+
	"Item Quantity:"+arguments[3]+"\n";
	$("bodytext").innerHTML = $("bodytext").innerHTML+"\n"+strBody;
}

function testjsrpc(){
	jQuery.ajax({
		   type: "GET",
		   url: "jsrpc.action?screenName=frmRFQ&rfqid="+rfqid+"&vendorid="+vendorid+"&rpcid=vendordata",
		   success:  rpccallback

		 });
}
function rpccallback(parm){
	var jobj = JSON.parse(parm);
	if(jobj.error != null ){
	alert(jobj.error);return;
	} 
	$("vendorid").innerHTML = jobj.vendor_id;
	$("vendoremail").innerHTML = jobj.vendor_email;
	$("vendorname").innerHTML = jobj.vendor_name;
	$("vendoraddress").innerHTML = jobj.vendor_address;
	$("rfqid").innerHTML = rfqid;
	$("itemtype").innerHTML = jobj.rfq_item_type;
	$("itemdesc").innerHTML = jobj.rfq_item_desc;
	$("itemquantity").innerHTML = jobj.rfq_item_qty;
	$("vendoremailidtxt").value= jobj.vendor_email;

	var somtext = $("quotation").innerHTML;
	document.getElementById("edited").value = somtext;
	 fncopy();
}



function fncopy() {
	var strbody = document.getElementById("edited").value;
	  
	 var ifrm = document.getElementById('myIframe');
	              ifrm = (ifrm.contentWindow) ? ifrm.contentWindow : (ifrm.contentDocument.document) ? ifrm.contentDocument.document : ifrm.contentDocument;
	              ifrm.document.open();
	              ifrm.document.write(Encoder.htmlDecode(strbody));
	              ifrm.document.close();
	                        
	}

 var state = "preview";
	 
 function toggleDiv(btnobj) {
	  if(state=="preview"){
			state = "edit";
			btnobj.value = "Preview ";
		
			document.getElementById("float1").style.display = "block";
			document.getElementById("float2").style.display = "none";
			document.getElementById("edited").style.display = "block";
			jQuery("#edited").htmlarea();
			 reHeight();  
		}else{
			state = "preview";
			btnobj.value = "    Edit    ";
			document.getElementById("float1").style.display = "none";
			document.getElementById("float2").style.display = "block";
			document.getElementById("edited").style.display = "none";
			 fncopy();
			 jQuery("#edited").htmlarea("dispose");
		}
	 

	 }

 function fnEmailSend() {
	   var content = "<html>"+document.getElementById('myIframe').contentWindow.document.documentElement.innerHTML+"</html>";
	var url = "<%=request.getContextPath() %>/sendmail.action?sendto="+jQuery("#vendoremailidtxt").val()+
	'&from='+jQuery("#fromtxt").val()+'&msgbody='+escape(content)+'&subject='+jQuery('#subjecttxt').val();
//alert(content);
	 jQuery.ajax({
		  type: "GET",
		  url: url,
		  success:emailSendNotification
	});
	
}

function emailSendNotification(res){ 
	alert("returned result"+res);
	var result = JSON.parse(res);
	if(result.ERROR != null ){
		alert("Error occured:"+result.ERROR);
	}else{
		alert("Email sent");
		opener.rfqemailSent(vendorid);
		window.close();
	}
	
}


 function reHeight(){
	 var off = jQuery("#container").offset();
     jQuery('.jHtmlArea iframe').css("height",String(parseInt(jQuery("#container").height())-28)+"px");

	} 	
</script>
 <body onload="testjsrpc(); ">

 <input type="button" value="Send" onclick="fnEmailSend();" /> 
 <input type="button" value="Edit" onclick="toggleDiv(this)" /> 
<table border="1" width="700px">
<tr>
	<td width="30%">To:</td>
	<td><input type="text" name="totxt" id="vendoremailidtxt" value="" class="headertxt" /></td>
</tr>
<tr>
	<td>From:</td>
	<td><input type="text" value="ams@nucleussoftware.com" name="fromtxt" id='fromtxt' class="headertxt"  /></td>
</tr>
<tr>
	<td>CC:</td>
	<td><input type="text" name="cctxt" id="cctxt"  value="" class="headertxt" /></td>
</tr>
<tr>
	<td>Subject</td>
	<td><input type="text" id="subjecttxt" value="Request for quotation" class="headertxt" /></td>
</tr>
<tr>
	<td colspan="2">
	
	<!--  body text of email  -->
	
		<div id="container" class="container" style="position:relative;height:340px">
			<div id="float1" style="position:absolute;top:0px;left:0px;paddin:0px;margin:0px;display:none">
				<form id="Whizzy">
				 
				<textarea id="edited" rows="10" cols="20"  class="emailtext"  >
				 
				 
				</textarea>
				</form>
			</div>
			 <div id="float2" class="" style="position:absolute;top:0px;left:0px;paddin:0px;margin:0px;display:block">
				<iframe id="myIframe" src=""  width="690" height="340"  class="emailtext">
				
			
				</iframe>
			  </div>	
 		</div> 
		<div id="quotation" style="display:none">
		Nucleus Software requires a quotation for the following items from<br />
					<table>
						<tr><td>Vendor Id:</td><td><div id="vendorid"  ></div></td></tr> 
						<tr><td>Email:</td><td><div id="vendoremail"  ></div></td></tr>
						<tr><td>Name:</td><td><div id="vendorname"  ></div></td></tr>
						<tr><td>Address:</td><td><div id="vendoraddress"  ></div></td></tr>
					</table>  
					<hr />
					<h4>The request is for the following items:</h4>
					<table width="100%">
							<tbody>
							<tr>
								<td>Request for Quotation ID:</td>
								<td align="right"><div id="rfqid"></div></td>
							</tr>
							<tr></tr>
							<tr>
								<td>Item Type:</td>
								<td align="right"><div id="itemtype"></div></td>
							</tr>
							<tr></tr>
							<tr>
								<td>Item Description:</td>
								<td align="right"><div id="itemdesc"></div></td>
							</tr>
							<tr></tr>
							<tr>
								<td>Item Quantity:</td>
								<td align="right"><div id="itemquantity"></div></td>
							</tr>
							<tr></tr>
							</tbody>
					</table> 
		</div>
		
		
		<!--  end of body text of email  -->
	</td>
	
</tr>
</table>
 
 </body>
</html>
