package org.jbpm.samarjit.myengine;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.jbpm.samarjit.RestoreWorkflowSession;
import org.xml.sax.SAXException;

public class TestClass {
	private static Logger logger = Logger.getLogger(TestClass.class);
	/**
	 * @param args
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException, SAXException, IOException {
		LwWorkflowManager lw = new LwWorkflowManager();
		lw.readWorkflowFiles(new FileInputStream("C:/softwares/Workflow/jBPM500/jbpm-installer/sample/evaluation/src/main/resources/Evaluation.bpmn"));
		
	}

}
