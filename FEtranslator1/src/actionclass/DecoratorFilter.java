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
	 private FilterConfig filterConfig = null;
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		System.out.println("DecoratorFilter: called");
		if (filterConfig == null)
	         return;
		 
		HttpServletRequest request = (HttpServletRequest) req;
	        HttpServletResponse response = (HttpServletResponse) res;
		long startTime = System.currentTimeMillis();
		System.out.println("DecoratorFilter:Starting filter.."+request.getParameter("retrieveName"));
		chain.doFilter(request, response);
		
		PrintWriter out = response.getWriter();
		response.setContentType("text/html");
		response.setContentLength(31);
		
		out.print("<html><body>hello from servlet</body></html>\n");
		out.flush();
		String finalLocation =  "/second.jsp";
		RequestDispatcher dispatcher = request.getRequestDispatcher(finalLocation);
		if (  !response.isCommitted() && (request.getAttribute("javax.servlet.include.servlet_path") == null)) {
            request.setAttribute("struts.view_uri", finalLocation);
            request.setAttribute("struts.request_uri", request.getRequestURI());
            System.out.println("DecoratorFilter:forward: ");
            dispatcher.forward(request, response);
        } else {
        	System.out.println("DecoratorFilter:include: ");
        	dispatcher.include(request, response);
        }
		
		long stopTime = System.currentTimeMillis();
		System.out.println("DecoratorFilter:Time to execute request: " + (stopTime - startTime) + " milliseconds");
		System.out.println("DecoratorFilter:Ending filter"+request.getParameter("retrieveName")+" "+request.getParameter("name"));
		 
		
		
		
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
		 this.filterConfig = null;
		
	}


	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
		
	}
}