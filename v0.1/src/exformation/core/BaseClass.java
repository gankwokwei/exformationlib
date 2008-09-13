package exformation.core;

public class BaseClass {

		public void debug(Object message){
			System.out.println(this+" "+message);
		}
		
		public String toString(){
			return "["+getClass().getName()+"]";
		}
}
