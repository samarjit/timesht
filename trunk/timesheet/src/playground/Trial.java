package playground;
import java.lang.reflect.*;

public class Trial {
	public int add(int a, int b)
    {
       return a + b;
    }
	
   public static void main(String args[])
   {
      try {
         Class cls = Class.forName("playground.Trial");
         Object o = cls.newInstance();
         //Class cls = Class.forName("method2");
         Class partypes[] = new Class[2];
          partypes[0] = Integer.TYPE;
          partypes[1] = Integer.TYPE;
          Method meth = cls.getMethod("add", partypes);
           
          Object arglist[] = new Object[2];
          arglist[0] = new Integer(37);
          arglist[1] = new Integer(47);
          Object retobj 
            = meth.invoke(o, arglist);
          Integer retval = (Integer)retobj;
          System.out.println(retval.intValue());
      }
      catch (Throwable e) {
         System.err.println(e);
      }
   }
}