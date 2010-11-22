package util;

import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Utility {
	/**
	 * @return where clause as key value pair in hashmaps, these hashmaps are again encapsulated into 
	 * another hashmap whose key will be panelName
	 * @throws JSONException 
	 */
	public static HashMap<String, String> extractWhereClause(String str/*String whereStringOfPanel String panelName */) throws JSONException{
		//"whereClausefrmRequest"
		
//		String strWhereClause = str;
//		String[] arWhere = strWhereClause.split("~#");
		HashMap<String,String> whereClausePart = new HashMap<String, String>();
//		for(String strWhrKVpair:arWhere){
//			String[] kvpair = strWhrKVpair.split("!");
//			whereClausePart.put(kvpair[0], kvpair[1]);
//		}
		 
		JSONObject jobj = new JSONObject(str);
		JSONArray jarr = jobj.getJSONArray("json");
		for(int i=0;i< jarr.length();i++){
			JSONObject jo = jarr.getJSONObject(i);
			 whereClausePart.put(jo.getString("key"), jo.getString("value"));
			 
		}
	 	
		return whereClausePart;
	}
	
	public static HashMap<String, HashMap<String, String>> extractKeyValPair(String str/*String whereStringOfPanel String panelName */) throws JSONException{
		//"whereClausefrmRequest"
		
		String panleName = null;
		JSONObject injsonobj;
		JSONObject innerjsonobj;

		HashMap<String, HashMap<String, String>> whereClause = new HashMap<String, HashMap<String,String>>();

		//Sample string
		//String jsonStr = "{\"json\":[{\"name\":\"panelFields\",\"valuesar\":[{\"key\":\"bdate\",\"value\":\"sd\"},{\"key\":\"empid\",\"value\":\"sd\"},{\"key\":\"empname\",\"value\":\"\"},{\"key\":\"empemail\",\"value\":\"\"}]},{\"name\":\"panelFields1\",\"valuesar\":[{\"key\":\"bdate\",\"value\":\"\"},{\"key\":\"empid\",\"value\":\"\"},{\"key\":\"empname\",\"value\":\"\"},{\"key\":\"empemail\",\"value\":\"\"}]},{\"name\":\"panelFields2\",\"valuesar\":[{\"key\":\"bdate\",\"value\":\"\"},{\"key\":\"empid\",\"value\":\"\"},{\"key\":\"empname\",\"value\":\"\"},{\"key\":\"empemail\",\"value\":\"\"}]}]}";

		JSONObject json = new JSONObject(str);
		JSONArray injson = (JSONArray) json.get("json") ;

		for(int i = 0; i< injson.length(); i++ ){
			injsonobj = (JSONObject) injson.get(i);
			Iterator itjobj = injsonobj.keys();
			while(itjobj.hasNext()){
				//System.out.println(injsonobj.get(itjobj.next().toString()) instanceof JSONObject);
				HashMap<String, String> valueKey = new HashMap<String, String>();
				panleName = injsonobj.get(itjobj.next().toString()).toString();
				//System.out.println(panleName);
				JSONArray innerjson = (JSONArray) injsonobj.get(itjobj.next().toString());
				for(int j = 0; j< innerjson.length(); j++ ){
					innerjsonobj = (JSONObject) innerjson.get(j);
					Iterator itinjobj = innerjsonobj.keys();
					while(itinjobj.hasNext()){    
						//System.out.println(ii);						
						//System.out.println("+++++++++++==  " + itinjobj.next().toString());
						//System.out.println(itinjobj.next().toString());
						//System.out.println(itinjobj.next().toString());
						String value = innerjsonobj.get(itinjobj.next().toString()).toString();
						String key = innerjsonobj.get(itinjobj.next().toString()).toString();						
						valueKey.put(key, value);
					}
				}
				//System.out.println(injsonobj.get(itjobj.next().toString() instanceof JSONArray);
				whereClause.put(panleName, valueKey);				
			}
			
		}				
		
	/*	String strWhereClause = str;
		String[] arWhere = strWhereClause.split("~#");
		HashMap<String,String> whereClausePart = new HashMap<String, String>();
		for(String strWhrKVpair:arWhere){
			String[] kvpair = strWhrKVpair.split("!");
			if(kvpair[0] !=null )
			whereClausePart.put(kvpair[0].toLowerCase(), kvpair[1]);
		}*/
		
		
		return whereClause;
	}
	/*
	public static void main(String args[]){
		String jsontest ="{\"json\":[{\"key\":\"mgrid\",\"value\":\"1\"},{\"key\":\"empid\",\"value\":\"2\"},{\"key\":\"reqid\",\"value\":\"1812\"}]}";
		try {
			System.out.println(Utility.extractWhereClause(jsontest));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	} */
}
