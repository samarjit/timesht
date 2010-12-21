package actionclass;

import java.io.CharArrayWriter;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;
import com.opensymphony.xwork2.interceptor.PreResultListener;
import com.ycs.fe.HTMLProcessor;
 
public class DecoratorInterceptor implements Interceptor {
	private Logger logger = Logger.getLogger(this.getClass());
	
	private HttpServletResponse responseParent;

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		logger.debug("DecoratorInterceptor:Intercepted ... ");
		final ActionContext context = invocation.getInvocationContext ();
		HttpServletRequest request = (HttpServletRequest) context.get(ServletActionContext.HTTP_REQUEST);
		 responseParent = (HttpServletResponse) context.get(ServletActionContext.HTTP_RESPONSE);
		 CharResponseWrapper wrapper = new CharResponseWrapper(responseParent);
		 ServletActionContext.setResponse(wrapper);
//		ExampleXSLTAction action = (ExampleXSLTAction)invocation.getAction();
//		 System.out.print(action.getName());
//		 loger.debug(action.getRetrievename());
		 invocation.addPreResultListener(new PreResultListener() {
             public void beforeResult(ActionInvocation invocation, String resultCode) {
                 // perform operation necessary before Result execution
//            	 try {
//            		 
////            		 responseParent = (HttpServletResponse) ActionContext.getContext().get(StrutsStatics.HTTP_RESPONSE);
////            		 responseParent = ServletActionContext.getResponse();
////            		 PrintWriter out = responseParent.getWriter();				
////					 out.write("hello this should come first");
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
            	  
             }
		 });
		 
		 String result =  invocation.invoke();
		  
			logger.debug( "DecoratorInterceptor:request.getContentLength()"+wrapper.toString());
			HTMLProcessor processor = new HTMLProcessor();
			String resulthtml = processor.process(wrapper.toString(), invocation);
			
			
			CharResponseWrapper newrsp = new CharResponseWrapper(wrapper);
			PrintWriter out = responseParent.getWriter();
			CharArrayWriter car = new CharArrayWriter();
			if(processor.getLastResult())
				car.write(resulthtml);
			else
				car.write(wrapper.toString());	
			car.write("TODO: hello from DecoratorInterceptor");
					    
			responseParent.setContentLength(car.toString().length());
			out.write(car.toString());
			out.flush();
			
//		 loger.debug("DecoratorInterceptor:..ending interceptor "+ action.getName()+","+action.getRetrievename());
		 logger.debug("DecoratorInterceptor:..ending");
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
