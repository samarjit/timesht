package com.ycs.fe;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

 

public class HTMLProcessor {
	
 
	
	public static void main(String[] args) throws Exception {
		/* Create and adjust the configuration */
		System.out.println("Running template engine");
		Configuration cfg = new Configuration();
		cfg.setDirectoryForTemplateLoading(new File("map/loginmap.xml"));
		// cfg.setObjectWrapper(new DefaultObjectWrapper());
		Map root = new HashMap();
		/* ------------------------------------------------------------------- */
		/* You usually do these for many times in the application life-cycle: */

		/* Get or create a template */
		TemplateEngine temp = cfg.getTemplate("WebContent/pages/logintpl.html");

		Writer out = new OutputStreamWriter(System.out);
		temp.process(root, out);
		out.flush();

	}
}
