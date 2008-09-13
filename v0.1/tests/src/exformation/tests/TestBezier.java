package exformation.tests;

import megamu.shapetween.Tween;
import processing.core.PApplet;
import processing.core.PGraphics;
import exformation.core.Application;
import exformation.geom.Dimension;
import exformation.geom.Point;
import exformation.geom.Polygon;
import exformation.geom.BezierCurve;
import exformation.utils.Delegate;
import exformation.utils.MathUtil;

public class TestBezier extends Application {

	private static final long serialVersionUID = 1L;

	private int NUM_POLYS			= 10;
	private int NUM_POINTS_PER_POLY = 20;
	private int NUM_PULSES_PER_POLY	= 10;
	
	private Polygon [] polys 	= new Polygon[NUM_POLYS];
	private Pulse [] pulses		= new Pulse[NUM_POLYS*NUM_PULSES_PER_POLY];
	private boolean useBeziers = true;
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
				Pulse pulse =  new Pulse(bezier,0.01);
				pulse.cursor=m;
				pulses[index] = pulse;
				index++;
			}
		}
		addShortCut('1', "Render with Beziers",new Delegate(this,"toggleBezier"));
		addShortCut('2', "Render with Polygons",new Delegate(this,"togglePolygon"));
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
		//pg.beginDraw();
		//pg.background(255,10);
		background(255,10);
		stroke(0);
		g.noFill();
		for (int n=0; n<polys.length; n++){
			Polygon poly = polys[n];
			poly.render(g);
			for(int j=0;j<poly.length();j++){
				TweenablePoint p = (TweenablePoint)poly.getPointAt(j);
				p.update();
			}
		}
		g.fill(0);
		for (int i=0;i<pulses.length;i++){
			Pulse pulse = pulses[i];
			pulse.update(g);
		}
		root.render();
		//pg.endDraw();
		//image(pg,0,0);
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
		
		Tween tweenX;
		Tween tweenY;
		Point start;
		Point target;
		
		public TweenablePoint(PApplet p,float x, float y){
			this.x = x;
			this.y = y;
			start  = new Point();
			target = new Point();
			tweenX = new Tween(p,10, Tween.FRAMES, Tween.COSINE, Tween.ONCE);
			tweenY = new Tween(p,10, Tween.FRAMES, Tween.COSINE, Tween.ONCE);
		}
		
		public void move(Point p){
			target.copy(p);
			start.copy(this);
			tweenX.start();
			tweenY.start();
		}
		
		public void update(){
			x = lerp( start.x, target.x, tweenX.position() );
			y = lerp( start.y, target.y, tweenY.position() );
		}
	}
	
	class Pulse{
		public double cursor = 0;
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
}


