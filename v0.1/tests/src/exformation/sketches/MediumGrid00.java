package exformation.sketches;


import processing.core.PApplet;

public class MediumGrid00 extends PApplet{
	
    private static final long serialVersionUID = 1L;
	int res = 4;
	Map map;
	
	public void setup() {
	  size(400,400);
	  //background(255);
	  map = new Map(res);
	}

	public void draw () {
	  map.render();
	}

	class Map {
	  int res, _width, _height, coln, rown, totaln, counterx;
	  Cell[] cell_arr;
	  Cell[][] cell_arr2d;
	  Map (int res) {
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
	  void render () {
	   int n;
	   for (n=0; n<totaln; n++) {
	     cell_arr[n].calc();
	    // cell_arr[n].render();
	   }
	   n=0;
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
	   for (n=0; n<totaln; n++) {
	     //cell_arr[n].calc();
	     cell_arr[n].render();
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
	    /*_vd+=(target-col)*_k;
	    _vd*=_damp; 
	    col+=_vd;
	   */
	    col+=(target-col)*0.25f;
	  }
	  void tweenColor (float target) {
	    //col+=(target-col)*0.005;
	    _vd+=(target-col)*.01f;
	    _vd*=.999f; 
	    col+=_vd;
	  }
	  void calc () {
	   if (mousePressed) {
	      if (hitTest(mouseX,mouseY)) {
	        col = 1;//cos((_count++)/10)*100;
	      }
	    }
	    tweenColor(255);
	    //col = constrain(col,1,220);
	  }
	  
	  void render() {
	    noStroke();
	    fill(col);
	    rect(x,y,_width,_height);
	  }
	  boolean hitTest (float tx, float ty) {
	    float w = 50;//_width;
	   // float h = 10;//_height;
	   // float minx = x-w;//_width;
	   // float maxx = x+w;//_width;
	   // float miny = y-h;//_height;
	   // float maxy = y+h;//_height;
	    
	    float dx = tx-x;
	    float dy = ty-y;
	    float len = sqrt(dx*dx+dy*dy);
	    if (abs(len) < w)  {
	      return true;
	    }
	     /*
	    if ( tx>minx && tx<maxx && ty>miny && ty<maxy) {
	      return true;
	    }*/
	    return false;
	  };
	}

	class Node {
	  Node() {
	  
	  }
	}

	class Point {
	  float x,y;
	  Point(float _x, float _y) {
	    x = _x;
	    y = _y;
	  }
	}
}
