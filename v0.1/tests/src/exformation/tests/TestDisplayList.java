package exformation.tests;

import exformation.core.Application;
import exformation.display.Sprite;
import exformation.geom.Point;
import exformation.utils.MathUtil;

public class TestDisplayList extends Application{

	public void main(){
		smooth();
		int len=5;
		for (int n=0;n<len;n++){
			Chain chain = new Chain();
			chain.rotation.z = 360/len*n;
			chain.setPosition(stage.center());
			addChild(chain);
		}
	}

	private class Box extends Sprite{
		
		public int color = MathUtil.random(100);
		public Point target = new Point();
		
		public void draw(){
			g.fill(color,30);
			g.rect(0, 0, 100, 100);
			g.noFill();
		}
	}
	
	private class Chain extends Sprite{
		Box a, b, c, d, e;
		
		public Chain() {
			a = new Box();
			b = new Box();
			d = new Box();		
			c = new Box();
			e = new Box();
			
			b.setPosition(100, 100);
			c.setPosition(100, 100);
			d.setPosition(100, 100);
			e.setPosition(100,100);
			e.rotation.z+=180;
			
			addChild(a);
			a.addChild(b);
			b.addChild(c);
			c.addChild(d);
			c.addChild(e);
		}
		
		public void calc(){
			//a.position.ease(mousePosition,0.2f);
			a.rotation.z+=0.5;
			b.rotation.z-=0.5;
			c.rotation.z+=1;
			d.rotation.z-=1;
			e.rotation.z-=1.5;
		}
	}
}
