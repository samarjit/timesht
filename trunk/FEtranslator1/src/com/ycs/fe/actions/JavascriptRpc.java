package com.ycs.fe.actions;

import java.io.InputStream;
import java.io.StringBufferInputStream;
import java.util.Iterator;
import java.util.Set;

import map.ScreenMapRepo;
import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import android.database.DataSetObserver;

import com.google.gson.Gson;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.util.ValueStack;
import com.ycs.fe.businesslogic.BaseBL;
import com.ycs.fe.cache.BusinessLogicFactory;
import com.ycs.fe.crud.JsrpcPojo;
import com.ycs.fe.dto.ResultDTO;


public class JavascriptRpc extends ActionSupport {
	private Logger logger = Logger.getLogger(JavascriptRpc.class);
	
	private static final long serialVersionUID = 1L;
	private String command;
	private String screenName;
	private InputStream inputStream;
	private String panelName;
	private String submitdata = "{}";
 
	
	public JavascriptRpc() {
		super();
		screenName="ProgramSetup";
		panelName="form1";
		command="selectonload";
	}
	
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
	 * @return
	 */
	public ResultDTO commandProcessor( JSON submitdataObj){
		JsrpcPojo rpc = new JsrpcPojo();
		Element rootXml = ScreenMapRepo.findMapXMLRoot(screenName);
		
		ResultDTO resDTO = null;
		
			
		    @SuppressWarnings("unchecked")
			Set<String>  itr =  ( (JSONObject) submitdataObj).keySet();
		    for (String dataSetkey : itr) {
		    	JSONArray dataSetJobj = ((JSONObject) submitdataObj).getJSONArray(dataSetkey);
		    	for (Object jsonPart : dataSetJobj) {
		    		String cmd = ((JSONObject) jsonPart).getString("command");
		    		Element elmCmd = (Element) rootXml.selectSingleNode("//commands/cmd[@name='"+command+"' and @instack='"+dataSetkey+"'] ");
		    		System.out.println("//commands/cmd[@name='"+command+"' and @instack='"+cmd+"'] ");
		    		String instack = elmCmd.attributeValue("instack");
		    		String operation = elmCmd.attributeValue("opt");
		    		String[] opts = operation.split("\\|");
		    		for (String opt : opts) {
		    			String[] sqlcmd = opt.split("\\:");
		    			String querynode =  sqlcmd[0]+"[@id='"+sqlcmd[1]+"']";
		    		resDTO = rpc.selectData(  screenName,   panelName, querynode ,   (JSONObject)jsonPart);
		    		}
		    	}
			}
		 
		return resDTO;
	}
	
	@Action(value="jsrpc",params={"configxml","ProductSetup.xml"},
			results={@Result(name="success",type="stream",params={"contentType","text/html","inputName","inputStream","resultxml","ProductSetup.xml"})}
//	results={@Result(name="success",location="/test.jsp")}
	)
	public String execute(){
		System.out.println("js RPC called with command:"+command+" for screen:"+screenName);
		BaseBL bl = BusinessLogicFactory.getBusinessLogic(screenName);
		bl.preJsRPCListerner(ActionContext.getContext().getActionInvocation());
		
		String path = ScreenMapRepo.findMapXML(screenName);
		String parsedquery = "";
		ResultDTO resDTO = new ResultDTO();
		
		ValueStack stack = ActionContext.getContext().getValueStack();
		try {
			 logger.debug(path);
				Document doc = new SAXReader().read(path);
				Element root = doc.getRootElement();
				 
				logger.debug("JsonRPC with submitdata="+submitdata);
				JSON submitdataObj = JSONObject.fromObject(submitdata);
				
				
				resDTO = commandProcessor (  submitdataObj);  
					
					
				 
				  
			 
		} catch (DocumentException e) {
			resDTO.addError("ERROR:"+e);
			e.printStackTrace();
		} catch (Exception e) {
			resDTO.addError("ERROR:"+e);
			e.printStackTrace();
		}
		
				
		logger.debug(stack.getContext().get("resultDTO"));
		
		bl.postJsRPCListerner(ActionContext.getContext().getActionInvocation());
		
		Gson gson = new Gson();
		String json1 = gson.toJson(stack.getContext().get("resultDTO"));
		logger.debug("Gson result(not sent back to client):"+json1);
//		setResultDTO((ResultDTO)stack.getContext().get("resultDTO"));
		
//		try {
//			OgnlContext context = new OgnlContext();
//			Object expression = Ognl.parseExpression("resultDTO.data.form1[0].txtnewprogname");
//			logger.debug(Ognl.getValue(expression,stack.getContext()));
			logger.debug(stack.findString("#resultDTO.data.form1[0].countryofissue" ));
//		} catch (OgnlException e1) {
//			e1.printStackTrace();
//		}
		
		JSONObject jobj = JSONObject.fromObject(resDTO);
		try {
			jobj.put("data",resDTO.getData());
			jobj.put("pagination",resDTO.getPagination());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String json = jobj.toString();
		logger.debug("Sent back to client:"+json);
		inputStream = new StringBufferInputStream(json);
		 
		return "success";
	}
	public static void main(String[] args) {
	}
	
	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}
	
	public InputStream getInputStream() {
		return inputStream;
	}
	public void setCommand(String command) {
		this.command = command;
	}
	public String getCommand() {
		return command;
	}
	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}
	public String getScreenName() {
		return screenName;
	}
	public void setPanelName(String panelName) {
		this.panelName = panelName;
	}
	public String getPanelName() {
		return panelName;
	}
	public void setSubmitdata(String submitdata) {
		this.submitdata = submitdata;
	}
	public String getSubmitdata() {
		return submitdata;
	}
 
	
	
}
