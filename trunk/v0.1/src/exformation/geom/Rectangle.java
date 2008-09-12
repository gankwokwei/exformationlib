package exformation.geom;


public class Rectangle extends java.awt.Rectangle{
	
	private static final long serialVersionUID = 4116209729176250856L;

	public Rectangle(){
		width = 0;
		height = 0;
	}
	public Rectangle(int x, int y, int w,int h){
		this.x = x;
		this.y = y;
		width = w;
		height = h;
	}
	public Rectangle(int w,int h){
		width = w;
		height = h;
	}
	public void right(int val){
		width = val-x;
	}
	public void bottom(int val){
		height = val-y;
	}
	public float left(){
		return x;
	}
	public float right(){
		return x+width;
	}
	public float top(){
		return y;
	}
	public float bottom(){
		return y+height;
	}
	public boolean contains(Point p){
		return contains(new java.awt.Point((int)p.x,(int)p.y));
	}
}
