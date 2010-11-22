package workflow;

import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

import bsh.EvalError;
import bsh.Interpreter;
import businesslogic.BaseBL;

/**
 * This class is used to parse the xml file and read the data defined as workflow.
 *
 */
public class ScreenFlow {
	private static boolean initialized = false;
	private HashMap propertyset ;
	
	public ScreenFlow() {
		super();
		if(!initialized)init();
	}
	private static HashMap<String,String> workflowlocationcache = null; 
	
	private Document parserXML(String file) throws SAXException, IOException, ParserConfigurationException
	{
		return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file);
	}
 
	
	public HashMap getPropertyset() {
		return propertyset;
	}


	public void setPropertyset(HashMap propertyset) {
		this.propertyset = propertyset;
	}


/**This function parses the given file.
 * 
 */
private void init(){
	if(initialized)return;
	initialized = true;
	URL  st =  ScreenFlow.class.getResource("/scrflowxml/screenflowfactory.xml");
	Document doc = null;
	propertyset = new HashMap();
	workflowlocationcache = new HashMap<String, String>();
	try {
		doc = parserXML(st.getFile());
		
	 	NodeList nl = doc.getElementsByTagName("screenflow");
          for(int i = 0 ;i<nl.getLength();i++){
        	  Node node = nl.item(i);
        	  if(node.getNodeType() == Document.ELEMENT_NODE){
        		   Element elm = (Element) node;
        		   String key = elm.getAttribute("key");
        		   String val = elm.getAttribute("value");
        		   workflowlocationcache.put(key	, val);
        	  }
          }
  
	} catch (SAXException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	} catch (ParserConfigurationException e) {
		e.printStackTrace();
	}
}

/**
 * This function gets the next available action from the workflow
 * @param scrFlowName
 * @param currentAction
 * @param decision
 * @return list of next available actions
 */
public ArrayList<String> getNextActions(String scrFlowName, String currentAction, String decision){
	ArrayList<String> retar = new ArrayList<String>();
	String workflowFile = 	ScreenFlow.workflowlocationcache.get(scrFlowName);
	String transitTo ="";
	try {
		URL url = ScreenFlow.class.getResource(workflowFile);
		if(url == null) throw new IOException("File not found");
		Document doc = parserXML(url.getFile());
		NodeList nl = doc.getElementsByTagName("state");
			if(currentAction==null || "".equals(currentAction)){
				NodeList nlstart = doc.getElementsByTagName("start-state");
				Element firstNode = (Element)nlstart.item(0);
				retar.add(firstNode.getAttribute("name"));
				/* NodeList descNodes = firstNode.getChildNodes();
				  for(int i=0;i<descNodes.getLength();i++){
					  if(descNodes.item(i).getNodeType() == Document.ELEMENT_NODE){
						  Element desElm = (Element) descNodes.item(i);
						  if(desElm.getNodeName().equals("description")){
							  if(desElm.getTextContent() != null  &&   desElm.getTextContent().length() > 1){
								  System.out.println(desElm.getTextContent().trim());
							  retar.add (desElm.getTextContent().trim());
							  }
							  
						  }
					  }
				  }*/
			}else{ //if already start state
				NodeList nlstart = doc.getElementsByTagName("start-state");
				Element firstNode = (Element)nlstart.item(0);
				Element currentElement = null;
				boolean found = false; 
				 if(currentAction.equals(firstNode.getAttribute("name"))){
					 currentElement = firstNode;
					 found=true;
				 }
				 
				 
				
				XPath xpath = XPathFactory.newInstance().newXPath();
				if( !found ){ 
					NodeList nlstate = (NodeList) xpath.evaluate("process-definition/state", doc, XPathConstants.NODESET);
					//parsing all the states amd match names with  currentAction
					for(int i =0 ;i<nlstate.getLength();i++){
						Node node =  nlstate.item(i);
						if(node.getNodeType() == Document.ELEMENT_NODE){
							Element elm = (Element) node;
							if(currentAction.equals(elm.getAttribute("name"))){
								currentElement = elm;
								 found=true; break;
							}
						}
					}
				}
				
				if( !found ){ 
					NodeList nlstate = (NodeList) xpath.evaluate("process-definition/task-node", doc, XPathConstants.NODESET);
					for(int i =0 ;i<nlstate.getLength();i++){
						Node node =  nlstate.item(i);
						if(node.getNodeType() == Document.ELEMENT_NODE){
							Element elm = (Element) node;
							if(currentAction.equals(elm.getAttribute("name"))){
								currentElement = elm;
								 found=true; break;
							}
						}
					}
				}
				
				if( !found ){ 
					NodeList nlstate = (NodeList) xpath.evaluate("process-definition/fork", doc, XPathConstants.NODESET);
					for(int i =0 ;i<nlstate.getLength();i++){
						Node node =  nlstate.item(i);
						if(node.getNodeType() == Document.ELEMENT_NODE){
							Element elm = (Element) node;
							if(currentAction.equals(elm.getAttribute("name"))){
								currentElement = elm;
								 found=true; break;
							}
						}
					}
				}
				
				//decision is very important will work on it later to implement handlers
				if( !found ){ 
					NodeList nlstate = (NodeList) xpath.evaluate("process-definition/decision", doc, XPathConstants.NODESET);
					for(int i =0 ;i<nlstate.getLength();i++){
						Node node =  nlstate.item(i);
						if(node.getNodeType() == Document.ELEMENT_NODE){
							Element elm = (Element) node;
							if(currentAction.equals(elm.getAttribute("name"))){
								currentElement = elm;
								 found=true; break;
							}
						}
					}
				}
				
				 if( !found ){
					 NodeList nlstate = doc.getElementsByTagName("node");
					 for(int i =0 ;i<nlstate.getLength();i++){
							Node node =  nlstate.item(i);
							if(node.getNodeType() == Document.ELEMENT_NODE){
								Element elm = (Element) node;
								if(currentAction.equals(elm.getAttribute("name"))){
									currentElement = elm;
									 found=true; break;
								}
							}
						}
					 
				 }
				 
				 if( !found ){ 
						NodeList nlstate = (NodeList) xpath.evaluate("process-definition/join", doc, XPathConstants.NODESET);
						for(int i =0 ;i<nlstate.getLength();i++){
							Node node =  nlstate.item(i);
							if(node.getNodeType() == Document.ELEMENT_NODE){
								Element elm = (Element) node;
								if(currentAction.equals(elm.getAttribute("name"))){
									currentElement = elm;
									 found=true; break;
								}
							}
						}
					}
				 if( !found ){ 
						NodeList nlstate = (NodeList) xpath.evaluate("process-definition/mail-node", doc, XPathConstants.NODESET);
						for(int i =0 ;i<nlstate.getLength();i++){
							Node node =  nlstate.item(i);
							if(node.getNodeType() == Document.ELEMENT_NODE){
								Element elm = (Element) node;
								if(currentAction.equals(elm.getAttribute("name"))){
									currentElement = elm;
									 found=true; break;
								}
							}
						}
					}
				 if( !found ){
					 retar.add("Element Not Found");
					 throw new Exception("element not found");
				 }
				  NodeList descNodes = currentElement.getChildNodes();
				  
				 // if(currentElement.getNodeName().equals("decision")){
					  
				  //}
				  
				  for(int i=0;i<descNodes.getLength();i++){
					  if(descNodes.item(i).getNodeType() == Document.ELEMENT_NODE){
						  Element desElm = (Element) descNodes.item(i);
						  if(desElm.getNodeName().equals("transition")){
							  if(desElm.getAttribute("to") != null  &&   desElm.getAttribute("to").length() > 1){
								  transitTo = desElm.getAttribute("to");
								  if(decision != null){
									  String action = desElm.getAttribute("name");
									  if(action.equals(decision)){
										  retar.add (transitTo.trim());  
									  }	
									  continue;
								  }else{
									  retar.add (transitTo.trim());  
								  }
								  							  								  
							  }
							  
						  }
					  }
				  }
				      
			}
	} catch (SAXException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	} catch (ParserConfigurationException e) {
		e.printStackTrace();
	} catch (XPathExpressionException e) {
		e.printStackTrace();
	} catch (Exception e) {
		e.printStackTrace();
	}
	
	return retar;
}


/**
 * gets the screen Name, consider using database table before using screen name from screenflow xml 
 * @param scrFlowName
 * @param currentAction
 * @return screen name
 */
public String getActionScreenName(String scrFlowName,String currentAction){
	String workflowFile = 	ScreenFlow.workflowlocationcache.get(scrFlowName);
	String transitTo ="";
	String screenName = "";
	boolean  found = false;
	try {
		URL url = ScreenFlow.class.getResource(workflowFile);
		if(url == null) throw new IOException("File not found");
		Document doc = parserXML(url.getFile());
		XPath xpath = XPathFactory.newInstance().newXPath();
		NodeList nodelist = (NodeList) xpath.evaluate("/process-definition/start-state[@name=\""+currentAction+"\"]/description", doc, XPathConstants.NODESET);
		
		if(nodelist == null || nodelist.getLength() <1 ){
			nodelist = (NodeList) xpath.evaluate("/process-definition/state[@name=\""+currentAction+"\"]/description", doc, XPathConstants.NODESET);
		}
		if(nodelist == null || nodelist.getLength() ==0){
			nodelist = (NodeList) xpath.evaluate("/process-definition/task-node[@name=\""+currentAction+"\"]/description", doc, XPathConstants.NODESET);
		} 
		if(nodelist == null || nodelist.getLength() <1){
			nodelist = (NodeList) xpath.evaluate("/process-definition/fork[@name=\""+currentAction+"\"]/description", doc, XPathConstants.NODESET);
		} 
		if(nodelist == null || nodelist.getLength() <1){
			nodelist = (NodeList) xpath.evaluate("/process-definition/decision[@name=\""+currentAction+"\"]/description", doc, XPathConstants.NODESET);
		} 
		if(nodelist == null || nodelist.getLength() <1){
			nodelist = (NodeList) xpath.evaluate("/process-definition/join[@name=\""+currentAction+"\"]/description", doc, XPathConstants.NODESET);
		}
		
		if(nodelist != null ){
			found = true;
			
			Node descnode = nodelist.item(0);
			if(descnode != null){
				screenName = descnode.getTextContent();
				screenName = screenName.trim();
			}else{
				screenName = "no screen mapped";	
			}
		}
		
	}catch(IOException e){
		e.printStackTrace();
	} catch (SAXException e) {
		e.printStackTrace();
	} catch (ParserConfigurationException e) {
		e.printStackTrace();
	} catch (XPathExpressionException e) {
		e.printStackTrace();
	}
	
	return screenName;
}
	

	/**
	 * Gets the business Logic for the current action
	 * @param flowName
	 * @param currentAction
	 * @return Business Logic name
	 */
	public String getBusinessLogic(String flowName, String currentAction) {
		String workflowFile = 	ScreenFlow.workflowlocationcache.get(flowName);
		String transitTo ="";
		String businessLogic = "";
		boolean  found = false;
		try {
			URL url = ScreenFlow.class.getResource(workflowFile);
			if(url == null) throw new IOException("File not found");
			Document doc = parserXML(url.getFile());
			XPath xpath = XPathFactory.newInstance().newXPath();
			NodeList nodelist = (NodeList) xpath.evaluate("/process-definition/start-state[@name=\""+currentAction+"\"]/event/action", doc, XPathConstants.NODESET);
			if(nodelist == null || nodelist.getLength() <1){
				nodelist = (NodeList) xpath.evaluate("/process-definition/state[@name=\""+currentAction+"\"]/event/action", doc, XPathConstants.NODESET);
			}
			if(nodelist == null || nodelist.getLength() <1){
				nodelist = (NodeList) xpath.evaluate("/process-definition/task-node[@name=\""+currentAction+"\"]/event/action", doc, XPathConstants.NODESET);
			}
			if(nodelist == null || nodelist.getLength() <1){
				nodelist = (NodeList) xpath.evaluate("/process-definition/fork[@name=\""+currentAction+"\"]/event/action", doc, XPathConstants.NODESET);
			}
			if(nodelist == null || nodelist.getLength() <1){
				nodelist = (NodeList) xpath.evaluate("/process-definition/decision[@name=\""+currentAction+"\"]/event/action", doc, XPathConstants.NODESET);
			}
			if(nodelist == null || nodelist.getLength() <1){
				nodelist = (NodeList) xpath.evaluate("/process-definition/join[@name=\""+currentAction+"\"]/event/action", doc, XPathConstants.NODESET);
			}

			if(nodelist != null ){
				found = true;
				Element descnode = (Element) nodelist.item(0);
				if(descnode != null){
					businessLogic =  descnode.getAttribute("class");
					businessLogic = businessLogic.trim();
				}else{
					businessLogic = "";	
				}
			}
				
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return businessLogic;
	}
	
	/**
	 * @param args
	 * @throws ClassNotFoundException just before class loading
	 * @throws IllegalAccessException  while class loading
	 * @throws InstantiationException while class was instanciation
	 */
	/**
	 * @param args
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		ScreenFlow scf = new ScreenFlow();
		scf.init();
		System.out.println("NextAction:"+scf.getNextActions("newwfl", "mail-node1", null));
		//System.out.println("Current Screen Name:"+scf.getActionScreenName("newwfl", "start-state1"));
		//System.out.println("BusinessLogic:"+scf.getBusinessLogic("loginflow", ""));
		//String className = scf.getBusinessLogic("loginflow", "start");
		//Class aclass = Class.forName(className);
		//BaseBL basebl = (BaseBL) aclass.newInstance();
		System.out.println("findScrFlowNode:");
		ScrFlowNode sfn = scf.populateScrFlowNode ("scrrfqwfl", "RRFMail");
		String script = sfn.getEventscript();
		Interpreter i = new Interpreter(); 
		try {
			if(script!=null) {
				System.out.println("=========Executing BSH script============:\n"+script+" :::");
				i.eval(script);
			}
			 
			System.out.println("Eval="+ i.get("propertySet") );
		} catch (EvalError e) {
			System.out.print("Exception in Script evaluation:"+e.getMessage()+" "+e.getErrorLineNumber());
			 e.printStackTrace();
		}
		
		System.out.println();
	}
	
	
	/**
	 * The xml format which is supported by ScreenFlow can be populated into this object
	 * 
	 * @param flowName
	 * @param currentAction
	 * @return
	 */
	public ScrFlowNode populateScrFlowNode(String flowName, String currentAction) {
		String workflowFile = 	ScreenFlow.workflowlocationcache.get(flowName);
		String transitTo ="";
		String businessLogic = "";
		Node node = null;
		boolean  found = false;
		ScrFlowNode scrflownode = new ScrFlowNode();
		try {
			URL url = ScreenFlow.class.getResource(workflowFile);
			if(url == null) throw new IOException("File not found");
			Document doc = parserXML(url.getFile());
			XPath xpath = XPathFactory.newInstance().newXPath();
			NodeList nodelist = (NodeList) xpath.evaluate("/process-definition/start-state[@name=\""+currentAction+"\"]", doc, XPathConstants.NODESET);
			if(nodelist != null && nodelist.getLength()>0){
				scrflownode.setNodeType("start-state");
			}
			if(nodelist == null || nodelist.getLength() <1){
				nodelist = (NodeList) xpath.evaluate("/process-definition/state[@name=\""+currentAction+"\"]", doc, XPathConstants.NODESET);
				scrflownode.setNodeType("state");
			}
			if(nodelist == null || nodelist.getLength() <1){
				nodelist = (NodeList) xpath.evaluate("/process-definition/task-node[@name=\""+currentAction+"\"]", doc, XPathConstants.NODESET);
				scrflownode.setNodeType("task-node");
			}
			if(nodelist == null || nodelist.getLength() <1){
				nodelist = (NodeList) xpath.evaluate("/process-definition/fork[@name=\""+currentAction+"\"]", doc, XPathConstants.NODESET);
				scrflownode.setNodeType("fork");
			}
			if(nodelist == null || nodelist.getLength() <1){
				nodelist = (NodeList) xpath.evaluate("/process-definition/decision[@name=\""+currentAction+"\"]", doc, XPathConstants.NODESET);
				scrflownode.setNodeType("decision");
			}
			if(nodelist == null || nodelist.getLength() <1){
				nodelist = (NodeList) xpath.evaluate("/process-definition/join[@name=\""+currentAction+"\"]", doc, XPathConstants.NODESET);
				scrflownode.setNodeType("join");
			}
			if(nodelist == null || nodelist.getLength() <1){
				nodelist = (NodeList) xpath.evaluate("/process-definition/mail-node[@name=\""+currentAction+"\"]", doc, XPathConstants.NODESET);
				scrflownode.setNodeType("mail-node");
				HashMap<String,String> emailPropSet = new HashMap<String, String>();
				node = nodelist.item(0); 
				if(node== null)return scrflownode;
				String template = ((Element)node).getAttribute("template");
				String sendto = ((Element)node).getAttribute("to");
				emailPropSet.put("template", (String)template);
				emailPropSet.put("sendto", (String)sendto);
				Element elm = (Element)node;
			 
					NodeList subject = elm.getElementsByTagName("subject");
					String subjectstr="";
					if(subject.item(0) != null)
					 subjectstr = trim(subject.item(0).getTextContent());      
					String textstr="";
					NodeList text = elm.getElementsByTagName("text");
					if(text.item(0) != null)
					textstr = trim(text.item(0).getTextContent());      
				 
				emailPropSet.put("subject", subjectstr);
				emailPropSet.put("msgbody", textstr);
				System.out.println(emailPropSet);
				scrflownode.setEmail(emailPropSet);
			}
			if(nodelist != null && nodelist.getLength()>0){
				found = true;
				Element descnode = (Element) nodelist.item(0);
				node = nodelist.item(0);
				scrflownode.setNode(node);
				// System.out.println( scrflownode.toXMLStr());
				//listNodes(node," ");
				if(descnode != null){
					NodeList innerNodeList = (NodeList) xpath.evaluate("description", node, XPathConstants.NODESET);
					if(innerNodeList !=null && innerNodeList.getLength() >0){
						scrflownode.setDescription(trim(innerNodeList.item(0).getTextContent()));
					}
					
					if(node !=null  ){
						businessLogic =  ((Element)node).getAttribute("expression");
						scrflownode.setDecisionExpression(businessLogic);
					}
					
					innerNodeList = (NodeList) xpath.evaluate("event/action[@class]", node, XPathConstants.NODESET);
					if(innerNodeList !=null && innerNodeList.getLength() >0){
						businessLogic =  ((Element)innerNodeList.item(0)).getAttribute("class");
						scrflownode.setEventactions(businessLogic);
						}
					innerNodeList = (NodeList) xpath.evaluate("event/action[@expression]", node, XPathConstants.NODESET);
					if(innerNodeList !=null && innerNodeList.getLength() >0){
						businessLogic =  ((Element)innerNodeList.item(0)).getAttribute("expression");
						scrflownode.setEventexpression(businessLogic);
						}
					innerNodeList = (NodeList) xpath.evaluate("event/script", node, XPathConstants.NODESET);
					if(innerNodeList !=null && innerNodeList.getLength() >0){
						businessLogic =  trim(((Element)innerNodeList.item(0)).getTextContent());
						scrflownode.setEventscript(businessLogic);
						}
					innerNodeList = (NodeList) xpath.evaluate("transition", node, XPathConstants.NODESET);
					if(innerNodeList !=null && innerNodeList.getLength() >0){
						ArrayList<String> transitionto = new ArrayList<String>();
						for(int i =0; i<innerNodeList.getLength();i++){
							Element elm = (Element)innerNodeList.item(i);
							transitionto.add(elm.getAttribute("to"));
						}
						scrflownode.setTransitionto(transitionto);
					}
					innerNodeList = (NodeList) xpath.evaluate("handler[@class]", node, XPathConstants.NODESET);
					if(innerNodeList !=null && innerNodeList.getLength() >0){ 
						businessLogic =  ((Element)innerNodeList.item(0)).getAttribute("class");
						scrflownode.setDecisionHandler(businessLogic);
						}
					
				} 
			}
			System.out.println("ScrFlowNode:"+scrflownode);	
		}catch(Exception e){
			e.printStackTrace();
		}
		return scrflownode;
	}

	private String trim(String textContent) {
		  if(textContent != null && textContent.length() >0)textContent=textContent.trim();
		return textContent;
	}
	
	
	 
	
}
