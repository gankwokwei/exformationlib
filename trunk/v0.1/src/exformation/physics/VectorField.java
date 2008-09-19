package exformation.physics;

import processing.core.PGraphics;
import exformation.display.DisplayObject;
import exformation.geom.Point;

public class VectorField extends DisplayObject {

	  int res, _width, _height, coln, rown, totaln, counterx;
	  Cell[] cell_arr;
	  Cell[][] cell_arr2d;
	  
	  public VectorField (int width, int height, int cellSize) {
		res = cellSize;
	    _width = width;
	    _height = height;
	    coln = _width/res;
	    rown = _height/res;
	    totaln = coln*rown;
	    cell_arr = new Cell[totaln];
	    cell_arr2d = new Cell[coln][rown];
	    counterx=0;
	   int n = 0;
	    for (int x =0; x<coln; x++) {
	      for(int y=0; y<rown; y++) {
	        Cell cell = new Cell(x*res, y*res, res, res, n);
	        cell_arr[n] = cell;
	        cell_arr2d[x][y] = cell;
	        n++;
	      }
	    }
	  }
	  
	  public void setMouse(boolean mousePressed, int mouseX,int mouseY){
		  for (int n=0; n<totaln; n++) {
		     cell_arr[n].calc(mousePressed,mouseX,mouseY);
		    // cell_arr[n].render();
		   } 
	  }
	  
	  public void calc() {
		   int n=0;
		   for (int x =0; x<coln; x++) {
		      for(int y=0; y<rown; y++) {
		        if (x>0 & x<coln-1 && y>0 && y<rown-1) {
		               float current = cell_arr2d[x][y].col;
		               cell_arr2d[x][y-1].springColor(current);//col += (current - cell_arr2d[x][y-1].col)*sp;
		               cell_arr2d[x-1][y-1].springColor(current);
		               cell_arr2d[x+1][y-1].springColor(current);//col += (current - cell_arr2d[x+1][y-1].col)*sp;
		               cell_arr2d[x-1][y+1].springColor(current);//col += (current - cell_arr2d[x-1][y+1].col)*sp;
		               cell_arr2d[x+1][y+1].springColor(current);//col += (current - cell_arr2d[x+1][y+1].col)*sp;
		               cell_arr2d[x+1][y].springColor(current);//col += (current - cell_arr2d[x+1][y].col)*sp;
		               cell_arr2d[x-1][y].springColor(current);//col += (current - cell_arr2d[x-1][y].col)*sp;
		               cell_arr2d[x][y+1].springColor(current);//col += (current - cell_arr2d[x][y+1].col)*sp;
		        }
		        n++;
		      }
		   }
	  }
	  
	  public void draw(){
		   for (int n=0; n<totaln; n++) {
		     //cell_arr[n].calc();
		     cell_arr[n].render(g);
		   }
	  }
}

class Cell {
	  Point position;
	  int id,_count;
	  float x,y,_width, _height, col, _vx, _vy, _vd, _k, _damp;
	  Cell(float _x,float _y, float w, float h, int n) {
	    id = n;
	    x = _x;
	    y = _y;
	    _k = 0.1f;
	    _damp = 0.7f;
	    _vx =_vy = _vd = 0;
	    position = new Point(x,y);
	    _width = w;
	    _height = h;
	    _count = 0;
	    col = 220;
	  }
	  void springColor (float target) {
	    col+=(target-col)*0.25f;
	  }
	  void tweenColor (float target) {
	    _vd+=(target-col)*.01f;
	    _vd*=.99f; 
	    col+=_vd;
	  }
	  void calc (boolean mousePressed,int mouseX, int mouseY) {
	   if (mousePressed) {
	      if (hitTest(mouseX,mouseY)) {
	        col = 1;
	      }
	    }
	    tweenColor(255);
	  }
	  
	  void render(PGraphics p) {
	    p.noStroke();
	   // p.fill(col);
	   // p.rect(x,y,_width,_height);
	    p.pushMatrix();
	    p.translate(x, y);
	    p.stroke(0);
	    p.rotate((float) (Math.PI/180*(col*2)));
	    p.line(0,0,_width,0);
	    p.popMatrix();
	  }
	  
	  boolean hitTest (float tx, float ty) {
	    float w = 50;
	    float dx = tx-x;
	    float dy = ty-y;
	    float len = (float)Math.sqrt(dx*dx+dy*dy);
	    if (Math.abs(len) < w)  {
	      return true;
	    }
	    return false;
	  };
	}