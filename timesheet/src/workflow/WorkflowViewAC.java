package workflow;

import java.beans.Encoder;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringBufferInputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.struts2.ServletActionContext;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import businesslogic.RfqBL;

import com.opensymphony.xwork2.ActionSupport;

/**
 * 
 * To view state of Screen Flow activity 
 * @author SAMARJIT
 *
 */
public class WorkflowViewAC extends ActionSupport {
	private void debug(int priority, String s) {
		if(priority > 0){
			System.out.println("WorkflowViewAC:"+s);
		}
	}
	
private static final long serialVersionUID = 7686422445594093075L;
private  boolean loadworkflow = false;
private String wflid;
private String workflowxmlpath;
private InputStream inputStream;

public String getWflid() {
	return wflid;
}

public void setWflid(String wflid) {
	this.wflid = wflid;
}

private  void copyfile(String srFile, String dtFile){
    try{
      File f1 = new File(srFile);
      File f2 = new File(dtFile);
      InputStream in = new FileInputStream(f1);
      String dstDirpath = dtFile.substring(0,dtFile.lastIndexOf("\\"));
      
      File dstDir = new File(dstDirpath);
      if(!dstDir.isDirectory()){
    	  if(dstDir.mkdirs() == false){
    		  throw new FileNotFoundException("Directory creation exception.");
    	  }
      }
      
      debug(0,"from:"+f1.getAbsolutePath()+"  \nto:"+f2.getAbsolutePath());
      //For Append the file.
//      OutputStream out = new FileOutputStream(f2,true);

      //For Overwrite the file.
      OutputStream out = new FileOutputStream(f2);

      byte[] buf = new byte[1024];
      int len;
      while ((len = in.read(buf)) > 0){
        out.write(buf, 0, len);
      }
      in.close();
      out.close();
      debug(0,"File copied.");
    }
    catch(FileNotFoundException ex){
      debug(5,ex.getMessage() + " in the specified directory.");
    }
    catch(IOException e){
      debug(5,e.getMessage());      
    }
  }


	public String execute(){
		URL  st =  WorkflowViewAC.class.getResource("/scrflowxml/screenflowfactory.xml"); 
		Document doc = null;
		String resultStr = "";
		JSONObject jobj = new JSONObject();
		String error = "";
		try {
				if(loadworkflow == false){
					String topath = ServletActionContext.getServletContext().getRealPath("/scrflowxml");
					String scrflowfile = st.getFile();
					
					String rootfolder = scrflowfile .substring(0, scrflowfile .lastIndexOf("/"));
					debug(0,"toFolder"+topath+"  froomFolder:"+rootfolder);
					File f = new File(rootfolder);
					doc = parserXML(st.getFile());
				 	NodeList nl = doc.getElementsByTagName("screenflow");
				 	
					 for(int i = 0 ;i<nl.getLength();i++){
			        	  Node node = nl.item(i);
			        	  if(node.getNodeType() == Document.ELEMENT_NODE){
			        		   Element elm = (Element) node;
			        		   String key = elm.getAttribute("key");
			        		   String val = elm.getAttribute("value");
			        		   String packagename = val.substring(val.indexOf("/scrflowxml/")+12,val.lastIndexOf("/"));
								String gpd =  rootfolder+"/"+packagename+"/.gpd."+val.substring(val.lastIndexOf("/")+1);
								String img = rootfolder+"/"+packagename+"/processimage.jpg";
								
								copyfile(img, topath+"\\"+packagename+"\\"+"processimage.jpg");
								copyfile(gpd, topath+"\\"+packagename+"\\.gpd."+val.substring(val.lastIndexOf("/")+1));
							}
						}
					loadworkflow = true;
				}
		
		
			doc = parserXML(st.getFile());
			
		 	NodeList nl = doc.getElementsByTagName("screenflow");
	          for(int i = 0 ;i<nl.getLength();i++){
	        	  Node node = nl.item(i);
	        	  if(node.getNodeType() == Document.ELEMENT_NODE){
	        		   Element elm = (Element) node;
	        		   String key = elm.getAttribute("key");
	        		   String val = elm.getAttribute("value");
	        		   if(key.equals(wflid)){
	        			   String packagename = val.substring(0,val.lastIndexOf("/"));
	        			   String gpd =   ""+packagename+"/.gpd."+val.substring(val.lastIndexOf("/")+1);
						   String img =  ""+packagename+"/processimage.jpg";
	        			   jobj.put("gpd",gpd);
	        			   jobj.put("image", img);
	        			   URL  furl = WorkflowViewAC.class.getResource(gpd);
	        			   File f = new File(furl.getFile());
	        			   debug(0,f.getAbsolutePath()+"  exists"+f.exists());
	        			   BufferedReader br = new BufferedReader(new FileReader(furl.getFile()));
	        			   char[] buf= new char[1024];
	        			   int len;
	        			   StringBuffer xmlFile= new StringBuffer();
	        			   xmlFile.setLength(0);
	        			   debug(0,"xmlFile:"+xmlFile+"   \nbuf:"+new String(buf));
		        			   while((len = br.read(buf))>0){
		        				   xmlFile.append(new String(buf));
		        				   java.util.Arrays.fill(buf,' ');
		        			   }  
	        			    debug(0,xmlFile.toString().trim());
	        			   jobj.put("gpdfile", URLEncoder.encode(xmlFile.toString().trim(), "UTF-8").replaceAll("\\+", "%20") );
	        			   
	        		   }
	        	  }
	          }
	  
		} catch (SAXException e) {
			e.printStackTrace();
			error = "Exception in xml parsing";
		} catch (IOException e) {
			e.printStackTrace();
			error = "Exception in file reading";
		} catch (Exception e) {
			e.printStackTrace();
			error = "Exception";
		}
		
		try {
			if(error.length() > 0)
			jobj.put("error",error);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		debug(0,jobj.toString());
		inputStream = new StringBufferInputStream(jobj.toString());
		return SUCCESS;
	}
	
	private Document parserXML(String file) throws SAXException, IOException, ParserConfigurationException
	{
		return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file);
	}
	
	public InputStream getInputStream() {
	    return inputStream;
	}
}
