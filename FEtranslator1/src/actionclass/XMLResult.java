package actionclass;

import java.io.CharArrayWriter;
import java.io.File;
import java.io.PrintWriter;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.URIResolver;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.StrutsResultSupport;
import org.apache.struts2.views.freemarker.FreemarkerManager;
import org.apache.struts2.views.freemarker.FreemarkerResult;
import org.apache.struts2.views.xslt.AdapterFactory;
import org.apache.struts2.views.xslt.ServletURIResolver;
import org.json.JSONObject;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.inject.Inject;
import com.opensymphony.xwork2.util.ValueStack;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.ObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;



public class XMLResult extends StrutsResultSupport {
    protected FreemarkerManager freemarkerManager;
    
    @Inject
    public void setFreemarkerManager(FreemarkerManager mgr) {
        this.freemarkerManager = mgr;
    }
    
	private AdapterFactory adapterFactory;
	    /** Indicates the ognl expression respresenting the bean which is to be exposed as xml. */
	    private String exposedValue;
		private ActionInvocation invocation;
		private ObjectWrapper wrapper;
	    
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

	public void execute1(ActionInvocation invocation) throws Exception {
		System.out.println("XMLResult: started");
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
        // transformer.transform(xmlSource, result1);
         
         //freemarker
        this.invocation = invocation;
      
        Configuration cfg = new Configuration();
        	cfg.setDirectoryForTemplateLoading(new File("F:/eclipse/workspace/charts/deployserver/wtpwebapps/FEtranslator1/WEB-INF/classes/map"));
     		cfg.setObjectWrapper(new DefaultObjectWrapper()); 
     		 wrapper = cfg.getObjectWrapper();
         Template template = cfg.getTemplate("loginmap.xml");
         TemplateModel model =  createModel();
         
         CharArrayWriter charArrayWriter = new CharArrayWriter();
         try {
             template.process(model, charArrayWriter);
             charArrayWriter.flush();
             charArrayWriter.writeTo(writer);
         } finally {
             if (charArrayWriter != null)
                 charArrayWriter.close();
         }
         
         //freemarker
         
         System.out.println(result1.getWriter().toString());
         writer.write("hello World from XMLResult.java");
         writer.flush(); 
         
	}
	protected Configuration getConfiguration() throws TemplateException {
        return freemarkerManager.getConfiguration(ServletActionContext.getServletContext());
    }
	protected Source getDOMSourceForStack(Object value) throws IllegalAccessException, InstantiationException {
		return new DOMSource(getAdapterFactory().adaptDocument("result", value));
	}

	protected void setAdapterFactory(AdapterFactory adapterFactory) {
		this.adapterFactory = adapterFactory;
	}

	 protected TemplateModel createModel() throws TemplateModelException {
	        ServletContext servletContext = ServletActionContext.getServletContext();
	        HttpServletRequest request = ServletActionContext.getRequest();
	        HttpServletResponse response = ServletActionContext.getResponse();
	        ValueStack stack = ServletActionContext.getContext().getValueStack();

	        Object action = null;
	        if(invocation!= null ) action = invocation.getAction(); //Added for NullPointException
	        return freemarkerManager.buildTemplateModel(stack, action, servletContext, request, response, wrapper);
	    }
	 
	@Override
	protected void doExecute(String finalLocation, ActionInvocation invocation) throws Exception {
		System.out.println("finalLocation:"+finalLocation);
		execute1(invocation);
		
	}
}
