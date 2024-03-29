package com.ycs.fe.commandprocessor;

import net.sf.json.JSONObject;

import com.ycs.fe.dto.InputDTO;
import com.ycs.fe.dto.ResultDTO;

public interface BaseCommandProcessor {
/**
 * @param screenName - This is required for accessing the main screen related resources and screenDefinition XML
 * @param querynodeXpath - Contains the XPath of the node to be processed. Referenced in Opts field in cmd  
 * @param jsonRecord - Individual record that is currently getting processes. This is essentially a subset of inputDTO
 * @param inputDTO  - Complete DTO containing the full request
 * @param resultDTO - Complete result created per request from client. This will howver contain all the results of chained 
 * queries
 * @return The complete resultDTO once all the queries in the chain are processed.
 */
ResultDTO processCommand(String screenName,String querynodeXpath, JSONObject jsonRecord, InputDTO inputDTO, ResultDTO resultDTO);
}
