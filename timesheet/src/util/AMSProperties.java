package util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

/**
 * AMSProperties class will be used to keep in cache all the property related details that are set up in ams.properties file essential 
 * while running of AMS application. Info like email of administrator database URL etc. will be cached  
 * @author SAMARJIT
 *
 */
public class AMSProperties {
public static  Properties prop;
private static   AMSProperties isntance;
private boolean initialized = false;

static {
	loadProperties("ams.properties");
}

//private AMSProperties(){
//	loadProperties("ams.properties");
//	if (prop == null) {
//        throw new RuntimeException("ams.properties initialization failed");
//    }
//	
//}
//
// 
//
//public static AMSProperties getInstance(){
//	if(isntance == null) isntance = new AMSProperties();
//	return isntance;
//}


// public void loadProperties(String name) {
//	This doesnot work in servlets, why?? 
//	ClassLoader loader = ClassLoader.getSystemClassLoader();
//	System.out.println("Loading properties..");
//	if(loader != null) {
//		URL url = loader.getResource(name);
//		if(url == null) {
//			url = loader.getResource("/"+name);
//		}
//		System.out.println(" 2. Loading properties.. url:"+url);
//		if(url != null) {
//			try {
//				InputStream in = url.openStream();
//				  prop = new Properties();
//				prop.load(in);
//				System.out.println(" 3. Loading properties..");
//			} catch(IOException ioe) {
//				System.out.println("Exception is AMSProperties:"+ioe);
//			}
//		}
//	}else{
//		System.out.println("Loading properties..class loader is null");
//	}
//
// 
//}

 	
	 
 
public static  String get(String property){
	
	return prop.getProperty(property);
}

	private static   void loadProperties(String string) {
		 URL url = AMSProperties.class.getResource("/"+string);
		 prop = new Properties();
		 try {
	 
			 prop.load(new FileInputStream(url.getFile()));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	public static void main(String[] args) {
	
	}
}
