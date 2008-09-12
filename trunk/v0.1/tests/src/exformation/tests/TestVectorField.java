package exformation.tests;

import exformation.core.Application;
import exformation.physics.VectorField;

public class TestVectorField extends Application {

	private static final long serialVersionUID = 1L;

	private VectorField map;

	public int width  = 800;
	public int height = 600;

	public void main(){
		map = new VectorField(width,height,8);
	}
	
	public void draw(){
		g.background(255);
		map.render(g,mousePressed,mouseX,mouseY);
	}
}

