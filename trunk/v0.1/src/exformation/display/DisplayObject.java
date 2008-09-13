package exformation.display;

import processing.core.PGraphics;
import exformation.core.Application;
import exformation.core.BaseClass;
import exformation.geom.Point;

public class DisplayObject extends BaseClass{

	//private static final long serialVersionUID = 0L;
	
	public Point position;
	protected boolean visible = true;
	protected boolean dirty;
	public PGraphics g;
	public DisplayObject parent;
	
	public DisplayObject(){
		g = Application.PGRAPHICS;
		position = new Point();
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
			if(visible){
				g.pushMatrix();
				g.translate(position.x,position.y);
				draw();
				dirty = false;
				g.popMatrix();
			}
		//}
	}

	public void visible(boolean val){
		if(visible != val){
			visible = val;
			debug("set visible "+val);
			dirty = true;
		}
	}
	
	public void draw(){
		
	}
	
	public void dispose(){
		g = null;
	}
}
