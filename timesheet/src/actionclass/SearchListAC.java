package actionclass;

import java.io.InputStream;
import java.io.StringBufferInputStream;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.sql.rowset.CachedRowSet;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ParameterAware;

import pojo.ListAttribute;


import com.opensymphony.xwork2.ActionSupport;

import crud.SearchList;

import dao.CrudDAO;
import dbconn.DBConnector;

/**
 * This is a action class that inherits Strut's Framework's ActionSupport class and is used to perform the search operation. 
 *
 */
public class SearchListAC extends ActionSupport implements ParameterAware {

	private void debug(int priority, String s){
		if(priority > 1){
			System.out.println("SearchListAC:"+s);
		}
	}
	private InputStream inputStream;
    public InputStream getInputStream() {
        return inputStream;
    }
    private String recCountQuery;
    private int reccount;
    private int pagesize = 10;
    private int pageno = 0;
	private String screenName;
	private String panelName;
	private Map<String, String[]> parameter;
    
    
    public int getPagesize() {
		return pagesize;
	}


	public void setPagesize(int pagesize) {
		this.pagesize = pagesize;
	}


	public int getPageno() {
		return pageno;
	}


	public void setPageno(int pageno) {
		this.pageno = pageno;
	}


	
    
    
    
    /**
     * This method is executed by default. 
     * @returns String 
     *
     */
    public String execute() throws Exception {
    	HashMap metadata = new LinkedHashMap();
    	//inputStream = new StringBufferInputStream("Hello World! This is a text string response from a Struts 2 Action.");
    	SearchList searchList = new SearchList(pagesize,pageno);
    	Map<String,String[]> map = parameter;
    	String qry  = searchList.createSearchQuery(metadata,screenName,panelName,map);
        //debug(qry);
        String resXML  = searchList.getResultXML (qry,metadata); 
        inputStream = new StringBufferInputStream(resXML);
        
        return SUCCESS;
    }
	public String getScreenName() {
		return screenName;
	}


	public void setScreenName(String scrname) {
		this.screenName = scrname;
	}


	public String getPanelName() {
		return panelName;
	}


	public void setPanelName(String panelName) {
		this.panelName = panelName;
	}


	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}


	@Override
	public void setParameters(Map<String, String[]> parameter) {
		this.parameter = parameter;
		
	}

}
