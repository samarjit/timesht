package com.ycs.fe;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

/**
 * This class is for future use in case some initialization of resources is to be done or some resource disallocation or some housekeeping
 * activity should be done here.
 * @author SAMARJIT
 *
 */
public class InitFE implements ServletContextListener {
	 
	@Override
	public void contextDestroyed(ServletContextEvent contextEvent) {
		System.out.println("FE Servlet context shutting down...");
	}

	/* Any AMS related initialization of resources can be done here
	 * For future use
	 */
	@Override
	public void contextInitialized(ServletContextEvent contextEvent) {
	 	Logger logger  = Logger.getLogger(InitFE.class);
	 	logger.debug("servlet starting...");
		System.out.println("FE Servlet context Started!!!");
	}

}
