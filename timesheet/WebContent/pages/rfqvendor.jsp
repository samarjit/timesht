<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags" %>    
Vendor List: 
<select name="rfqvendorlist" id="rfqvendorlist">
	<option>vendor 1</option>
	<option>vendor 2</option>
</select>
Type of Notification
<select id="typenotify" name="typenotify">
<option value="E~0~X~0">Email</option>
	<option value="X~0~P~0">Print</option>
	<option value="E~0~P~0">Email and Print</option>
</select>

Suggested Delivery Time
<input type="text" name="suggestdelvtime" id="suggestdelvtime" /> 
<button onclick="insertVendor();" id="btnAdd">Add</button>
<br /> 
<s:property value="vendorList" />
<br />
<div id="vendorlist">
</div> <!-- close vendorlist -->