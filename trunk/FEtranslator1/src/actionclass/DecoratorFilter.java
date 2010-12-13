package actionclass;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.StrutsResultSupport;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;

public final class DecoratorFilter extends StrutsResultSupport implements Filter {

	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		  HttpServletRequest request = (HttpServletRequest) req;
	        HttpServletResponse response = (HttpServletResponse) res;
		long startTime = System.currentTimeMillis();
		System.out.println("Starting filter.."+request.getParameter("retrieveName"));
		
		PrintWriter out = response.getWriter();
		response.setContentType("text/html");
		response.setContentLength(31);
		out.print("<html><body>hello</body></html>");
		out.flush();
		String finalLocation =  "/index.jsp";
		RequestDispatcher dispatcher = request.getRequestDispatcher(finalLocation);
		if (  !response.isCommitted() && (request.getAttribute("javax.servlet.include.servlet_path") == null)) {
            request.setAttribute("struts.view_uri", finalLocation);
            request.setAttribute("struts.request_uri", request.getRequestURI());

            dispatcher.forward(request, response);
        } else {
            dispatcher.include(request, response);
        }
		
		chain.doFilter(request, response);
		long stopTime = System.currentTimeMillis();
		System.out.println("Time to execute request: " + (stopTime - startTime) + " milliseconds");
		System.out.println("Ending filter"+request.getParameter("retrieveName")+" "+request.getParameter("name"));
		 
		
		
		
	}

	 
	@Override
	protected void doExecute(String finalLocation, ActionInvocation invocation) throws Exception {
		ActionContext ctx = invocation.getInvocationContext();
        HttpServletRequest request = (HttpServletRequest) ctx.get(ServletActionContext.HTTP_REQUEST);
        HttpServletResponse response = (HttpServletResponse) ctx.get(ServletActionContext.HTTP_RESPONSE);
        System.out.println("doing filter");
		((HttpServletResponse) response).setStatus(200);
        ((HttpServletResponse) response).setHeader("Location", "somelocation");
        response.getWriter().write("somelocation");
        response.getWriter().close();
        response.sendError(900, "Unable to show problem report: ");
	}


	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}
}