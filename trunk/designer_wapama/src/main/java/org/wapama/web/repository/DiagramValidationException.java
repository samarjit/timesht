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
package org.wapama.web.repository;
/**
 * A customized exception that be thrown when saving a diagram but have property validation errors 
 * @author Ludwig.Xu
 *
 */
public class DiagramValidationException extends Exception {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 2597731669083135146L;

    /** Error JSON String */
    private String _errorJsonStr;

    public DiagramValidationException(String errorJsonString) {
        super("Validation error found.");
        this._errorJsonStr = errorJsonString;
    }

    /**
     * get error JSON String.
     * @return
     */
    public String getErrorJsonStr() {
        return this._errorJsonStr;
    }
}
