package com.ycs.fe.crud;

import java.util.Set;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.dom4j.Element;

import com.ycs.fe.commandprocessor.BaseCommandProcessor;
import com.ycs.fe.commandprocessor.CommandProcessorResolver;
import com.ycs.fe.dto.InputDTO;
import com.ycs.fe.dto.ResultDTO;
import com.ycs.fe.util.ScreenMapRepo;

public class CommandProcessor {

	private static Logger logger  = Logger.getLogger(CommandProcessor.class);

	/**
	 * command="jrpcCmd1" should be present in each record see in submitdata data structure
	 * 
	 * submitdata={"form1":[{"row":0,"programname":"LOYCARD","txtnewprogname":"LOYCARD","txtprogramdesc":"Loyalty Card Program",
	 * "issuername":"HSBC Bank","countryofissue":"SINGAPORE","txtstatus":"Modify",command:"jrpcCmd1"},{"row":1,"programname":"TRACARD",
	 * "txtnewprogname":"TRACARD","txtprogramdesc":"Travel Card Program","issuername":"HSBC Bank","countryofissue":"SINGAPORE",
	 * "txtstatus":"Modify",command:"jrpcCmd1"}],txnrec:{single:,multiple:[{aaa:},{aaa:}]}}
	 * 
	 * @param command
	 * @param submitdataObj
	 * @param screenName 
	 * @param inputDTO 
	 * @return
	 */
	public ResultDTO commandProcessor( JSON submitdataObj, String screenName, InputDTO inputDTO){
//		JsrpcPojo rpc = new JsrpcPojo();
		Element rootXml = ScreenMapRepo.findMapXMLRoot(screenName);
		
		ResultDTO resDTO = null;
		
			
		    @SuppressWarnings("unchecked")
			Set<String>  itr =  ( (JSONObject) submitdataObj).keySet(); 
		    for (String dataSetkey : itr) { //form1, form2 ...
		    	JSONArray dataSetJobj = ((JSONObject) submitdataObj).getJSONArray(dataSetkey);
		    	for (Object jsonRecord : dataSetJobj) { //rows in dataset a Good place to insert DB Transaction
		    		String cmd = ((JSONObject) jsonRecord).getString("command");
		    		Element elmCmd = (Element) rootXml.selectSingleNode("//commands/cmd[@name='"+cmd+"' ] ");
		    		System.out.println("//commands/cmd[@name='"+cmd+"' ] ");
//		    		String instack = elmCmd.attributeValue("instack");
		    		String operation = elmCmd.attributeValue("opt");
		    		String strProcessor = elmCmd.attributeValue("processor");
		    		logger  .debug("Command Processor:"+strProcessor+" operation:"+operation);
		    		String[] opts = operation.split("\\|"); //get chained commands
		    		for (String opt : opts) {
		    			String[] sqlcmd = opt.split("\\:"); //get Id of query 
		    			String querynodeXpath =  sqlcmd[0]+"[@id='"+sqlcmd[1]+"']"; //Query node xpath
		    		    BaseCommandProcessor cmdProcessor =  CommandProcessorResolver.getCommandProcessor(strProcessor);
		    		    resDTO = cmdProcessor.processCommand(screenName, querynodeXpath, (JSONObject) jsonRecord, inputDTO, resDTO);				
		    		    //resDTO = rpc.selectData(  screenName,   null, querynodeXpath ,   (JSONObject)jsonRecord);
		    		}
		    	}
			}
		 
		return resDTO;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
