package com.ycs;

import java.io.IOException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class BpmnViewer
 */
public class BpmnViewer extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BpmnViewer() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String s ="<?xml version=\"1.0\" encoding=\"UTF-8\"?> \r\n" + 
				"<definitions id=\"Definition\"\r\n" + 
				"             targetNamespace=\"http://www.jboss.org/drools\"\r\n" + 
				"             typeLanguage=\"http://www.java.com/javaTypes\"\r\n" + 
				"             expressionLanguage=\"http://www.mvel.org/2.0\"\r\n" + 
				"             xmlns=\"http://www.omg.org/spec/BPMN/20100524/MODEL\"\r\n" + 
				"             xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\r\n" + 
				"             xsi:schemaLocation=\"http://www.omg.org/spec/BPMN/20100524/MODEL BPMN20.xsd\"\r\n" + 
				"             xmlns:g=\"http://www.jboss.org/drools/flow/gpd\"\r\n" + 
				"             xmlns:bpmndi=\"http://www.omg.org/spec/BPMN/20100524/DI\"\r\n" + 
				"             xmlns:dc=\"http://www.omg.org/spec/DD/20100524/DC\"\r\n" + 
				"             xmlns:di=\"http://www.omg.org/spec/DD/20100524/DI\"\r\n" + 
				"             xmlns:tns=\"http://www.jboss.org/drools\">\r\n" + 
				"\r\n" + 
				"  <process processType=\"Private\" isExecutable=\"true\" id=\"newId\" name=\"droolflow.b\" >\r\n" + 
				"\r\n" + 
				"    <!-- nodes -->\r\n" + 
				"    <startEvent id=\"_1\" name=\"Start\" />\r\n" + 
				"    <userTask id=\"_2\" name=\"User Task\" >\r\n" + 
				"      <extensionElements>\r\n" + 
				"        <tns:onEntry-script>\r\n" + 
				"          <script>System.out.println(\"script to execute on entry\");</script>\r\n" + 
				"        </tns:onEntry-script>\r\n" + 
				"        <tns:onExit-script>\r\n" + 
				"          <script>System.out.println(\"On exit Actions\");</script>\r\n" + 
				"        </tns:onExit-script>\r\n" + 
				"      </extensionElements>\r\n" + 
				"      <ioSpecification>\r\n" + 
				"        <dataInput id=\"_2_MyparameterInput\" name=\"Myparameter\" />\r\n" + 
				"        <dataInput id=\"_2_Myparameter2Input\" name=\"Myparameter2\" />\r\n" + 
				"        <dataInput id=\"_2_CommentInput\" name=\"Comment\" />\r\n" + 
				"        <dataInput id=\"_2_SkippableInput\" name=\"Skippable\" />\r\n" + 
				"        <dataInput id=\"_2_ContentInput\" name=\"Content\" />\r\n" + 
				"        <dataInput id=\"_2_TaskNameInput\" name=\"TaskName\" />\r\n" + 
				"        <dataInput id=\"_2_GroupIdInput\" name=\"GroupId\" />\r\n" + 
				"        <dataOutput id=\"_2_resultParam1Output\" name=\"resultParam1\" />\r\n" + 
				"        <dataOutput id=\"_2_resultParam2Output\" name=\"resultParam2\" />\r\n" + 
				"        <inputSet>\r\n" + 
				"          <dataInputRefs>_2_MyparameterInput</dataInputRefs>\r\n" + 
				"          <dataInputRefs>_2_Myparameter2Input</dataInputRefs>\r\n" + 
				"          <dataInputRefs>_2_CommentInput</dataInputRefs>\r\n" + 
				"          <dataInputRefs>_2_SkippableInput</dataInputRefs>\r\n" + 
				"          <dataInputRefs>_2_ContentInput</dataInputRefs>\r\n" + 
				"          <dataInputRefs>_2_TaskNameInput</dataInputRefs>\r\n" + 
				"          <dataInputRefs>_2_GroupIdInput</dataInputRefs>\r\n" + 
				"        </inputSet>\r\n" + 
				"        <outputSet>\r\n" + 
				"          <dataOutputRefs>_2_resultParam1Output</dataOutputRefs>\r\n" + 
				"          <dataOutputRefs>_2_resultParam2Output</dataOutputRefs>\r\n" + 
				"        </outputSet>\r\n" + 
				"      </ioSpecification>\r\n" + 
				"      <dataInputAssociation>\r\n" + 
				"        <sourceRef>Myvariable1</sourceRef>\r\n" + 
				"        <targetRef>_2_MyparameterInput</targetRef>\r\n" + 
				"      </dataInputAssociation>\r\n" + 
				"      <dataInputAssociation>\r\n" + 
				"        <sourceRef>Myvariable2</sourceRef>\r\n" + 
				"        <targetRef>_2_Myparameter2Input</targetRef>\r\n" + 
				"      </dataInputAssociation>\r\n" + 
				"      <dataInputAssociation>\r\n" + 
				"        <targetRef>_2_CommentInput</targetRef>\r\n" + 
				"        <assignment>\r\n" + 
				"          <from xsi:type=\"tFormalExpression\">Mycomment1</from>\r\n" + 
				"          <to xsi:type=\"tFormalExpression\">_2_CommentInput</to>\r\n" + 
				"        </assignment>\r\n" + 
				"      </dataInputAssociation>\r\n" + 
				"      <dataInputAssociation>\r\n" + 
				"        <targetRef>_2_SkippableInput</targetRef>\r\n" + 
				"        <assignment>\r\n" + 
				"          <from xsi:type=\"tFormalExpression\">false</from>\r\n" + 
				"          <to xsi:type=\"tFormalExpression\">_2_SkippableInput</to>\r\n" + 
				"        </assignment>\r\n" + 
				"      </dataInputAssociation>\r\n" + 
				"      <dataInputAssociation>\r\n" + 
				"        <targetRef>_2_ContentInput</targetRef>\r\n" + 
				"        <assignment>\r\n" + 
				"          <from xsi:type=\"tFormalExpression\">not sure what a content is</from>\r\n" + 
				"          <to xsi:type=\"tFormalExpression\">_2_ContentInput</to>\r\n" + 
				"        </assignment>\r\n" + 
				"      </dataInputAssociation>\r\n" + 
				"      <dataInputAssociation>\r\n" + 
				"        <targetRef>_2_TaskNameInput</targetRef>\r\n" + 
				"        <assignment>\r\n" + 
				"          <from xsi:type=\"tFormalExpression\">taskName</from>\r\n" + 
				"          <to xsi:type=\"tFormalExpression\">_2_TaskNameInput</to>\r\n" + 
				"        </assignment>\r\n" + 
				"      </dataInputAssociation>\r\n" + 
				"      <dataInputAssociation>\r\n" + 
				"        <targetRef>_2_GroupIdInput</targetRef>\r\n" + 
				"        <assignment>\r\n" + 
				"          <from xsi:type=\"tFormalExpression\">Mygroup1</from>\r\n" + 
				"          <to xsi:type=\"tFormalExpression\">_2_GroupIdInput</to>\r\n" + 
				"        </assignment>\r\n" + 
				"      </dataInputAssociation>\r\n" + 
				"      <dataOutputAssociation>\r\n" + 
				"        <sourceRef>_2_resultParam1Output</sourceRef>\r\n" + 
				"        <targetRef>resultParamVariable1</targetRef>\r\n" + 
				"      </dataOutputAssociation>\r\n" + 
				"      <dataOutputAssociation>\r\n" + 
				"        <sourceRef>_2_resultParam2Output</sourceRef>\r\n" + 
				"        <targetRef>resultParamVariable2</targetRef>\r\n" + 
				"      </dataOutputAssociation>\r\n" + 
				"      <potentialOwner>\r\n" + 
				"        <resourceAssignmentExpression>\r\n" + 
				"          <formalExpression>MyAct1</formalExpression>\r\n" + 
				"        </resourceAssignmentExpression>\r\n" + 
				"      </potentialOwner>\r\n" + 
				"    </userTask>\r\n" + 
				"    <userTask id=\"_3\" name=\"User Task\" >\r\n" + 
				"      <ioSpecification>\r\n" + 
				"        <inputSet>\r\n" + 
				"        </inputSet>\r\n" + 
				"        <outputSet>\r\n" + 
				"        </outputSet>\r\n" + 
				"      </ioSpecification>\r\n" + 
				"    </userTask>\r\n" + 
				"    <endEvent id=\"_4\" name=\"End\" >\r\n" + 
				"        <terminateEventDefinition/>\r\n" + 
				"    </endEvent>\r\n" + 
				"    <scriptTask id=\"_5\" name=\"Script2\" scriptFormat=\"http://www.java.com/java\" >\r\n" + 
				"      <script>System.out.println(\"Script task is here\");</script>\r\n" + 
				"    </scriptTask>\r\n" + 
				"    <inclusiveGateway id=\"_6\" name=\"Gateway1\" gatewayDirection=\"Diverging\" />\r\n" + 
				"    <scriptTask id=\"_7\" name=\"Script1\" >\r\n" + 
				"      <script></script>\r\n" + 
				"    </scriptTask>\r\n" + 
				"    <parallelGateway id=\"_8\" name=\"Gateway2\" gatewayDirection=\"Converging\" />\r\n" + 
				"\r\n" + 
				"    <!-- connections -->\r\n" + 
				"    <sequenceFlow id=\"_1-_2\" sourceRef=\"_1\" targetRef=\"_2\" />\r\n" + 
				"    <sequenceFlow id=\"_6-_3\" sourceRef=\"_6\" targetRef=\"_3\" name=\"constraint\" >\r\n" + 
				"      <conditionExpression xsi:type=\"tFormalExpression\" >manualProcess==true</conditionExpression>\r\n" + 
				"    </sequenceFlow>\r\n" + 
				"    <sequenceFlow id=\"_5-_4\" sourceRef=\"_5\" targetRef=\"_4\" />\r\n" + 
				"    <sequenceFlow id=\"_8-_5\" sourceRef=\"_8\" targetRef=\"_5\" />\r\n" + 
				"    <sequenceFlow id=\"_2-_6\" sourceRef=\"_2\" targetRef=\"_6\" />\r\n" + 
				"    <sequenceFlow id=\"_6-_7\" sourceRef=\"_6\" targetRef=\"_7\" name=\"constraint2\" >\r\n" + 
				"      <conditionExpression xsi:type=\"tFormalExpression\" >manualProcess == false</conditionExpression>\r\n" + 
				"    </sequenceFlow>\r\n" + 
				"    <sequenceFlow id=\"_7-_8\" sourceRef=\"_7\" targetRef=\"_8\" />\r\n" + 
				"    <sequenceFlow id=\"_3-_8\" sourceRef=\"_3\" targetRef=\"_8\" />\r\n" + 
				"\r\n" + 
				"  </process>\r\n" + 
				"\r\n" + 
				"  <bpmndi:BPMNDiagram>\r\n" + 
				"    <bpmndi:BPMNPlane bpmnElement=\"newId\" >\r\n" + 
				"      <bpmndi:BPMNShape bpmnElement=\"_1\" >\r\n" + 
				"        <dc:Bounds x=\"14\" y=\"136\" width=\"48\" height=\"48\" />\r\n" + 
				"      </bpmndi:BPMNShape>\r\n" + 
				"      <bpmndi:BPMNShape bpmnElement=\"_2\" >\r\n" + 
				"        <dc:Bounds x=\"98\" y=\"137\" width=\"100\" height=\"45\" />\r\n" + 
				"      </bpmndi:BPMNShape>\r\n" + 
				"      <bpmndi:BPMNShape bpmnElement=\"_3\" >\r\n" + 
				"        <dc:Bounds x=\"313\" y=\"23\" width=\"100\" height=\"48\" />\r\n" + 
				"      </bpmndi:BPMNShape>\r\n" + 
				"      <bpmndi:BPMNShape bpmnElement=\"_4\" >\r\n" + 
				"        <dc:Bounds x=\"642\" y=\"151\" width=\"48\" height=\"48\" />\r\n" + 
				"      </bpmndi:BPMNShape>\r\n" + 
				"      <bpmndi:BPMNShape bpmnElement=\"_5\" >\r\n" + 
				"        <dc:Bounds x=\"521\" y=\"129\" width=\"80\" height=\"48\" />\r\n" + 
				"      </bpmndi:BPMNShape>\r\n" + 
				"      <bpmndi:BPMNShape bpmnElement=\"_6\" >\r\n" + 
				"        <dc:Bounds x=\"250\" y=\"135\" width=\"48\" height=\"48\" />\r\n" + 
				"      </bpmndi:BPMNShape>\r\n" + 
				"      <bpmndi:BPMNShape bpmnElement=\"_7\" >\r\n" + 
				"        <dc:Bounds x=\"312\" y=\"236\" width=\"100\" height=\"48\" />\r\n" + 
				"      </bpmndi:BPMNShape>\r\n" + 
				"      <bpmndi:BPMNShape bpmnElement=\"_8\" >\r\n" + 
				"        <dc:Bounds x=\"430\" y=\"130\" width=\"48\" height=\"48\" />\r\n" + 
				"      </bpmndi:BPMNShape>\r\n" + 
				"      <bpmndi:BPMNEdge bpmnElement=\"_1-_2\" >\r\n" + 
				"        <di:waypoint x=\"38\" y=\"160\" />\r\n" + 
				"        <di:waypoint x=\"148\" y=\"159\" />\r\n" + 
				"      </bpmndi:BPMNEdge>\r\n" + 
				"      <bpmndi:BPMNEdge bpmnElement=\"_6-_3\" >\r\n" + 
				"        <di:waypoint x=\"274\" y=\"159\" />\r\n" + 
				"        <di:waypoint x=\"281\" y=\"70\" />\r\n" + 
				"        <di:waypoint x=\"363\" y=\"47\" />\r\n" + 
				"      </bpmndi:BPMNEdge>\r\n" + 
				"      <bpmndi:BPMNEdge bpmnElement=\"_5-_4\" >\r\n" + 
				"        <di:waypoint x=\"561\" y=\"153\" />\r\n" + 
				"        <di:waypoint x=\"666\" y=\"175\" />\r\n" + 
				"      </bpmndi:BPMNEdge>\r\n" + 
				"      <bpmndi:BPMNEdge bpmnElement=\"_8-_5\" >\r\n" + 
				"        <di:waypoint x=\"454\" y=\"154\" />\r\n" + 
				"        <di:waypoint x=\"561\" y=\"153\" />\r\n" + 
				"      </bpmndi:BPMNEdge>\r\n" + 
				"      <bpmndi:BPMNEdge bpmnElement=\"_2-_6\" >\r\n" + 
				"        <di:waypoint x=\"148\" y=\"159\" />\r\n" + 
				"        <di:waypoint x=\"274\" y=\"159\" />\r\n" + 
				"      </bpmndi:BPMNEdge>\r\n" + 
				"      <bpmndi:BPMNEdge bpmnElement=\"_6-_7\" >\r\n" + 
				"        <di:waypoint x=\"274\" y=\"159\" />\r\n" + 
				"        <di:waypoint x=\"285\" y=\"254\" />\r\n" + 
				"        <di:waypoint x=\"362\" y=\"260\" />\r\n" + 
				"      </bpmndi:BPMNEdge>\r\n" + 
				"      <bpmndi:BPMNEdge bpmnElement=\"_7-_8\" >\r\n" + 
				"        <di:waypoint x=\"362\" y=\"260\" />\r\n" + 
				"        <di:waypoint x=\"452\" y=\"255\" />\r\n" + 
				"        <di:waypoint x=\"454\" y=\"154\" />\r\n" + 
				"      </bpmndi:BPMNEdge>\r\n" + 
				"      <bpmndi:BPMNEdge bpmnElement=\"_3-_8\" >\r\n" + 
				"        <di:waypoint x=\"363\" y=\"47\" />\r\n" + 
				"        <di:waypoint x=\"448\" y=\"68\" />\r\n" + 
				"        <di:waypoint x=\"454\" y=\"154\" />\r\n" + 
				"      </bpmndi:BPMNEdge>\r\n" + 
				"    </bpmndi:BPMNPlane>\r\n" + 
				"  </bpmndi:BPMNDiagram>\r\n" + 
				"\r\n" + 
				"</definitions>";
	}

}
