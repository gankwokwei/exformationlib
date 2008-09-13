package exformation.display;

import java.util.Vector;

import exformation.geom.Dimension;
import exformation.utils.MathUtil;


public class Sprite extends DisplayObject{// implements Serializable{


	private static final long serialVersionUID = 0L;
	
	public Vector<DisplayObject> children;
	
	public Sprite(){
		children = new Vector<DisplayObject>();
	}
	
	public DisplayObject addChild(DisplayObject child){
		children.add(child);
		child.parent = this;
		return child;
	}
	
	public DisplayObject removeChild(DisplayObject child){
		if(children.contains(child)){
			children.remove(child);
			child.parent = null;
		}
		return child;
	}
	
	public int numChildren(){
		return children.size();
	}
	
	public Dimension dimension(){
		Dimension result = new Dimension();
		int len = numChildren();
		for(int n=0;n<len;n++){
			DisplayObject child = children.get(n);
			Dimension dim = child.dimension();
			dim.w+=child.position.x;
			dim.h+=child.position.y;
			result.max(dim);
		}
		return result;
	}
	
	public void render(){
		//if(dirty){
			//if(visible){
			//	draw();
			//}
			//dirty = false;
		//}
		calc();
		g.pushMatrix();
		g.translate(position.x, position.y);
		g.rotate((float)(MathUtil.C_PI_180*rotation.z));
		//g.rotateZ((float)(MathUtil.C_PI_180*rotation.z));
		//g.rotateX((float)(MathUtil.C_PI_180*rotation.x));
		//g.rotateY((float)(MathUtil.C_PI_180*rotation.y));
		if(visible){
			draw();
			int len = numChildren();
			for(int n=0;n<len;n++){
				children.get(n).render();
			}
		}

		g.popMatrix();
	}
	

}
