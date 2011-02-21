package com.ycs.fe;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import net.sf.ehcache.CacheException;

import org.apache.log4j.Logger;

import com.ycs.fe.cache.AppCacheManager;

/**
 * This class is for future use in case some initialization of resources is to be done or some resource disallocation or some housekeeping
 * activity should be done here.
 * @author SAMARJIT
 *
 */
public class InitFE implements ServletContextListener {
	ServletContext context;
	
	@Override
	public void contextDestroyed(ServletContextEvent contextEvent) {
		context = contextEvent.getServletContext();
		System.out.println("FE Servlet context shutting down...");
	}

	/* Any AMS related initialization of resources can be done here
	 * For future use
	 */
	@Override
	public void contextInitialized(ServletContextEvent contextEvent) {
	 	Logger logger  = Logger.getLogger(InitFE.class);
	 	logger.debug("servlet starting...");
	 	context = contextEvent.getServletContext();
	 	context.setAttribute("TEST", "TEST_VALUE");	
	 	AppCacheManager appcache;
		try {
			appcache = AppCacheManager.getInstance();
			appcache.setContext(context);
			appcache.initCache();
		} catch (CacheException e) {
			e.printStackTrace();
		}
	 	System.out.println("FE Servlet context Started!!!");
	}

}
