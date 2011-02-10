package actionclass;

import java.io.InputStream;
import java.io.StringBufferInputStream;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.opensymphony.xwork2.ActionSupport;
import com.ycs.fe.crud.UpdateData;

public class ProgramSetup extends ActionSupport {
private Logger logger = Logger.getLogger(getClass());
	/**
	 * 
	 */
	private static final long serialVersionUID = -552896450198022478L;

 
	private String submitdata;
	private String desc;
	private InputStream inputStream;
	
	public InputStream getInputStream() {
		return inputStream;
	}

	public String view() throws Exception {
		 
		return "view";
	}
	
	public String save() throws Exception {
		 
		String resultHtml = "";
		String formname = "";
		
		logger.debug(submitdata);
		JSONObject jobj1 = new JSONObject(submitdata);
		
		for (String name : JSONObject.getNames(jobj1)) {
			formname = name; 
			logger.debug(name);
			JSONArray jobj = jobj1.getJSONArray(formname);
			Gson gson = new Gson();
			JsonElement jelm = gson.toJsonTree(submitdata);
			
			for (int i = 0; i < jobj.length(); i++) {
				
				if(jobj.getJSONObject(i).getString("txtstatus").equalsIgnoreCase("Modify")){
				 
						logger.debug("Going to Modify Block");
						UpdateData upd = new UpdateData();
						 upd.update(formname,jobj.getJSONObject(i));
				}	 
				if(jobj.getJSONObject(i).getString("txtstatus").equalsIgnoreCase("new")){
					
				}
				if(jobj.getJSONObject(i).getString("txtstatus").equalsIgnoreCase("close")){
					
				}
					
				
			}
			
		} 
		
		
		inputStream = new StringBufferInputStream(resultHtml );
		return "saveajax";
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

	

	
}
