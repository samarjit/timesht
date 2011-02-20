package com.ycs.fe.cache;

import java.io.InputStream;
import java.util.List;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class ApplicationCache {
	private static Logger logger= Logger.getLogger(ApplicationCache.class);
	public void initCache(){
		String path = null;
		//String tplpath = ServletActionContext.getServletContext().getRealPath("WEB-INF/classes/map"); 
		InputStream scrxml = ApplicationCache.class.getResourceAsStream("/map/screenmap.xml");
		System.out.println("InitCache");
		Document doc;
		BusinessLogicFactory blf = new BusinessLogicFactory();
		try {
		
			doc = new SAXReader().read(scrxml);
			Element root = doc.getRootElement();
			blf.createBLCache(root);
			
			
			 
			 
			
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new ApplicationCache().initCache();
	}

}
