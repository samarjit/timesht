package actionclass;

import java.util.Date;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;

public class DecoratorInterceptor implements Interceptor {

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		System.out.print("DecoratorInterceptor:Intercepted ... ");
		
		 TestactionAC action = (TestactionAC)invocation.getAction();
		 System.out.print(action.getName());
		 System.out.println(action.getRetrievename());
		 
		 String result =  invocation.invoke();
		 System.out.println("DecoratorInterceptor:..ending interceptor "+ action.getName()+","+action.getRetrievename());
		  
		 return result;
	}

}
