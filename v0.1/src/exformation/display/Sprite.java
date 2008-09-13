package exformation.display;

//import java.io.Serializable;
import java.util.Vector;


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
	
	public void render(){
		//if(dirty){
			//if(visible){
			//	draw();
			//}
			//dirty = false;
		//}
		if(visible){
			g.pushMatrix();
			g.translate(position.x, position.y);
			draw();
			int len = numChildren();
			for(int n=0;n<len;n++){
				children.get(n).render();
			}
			g.popMatrix();
		}
	}

}
