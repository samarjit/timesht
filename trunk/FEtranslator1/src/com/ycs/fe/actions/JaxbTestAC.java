package com.ycs.fe.actions;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import map.generated.Root;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.opensymphony.xwork2.ActionSupport;


@Action(value="jaxbtest",results={@Result(name="success",location="/pages/screenmap.jsp")})
public class JaxbTestAC extends ActionSupport{
 
	private static final long serialVersionUID = -3779974905508711588L;
private Root screenroot; 

	public String execute(){
		System.out.println("JaxB Test screenMap ");
		JSONObject json1 = new JSONObject(screenroot);
		System.out.println("json1="+json1);
		JAXBContext jaxbContext;
		try {
			jaxbContext = JAXBContext.newInstance(Root.class);
			final Root root = (Root) jaxbContext
					.createUnmarshaller()
					.unmarshal(
							new File(
									"F:/eclipse/workspace/charts/FEtranslator1/src/map/ProgramSetup.xml"));
			screenroot = root;
			JSONObject json = new JSONObject(root);
			Gson gson = new Gson();
//			String gsonstr = gson.toJson(root);
			System.out.println("JSON="+json);
//			System.out.println("gsonstr="+gsonstr);
			
		} catch (JAXBException e) {
			e.printStackTrace();
		}

		return SUCCESS;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			final JAXBContext jaxbContext = JAXBContext.newInstance(Root.class);
			final Root root =
	            (Root) jaxbContext.createUnmarshaller().unmarshal(
	                new File("F:/eclipse/workspace/charts/FEtranslator1/src/map/ProgramSetup.xml"));
			System.out.println(root.getPanels().getPanel().get(0).getContent());
			
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		
	}
	public void setScreenroot(Root screenroot) {
		this.screenroot = screenroot;
	}
	public Root getScreenroot() {
		return screenroot;
	}
	  

}
