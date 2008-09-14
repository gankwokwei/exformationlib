package exformation.tests;

import processing.core.PGraphics;
import exformation.core.Application;
import exformation.display.DisplayObject;
import exformation.geom.Point;
import exformation.utils.Delegate;
import exformation.utils.MathUtil;

public class TestPoint extends Application {

	private int len = 100;
	private Star[] list = new Star[len];
	PGraphics pg;
	
	public static int mode = 0;
	
	public void main(){
		pg = createGraphics(stage.width, stage.height, P3D);
		Application.PGRAPHICS = pg;
		clearBackgrondOnRender = false;
		for (int n=0;n<len;n++){
			Star p = new Star();
			p.position.random(stage);
			addChild(p);
			list[n]= p;
		}
		addShortCut('1', "toggle wrap / bounce / constrain", Delegate.create(this, "toggleMode"));
	}
	
	public void toggleMode(){
		TestPoint.mode ++;
		TestPoint.mode %=3;
	}
	
	public void draw(){
		pg.beginDraw();
		pg.background(255,10);
		pg.color(255,0,0);
		render();
		pg.endDraw();
		image(pg,0,0);
		shortcuts.render();
	}
	
	public void mousePressed(){
		for (Star s:list){
			s.randomizeDirection();
		}
	}
	
	private class Star extends DisplayObject{
		
		Point direction 	= new Point();
		Point lastPosition 	= new Point();
		
		float speed 	= MathUtil.random(3.0f)+1.0f;
		int color		= 0;
		public Star(){
			randomizeDirection();
		};
		
		public void randomizeDirection(){
			direction.x = MathUtil.random(4.0f)-2.0f;
			direction.y = MathUtil.random(4.0f)-2.0f;
		}
		
		public void calc(){
			position.sum(direction.x*speed,direction.y*speed);
			switch(mode){
				case 0: position.bounce(stage,direction);break;
				case 1: position.wrap(stage);break;
				case 2: position.constrain(stage);break;
			}
			rotation.z = position.angleTo(lastPosition);
			lastPosition.copy(position);
		};
		
		public void draw(){
			g.fill(color);
			g.rect(-1,-1,6,3);
		}
	}
}


