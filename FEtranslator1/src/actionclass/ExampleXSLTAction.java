package actionclass;

import com.opensymphony.xwork2.ActionSupport;


public class ExampleXSLTAction extends ActionSupport{
	

	private static final long serialVersionUID = -8056602884103702806L;
	
	private String testHeader;
	private String testFooter;
	public void setTestHeader(String arg) {
		testHeader = arg;
	}
	public String getTestHeader(){
		return testHeader;
	}
	public void setTestFooter(String arg) {
		testFooter = arg;
	}
	public String getTestFooter(){
		return testFooter;
	}
    public String preview() throws Exception {
		if((testHeader != null && testHeader.length() >0) &&
			(testFooter != null && testFooter.length() >0)){
           System.out.println("ExampleXSLTAction:showing preview in XSLT");
			return "preview";
		}else{
			System.out.println("ExampleXSLTAction: getting input header="+testHeader+", footer="+testFooter);
			return "input";
		}
	}
}