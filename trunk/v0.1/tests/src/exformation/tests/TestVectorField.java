package exformation.tests;

import exformation.core.Application;
import exformation.physics.VectorField;

public class TestVectorField extends Application {

	private VectorField map;

	public int width  = 800;
	public int height = 600;

	public void main(){
		map = new VectorField(width,height,8);
		addChild(map);
		registerPre(this);
	}
	
	public void pre(){
		map.setMouse(mousePressed,mouseX,mouseY);
	}
}

