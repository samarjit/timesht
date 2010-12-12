package engine.ac;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringBufferInputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.List;

import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ClassFile;
import javassist.bytecode.annotation.Annotation;

import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.Table;

import org.hibernate.Hibernate;
import org.json.JSONObject;

import pojo.entity.FwMenu;
import pojo.entity.PanelField;
import pojo.entity.Screen;
import pojo.entity.ScreenPanel;

import com.google.gson.Gson;
import com.opensymphony.xwork2.ActionSupport;



public class EngineAC extends ActionSupport {

	private static EntityManager em;
	private String command;
	private InputStream inputStream;
	private String data;
	static{
		EntityManagerFactory emf =    		Persistence.createEntityManagerFactory("timesheet_hibernate");
    	em = emf.createEntityManager();	
	}
	public EngineAC() {
		
	}
	@Deprecated
	public String execute(){
		
//    	em.getTransaction().begin();
//    	em.persist(newCustomer);
//    	em.getTransaction().commit();
    	Query q = em.createQuery("SELECT x FROM pojo.entity.ScreenPanel x where x.id.scrName = :name").setParameter("name", "frmTSheet");
//    	OpenJPAQuery jpaQuery = (OpenJPAQuery) q;
//		System.out.println("HasPositional Parameters:"+jpaQuery.hasPositionalParameters());
//		 Set<Parameter<?>> param = jpaQuery.getParameters();
//		 
//		 for (Parameter<?> parameter : param) {
//			System.out.println(parameter.getPosition()+" "+parameter.getName()+" "+parameter.getParameterType());
//		}
		 
    	List   lc = q.getResultList();
    	for (Object objects : lc) {
    		//FwMenu fw = (FwMenu) objects.get("0"); //efectively this invokes the toString()
 			System.out.println(objects+"| ");
 		}
		
		
		
		//return "returnajax" can be used to forcefully return as ajax, by default it refreshes the page
		return SUCCESS;
	}
	
	public String executeAjax(){
		String resXML = "";
		System.out.println("Execute Ajax");
		
		
		if("selectallmenu_list".equals(command)){
			resXML = getMenuList();
		}else if("menu_listcrud".equals(command)){
			resXML = menuCRUD();
		} 
		if("selectallscreen_list".equals(command)){
			resXML = getScreenList();
		}else if("screen_listcrud".equals(command)){
			resXML = screenCRUD();
		} 
		if("selectallscrpanel_list".equals(command)){
			resXML = getScreenPanelList();
		}else if("scrpanel_listcrud".equals(command)){
			resXML = screenPanelCRUD();
		}
		if("selectallpanelfield_list".equals(command)){
			resXML = getPanelFieldsList();
		}else if("panelfield_listcrud".equals(command)){
			resXML = panelFieldsCRUD();
		}
		System.out.println("Returned XML:"+command+":"+resXML);
        inputStream = new StringBufferInputStream(resXML);
        
		return SUCCESS;
	}

	public String getMenuList(){
		int pageNo =0;
		int pagesize = 0;
		String retStr = "";
		String paging = "";
		String whereclause="";
		String joiner = " WHERE ";
		try {
			JSONObject jobj = new JSONObject(data);
			System.out.println("Input data:"+data);
			pageNo = Integer.parseInt((String)jobj.get("pageno"));
			pagesize = Integer.parseInt((String)jobj.get("pagesize"));
			System.out.println("pageNo:"+pageNo);
		//whereclause
			JSONObject whereobj = null;
			String wherepropar[] = new String[0];
			 
			if (jobj.has("whereclause")) {
				whereobj = jobj.getJSONObject("whereclause");
				wherepropar = JSONObject.getNames(whereobj);

				for (String whereprop : wherepropar) {
					whereclause += joiner + "x." + whereprop + " like :" + whereprop + " ";
					joiner = " AND ";
				}
			}
  
		System.out.println("where clause:"+whereclause);	
		String queryStr = "select  x from FwMenu x "+whereclause+"  order by x.menuId";
		//pagination
		Query count =   (Query) em.createQuery(queryStr 
                .replaceFirst("(?i)SELECT (.*?) FROM", "SELECT COUNT(x.menuName) FROM")
                .replaceFirst("(?i)ORDER BY .*", ""));
		//where replacement
		for (String whereprop : wherepropar) {
			count.setParameter(whereprop, whereobj.get(whereprop));
		}
		
		long ct = (Long)count.getSingleResult();
		System.out.println("Total count:"+ct);
		int pages = (int) Math.ceil((double)ct/pagesize);
		System.out.println("pages :pages"+pages);
		paging +=  "Page Size:<input class='pagesize' style='width:14px' value='"+pagesize+"' ><select class='pageno'>";
		for(int i=0 ; i < pages; i++){
			if(i== pageNo){
				paging +="<option selected>"+i+"</option>";
			}else{
				paging +="<option>"+i+"</option>";
			}
		}
		paging +="</select>";
		//pagination
		
		Query q = em.createQuery(queryStr);
		//where replacement
		for (String whereprop : wherepropar) {
			q.setParameter(whereprop,Long.parseLong((String)whereobj.get(whereprop)));
		}
		
		
		q.setFirstResult(pagesize*pageNo);
		q.setMaxResults(pagesize);
    	FwMenu fw = new FwMenu(); 
    	retStr = "{\"primarykeys\":\""+fw.getPrimaryKeys()+"\",";
    	retStr += "\"listattrs\":\""+fw.getListAttr()+"\",";
    	retStr += "\"paging\":\""+paging+"\",";
    	retStr += "\"listtabledata\":\""+fw.getHeaderNames()+"\n";
    	List<FwMenu> lc = q.getResultList();
    	for (FwMenu objects : lc) {
    		 
    		retStr +=(objects+"");
 		}
    	
    	retStr += "\"}";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retStr;
	}
	public String menuCRUD(){
		System.out.println("data:"+data);
		try {
			JSONObject jobj = new JSONObject(data);
			String subcmd = (String) jobj.get("subcmd");
			System.out.println(subcmd);
			
			JSONObject entitydataobj = jobj.getJSONObject("entitydata");
			Gson gson = new Gson();
			//FwMenu fw = gson.fromJson(entitydataobj.toString(), FwMenu.class);
			FwMenu fw = new FwMenu(jobj.getJSONObject("entitydata"));
			System.out.println(fw);
			String [] star = jobj.getNames(jobj.getJSONObject("entitydata"));
			System.out.println(star.length);
			 
//			 Class fwClass  = Class.forName("pojo.entity.FwMenu");
//			 Class[] types = new Class[] {};
//			 Constructor cons = fwClass.getConstructor(types);
//			 Object fw =  cons.newInstance();
//			 for (String prop : star) {
//				System.out.println(prop);
//				String methodname = "set"+Character.toUpperCase(prop.charAt(0))+prop.substring(1);
//				Method meth = fwClass.getMethod(methodname, types);
//				meth.invoke(fw, new Object[]{entitydataobj.get(prop)});
//				System.out.println(entitydataobj.get(prop));
//			 }
			 em.getTransaction().begin();
			 if("delete".equals(subcmd)){
				 System.err.println("Inside delete..");
//				 Query q = em.createQuery("delete from FwMenu x where x.menuId =:name");
//				 q.setParameter("name", fw.getMenuId());
//				 int deleted = q.executeUpdate();
				 FwMenu fw2 = em.find(FwMenu.class, fw.getMenuId());
				 em.remove(fw2);
			 }else{
				 System.err.println("Inside general subcmd...");
				 em.merge(fw);			 
			 }		    	
		    
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(em.getTransaction().isActive())
			 em.getTransaction().commit();
		}
		return "";
	}
	
	
	///Screen
	public String getScreenList(){
		int pageNo =0;
		int pagesize = 0;
		String retStr = "";
		String paging = "";
		String whereclause="";
		String joiner = " WHERE ";
		try {
			JSONObject jobj = new JSONObject(data);
			pageNo = Integer.parseInt((String)jobj.get("pageno"));
			pagesize = Integer.parseInt((String)jobj.get("pagesize"));
			System.out.println("pageNo:"+pageNo);
			//whereclause
			JSONObject whereobj = null;
			String wherepropar[] = new String[0];
			 
			if (jobj.has("whereclause")) {
				whereobj = jobj.getJSONObject("whereclause");
				wherepropar = JSONObject.getNames(whereobj);

				for (String whereprop : wherepropar) {
					whereclause += joiner + "x." + whereprop + " like :" + whereprop + " ";
					joiner = " AND ";
				}
			}
  
		System.out.println("where clause:"+whereclause);
		String queryStr = "select x from Screen x "+whereclause+" order by x.scrName";
		Query count =   (Query) em.createQuery(queryStr 
                .replaceFirst("(?i)SELECT (.*?) FROM", "SELECT COUNT(x.scrName) FROM")
                .replaceFirst("(?i)ORDER BY .*", ""));
		
		//where replacement
		for (String whereprop : wherepropar) {
			count.setParameter(whereprop, whereobj.get(whereprop));
		}
		
		long ct = (Long)count.getSingleResult();
		System.out.println("Total count:"+ct);
		int pages = (int) Math.ceil((double)ct/pagesize);
		System.out.println("pages :pages"+pages);
		paging +=  "Page Size:<input class='pagesize' style='width:14px' value='"+pagesize+"' ><select class='pageno'>";
		for(int i=0 ; i < pages; i++){
			if(i== pageNo){
				paging +="<option selected>"+i+"</option>";
			}else{
				paging +="<option>"+i+"</option>";
			}
		}
		paging +="</select>";
		
		Query q = em.createQuery(queryStr);
		
		//where replacement
		for (String whereprop : wherepropar) {
			q.setParameter(whereprop, whereobj.get(whereprop));
		}
		
		q.setFirstResult(pagesize*pageNo);
		q.setMaxResults(pagesize);
    	Screen fw = new Screen(); 
    	retStr = "{\"primarykeys\":\""+fw.getPrimaryKeys()+"\",";
    	retStr += "\"listattrs\":\""+fw.getListAttr()+"\",";
    	retStr += "\"paging\":\""+paging+"\",";
    	retStr += "\"listtabledata\":\""+fw.getHeaderNames()+"\n";
    	List<Screen> lc = q.getResultList();
    	for (Screen objects : lc) {
    		 
    		retStr +=(objects+"");
 		}
    	
    	retStr += "\"}";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retStr;
	}
	public String screenCRUD(){
		System.out.println("data:"+data);
		try {
			JSONObject jobj = new JSONObject(data);
			String subcmd = (String) jobj.get("subcmd");
			System.out.println(subcmd);
			Screen fw = new Screen(jobj.getJSONObject("entitydata"));
			System.out.println(fw);
			JSONObject entitydataobj = jobj.getJSONObject("entitydata");
			 String [] star = jobj.getNames(jobj.getJSONObject("entitydata"));
			 System.out.println(star.length);

			 em.getTransaction().begin();
			 if("delete".equals(subcmd)){
				 System.err.println("Inside delete..");
//				 Query q = em.createQuery("delete from Screen x where x.menuId =:name");
//				 q.setParameter("name", fw.getMenuId());
//				 int deleted = q.executeUpdate();
				 Screen fw2 = em.find(Screen.class, fw.getScrName());
				 em.remove(fw2);
			 }else{
				 System.err.println("Inside general subcmd...");
				 em.merge(fw);			 
			 }		    	
		    
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(em.getTransaction().isActive())
			 em.getTransaction().commit();
		}
		return "";
	}
	//////Screen
	
	
	
	///ScreenPanel
	public String getScreenPanelList(){
		int pageNo =0;
		int pagesize = 0;
		String retStr = "";
		String paging = "";
		String whereclause="";
		String joiner = " WHERE ";
		try {
			JSONObject jobj = new JSONObject(data);
			pageNo = Integer.parseInt((String)jobj.get("pageno"));
			pagesize = Integer.parseInt((String)jobj.get("pagesize"));
			System.out.println("pageNo:"+pageNo);
			//whereclause
			JSONObject whereobj = null;
			String wherepropar[] = new String[0];
			 
			if (jobj.has("whereclause")) {
				whereobj = jobj.getJSONObject("whereclause");
				wherepropar = JSONObject.getNames(whereobj);

				for (String whereprop : wherepropar) {
					String tempprop = whereprop.replaceAll("_", "\\."); 
					whereclause += joiner + "x." + tempprop + " like :" + whereprop + " ";
					joiner = " AND ";
				}
			}
  
		System.out.println("where clause:"+whereclause);
		String queryStr = "select x from ScreenPanel x "+whereclause+" order by x.id";
		Query count =   (Query) em.createQuery(queryStr 
                .replaceFirst("(?i)SELECT (.*?) FROM", "SELECT COUNT(x.sortorder) FROM")
                .replaceFirst("(?i)ORDER BY .*", ""));
		
		//where replacement
		for (String whereprop : wherepropar) {
			count.setParameter(whereprop, whereobj.get(whereprop));
		}
		
		long ct = (Long)count.getSingleResult();
		System.out.println("Total count:"+ct);
		int pages = (int) Math.ceil((double)ct/pagesize);
		System.out.println("pages :pages"+pages);
		paging +=  "Page Size:<input class='pagesize' style='width:14px' value='"+pagesize+"' ><select class='pageno'>";
		for(int i=0 ; i < pages; i++){
			if(i== pageNo){
				paging +="<option selected>"+i+"</option>";
			}else{
				paging +="<option>"+i+"</option>";
			}
		}
		paging +="</select>";
		
		Query q = em.createQuery(queryStr);
		
		//where replacement
		for (String whereprop : wherepropar) {
			q.setParameter(whereprop, whereobj.get(whereprop));
		}
		
		q.setFirstResult(pagesize*pageNo);
		q.setMaxResults(pagesize);
    	ScreenPanel fw = new ScreenPanel(); 
    	retStr = "{\"primarykeys\":\""+fw.getPrimaryKeys()+"\",";
    	retStr += "\"listattrs\":\""+fw.getListAttr()+"\",";
    	retStr += "\"paging\":\""+paging+"\",";
    	retStr += "\"listtabledata\":\""+fw.getHeaderNames()+"\n";
    	List<ScreenPanel> lc = q.getResultList();
    	for (ScreenPanel objects : lc) {
    		 
    		retStr +=(objects+"");
 		}
    	
    	retStr += "\"}";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retStr;
	}
	public String screenPanelCRUD(){
		System.out.println("data:"+data);
		try {
			JSONObject jobj = new JSONObject(data);
			String subcmd = (String) jobj.get("subcmd");
			System.out.println(subcmd);
			ScreenPanel fw = new ScreenPanel(jobj.getJSONObject("entitydata"));
			System.out.println(fw);
			JSONObject entitydataobj = jobj.getJSONObject("entitydata");
			 String [] star = jobj.getNames(jobj.getJSONObject("entitydata"));
			 System.out.println(star.length);

			 em.getTransaction().begin();
			 if("delete".equals(subcmd)){
				 System.err.println("Inside delete..");
//				 Query q = em.createQuery("delete from Screen x where x.menuId =:name");
//				 q.setParameter("name", fw.getMenuId());
//				 int deleted = q.executeUpdate();
				 ScreenPanel fw2 = em.find(ScreenPanel.class, fw.getId());
				 em.remove(fw2);
			 }else{
				 System.err.println("Inside general subcmd...");
				 ScreenPanel fw2 = em.find(ScreenPanel.class, fw.getId());
				 System.out.println("fw2 exists:"+fw2);
				 em.merge(fw);			 
			 }		    	
		    
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(em.getTransaction().isActive())
			 em.getTransaction().commit();
		}
		return "";
	}
	//////Screen Panel
	
	///Panel Fields
	public String getPanelFieldsList(){
		int pageNo =0;
		int pagesize = 0;
		String retStr = "";
		String paging = "";
		String whereclause="";
		String joiner = " WHERE ";
		try {
			JSONObject jobj = new JSONObject(data);
			pageNo = Integer.parseInt((String)jobj.get("pageno"));
			pagesize = Integer.parseInt((String)jobj.get("pagesize"));
			System.out.println("pageNo:"+pageNo);
			//whereclause
			JSONObject whereobj = null;
			String wherepropar[] = new String[0];
			 
			if (jobj.has("whereclause")) {
				whereobj = jobj.getJSONObject("whereclause");
				wherepropar = JSONObject.getNames(whereobj);

				for (String whereprop : wherepropar) {
					String tempprop = whereprop.replaceAll("_", "\\."); 
					whereclause += joiner + "x." + tempprop + " like :" + whereprop + " ";
					joiner = " AND ";
				}
			}
  
		System.out.println("where clause:"+whereclause);
		String queryStr = "select x from PanelField x "+whereclause+" order by x.id";
		Query count =   (Query) em.createQuery(queryStr 
                .replaceFirst("(?i)SELECT (.*?) FROM", "SELECT COUNT(x.idname) FROM")
                .replaceFirst("(?i)ORDER BY .*", ""));
		
		//where replacement
		for (String whereprop : wherepropar) {
			count.setParameter(whereprop, whereobj.get(whereprop));
		}
		
		long ct = (Long)count.getSingleResult();
		System.out.println("Total count:"+ct);
		int pages = (int) Math.ceil((double)ct/pagesize);
		System.out.println("pages :pages"+pages);
		paging +=  "Page Size:<input class='pagesize' style='width:14px' value='"+pagesize+"' ><select class='pageno'>";
		for(int i=0 ; i < pages; i++){
			if(i== pageNo){
				paging +="<option selected>"+i+"</option>";
			}else{
				paging +="<option>"+i+"</option>";
			}
		}
		paging +="</select>";
		
		Query q = em.createQuery(queryStr);
		
		//where replacement
		for (String whereprop : wherepropar) {
			q.setParameter(whereprop, whereobj.get(whereprop));
		}
		
		q.setFirstResult(pagesize*pageNo);
		q.setMaxResults(pagesize);
    	PanelField fw = new PanelField(); 
    	retStr = "{\"primarykeys\":\""+fw.getPrimaryKeys()+"\",";
    	retStr += "\"listattrs\":\""+fw.getListAttr()+"\",";
    	retStr += "\"paging\":\""+paging+"\",";
    	retStr += "\"listtabledata\":\""+fw.getHeaderNames()+"\n";
    	List<PanelField> lc = q.getResultList();
    	for (PanelField objects : lc) {
    		 
    		retStr +=(objects+"");
 		}
    	
    	retStr += "\"}";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retStr;
	}
	public String panelFieldsCRUD(){
		System.out.println("data:"+data);
		try {
			JSONObject jobj = new JSONObject(data);
			String subcmd = (String) jobj.get("subcmd");
			System.out.println(subcmd);
			PanelField fw = new PanelField(jobj.getJSONObject("entitydata"));
			System.out.println(fw);
			JSONObject entitydataobj = jobj.getJSONObject("entitydata");
			 String [] star = jobj.getNames(jobj.getJSONObject("entitydata"));
			 System.out.println(star.length);

			 em.getTransaction().begin();
			 if("delete".equals(subcmd)){
				 System.err.println("Inside delete..");
//				 Query q = em.createQuery("delete from Screen x where x.menuId =:name");
//				 q.setParameter("name", fw.getMenuId());
//				 int deleted = q.executeUpdate();
				 PanelField fw2 = em.find(PanelField.class, fw.getId());
				 em.remove(fw2);
			 }else{
				 System.err.println("Inside general subcmd...");
				 PanelField fw2 = em.find(PanelField.class, fw.getId());
				 System.out.println("fw2 exists:"+fw2);
				 
				 em.merge(fw);			 
			 }		    	
		    
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(em.getTransaction().isActive())
			 em.getTransaction().commit();
		}
		return "";
	}
	//////Panel Fields
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
		new EngineAC().execute();
	}

}
