package com.ycs.fe;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import actionclass.XMLResult;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.config.entities.ResultConfig;
import com.ycs.fe.dao.FETranslatorDAO;

public abstract class HTMLProcessor {
	private Logger logger = Logger.getLogger(this.getClass());
	public HTMLProcessor() {
		super();
	}

	protected String fileReadAll(String filename) {
		String str ="";
		try {
			BufferedReader bfr = new BufferedReader(new FileReader(filename));
			String tmp = "";
			while((tmp=bfr.readLine())!= null){
				str +=tmp;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}

	 
	public void populateValueStack(ActionInvocation invocation, String resultCode) {
		ResultConfig resultConfig = invocation.getProxy().getConfig().getResults().get(resultCode);
		logger.debug("Result classname = "+resultConfig.getClassName()); 
		if(XMLResult.class.getName().equals(resultConfig.getClassName())){
			String tplpath = ServletActionContext.getServletContext().getRealPath("WEB-INF/classes/map");
			String xmlFileName = resultConfig.getParams().get("resultxml");
			
			try {
				org.dom4j.Document document1 = new SAXReader().read(new File(tplpath+"/"+xmlFileName));
				org.dom4j.Element root = document1.getRootElement();
				List nodeList = root.selectNodes("//query");
				logger.debug("query list size:"+nodeList.size());
				for (Iterator queryList = nodeList.iterator(); queryList.hasNext();) {
					org.dom4j.Node node = (org.dom4j.Node) queryList.next();
					logger.debug("Query Node:"+node.getText());
					String stackid = ((org.dom4j.Element) node).attributeValue("stackid");
					String type = ((org.dom4j.Element) node).attributeValue("type");
					String sqlquery = node.getText();
					FETranslatorDAO feDAO = new FETranslatorDAO();
					feDAO.executequery(sqlquery,stackid,type);
					org.dom4j.Element e = (org.dom4j.Element) node;
				
				}
			} catch (DocumentException e) {
				logger.debug("result xml file not readable --",e);
			}
		}
		
		
	}

	public abstract boolean getLastResult();

	public abstract void appendXmlFragment(DocumentBuilder docBuilder, Node parent, NodeList fragment) throws IOException, SAXException;

	public abstract void appendXmlFragment(DocumentBuilder docBuilder, Node parent, String fragment) throws IOException, SAXException;

	public abstract String process(String inputXML, ActionInvocation invocation);

}