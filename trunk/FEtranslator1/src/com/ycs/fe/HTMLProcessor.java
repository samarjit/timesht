package com.ycs.fe;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.struts2.ServletActionContext;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.opensymphony.xwork2.ActionInvocation;
 

 

public class HTMLProcessor {
	
	/**
	 * This method converts logical screen which is pre-processed from struts by filling in some dynamic contents
	 *  to physical HTML
	 * @return 
	 */
	public String process(String inputXML, ActionInvocation invocation){
		ClassLoader loader = this.getClass().getClassLoader();	
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder dbuild;
		String xmlString ="";
		try {
			dbuild = dbf.newDocumentBuilder();
			InputStream reader = new ByteArrayInputStream(inputXML.getBytes());
			System.out.println("HTMLProcessor: before input parsing");
			Document doc = dbuild.parse(reader);
			System.out.println("HTMLProcessor: after input parsing");
			Element xmlelmNode = doc.getDocumentElement();
			
			String pathhtml = null;
			if(xmlelmNode.getElementsByTagName("htmltempalte").getLength() > 0){
				pathhtml = xmlelmNode.getElementsByTagName("htmltempalte").item(0).getTextContent().trim();
			}else{
				System.out.println("HTMLProcessor: no template found in "+inputXML.substring(0,20)+"..." ); 
				return "HTMLProcessor: no template found in "+inputXML.substring(0,20)+"...";
			}
			XPath  xp = XPathFactory.newInstance().newXPath(); 
		 
		

			
			System.out.println("HTMLProcessor: TemplatePath="+ServletActionContext.getServletContext().getRealPath("/"+pathhtml));
			pathhtml = ServletActionContext.getServletContext().getRealPath("/"+pathhtml);
			if(new File(pathhtml).exists())System.out.println("The html exists");
			FileInputStream fin = new FileInputStream(new File(pathhtml));
			//InputStream is = loader.getResourceAsStream("log4j.properties");
			System.out.println("HTMLProcessor: before html parsing");
			Document dochtml = dbuild.parse(fin);
			System.out.println("HTMLProcessor: after html parsing");
			System.out.println("HTMLProcessor: dochtml num childs:"+dochtml.getChildNodes().getLength());
			
			NodeList nl = (NodeList) xp.evaluate("//fields/field", xmlelmNode, XPathConstants.NODESET);
			for (int i = 0; i < nl.getLength(); i++) {
				Element elmField = (Element) nl.item(i);
				System.out.println("here.. inside //field"+elmField.getChildNodes().item(1).getNodeName());
				if("input".equals(elmField.getChildNodes().item(1).getNodeName())){
					System.out.println(" .. found input type = ..");
					Element inputElm = (Element) elmField.getChildNodes().item(1);
					String htmlid = inputElm.getAttribute("forid");
					Element n = (Element) xp.evaluate("//*[@id=\""+htmlid+"\"]", dochtml, XPathConstants.NODE);
					System.out.println("setting values forid:"+"//*[@id=\""+htmlid+"\"]");
					n.setAttribute("value", inputElm.getAttribute("value"));
				}
			}
			
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			//initialize StreamResult with File object to save to file
			StreamResult result = new StreamResult(new StringWriter());
			DOMSource source = new DOMSource(dochtml );

			transformer.transform(source, result);

			
			xmlString = result.getWriter().toString();
			
			
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerFactoryConfigurationError e) {			
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return xmlString;
		
	}
	
	private String fileReadAll(String filename){
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
	
	public static void main(String[] args) throws Exception {
		/* Create and adjust the configuration */
		System.out.println("Running template engine");
		Configuration cfg = new Configuration();
		cfg.setDirectoryForTemplateLoading(new File("map/loginmap.xml"));
		// cfg.setObjectWrapper(new DefaultObjectWrapper());
		Map root = new HashMap();
		/* ------------------------------------------------------------------- */
		/* You usually do these for many times in the application life-cycle: */

		/* Get or create a template */
		TemplateEngine temp = cfg.getTemplate("WebContent/pages/logintpl.html");

		Writer out = new OutputStreamWriter(System.out);
		temp.process(root, out);
		out.flush();
		 HTMLProcessor htmp = new HTMLProcessor();
		 htmp.process(htmp.fileReadAll("C:/Eclipse/workspace1/FEtranslator1/src/actionclass/sampleoutput.xml"), null);
	}
}
