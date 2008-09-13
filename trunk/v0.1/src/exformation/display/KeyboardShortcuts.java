package exformation.display;

import java.awt.event.KeyEvent;
import java.util.Hashtable;

import exformation.core.Application;
import exformation.geom.Rectangle;
import exformation.utils.Delegate;

public class KeyboardShortcuts extends Sprite{

	private Hashtable<Character, ShortCutItem> list = new Hashtable<Character, ShortCutItem>();
	private Rectangle background;
	private ShortCutItem first;
	private int maxWidth;
	
	public KeyboardShortcuts(Application parent){
		visible(false);
		background = new Rectangle(0,0,300,200);
		first = addShortCut('?', "Display this panel", Delegate.create(this,"display"));
		parent.registerKeyEvent(this);
	}
	
	public void pre(){
		render();
	}
	
	public void keyEvent(KeyEvent evt){
		switch (evt.getID()) {
		    case KeyEvent.KEY_PRESSED:
		    	ShortCutItem item = list.get(evt.getKeyChar());
				if(item!=null){
					item.execute();
				}
		      break;
		    case KeyEvent.KEY_RELEASED:
		    	hide();
		      break;
		}
	}
	
	
	public void display(){
		visible(true);
	}
	
	public void hide(){
		visible(false);
	}
	
	public void draw(){
		g.fill(0);
		g.rect(background.x,background.y,background.width,background.height);
		g.noFill();
	}
	
	public ShortCutItem addShortCut(char key,String message, Delegate handler){
	    //if(key=='space')key = ' ';  
		//var w:int = 300;
		int h = 20;
		ShortCutItem item = new ShortCutItem(key,message,handler);
		maxWidth = Math.max(maxWidth,item.width()+20);
		item.position.y = (list.size()-1)*h;
		addChild(item);
		if(first!=null) first.setY(list.size()*h);
		list.put(key, item);
		background.setSize(maxWidth,(list.size()*h)+5);
		return item;
	}
	
	private class ShortCutItem extends DisplayObject{
		
		char key;
		String message;
		Delegate handler;
		
		public ShortCutItem(char key,String message, Delegate handler){
			this.key = key;
			this.message = message;
			this.handler = handler;
		}
		
		public int width(){
			return 200;
		}
		
		public void execute(){
			handler.execute();
		}
		
		public void draw(){
			g.fill(255,0,0);
			g.rect(0,0,200,20);
			g.noFill();
		}
	}
}
