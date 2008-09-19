package exformation.display;

import processing.core.PGraphics;
import exformation.core.Application;
import exformation.core.BaseClass;
import exformation.geom.Dimension;
import exformation.geom.Point;
import exformation.geom.Rectangle;
import exformation.utils.MathUtil;

public class DisplayObject extends BaseClass{

	//private static final long serialVersionUID = 0L;
	
	public Dimension dimension;
	public Point position;
	public Point rotation;
	
	protected boolean visible = true;
	protected boolean dirty;
	public PGraphics g;
	public DisplayObject parent;
	public boolean isMouseDown;
	
	public DisplayObject(){
		g = Application.PGRAPHICS;
		dimension 		= new Dimension();
		position 		= new Point();
		rotation 		= new Point();
	}
	
	public void setPosition(int x,int y){
		position.x = x;
		position.y = y;
	}

	public void setPosition(Point p){
		position.copy(p);
	}
	
	public void centerOn(Point p){
		position.copy(p);
		Dimension d = dimension();
		position.sum((float)(d.w)*-0.5f,(float)(d.h)*-0.5f);
	}
	
	public Dimension dimension(){
		return dimension.clone();
	}
	
	public Rectangle rect(){
		return new Rectangle(position.x, position.y, dimension.w, dimension.h);
	}
	public void centerOn(Rectangle r){
		centerOn(r.center());
	}
	
	public int width(){
		return (int)dimension.w;
	}
	
	public int height(){
		return (int)dimension.h;
	}
	
	public void setSize(int w, int h){
		dimension.setSize(w,h);
	}
	public float getX(){
		return position.x;
	}
	
	public void setX(float val){
		position.x = val;
		dirty = true;
	}
	public float getY(){
		return position.y;
	}
	
	public void setY(float val){
		position.y = val;
		dirty = true;
	}
	
	public void render(){
		//if(dirty){
			calc();
			if(visible){
				g.pushMatrix();
				g.translate(position.x,position.y);
				g.rotate((float)(MathUtil.C_PI_180*rotation.z));
				//g.rotateZ((float)(MathUtil.C_PI_180*rotation.z));
				//g.rotateX((float)(MathUtil.C_PI_180*rotation.x));
				//g.rotateY((float)(MathUtil.C_PI_180*rotation.y));
				draw();
				dirty = false;
				g.popMatrix();
			}
		//}
	}

	public void calc(){};
	public void draw(){};
	
	public void display(){
		visible(true);
	}
	
	public void hide(){
		visible(false);
	}
	
	public boolean visible(){
		return visible;
	}
	
	public void visible(boolean val){
		if(visible != val){
			visible = val;
			dirty = true;
		}
	}
	
	public void dispose(){
		g = null;
		parent = null;
	}
}
