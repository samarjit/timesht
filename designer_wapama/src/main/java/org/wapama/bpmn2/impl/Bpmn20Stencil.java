/***************************************
 * Copyright (c) Intalio, Inc 2010
 *
 * Permission is hereby granted, free of charge, to any person obtaining a
 * copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
****************************************/
package org.wapama.bpmn2.impl;

import org.eclipse.bpmn2.Association;
import org.eclipse.bpmn2.AssociationDirection;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.Bpmn2Factory;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.CatchEvent;
import org.eclipse.bpmn2.EventDefinition;
import org.eclipse.bpmn2.ThrowEvent;
import org.eclipse.emf.ecore.EClass;

/**
 * @author Antoine Toulme
 * the mapping to stencil ids to BPMN 2.0 metamodel classes
 *
 */
public enum Bpmn20Stencil {

    Task(Bpmn2Package.eINSTANCE.getTask()), 
    BPMNDiagram(Bpmn2Package.eINSTANCE.getDefinitions()),
    Pool(Bpmn2Package.eINSTANCE.getProcess()), 
    Lane(Bpmn2Package.eINSTANCE.getLane()),
    SequenceFlow(Bpmn2Package.eINSTANCE.getSequenceFlow()),
    Task_None(Bpmn2Package.eINSTANCE.getTask()), 
    Task_Script(Bpmn2Package.eINSTANCE.getScriptTask()),
    Task_User(Bpmn2Package.eINSTANCE.getUserTask()),
    Task_Business_Rule(Bpmn2Package.eINSTANCE.getBusinessRuleTask()),
    Task_Manual(Bpmn2Package.eINSTANCE.getManualTask()),
    Task_Service(Bpmn2Package.eINSTANCE.getServiceTask()),
    Task_Send(Bpmn2Package.eINSTANCE.getSendTask()),
    Task_Receive(Bpmn2Package.eINSTANCE.getReceiveTask()),
    Exclusive_Databased_Gateway(Bpmn2Package.eINSTANCE.getExclusiveGateway()),
    ParallelGateway(Bpmn2Package.eINSTANCE.getParallelGateway()),
    EventBasedGateway(Bpmn2Package.eINSTANCE.getEventBasedGateway()),
    InclusiveGateway(Bpmn2Package.eINSTANCE.getInclusiveGateway()),
    StartNoneEvent(Bpmn2Package.eINSTANCE.getStartEvent()),
    StartMessageEvent(Bpmn2Package.eINSTANCE.getStartEvent(), Bpmn2Package.eINSTANCE.getMessageEventDefinition()),
    StartEscalationEvent(Bpmn2Package.eINSTANCE.getStartEvent(), Bpmn2Package.eINSTANCE.getEscalationEventDefinition()),
    StartCompensationEvent(Bpmn2Package.eINSTANCE.getStartEvent(), Bpmn2Package.eINSTANCE.getCompensateEventDefinition()),
    StartSignalEvent(Bpmn2Package.eINSTANCE.getStartEvent(), Bpmn2Package.eINSTANCE.getSignalEventDefinition()),
    StartMultipleEvent(Bpmn2Package.eINSTANCE.getStartEvent()),
    StartParallelMultipleEvent(Bpmn2Package.eINSTANCE.getStartEvent()),
    StartTimerEvent(Bpmn2Package.eINSTANCE.getStartEvent(), Bpmn2Package.eINSTANCE.getTimerEventDefinition()),
    TextAnnotation(Bpmn2Package.eINSTANCE.getTextAnnotation()),
    Group(Bpmn2Package.eINSTANCE.getGroup()),
    DataObject(Bpmn2Package.eINSTANCE.getDataObject()),
    DataStore(Bpmn2Package.eINSTANCE.getDataStore()),
    Message(Bpmn2Package.eINSTANCE.getMessage()),
    EndNoneEvent(Bpmn2Package.eINSTANCE.getEndEvent()),
    EndMessageEvent(Bpmn2Package.eINSTANCE.getEndEvent(), Bpmn2Package.eINSTANCE.getMessageEventDefinition()),
    EndEscalationEvent(Bpmn2Package.eINSTANCE.getEndEvent(), Bpmn2Package.eINSTANCE.getEscalationEventDefinition()),
    EndErrorEvent(Bpmn2Package.eINSTANCE.getEndEvent(), Bpmn2Package.eINSTANCE.getErrorEventDefinition()),
    EndSignalEvent(Bpmn2Package.eINSTANCE.getEndEvent(), Bpmn2Package.eINSTANCE.getSignalEventDefinition()),
    EndTerminateEvent(Bpmn2Package.eINSTANCE.getEndEvent(), Bpmn2Package.eINSTANCE.getTerminateEventDefinition()),
    EndMultipleEvent(Bpmn2Package.eINSTANCE.getEndEvent()),
    EndCompensationEvent(Bpmn2Package.eINSTANCE.getEndEvent(), Bpmn2Package.eINSTANCE.getCompensateEventDefinition()),
    IntermediateMessageEventCatching(Bpmn2Package.eINSTANCE.getIntermediateCatchEvent(), Bpmn2Package.eINSTANCE.getMessageEventDefinition()),
    IntermediateTimerEvent(Bpmn2Package.eINSTANCE.getIntermediateCatchEvent(), Bpmn2Package.eINSTANCE.getTimerEventDefinition()),
    IntermediateEscalationEvent(Bpmn2Package.eINSTANCE.getIntermediateCatchEvent(), Bpmn2Package.eINSTANCE.getEscalationEventDefinition()),
    IntermediateConditionalEvent(Bpmn2Package.eINSTANCE.getIntermediateCatchEvent(), Bpmn2Package.eINSTANCE.getConditionalEventDefinition()),
    IntermediateLinkEventCatching(Bpmn2Package.eINSTANCE.getIntermediateCatchEvent(), Bpmn2Package.eINSTANCE.getLinkEventDefinition()),
    IntermediateErrorEvent(Bpmn2Package.eINSTANCE.getIntermediateCatchEvent(), Bpmn2Package.eINSTANCE.getErrorEventDefinition()),
    IntermediateCancelEvent(Bpmn2Package.eINSTANCE.getIntermediateCatchEvent(), Bpmn2Package.eINSTANCE.getCancelEventDefinition()),
    IntermediateCompensationEventCatching(Bpmn2Package.eINSTANCE.getIntermediateCatchEvent(), Bpmn2Package.eINSTANCE.getCompensateEventDefinition()),
    IntermediateMultipleEventCatching(Bpmn2Package.eINSTANCE.getIntermediateCatchEvent()),
    IntermediateParallelMultipleEventCatching(Bpmn2Package.eINSTANCE.getIntermediateCatchEvent()),
    IntermediateEvent(Bpmn2Package.eINSTANCE.getIntermediateThrowEvent()),
    IntermediateMessageEventThrowing(Bpmn2Package.eINSTANCE.getIntermediateThrowEvent(), Bpmn2Package.eINSTANCE.getMessageEventDefinition()),
    IntermediateEscalationEventThrowing(Bpmn2Package.eINSTANCE.getIntermediateThrowEvent(), Bpmn2Package.eINSTANCE.getEscalationEventDefinition()),
    IntermediateLinkEventThrowing(Bpmn2Package.eINSTANCE.getIntermediateThrowEvent(), Bpmn2Package.eINSTANCE.getLinkEventDefinition()),
    IntermediateCompensationEventThrowing(Bpmn2Package.eINSTANCE.getIntermediateThrowEvent(), Bpmn2Package.eINSTANCE.getCompensateEventDefinition()),
    IntermediateSignalEventThrowing(Bpmn2Package.eINSTANCE.getIntermediateThrowEvent(), Bpmn2Package.eINSTANCE.getSignalEventDefinition()),
    IntermediateMultipleEventThrowing(Bpmn2Package.eINSTANCE.getIntermediateThrowEvent()),
    Association_Undirected(Bpmn2Package.eINSTANCE.getAssociation(), AssociationDirection.NONE),
    Association_Unidirectional(Bpmn2Package.eINSTANCE.getAssociation(), AssociationDirection.ONE),
    Association_Bidirectional(Bpmn2Package.eINSTANCE.getAssociation(), AssociationDirection.BOTH);
    
    
    
    public String id;
    public EClass className;
    public EClass eventType;
    public AssociationDirection associationDirection;
    
    private Bpmn20Stencil(EClass className) {
        this.className = className;
    }
    
    private Bpmn20Stencil(EClass className, AssociationDirection assocDir) {
        this.className = className;
        this.associationDirection = assocDir;
    }
    
    private Bpmn20Stencil(EClass className, EClass eventType) {
        this.className = className;
        this.eventType = eventType;
    }
    
    
    public static BaseElement createElement(String stencilId, String taskType ) {
        Bpmn20Stencil stencil = Bpmn20Stencil.valueOf(taskType == null ? stencilId : stencilId + "_" + taskType.replaceAll(" ", "_"));
        if (stencil == null) {
            throw new IllegalArgumentException("unregistered stencil id: " + stencilId);
        }
        BaseElement elt = (BaseElement) Bpmn2Factory.eINSTANCE.create(stencil.className);
        if (stencil.eventType != null) {
            if (elt instanceof CatchEvent) {
                ((CatchEvent) elt).getEventDefinitions().add((EventDefinition) Bpmn2Factory.eINSTANCE.create(stencil.eventType));
            } else if (elt instanceof ThrowEvent) {
                ((ThrowEvent) elt).getEventDefinitions().add((EventDefinition) Bpmn2Factory.eINSTANCE.create(stencil.eventType));
            } else {
                throw new IllegalArgumentException("Cannot set eventType on " + elt);
            }
        }
        if (stencil.associationDirection != null) {
            ((Association) elt).setAssociationDirection(stencil.associationDirection);
        }
        return elt;
    }
}
