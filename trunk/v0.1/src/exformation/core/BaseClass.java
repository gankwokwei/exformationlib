package exformation.core;

public class BaseClass {

		public void debug(String message){
			System.out.println(message);
		}
		
		public String toString(){
			return this.getClass().getPackage().toString()+":"+getClass().getName();
		}
}
