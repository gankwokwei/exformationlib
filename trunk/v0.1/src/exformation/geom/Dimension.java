package exformation.geom;

public class Dimension {
	public float w;
	public float h;
	
	public Dimension (float w, float h){
		this.w = w;
		this.h = h;
	}
	public Dimension(){
		this(0,0);
	}
	public float min(){
		return Math.min(w,h);
	}
	public float max(){
		return Math.max(w,h);
	}
	
	public Dimension clone(){
		return new Dimension(w,h);
	}
	public void max(Dimension d){
		w = Math.max(w, d.w);
		h = Math.max(h, d.h);
	}
	public void min(Dimension d){
		w = Math.min(w, d.w);
		h = Math.min(h, d.h);
	}
	public float aspectRatio(){
		return w/h;
	}
	public void setSize(int w, int h){
		this.w = w;
		this.h = h;
	}
	public String toString(){
		return "["+getClass()+"] w="+w+" h="+h;
	}
}
