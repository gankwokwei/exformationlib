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
		position.setValue(100, 100);
		//rotation.z = 45;
		background = new Rectangle(0,0,300,200);
		first = addShortCut('?', "Display this panel", Delegate.create(this,"display"));
		parent.registerKeyEvent(this);
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
	
	public void draw(){
		//rotation.z+=1;
		//rotation.z%=360;
		g.fill(0);
		g.rect(background.x,background.y,background.width,background.height);
		g.noFill();
	}
	
	public ShortCutItem addShortCut(char key,String message, Delegate handler){

		int h = 20;
		ShortCutItem item = new ShortCutItem(key,message,handler);
		addChild(item);
		maxWidth = Math.max(maxWidth,item.width()+20);
		item.position.y = (list.size()-1)*h;
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
			setSize(200,20);
		}
		
		public void execute(){
			handler.execute();
		}
		
		public void draw(){
			g.textFont(Application.DEFAULT_FONT);
			g.fill(255);
			g.text(key+": "+message,5,18);
			g.noFill();
		}
	}
}
