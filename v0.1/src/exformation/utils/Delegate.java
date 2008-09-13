package exformation.utils;

import java.lang.reflect.Method;

public class Delegate {

	//public static String EXECUTE = "execute";

	public Object obj;
	public String method;
	public Object[] args;
	
	public Delegate(Object obj, String method, Object[]args){
		this.method = method;
		this.obj	= obj;
		this.args	= args;
	}
	
	public Delegate(Object obj, String method){
		this(obj,method,null);
	}
	
	public static Delegate create(Object obj, String method){
		return new Delegate(obj,method);
	}
	
	public void execute(){
	 try {
		    Class  c  = obj.getClass();
	        Method m  = c.getMethod(method);
	        m.invoke(obj,args);
	    } catch (Throwable any){
	        System.out.println("[EventCallback]"+any);
	        any.printStackTrace();
	    }
	}

}
