package exformation.geom;

import java.util.Vector;

import processing.core.PGraphics;
import exformation.utils.MathUtil;

	
public class BezierCurve extends Polygon{
	
	public boolean renderTips = true;
	/**
	 * @param list a list of Points
	 * @param isClosed is this a closed shape.
	 * */
	public BezierCurve (Point[] list, boolean isClosed){
		super(list,isClosed);
	}
	/**
	 * @param list a list of Points
	 * @param isClosed is this a closed shape.
	 * */
	public BezierCurve (Vector<Point> list, boolean isClosed){
		super(list,isClosed);
	}
	/**
	 * renders a curve on the provided graphics element
	 * @param g the graphic where to draw this curve on
	 * */
	public void render(PGraphics g){
		int len 	= length();
		if(len>1){
			if(isClosed)len+=1;
			int index   = isClosed ? lastIndex() : 0;
			Point a	= getPointAt(index);
			Point b = getPointAt(index+1);
			Point c = Point.getMidPoint(a,b);
			Point d	= c.clone();

			if(!isClosed && renderTips){
				//g.moveTo(a.x,a.y);
				//g.lineTo(c.x,c.y);
				g.line(a.x, a.y, c.x, c.y);
			}else{
				//g.moveTo(c.x, c.y);
				
			}
			g.beginShape();
			//g.fill(200,20);
			g.vertex(c.x, c.y);
			//Point b2 = new Point();
			for (int n = isClosed ? 1 : 2; n<len; n++) {
				a = getPointAt(n);
				c.calcMidPoint(a,b);
				//theres something wrong here...
				float amt = 0.3f;
				Point b1 = Point.lerp(b,c,amt);
				Point b2 = Point.lerp(b,d,amt);
				Point b3 = Point.getMidPoint(b1, b2);
				// if this is B it almost works
				//g.bezierVertex( b.x, b.y, b.x, b.y ,c.x, c.y);
				g.bezierVertex( b3.x, b3.y, b3.x, b3.y ,c.x, c.y);
				//g.curve( c.x, c.y, d.x, d.y, a.x, a.y, b.x, b.y);
				b = a;
				d = c.clone();
			}
			//debug(b2);
			if(!isClosed && renderTips){
				//g.lineTo(b.x,b.y);
				g.line(c.x, c.y, b.x, b.y);
			}
			g.endShape();
		}
	}
	/**
	 * @param time a value between 0 and 1 where 0 is the beginning of the curve and 1 the end
	 * */
	public Point getPointByTime(float time){
			time = MathUtil.constrainValue(time,0,1);
			int len	= length();
			float n = time*len;
			float t = n%1;
			return getPointForSegmentByTime((int)n,t);
	}
	public Polygon clone(){
		Point[] list = new Point[length()];
		int n = 0;
		for (Point p : points){
			list[n]=p.clone();
			n++;
		}
		return new BezierCurve(list,isClosed);
	}
	public Polygon toPoly(){
		Polygon p = new Polygon(points,isClosed);
		return p;
	}
	public static BezierCurve fromPoly(Polygon p){
		return new BezierCurve(p.points,p.isClosed);
	}
	/**
	 * @param time a value between 0 and 1 where 0 is the beginning of the curve and 1 the end
	 * */
	public Point getPointForSegmentByTime(int segmentIndex, float time){
		
		time = MathUtil.constrainValue(time,0,1);
		
		int n = segmentIndex;
		Point s1 = getPointAt(n-1);
		Point s2 = getPointAt(n);
		Point s3  = getPointAt(n+1);
		if(s1!=null && s2!=null && s3!=null){
			Point a	 = Point.getMidPoint(s1,s2);
			Point b	 = s2;
			Point c	 = Point.getMidPoint(s2,s3);
			
			if(!isClosed && (segmentIndex==0 || segmentIndex == lastIndex()) ){
				if(segmentIndex==0){
					return Point.getDiscretePosition(b,c,time);
				}else{
					return Point.getDiscretePosition(a,b,time);
				}
			}else{
				return Point.getDiscreteBezierPosition(a, b, c, time);
			}
		}
		return null;
	}

}