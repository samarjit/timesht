package pojo;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.rowset.CachedRowSet;

import org.json.JSONException;
import org.json.JSONObject;

import dbconn.DBConnector;





/**
 * This class constructs html code based on the data defined in the database.
 *
 */
public class Createhtml {
	private String datatype ;
	private String dbcol ;
	private String fname ;
	private String idname ;
	private String lblname ;
	private String ncol ;
	private String nrow ;
	private String panel_name ;
	private String strquery ;
	private String scr_Name ;
	private String validation ;
	private String classname;
	private String htmlelm;
	private String elem_attributes = "";
	private String attributes = "";
	private String dbcolsize="";
	private String validrule ="";
	private String validmsg = "";
	
	
	private String BUTTONPANEL = "buttonPanel";
	private int  COL1ENTRY1  = 1; 
	private int  COL2ENTRY1  = 2; 
	

	private void debug( int priority,String s){
		if(priority > 1)
		System.out.println("Createhtml:"+s);
	}
	
	/**
	 * Gets a list of panels based on the screeName from DB
	 * @param screenName
	 * @return list of panel names
	 */
	public List<String> getPanels(String screenName){
		String SQL = 
			"SELECT panel_name   FROM  screen_panel where scr_name='"+screenName+"' order by SORTORDER ASC";
		DBConnector db = new DBConnector();
		List<String> panelNames = new ArrayList<String>();
		try {
			CachedRowSet crs = db.executeQuery(SQL);
			while(crs.next()){
				panelNames.add( crs.getString("panel_name"));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return 	panelNames;
	}
	
	
	/**
	 * Gets the template name from DB
	 * @param screenName
	 * @return template name
	 */
	public String getTemplateName(String screenName){
		String SQL = 
			"SELECT template_name   FROM  screen  where   scr_name='"+screenName+"'";
		//System.out.println(SQL);
		String templName = "";
		DBConnector db = new DBConnector();
		try {
			CachedRowSet crs = db.executeQuery(SQL);
			while(crs.next()){
				templName =  crs.getString("template_name");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return 	templName;
	}
	
	/**
	 * Generates the html code based on the data defined in DB
	 * @param screenName
	 * @param panelName
	 * @return HTML 
	 */
	public String makehtml(String screenName, String panelName){
		String elmStr="";
		String panelCssClassName = "";
		HTable htable =null;
		DBConnector db = new DBConnector();
		JSONObject rulesall =  new JSONObject();
		JSONObject fieldmsg =  new JSONObject();
		String tablestr = "";
		JSONObject fieldrule = new JSONObject();
		//1: two rows per entry for label field type, 2: for one column per entry
		int panelType = COL2ENTRY1; 
		
		String paneltypeSQL = 
			"select paneltype, CSS_CLASS from screen_panel where panel_name = '"+panelName+"' and scr_name='"+screenName+"'";

		//System.out.println(paneltypeSQL);
		try{
			CachedRowSet crs = db.executeQuery(paneltypeSQL);
			if(crs.next()){
				String panel  = crs.getString("paneltype");
				panelCssClassName = crs.getString("CSS_CLASS");
				if(null!=panel && !"".equalsIgnoreCase(panel))
				     panelType = Integer.parseInt(panel);  //else paneltype =  COL2ENTRY1;
				else {
					System.out.println("Going with default panel type");
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		String SQL = 
			"SELECT scr_Name, panel_name, lblname, fname, idname, datatype," +
					" dbcol, validation, strquery , nrow, ncol,classname,htmlelm,elem_attrib,DBCOL_SIZ,VALID_RULE,VALID_MSG FROM  panel_fields where scr_name='"+screenName+"' AND panel_name='"+panelName+"' order by orderNo";
		//System.out.println(SQL);
		
		try {
			CachedRowSet crs = db.executeQuery(SQL);
			int maxrows=0,xrow=0;
			int maxcols=0,xcol=0;
			while(crs.next()){
				ncol = crs.getString("ncol");
				if(ncol !=null )
					xcol = Integer.parseInt(ncol);
				
				nrow = crs.getString("nrow");
				if(nrow != null)
					xrow = Integer.parseInt(nrow);
				
				if( maxcols < xcol)maxcols = xcol;
				if( maxrows < xrow)maxrows = xrow;
				
				
			}
			crs.absolute(-1); //resetting crs to start point to parse it again
			
			debug(1,"panelType:"+panelType+" "+panelName);
			if(panelType == COL2ENTRY1)
				htable = new HTable((maxrows+1),(maxcols+1)*2);
			else if(panelType == COL1ENTRY1){
				htable = new HTable((maxrows+1),(maxcols+1));
			}
			ncol ="0";
			nrow="0";
			htable.setTableId(panelName);
			
			htable.setCssClassName(panelCssClassName);
			while(crs.next()){
				attributes = "";
				datatype = crs.getString("datatype");
				dbcol = crs.getString("dbcol");
				fname = crs.getString("fname");
				idname = crs.getString("idname");
				lblname = crs.getString("lblname");
				ncol = crs.getString("ncol");
				nrow = crs.getString("nrow");
				panel_name = crs.getString("panel_name");
				scr_Name = crs.getString("scr_Name");
				validation = crs.getString("validation");
				classname = crs.getString("classname");
				htmlelm = crs.getString("htmlelm");
				strquery = crs.getString("strquery");
				elem_attributes = crs.getString("elem_attrib");
				dbcolsize = crs.getString("DBCOL_SIZ");
				validrule = crs.getString("VALID_RULE");
				validmsg = crs.getString("VALID_MSG");
				if(elem_attributes==null){
					elem_attributes = "";
				}
				
				if(validation==null){
					validation = "";
				}
				
				
				if(!elem_attributes.equals("")){
				 attributes = elem_attributes.replaceAll("~", " ");
				}

				//System.out.println(panel_name+" :" + nrow + " "+ncol);
				if("TEXTBOX".equalsIgnoreCase(htmlelm)){
					elmStr = lblname;
					htable.add(Integer.parseInt(nrow), Integer.parseInt(ncol)* panelType, elmStr);
					elmStr = "<input type=\"text\" name='"+fname+"' id='"+idname+"' value='' "+validation+" "+attributes+" class="+classname+" />";
					htable.add(Integer.parseInt(nrow), Integer.parseInt(ncol)* panelType+1, elmStr);
				}
				
				if("PASSWORD".equalsIgnoreCase(htmlelm)){
					elmStr = lblname;
					htable.add(Integer.parseInt(nrow), Integer.parseInt(ncol)* panelType, elmStr);
					elmStr = "<input type=\"password\" name='"+fname+"' id='"+idname+"' value='' "+validation+" "+attributes+" class="+classname+" />";
					htable.add(Integer.parseInt(nrow), Integer.parseInt(ncol)* panelType+1, elmStr);
				}
				
				if("DROPDOWN".equalsIgnoreCase(htmlelm)){
					elmStr = lblname;
					htable.add(Integer.parseInt(nrow), Integer.parseInt(ncol)* panelType, elmStr);
					elmStr = "<select id='"+idname+"' name='"+fname+"' "+validation+" "+attributes+" class="+classname+"><option value=\"\">Select</option>";
					CachedRowSet crs1 = null;
					if(strquery!=null){
					try {
						 
						crs1 = db.executeQuery(strquery);
						while(crs1.next()){
							elmStr+=	"<option value=\""+crs1.getString("VAL")+"\">"+crs1.getString("DES")+"</option>";
						}
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						if (crs1 != null) {
							try {
								crs1.close();
							} catch (Exception e) {
							}
						}
					} }
					elmStr+= "</select>";
					htable.add(Integer.parseInt(nrow), Integer.parseInt(ncol)* panelType+1, elmStr);
				}
				if("HIDDEN".equalsIgnoreCase(htmlelm)){
					//elmStr = lblname;
					//htable.add(Integer.parseInt(nrow), Integer.parseInt(ncol)* panelType, elmStr);
					elmStr = "<input type=\"hidden\" name='"+fname+"' id='"+idname+"' value=''  "+validation+" "+attributes+" class="+classname+" />";
					htable.add(Integer.parseInt(nrow), Integer.parseInt(ncol)* panelType+1, elmStr);
				}
				if("LABEL".equalsIgnoreCase(htmlelm)){
					elmStr = "<strong>"+lblname+"</strong>";
					htable.add(Integer.parseInt(nrow), Integer.parseInt(ncol)* panelType, elmStr);
					elmStr = "";
					htable.add(Integer.parseInt(nrow), Integer.parseInt(ncol)* panelType+1, elmStr);
				}
				if(htmlelm == null || "".equalsIgnoreCase(htmlelm)){
					elmStr = lblname;
					htable.add(Integer.parseInt(nrow), Integer.parseInt(ncol)* panelType, elmStr);
					elmStr = " name='"+fname+"' id='"+idname+"#"+dbcol+"' ";
					htable.add(Integer.parseInt(nrow), Integer.parseInt(ncol)* panelType+1, elmStr);
				}
				
				if("CHECKBOX".equalsIgnoreCase(htmlelm)){
					
				}
				if("RADIO".equalsIgnoreCase(htmlelm)){
					
				}
				if("TEXTAREA".equalsIgnoreCase(htmlelm)){
					elmStr = lblname;
					htable.add(Integer.parseInt(nrow), Integer.parseInt(ncol)* panelType, elmStr);
					elmStr = "<TEXTAREA name='"+fname+"' id='"+idname+"' "+validation+" class="+classname+" ></TEXTAREA>";
					htable.add(Integer.parseInt(nrow), Integer.parseInt(ncol)* panelType+1, elmStr);
				}
				if("BUTTON".equalsIgnoreCase(htmlelm)){
					elmStr = lblname;
				//	htable.add(Integer.parseInt(nrow), Integer.parseInt(ncol)* panelType, "");
				//	elmStr = "<input type=\"button\" name='"+fname+"' id='"+idname+"' value='"+lblname+"'  "+validation+" />";
				//	elmStr = "<div class=\"clear\" "+validation+"><a href=\"#\" class=\"button\" name='"+fname+"' id='"+idname+"'     ><SPAN>"+lblname+"</SPAN></a></div>";
					elmStr = "<button "+validation+" class=\"button "+classname+"\" name='"+fname+"' id='"+idname+"' "+attributes+">"+lblname+"</button>";
					
					int column =0;
					if(panelType == COL2ENTRY1)
						column=1;
					else column=0;
					htable.add(Integer.parseInt(nrow), Integer.parseInt(ncol)* panelType +column, elmStr);
				}
				
				JSONObject rule = new JSONObject();
					try {
						if(dbcolsize != null)rule.put("maxlength", dbcolsize);
						
						if(datatype!=null){
							if("NUMBER".equalsIgnoreCase(datatype))
								rule.put("number", "true");
							else if("INTEGER".equalsIgnoreCase(datatype))
								rule.put("number", "true");
							else if("INT".equalsIgnoreCase(datatype))
								rule.put("number", "true");
							else if("FLOAT".equalsIgnoreCase(datatype))
								rule.put("numberDE", "true");
							else if("DATE".equalsIgnoreCase(datatype))
								rule.put("date", "true");
						}
						String rl = rule.toString();
						if(rl.length() > 2 && validrule!= null ){
							rl = rl.replace("}", ","+validrule+"}");
						}else if(rl.indexOf("}") >-1 && validrule!= null ){
							rl = rl.replace("}", validrule+"}");
						}
						
						JSONObject fieldruleset = new JSONObject(rl);
						if(fname !=null ){
							fieldrule.put(fname, fieldruleset);
							
						}
						
						JSONObject messages = null;
						 if(validmsg!=null && fname !=null && validmsg.contains("}")){
							 messages = new JSONObject(validmsg);
							 fieldmsg.put(fname, messages);
						 }else if(validmsg!=null && fname !=null){
							 messages = new JSONObject();
							 fieldmsg.put(fname, validmsg);
						 }
						 debug(5,"rule:"+rl+ "  :"+validrule+" msg:"+fieldmsg.toString()); 
						
					} catch (JSONException e) {
						e.printStackTrace();
					}
			
			}
			try {
				rulesall.put("rule",fieldrule);
				rulesall.put("message",fieldmsg);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			tablestr = (String)htable.toString();
			tablestr  += "<div id=\"rule\">"+rulesall.toString()+"</div>";
			 debug(1,tablestr);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
return  tablestr;
	} 
	
	
	/**
	 * @param args
	 */
	/*
	public static void main(String[] args) {
		Createhtml htmlc = new Createhtml();
		htmlc.makehtml("frmRequest","panelFields");

	} */

	/**
	 * get's the Javascript,CSS, HTML files defined in DB for the screen Name to be generated.
	 * @param screenName
	 * @return filename
	 */
	public String getJsCsshtml(String screenName) {
		 String jsStr="";
		 String cssStr = "";
		 String[] arjsStr = {};
		 String[] arcssStr = {};
		 String htmlStr = "";
//		 String SQL = "select jsname,cssname from screen_panel where SORTORDER =1  and scr_name='"+screenName+"'";
		 String SQL = "select jsname,cssname from screen  where   scr_name='"+screenName+"'";

		 DBConnector db = new DBConnector();
		 try {
			CachedRowSet crs = db.executeQuery(SQL);
			while(crs.next()){
				jsStr = crs.getString("jsname");
				cssStr = crs.getString("cssname");
			}
			if(jsStr!=null && !"".equals(jsStr))
			arjsStr = jsStr.split(",");
			
			if(cssStr != null && !"".equals(cssStr))
			arcssStr = cssStr.split(",");
			for(String s :arjsStr){
				htmlStr +=	"<script language=\"JavaScript\" src=\"js/"+s+"\"></script>\n";
			}
			for(String s :arcssStr){
				htmlStr +=  "<LINK href=\"css/"+s+"\"  rel=\"stylesheet\" type=\"text/css\">\n";
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		 
		 
		return htmlStr;
	}

	/**
	 * gets the screen Title for the screenName
	 * @param screenName
	 * @return screen Title name
	 */
	public String findScreenTitle(String screenName) {
		CachedRowSet crs = null;
		 String SQL = "select screen_title		 from screen  where   scr_name='"+screenName+"'";
		String scr_title = ""; 
		try {
			DBConnector db = new DBConnector();
			crs = db.executeQuery(SQL);
			if(crs.next()){
				scr_title = crs.getString("screen_title");
			}
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
		return scr_title;
	}

}
