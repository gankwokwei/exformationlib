package exformation.tests;

import processing.core.PGraphics;
import exformation.core.Application;
import exformation.geom.Point;
import exformation.geom.Rectangle;

public class TestPoint extends Application {

	private static final long serialVersionUID = 1L;

	private int len = 10000;
	private Star[] list = new Star[len];

	public int width = 800;
	public int height = 600;
	PGraphics pg;

	public void main(){
		pg = createGraphics(width, height, P3D);
		print("start");
		//size(width,height,P3D);
		//stage = new Rectangle(width,height);
		for (int n=0;n<len;n++){
			Star p = new Star();
			p.position.x = random(width);
			p.position.y = random(height);
			list[n]= p;
		}
		background(255);
	}
	
	public void draw(){
		pg.beginDraw();
		pg.background(255,10);
		color(0);
		for (int n=0;n<len;n++){
			Star p = list[n];
			p.move(stage);
			p.render(pg);
		}
		pg.endDraw();
		image(pg,0,0);
	}
}

class Star{
	
	Point direction = new Point(1,1);
	Point position	= new Point();
	
	double speed 	= (Math.random()*10);
	
	public Star(){};
	public void move(Rectangle rect){
		//direction.x=Math.random()
		position.sum(direction.x+speed,direction.y+speed);
		//position.sum(direction);
		position.bounce(rect,direction);
	}
	public void render(PGraphics g){
		g.point(position.x,position.y);
	}
}
