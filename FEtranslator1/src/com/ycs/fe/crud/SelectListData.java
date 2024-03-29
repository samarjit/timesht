package com.ycs.fe.crud;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import net.sf.json.JSONObject;

import com.opensymphony.xwork2.ActionContext;
import com.ycs.fe.dto.PrepstmtDTO;
import com.ycs.fe.dto.PrepstmtDTO.DataType;
import com.ycs.fe.dto.PrepstmtDTOArray;
import com.ycs.fe.util.ScreenMapRepo;

public class SelectListData {
private Logger logger = Logger.getLogger(getClass()); 
	public String selectList(String screenName, String panelname, JSONObject jsonObject) {
		 return selectList(screenName, panelname,"sqlselect", jsonObject);
	}
	public String selectList(String screenName, String panelname,String querynode, JSONObject jsonObject) {
		 	String xmlconfigfile =  ScreenMapRepo.findMapXML(screenName);
			String parsedquery = "";
			try {
				org.dom4j.Document document1 = new SAXReader().read(xmlconfigfile);
				org.dom4j.Element root = document1.getRootElement();
				Node crudnode = root.selectSingleNode("//crud");
				Node node = crudnode.selectSingleNode(querynode);
				if(node == null)throw new Exception("<"+querynode+"> node not defined");
				
				String outstack = ((Element) node).attributeValue("outstack"); 
				panelname = outstack;
				
				String updatequery = "";
				updatequery += node.getText();
				
				List<Element> nodeList = crudnode.selectNodes("../fields/field/*");
				logger.debug("fields size:"+nodeList.size());
				HashMap<String, DataType> hmfielddbtype = new HashMap<String, PrepstmtDTO.DataType>();
				QueryParser.populateFieldDBType(nodeList, hmfielddbtype);
				
				/*Pattern pattern  = Pattern.compile(":(\\w*)",Pattern.DOTALL|Pattern.MULTILINE);
				Matcher m = pattern.matcher(updatequery);
				while(m.find()){
					String val = "";
					logger.debug(m.group(0)+ " "+ m.group(1));
					if(jsonObject.has(m.group(1))){
						val = jsonObject.getString(m.group(1));
						updatequery = updatequery.replaceAll(":"+m.group(1), val);
					}
				}*/
				//SET
				List<Element> primarykeys = crudnode.selectNodes("../fields/field/*[@primarykey]");
				
				PrepstmtDTOArray  arparam = new PrepstmtDTOArray();
				parsedquery = QueryParser.parseQuery(updatequery, panelname, jsonObject, arparam, hmfielddbtype);
			       
			       logger.debug("UPDATE query:"+parsedquery+"\n Expanded prep:"+arparam.toString(parsedquery));
			}catch(Exception e){
				logger.debug("Exception caught in InsertData",e);
			}
		return parsedquery;
	}

}
