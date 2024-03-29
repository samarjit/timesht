package actionclass;

import java.io.File;
import java.io.InputStream;
import java.io.StringBufferInputStream;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.apache.log4j.Logger;
import net.sf.json.JSONArray;
import net.sf.json.JSONException;
 
import net.sf.json.JSONObject;

import repo.txnmap.generated.Root;
import repo.txnmap.generated.Txn;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.ycs.fe.crud.CommandProcessor;
import com.ycs.fe.crud.InsertData;
import com.ycs.fe.crud.UpdateData;
import com.ycs.fe.dto.InputDTO;
import com.ycs.fe.dto.ResultDTO;

public class ProgramSetup extends ActionSupport {
private Logger logger = Logger.getLogger(getClass());
	/**
	 * 
	 */
	private static final long serialVersionUID = -552896450198022478L;

 
	private String submitdata;
	private String desc;
	private InputStream inputStream;
	private String screenName;
	private Map<String, Object> session;
	private String submitdatatxncode; 
	
	public InputStream getInputStream() {
		return inputStream;
	}

	public String view() throws Exception {
		System.out.println("Program Setup view ****************"); 
		if(screenName == null || "".equals(screenName))
		screenName = "ProgramSetup";
//		ActionContext.getContext().getValueStack().getContext().put("ZHello", "World");
//		ActionContext.getContext().getValueStack().set("ZHello2", "World2");
		return "view";
	}
	
	public String save() throws Exception {
		 
		String resultHtml = "";
		String formname = "";
		ResultDTO result = new ResultDTO();
		logger.debug("submitdata:"+submitdata);
		JSONObject jobj1 =   JSONObject.fromObject(submitdata);
		InputDTO inputDTO = new InputDTO();
		inputDTO.setData((JSONObject) jobj1);
		ActionContext.getContext().getValueStack().getContext().put("inputDTO", inputDTO);
		
		
		try{
			CommandProcessor cmdpr = new CommandProcessor();
			ResultDTO resDTO = cmdpr.commandProcessor(jobj1, screenName, inputDTO); 
			JSONObject resObj = JSONObject.fromObject(resDTO);
			resultHtml = resObj.toString();
		}catch(Exception e){
			logger.debug("ActionClass Exception occured",e);
		}
		/*for (Iterator itr = jobj1.keys();itr.hasNext();) {
			String name = (String) itr.next();
			formname = name; 
			logger.debug(name);
			JSONArray jobj = jobj1.getJSONArray(formname);
			Gson gson = new Gson();
			JsonElement jelm = gson.toJsonTree(submitdata);
			
			for (int i = 0; i < jobj.size(); i++) {
				
				if(jobj.getJSONObject(i).getString("txtstatus").equalsIgnoreCase("Modify")){
				 
						logger.debug("Going to Modify Block");
						UpdateData upd = new UpdateData();
						result =	upd.update(screenName,formname,jobj.getJSONObject(i));
						if(result.getMessages().size() > 0)
							resultHtml += "Records("+result.getMessages().size()+") Modified Successfully<br/>";
						else
							resultHtml += "Request failed "+ result.getErrors().size()+"\n";
				}	 
				if(jobj.getJSONObject(i).getString("txtstatus").equalsIgnoreCase("new")){
						logger.debug("Going to Modify Block");
						InsertData ins = new InsertData();
						result = ins.insert(screenName,formname,jobj.getJSONObject(i));
						if(result.getMessages().size() > 0)
							resultHtml += "Records("+result.getMessages().size()+") Created Successfully<br/>";
						else
							resultHtml += "Request failed "+result.getErrors().size()+"\n";
				}
				if(jobj.getJSONObject(i).getString("txtstatus").equalsIgnoreCase("close")){
						logger.debug("Going to Modify Block");
						UpdateData upd = new UpdateData();
						result = upd.update(screenName,formname,jobj.getJSONObject(i));
						if(result.getMessages().size() > 0)
							resultHtml += "Records("+result.getMessages().size()+") Closed Successfully<br/>";
						else
							resultHtml += "Request failed "+result.getErrors().size()+"\n";
				}
					
				
			}
			
		}*/
		
		 
//		resultHtml = result;
		inputStream = new StringBufferInputStream(resultHtml );
		return "saveajax";
	}
	
	public String processTxn(){
		String resultHtml = null;
		session = ActionContext.getContext().getSession();
		mockLogin();
		
		String unique = new String();
		String application_name = (String) session.get("APPLICATION_NAME");
		String transcode = "CNUCNF"; //will be coming form command
		
		// creating a unique id.
		// unique id = transaction code.
		unique += (String) session.get("NET_ID");
		unique += "_" + System.currentTimeMillis();

		String xml = "<?xml version=\"1.0\"?>";
		xml += "<IDCT>";
		xml += "<TRANS_CODE>" + transcode + "</TRANS_CODE>";
		xml += "<IDCT_ID>" + application_name + "_" + unique
				+ "</IDCT_ID>";
		xml += "<DATETIME>" + new Date().toString() + "</DATETIME>";
		xml += "<NET_ID>" + (String) session.get("NET_ID")
				+ "</NET_ID>";
		xml += "<MESSAGE_VER_NO>1.0</MESSAGE_VER_NO>";
		xml += "<CHANNEL_ID>WEB</CHANNEL_ID>";
		xml += "<MESSAGE_DIGEST>NO_DATA</MESSAGE_DIGEST>";
		xml += "<IDCT_STATUS>NO_DATA</IDCT_STATUS>";
		xml += "<IDCT_ERR_CODE>NO_DATA</IDCT_ERR_CODE>";
		xml += "<IDCT_MESSAGE_TYPE>01</IDCT_MESSAGE_TYPE>";
		
		try {
			JSONObject jobj1 =   JSONObject.fromObject(submitdatatxncode);

			JSONObject txnrec = jobj1.getJSONObject("txnrec");
			JSONObject single = txnrec.getJSONObject("single");
			JSONArray multiple = txnrec.getJSONArray("multiple");

			final JAXBContext jc = JAXBContext.newInstance(Root.class);
			final Root root = (Root) jc.createUnmarshaller().unmarshal(
					new File("C:/Eclipse/workspace1/FEtranslator1/src/repo/txnmap/nrow_txnmap.xml"));
			
			for (Txn txn : root.getTxn()) {
				if (txn.getId().equals(transcode)) {
					String strReqSingle = txn.getReq().getSingle();
					if(strReqSingle !=null){
						String[] arSingle = strReqSingle.split(",");
						
					}
					String strReqMultiple = txn.getReq().getSingle();
					if(strReqMultiple !=null){
						String[] arMultiple = strReqMultiple.split(",");
						
					}
				}
			}
			
		} catch (JSONException e) {
			logger.debug("submitdata parsing error", e);
			e.printStackTrace();
		} catch (JAXBException e) {
			logger.debug("submitdata parsing error", e);
			e.printStackTrace();
		}
		
		
		inputStream = new StringBufferInputStream(resultHtml );
		return "saveajax";
	}
	public String callPLSQL(String xml){
		System.out.println(xml);
		return xml;
	}
	private void mockLogin() {
		 if(session == null){
			logger.debug("ERROR Session is null!!");
		 }
		 session.put("APPLICATION_NAME", "ICICI");
		 session.put("NET_ID", "Henry");
	}

	public void setSubmitdata(String submitdata) {
		this.submitdata = submitdata;
	}

	public String getSubmitdata() {
		return submitdata;
	}
	
	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getScreenName() {
		return screenName;
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}

	public void setSubmitdatatxncode(String submitdatatxncode) {
		this.submitdatatxncode = submitdatatxncode;
	}

	public String getSubmitdatatxncode() {
		return submitdatatxncode;
	}

	

	
}
