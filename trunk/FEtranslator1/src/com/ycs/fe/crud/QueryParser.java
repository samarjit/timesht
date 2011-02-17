package com.ycs.fe.crud;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.ycs.fe.dto.PrepstmtDTOArray;
import com.ycs.fe.dto.PrepstmtDTO.DataType;

public class QueryParser{
	private static Logger logger = Logger.getLogger(QueryParser.class);
	
	public static String parseQuery(String updatequery,String panelname,JSONObject jsonObject, PrepstmtDTOArray  arparam, HashMap<String, DataType> hmfielddbtype) throws Exception{
		//Where
//		String updatewhere = crudnode.selectSingleNode("sqlwhere").getText();
		Pattern   pattern = Pattern.compile("\\:(\\w*)\\[?(\\d*)\\]?\\.?(\\w*)",Pattern.DOTALL|Pattern.MULTILINE);
		
		
		
		
		Matcher m1 = pattern.matcher(updatequery); // get a matcher object
	       int count = 0;
	       int end = 0;
	       String parsedquery = "";
		while(m1.find()) {
	          
	          String prop =  m1.group();
	          logger.debug("Start preparing:"+prop + m1.start() + " "+m1.end()+" "+m1.group(1)+" "+m1.group(2)+" "+m1.group(3));
	          if(! "".equals(m1.group(2))){
	        	  if(m1.group(1).equals(panelname)){ //:form[0].param
	        		  //fill with present panel row object
	        		  String propname = m1.group(3);
	        		  String propval = "";
	        		  if(jsonObject.has(propname)){
	        			  propval = jsonObject.getString(propname);
	        			  parsedquery += updatequery.substring(end,m1.start());//
	        			  
	        			  arparam.add(hmfielddbtype.get(propname),propval);
	        			   
	        			  
	        			  
	        			  parsedquery += "?";
	        		  }
	        		  end = m1.end(); 
	        		  logger.debug("with dot"+m1.group(2)+"."+propname);
	        	  }else{ //:formXX[0].param
	        		  logger.debug("does it come here"+  m1.group(1));
	        		  //TODO: implement for object filling from related panels.
	        		  
	        	  }
	          }else{ //fill with present panel row object :formxparam
	        	  String propval;
	        	  if(jsonObject.has(m1.group(1)) ){
	        		  String propname = m1.group(1);
	        		  propval = jsonObject.getString(propname);
	        		  parsedquery += updatequery.substring(end,m1.start());//
					 
        				  arparam.add(hmfielddbtype.get(propname),propval);
        			   
	        		  parsedquery += "?";
	        	  }
	        	  
        		  end = m1.end(); 
        		  logger.debug("else no dot"+m1.group(1));
	          }
	          count++;
	       }
	       parsedquery += updatequery.substring(end);
	       updatequery = parsedquery;
	       return parsedquery;
	}
	
}