package exformation.geom;

public class Dimension {
	public float w;
	public float h;
	
	public Dimension (float w, float h){
		this.w = w;
		this.h = h;
	}
	public float min(){
		return Math.min(w,h);
	}
	public float max(){
		return Math.max(w,h);
	}
}
