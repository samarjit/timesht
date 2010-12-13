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
		System.out.print("Intercepted ... action is available now");
		
		 TestactionAC action = (TestactionAC)invocation.getAction();
		 System.out.println(action.getName());
		 System.out.println(action.getRetrieveName());
		 
	       return invocation.invoke();
	}

}
