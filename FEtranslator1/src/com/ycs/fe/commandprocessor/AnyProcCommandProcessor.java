package com.ycs.fe.commandprocessor;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import com.ycs.fe.dto.InputDTO;
import com.ycs.fe.dto.ResultDTO;
import com.ycs.fe.util.ParseSentenceOgnl;
import com.ycs.fe.util.ScreenMapRepo;
import com.ycs.fe.util.SentenceParseException;
import com.ycs.fe.ws.SPCall;
import com.ycs.fe.ws.SPCallService;

public class AnyProcCommandProcessor implements BaseCommandProcessor {

	private static Logger logger = Logger.getLogger(AnyProcCommandProcessor.class);

	@Override
	public ResultDTO processCommand(String screenName, String querynodeXpath, JSONObject jsonRecord, InputDTO inputDTO, ResultDTO resultDTO) {
		logger.debug("Processing AnyProc call");
		HashMap<String, Object>  data = new HashMap<String, Object>();
		resultDTO = new ResultDTO();
//		 String resultJsonConf =
//		 "{'procname':'WS_TEST_PROC','inputparam':[[{'NAME':'sam','EMAIL':'sam@yl.com'},{'NAME':'samarjit','EMAIL':'samarjit@yl.com'}],{'data1':'param2'}],'outputparam':'param3'}";

		try {
			 String pageconfigxml =  ScreenMapRepo.findMapXML(screenName);
			 org.dom4j.Document document1 = new SAXReader().read(pageconfigxml);
			org.dom4j.Element root = document1.getRootElement();
			Node crudnode = root.selectSingleNode("//anyprocs");
			Node queryNode = crudnode.selectSingleNode(querynodeXpath);
			
			Element rootXml = ScreenMapRepo.findMapXMLRoot(screenName);
			Node selectSingleNode = rootXml.selectSingleNode(querynodeXpath);
			
			String jsonFromConf = queryNode.getText();
			System.out.println("JsonRecord in any proc call :"+jsonRecord);
			String resultJsonConf = ParseSentenceOgnl.parse(jsonFromConf, jsonRecord);
			JSONObject jsObj = JSONObject.fromObject(resultJsonConf);

			Document doc = DocumentFactory.getInstance().createDocument();
			Element rootElement = doc.addElement("root");
			Element procele = rootElement.addElement("procedure");

			Element pname = procele.addElement("procname");
			Element inputele = procele.addElement("inputparam");
			Element outputele = procele.addElement("outputparam");
			String outstack = procele.attributeValue("outstack");
			String procname = jsObj.getString("procname");
			pname.addText(procname.toUpperCase());

			// JSON js = JSONSerializer.toJSON(json);
			// System.out.println(js.isArray());
			JSONArray inputarr = jsObj.optJSONArray("inputparam");
			JSONObject inputObj = jsObj.optJSONObject("inputparam");

			inputParamParser(doc, inputele, inputarr, inputObj);

			JSONArray outputarr = jsObj.optJSONArray("outputparam");
			String outputString = jsObj.optString("outputparam");

			outputParamParser(outputele, outputarr, outputString);

			String xmlString = doc.asXML();
			String resXML = callProcedure(xmlString);
			data.put(outstack, resXML);
			resultDTO.setData(data);
			
		} catch (SentenceParseException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return resultDTO;
	}

	public String callProcedure(String xmlString) {
		
		System.out.println("InputData :" + xmlString);
		SPCallService spcallsvc = new SPCallService();
		SPCall spcallendpoint  = spcallsvc.getSPCallPort();
		String resXML = spcallendpoint.callSP(xmlString);
//		MainWithoutType callproc = new MainWithoutType();
//		String htmlString = callproc.executeGenericProc(xmlString);
//		System.out.println("htmlString=" + htmlString);
		return resXML;
	}

	private void outputParamParser(Element outputele, JSONArray outputarr, String outputString) {
		if (outputarr != null) {
			for (int i = 0; i < outputarr.size(); i++) {
				String output = outputarr.getString(i);
				Element outparamele = outputele.addElement("parameter");
				outparamele.addText(output);
			}
		} else {
			Element outparamele = outputele.addElement("parameter");
			outparamele.addText(outputString);
		}
	}

	private void inputParamParser(Document doc, Element inputele, JSONArray inputarr, JSONObject inputObj) {
		if (inputarr != null) {
			for (int i = 0; i < inputarr.size(); i++) {
				JSONArray paramarr = inputarr.optJSONArray(i);
				JSONObject paramObj = inputarr.optJSONObject(i);
				Element inparamele = inputele.addElement("parameter");
				if (paramarr != null) {
					arrayParamParser(doc, paramarr, inparamele);
				} else {
					objectParamParser(doc, paramObj, inparamele);
				}
			}
		} else {
			Element inparamele = inputele.addElement("parameter");
			objectParamParser(doc, inputObj, inparamele);
		}
	}

	private void objectParamParser(Document doc, JSONObject paramObj, Element paramele) throws JSONException {
		Set<String> dataSet = paramObj.keySet();
		Iterator<String> it = dataSet.iterator();
		if (dataSet.size() == 1) {
			String key = it.next();
			Element dataele = paramele.addElement("data1");
			String val = paramObj.getString(key);
			dataele.addText(val);
		} else {
			Element structele = paramele.addElement("STRUCT");
			while (it.hasNext()) {
				String key = it.next();
				Element dataele = structele.addElement("data1");
				String val = paramObj.getString(key);
				dataele.addText(val);
				dataele.addAttribute("name", key);
			}
		}
	}

	private void arrayParamParser(Document doc, JSONArray paramarr, Element paramele) throws JSONException {
		Element arrele = paramele.addElement("ARRAY");
		for (int j = 0; j < paramarr.size(); j++) {
			JSONObject dataObj = paramarr.optJSONObject(j);
			String dataStr = paramarr.optString(j);
			if (dataObj != null) {
				Element structele = arrele.addElement("STRUCT");
				Set<String> dataSet = dataObj.keySet();
				Iterator<String> it = dataSet.iterator();
				while (it.hasNext()) {
					String key = it.next();
					Element dataele = structele.addElement("data1");
					dataele.addAttribute("name", key);
					String val = dataObj.getString(key);
					dataele.addText(val);
				}
			} else {
				Element dataele = arrele.addElement("data1");
				dataele.addText(dataStr);
			}
		}
	}

	public static void main(String[] args) {

		String json = "{'procname':'WS_TEST_PROC','inputparam':[[{'name':'AAA','email':'aaa@f'},{'name':'AAA','email':'aaa@f'}],{'data1':'2'},['aaaa','bbbb','cccc'],{'name':'AAA','email':'aaa@f'}],'outputparam':['param3','param4']}";
		// json =
		// "{'procname':'WS_TEST_PROC','inputparam':{'data1'=3},'outputparam':'param3'}}";

		
		new AnyProcCommandProcessor().processCommand(null, null, null, null, null);

	}
}
