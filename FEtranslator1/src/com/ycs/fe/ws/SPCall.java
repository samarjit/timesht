
package com.ycs.fe.ws;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.1.6 in JDK 6
 * Generated source version: 2.1
 * 
 */
@WebService(name = "SPCall", targetNamespace = "http://ws.plsqlcall/")
@XmlSeeAlso({
    ObjectFactory.class
})
public interface SPCall {


    /**
     * 
     * @param arg0
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "callSP", targetNamespace = "http://ws.plsqlcall/", className = "com.ycs.fe.ws.CallSP")
    @ResponseWrapper(localName = "callSPResponse", targetNamespace = "http://ws.plsqlcall/", className = "com.ycs.fe.ws.CallSPResponse")
    public String callSP(
        @WebParam(name = "arg0", targetNamespace = "")
        String arg0);

    /**
     * 
     * @param arg0
     * @return
     *     returns java.lang.String
     * @throws Exception_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "callPLSQL", targetNamespace = "http://ws.plsqlcall/", className = "com.ycs.fe.ws.CallPLSQL")
    @ResponseWrapper(localName = "callPLSQLResponse", targetNamespace = "http://ws.plsqlcall/", className = "com.ycs.fe.ws.CallPLSQLResponse")
    public String callPLSQL(
        @WebParam(name = "arg0", targetNamespace = "")
        String arg0)
        throws Exception_Exception
    ;

}
