package com.ycs.fe.crud;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import map.ScreenMapRepo;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.json.JSONObject;

import com.opensymphony.xwork2.ActionContext;
import com.ycs.fe.dao.FETranslatorDAO;
import com.ycs.fe.dto.PrepstmtDTO;
import com.ycs.fe.dto.PrepstmtDTO.DataType;
import com.ycs.fe.dto.PrepstmtDTOArray;
import com.ycs.fe.dto.ResultDTO;

public class JsrpcPojo {
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
				Node crudnode = root.selectSingleNode("//panel[@id='"+panelname+"']/crud");
				Node node = crudnode.selectSingleNode(querynode);
				if(node == null)throw new Exception("<"+querynode+"> node not defined");
				
				String updatequery = "";
				updatequery += node.getText();
				
				List<Element> nodeList = crudnode.selectNodes("../fields/field/*");
				logger.debug("fields size:"+nodeList.size());
				HashMap<String, DataType> hmfielddbtype = new HashMap<String, PrepstmtDTO.DataType>();
				for (Element nodeelm : nodeList) {
					String col = nodeelm.attributeValue("column");
					String fldname = nodeelm.attributeValue("name");
					String dbtype = nodeelm.attributeValue("dbdatatype");
					
					if(fldname != null && !"".equals(fldname)){
						if(dbtype != null  ){
//							updatequery += " "+col+"= TO_DATE('"+jsonObject.getString(fldname)+"', 'DD/MM/YYYY')";
							hmfielddbtype.put(fldname, PrepstmtDTO.getDataTypeFrmStr(dbtype)  );
						}else{
//							updatequery += " "+col+"='"+jsonObject.getString(fldname)+"',";
							hmfielddbtype.put(fldname, PrepstmtDTO.getDataTypeFrmStr("STRING"));
						}
					}
				}
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
				
				
				//Where
//				String updatewhere = crudnode.selectSingleNode("sqlwhere").getText();
				Pattern   pattern = Pattern.compile("\\:(\\w*)\\[(\\d*)\\]?\\.?(\\w*)",Pattern.DOTALL|Pattern.MULTILINE);
				
				PrepstmtDTOArray  arparam = new PrepstmtDTOArray();
				
				
				Matcher m1 = pattern.matcher(updatequery); // get a matcher object
			       int count = 0;
			       int end = 0;
			       while(m1.find()) {
			          
			          String prop =  m1.group();
			          logger.debug("Start preparing:"+prop + m1.start() + " "+m1.end()+" "+m1.group(1)+" "+m1.group(2)+" "+m1.group(3));
			          if(! "".equals(m1.group(2))){
			        	  if(m1.group(1).equals(panelname)){ //:form[0].param
			        		  //fill with present panel row object
			        		  String propname = m1.group(3);
			        		  String propval = "";
			        		  if(jsonObject.has(propname)){
			        			  propval = jsonObject.getString(propname);
			        			  parsedquery += updatequery.substring(end,m1.start());//
			        			  
			        			  arparam.add(hmfielddbtype.get(propname),propval);
			        			   
			        			  
			        			  
			        			  parsedquery += "?";
			        		  }
			        		  end = m1.end(); 
			        		  logger.debug("with dot"+m1.group(2)+"."+propname);
			        	  }else{ //:formx[0].param
			        		  logger.debug("does it come here"+  m1.group(1));
			        		  //TODO: implement for object filling from related panels.
			        	  }
			          }else{ //fill with present panel row object :formxparam
			        	  String propval;
			        	  if(jsonObject.has(m1.group(1)) ){
			        		  String propname = m1.group(1);
			        		  propval = jsonObject.getString(m1.group(1));
			        		  parsedquery += updatequery.substring(end,m1.start());//
							 
		        				  arparam.add(hmfielddbtype.get(propname),propval);
		        			   
			        		  parsedquery += "?";
			        	  }
			        	  
		        		  end = m1.end(); 
		        		  logger.debug("else no dot"+m1.group(1));
			          }
			          count++;
			       }
			       parsedquery += updatequery.substring(end);
			       updatequery = parsedquery;
			       
			       logger.debug("INSERT query:"+parsedquery+"\n Expanded prep:"+arparam.toString(updatequery));
			       FETranslatorDAO fetranslatorDAO = new FETranslatorDAO();
			       resultDTO = fetranslatorDAO.executecrud(screenName, parsedquery, panelname, arparam);
			       
			}catch(Exception e){
				logger.debug("Exception caught in InsertData",e);
			}
		return resultDTO;
	}

}
