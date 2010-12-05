package engine.ac;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringBufferInputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ClassFile;
import javassist.bytecode.annotation.Annotation;

import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.Table;

import org.json.JSONObject;

import pojo.entity.FwMenu;
import pojo.entity.TsProject;

import com.opensymphony.xwork2.ActionSupport;

public class EngineAC extends ActionSupport {

	private static EntityManager em;
	private String command;
	private InputStream inputStream;
	private String data;
	static{
		//EntityManagerFactory emf =    		Persistence.createEntityManagerFactory("timesheet");
    	//em = emf.createEntityManager();	
	}
	public EngineAC() {
		
	}
	@Deprecated
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
	public String menuCRUD(){
		System.out.println("data:"+data);
		try {
			JSONObject jobj = new JSONObject(data);
			System.out.println(jobj.get("subcmd"));
			FwMenu fw = new FwMenu(jobj.getJSONObject("entitydata"));
			System.out.println(fw);
			JSONObject entitydataobj = jobj.getJSONObject("entitydata");
			 String [] star = jobj.getNames(jobj.getJSONObject("entitydata"));
			 System.out.println(star.length);
			 for (String string : star) {
				System.out.println(string);
				System.out.println(entitydataobj.get(string));
			}
			 em.getTransaction().begin();
			FwMenu fw2 = new FwMenu();
			fw2 = em.merge(fw);
			
			// em.persist(fw);		    	
		    em.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public static void persist( Object o )
	  {
	    try
	    {
	      // Get the table annotation
	      Class tableClass = Class.forName( "javax.persistence.Table" );
	      Table table = ( Table )o.getClass().getAnnotation( tableClass );

	      if( table == null )
	      {
	        // This object doesn’t support the table annotation
	        System.out.println( "I cannot persist this object, it does not support the @Table annotation" );
	        return;
	      }

	      // Read the table name
	      System.out.println( "Table name: " + table.name() );

	      // Access the column class so that we can extract it from our get methods
	      Class columnClass = Class.forName( "javax.persistence.Column" );
	      Field[] f =o.getClass().getDeclaredFields();
	      for (Field field : f) {
			Column c = field.getAnnotation(columnClass);
			if(c != null)
			System.out.println(field.getName()+" "+c.name());
		}
	      
	    }
	    catch( Exception e )
	    {
	      e.printStackTrace();
	    }
	  }
	
	public String executeAjax(){
		String resXML = "";
		System.out.println("Execute Ajax");
		
		
		if("selectallmenu".equals(command)){
			resXML = getMenuList();
		}else if("menucrud".equals(command)){
			resXML = menuCRUD();
		}
		
		
		
		System.out.println("Returned XML:"+command+":"+resXML);
        inputStream = new StringBufferInputStream(resXML);
        
		return SUCCESS;
	}
	
	
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
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

	public void getMyAnnotations(Object fw) throws IOException{
		URL url = EngineAC.class.getResource("/pojo/entity/FwMenu.class");
		System.out.println(url);
		//URL url = new URL("sdf");
		FileInputStream fin = new FileInputStream(url.getPath());
		DataInputStream dstream = new DataInputStream(new BufferedInputStream(fin));

		ClassFile cf =  new ClassFile(dstream);
		String className = cf.getName();
		AnnotationsAttribute visible = (AnnotationsAttribute) cf.getAttribute(AnnotationsAttribute.visibleTag);
		AnnotationsAttribute invisible = (AnnotationsAttribute) cf.getAttribute(AnnotationsAttribute.invisibleTag);
		 
		for (Annotation ann : visible.getAnnotations())
		{
		     System.out.println("@" + ann.getTypeName()+" "+ann.getMemberNames()+" "+ann.getMemberValue("name"));
		      
		}
		 
	}
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		FwMenu fw = new FwMenu();
		new EngineAC().persist(fw);
	}

}
