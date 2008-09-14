package exformation.core;

import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
//import java.awt.event.KeyListener;

import exformation.display.DisplayObject;
import exformation.display.KeyboardShortcuts;
import exformation.display.Sprite;
import exformation.geom.Point;
import exformation.geom.Rectangle;
import exformation.utils.Delegate;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PGraphics;
public class Application extends PApplet implements ComponentListener{
	
	static final long serialVersionUID = 1L;
 
	public static PGraphics PGRAPHICS;
	public static PFont DEFAULT_FONT;
	
	public int backgroundColor = color(255,255,255);
	
    public Rectangle stage = new Rectangle(width,height);
    public static Rectangle Stage;
    public KeyboardShortcuts shortcuts;
    public Sprite root;
    public Point mousePosition = new Point();
    public boolean isInit;
    public boolean clearBackgrondOnRender=true;
    
	public void setup(){
		Stage = stage;
		size(width,height);
		//frameRate(4);
		PGRAPHICS 		= g;
		DEFAULT_FONT 	= loadFont("../data/CourierNew-12.vlw"); 
		background(backgroundColor);
		root = new Sprite();
		shortcuts = new KeyboardShortcuts(this);
		registerPre(this);
		registerSize(this);
		addComponentListener(this);
		isInit = true;
		refresh();
		main();
	}
	
	public void pre(){
		mousePosition.setValue(mouseX, mouseY);
		calc();
	}
	
	public void draw(){
		render();
	}
	
	public void render(){
		if(clearBackgrondOnRender){
			background(backgroundColor);
		}
		root.render();
		shortcuts.render();
	}
	
	public void debug(Object message){
		root.debug(message==null?"null":message.toString());
	}
	
	public void addShortCut(char key,String message, Delegate handler){
		shortcuts.addShortCut(key, message, handler);
		refresh();
	}
	
	public DisplayObject addChild(DisplayObject child){
		return root.addChild(child);
	}

	public DisplayObject removeChild(DisplayObject child){
		return root.removeChild(child);
	}
	
	public void resize(Dimension d){
		resize(d.width,d.height);
	}
	
	private void refresh(){
		if(isInit){
			shortcuts.centerOn(stage);
		}
	}
	
	public void resize(int w, int h){ 
		stage.setSize(w,h);
		refresh();
		super.resize(w,h);
		if(isInit){
			onResize();
		}
	}

	public void main(){};
	public void calc(){};
	public void onResize(){};
	public void componentHidden(ComponentEvent evt) {}
	public void componentShown(ComponentEvent evt) {}
	public void componentMoved(ComponentEvent evt) {}

	public void componentResized(ComponentEvent e) {
		resize(e.getComponent().getSize());
	}
}
