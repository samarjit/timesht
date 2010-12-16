package actionclass;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.URIResolver;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.views.xslt.AdapterFactory;
import org.apache.struts2.views.xslt.ServletURIResolver;
import org.json.JSONObject;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.Result;
import com.opensymphony.xwork2.util.ValueStack;



public class XMLResult implements Result {
	 private AdapterFactory adapterFactory;
	    /** Indicates the ognl expression respresenting the bean which is to be exposed as xml. */
	    private String exposedValue;
	    
	protected URIResolver getURIResolver() {
		return new ServletURIResolver(ServletActionContext.getServletContext());
	}

	protected AdapterFactory getAdapterFactory() {
		if (adapterFactory == null)
			adapterFactory = new AdapterFactory();
		return adapterFactory;
	}
    public String getExposedValue() {
        return exposedValue;
    }

    public void setExposedValue(String exposedValue) {
        this.exposedValue = exposedValue;
    }

	@Override
	public void execute(ActionInvocation invocation) throws Exception {
		System.out.println("XMLResult:");
		 HttpServletResponse response = ServletActionContext.getResponse();
		 response.setContentType("text/html");

         Object result = invocation.getAction();
         if (exposedValue != null) {
        	 System.out.println("XMLResult: exposedValue is not null");
             ValueStack stack = invocation.getStack();
             result = stack.findValue(exposedValue);
         }
         PrintWriter writer = response.getWriter();
         JSONObject jobj = new JSONObject(result);
         System.out.println("XMLResult:jobj="+jobj.toString());
         Source xmlSource = getDOMSourceForStack(result);
         
         String stylepath = ServletActionContext.getServletContext().getRealPath("/xml/html.xsl");
			System.out.println("XSLTFilter: transforming with XSL:"+stylepath);
			Source styleSource = new StreamSource(stylepath);
			
         Transformer transformer = TransformerFactory.newInstance().newTransformer(styleSource);
         StreamResult result1 = new StreamResult(writer);
        //DOMSource source = new DOMSource(xmlSource);
        System.out.println("xmlSource = " + xmlSource);
         transformer.transform(xmlSource, result1);
         
         
         System.out.println(result1.getWriter().toString());
         writer.write("hello World from XMLResult.java");
         writer.flush(); 
         
	}

	protected Source getDOMSourceForStack(Object value) throws IllegalAccessException, InstantiationException {
		return new DOMSource(getAdapterFactory().adaptDocument("result", value));
	}

	protected void setAdapterFactory(AdapterFactory adapterFactory) {
		this.adapterFactory = adapterFactory;
	}
}
