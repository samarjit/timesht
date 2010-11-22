package pojo;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * This class constructs a table based on the data to be filled.
 *
 */
public class HTable {
	
	private int row;
	private int col;
	private ArrayList<ArrayList<String>> tdata;
	private String panelName;
	private String cssClassName="";

	public String getCssClassName() {
		return cssClassName;
	}
	public void setCssClassName(String cssClassName) {
		this.cssClassName = cssClassName;
	}
	/**
	 * 
	 * Finds the number of rows and coloumns
	 */
	public HTable(int row, int col) {
		
			tdata = new ArrayList<ArrayList<String>>();
			ArrayList<String> trow = null;
			for(int i =0 ; i<row;i++){
				String [] a = new String [col];
				trow = new ArrayList<String>(Arrays.asList(a));
				this.tdata.add(trow);
			}
			//System.out.println("allocated size:"+tdata.size()+ " "+tdata.get(0).size());
			this.row = row;
			this.col = col;
			
		}
	
	
	/**
	 * Function sets data to the corresponding cell
	 * @param row
	 * @param col
	 * @param val
	 */
	public void add(int row,int col, String val){
		//System.out.println("adding:"+row+","+col+" val:"+val);
		ArrayList<String> trow = tdata.get(row);
		String data = trow.get(col);
		if(data == null)data="";
		data += val;
		trow.set(col, data);
	}

	
	public String toString(){
		String tstring ="";
		String cssString = "";
		for(int i =0 ; i<row;i++){
			ArrayList<String> trow = tdata.get(i);
			tstring +="<TR>";
			for(int j=0; j< col; j++){
				tstring += "<TD >"+((null != trow.get(j))?trow.get(j):"&nbsp;")+"</TD>";
				
			}
			tstring +="</TR>\n";
		}
		if(!"".equals(cssClassName))
		cssString = "class='"+cssClassName+"'";
		
		if(panelName!=null && !("".equals(panelName))){
			tstring ="<TABLE border=1 id="+ panelName +" "+cssString+">"+tstring +"</TABLE>";
		}
		else{
			tstring ="<TABLE border=1 "+cssString+">"+tstring +"</TABLE>";
		}
		return tstring;
	}
	
	public void setTableId(String panelName) {
		this.panelName = panelName;		
	}


	}