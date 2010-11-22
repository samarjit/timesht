<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Print</title>

<style type="text/css">
 #vendor div{
 text-decoration:emphasis;
 }
 
 table address{
 float:left;
 }
</style>


<script language="javascript" src="<%=request.getContextPath() %>/js/jquery.js"></script>
<script language="javascript" src="<%=request.getContextPath() %>/js/json2.js"></script>
<script language="javascript">
var vendorid = '${vendorid}';
var rfqid = '${rfqid}';
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
function fnEmailSend() {

	opener.rfqPrinted(vendorid);
	 
	window.close();
	
}

function populateData(){
	opener.populatePrintPage(this);
}
function populateCallbackValues(){
	alert("Args:"+arguments[0]);
	//$F("rfqid"),$F("itemtype"),$F("itemdesc"),$F("quantity")
	$("quotation").value; 
	var strBody = "<table width='100%'>"+
		"<tr><td>Request for Quotation ID:</td><td align='right'>"+arguments[0]+"</td><tr>"+
//	"<tr><td>Vendor ID:</td><td align='right'>"+vendorid+"</td><tr>"+
	"<tr><td>Item Type:</td><td align='right'>"+arguments[1]+"</td><tr>"+
	"<tr><td>Item Description:</td><td align='right'>"+arguments[2]+"</td><tr>"+
	"<tr><td>Item Quantity:</td><td align='right'>"+arguments[3]+"</td><tr></table>";
	$("quotation").innerHTML = $("quotation").innerHTML+"<br />"+strBody;
}
function fnPrint(objBtn){
objBtn.style.visibility = "hidden";	
	window.print();
	fnEmailSend();
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
}
</script>
</head>
<body onload="testjsrpc()">
<input type="button" value="Print" onclick="fnPrint(this);" /> 
 

<br />
<div style="float:left"> 
<img src="css/images/nuc_logo1.jpg"  alt="" />
</div>
<div align=center>	 
<h1>
Nucleus Software Solution Pte. Ltd.  
</h1>
 </div>
<center><h3> Request for Quotation</h3> </center>
<div id="vendor">
<u>Vendor Details</u>
<table >
<tr><td>Id:</td><td><div id="vendorid"></div></td></tr>
<tr><td>Name:</td><td> <div id="vendorname"></div></td></tr>
<tr><td>Email:</td><td><div id="vendoremail"></div></td></tr>
<tr><td>Address:</td><td><div id="vendoraddress"></div></td></tr>
</table>
</div>
<hr />
A quotation is required for the following items  
<div id="quotation">
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

<hr />
<div id="nucleus"  align="center">
<address>
Nucleus Software Solution Pte. Ltd. <br />
300, Tampines Juncation,
#05-05, Tampines Avenue 5,
Singapore -529653,<br />
Tel:  +65-67859024
</address>
</div>  


</body>
</html>