package actionclass;

import java.io.BufferedReader;
import java.io.FileReader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;

public class DecoratorInterceptor implements Interceptor {

	

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		System.out.println("DecoratorInterceptor:Intercepted ... ");
		final ActionContext context = invocation.getInvocationContext ();
		HttpServletRequest request = (HttpServletRequest) context.get(ServletActionContext.HTTP_REQUEST);
		HttpServletResponse response = (HttpServletResponse) context.get(ServletActionContext.HTTP_RESPONSE);
//		ExampleXSLTAction action = (ExampleXSLTAction)invocation.getAction();
//		 System.out.print(action.getName());
//		 System.out.println(action.getRetrievename());
		 
		 String result =  invocation.invoke();
		 
			
			System.out.println( "DecoratorInterceptor:request.getContentLength()"+response.toString());
			
			
//		 System.out.println("DecoratorInterceptor:..ending interceptor "+ action.getName()+","+action.getRetrievename());
		 System.out.println("DecoratorInterceptor:..ending");
		 return result;
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

}
