package com.ycs.fe.crud;

import java.io.File;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.json.JSONObject;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;

public class UpdateData {
private Logger logger = Logger.getLogger(getClass()); 
	public String update(String panelname, JSONObject jsonObject) {
		ActionInvocation invocation = ActionContext.getContext().getActionInvocation();
		 
		String pageconfigxml =  invocation.getProxy().getConfig().getParams().get("pageconfigxml");
		logger.debug("somemap2");
 
			String tplpath = ServletActionContext.getServletContext().getRealPath("WEB-INF/classes/map");
			String xmlFileName = pageconfigxml;
			
			try {
				org.dom4j.Document document1 = new SAXReader().read(new File(tplpath+"/"+xmlFileName));
				org.dom4j.Element root = document1.getRootElement();
				Node node = root.selectSingleNode("//panel[@id='"+panelname+"']/sqlupdate");
				String updatequery = "";
				updatequery += node.getText();
				
				List<Element> nodeList = root.selectNodes("//"+panelname+"/field/*");
				for (Element nodeelm : nodeList) {
					String col = nodeelm.attributeValue("column");
					String fldname = nodeelm.attributeValue("name");
					updatequery += " "+col+"="+jsonObject.getString(fldname);
				}
				
				String updatewhere = root.selectSingleNode("//panel[@id='"+panelname+"']/sqlwhere").getText();
				 Pattern pattern = Pattern.compile("\\{.*?\\}",Pattern.DOTALL);
				String finalupdatewhere = "";
				 Matcher m = pattern.matcher(updatewhere); // get a matcher object
			       int count = 0;
			       int end = 0;
			       while(m.find()) {
			          
			          String prop =  m.group();
			          logger.debug(prop + m.start() + " "+m.end());
			          if(prop.indexOf(".") >-1){
			        	  if(prop.substring(1, prop.indexOf(".")).equals(panelname)){
			        		  //fill with present panel row object
			        		  String propname = prop.substring( prop.indexOf(".")+1,prop.length() -1);
			        		  String propval = jsonObject.getString(propname);
			        		  finalupdatewhere += updatewhere.substring(end,m.start());//
			        		  finalupdatewhere += propval;
			        		  end = m.end(); 
			        		  logger.debug(propname);
			        	  }else{
			        		  logger.debug("does it come here"+prop.substring(0, prop.indexOf("."))+"#"+panelname);
			        		  //TODO: implement for object filling from related panels.
			        	  }
			          }else{ //fill with present panel row object
			        	  String propval = jsonObject.getString(prop);
		        		  finalupdatewhere += updatewhere.substring(end,m.start());//
		        		  finalupdatewhere += propval;
		        		  end = m.end(); 
		        		  logger.debug(prop);
			          }
			          count++;
			       }
			       finalupdatewhere += updatewhere.substring(end);
			       
			       if(finalupdatewhere.length() > 0) 
			    	   updatequery += " where "+finalupdatewhere;
			       
			       
			       logger.debug("UPDATE query:"+updatequery);
			}catch(Exception e){
				logger.debug("Exception caught in UpdateData",e);
			}
		return null;
	}

}
