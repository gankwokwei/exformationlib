package exformation.tests;

import megamu.shapetween.Tween;
import processing.core.PApplet;
import exformation.core.Application;
import exformation.display.DisplayObject;
import exformation.geom.Dimension;
import exformation.geom.Point;
import exformation.geom.Polygon;
import exformation.geom.BezierCurve;
import exformation.utils.Delegate;
import exformation.utils.MathUtil;

public class TestBezier extends Application {

	private int NUM_POLYS           = 10;
	private int NUM_POINTS_PER_POLY = 20;
	private int NUM_PULSES_PER_POLY	= 10;
	
	private Polygon [] polys        = new Polygon[NUM_POLYS];
	private Pulse [] pulses         = new Pulse[NUM_POLYS*NUM_PULSES_PER_POLY];
	private boolean useBeziers      = true;
	
	//private PGraphics pg;
	
	public void main(){
		
		//pg = createGraphics(width, height, P3D);
		int len = polys.length;
		int w = stage.width;
		int h = stage.height;
		int index = 0;
		double m =0.001;
		for (int n=0; n<len; n++){
			
			TweenablePoint[] points = new TweenablePoint[NUM_POINTS_PER_POLY];
			for (int i=0;i<points.length;i++){
				points[i]=new TweenablePoint(this,w/2,h/2);
			}
			
			Polygon bezier = new BezierCurve(points,true);
			polys[n]=bezier;
			randomize(bezier);
			
			//now create some pulses giving each of them a reference to the bezier to follow
			for (int i=0;i<NUM_PULSES_PER_POLY;i++){
				m+=0.005;
				Pulse pulse =  new Pulse(bezier,0.005);
				pulse.cursor=m;
				pulses[index] = pulse;
				addChild(pulse);
				index++;
			}
		}
		addShortCut('1', "Render with Beziers",Delegate.create(this,"toggleBezier"));
		addShortCut('2', "Render with Polygons",Delegate.create(this,"togglePolygon"));
	}
	
	public void toggleBezier(){
		useBeziers = true;
		toggleDrawing();
	}
	
	public void togglePolygon(){
		useBeziers = false;
		toggleDrawing();
	}
	
	public void toggleDrawing(){
		int len = polys.length;
		int index = 0;
		for (int n=0; n<len; n++){
			Polygon poly = polys[n];
			if(useBeziers){
				poly = new BezierCurve(poly.points,poly.isClosed);
			}else{
				poly = new Polygon(poly.points,poly.isClosed);
			}
			polys[n]=poly;
			for (int i=0;i<NUM_PULSES_PER_POLY;i++){
				Pulse pulse =  pulses[index];
				pulse.poly = poly;
				index++;
			}
		}
	}
	
	public void draw(){
		g.background(255);
		g.stroke(100);
		//g.fill(100);
		//g.beginShape();
		for (int n=0; n<polys.length; n++){
			Polygon poly = polys[n];
			for(int j=0;j<poly.length();j++){
				TweenablePoint p = (TweenablePoint)poly.getPointAt(j);
				p.update();
			}
			poly.render(g);
		}
		root.render();
		shortcuts.render();
		//g.endShape();
		//mousePosition.draw(g);
	}
	
	public void mousePressed(){
		refresh();
	}
	
	public void onResize(){
		refresh();
	}
	
	public void refresh(){
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
		TweenablePoint point;
		float u = w/(len-1);
		for(int n=0; n<len;n++){
			//x = (int) (Math.random()*w);
			y = MathUtil.random(h);
			p.x = u*n;
			p.y = y;
			p.convertToRadialPosition(d,bezier.length(),360,0,d.min()-500);
			point = (TweenablePoint)bezier.getPointAt(n);
			//point.copy(p);
			point.move(p);
			//Tween.inOut(point,{x:p.x,y:p.y},0.3,n/10);
		}
	}
	
	class TweenablePoint extends Point{
		
		Tween tween;
		Point start;
		Point target;
		int time = MathUtil.random(20)+10;
		public TweenablePoint(PApplet p,float x, float y){
			this.x = x;
			this.y = y;
			start  = new Point();
			target = new Point();
			tween = new Tween(p,time, Tween.FRAMES, Tween.BEZIER, Tween.ONCE);
		}
		
		public void move(Point p){
			target.copy(p);
			start.copy(this);
			tween.start();
		}
		
		public void update(){
			copy(Point.lerp(start, target, tween.position()));
		}
	}

	class Pulse extends DisplayObject{
		public double cursor = 0;
		public double speed = 0.001;
		public Polygon poly;
		//public Point position;
		
		public Pulse(Polygon p,double speed){
			this.poly  = p;
			this.speed = speed;
			//position = new Point();
		}
		public void draw(){
			cursor+=speed;
			cursor%=1;
			Point p = poly.getPointByTime((float)cursor);
			rotation.z = position.angleTo(p);
			position.copy(p);
			g.fill(0);
			g.noStroke();
			g.rect(-1,-1,6,3);
			g.noFill();
			position.copy(p);
		}
	}
}


