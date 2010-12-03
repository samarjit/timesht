package pojo.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;


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
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="MENU_ID")
	private BigDecimal menuId;

	@Column(name="MENU_NAME")
	private String menuName;

	@Column(name="MENU_ROLE_ID")
	private String menuRoleId;

    public FwMenu() {
    }

	public String getMenuAction() {
		return this.menuAction;
	}

	public void setMenuAction(String menuAction) {
		this.menuAction = menuAction;
	}

	public BigDecimal getMenuId() {
		return this.menuId;
	}

	public void setMenuId(BigDecimal menuId) {
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