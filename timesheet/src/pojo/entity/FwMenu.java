package pojo.entity;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import org.apache.el.util.ReflectionUtil;
import org.json.JSONObject;


/**
 * The persistent class for the FW_MENU database table.
 * 
 */
@Entity
@Table(name="FW_MENU")
public class FwMenu implements Serializable {
	private static final long serialVersionUID = 1L;



	@Id
	 //GenericGenerator(name = "mygen1", strategy = "increment") 
	//@GeneratedValue(generator = "mygen1")
	//@GeneratedValue(strategy= GenerationType.AUTO) JPA
	@GeneratedValue(strategy=GenerationType.TABLE, generator="EMP_SEQ")
    @TableGenerator(name="EMP_SEQ", table="SEQUENCE_TABLE", pkColumnName="SEQ_NAME",
        valueColumnName="SEQ_COUNT", pkColumnValue="EMP_SEQ")
	@Column(name="MENU_ID", unique=true)
	private Long menuId;

	@Column(name="MENU_ACTION")
	private String menuAction;
	
	@Column(name="MENU_NAME")
	private String menuName;

	@Column(name="MENU_ROLE_ID")
	private String menuRoleId;

    public FwMenu() {
    }

	public FwMenu(JSONObject json) {
		 try {
			 String id = (String) json.get("menuId");
			 if(id!=null && !"".equals(id))
			 menuId =     Long.parseLong( id);
			 menuAction = (String) json.get("menuAction");
			 menuName = (String) json.get("menuName");
			 menuRoleId = (String) json.get("menuRoleId");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getMenuAction() {
		return this.menuAction;
	}

	public void setMenuAction(String menuAction) {
		this.menuAction = menuAction;
	}

	 

	public Long getMenuId() {
		return menuId;
	}

	public void setMenuId(Long menuId) {
		this.menuId = menuId;
	}

	public String getMenuName() {
		return this.menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public String getMenuRoleId() {
		return this.menuRoleId;
	}

	public void setMenuRoleId(String menuRoleId) {
		this.menuRoleId = menuRoleId;
	}
	
	public String toString(){
		return "<tr><td>"+menuId+"</td><td>"+menuName+"</td><td>"+menuRoleId+"</td><td>"+menuAction+"</td></tr>";
	}
	
	public String getHeaderNames(){
		return "<tr><td class='menuId'>Menu Id</td><td class='menuName'>Menu Name</td><td class='menuRoleId'>Menu Role Id</td><td class='menuAction'>Menu Action</td></tr>";
	}
	
	public String getListAttr(){
		return "menuId,menuName,menuRoleId,menuAction";
	}
	public String getPrimaryKeys(){
		return "menuId";
	}
	
	

	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		 Class clazz  = Class.forName("pojo.entity.PanelField");
		 Field[] fld  = clazz.getDeclaredFields();
		 String toString  = "\"<tr>";
		 String headerStr = "\"<tr>";
		 String listArray = "\"";
		 String primaryKeys = "\"";
		 String formField = "";
		 String json="";
		 
		 Object obj = clazz.newInstance();
		 Class columnClass = Class.forName( "javax.persistence.Id" ); 
		 for (Field field : fld) {
			 String fieldName = field.getName();
			 String camelcasefldname = Character.toUpperCase(fieldName.charAt(0))+fieldName.substring(1);
			 String camelcasefldnameSpc = camelcasefldname.replaceAll("([a-z]+)([A-Z])", "$1 $2");
			 
			 Class[] types = new Class[] {};
			 Method method;
			 try{ 
				// System.out.println("get"+camelcasefldname);
				method = ReflectionUtil.getMethod(obj, "get"+camelcasefldname, types);
				 
				  
			 }catch(Exception e){
				 method = null;
			 }
			 //System.out.println("method:"+method);
			 if(method == null )continue;
			 Id  id = field.getAnnotation(columnClass);
				if(id != null){
					//System.out.println(field.getName()+" ID ");
					primaryKeys +=field.getName()+",";
				}
				
			 toString +="<td>\"+"+fieldName+"+\"</td>";
			 listArray +=fieldName+",";
			 headerStr +="<td class='"+fieldName+"'>"+camelcasefldnameSpc+"</td>";
			 formField+=camelcasefldnameSpc+ " <input name='"+fieldName+"'/>\n";
			 json += fieldName+" = (String)json.get(\""+fieldName+"\");\n";
			 // System.out.println(fieldName+":"+camelcasefldname);
			 //System.out.println(field.getType());
			 
		 }
		 
		 if(primaryKeys.length() > 0){
			 primaryKeys=  primaryKeys.substring(0,primaryKeys.length()-1);
		 }
		 primaryKeys +="\"";
		 listArray = listArray.substring(0,listArray.length()-1);
		 listArray +="\"";
		 toString +="</tr>\"";
		 headerStr +="</tr>\"";
		 System.out.println("primaryKeys:"+primaryKeys);
		 System.out.println("headerStr:"+headerStr);
		 System.out.println("toString:"+toString);
		 System.out.println("listArray:"+listArray);
		 System.out.println("formField:"+formField);
		 System.out.println("json:"+json);
	}
}