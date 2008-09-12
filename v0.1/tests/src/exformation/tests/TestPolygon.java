package exformation.tests;

import processing.core.PGraphics;
import exformation.core.Application;
import exformation.geom.Dimension;
import exformation.geom.Point;
import exformation.geom.Polygon;

public class TestPolygon extends Application {

	private static final long serialVersionUID = 1L;

	private int pulsesPerPoly	= 5;
	private Polygon[] polys 	= new Polygon[10];
	private Pulse[]pulses		= new Pulse[polys.length*pulsesPerPoly];
	
	public void main(){
		int len = polys.length;
		int w = stage.width;
		int h = stage.height;
		int index = 0;
		double m =0.001;
		for (int n=0; n<len; n++){
			
			Point[] points = new Point[10];
			for (int i=0;i<points.length;i++){
				points[i]=new Point(w/2,h/2);
			}
			
			Polygon bezier = new Polygon(points,true);
			polys[n]=bezier;
			randomize(bezier);
			
			
			//now create some pulses giving each of them a reference to the bezier to follow
			for (int i=0;i<pulsesPerPoly;i++){
				m+=0.005;
				Pulse pulse =  new Pulse(bezier,0.01);
				pulse.cursor=m;
				pulses[index] = pulse;
				index++;
			}
		}	
	}
	
	public void draw(){
		background(255);
		stroke(0);
		for (int n=0; n<polys.length; n++){
			Polygon poly = polys[n];
			poly.render(g);
		}
		for (int i=0;i<pulses.length;i++){
			Pulse pulse = pulses[i];
			pulse.update(g);
			//println(pulse.cursor);
		}
	}
	
	public void mousePressed(){
		for (Polygon poly:polys){
			randomize(poly);
		}
	}
	public void randomize(Polygon bezier){
		int len = bezier.length();
		//int x;
		int y,w,h;
		w = stage.width;
		h = stage.height;
		Dimension d = new Dimension(w,h);
		Point p	= new Point();
		Point point;
		float u = w/(len-1);
		for(int n=0; n<len;n++){
			//x = (int) (Math.random()*w);
			y = (int) (Math.random()*h);
			p.x = u*n;
			p.y = y;
			p.convertToRadialPosition(d,bezier.length(),360,0,d.min()-500);
			point = bezier.getPointAt(n);
			point.copy(p);
			//Tween.inOut(point,{x:p.x,y:p.y},0.3,n/10);
		}
	}
}

class Pulse{
	public double cursor=0;
	public double speed = 0.001;
	public Polygon poly;
	public Point position;
	
	public Pulse(Polygon p,double speed){
		this.poly = p;
		this.speed = speed;
		position = new Point();
	}
	public void update(PGraphics g){
		cursor+=speed;
		cursor%=1;
		Point p = poly.getPointByTime((float)cursor);
		
		g.pushMatrix();
		g.translate(p.x, p.y);
		g.rotate((float) (Math.PI/180*position.angleTo(p)));
		g.fill(0);
		g.rect(-1,-1,6,3);
		g.popMatrix();
		position.copy(p);
	}
}
