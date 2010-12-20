package actionclass;

import java.io.CharArrayWriter;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;
import com.ycs.fe.HTMLProcessor;

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
			HTMLProcessor processor = new HTMLProcessor();
			String resulthtml = processor.process(response.toString(), invocation);
			
			CharResponseWrapper newrsp = new CharResponseWrapper(response);
			PrintWriter out = response.getWriter();
			CharArrayWriter car = new CharArrayWriter();
			car.write("<p>\nYou are visitor number <font color='red'>" +12 + "</font>");
			   car.write("\n</body></html>");
			   response.setContentLength(car.toString().length());
			   out.write(car.toString());
			car.write(resulthtml);
			out.write(car.toString());
			out.write("hello from DecoratorInterceptor");
			out.flush();
			
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
