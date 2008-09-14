package exformation.utils;

public class MathUtil {

	public static double C_PI_180 = Math.PI/180;
	public static double C_180_PI = 180/Math.PI;
	
	static public float constrainValue (float n, float mn, float mx) {
		return Math.max( Math.min(n, mx), mn);
	}
	
	static public float degreesToRadians(float theta) {
		return (float) (theta * C_PI_180);
	}
	static public float radiansToDegrees(float theta) {
		return (float) (theta * C_180_PI);
	}
	static public float random(float range){
		return (float)(Math.random()*range);
	}
	static public int random(int range){
		return (int)(Math.random()*range);
	}
	static public float lerp(float start, float stop, float amt){
	 	return start + (stop-start) * amt;
	}
}
