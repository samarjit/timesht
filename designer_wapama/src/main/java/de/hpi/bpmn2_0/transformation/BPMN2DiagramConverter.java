/**
 * Copyright (c) 2010

 * Philipp Giese, Sven Wagner-Boysen, Robert Gurol
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package de.hpi.bpmn2_0.transformation;

import java.util.List;

import org.oryxeditor.server.diagram.Diagram;

import de.hpi.bpmn2_0.model.Definitions;



/**
 * Converter that transforms a {@link Definition} to a {@link Diagram}.
 *  
 * @author Sven Wagner-Boysen, Robert Gurol
 *
 */
@Deprecated
public class BPMN2DiagramConverter {

	public BPMN2DiagramConverter(String string) {
		System.out.println("BPMN diagram converter samarjit TODO constructor()"+string);
		// TODO Auto-generated constructor stub
	}

	public List<Diagram> getDiagramFromBpmn20(Definitions def) {
		System.out.println("BPMN diagram converter samarjit TODO getDiagramFromBpmn20(def)"+def.getName());
		return null;
	}

}
