package exformation.core;

import java.awt.event.ComponentEvent;
//import java.awt.event.KeyListener;

import exformation.display.DisplayObject;
import exformation.display.KeyboardShortcuts;
import exformation.display.Sprite;
import exformation.geom.Rectangle;
import exformation.utils.Delegate;
import processing.core.PApplet;
import processing.core.PGraphics;
public class Application extends PApplet {
	
	static final long serialVersionUID = 1L;
 
	public static PGraphics PGRAPHICS;
	
    public Rectangle stage = new Rectangle(width,height);
    public KeyboardShortcuts shortcuts;
    public Sprite root;
    
	public void setup(){
		size(width,height,P3D);
		PGRAPHICS = g;
		root = new Sprite();
		shortcuts = new KeyboardShortcuts(this);
		shortcuts.position.setValue(100,100);
		addChild(shortcuts);
		//addComponentListener(this);
		print(width);
		main();
		//registerDraw(this);
		//addKeyListener(this);
	}

	public void draw(){
		//root.debug("render");
		root.render();
	}
	
	public void debug(String message){
		root.debug(message);
	}
	public void addShortCut(char key,String message, Delegate handler){
		shortcuts.addShortCut(key, message, handler);
	}

	public void addShortCut(char key,String message, String method){
		shortcuts.addShortCut(key, message, new Delegate(this,"method"));
	}
	
	public DisplayObject addChild(DisplayObject child){
		return root.addChild(child);
	}

	public DisplayObject removeChild(DisplayObject child){
		return root.removeChild(child);
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
