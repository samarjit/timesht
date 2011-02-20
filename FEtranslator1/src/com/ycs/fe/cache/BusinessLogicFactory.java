package com.ycs.fe.cache;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

import map.ScreenMapRepo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.ycs.fe.businesslogic.BaseBL;

public class BusinessLogicFactory {
	private Log logger = LogFactory.getLog(BusinessLogicFactory.class);
	private  HashMap<String,BaseBL> blcache = new HashMap<String,BaseBL>();
	
	public void createBL(String mappingxmlpath)  {
		 InputStream mappingxml = ScreenMapRepo.class.getResourceAsStream("/"+mappingxmlpath);
		 Document mapdoc;
		try {
			mapdoc = new SAXReader().read(mappingxml);
		 Element screenElm = (Element) mapdoc.selectSingleNode("/root/screen");
		 String screenName = screenElm.attributeValue("name");
		 Element e = (Element) screenElm.selectSingleNode("/callbackclass");
		 logger.debug("creating Business Logic for "+screenName);
		 if(e != null){
			 System.out.println(e.getText());
			 String classname = e.getTextTrim();
			 BaseBL  baseBL = (BaseBL) Class.forName(classname).newInstance();
			 blcache.put(screenName, baseBL);
		 }
		} catch (DocumentException e1) {
			e1.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public BaseBL getBusinessLogic(String screenName){
		if(! blcache.containsKey(screenName) )refreshCache();
		 
		return blcache.get(screenName);
		 
	}

	private void refreshCache() {
		logger.debug("refreshing cache: It should not come here: Implementation TODO");
	}

	public void createBLCache(Element root) {
		String path;
		List<Element> n =  root.selectNodes("screen");
		
		if(n != null){
			 for (Element elm : n) {
				 path = elm.attributeValue("mappingxml");
				logger.debug("scrren clCache"+path);
				 createBL(path);
			}
		}
	}
 
}
