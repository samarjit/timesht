package com.ycs.fe.commandprocessor;

import org.apache.log4j.Logger;

import net.sf.json.JSONObject;

import com.ycs.fe.dto.InputDTO;
import com.ycs.fe.dto.ResultDTO;

public class BlCommandProcessor implements BaseCommandProcessor {
	private static Logger logger = Logger.getLogger(BlCommandProcessor.class);
	
	@Override
	public ResultDTO processCommand(String screenName, String querynodeXpath, JSONObject jsonRecord, InputDTO inputDTO, ResultDTO resultDTO) {
		logger.debug("Currently processing record:"+jsonRecord.toString());
		return null;
	}

}
