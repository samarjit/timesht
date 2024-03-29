package com.ycs.fe.crud;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.util.ValueStack;
import com.ycs.fe.dao.FETranslatorDAO;
import com.ycs.fe.dto.PrepstmtDTO;
import com.ycs.fe.dto.PrepstmtDTO.DataType;
import com.ycs.fe.dto.PrepstmtDTOArray;
import com.ycs.fe.dto.ResultDTO;
import com.ycs.fe.util.ScreenMapRepo;

public class SelectData {
private Logger logger = Logger.getLogger(getClass()); 
	public ResultDTO selectData(String screenName, String panelname,  JSONObject jsonObject) {
		return selectData(screenName, panelname,"sqlselect", jsonObject);
	}
	
	public ResultDTO selectData(String screenName, String panelname,String querynode, JSONObject jsonObject) {
		 
		 
		    String pageconfigxml =  ScreenMapRepo.findMapXML(screenName);
			String tplpath = ServletActionContext.getServletContext().getRealPath("WEB-INF/classes/map");
			String parsedquery = "";
			ResultDTO resultDTO = new ResultDTO();
			try {
				org.dom4j.Document document1 = new SAXReader().read(pageconfigxml);
				org.dom4j.Element root = document1.getRootElement();
				Node crudnode = root.selectSingleNode("//crud");
				Node queryNode = crudnode.selectSingleNode(querynode);
				if(queryNode == null)throw new Exception("<"+querynode+"> node not defined");
				
				String outstack = ((Element) queryNode).attributeValue("outstack"); 
				 
				
				String updatequery = "";
				updatequery += queryNode.getText();
				
				Element errorNode = (Element) queryNode.selectSingleNode("error");
				String errorTemplate = "";
				if(errorNode !=null)errorTemplate=errorNode.attributeValue("message");
				
				Element messageNode = (Element) queryNode.selectSingleNode("message");
				String messageTemplate = "";
				if(messageNode !=null)messageTemplate=messageNode.attributeValue("message");
				
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
				
				FETranslatorDAO fetranslatorDAO = new FETranslatorDAO();
				//pagination
				Element countqrynode = (Element)crudnode.selectSingleNode("countquery");
				if(countqrynode != null){
					String strpagesize = countqrynode.attributeValue("pagesize");
					int pagesize = 0;
					if(strpagesize != null ){
						pagesize = Integer.parseInt(strpagesize);
					}
					String countquery = countqrynode.getText();
					if(countquery != null){
						PrepstmtDTOArray  arparam = new PrepstmtDTOArray();
						parsedquery = QueryParser.parseQuery(updatequery, outstack, jsonObject, arparam, hmfielddbtype);
						int reccount = fetranslatorDAO.executeCountQry(screenName, parsedquery, outstack, arparam);
						
						if(reccount > pagesize){
							JSONObject jsonobject = jsonObject.getJSONObject("pagination");
							int pageno = 0;
							JSONObject	panel = jsonobject.getJSONObject(outstack);
							pageno =  panel.getInt("currentpage");
							int pagecount = (int) Math.ceil((double)reccount / pagesize); 
							 
							//pagination:{form1:{currentpage:1,pagecount:200}} 
							ValueStack stack = ActionContext.getContext().getValueStack();
							ResultDTO tempresDTO = (ResultDTO) stack.getContext().get("resultDTO");
							if(tempresDTO == null){
								tempresDTO = new ResultDTO();
							}
							tempresDTO.setPageDetails(outstack, pageno, pagecount, pagesize);
							stack.getContext().put("resultDTO",tempresDTO);
							
							int recfrom = pageno * pagesize;
							int recto = recfrom + pagesize;
							jsonObject.put("recto", recto); //put into current row value the recfrom and recto so that it can be used in count query
							jsonObject.put("recfrom", recfrom);
							hmfielddbtype.put("recto",PrepstmtDTO.getDataTypeFrmStr("INT") );
							hmfielddbtype.put("recfrom",PrepstmtDTO.getDataTypeFrmStr("INT"));
						}
					}
				}
				//pagination end
				PrepstmtDTOArray  arparam = new PrepstmtDTOArray();
				parsedquery = QueryParser.parseQuery(updatequery, outstack, jsonObject, arparam, hmfielddbtype);
			       
			       logger.debug("INSERT query:"+parsedquery+"\n Expanded prep:"+arparam.toString(parsedquery));
			       
			       resultDTO = fetranslatorDAO.executecrud(screenName, parsedquery, outstack,jsonObject, arparam, errorTemplate, messageTemplate);
			       
			}catch(Exception e){
				logger.debug("Exception caught in InsertData",e);
			}
		return resultDTO;
	}

}
