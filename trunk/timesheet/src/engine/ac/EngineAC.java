package engine.ac;

import java.io.InputStream;
import java.io.StringBufferInputStream;
import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import pojo.entity.FwMenu;

import com.opensymphony.xwork2.ActionSupport;

public class EngineAC extends ActionSupport {

	private EntityManager em;
	private String command;
	private InputStream inputStream;
	
	public EngineAC() {
		EntityManagerFactory emf =    		Persistence.createEntityManagerFactory("timesheet");
    	em = emf.createEntityManager();
	}

	public String execute(){
		
//    	em.getTransaction().begin();
//    	em.persist(newCustomer);
//    	em.getTransaction().commit();
    	Query q = em.createQuery("select new map(x) from FwMenu x");
    	List<HashMap>  lc = q.getResultList();
    	for (HashMap objects : lc) {
    		//FwMenu fw = (FwMenu) objects.get("0");
 			System.out.println(objects.get("0")+"| ");
 		}
		
		
		
		//return "returnajax" can be used to forcefully return as ajax, by default it refreshes the page
		return SUCCESS;
	}
	
	

	public String getMenuList(){
		String retStr = "";
		Query q = em.createQuery("select new map(x) from FwMenu x");
    	List<HashMap>  lc = q.getResultList();
    	FwMenu fw = new FwMenu(); 
    	retStr = "{\"primarykeys\":\""+fw.getPrimaryKeys()+"\",";
    	retStr += "\"listattrs\":\""+fw.getListAttr()+"\",";
    	retStr += "\"listtabledata\":\""+fw.getHeaderNames()+"\n";
    	for (HashMap objects : lc) {
    		 
    		retStr +=(objects.get("0")+"");
 		}
    	
    	retStr += "\"}";
    	
		return retStr;
	}
	
	
	public String executeAjax(){
		String resXML = "";
		
		
		if("selectallmenu".equals(command)){
			resXML = getMenuList();
		}
		
		
		
		System.out.println("Returned XML:"+command+":"+resXML);
        inputStream = new StringBufferInputStream(resXML);
        
		return SUCCESS;
	}
	
	
	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}
	
	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new EngineAC().execute();
	}

}
