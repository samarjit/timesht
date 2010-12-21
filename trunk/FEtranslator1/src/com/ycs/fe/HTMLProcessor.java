package com.ycs.fe;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
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

import nu.xom.Builder;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.opensymphony.xwork2.ActionInvocation;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.NodeType;
 

 

/**
 * @author Samarjit
 * possible XSD starting point any valid element {@link http://www.stylusstudio.com/w3c/schema0/any.htm } for maximum flexibility
 */
public class HTMLProcessor {
	private Logger logger = Logger.getLogger(this.getClass());
	
	private boolean templateprocessed = false;
	
	public boolean getLastResult(){
		return templateprocessed;
	}
	
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
			dbf.setIgnoringElementContentWhitespace(true); 
			dbuild = dbf.newDocumentBuilder();
			InputStream reader = new ByteArrayInputStream(inputXML.getBytes());
			logger.debug("HTMLProcessor: before input parsing");
			Document docXML = dbuild.parse(reader);
			logger.debug("HTMLProcessor: after input parsing");
			Element xmlelmNode = docXML.getDocumentElement();
			
			String pathhtml = null;
			if(xmlelmNode.getElementsByTagName("htmltempalte").getLength() > 0){
				pathhtml = xmlelmNode.getElementsByTagName("htmltempalte").item(0).getTextContent().trim();
			}else{
				logger.debug("HTMLProcessor: no template found in "+inputXML.substring(0,20)+"..." ); 
				templateprocessed= false;
				return "HTMLProcessor: no template found in "+inputXML.substring(0,20)+"...";
			}
			XPath  xp = XPathFactory.newInstance().newXPath(); 
		 
		

			
			logger.debug("HTMLProcessor: TemplatePath="+ServletActionContext.getServletContext().getRealPath("/"+pathhtml));
			pathhtml = ServletActionContext.getServletContext().getRealPath("/"+pathhtml);
			if(new File(pathhtml).exists())logger.debug("The html exists");
			FileInputStream fin = new FileInputStream(new File(pathhtml));
			//InputStream is = loader.getResourceAsStream("log4j.properties");
			logger.debug("HTMLProcessor: before html parsing");
			Document dochtml = dbuild.parse(fin);
			logger.debug("HTMLProcessor: after html parsing");
			logger.debug("HTMLProcessor: dochtml num childs:"+dochtml.getChildNodes().getLength());
			
			NodeList nl = (NodeList) xp.evaluate("//fields/field/input", xmlelmNode, XPathConstants.NODESET);
			for (int i = 0; i < nl.getLength(); i++) {
				Element inputElm = (Element) nl.item(i);
				logger.debug("here.. inside //field"+inputElm.getNodeName());
			    logger.debug(" .. found input type = ..");
			    String htmlid = inputElm.getAttribute("forid");
				Element n = (Element) xp.evaluate("//*[@id=\""+htmlid+"\"]", dochtml, XPathConstants.NODE);
				logger.debug("setting values forid:"+"//*[@id=\""+htmlid+"\"]");
				if(n != null){
					n.setAttribute("value", inputElm.getAttribute("value"));
				}else{
					//TODO: We need to insert in custom fields
				}
			}
			
			nl = (NodeList) xp.evaluate("//fields/field/customfield", xmlelmNode, XPathConstants.NODESET);
			for (int i = 0; i < nl.getLength(); i++) {
				Element inputElm = (Element) nl.item(i);
				String htmlid = inputElm.getAttribute("forid");
				Element n = (Element) xp.evaluate("//*[@id=\""+htmlid+"\"]", dochtml, XPathConstants.NODE);
				logger.debug("setting values forid:"+"//*[@id=\""+htmlid+"\"]");
				if(n != null){
					NodeList textnodenl = inputElm.getElementsByTagName("text");
					Element textelement = null;
					if(textnodenl != null){
						textelement = (Element) textnodenl.item(0);
					}
					CDATASection cdata = dochtml.createCDATASection(textelement.getTextContent());
					//element.appendChild(cdata);
					appendXmlFragment(dbuild,n,textelement.getChildNodes());
				}else{
					//TODO: We need to insert in custom fields
				}
				
			}
			String scripts = (String) xp.evaluate("//scripts/text()", xmlelmNode, XPathConstants.STRING);
				NodeList headnl = dochtml.getElementsByTagName("head");
				if(headnl.getLength() < 1){
					Element elm = dochtml.createElement("head");
					//TODO: append head
				}else{
					Element headelm = (Element) headnl.item(0);
					appendXmlFragment(dbuild,headelm,scripts);
				}
				 
			 			
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			//initialize StreamResult with File object to save to file
			StreamResult result = new StreamResult(new StringWriter());
			DOMSource source = new DOMSource(dochtml );

			transformer.transform(source, result);

			
			xmlString = result.getWriter().toString();
			templateprocessed = true;
			
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
	public   void appendXmlFragment(DocumentBuilder docBuilder, Node parent, NodeList fragment) throws IOException, SAXException {
		logger.debug("still coming here");
		Document doc = parent.getOwnerDocument();
//		Node fragmentNode = docBuilder.parse(new InputSource(new StringReader("<root>"+fragment+"</root>"))).getDocumentElement();
		NodeList nl = fragment;
		if(fragment.item(0).getNodeType() == Node.CDATA_SECTION_NODE){
			appendXmlFragment( docBuilder,  parent,  fragment.item(0).getTextContent());
			return;
		}
		for (int i = 0; i < nl.getLength(); i++) {
			Node n = nl.item(i);			
				Node node = doc.importNode(n, true);
				parent.appendChild(node);
		}
	}
	public   void appendXmlFragment(DocumentBuilder docBuilder, Node parent, String fragment) throws IOException, SAXException {
		logger.debug("still coming here");
		Document doc = parent.getOwnerDocument();
		Node fragmentNode = docBuilder.parse(new InputSource(new StringReader("<root>"+fragment+"</root>"))).getDocumentElement();
		NodeList nl = fragmentNode.getChildNodes();
		for (int i = 0; i < nl.getLength(); i++) {
			Node n = nl.item(i);			
				Node node = doc.importNode(n, true);
				parent.appendChild(node);
		}
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
