package crud;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.sql.rowset.CachedRowSet;

import org.apache.struts2.ServletActionContext;

import pojo.ListAttribute;
import dao.CrudDAO;
import dao.SearchListDAO;
import dbconn.DBConnector;

public class SearchList {
	private String recCountQuery;
	private int reccount;
	private int pagesize;
	private int pageno;
	

	public SearchList(int pagesize, int pageno) {
		this.pagesize = pagesize;
		this.pageno = pageno;
	}

	private void debug(int priority, String s){
		if(priority > 1){
			System.out.println("SearchList:"+s);
		}
	}
	
	public String getParameter(Map parameterMap, String paramName){
		String[] tmp = (String[]) parameterMap.get(paramName);
		String parameter = (String)(tmp[0]);
		return parameter;
	}
	
	/**
     * This function creates a searchquery based on the screenName, panelName
     * @param metadata 
	 * @param pagesize 
	 * @param pageno 
     * @return String
     */
    public String createSearchQuery(HashMap metadata,String scrname,String panelName,Map<String,String[]> parameterMap){
    	
    	String searchQueryWhere = "";
    	String splWhereClause = "";
    	String searchQuery = "";
    	String joiner = " WHERE ";
     
        DBConnector db = new DBConnector();
    	SearchListDAO searchListDAO = new SearchListDAO(); 
        
    	 
        parameterMap.remove(scrname);
        parameterMap.remove(panelName);
        
        Iterator itr = parameterMap.keySet().iterator();
        
       debug(1,parameterMap.toString());
        while(itr.hasNext()){
        	String fname = (String) itr.next();
        	 String val =  getParameter(parameterMap,fname);
        	
        	CachedRowSet crs;
			try {   
				crs = searchListDAO.getDBColDatatype(scrname,panelName,fname);
			
	        	if(crs.next())	        	{
	        		String dbcol = crs.getString("dbcol");
	        		String datatype = crs.getString("datatype");
	        		
	        		if(null != datatype && !"".equalsIgnoreCase(datatype)){
	        			if("DATE".equalsIgnoreCase(datatype) && !val.equalsIgnoreCase("")){
	        				//MySQL????x
	        				//Oracle ??y
	        				val = val.toUpperCase();
		        			searchQueryWhere +=joiner+" "+dbcol+" = TO_DATE('"+val+"','dd/mm/yyyy')";
		        			joiner = " AND ";
	        			}
	        		}
	        		if(!"".equals(searchQueryWhere)){
	        			joiner = " AND ";
	        		}
	        		
	        		if(!val.equalsIgnoreCase("")){
	        			val = val.toUpperCase();
	        			searchQueryWhere +=joiner+"UPPER("+dbcol+") like '%"+val+"%'";
	        			joiner = " AND ";
	        		}
	        	}
	        	crs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
        }
        
        /////////////////////relatedpanel may be put in screen_panel ///////////////
       
		
		String relatedPanel  = searchListDAO.getRelatedPanelFrmScrPanel(scrname,panelName);
		
		 String tableName = "";
        
		 try {
			CachedRowSet crs = searchListDAO.getTableSplwhereclauseFrmRelatedPanel(scrname,relatedPanel); 
			while(crs.next()){
				tableName = crs.getString("TABLE_NAME");
				splWhereClause = crs.getString("splwhereclause");
			}
			crs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		
		String colquery ="";
		
		 
        try {
			CachedRowSet crs = searchListDAO.getDBmetadataFromRelatedPanel(scrname,relatedPanel);
		
			while(crs.next()){ 
				ListAttribute ls = new ListAttribute();
				ls.setClassname(crs.getString("classname"));
				ls.setDatatype(crs.getString("datatype"));
				ls.setDbcol(crs.getString("dbcol"));
				ls.setFname(crs.getString("fname"));
				ls.setIdname(crs.getString("idname"));
				ls.setLblname(crs.getString("lblname"));
				ls.setPrkey(crs.getString("prkey"));
				colquery = crs.getString("strquery");
				
				metadata.put(crs.getString("idname"), ls) ;
				if(colquery !=null && colquery.length() > 1){
					searchQuery +="("+colquery+") "+crs.getString("fname")+",";
				}else{
				    searchQuery +=crs.getString("dbcol")+" "+crs.getString("idname")+",";
				}
				colquery = "";
			}
			if(searchQuery.length() > 0){
				searchQuery = searchQuery.substring(0, searchQuery.length()-1);
			}
			crs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		searchQuery = "SELECT rownum rn,"+searchQuery + " FROM "+tableName+ searchQueryWhere ;
		
		CrudDAO cd = new CrudDAO();
		String predefQuery = "";
		
		/*
		 * predefquery will contain only the query upto TABLE(+)
		 *   + searchQueryWhere + splWhereClause
		 *   searchQueryWhere already took care of joiner
		 */
		predefQuery = cd.findPreDefQuery(scrname, relatedPanel);
		if(predefQuery!=null && predefQuery.length() > 0 ){
			searchQuery =predefQuery+" "+searchQueryWhere;//.replaceFirst("(?i:SELECT)", "SELECT rownum rn, ")+" "+searchQueryWhere;
			debug(1,"searching ffrom predef query");
		}
		
		
		if(null != splWhereClause && !"".equalsIgnoreCase(splWhereClause)){
			searchQuery+= joiner + splWhereClause;
			joiner = " AND ";
		}
		recCountQuery = searchQuery.replaceFirst("([\\S\\s])*(?ims:FROM)", "SELECT count('x') countrec FROM ");
		
    	
		CachedRowSet crs = null;
		String partialPageQuery = "";
		try {
			db = new DBConnector();
			crs = db.executeQuery(recCountQuery);
			int recfrom , recto;
			if(crs.next()){
				reccount = crs.getInt("countrec");
			}
			debug(1,"joiner="+joiner);
			if(reccount > pagesize){
				recfrom = pageno * pagesize;
				recto = recfrom + pagesize;
				partialPageQuery = " rownum > "+recfrom+" and rownum < "+recto;
				//Applying SQL hint Oracle specific also rownum is an oracle specific pseudocolumn
				searchQuery = "select * from (select /*+ FIRST_ROWS(n) */  rownum rnum,a.* from ("+searchQuery+") a where ROWNUM <="+recto+" ) where rnum > "+recfrom;
				
				//searchQuery = searchQuery.replaceFirst("(.*)(?i:WHERE)(.)", "$1 WHERE "+partialPageQuery +"  AND $2  ");
			}//else just use the original query without doing anything
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (crs != null) {
				try {
					crs.close();
				} catch (Exception e) {
				}
			}
		}
		debug(2,"searchQuery-"+ panelName+":"+searchQuery+"  \ncount query:"+recCountQuery);
		return searchQuery;
    }
    
    
    /**
     * This functions generates the HTML.
     * @param query, metadata
     * @return String
     */
    public String getResultXML(String query, HashMap metadata){
    	  String html = "";
    	  String tableHeader = "";
    	  String data = "";
        try {
        	
			
			SearchListDAO searchListDAO =  new SearchListDAO();
			CachedRowSet crs = searchListDAO.executeSearchQuery(query); 
			boolean firstItr=true;
				while(crs.next()){ 
					
					html += "\n<tr >";
					if(firstItr){
						tableHeader += "\n<tr >";
					}
					Iterator itr = metadata.keySet().iterator();
					 while (itr.hasNext()) {
						String fname = (String) itr.next();
						ListAttribute ls = (ListAttribute) metadata.get(fname);
						data  = crs.getString(fname);
						if(data ==null)data="";
						debug(0,crs.getString(fname));
						if(firstItr){
							tableHeader += "<th><div id="+fname+" style='display:none'>"+ls+"</div>"+ls.getLblname()+"</th>";
							
						}
						 
						html += "<td id="+fname+"> "+data+"</td>";
							
					}if(firstItr){
					 tableHeader += "</tr>";
					 firstItr= false;
					}
					 html += "</tr>";
				
				}
				
				String temphtml="";
				if(html== null || "".equals(html))
				{
					html="No data found";
				}else{
				
				int maxpageno = (int) Math.ceil((double)reccount / pagesize); 
				temphtml = "Page Size:<input style='width:30px;padding:0px' type=\"text\" value='"+pagesize+"' class=\"pagesize\" name=\"pagesize\" onchange=\"javascript:document.getElementById('pageno').value=0;search(this);\" />";
				temphtml+="<select class=\"pageno\" id='pageno' onchange=\"search(this);\">";
					for (int i = 0; i < maxpageno; i++) {
						if(i == pageno)
							temphtml+="<option value=\""+i+"\" selected>"+i+"</option>";
						else
							temphtml+="<option value=\""+i+"\">"+i+"</option>";	
					}
				 temphtml+="</select>";
				}
				
				html = temphtml+"<table border=1>"+tableHeader+html+"</table>";
			crs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
    	
    	
    	return html;
    }
}
