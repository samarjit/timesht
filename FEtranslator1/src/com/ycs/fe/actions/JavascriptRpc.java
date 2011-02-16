package com.ycs.fe.actions;

import java.io.InputStream;
import java.io.StringBufferInputStream;

import map.ScreenMapRepo;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.util.ValueStack;
import com.ycs.fe.crud.JsrpcPojo;
import com.ycs.fe.dto.ResultDTO;


public class JavascriptRpc extends ActionSupport {
	private Logger logger = Logger.getLogger(JavascriptRpc.class);
	
	private static final long serialVersionUID = 1L;
	private String command;
	private String screenName;
	private InputStream inputStream;
	private String panelName;
	private String data = "{}";
	
	@Action(value="jsrpc",params={"configxml","ProductSetup.xml"},
			results={@Result(name="success",type="stream",params={"contentType","text/html","inputName","inputStream","resultxml","ProductSetup.xml"})}
	)
	public String execute(){
		System.out.println("js RPC called with command:"+command+" for screen:"+screenName);
		screenName="ProgramSetup";
		panelName="form1";
		 
		String path = ScreenMapRepo.findMapXML(screenName);
		String parsedquery = "";
		ResultDTO resDTO = new ResultDTO();
		
		ValueStack stack = ActionContext.getContext().getValueStack();
		try {
			 logger.debug(path);
				Document doc = new SAXReader().read(path);
				Element root = doc.getRootElement();
				 
				JSONObject jsonObject = new JSONObject(data);
				JsrpcPojo rpc = new JsrpcPojo();
				 resDTO = rpc.selectData(  screenName,   panelName,command,   jsonObject);
				  
			 
		} catch (DocumentException e) {
			resDTO.addError("ERROR:"+e);
			e.printStackTrace();
		} catch (JSONException e) {
			resDTO.addError("ERROR:"+e);
			e.printStackTrace();
		} catch (Exception e) {
			resDTO.addError("ERROR:"+e);
			e.printStackTrace();
		}
		
				
		logger.debug(stack.getContext().get("resultDTO"));
		 
//		Gson gson = new Gson();
//		String json = gson.toJson(stack.getContext().get("resultDTO"));
		 
		JSONObject jobj = new JSONObject(resDTO);
		try {
			jobj .put("data",resDTO.getData());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String json = jobj.toString();
		logger.debug(json);
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

	
}
