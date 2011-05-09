package org.jbpm.samarjit.myengine;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.drools.definition.process.Process;
import org.drools.xml.SemanticModules;
import org.jbpm.bpmn2.xml.BPMNDISemanticModule;
import org.jbpm.bpmn2.xml.BPMNExtensionsSemanticModule;
import org.jbpm.bpmn2.xml.BPMNSemanticModule;
import org.jbpm.compiler.xml.XmlProcessReader;
import org.xml.sax.SAXException;

public class TestClass {

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
