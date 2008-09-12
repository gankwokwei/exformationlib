package exformation.core;

import java.awt.event.ComponentEvent;


import exformation.geom.Rectangle;
import processing.core.PApplet;
public class Application extends PApplet{
	
	static final long serialVersionUID = 1L;
 
    public Rectangle stage = new Rectangle(width,height);
    
	public void setup(){
		size(width,height,P3D);
		//addComponentListener(this);
		print(width);
		main();
	}

	public void resize(int w, int h){ 
		stage.width = w;
		stage.height = h;
		println("onREsize");
		super.resize(w,h);
		onResize();
	}
	public void main(){}
	public void onResize(){};
	public void componentHidden(ComponentEvent evt) {}
	public void componentShown(ComponentEvent evt) {}
	public void componentMoved(ComponentEvent evt) {}
}
