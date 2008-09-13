package exformation.geom;

import java.util.Vector;

import processing.core.PGraphics;

public class Polygon {
		
			public Vector<Point> points = new Vector<Point>();
			public boolean isClosed;
			
			public Polygon(Point[] list, boolean isClosed){
				this.isClosed = isClosed;
				int len = list.length;
				for(int n=0; n<len;n++){
					Point p = list[n];
					addPoint(p);
				}
			}

			public Polygon(Vector<Point> list, boolean isClosed){
				this.isClosed = isClosed;
				int len = list.size();
				for(int n=0; n<len;n++){
					Point p = list.get(n);
					addPoint(p);
				}
			}
			
			public Polygon(boolean isClosed){
				this.isClosed = isClosed;
			}
			
			public void addPoint(Point p){
				points.add(p);
			}
			
			public Rectangle bounds(){
				Rectangle rect = new Rectangle(Integer.MAX_VALUE,Integer.MIN_VALUE,0,0);
				for (Point p :points){
					rect.x 		= (int)Math.min( p.x,rect.x);
					rect.y 		= (int)Math.min( p.y,rect.y);
				}
				for (Point p :points){
					rect.right((int)Math.max( p.x,rect.right()));
					rect.bottom((int) Math.max( p.y,rect.bottom()));
				}		
					
				return rect;
			}
		
			public Polygon fromRect(Rectangle rect){
				Polygon poly = new Polygon(true);
				poly.addPoint(new Point(rect.x,				rect.y));
				poly.addPoint(new Point(rect.right(),		rect.y));
				poly.addPoint(new Point(rect.right(),		rect.bottom()));
				poly.addPoint(new Point(rect.x,				rect.bottom()));
				return poly;
			}
			
			public void offset(double x, double y){
				int len = length();
				for (int n=0;n<len;n++){
					getPointAt(n).sum(x,y);
				}	
			}
			
			public Point getPointAt(int n){
				int index = (n+length())%length();
				return points.get(index);
			}
			
			public Point lastPoint(){
				return getPointAt(lastIndex());
			}
			
			public int length(){
				return points.size();
			}
			
			public int lastIndex(){
				return points.size()-1;
			}
			
			public void render(PGraphics g){
				int len = length();
				if(len>0){
					Point p;
					int n;
					p = getPointAt(0);
					float x = p.x;
					float y = p.y;
					// dg.moveTo(p.x,p.y);
					for (n=1;n<len;n++){
						//Console.log(len);
						p = getPointAt(n);
						g.line(x,y,p.x,p.y);
						x = p.x;
						y = p.y;
					}
					if(isClosed){
						x = p.x;
						y = p.y;
						p = getPointAt(0);
						g.line(x,y,p.x,p.y);
					}
				}
			}
			
			public void scale(float factorX,float factorY){
				int len = length();
				for (int n=0;n<len;n++){
					getPointAt(n).scale(factorX,factorY);
				}	
			}
			
			public void rotate(Point axis,float angle){
				int len = length();
				for (int n=0;n<len;n++){
					getPointAt(n).rotate(axis,angle);;
				}
			}
			
			public void removeAll(){
				points.removeAllElements();
			}
			
			public Polygon clone(){
				Point[]list = new Point[length()];
				int n = 0;
				for (Point p : points){
					list[n]= p.clone();
					n++;
				}
				return new Polygon(list,isClosed);
			}
			
			public void copy(Polygon poly){
				removeAll();
				for (Point p : poly.points){
					addPoint(p.clone());
				}
			}
			/**
			 * @param time a value between 0 and 1 where 0 is the beggining of the curve and 1 the end
			 * */
			public Point getPointByTime(float time){
				time 	= Math.max(Math.min(time,1),0);
				int len = isClosed ? length() : length()-1;
				float n	= (float)Math.floor(time*len);
				float t = (time*len)%1;
				return getPointForSegmentByTime((int)n,t);
			}
			
			/**
			 * @param time a value between 0 and 1 where 0 is the beggining of the curve and 1 the end
			 * */
			public Point getPointForSegmentByTime(int segmentIndex, float time){
				
				time = Math.max(Math.min(time,1),0);
				int n   = segmentIndex;
				Point a  = getPointAt(n);
				Point b = getPointAt(n+1);
				if(a!=null && b!=null){
					return Point.getDiscretePosition(a, b, time);
				}
				return null;
			}
			/**
			 * @returns the total distance covered between the first point and the last one
			 * */
			public float lineLength(){
		       	float result = 0;
		       	Point a = getPointAt(0);
		       	int len = length();
		     	for(int n=1;n<len;n++){
		     		Point b = getPointAt(n);
		     		result+=a.distance(b);
		     		a = b;
		     	}
		     	if(isClosed){
		     		result+=a.distance(getPointAt(0));
		     	}
		     	return result;
		   	}
		   	
			public void distort(Quadrangle quad){
				/*
				for each (var point:Point in points){
					quad.distortPoint(point);
				}
				*/
				
				Point p1, p2, p3, p4;//,p2:Point,p3:Point,p4:Point;
				p1 = quad.p1;
				p2 = quad.p2;
				p3 = quad.p3;
				p4 = quad.p4;
				
				Rectangle b = bounds();
				float w 	= b.width;
				float h 	= b.height;
				int len		= length();
				float dx30 = p4.x - p1.x;
				float dy30 = p4.y - p1.y;
				float dx21 = p3.x - p2.x;
				float dy21 = p3.y - p2.y;
				
				for(int n = 0; n<len; n++){
					Point point = getPointAt(n);
					float gx = ( point.x - b.x ) / w;
					float gy = ( point.y - b.y ) / h;
					float bx = p1.x + gy * ( dx30 );
					float by = p1.y + gy * ( dy30 );
					point.x = bx + gx * ( ( p2.x + gy * ( dx21 ) ) - bx );
					point.y = by + gx * ( ( p2.y + gy * ( dy21 ) ) - by );
				}
				
				
			}

	}

