package pojo.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.json.JSONObject;


/**
 * The persistent class for the FW_MENU database table.
 * 
 */
@Entity
@Table(name="FW_MENU")
public class FwMenu implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="MENU_ACTION")
	private String menuAction;

	@Id
	@GenericGenerator(name = "mygen1", strategy = "increment") 
	@GeneratedValue(generator = "mygen1")
	@Column(name="MENU_ID", unique=true)
	private int menuId;

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
			 menuId =   Integer.parseInt( id);
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

	public int getMenuId() {
		return this.menuId;
	}

	public void setMenuId(int menuId) {
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
}