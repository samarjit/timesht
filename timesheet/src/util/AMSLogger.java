
/**
* @Author			: 	samrjit
* @Creation Date	: 	13/03/2009
* @Description		:   Logger file
*  
* @          -------------------------------------------------
* @Revision: version Revision Date    Name     Change Description      
* @          -------------------------------------------------
* @          1.0.0.0.0  12/002/2009  Justin		desc          
*/

package util;

import java.net.URL;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;


public class AMSLogger  {
	
    private static boolean initialize = false;
    private static AMSLogger usslogger = null;
    private Logger logger;
    
    static {
        try {
            AMSLogger.initialize("log4j.properties");
        } catch (Exception e) {
            e.printStackTrace();
        }
    } //end of static
    
    private AMSLogger() {
        if (!initialize) {
            throw new RuntimeException("Initialize first");
        }
        logger = Logger.getLogger("sg.edu.nus.iss");
    }
    
    public static AMSLogger getInstance() {
    	if(AMSLogger.usslogger == null) {
    		AMSLogger.usslogger = new AMSLogger();
    	}
    	return usslogger;
    }
    //0.0.0.0.0 
	public static void initialize(String fileName) throws Exception {
        try {
        	URL url = AMSLogger.class.getResource("/"+fileName);
            PropertyConfigurator.configure(url.getFile());
        } catch (Exception e) {
            e.printStackTrace();
        }
        initialize = true;
    }
	//1.0.0.0.0  EOC
    public static boolean isInitialized() {
        return initialize;
    }

    public void debug(Object message) {
    	if (logger.isDebugEnabled()) {
            logger.debug(message);
        }
    }

    public void info(Object message) {
        logger.info(message);
    }

    public void warn(Object message) {
        logger.warn(message);
    }

    public void error(Object message) {
        logger.error(message);
    }

    public void fatal(Object message) {
        logger.fatal(message);
    }

    public boolean isDebugEnabled() {
        return logger.isDebugEnabled();
    }
    public static void main(String[] args) {
		AMSLogger logger = AMSLogger.getInstance();
		logger.debug("hello");
		System.out.println("logger");
	}
}
