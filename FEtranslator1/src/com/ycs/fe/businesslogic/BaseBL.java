package com.ycs.fe.businesslogic;

import java.util.HashMap;

import com.opensymphony.xwork2.ActionInvocation;

/**
 * This class must be thread safe. Classes implementing this must be 
 * @author Samarjit
 *
 */
public interface BaseBL {
	
HashMap preJsRPCListerner(ActionInvocation invocation);
HashMap postJsRPCListerner(ActionInvocation invocation);
HashMap preCrudListener(ActionInvocation invocation);
HashMap postCrudListener(ActionInvocation invocation);
HashMap preWorkflowListener(ActionInvocation invocation);
HashMap postWorkflowListener(ActionInvocation invocation);

}
